package elevator;

import com.oocourse.elevator3.PersonRequest;

import java.util.Vector;

public class Runquest {
    private Vector<PersonRequest> runquest = new Vector<>(); // run

    public void addRun(PersonRequest request) {
        runquest.add(request);
    }

    public int getPro() { // return lowest floor of up elevator
        int i;
        int k = 20;
        int index = -1;
        for (i = 0; i < runquest.size(); i++) {
            if (runquest.get(i).getToFloor() <= k) {
                k = runquest.get(i).getToFloor();
                index = i;
            }
        }
        return index;
    }

    public int getDown() { // return highest floor of down elevator
        int i;
        int k = -3;
        int index = -1;
        for (i = 0; i < runquest.size(); i++) {
            if (runquest.get(i).getToFloor() >= k) {
                k = runquest.get(i).getToFloor();
                index = i;
            }
        }
        return index;
    }

    public PersonRequest getRun(int index) { // return
        return runquest.get(index);
    }

    public PersonRequest removeRun(int index) { // return and remove
        return runquest.remove(index);
    }

    public boolean isEmptyRun() {
        return runquest.isEmpty();
    }
}
