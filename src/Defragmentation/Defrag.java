package Defragmentation;

import ExcelWriter.LinkTableOutput;
import Test.Node;
import jxl.write.WriteException;

import java.io.IOException;
import java.util.List;

/**
 * Created by FuBaR on 8/28/2016.
 */
public class Defrag {
    LightpathManager lightpathManager;
    LinkTableManager linkTableManager;
    LinkTableOutput linkTableOutput = new LinkTableOutput("initial.xls");
    GeneticAlgorithm geneticAlgorithm;

    public Defrag(List<Node> nodes) {
        lightpathManager = new LightpathManager(nodes);
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

    }
}
