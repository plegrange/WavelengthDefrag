package Test;

import java.util.*;

public class Dijkstra {

    private RoutingTable _Table;

    private final ArrayList<Node> _Nodes;
    private final ArrayList<Link> _Links;
    private Set<Node> _SettledNodes;
    private Set<Node> _UnsettledNodes;
    private Map<Node, Node> _Predecessors;
    private Map<Node, Double> _Distance;

    public Dijkstra(RoutingTable Table, ArrayList<Node> Nodes, ArrayList<Link> Links) {
        this._Table = Table;
        this._Nodes = Nodes;
        this._Links = Links;
    }

    public void execute(Node source) {
        _SettledNodes = new HashSet<Node>();
        _UnsettledNodes = new HashSet<Node>();
        _Distance = new HashMap<Node, Double>();
        _Predecessors = new HashMap<Node, Node>();
        _Distance.put(source, 0.0);
        _UnsettledNodes.add(source);
        while (_UnsettledNodes.size() > 0) {
            Node node = getMinimum(_UnsettledNodes);
            _SettledNodes.add(node);
            _UnsettledNodes.remove(node);
            findMinimalDistances(node);
        }
    }

    private void findMinimalDistances(Node node) {
        List<Node> adjacentNodes = getNeighbors(node);
        for (Node target : adjacentNodes) {
            if (getShortestDistance(target) > getShortestDistance(node) + getDistance(node, target)) {
                _Distance.put(target, getShortestDistance(node) + getDistance(node, target));
                _Predecessors.put(target, node);
                _UnsettledNodes.add(target);
            }
        }
    }

    private double getDistance(Node node, Node target) {
        Link link = _Table.GetLink(node._ID, target._ID);
        if (link != null) {
            return link._Length;
        }
        throw new RuntimeException("No link.");
    }

    private List<Node> getNeighbors(Node node) {
        List<Node> neighbors = new ArrayList<Node>();
        for (Link link : _Links) {
            if (link._A == node && !isSettled(link._B)) {
                neighbors.add(link._B);
            } else if (link._B == node && !isSettled(link._A)) {
                neighbors.add(link._A);
            }
        }
        return neighbors;
    }

    private Node getMinimum(Set<Node> snodes) {
        Node minimum = null;
        for (Node node : snodes) {
            if (minimum == null) {
                minimum = node;
            } else {
                if (getShortestDistance(node) < getShortestDistance(minimum)) {
                    minimum = node;
                }
            }
        }
        return minimum;
    }

    private boolean isSettled(Node node) {
        return _SettledNodes.contains(node);
    }

    private double getShortestDistance(Node destination) {
        Double d = _Distance.get(destination);
        if (d == null) {
            return Double.MAX_VALUE;
        } else {
            return d;
        }
    }

    public LinkedList<Node> getPath(Node target) {
        LinkedList<Node> path = new LinkedList<Node>();
        Node step = target;
        if (_Predecessors.get(step) == null) {
            return null;
        }
        path.add(step);
        while (_Predecessors.get(step) != null) {
            step = _Predecessors.get(step);
            path.add(step);
        }
        Collections.reverse(path);
        return path;
    }

}
