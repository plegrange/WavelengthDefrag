package Defragmentation;

import Test.Node;
import Test.Signal;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FuBaR on 8/28/2016.
 */
public class LightpathManager {
    List<Node> nodes;
    List<Signal> signalsAll;
    List<Lightpath> lightPaths;
    private SecureRandom random;

    public LightpathManager(List<Node> nodes) {
        this.nodes = nodes;
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
            List<Signal> signals = getSimilarSignals(signalsAll.get(0));
            lightPaths.add(new Lightpath(signals, new BigInteger(100, random).toString(16)));
        }
    }

    private List<Signal> getSimilarSignals(Signal signal) {
        List<Signal> list = new ArrayList<>();
        list.add(signal);
        signalsAll.remove(signal);
        for (Signal s : signalsAll) {
            if (s.isSameSignal(signal)) {
                list.add(s);
                signalsAll.remove(s);
            }
        }
        return list;
    }

    private void extractSignals() {
        for (Node n : nodes) {
            List<Signal> currentSignals = n.getCurrent();
            for (Signal s : currentSignals) {
                addSignal(s);
            }
        }
    }

    private void addSignal(Signal signal) {
        signalsAll.add(signal);
    }

    public List<Lightpath> getRandomLightpaths() {
        double min = 1200.00, max = 1800;
        SecureRandom random = new SecureRandom();
        List<Lightpath> newLightpaths = lightPaths;
        for (Lightpath lightpath : newLightpaths) {
            lightpath.wavelength = min + (max - min) * random.nextDouble();
        }
        return newLightpaths;
    }
}
