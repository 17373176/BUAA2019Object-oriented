package elevator;

public class StopElevator {
    private static boolean STOP = false; //input thread stop flag
    private Elevator e1;
    private Elevator e2;
    private  Elevator e3;

    public void get(Elevator elevator1,Elevator elevator2,Elevator elevator3) {
        e1 = elevator1;
        e2 = elevator2;
        e3 = elevator3;
    }

    public void runStop() {
        synchronized (e1.get()) {
            e1.getVector().notifyAll();
        }
        synchronized (e2.get()) {
            e2.getVector().notifyAll();
        }
        synchronized (e3.get()) {
            e3.getVector().notifyAll();
        }
        this.STOP = true;
    }

    public boolean getStop() {
        return this.STOP;
    }
}
