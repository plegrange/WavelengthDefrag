package Defragmentation;

import java.util.List;
import java.util.Random;

/**
 * Created by FuBaR on 8/30/2016.
 */
public class Mutator {
    public Mutator() {
        globalRandom = new Random();
    }

    private double mutationRate;
    private double mutationDistance = 50;
    private Random globalRandom;
    private double thickness;

    public LinkTable mutateLightpaths(LinkTable linkTable) {
        List<Lightpath> lightpaths = linkTable.getLightPaths();
        Random random = new Random();
        globalRandom = new Random();
        //mutationRate = 1;
        thickness = 0.4;//getSmallestGap(linkTable.wavelengths);
        for (int i = 0; i < lightpaths.size()/100; i++) {
            //if (Math.random() * 100 < mutationRate) {
            //mutationRate = (Math.random());
            Lightpath lightpath = lightpaths.get(random.nextInt(lightpaths.size()));
            //lightpaths.remove(lightpath);
            double old = lightpath.getWavelength();
            lightpath.setWavelength(getMutatedWavelength(old, linkTable.wavelengths));
            //lightpath.setWavelength(getBucketWavelength(old, linkTable.wavelengths));
            //lightpath.setWavelength(getAvailableWavelength(1200,1800, linkTable.wavelengths));
            linkTable.replaceWavelength(old, lightpath.getWavelength());
            //lightpaths.add(lightpath);
        }
        //}
        LinkTableManager linkTableManager = new LinkTableManager(lightpaths);
        return linkTableManager.buildInitial();
    }

    private double getSmallestGap(List<Double> wavelengths) {
        double smallestGap = 999999;
        for (int i = 0; i < wavelengths.size() - 1; i++) {
            double left = wavelengths.get(i);
            double right = wavelengths.get(i + 1);
            if (right - left < smallestGap) {
                smallestGap = right - left;
            }
        }
        return smallestGap;
    }

    private double getMutatedWavelength(double wav, List<Double> wavelengths) {
        double newWav;
        do {
            newWav = wav - mutationDistance + 2 * mutationDistance * Math.random();
        } while (!isAvailable(newWav, wavelengths));
        return newWav;
    }

    private double getAvailableWavelength(double min, double max, List<Double> wavelengths) {
        //Random random = new Random();
        double value;
        do {
            value = min + (max - min) * Math.random();
        } while (!isAvailable(value, wavelengths));
        return value;
    }

    private boolean isAvailable(double wavelength, List<Double> wavelengths) {
        if (wavelength < 1200 || wavelength > 1800) return false;
        for (int i = 0; i < wavelengths.size(); i++) {
            if (Math.abs(wavelength - wavelengths.get(i)) < thickness || wavelength == wavelengths.get(i))
                return false;
        }
        return true;
    }

    private double getBucketWavelength(double wav, List<Double> wavelengths) {
        double lowerBound = 1200;
        double bucketWRange = 100, bucketMidPoint;
        double wavelength;
        while (lowerBound <= 1800 - bucketWRange) {
            bucketMidPoint = lowerBound + bucketWRange / 2;
            if (wav < lowerBound + bucketWRange) {
                wavelength = bucketMidPoint + globalRandom.nextGaussian() * (bucketMidPoint - lowerBound);
                while (!isAvailable(wavelength, wavelengths)) {
                    wavelength = bucketMidPoint + globalRandom.nextGaussian() * (bucketMidPoint - lowerBound);
                }
                return wavelength;
            }
            lowerBound += bucketWRange;
        }
        return wav;
    }
}
