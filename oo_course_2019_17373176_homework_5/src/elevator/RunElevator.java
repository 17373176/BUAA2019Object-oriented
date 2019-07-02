package elevator;

import com.oocourse.TimableOutput;

import static java.lang.Math.abs;

public class RunElevator {
    private final int personId;
    private final int fromFloor;
    private final int toFloor;
    private final int nowFloor;

    private int getFloor;

    public RunElevator(int personId, int fromFloor, int toFloor, int nowFloor) {
        this.personId = personId;
        this.fromFloor = fromFloor;
        this.toFloor = toFloor;
        this.nowFloor = nowFloor;
    }

    public synchronized int Run() throws InterruptedException {
        try {
            getFloor = this.fromFloor;
            if (this.fromFloor == this.nowFloor) { // now
                loop();
            } else {
                Thread.sleep(500 * abs(this.fromFloor - this.nowFloor));
                loop();
            }
        } catch (InterruptedException e) {
            System.out.println(e);
        }
        return getFloor;
    }

    public void loop() throws InterruptedException {
        try {
            open();
            in();
            close();
            running();
            open();
            out();
            close();
        } catch (InterruptedException e) {
            System.out.println(e);
        }
    }

    public void open() throws InterruptedException {
        TimableOutput.println(String.format("OPEN-%d", getFloor));
        Thread.sleep(250);
    }

    public void close() throws InterruptedException {
        Thread.sleep(250);
        TimableOutput.println(String.format("CLOSE-%d", getFloor));
    }

    public void in() {
        TimableOutput.println(String.format("IN-%d-%d",
                this.personId, getFloor));
    }

    public void running() throws InterruptedException {
        Thread.sleep(500 * abs(this.toFloor - getFloor));
        getFloor = this.toFloor;
    }

    public void out() {
        TimableOutput.println(String.format("OUT-%d-%d",
                this.personId, getFloor));
    }
}
