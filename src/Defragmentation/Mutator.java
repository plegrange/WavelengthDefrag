package Defragmentation;

import java.util.List;
import java.util.Random;

/**
 * Created by FuBaR on 8/30/2016.
 */
public class Mutator {
    public Mutator() {
    }

    private double mutationRate = 0.1;

    public LinkTable mutateLightpaths(LinkTable linkTable) {
        List<Lightpath> lightpaths = linkTable.lightPaths;
        Random random = new Random();
        for (Lightpath lightpath : lightpaths) {
            if (random.nextDouble() < mutationRate) {
                double old = lightpath.wavelength;
                lightpath.wavelength = getAvailableWavelength(1200, 1800, linkTable.wavelengths);
                linkTable.replaceWavelength(old, lightpath.wavelength);
            }
        }
        LinkTableManager linkTableManager = new LinkTableManager(lightpaths);
        return linkTableManager.buildInitial();
    }

    private double getAvailableWavelength(double min, double max, List<Double> wavelengths) {
        Random random = new Random();
        double value;
        do {
            value = min + (max - min) * random.nextDouble();
        } while (!isAvailable(value, wavelengths));
        return value;
    }

    private boolean isAvailable(double wavelength, List<Double> wavelengths) {
        for (Double wav : wavelengths) {
            if (wav == wavelength)
                return false;
        }
        return true;
    }
}
