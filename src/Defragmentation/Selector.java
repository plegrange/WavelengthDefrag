package Defragmentation;

import java.util.ArrayList;
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
        random = new Random();
        int index = random.nextInt(list.size());
        return list.get(index);
    }

    public LinkTable selectElite(List<LinkTable> list, FitnessTester fitnessTester, int alpha) {
        List<LinkTable> elite = new ArrayList<>();
        for (LinkTable linkTable : list) {
            {
                fitnessTester.testLinkTableFitness(linkTable);
                elite = addElite(elite, linkTable, alpha);
            }
        }
        random = new Random();
        int index = random.nextInt(elite.size());
        return elite.get(index);
    }

    private List<LinkTable> addElite(List<LinkTable> list, LinkTable table, int alpha) {
        if (list.size() < alpha) {
            list.add(table);
        } else {
            double worst = -99999;
            int worstIndex = 0;
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).fitness > worst) {
                    worst = list.get(i).fitness;
                    worstIndex = i;
                }
            }
            if (table.fitness < worst) {
                list.remove(worstIndex);
                list.add(worstIndex, table);
            }
        }
        return list;
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
