package Defragmentation;

/**
 * Created by FuBaR on 8/30/2016.
 */
public class GeneticAlgorithm {
    Mutator mutator;
    Selector selector;
    Crosser crosser;
    FitnessTester fitnessTester;

    public GeneticAlgorithm(LinkTable linkTable) {
        mutator = new Mutator();
        selector = new Selector();
        crosser = new Crosser();
        fitnessTester = new FitnessTester();
    }
}
