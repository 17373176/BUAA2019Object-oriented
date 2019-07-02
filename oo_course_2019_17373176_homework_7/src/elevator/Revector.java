package elevator;

import com.oocourse.elevator3.PersonRequest;

import java.util.Vector;

public class Revector {
    private Elevator ele;
    private Vector<PersonRequest> requestEle = new Vector<>(); // 当前电梯请求队列
    //private Controller controller;

    public Revector(Elevator ele) {
        this.ele = ele;
    }

    public void add(PersonRequest request) {
        synchronized (this) {
            requestEle.add(request);
            notifyAll();
        }
    }

    public synchronized PersonRequest get(int index) {
        return requestEle.get(index);
    }

    public synchronized int proRequest(int nowfloor) { //return equal nowfloor
        int i;
        for (i = 0; i < requestEle.size(); i++) {
            if (requestEle.get(i).getFromFloor() == nowfloor) {
                return i;
            }
        }
        return -1;
    }

    protected synchronized PersonRequest getRequest() { // return
        try {
            while (isEmpty()) {
                if (ele.getStop().getStop()) {
                    return null;
                } else {
                    wait();
                }
            }
            return requestEle.get(0);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    protected synchronized PersonRequest remove(int index) { //return and remove
        return requestEle.remove(index);
    }

    protected synchronized boolean isEmpty() {
        return requestEle.isEmpty();
    }
}
