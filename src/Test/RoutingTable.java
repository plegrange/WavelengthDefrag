package Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class RoutingTable {

    Link _Table[][] = new Link[100][100];
    ArrayList<Link> _Reservations = new ArrayList<Link>();
    ArrayList<Node> _Nodes;
    ArrayList<Link> _Links;

    //bug tracking
    ArrayList<String> lReserve = new ArrayList<String>();
    ArrayList<String> lRelease = new ArrayList<String>();

    public RoutingTable(ArrayList<Node> Nodes, ArrayList<Link> Links) {
        this._Nodes = Nodes;
        this._Links = Links;
        ClearTable();

        lReserve.clear();
        lRelease.clear();
    }

    public void ClearTable() {
        for (int i = 0; i < 100; i++) {
            for (int j = 0; j < 100; j++) {
                _Table[i][j] = null;
            }
        }
    }

    public Boolean AddLink(Link link, int A, int B) {
        if (GetLink(A, B) == null) {
            _Table[A][B] = link;
            return true;
        }
        return false;
    }

    public Link GetLink(int A, int B) {
        Link temp = _Table[A][B];
        if (temp == null) {
            temp = _Table[B][A];
        }
        return temp;
    }
    /*

	public void RemoveLink(int A, int B)
	{
		_Table[A][B] = null;
		_Table[B][A] = null;
	}
	
	public void RemoveNode(int node)
	{
		for(int i = 0; i < 100; i++)
		{
			_Table[node][i] = null;
			_Table[i][node] = null;
		}
	}
	*/

    public boolean ReserveLink(Signal signal, int A, int B) {
        Link temp = GetLink(A, B);
        if (temp != null) {
            for (Signal linkSig : temp._Reservations) {
                if (linkSig._Wavelength == signal._Wavelength || Math.abs(linkSig._Wavelength - signal._Wavelength) < 0.001) {
                    return false;
                }
            }
            temp._Reservations.add(signal);
            if (!_Reservations.contains(temp)) {
                _Reservations.add(temp);
            }
            return true;
        }
        return false;
    }

    public void ReleaseLink(Signal signal, int A, int B) {
        Link temp = GetLink(A, B);
        //add to list

        temp._Reservations.remove(signal);
        if (temp._Reservations.isEmpty()) {
            _Reservations.remove(temp);
        }
    }

    public void LoadNetwork() {
        ClearTable();
        _Nodes.clear();
        _Links.clear();
        String line;
        String[] split;
        boolean linkTest = false;
        File file = new File("network.txt");
        Scanner scan = null;
        try {
            scan = new Scanner(file);
            while (scan.hasNextLine()) {
                line = scan.nextLine();
                if (line.equals("-")) {
                    linkTest = true;
                } else {
                    split = line.split("#");
                    if (!linkTest) {
                        Node node = new Node(Integer.parseInt(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]));
                        _Nodes.add(node);
                    } else {
                        Node node1 = _Nodes.get(Integer.parseInt(split[0]) - 1);
                        Node node2 = _Nodes.get(Integer.parseInt(split[1]) - 1);
                        Link link = new Link(node1, node2, Integer.parseInt(split[2]), Double.parseDouble(split[3]));
                        _Links.add(link);
                        AddLink(link, node1._ID, node2._ID);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
