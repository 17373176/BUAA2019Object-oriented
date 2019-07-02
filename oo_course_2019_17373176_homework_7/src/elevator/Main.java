package elevator;

import com.oocourse.TimableOutput;

public class Main {
    public static void main(String[] args) {
        // initialize start timestamp at the beginning
        TimableOutput.initStartTimestamp();
        Input input = new Input();
        new Thread(input).start();
    }
}
