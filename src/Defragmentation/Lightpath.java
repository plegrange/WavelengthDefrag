package Defragmentation;

import Test.Node;
import Test.Path;
import Test.Signal;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by FuBaR on 8/28/2016.
 */
public class Lightpath {
    private double wavelength;
    Path route;
    String id;
    private Signal signal;
    List<String> linkIDs;

    public Lightpath(Signal signal, String id) {
        this.signal = signal;
        this.route = signal.get_Route();
        this.id = id;
        this.wavelength = signal.get_Wavelength();
        setLinkIDs();
    }

    public Signal getSignal() {
        return signal;
    }

    public Signal getNewSignal() {
        Signal newSignal = signal.cloneSignal();
        newSignal.setWavelength(this.wavelength);
        return newSignal;
    }

    public void setWavelength(double wavelength) {
        this.wavelength = wavelength;
    }

    public double getWavelength() {
        return wavelength;
    }

    public Lightpath cloneWithNewWavelength(double wavelength) {
        Lightpath newLightPath = new Lightpath(this.signal, this.id);
        newLightPath.setWavelength(wavelength);
        newLightPath.route = this.route;
        newLightPath.linkIDs = this.linkIDs;
        return newLightPath;
    }

    private void setLinkIDs() {
        linkIDs = new ArrayList<>();
        String newID;
        List<Node> nodes = (List<Node>) route._Path;
        for (int i = 0; i < nodes.size() - 1; i++) {
            int newInt = nodes.get(i).getID();
            newID = String.valueOf(newInt);
            newInt = nodes.get(i + 1).getID();
            if (String.valueOf(newInt).compareTo(newID) > 0)
                newID = newID + String.valueOf(newInt);
            else
                newID = String.valueOf(newInt) + newID;
            linkIDs.add(newID);
        }
    }
}
