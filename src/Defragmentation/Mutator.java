package Defragmentation;

import java.util.List;
import java.util.Random;

/**
 * Created by FuBaR on 8/30/2016.
 */
public class Mutator {
    public Mutator() {
    }

    private double mutationRate = 0.01;
    private double mutationDistance = 0.5;
    private Random globalRandom;

    public LinkTable mutateLightpaths(LinkTable linkTable) {
        List<Lightpath> lightpaths = linkTable.getLightPaths();
        Random random = new Random();
        globalRandom = new Random();
        for (Lightpath lightpath : lightpaths) {
            if (random.nextDouble() < mutationRate) {
                double old = lightpath.getWavelength();
                lightpath.setWavelength(getMutatedWavelength(old, linkTable.wavelengths));
                //lightpath.setWavelength(getBucketWavelength(old, linkTable.wavelengths));
                //lightpath.setWavelength(getAvailableWavelength(old - mutationDistance, old + mutationDistance, linkTable.wavelengths));
                linkTable.replaceWavelength(old, lightpath.getWavelength());
            }
        }
        LinkTableManager linkTableManager = new LinkTableManager(lightpaths);
        return linkTableManager.buildInitial();
    }

    private double getMutatedWavelength(double wav, List<Double> wavelengths) {
        double newWav;
        do {
            newWav = wav + 2 * mutationDistance * globalRandom.nextDouble();
        } while (!isAvailable(newWav, wavelengths));
        return newWav;
    }

    private double getAvailableWavelength(double min, double max, List<Double> wavelengths) {
        Random random = new Random();
        double value;
        do {
            value = min + (max - min) * globalRandom.nextDouble();
        } while (!isAvailable(value, wavelengths));
        return value;
    }

    private boolean isAvailable(double wavelength, List<Double> wavelengths) {
        if (wavelength < 1200 || wavelength > 1800) return false;
        for (Double wav : wavelengths) {
            if (wav == wavelength)
                return false;
        }
        return true;
    }

    private double getBucketWavelength(double wav, List<Double> wavelengths) {
        double lowerBound = 1200;
        double bucketWRange = 100;
        while (lowerBound <= 1800 - bucketWRange) {
            if (wav < 1200 || wav > 1800)
                return getAvailableWavelength(1200, 1800, wavelengths);
            if (wav < lowerBound + bucketWRange) {
                return getAvailableWavelength(lowerBound, lowerBound + bucketWRange, wavelengths);
            }
            lowerBound += bucketWRange;
        }
        return wav;
    }
}
