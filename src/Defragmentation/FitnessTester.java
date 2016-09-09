package Defragmentation;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FuBaR on 8/30/2016.
 */
public class FitnessTester {
    private double maxWavelength = 1800.0, minWavelength = 1200.0;

    public FitnessTester() {
    }

    public double testLinkTableFitness(LinkTable linkTable) {
        double fragmentation = 0;
        try {
            for (int i = 0; i < linkTable.linkIDs.size(); i++) {
                List<Double> linkWavelengths = getLinkWavelengths(i, linkTable);
                if (linkWavelengths.size() == 0) {

                } else
                    fragmentation += calculateFragmentation(linkWavelengths);
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return fragmentation / linkTable.linkIDs.size() * 100;
    }

    private List<Double> getLinkWavelengths(int i, LinkTable linkTable) {
        // input : link index
        // output : list containing all occupied wavelengths on given link
        List<Double> linkWavelengths = new ArrayList<>();

        for (int j = 0; j < linkTable.wavelengths.size(); j++) {
            try {
                if (linkTable.table[i][j] != "") {
                    linkWavelengths.add(linkTable.wavelengths.get(j));
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }

        return linkWavelengths;
    }

    private double blockingProbability(List<Double> linkWavelengths) {
        double availableSlotBlocks = 0;
        double point1;
        double point2;
        double separation;
        double minSpace = 2.00;
        int collisionsDetected = 0;
        for (int i = 0; i < linkWavelengths.size() - 1; i++) {
            point1 = linkWavelengths.get(i);
            point2 = linkWavelengths.get(i + 1);
            separation = point2 - point1;
            if (separation > minSpace)
                availableSlotBlocks++;
        }
        return 2.00 / (Math.pow((maxWavelength - minWavelength) / 4.00 + 1.00, 2) + Math.pow(availableSlotBlocks, 2));
    }

    private double calculateFragmentation(List<Double> linkWavelengths) {
        // input : list of a given link's occupied wavelengths
        // output : fragmentation coefficient
        double freeMax = -999;
        double free = 0;
        double t = 0.1;
        double point1;
        double point2;
        double separation;
        int collisionsDetected = 0;
        for (int i = 0; i < linkWavelengths.size() - 1; i++) {
            point1 = linkWavelengths.get(i);
            point2 = linkWavelengths.get(i + 1);
            separation = (point2 - t) - (point1 + t);
            if (separation < 0)
                collisionsDetected++;
            else {
                free += separation;
                if (separation > freeMax)
                    freeMax = separation;
            }
        }
        return (free - freeMax) / free + collisionsDetected / linkWavelengths.size();
    }
}
