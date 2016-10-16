package Defragmentation;

import java.util.List;

/**
 * Created by FuBaR on 8/28/2016.
 */
public class LinkTable {
    private List<Lightpath> lightPaths;
    public List<Double> wavelengths;
    public List<String> linkIDs;
    public String[][] table;
    public double fitness;

    public LinkTable(List<Lightpath> lightPaths, List<Double> wavelengths, List<String> linkIDs, String[][] table) {
        this.wavelengths = wavelengths;
        this.lightPaths = lightPaths;
        this.linkIDs = linkIDs;
        this.table = table;
    }

    public void replaceWavelength(double old, double newWav) {
        for (double wavelength : wavelengths) {
            if (wavelength == old) {
                wavelengths.remove(wavelength);
                addWavelength(newWav);
                break;
            }
        }
    }

    public List<Lightpath> getLightPaths(){
        return lightPaths;
    }

    private void addWavelength(double newWavelength) {
        if (wavelengths.size() == 0) {
            wavelengths.add(newWavelength);
        } else if (wavelengths.size() == 1) {
            if (wavelengths.get(0) > newWavelength)
                wavelengths.add(0, newWavelength);
            else
                wavelengths.add(1, newWavelength);
        } else {
            for (int i = 0; i < wavelengths.size() - 1; i++) {
                double left = wavelengths.get(i);
                double right = wavelengths.get(i + 1);
                if (newWavelength < left) {
                    wavelengths.add(i, newWavelength);
                    return;
                } else if (newWavelength < right) {
                    wavelengths.add(i + 1, newWavelength);
                    return;
                }
            }
            wavelengths.add(newWavelength);
        }

    }

    public LinkTable cloneLinkTable(){
        return new LinkTable(this.lightPaths, this.wavelengths, this.linkIDs, this.table);
    }
}
