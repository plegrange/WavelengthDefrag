package Test;

import java.util.ArrayList;

public class Link {

    double _Length;
    double _Loss;
    Node _A;
    Node _B;
    ArrayList<Signal> _Reservations = new ArrayList<Signal>();

    public Link(Node A, Node B, double Length, double Loss) {
        this._A = A;
        this._B = B;
        this._Length = Length;
        this._Loss = Loss;
    }

    public boolean WavelengthAvailability(double wave) {
        for (Signal signal : _Reservations) {
            if (signal._Wavelength == wave || Math.abs(signal._Wavelength - wave) < 0.001) {
                return false;
            }
        }
        return true;
    }

    public String getLinkID() {
        String s = String.format("%s%s", _A._ID, _B._ID);
        return s;
    }

    public ArrayList<Signal> get_Reservations() {
        return _Reservations;
    }

    public Node get_A(){
        return _A;
    }

    public Node get_B(){
        return _B;
    }

}
