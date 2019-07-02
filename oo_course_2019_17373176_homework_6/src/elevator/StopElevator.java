package elevator;

public class StopElevator {
    private static boolean STOP = false; //input thread stop flag

    public void runStop() {
        this.STOP = true;
    }

    public boolean getStop() {
        return this.STOP;
    }
}
