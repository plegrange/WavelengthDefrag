package Test;

import java.util.Stack;

public class Signal {

    Node _Source;
    Node _Destination;
    double _Intensity;
    double _Penalty;
    double _Wavelength;
    Node _Next;
    int _Hop;
    Path _Route;

    RoutingTable _Table;
    Stack<Node> _Visited = new Stack<Node>();

    public Signal(RoutingTable Table, Node Source, Node Destination, double Intensity, double Wavelength) {
        this._Table = Table;
        this._Source = Source;
        this._Destination = Destination;
        this._Intensity = Intensity;
        this._Wavelength = Wavelength;
        this._Hop = 0;
    }

    public double GetVisitedLength() {
        double length = 0.0;
        Link temp;
        for (int i = 1; i < _Visited.size(); i++) {
            temp = _Table.GetLink(_Visited.get(i - 1)._ID, _Visited.get(i)._ID);
            if (temp != null) {
                length += temp._Length;
            }
        }
        return length;
    }

    public boolean Visited(Node node) {
        for (Node v : _Visited) {
            if (v == node) {
                return true;
            }
        }
        return false;
    }

    public double get_Wavelength() {
        return _Wavelength;
    }

    public void setWavelength(double wavelength) {
        this._Wavelength = wavelength;
    }

    public int getSource() {
        return _Source.getID();
    }

    public int getDestination() {
        return _Destination.getID();
    }

    public boolean isSameSignal(Signal other) {
        if (this._Wavelength == other._Wavelength) {
            if (this._Route._Path == other._Route._Path) {
                System.out.println();
                return true;
            }
        }
        return false;
    }

    public Path get_Route() {
        return _Route;
    }
}
