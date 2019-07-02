package implement;

import com.oocourse.uml2.interact.AppRunner;

public class Main {
    public static void main(String[] args) throws Exception {
        AppRunner runner = AppRunner.newInstance(
                MyUmlGeneralInteraction.class);
        runner.run(args);
    }
}

