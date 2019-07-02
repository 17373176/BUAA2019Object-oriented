package elevator;

import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.PersonRequest;

public class Input implements Runnable {
    private ElevatorInput eleInput = new ElevatorInput(System.in);
    private StopElevator stop = new StopElevator();
    private Controller controller = new Controller(stop);
    private Elevator elevatorA = new Elevator(controller, 400, 6, "A", stop);
    private Elevator elevatorB = new Elevator(controller, 500, 8, "B", stop);
    private Elevator elevatorC = new Elevator(controller, 600, 7, "C", stop);
    private Runele runele = new Runele(controller, elevatorA,
            elevatorB, elevatorC);

    @Override
    public void run() {
        stop.get(elevatorA,elevatorB,elevatorC);
        Thread eleA = new Thread(elevatorA);
        Thread eleB = new Thread(elevatorB);
        Thread eleC = new Thread(elevatorC);
        eleA.start();
        eleB.start();
        eleC.start();
        try {
            int count = 0;
            while (true) {
                PersonRequest request = eleInput.nextPersonRequest();
                count++;
                if (request == null || count > 50) {
                    break;
                } else {
                    controller.add(request);
                    runele.deal();
                }
            }
            eleInput.close();
            //stop flag
            stop.runStop();
            getNotify();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void getNotify() {
        notifyAll();
    }
}