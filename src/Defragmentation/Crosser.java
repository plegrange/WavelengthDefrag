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
        if (A.equals(B)) return newLightpaths;
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

        double wavelength = (a.wavelength + b.wavelength) / 2.00;
        if (!isAvailable(wavelength, lightpaths)) {
            do {
                if (a.wavelength < b.wavelength)
                    wavelength = a.wavelength + (b.wavelength - a.wavelength) * random.nextDouble();
                else
                    wavelength = b.wavelength + (a.wavelength - b.wavelength) * random.nextDouble();
            } while (!isAvailable(wavelength, lightpaths));
        }
        return wavelength;
    }

    private boolean isAvailable(double wavelength, List<Lightpath> lightpaths) {
        for (Lightpath lightpath : lightpaths) {
            if (lightpath.wavelength == wavelength)
                return false;
        }
        return true;
    }
}
