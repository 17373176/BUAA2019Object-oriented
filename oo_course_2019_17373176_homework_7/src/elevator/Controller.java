package elevator;

import com.oocourse.elevator3.PersonRequest;

import java.util.ArrayList;

public class Controller {
    private ArrayList<PersonRequest> requestV = new ArrayList<>(); // input

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
        if (!isEmpty()) {
            return requestV.get(0);
        } else {
            return null;
        }
    }

    public synchronized PersonRequest get(int index) {
        return requestV.get(index);
    }

    public void removeRequest(int index) {
        requestV.remove(index);
    }

    public StopElevator getStop() {
        return stop;
    }
}