package Defragmentation;

import java.util.List;
import java.util.Random;

/**
 * Created by FuBaR on 8/30/2016.
 */
public class Crosser {
    public Crosser() {
    }

    public List<Lightpath> crossover(List<Lightpath> A, List<Lightpath> B) {
        List<Lightpath> newLightpaths = A;
        for (Lightpath lightpath : newLightpaths) {
            lightpath.wavelength = selectWavelength(getLightpath(A, lightpath.id), getLightpath(B, lightpath.id), newLightpaths);
        }
        return newLightpaths;
    }

    private Lightpath getLightpath(List<Lightpath> list, String id) {
        for (Lightpath lightpath : list) {
            if (id.equals(lightpath.id))
                return lightpath;
        }
        return null;
    }

    private double selectWavelength(Lightpath a, Lightpath b, List<Lightpath> lightpaths) {
        Random random = new Random();
        if (random.nextDouble() >= 0.5) {
            if (isAvailable(a.wavelength, lightpaths))
                return a.wavelength;
            else
                return b.wavelength;
        } else {
            if (isAvailable(b.wavelength, lightpaths))
                return b.wavelength;
            else
                return a.wavelength;
        }
    }

    private boolean isAvailable(double wavelength, List<Lightpath> lightpaths) {
        for (Lightpath lightpath : lightpaths) {
            if (lightpath.wavelength == wavelength)
                return false;
        }
        return true;
    }
}
