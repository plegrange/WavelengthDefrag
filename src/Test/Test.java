package Test;

import Defragmentation.Defrag;
import ExcelWriter.SuccessOutput;
import ExcelWriter.WriteExcel;
import jxl.write.WriteException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Test {

    ArrayList<Node> nodes;
    ArrayList<Link> links;
    ACO ac;
    RoutingTable table;
    int nodeCount = 0;
    Node dragNode;
    ArrayList<Node> exclude;
    boolean pathFinding = false;
    int totalSignals = 600000;
    Random randomGenerator = new Random();
    Node genSource;
    Node genDest;
    int NrSignalsPerTime;
    double createWaveProb;
    int noWaves;
    WriteExcel writer1 = new WriteExcel("initialPlot.xls");
    WriteExcel writer2 = new WriteExcel("finalPlot.xls");
    WriteExcel writer3 = new WriteExcel("afterplot.xls");
    SuccessOutput defraggedSuccessOutput = new SuccessOutput("successes.xls");
    SuccessOutput normalSuccessOutput = new SuccessOutput("normalSuccesses.xls");

    public Test(ArrayList<Node> nodes, ArrayList<Link> links, RoutingTable table, ACO ac, int s1, int s2, boolean useDefrag) {
        this.ac = ac;
        this.nodes = nodes;
        this.links = links;
        this.table = table;
        noWaves = (s1);
        NrSignalsPerTime = (s2);
        exclude = new ArrayList<Node>();
        this.useDefrag = useDefrag;
        long startTime = System.currentTimeMillis();
        try {
            RunTest();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("Time taken : " + (endTime - startTime) + " milliseconds");

        System.exit(0);
    }

    public void GenerateTraffic(long num) {

        Random r = new Random();
        for (Node node : nodes) {
            for (int i = 0; i < num; i++) {
                int source = nodes.indexOf(node);
                int dest = randomGenerator.nextInt(nodes.size());
                while (dest == source) {
                    dest = randomGenerator.nextInt(nodes.size());
                }
                genSource = node;
                genDest = nodes.get(dest);
                Signal signal = new Signal(table, genSource, genDest, genSource._Intensity, 0);
                genSource._Current.add(signal);
            }
            ac.totalGen += num;
        }
    }

    Defrag defrag;
    boolean useDefrag;

    public void RunTest() throws IOException, WriteException {
        table.LoadNetwork();
        ac.BuildCandidates();
        ac.Reset();
        ac.noWaves = noWaves;
        ac.init();
        ac.WPD = Math.round(noWaves / nodes.size());
        boolean defragged = false, defragCheck = false;
        int counter = 0;
        List<Double> timeStepSuccesses = new ArrayList<>();
        while ((ac.total < totalSignals) || ac.Traffic() || ac.Acks()) {
            /*if(ac.total == totalSignals*0.5){
                try {
                    writer.write(links);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }*/


            System.out.println(ac.time);
            //counter++;
            if (ac.totalGen < totalSignals) {
                if ((totalSignals - ac.totalGen) < NrSignalsPerTime) {
                    GenerateTraffic(totalSignals - ac.totalGen);
                } else {
                    GenerateTraffic(NrSignalsPerTime);
                }
            }
            double success = ac.TimeStep();
            //if (ac.time % 5 == 0)
            timeStepSuccesses.add(success / NrSignalsPerTime);
            if (ac.time == 600) NrSignalsPerTime = NrSignalsPerTime * 2;
            if (ac.time == 600) {
                try {
                    writer1.write(links);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
                if (useDefrag) {
                    defrag = new Defrag(links);
                    rebuildNetwork();
                }
                //defragged = true;
                System.out.println("Defragged");

                //ac.BuildCandidates();
                try {
                    writer2.write(links);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
                defragged = true;
                //NrSignalsPerTime = NrSignalsPerTime + 5;
            }
            if (ac.time == 602 && useDefrag) {

                try {
                    writer3.write(links);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
            if (defragged) {
                //NrSignalsPerTime++;
            }
            // if (!defragged) NrSignalsPerTime++;
        }
        System.out.println(ac.success + " Successes");
        System.out.println(ac.blocked + " Blocks");
        System.out.println(ac.nlFailed + " Failed");
        System.out.println(ac.sourceblocked + " Source Blocks");
        System.out.println("Done");
        //ac.display();
        //ac.Duplicate();
        try {
            if (!useDefrag) normalSuccessOutput.write(timeStepSuccesses);
            else
                defraggedSuccessOutput.write(timeStepSuccesses);
        } catch (WriteException e) {
            e.printStackTrace();
        }
        ac.displaySuccess();
    }

    private void rebuildNetwork() {
        List<Signal> oldSignals = defrag.getOldSignals();
        List<Signal> newSignals = defrag.getNewSignals();
        for (Node node : nodes) {
            for (int i = 0; i < oldSignals.size(); i++) {
                node.rebuild(oldSignals.get(i), newSignals.get(i));
            }
        }
    }
}