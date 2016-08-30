package Test;

import java.util.*;

public class CandidateBuilder {

    RoutingTable _Table;

    ArrayList<Node> _Nodes;
    ArrayList<Node> _Neighbors;
    ArrayList<Link> _Links;
    ArrayList<Link> _ExcludingLinks;
    HashMap<Node, ArrayList<Path>> _Candidates;

    CandidateBuilder(RoutingTable table, ArrayList<Node> nodes, ArrayList<Link> links) {
        this._Table = table;
        this._Nodes = nodes;
        this._Links = links;
    }

    public void BuildCandidates(Node node) {
        _Candidates = new HashMap<Node, ArrayList<Path>>();
        _Neighbors = GetNeighbors(node);
        _ExcludingLinks = ModifiedPathList(node);
        Dijkstra dij = new Dijkstra(_Table, _Nodes, _ExcludingLinks);
        for (Node neighbor : _Neighbors) {
            dij.execute(neighbor);
            for (Node dest : _Nodes) {
                if (dest != neighbor && dest != node) {
                    LinkedList<Node> path = dij.getPath(dest);
                    if (path != null) {
                        path.addFirst(node);
                        Path holder = new Path(_Table, path);
                        if (_Candidates.containsKey(dest)) {
                            _Candidates.get(dest).add(holder);
                        } else {
                            ArrayList<Path> paths = new ArrayList<Path>();
                            paths.add(holder);
                            _Candidates.put(dest, paths);
                        }
                    }
                }
            }
        }
        Iterator iter = _Candidates.keySet().iterator();
        while (iter.hasNext()) {
            Node dest = (Node) iter.next();
            Collections.sort(_Candidates.get(dest), new CustomComparator());
        }
        node._Candidates = _Candidates;
    }

    private ArrayList<Node> GetNeighbors(Node node) {
        ArrayList<Node> nodeNeighbors = new ArrayList<Node>();
        for (Link link : _Links) {
            if (link._A == node) {
                nodeNeighbors.add(link._B);
                ArrayList<Path> paths = new ArrayList<Path>();
                LinkedList<Node> shortLink = new LinkedList<Node>();
                shortLink.add(node);
                shortLink.add(link._B);
                Path shortPath = new Path(_Table, shortLink);
                paths.add(shortPath);
                _Candidates.put(link._B, paths);
            }
            if (link._B == node) {
                nodeNeighbors.add(link._A);
                ArrayList<Path> paths = new ArrayList<Path>();
                LinkedList<Node> shortLink = new LinkedList<Node>();
                shortLink.add(node);
                shortLink.add(link._A);
                Path shortPath = new Path(_Table, shortLink);
                paths.add(shortPath);
                _Candidates.put(link._A, paths);
            }
        }
        return nodeNeighbors;
    }

    private ArrayList<Link> ModifiedPathList(Node node) {
        ArrayList<Link> modifiedLinks = new ArrayList<Link>();
        for (Link link : _Links) {
            if (link._A != node && link._B != node) {
                modifiedLinks.add(link);
            }
        }
        return modifiedLinks;
    }

    public class CustomComparator implements Comparator<Path> {
        @Override
        public int compare(Path p1, Path p2) {
            return p1._Length.compareTo(p2._Length);
        }
    }

}
