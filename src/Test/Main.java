package Test;

import java.util.ArrayList;

public class Main {

    static ArrayList<Node> nodes = new ArrayList<Node>();
    static ArrayList<Link> links = new ArrayList<Link>();
    static RoutingTable table;

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        table = new RoutingTable(nodes, links);
        ACO ac = new ACO(nodes, links, table);
        new Test(nodes, links, table, ac, 250, 50);
    }
}
