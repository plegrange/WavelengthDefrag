package Defragmentation;

import ExcelWriter.LinkTableOutput;
import Test.Link;
import Test.Signal;
import jxl.write.WriteException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by s213391244 on 8/28/2016.
 */
public class Defrag {
    LightpathManager lightpathManager;
    LinkTableManager linkTableManager;
    LinkTableOutput linkTableOutput = new LinkTableOutput("initial.xls"), outputfinal = new LinkTableOutput("final.xls");
    GeneticAlgorithm geneticAlgorithm;
    HeuristicAlgorithm heuristicAlgorithm;
    LinkTable newLinkTable;

    public Defrag(ArrayList<Link> links) {
        lightpathManager = new LightpathManager(links);
        List<Lightpath> lightpaths = lightpathManager.build();
        linkTableManager = new LinkTableManager(lightpaths);
        LinkTable linkTableInitial = linkTableManager.buildInitial();
        try {
            linkTableOutput.write(linkTableInitial);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        //heuristicAlgorithm = new HeuristicAlgorithm();
        //newLinkTable = heuristicAlgorithm.repack(linkTableInitial);

        geneticAlgorithm = new GeneticAlgorithm(lightpathManager);
        newLinkTable = geneticAlgorithm.run(linkTableInitial);

        try {
            outputfinal.write(newLinkTable);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
    }

    public List<Signal> getNewSignals() {
        List<Signal> signals = new ArrayList<>();
        for (Lightpath lightpath : newLinkTable.getLightPaths()) {
            signals.add(lightpath.getNewSignal());
        }
        return signals;
    }

    public List<Signal> getOldSignals() {
        List<Signal> signals = new ArrayList<>();
        for (Lightpath lightpath : newLinkTable.getLightPaths()) {
            signals.add(lightpath.getSignal());
        }
        return signals;
    }
}
