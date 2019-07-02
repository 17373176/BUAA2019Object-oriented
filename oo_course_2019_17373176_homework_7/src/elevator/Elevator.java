package elevator;

import com.oocourse.TimableOutput;
import com.oocourse.elevator3.PersonRequest;

import static java.lang.Math.abs;

public class Elevator implements Runnable {
    private Controller controller;
    private int nowFloor = 1; // nowfloor
    private int nowdir = 1; // direction
    private static final long optime = 200;
    private static final long cltime = 200;
    private long runtime; // 电梯运行时间不同
    private int volume; // 电梯载客不同
    private String elename; // 电梯标号
    private int nowvolume = 0; // 当前载客量
    private Revector vector = new Revector(this);
    private StopElevator stop;
    private Runquest runquest = new Runquest();
    private int out = 0;

    public Revector get() {
        return vector;
    }

    public Elevator(Controller controller, long runtime,
                    int volume, String elename, StopElevator stop) {
        this.controller = controller;
        this.runtime = runtime;
        this.volume = volume;
        this.elename = elename;
        this.stop = stop;
    }

    @Override
    public void run() {
        PersonRequest request;
        while ((request = vector.getRequest()) != null) {
            //System.out.println("AC");
            int personId = request.getPersonId();
            int fromFloor = request.getFromFloor();
            int toFloor = request.getToFloor();
            try {
                if (toFloor < nowFloor) {
                    nowdir = -1;
                } else {
                    nowdir = 1;
                }
                if (fromFloor == nowFloor) { // now
                    loop(personId, request);
                } else { // arrive
                    int i = 39;
                    while (i-- > 0) {
                        Thread.sleep(1);
                    }
                    if (runorder() == 1) {
                        //System.out.println("AC");
                        open(nowFloor);
                        order();
                        close(nowFloor);
                        running();
                    } else {
                        runarrive(fromFloor, nowFloor);
                        loop(personId, request);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void loop(int personId, PersonRequest request)
            throws InterruptedException { // loop running
        open(nowFloor);
        in(personId, nowFloor);
        vector.remove(0);
        runquest.addRun(request);
        subVolume();
        order(); // in
        outorder();
        close(nowFloor);
        running();
    }

    public void open(int floor) throws InterruptedException {
        TimableOutput.println(String.format("OPEN-%d-%s", floor, getName()));
        Thread.sleep(optime);
        Thread.sleep(optime);
    }

    public void close(int getFloor) {
        TimableOutput.println(String.format("CLOSE-%d-%s", getFloor,
                getName()));
    }

    public void in(int personId, int getFloor) {
        out = 1;
        TimableOutput.println(String.format("IN-%d-%d-%s",
                personId, getFloor, getName()));
    }

    public void out(int personId, int getFloor) {
        TimableOutput.println(String.format("OUT-%d-%d-%s",
                personId, getFloor, getName()));
        out = 1;
    }

    public void running() throws InterruptedException {
        while (!runquest.isEmptyRun()) {
            PersonRequest request;
            if (nowdir > 0) {
                request = runquest.removeRun(runquest.getPro());
            } else {
                request = runquest.removeRun(runquest.getDown());
            }
            runarrive(request.getToFloor(), nowFloor);
            open(request.getToFloor());
            out(request.getPersonId(), request.getToFloor());
            outorder(); // 同层多人下电梯
            order(); // 上电梯
            close(request.getToFloor());
            nowFloor = request.getToFloor();
        }
    }

    public void arrive(int floor) {
        TimableOutput.println(String.format("ARRIVE-%d-%s", floor, getName()));
    }

    public void runarrive(int toFloor, int getFloor)
            throws InterruptedException {
        int floor = abs(toFloor - getFloor) - 1;
        int arrivefloor = getFloor;
        while (floor > 0) {
            if (toFloor > arrivefloor && ++arrivefloor != 0) {
                nowdir = 1;
                arriveF(arrivefloor);
            } else if (toFloor < arrivefloor && --arrivefloor != 0) {
                nowdir = -1;
                arriveF(arrivefloor);
            }
            floor--;
        }
        Thread.sleep(runtime);
        arrive(toFloor);
        nowFloor = toFloor; // change nowfloor to arrive
    }

    public void arriveF(int arrivefloor) throws InterruptedException {
        Thread.sleep(runtime);
        arrive(arrivefloor);
        nowFloor = arrivefloor; // change nowfloor to arrive
        if (runorder() == 1 || runout() == 1) { // running order
            open(nowFloor);
            order();
            outorder();
            close(nowFloor);
        }
    }

    private void outorder() { // out order
        PersonRequest request;
        while (!runquest.isEmptyRun()) {
            int index = -1;
            if (nowdir > 0) {
                index = runquest.getPro();
                request = runquest.getRun(index);
            } else {
                index = runquest.getDown();
                request = runquest.getRun(index);
            }
            if (request.getToFloor() == nowFloor) {
                out(request.getPersonId(), request.getToFloor());
                runquest.removeRun(index);
                subVolume();
            } else {
                break;
            }
        }
    }

    public int runout() { // weather running out
        PersonRequest request;
        while (!runquest.isEmptyRun()) {
            int index = -1;
            if (nowdir > 0) {
                index = runquest.getPro();
                request = runquest.getRun(index);
            } else {
                index = runquest.getDown();
                request = runquest.getRun(index);
            }
            if (request.getToFloor() == nowFloor) {
                return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }

    public void order() { // in order
        PersonRequest request;
        while (!vector.isEmpty()) {
            int index = vector.proRequest(nowFloor);
            if (index >= 0) {
                request = vector.get(index);
                if ((nowdir * (request.getToFloor() -
                        request.getFromFloor())) > 0) {
                    vector.remove(index);
                    in(request.getPersonId(), nowFloor);
                    runquest.addRun(request);
                } else {
                    break;
                }
            } else {
                break;
            }
        }
    }

    public int runorder() { // weather running in order
        PersonRequest request;
        int index = vector.proRequest(nowFloor);
        if (index >= 0) {
            request = vector.get(index);
            if ((nowdir * (request.getToFloor() -
                    request.getFromFloor())) > 0) {
                return 1;
            } else {
                return 0;
            }
        } else {
            return 0;
        }
    }

    public String getName() {
        return this.elename;
    }

    public int getNowvolume() {
        return nowvolume;
    }

    public int getVolume() {
        return volume;
    }

    public void addVolume() {
        this.nowvolume++;
    }

    public void subVolume() {
        this.nowvolume--;
    }

    public Revector getVector() {
        return this.vector;
    }

    public StopElevator getStop() {
        return this.stop;
    }

    public int getFlag() {
        return out;
    }
}