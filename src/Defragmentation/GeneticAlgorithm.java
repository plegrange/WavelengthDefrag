package Defragmentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by FuBaR on 8/30/2016.
 */
public class GeneticAlgorithm {
    Mutator mutator;
    Selector selector;
    Crosser crosser;
    FitnessTester fitnessTester;
    LightpathManager lightpathManager;
    LinkTableManager linkTableManager;
    int P = 1000;
    int alpha = P / 20;
    List<LinkTable> chromosomes;

    public GeneticAlgorithm(LinkTable linkTable, LightpathManager lightpathManager) {
        mutator = new Mutator();
        selector = new Selector();
        crosser = new Crosser();
        fitnessTester = new FitnessTester();
        this.lightpathManager = lightpathManager;
        initializePopulation();
        //testPopulation();
        for (int i = 0; i < 100; i++) {
            crossOver();
            //testPopulation();
            mutate();
            //testPopulation();
            selectNewPopulation();
            testPopulation();
        }

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

    private void mutate() {
        Random random = new Random();
        List<LinkTable> mutationList = new ArrayList<>();
        for (int i = 0; i < P; i++) {
            if (random.nextDouble() >= 0.5) {
                LinkTable selected = selector.selectBest(chromosomes, fitnessTester);
                chromosomes.remove(selected);
                mutationList.add(mutator.mutateLightpaths(selected));
            } else {
                LinkTable selected = selector.selectRandom(chromosomes);
                chromosomes.remove(selected);
                mutationList.add(mutator.mutateLightpaths(selected));
            }
        }
        while (mutationList.size() > 0) {
            chromosomes.add(mutationList.remove(0));
        }
    }

    private void crossOver() {
        List<LinkTable> newPop = new ArrayList<>();
        for (int i = 0; i < P; i++) {
            LinkTable A = selector.selectRandom(chromosomes);
            LinkTable B = selector.selectRandom(chromosomes);
            linkTableManager = new LinkTableManager(crosser.crossover(A.lightPaths, B.lightPaths));
            LinkTable child = linkTableManager.buildInitial();
            newPop.add(child);
        }
        while (newPop.size() > 0)
            chromosomes.add(newPop.remove(0));
    }


    private void initializePopulation() {
        chromosomes = new ArrayList<>();
        for (int i = 0; i < P; i++) {
            List<Lightpath> newLightpaths = lightpathManager.getRandomLightpaths();
            linkTableManager = new LinkTableManager(newLightpaths);
            chromosomes.add(linkTableManager.buildInitial());
        }
    }

    private void testPopulation() {
        for (LinkTable linkTable : chromosomes) {
            System.out.println(fitnessTester.testLinkTableFitness(linkTable));
        }
    }
}
