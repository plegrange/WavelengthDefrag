package Test;

public class TraAck extends Ack {

    TraAck(Signal signal) {
        super(signal);
    }

    public Node Destination() {
        return _Signal._Visited.peek();
    }
}
