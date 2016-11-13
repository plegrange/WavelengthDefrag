package Defragmentation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by FuBaR on 8/30/2016.
 */
public class Crosser {

    public Crosser() {

    }

    public List<Lightpath> crossover(LinkTable A, LinkTable B) {
        //if (fitnessTester.testLinkTableFitness(A) < fitnessTester.testLinkTableFitness(B))
        //  return A.getLightPaths();
        //else return B.getLightPaths();
        List<Lightpath> lightpaths = new ArrayList<>();
        for (Lightpath lightpath : A.getLightPaths()) {
            lightpaths.add(lightpath.cloneWithNewWavelength(selectWavelength(lightpath, A.fitness, getLightpath(B.getLightPaths(), lightpath.id), B.fitness, lightpaths)));
        }
        return lightpaths;
    }

    private Lightpath getLightpath(List<Lightpath> list, String id) {
        for (Lightpath lightpath : list) {
            if (id.equals(lightpath.id))
                return lightpath;
        }
        return null;
    }

    FitnessTester fitnessTester = new FitnessTester();

    private double selectWavelength(Lightpath a, double fitnessA, Lightpath b, double fitnessB, List<Lightpath> lightpaths) {
        Random random = new Random();
        if (a.getWavelength() == b.getWavelength()) return a.getWavelength();
        double wavelength = (fitnessA / (fitnessA + fitnessB) * a.getWavelength() + fitnessB / (fitnessA + fitnessB) * b.getWavelength());
        if (!isAvailable(wavelength, lightpaths)) {
            do {
                if (a.getWavelength() < b.getWavelength())
                    wavelength =( a.getWavelength() + (b.getWavelength() - a.getWavelength()) * random.nextDouble());
                else
                    wavelength = (b.getWavelength() + (a.getWavelength() - b.getWavelength()) * random.nextDouble());
            } while (!isAvailable(wavelength, lightpaths));
        }
        return (wavelength);
    }

    private boolean isAvailable(double wavelength, List<Lightpath> lightpaths) {
        for (Lightpath lightpath : lightpaths) {
            if (lightpath.getWavelength() == wavelength)
                return false;
        }
        return true;
    }
}
