package Defragmentation;

import java.util.List;
import java.util.Random;

/**
 * Created by s213391244 on 2016/08/31.
 */
public class HeuristicAlgorithm {

    public HeuristicAlgorithm() {

    }

    FitnessTester fitnessTester;

    public LinkTable repack(LinkTable linkTable) {
        FitnessTester fitnessTester = new FitnessTester();
        double fitness = fitnessTester.testLinkTableFitness(linkTable);
        System.out.println(fitness);
        Random random = new Random();
        List<Lightpath> lightpaths = linkTable.getLightPaths();
        List<Double> wavelengths = linkTable.wavelengths;
        LinkTable newLinkTable = null;
        for (int i = 0; i < linkTable.getLightPaths().size()*5; i++) {
            Lightpath lightPath = lightpaths.remove(random.nextInt(lightpaths.size()));
            wavelengths.remove(lightPath.getWavelength());
            lightPath.setWavelength(firstFit(wavelengths));
            lightpaths.add(lightPath);
            LinkTableManager linkTableManager = new LinkTableManager(lightpaths);
            newLinkTable = linkTableManager.buildInitial();
        }
        fitness = fitnessTester.testLinkTableFitness(newLinkTable);
        System.out.println(fitness);

        return newLinkTable;
    }

    private double firstFit(List<Double> wavelengths) {
        double minSeparation = 0.5;
        double point1, point2;
        Random random = new Random();
        for (int i = 0; i < wavelengths.size() - 1; i++) {
            point1 = wavelengths.get(i);
            point2 = wavelengths.get(i + 1);
            double separation = point2 - point1;
            if (separation > minSeparation) {
                return point1 + (point2 - point1) * random.nextDouble();
            }
        }
        return -1;
    }

    private double getLargestGap(List<Double> wavelengths) {
        double largestGapMin = 0, largestGapMax = 0;
        double largestSeparation = -9999;
        for (int i = 0; i < wavelengths.size() - 1; i++) {
            double separation = wavelengths.get(i + 1) - wavelengths.get(i);
            if (separation > largestSeparation) {
                largestSeparation = separation;
                largestGapMin = wavelengths.get(i);
                largestGapMax = largestGapMin + separation;
            }
        }
        return getMutationWavelength(largestGapMin, largestGapMax, wavelengths);
    }

    private double getSmallestAvailableSpace(List<Double> wavelengths) {
        double t = 0.5;
        double freeMin = 9999;
        double bestMin = 0, bestMax = 0;
        double separation, bestSeparation;
        for (int i = 0; i < wavelengths.size() - 1; i++) {
            separation = wavelengths.get(i + 1) - wavelengths.get(i);
            if (separation < freeMin && separation > 2.0 * t) {
                freeMin = separation;
                bestMin = wavelengths.get(i);
                bestMax = bestMin + separation;
            }
        }
        return getMutationWavelength(bestMin + 2 * t, bestMax - 2 * t, wavelengths);
    }

    private double getMutationWavelength(double min, double max, List<Double> wavelengths) {
        Random random = new Random();
        double randomWavelength = min + (max - min) * random.nextDouble();
        while (wavelengths.contains(randomWavelength)) {
            randomWavelength = min + (max - min) * random.nextDouble();
        }
        return randomWavelength;
    }
}
