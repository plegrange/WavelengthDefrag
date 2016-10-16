package Defragmentation;

import java.util.List;
import java.util.Random;

/**
 * Created by FuBaR on 8/30/2016.
 */
public class Selector {
    public Selector() {
        random = new Random();
    }

    Random random;

    public LinkTable selectRandom(List<LinkTable> list) {
        //Random random = new Random();
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    public LinkTable selectBest(List<LinkTable> list, FitnessTester fitnessTester) {
        double bestFitness = 9999;
        LinkTable bestLinkTable = null;
        for (LinkTable linkTable : list) {
            double fitness = fitnessTester.testLinkTableFitness(linkTable);
            if (fitness < bestFitness) {
                bestFitness = fitness;
                bestLinkTable = linkTable;
            }
        }
        return bestLinkTable;
    }

}
