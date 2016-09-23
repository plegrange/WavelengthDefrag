package Defragmentation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FuBaR on 8/28/2016.
 */
public class LinkTableManager {
    List<Lightpath> lightpaths;

    public LinkTableManager(List<Lightpath> lightpaths) {
        this.lightpaths = lightpaths;
    }

    public LinkTable buildInitial() {
        List<Double> wavelengths = new ArrayList<>();
        List<String> linkIDs = new ArrayList<>();
        wavelengths = addWavelength(wavelengths, 1200.00);
        for (Lightpath lightpath : lightpaths) {
            wavelengths = addWavelength(wavelengths, lightpath.getWavelength());
            linkIDs = addLinkIDs(linkIDs, lightpath.linkIDs);
        }
        wavelengths = addWavelength(wavelengths, 1800.00);
        String[][] table = new String[linkIDs.size()][wavelengths.size()];
        for (int i = 0; i < linkIDs.size(); i++) {
            for (int j = 0; j < wavelengths.size(); j++) {
                table[i][j] = "";
            }
        }

        int i, j;
        for (Lightpath lightpath : lightpaths) {
            for (String s : lightpath.linkIDs) {
                i = getLinkIndex(s, linkIDs);
                j = getWavelengthIndex(lightpath.getWavelength(), wavelengths);
                table[i][j] = lightpath.id;
            }
        }
        return new LinkTable(lightpaths, wavelengths, linkIDs, table);
    }

    private int getLinkIndex(String id, List<String> linkIDs) {
        // input : id of a given link
        // output : index of given link in index array
        //          OR -1 if link not found
        for (String s : linkIDs) {
            if (s.equals(id))
                return linkIDs.indexOf(s);
        }
        return -1;
    }

    private int getWavelengthIndex(double wavelength, List<Double> wavelengths) {
        // input : value of a given wavelength
        // output : index of given wavelength in index array
        //          OR -1 if wavelength not found
        for (Double d : wavelengths) {
            if (d.equals(wavelength))
                return wavelengths.indexOf(d);
        }
        return -1;
    }

    private List<String> addLinkIDs(List<String> linkIDs, List<String> ids) {
        for (int i = 0; i < ids.size(); i++) {
            linkIDs = insertUniqueSortedLinkID(linkIDs, ids.get(i));
        }
        return linkIDs;
    }

    private List<String> insertUniqueSortedLinkID(List<String> linkIDs, String id) {
        if (linkIDs.size() == 0)
            linkIDs.add(id);
        else if (linkIDs.size() == 1) {
            if (linkIDs.get(0).compareTo(id) == 0)
                return linkIDs;
            else if (linkIDs.get(0).compareTo(id) > 0)
                linkIDs.add(0, id);
            else
                linkIDs.add(1, id);
        } else {
            for (int i = 0; i < linkIDs.size() - 1; i++) {
                String left = linkIDs.get(i);
                String right = linkIDs.get(i + 1);
                if (left.equals(id) || right.equals(id))
                    return linkIDs;
                else if (id.compareTo(left) < 0) {
                    linkIDs.add(i, id);
                    return linkIDs;
                } else if (id.compareTo(right) < 0) {
                    linkIDs.add(i + 1, id);
                    return linkIDs;
                }

            }
            linkIDs.add(id);
        }
        return linkIDs;
    }

    private List<Double> addWavelength(List<Double> wavelengths, double newWavelength) {
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
                    return wavelengths;
                } else if (newWavelength < right) {
                    wavelengths.add(i + 1, newWavelength);
                    return wavelengths;
                }
            }
            wavelengths.add(newWavelength);
        }
        return wavelengths;
    }
}
