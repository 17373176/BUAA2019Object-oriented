package elevator;

import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;
import com.oocourse.TimableOutput;

import java.util.LinkedList;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws Exception {
        ElevatorInput eleInput = new ElevatorInput(System.in);
        Queue<PersonRequest> requestQ = new LinkedList<PersonRequest>();
        while (true) {
            PersonRequest request = eleInput.nextPersonRequest();
            // when request == null
            // it means there are no more lines in stdin
            if (request == null) {
                break;
            } else {
                // a new valid request
                // add to queue
                ((LinkedList<PersonRequest>) requestQ).add(request);
            }
        }
        // initialize start timestamp at the beginning
        TimableOutput.initStartTimestamp();
        // elevator thread
        Elevator ele = new Elevator(requestQ);
        new Thread(ele).start();
        eleInput.close();
        ele.stop();
    }
}
