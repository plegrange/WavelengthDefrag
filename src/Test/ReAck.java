package Test;

import java.util.LinkedList;

public class ReAck extends Ack {

    RoutingTable _Table;
    Node _Source;
    Boolean _Success;

    LinkedList<Node> _Traversed = new LinkedList<Node>();

    ReAck(RoutingTable Table, Signal signal, boolean Success) {
        super(signal);
        this._Table = Table;
        this._Success = Success;
        this._Source = signal._Destination;
        _Traversed.add(signal._Destination);
    }

    ReAck(RoutingTable Table, Node Source, Signal signal, boolean Success) {
        super(signal);
        this._Table = Table;
        this._Success = Success;
        this._Source = Source;
        _Traversed.add(Source);
    }

    public Node NextNode() {
        Node temp = _Signal._Visited.pop();
        _Traversed.add(temp);
        return temp;
    }

    public double GetTraversedLength() {
        double length = 0;
        Link temp;
        for (int i = 1; i < _Traversed.size(); i++) {
            temp = _Table.GetLink(_Traversed.get(i - 1)._ID, _Traversed.get(i)._ID);
            length += temp._Length;
        }
        return length;
    }

}
