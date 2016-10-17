package Defragmentation;

import Test.Link;
import Test.Signal;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by FuBaR on 8/28/2016.
 */
public class LightpathManager {
    ArrayList<Link> links;
    List<Signal> signalsAll;
    List<Lightpath> lightPaths;
    private SecureRandom random;

    public LightpathManager(ArrayList<Link> links) {
        this.links = links;
    }

    public List<Lightpath> build() {
        random = new SecureRandom();
        signalsAll = new ArrayList<>();
        extractSignals();
        lightPaths = new ArrayList<>();
        buildLightpaths();
        return lightPaths;
    }

    private void buildLightpaths() {
        while (signalsAll.size() > 0) {
            lightPaths.add(new Lightpath(signalsAll.remove(0), new BigInteger(100, random).toString(16)));
        }
    }

    private void extractSignals() {
        for (Link link : links) {
            ArrayList<Signal> currentSignals = link.get_Reservations();
            for (Signal s : currentSignals) {
                addSignal(s);
            }
        }
    }

    private void addSignal(Signal signal) {
        if (!signalsAll.contains(signal)) signalsAll.add(signal);
    }

    public List<Lightpath> getRandomLightpaths() {
        double min = 1200.00, max = 1800;
        // SecureRandom random = new SecureRandom();
        Random rand = new Random();
        List<Lightpath> newLightpaths = new ArrayList<>();
        for (Lightpath lightpath : lightPaths) {
            double newWave;
            // double bucketPoint = (min+max)/2 + rand.nextGaussian()*300;
            do {
                newWave = min + (max - min) * Math.random();
                //newWave = (min + max)/2 + rand.nextGaussian()*250;
            } while (newWave > max || newWave < min);
            Lightpath newLightPath = lightpath.cloneWithNewWavelength(newWave);
            newLightpaths.add(newLightPath); //(min+max)/2 + random.nextGaussian()*300);
        }
        return newLightpaths;
    }
}
