package Defragmentation;

import Test.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by s213391244 on 9/8/2016.
 */
public class NetworkRebuilder {
    List<Signal> oldSignals, newSignals;
    public ArrayList<Node> initialNodes, finalNodes;
    public ArrayList<Link> initalLinks, finalLinks;
    public ACO aco, newACO;
    public RoutingTable table;
    public NetworkRebuilder(List<Signal> oldSignals, List<Signal> newSignals, ArrayList<Node> nodes, ArrayList<Link> links, ACO aco) {
        this.oldSignals = oldSignals;
        this.newSignals = newSignals;
        this.initialNodes = nodes;
        this.initalLinks = links;
        this.aco = aco;
        test();
    }

    public void rebuild() {
        finalNodes = rebuildNodes(initialNodes);
        finalLinks = rebuildLinks(initalLinks);
        //newACO = aco;
        //newACO.setNodes(finalNodes);
        //newACO.setLinks(finalLinks);
        //table = new RoutingTable(finalNodes, finalLinks);
    }

    private boolean test() {
        for (int i = 0; i < oldSignals.size(); i++) {
            if (!oldSignals.get(i).equals(newSignals.get(i)))
                return true;
        }
        return false;
    }

    public ArrayList<Node> rebuildNodes(ArrayList<Node> nodes) {
        for (Node node : nodes) {
            for (int i = 0; i < oldSignals.size(); i++) {
                node.rebuild(oldSignals.get(i), newSignals.get(i));
            }
        }
        return nodes;
    }

    public ArrayList<Link> rebuildLinks(ArrayList<Link> links) {
        for (Link link : links) {
            ArrayList<Signal> reservations = link.get_Reservations();
            for (Signal signal : reservations) {
                for (int i = 0; i < oldSignals.size(); i++) {
                    if (signal.isSameSignal(oldSignals.get(i))) {
                        signal.setWavelength(newSignals.get(i).get_Wavelength());
                    }
                }
            }
            link.set_Reservations(reservations);
        }
        return links;
    }

    public ArrayList<Node> rebuildACONodes(ACO aco) {
        ArrayList<Node> nodes = aco.getNodes();
        nodes = rebuildNodes(nodes);
        return nodes;
    }

    public ArrayList<Link> rebuildACOLinks(ACO aco) {
        ArrayList<Link> links = aco.getLinks();
        links = rebuildLinks(links);
        return links;
    }
}
