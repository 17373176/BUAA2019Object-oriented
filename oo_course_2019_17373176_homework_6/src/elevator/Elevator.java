package elevator;

import com.oocourse.TimableOutput;
import com.oocourse.elevator2.PersonRequest;

import static java.lang.Math.abs;

public class Elevator implements Runnable {
    private Controller controller;
    private int nowFloor = 1; // nowfloor
    private int nowdir = 1; // direction
    private static final long optime = 200;
    private static final long cltime = 200;
    private static final long runtime = 400;

    public Elevator(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void run() {
        PersonRequest request;
        while ((request = this.controller.getRequest()) != null) {
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
                    int i = 29;
                    while (i-- > 0) {
                        Thread.sleep(2);
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
        controller.removeRequest(0);
        controller.addRun(request);
        order(); // in
        outorder();
        close(nowFloor);
        running();
    }

    public void open(int floor) throws InterruptedException {
        TimableOutput.println(String.format("OPEN-%d", floor));
        Thread.sleep(optime);
        Thread.sleep(optime);
    }

    public void close(int getFloor) {
        TimableOutput.println(String.format("CLOSE-%d", getFloor));
    }

    public void in(int personId, int getFloor) {
        TimableOutput.println(String.format("IN-%d-%d",
                personId, getFloor));
    }

    public void out(int personId, int getFloor) {
        TimableOutput.println(String.format("OUT-%d-%d",
                personId, getFloor));
    }

    public void running() throws InterruptedException {
        while (!controller.isEmptyRun()) {
            PersonRequest request;
            if (nowdir > 0) {
                request = controller.removeRun(controller.getPro());
            } else {
                request = controller.removeRun(controller.getDown());
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
        TimableOutput.println(String.format("ARRIVE-%d", floor));
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
        while (!controller.isEmptyRun()) {
            int index = -1;
            if (nowdir > 0) {
                index = controller.getPro();
                request = controller.getRun(index);
            } else {
                index = controller.getDown();
                request = controller.getRun(index);
            }
            if (request.getToFloor() == nowFloor) {
                out(request.getPersonId(), request.getToFloor());
                controller.removeRun(index);
            } else {
                break;
            }
        }
    }

    public int runout() { // weather running out
        PersonRequest request;
        while (!controller.isEmptyRun()) {
            int index = -1;
            if (nowdir > 0) {
                index = controller.getPro();
                request = controller.getRun(index);
            } else {
                index = controller.getDown();
                request = controller.getRun(index);
            }
            if (request.getToFloor() == nowFloor) {
                return 1;
            } else {
                return 0;
            }
        }
        return 0;
    }

    public synchronized void order() { // in order
        PersonRequest request;
        while (!this.controller.isEmpty()) {
            int index = controller.proRequest(nowFloor);
            if (index >= 0) {
                request = controller.get(index);
                if ((nowdir * (request.getToFloor() -
                        request.getFromFloor())) > 0) {
                    controller.removeRequest(index);
                    in(request.getPersonId(), nowFloor);
                    controller.addRun(request);
                } else {
                    break;
                }
            } else {
                break;
            }
        }
    }

    public synchronized int runorder() { // weather running in order
        PersonRequest request;
        int index = controller.proRequest(nowFloor);
        if (index >= 0) {
            request = controller.get(index);
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
}