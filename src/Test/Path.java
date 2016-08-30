package Test;

import java.util.LinkedList;

public class Path implements Cloneable {

    double _Desirability = 0.0;
    Double _Length = 0.0;
    RoutingTable _Table;

    public LinkedList<Node> _Path;

    Path(RoutingTable Table, LinkedList<Node> Path) {
        this._Table = Table;
        this._Path = Path;
        CalculatePathLength();
        CalculateDesirability();
    }

    private void CalculatePathLength() {
        _Length = 0.0;
        Link temp;
        for (int i = 1; i < _Path.size(); i++) {
            temp = _Table.GetLink(_Path.get(i - 1)._ID, _Path.get(i)._ID);
            _Length += temp._Length;
        }
    }

    private void CalculateDesirability() {
        _Desirability = 1.0 / _Length;
    }

    //Modified clone() method in Employee class
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


}
