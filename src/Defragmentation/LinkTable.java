package Defragmentation;

import java.util.List;

/**
 * Created by FuBaR on 8/28/2016.
 */
public class LinkTable {
    public List<Lightpath> lightPaths;
    public List<Double> wavelengths;
    public List<String> linkIDs;
    public String[][] table;

    public LinkTable(List<Lightpath> lightPaths, List<Double> wavelengths, List<String> linkIDs, String[][] table) {
        this.wavelengths = wavelengths;
        this.lightPaths = lightPaths;
        this.linkIDs = linkIDs;
        this.table = table;
    }
}
