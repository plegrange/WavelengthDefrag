package Test;

import java.util.ArrayList;
import java.util.Random;


public class ACO {

    int time;
    int WaveCap = 16;
    int noWaves = 50;
    double evap = 0.01;
    double globalDecay = 0.75;
    double localDecay = 0.75;
    double localControl = 0.001;
    double shortControl = 2.0;
    int startBlock = 0;
    double A = 4.78 / 25;

    long hops;
    long blocked;
    long nlFailed;
    long success;
    long failure;
    long total;
    long totalGen;
    RoutingTable table;

    ArrayList<Node> nodes;
    ArrayList<Link> links;

    int wavelengthMax = 1800;
    int wavelengthMin = 1200;

    int datarate = 1024;

    double Prob = 0.4;//0.39
    double Greedy = 0.59;//0.6
    double Cervello = 0.8;

    boolean Ant = false;
    boolean Greed = true;

    boolean applyAttenuation = true;
    boolean applyNL = true;
    boolean randomSelection = false;
    boolean resend = false;

    long sourceblocked = 0;
    int WPD = 0;

    ACO(ArrayList<Node> nodes, ArrayList<Link> links, RoutingTable table) {
        hops = 0;
        time = 0;
        blocked = 0;
        nlFailed = 0;
        total = 0;
        success = 0;
        failure = 0;
        totalGen = 0;
        this.nodes = nodes;
        this.links = links;
        this.table = table;
    }

