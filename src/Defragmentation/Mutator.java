package Defragmentation;

import java.util.List;
import java.util.Random;

/**
 * Created by FuBaR on 8/30/2016.
 */
public class Mutator {
    public Mutator() {
    }

    public LinkTable mutateLightpaths(LinkTable linkTable) {
        List<Lightpath> lightpaths = linkTable.lightPaths;
        List<Double> wavelengths = linkTable.wavelengths;
        Random random = new Random();
        Lightpath lightpath = lightpaths.remove(random.nextInt(lightpaths.size()));
        wavelengths.remove(lightpath.wavelength);
        lightpath.wavelength = getAvailableWavlength(wavelengths);
        lightpaths.add(lightpath);
        LinkTableManager linkTableManager = new LinkTableManager(lightpaths);
        return linkTableManager.buildInitial();
    }

    private double getAvailableWavlength(List<Double> wavelengths) {
        double min = 1200, max = 1800.00;
        Random random = new Random();
        double wavelength;
        do {
            wavelength = min + (max - min) * random.nextDouble();
        } while (wavelengths.contains(wavelength));
        return wavelength;
    }
}
