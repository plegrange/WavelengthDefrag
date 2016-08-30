package Test;

import java.util.*;


public class Node {

    int _ID;
    double _Intensity;
    double _Receive;
    int _Sent;
    int _Blocked;
    int _Success;

    ArrayList<Signal> _Current = new ArrayList<Signal>();
    ArrayList<Signal> _Arriving = new ArrayList<Signal>();
    Queue<Ack> _AckArriving = new LinkedList<Ack>();
    Queue<Ack> _AckSend = new LinkedList<Ack>();
    ArrayList<Signal> _AckTraversing = new ArrayList<Signal>();
    HashMap<Node, ArrayList<Path>> _Candidates;
    ArrayList<Wave> _Waves = new ArrayList<Wave>();


    public Node(int ID, double Intensity, double Receive) {
        this._ID = ID;
        this._Intensity = Intensity;
        this._Receive = Receive;
        _Success = 0;
        _Sent = 0;
        _Blocked = 0;
        _Waves.clear();

    }

    public List<Signal> getCurrent() {
        return _Current;
    }

    public int getID() {
        return _ID;
    }

    public void SignalArriveToCurrent() {
        _Current.clear();
        for (Signal signal : _Arriving) {
            _Current.add(signal);
        }
        _Arriving.clear();
    }

    public void AckArriveToCurrent() {
        while (!_AckArriving.isEmpty()) {
            _AckSend.add(_AckArriving.remove());
        }
    }


    public void ClearNode() {
        _Waves.clear();
        _Current.clear();
        _Arriving.clear();
        _AckArriving.clear();
        _AckSend.clear();
        _AckTraversing.clear();
        _Success = 0;
        _Sent = 0;
        _Blocked = 0;

    }

    public void RemoveMinWave() {


        double obj = 0.0;
        Double min = Double.MAX_VALUE;
        Wave wav = new Wave();

        for (Wave w : _Waves) {

            if (w.getPheromone() < min) {
                min = w.getPheromone();
                obj = w.getWavelength();
            }

        }


        for (Wave w : _Waves) {

            if (w.getWavelength() == obj || Math.abs(w.getWavelength() - obj) < 0.001) {
                wav = w;
            }


        }

        _Waves.remove(wav);


    }

    public void RemoveMinWave(int Destination) {


        double obj = 0.0;
        Double min = Double.MAX_VALUE;
        Wave wav = new Wave();

        for (Wave w : _Waves) {

            if (w.getDestination() == Destination) {

                if (w.getPheromone() < min) {
                    min = w.getPheromone();
                    obj = w.getWavelength();
                }

            }


        }


        for (Wave w : _Waves) {

            if (w.getWavelength() == obj || Math.abs(w.getWavelength() - obj) < 0.001) {
                wav = w;
            }


        }

        _Waves.remove(wav);


    }


    public boolean containsWave(double d) {

        for (Wave w : _Waves) {

            if (w.getWavelength() == d || Math.abs(w.getWavelength() - d) < 0.001) {
                return true;
            }

        }
        return false;
    }

    public Wave getWave(double d) {

        for (Wave w : _Waves) {

            if (w.getWavelength() == d || Math.abs(w.getWavelength() - d) < 0.001)
                return w;

        }

        return new Wave();


    }


    public void removeBlock(double d) {

        Wave wav = new Wave();

        for (Wave w : _Waves) {

            if (w.getWavelength() == d || Math.abs(w.getWavelength() - d) < 0.001) {
                wav = w;
                break;
            }


        }

        _Waves.remove(wav);

    }


}