    public ArrayList<Node> getNodes() {
        return nodes;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public void Reset() {
        time = 0;
        totalGen = 0;
        blocked = 0;
        nlFailed = 0;
        total = 0;
        success = 0;
        failure = 0;
        hops = 0;
        for (Node node : nodes) {
            node.ClearNode();
        }
        for (Link link : links) {
            link._Reservations.clear();
        }
        table._Reservations.clear();
    }

    public float TimeStep() {
        float successPerTimeStep = success;
        ProcessAcknowledgements();
        successPerTimeStep = success - successPerTimeStep;
        NetworkSend();
        time++;
        return successPerTimeStep;
    }

    public void display() {

        for (Wave w : nodes.get(0)._Waves) {

            System.out.print(w.getWavelength() + "#" + w.getDestination() + "#");
            for (Node n : w.getPath()._Path) {
                System.out.print(n._ID);
            }
            System.out.println("#" + w.getSuccess() + "#" + w.getFailure() + "#" + w.getPheromone());
            System.out.println("_______________");

        }

        System.out.println("***************");

    }

    public void displaySuccess() {

        double s = 0;
        double f = 0;
        double p = 0.0;
        double c = 0;


        for (Node n : nodes) {

            for (Wave w : nodes.get(0)._Waves) {

                if (n._ID != nodes.get(0)._ID) {

                    if (w.getDestination() == n._ID) {

                        s += w.getSuccess();
                        f += w.getFailure();
                        c += 1.0;
                    }

                }
            }


            if (n._ID != nodes.get(0)._ID) {
                p = (s / s + f);

                System.out.println("Destination : " + n._ID + " Ratio : " + (p / c));
                System.out.println("_______________");

                p = 0;
                s = 0;
                f = 0;
                c = 0;
            }


        }


        System.out.println("***************");

    }

    private boolean TraverseTest(Node source, Node destination, Signal signal) {
        CalculateLoss(source, table.GetLink(source._ID, destination._ID), signal);
        if (signal._Intensity - signal._Penalty < destination._Receive) {
            return false;
        }
        return true;
    }

    private void CalculateLoss(Node source, Link link, Signal signal) {
        if (applyAttenuation) {
            signal._Penalty = link._Length * link._Loss;
        }
        if (applyNL) {
            for (Signal otherSig : source._Current) {
                if (otherSig != signal && signal._Next == otherSig._Next) {
                    signal._Penalty += A * ((link._Length * datarate * (Math.pow(10, (otherSig._Intensity / 10)))) / (Math.pow(10, (signal._Intensity / 10)) * datarate * Math.abs(otherSig._Wavelength - signal._Wavelength)));
                }
            }
        }
    }

    private void ApplyPenalties(Node node) {
        for (Signal signal : node._Current) {
            signal._Intensity -= signal._Penalty;
            signal._Penalty = 0.0;
        }
    }

    public void BuildCandidates() {
        CandidateBuilder cb = new CandidateBuilder(table, nodes, links);
        for (Node node : nodes) {
            cb.BuildCandidates(node);
        }
    }

    public void ProcessAcknowledgements() {
        Ack tempAck;
        Node ackDest;
        for (Node node : nodes) {
            while (!node._AckSend.isEmpty()) {
                tempAck = node._AckSend.remove();
                if (tempAck instanceof TraAck) {
                    ackDest = tempAck._Signal._Visited.peek();
                    ackDest._AckTraversing.remove(tempAck._Signal);
                    //LocalUpdate(tempAck._Signal, ackDest);
                }
                if (tempAck instanceof ReAck) {
                    ackDest = ((ReAck) tempAck).NextNode();
                    table.ReleaseLink(tempAck._Signal, node._ID, ackDest._ID);
                    if (ackDest != tempAck._Signal._Source) {
                        ackDest._AckArriving.add(tempAck);
                    } else {
                        if (((ReAck) tempAck)._Success) {
                            tempAck._Signal._Source._Success++;
                            success++;
                        } else {
                            if (resend) {
                                Signal temp = tempAck._Signal;
                                temp._Intensity = temp._Source._Intensity;
                                ackDest._Arriving.add(temp);
                            }
                            failure++;
                        }
                    }
                    GlobalUpdate((ReAck) tempAck, ackDest, node);
                }
            }
        }
        for (Node node : nodes) {
            for (Signal signal : node._AckTraversing) {
                table.ReleaseLink(signal, node._ID, signal._Next._ID);
                //No release Ack if it fails straight from the start
                ReAck reAck = new ReAck(table, node, signal, false);
                if (node != signal._Source) {
                    node._AckSend.add(reAck);
                } else {
                    if (resend) {
                        signal._Intensity = node._Intensity;
                        node._Arriving.add(signal);
                    }
                    failure++;
                }
                GlobalUpdate(reAck, node, signal._Next);
            }
            node._AckTraversing.clear();
            node.AckArriveToCurrent();
        }
    }

    ArrayList<Signal> removeSignals = new ArrayList<Signal>();

    public void NetworkSend() {
        Node source;
        for (Node node : nodes) {
            for (Signal signal : node._Current) {
                source = node;
                AntDecision(node, signal);
                if (signal._Next != null) {
                    if (!table.ReserveLink(signal, source._ID, signal._Next._ID)) {
                        if (source != signal._Source) {
                            ReAck reAck = new ReAck(table, source, signal, false);
                            source._AckSend.add(reAck);
                        } else {
                            failure++;
                            sourceblocked++;
                        }
                        blocked++;
                        signal._Source._Blocked++;
                        removeSignals.add(signal);
                    }
                } else {
                    if (source != signal._Source) {
                        ReAck reAck = new ReAck(table, source, signal, false);
                        source._AckSend.add(reAck);
                    } else {
                        sourceblocked++;
                    }
                    blocked++;
                    signal._Source._Blocked++;
                    removeSignals.add(signal);
                }
            }
            for (Signal signal : removeSignals) {
                node._Current.remove(signal);
            }
            for (Signal signal : node._Current) {
                Send(node, signal._Next, signal);
            }
            ApplyPenalties(node);
        }
        for (Node node : nodes) {
            node.SignalArriveToCurrent();
        }
    }

    public boolean Send(Node source, Node destination, Signal signal) {
        hops++;
        source._AckTraversing.add(signal);
        if (TraverseTest(source, destination, signal)) {
            if (signal._Destination == destination) {
                destination._AckSend.add(new TraAck(signal));
                destination._AckSend.add(new ReAck(table, signal, true));
            } else {
                destination._AckSend.add(new TraAck(signal));
                destination._Arriving.add(signal);
            }
            signal._Visited.push(source);
            return true;
        } else {
            nlFailed++;
            return false;
        }
    }

    private double calcWave() {
        //Calculate wavelength within a range
        double wavelength = (Math.random() * (wavelengthMax - wavelengthMin)) + wavelengthMin;
        return Double.parseDouble(String.valueOf(wavelength).substring(0, 8));
        //return wavelength;

    }


    Path globalPath;

    private void GlobalUpdate(ReAck ack, Node node, Node output) {
        double wavelength;
        double difference;
        double success = 1.0;
        double deposit;
        Node input;
        wavelength = ack._Signal._Wavelength;
        if (node != ack._Signal._Source) {
            input = ack._Signal._Visited.peek();
        } else {
            input = ack._Signal._Source;
        }
        if (!ack._Success) {
            success = -1.0;
        }


        if (node != ack._Source) {
            difference = ack.GetTraversedLength() - ack._Source._Candidates.get(node).get(0)._Length;
        } else {
            difference = 0;
        }

        deposit = Math.exp(-1.0 * globalDecay * difference);


        if (node != ack._Source) {
            if (node.containsWave(wavelength)) {
                if (!ack._Success)
                    node.getWave(wavelength).Failure();
                else
                    node.getWave(wavelength).Success();


                //double ph = node.getWave(wavelength).getPheromone();
                Wave w = node.getWave(wavelength);
                double ph = 1.0;

				/*
                if(w.getFailure() != 0)
					ph = w.getSuccess()/(w.getSuccess()+(w.getFailure()*w.getFailure()));
				else
					ph = 1.0;	
				*/

                ph = (1.0 + w.getSuccess()) / (1.0 + (w.getFailure() + w.getSuccess()));


                //double pheromone = (1.0 - evap)*ph+evap*success*deposit;
                double pheromone = ph;

                node.getWave(wavelength).Update(pheromone);

            }
        }


    }


    Random randomGenerator = new Random();

    private void Continue(Node node, Signal signal) {

        int hop = 0;

        for (int i = 0; i < signal._Route._Path.size(); i++) {
            if (node._ID == signal._Route._Path.get(i)._ID) {
                hop = i;
            }
        }

        Node n = signal._Route._Path.get(hop + 1);
        signal._Next = n;

    }

    public boolean WaveRoute(Node n, double d, int i) {

        for (Wave w : n._Waves) {

            if ((w.getWavelength() == d || Math.abs(w.getWavelength() - d) < 0.001) && (w.getPath()._Path.getLast()._ID == i))
                return true;
        }

        return false;
    }

    public boolean Traffic() {
        for (Node node : nodes) {
            if (node._Current.size() > 0 || node._AckSend.size() > 0) {
                return true;
            }
        }
        return false;
    }

    public boolean Acks() {
        for (Node node : nodes) {
            if (node._AckTraversing.size() > 0 || node._AckSend.size() > 0 || node._AckArriving.size() > 0) {
                return true;
            }
        }
        return false;
    }


    private void ChooseShortest(Node node, Signal signal) {
        ArrayList<Path> paths = node._Candidates.get(signal._Destination);
        Path path;
        Node output = null;

        if (paths != null) {
            path = paths.get(0);
            output = path._Path.get(1);
            signal._Route = path;
            signal._Next = output;
        }
    }


    private void CreateWavelength(Node node, Signal signal) {

        Path route = node._Candidates.get(signal._Destination).get(0);
        int ipath = 0;
        Random ran = new Random();
        double wavelength = calcWave();

        //randomly select 1 of 3 shortest paths
        ipath = ran.nextInt(node._Candidates.get(signal._Destination).size());
        route = node._Candidates.get(signal._Destination).get(ipath);

        double d = Math.random();

        //remove excess
        if (node._Waves.size() >= noWaves) {
            if (d < 0.95) {
                node.RemoveMinWave(signal._Destination._ID);
            } else {
                node.RemoveMinWave();
            }
        }

        signal._Route = route;
        Node next = signal._Route._Path.get(1);
        signal._Next = next;
        signal._Wavelength = wavelength;

        Wave w = new Wave(route, wavelength, 1.0);
        node._Waves.add(w);

    }

    private void Routing(Node node, Signal signal) {
        double max = 0;
        double tempCalc = 0;
        double des;
        Link link;

        if (node._Waves != null) {

            for (Wave w : node._Waves) {

                Path r = w.getPath();
                //check if entry has matching destination
                if (r._Path.getLast()._ID == signal._Destination._ID) {
                    //link must be for next node!!!!!
                    link = table.GetLink(node._ID, r._Path.get(1)._ID);
                    des = Math.pow(r._Desirability, shortControl);

                    double wavelength = w.getWavelength();
                    tempCalc = w.getPheromone() * des;
                    boolean a = tempCalc > max;
                    boolean b = link.WavelengthAvailability(wavelength);
                    boolean c = WaveRoute(node, wavelength, signal._Destination._ID);
                    if (a && b && c) {
                        max = tempCalc;
                        signal._Next = r._Path.get(1);
                        signal._Wavelength = wavelength;
                        signal._Route = r;
                    }

                }
            }
        }
    }


    public void Probability(Node node, Signal signal) {

        double sum = 0;
        double tempCalc;
        double des;
        double decision = Math.random();
        Link link;

        ArrayList<Double> pathProbSummations = new ArrayList<Double>();
        ArrayList<Wave> pathProb = new ArrayList<Wave>();

        if (node._Waves != null) {
            for (Wave wave : node._Waves) {
                if (wave.getDestination() == signal._Destination._ID) {
                    link = table.GetLink(node._ID, wave.getPath()._Path.get(1)._ID);

                    if (link.WavelengthAvailability(wave.getWavelength())) {
                        des = Math.pow(wave.getPath()._Desirability, shortControl);
                        tempCalc = wave.getPheromone() * des;
                        sum += tempCalc;
                        pathProbSummations.add(sum);
                        pathProb.add(wave);
                    }
                }

            }

            for (int i = 0; i < pathProbSummations.size(); i++) {
                double a = pathProbSummations.get(i) / sum;
                if (decision < a) {
                    signal._Next = pathProb.get(i).getPath()._Path.get(1);
                    signal._Wavelength = pathProb.get(i).getWavelength();
                    signal._Route = pathProb.get(i).getPath();
                    break;
                }
            }

        }
    }


    public void init() {
        for (Node n : nodes) {

            for (Node m : nodes) {

                if (n._ID != m._ID) {

                    for (int i = 0; i < 50; i++) {


                        Path route = n._Candidates.get(m).get(0);
                        int ipath = 0;
                        Random ran = new Random();
                        double wavelength = calcWave();

                        //randomly select 1 of 3 shortest paths
                        ipath = ran.nextInt(n._Candidates.get(m).size());
                        route = n._Candidates.get(m).get(ipath);

                        Wave w = new Wave(route, wavelength, 1.0);
                        n._Waves.add(w);


                    }
                }


            }


        }

    }

    private Node AntDecision(Node node, Signal signal) //Algorithm Functionality
    {
        signal._Next = null;
        if (node == signal._Source) {
            node._Sent++;
            total++;
            if (Ant) {

                Double r = Math.random();
                if (r < Greedy)
                    Routing(node, signal);
                else if (r < (Greedy + Prob))
                    Probability(node, signal);

                if (signal._Next == null) {
                    CreateWavelength(node, signal);
                }


            }
            if (Greed) {
                ChooseShortest(node, signal);
                double wavelength = calcWave();
                signal._Wavelength = wavelength;
            }


        } else {
            if (Ant) {
                Continue(node, signal);
            }
            if (Greed) {
                ChooseShortest(node, signal);
            }

        }
        return signal._Next;
    }


}
