package Defragmentation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FuBaR on 8/30/2016.
 */
public class GeneticAlgorithm {
    Mutator mutator;
    Selector selector;
    Crosser crosser;
    FitnessTester fitnessTester;
    LightpathManager lightpathManager;
    //LinkTableManager linkTableManager;
    int P = 100;
    int alpha = 20;
    List<LinkTable> chromosomes;

    public GeneticAlgorithm(LightpathManager lightpathManager) {
        this.lightpathManager = lightpathManager;
    }

    public LinkTable run(LinkTable linkTableInitial) {
        mutator = new Mutator();
        selector = new Selector();
        crosser = new Crosser();
        fitnessTester = new FitnessTester();
        LinkTable best;
        initializePopulation();
        //testPopulation();
        System.out.println("Initial: "+fitnessTester.testLinkTableFitness(linkTableInitial));
        for (int i = 0; i < 1000; i++) {
            //System.out.println(i);
            List<LinkTable> tempList = crossOver();
            //testPopulation();
            tempList = mutate(tempList);
            //testPopulation();
            chromosomes = merge(chromosomes, tempList);
            selectNewPopulation();
            //testPopulation();
            best = selector.selectBest(chromosomes, fitnessTester);
            System.out.println(i+" -> "+fitnessTester.testLinkTableFitness(best));
        }
        best = selector.selectBest(chromosomes, fitnessTester);
        System.out.println(fitnessTester.testLinkTableFitness(best));
        //testPopulation();
        return selector.selectBest(chromosomes, fitnessTester);
    }

    private List<LinkTable> merge(List<LinkTable> A, List<LinkTable> B) {
        while (B.size() > 0)
            A.add(B.remove(0));
        return A;
    }

    private void selectNewPopulation() {
        List<LinkTable> newPopulation = new ArrayList<>();
        for (int i = 0; i < alpha; i++) {
            LinkTable selected = selector.selectBest(chromosomes, fitnessTester);
            chromosomes.remove(selected);
            newPopulation.add(selected);
        }
        for (int i = alpha; i < P; i++) {
            LinkTable selected = selector.selectRandom(chromosomes);
            chromosomes.remove(selected);
            newPopulation.add(selected);
        }
        chromosomes = newPopulation;
    }

    private List<LinkTable> mutate(List<LinkTable> list) {
        for (LinkTable linkTable : list) {
            mutator.mutateLightpaths(linkTable);
        }
        return list;
    }

    private List<LinkTable> crossOver() {
        List<LinkTable> newPop = new ArrayList<>();
        for (int i = 0; i < P; i++) {
            LinkTable A = selector.selectRandom(chromosomes);
            LinkTable B = selector.selectRandom(chromosomes);
            LinkTableManager linkTableManager = new LinkTableManager(crosser.crossover(A, B));
            LinkTable child = linkTableManager.buildInitial();
            newPop.add(child);
        }
        return newPop;
    }


    private void initializePopulation() {
        chromosomes = new ArrayList<>();
        for (int i = 0; i < P; i++) {
            List<Lightpath> newLightpaths = lightpathManager.getRandomLightpaths();
            LinkTableManager linkTableManager = new LinkTableManager(newLightpaths);
            chromosomes.add(linkTableManager.buildInitial());
        }
    }

    private void testPopulation() {
        for (LinkTable linkTable : chromosomes) {
            System.out.println(fitnessTester.testLinkTableFitness(linkTable));
        }
    }
}
