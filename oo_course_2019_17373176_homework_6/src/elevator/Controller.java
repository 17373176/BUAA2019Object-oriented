package elevator;

import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;
import java.util.Vector;

public class Controller {
    private ArrayList<PersonRequest> requestV = new ArrayList<>(); // input
    private Vector<PersonRequest> runRequest = new Vector<>(); // run

    private StopElevator stop;

    public Controller(StopElevator stop) {
        this.stop = stop;
    }

    public synchronized void add(PersonRequest request) {
        requestV.add(request);
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        return requestV.isEmpty();
    }

    public synchronized PersonRequest getRequest() {
        try {
            while (isEmpty()) {
                if (stop.getStop() && isEmpty()) {
                    return null;
                } else {
                    wait();
                }
            }
            return requestV.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized PersonRequest get(int index) {
        return requestV.get(index);
    }

    public synchronized int proRequest(int nowfloor) { //return equal nowfloor
        int i;
        for (i = 0; i < requestV.size(); i++) {
            if (requestV.get(i).getFromFloor() == nowfloor) {
                return i;
            }
        }
        return -1;
    }

    public synchronized void removeRequest(int index) {
        requestV.remove(index);
    }

    public void addRun(PersonRequest request) {
        runRequest.add(request);
    }

    public int getPro() { // return lowest floor of up elevator
        int i;
        int k = 16;
        int index = -1;
        for (i = 0; i < runRequest.size(); i++) {
            if (runRequest.get(i).getToFloor() <= k) {
                k = runRequest.get(i).getToFloor();
                index = i;
            }
        }
        return index;
    }

    public int getDown() { // return highest floor of down elevator
        int i;
        int k = -3;
        int index = -1;
        for (i = 0; i < runRequest.size(); i++) {
            if (runRequest.get(i).getToFloor() >= k) {
                k = runRequest.get(i).getToFloor();
                index = i;
            }
        }
        return index;
    }

    public PersonRequest getRun(int index) { // return
        return runRequest.get(index);
    }

    public PersonRequest removeRun(int index) { // return and remove
        return runRequest.remove(index);
    }

    public boolean isEmptyRun() {
        return runRequest.isEmpty();
    }
}