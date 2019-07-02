package elevator;

import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.PersonRequest;

public class Input implements Runnable {
    private ElevatorInput eleInput = new ElevatorInput(System.in);
    private StopElevator stop = new StopElevator();
    private Controller controller = new Controller(stop);
    private Elevator elevator = new Elevator(controller);

    @Override
    public void run() {
        Thread ele = new Thread(elevator);
        ele.start();
        try {
            int count = 0;
            while (true) {
                PersonRequest request = eleInput.nextPersonRequest();
                count++;
                if (request == null || count > 30) {
                    break;
                } else {
                    controller.add(request);
                }
            }
            eleInput.close();
            stop.runStop(); //stop flag
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}