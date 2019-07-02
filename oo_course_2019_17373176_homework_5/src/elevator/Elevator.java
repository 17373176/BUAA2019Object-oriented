package elevator;

import com.oocourse.elevator1.PersonRequest;

import java.util.LinkedList;
import java.util.Queue;

public class Elevator extends Thread {
    private Queue<PersonRequest> requestQ;

    public Elevator(Queue<PersonRequest> requestQ) {
        this.requestQ = requestQ;
    }

    private int nowFloor = 1; // nowfloor

    @Override
    public void run() {
        // loop for running
        while (!this.requestQ.isEmpty()) {
            PersonRequest request = ((LinkedList<PersonRequest>)
                    this.requestQ).poll();
            RunElevator  person = new RunElevator(request.getPersonId(),
                    request.getFromFloor(), request.getToFloor(), nowFloor);
            try {
                nowFloor = person.Run();
            } catch (InterruptedException e) {
                System.out.println(e);
            }
        }
    }
}
