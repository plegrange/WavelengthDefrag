package Defragmentation;

import ExcelWriter.LinkTableOutput;
import Test.Node;
import Test.Signal;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FuBaR on 8/28/2016.
 */
public class Defrag {
    LightpathManager lightpathManager;
    LinkTableManager linkTableManager;
    LinkTableOutput linkTableOutput = new LinkTableOutput("initial.xls"), outputfinal = new LinkTableOutput("final.xls");
    GeneticAlgorithm geneticAlgorithm;
    HeuristicAlgorithm heuristicAlgorithm;
    LinkTable newLinkTable;

    public Defrag(List<Node> nodes) {
        lightpathManager = new LightpathManager(nodes);
        List<Lightpath> lightpaths = lightpathManager.build();
        linkTableManager = new LinkTableManager(lightpaths);
        LinkTable linkTableInitial = linkTableManager.buildInitial();

       // heuristicAlgorithm = new HeuristicAlgorithm();
        //newLinkTable = heuristicAlgorithm.repack(linkTableInitial);
        geneticAlgorithm = new GeneticAlgorithm(lightpathManager);
        newLinkTable = geneticAlgorithm.run(linkTableInitial);
    }

    public List<Signal> getNewSignals() {
        List<Signal> signals = new ArrayList<>();
        for (Lightpath lightpath : newLinkTable.lightPaths) {
            signals.add(lightpath.signal);
        }
        return signals;
    }

    public List<Signal> getOldSignals() {
        List<Signal> signals = new ArrayList<>();
        for (Lightpath lightpath : newLinkTable.lightPaths) {
            signals.add(lightpath.getNewSignal());
        }
        return signals;
    }
}
