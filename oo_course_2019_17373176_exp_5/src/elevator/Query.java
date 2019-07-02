package elevator;

public class Query {

    private /*@ spec_public @*/ static double inftyTime = 1e12;
    private /*@ spec_public @*/ int targetFloor;
    private /*@ spec_public @*/ double queryTime;
    private /*@ spec_public @*/ Direction queryDirection;

    enum Direction { UP, DOWN, NONE }

    /*@ assignable targetFloor,queryTime,queryDirection;
      @ ensures
      @ this.targetFloor == target && this.queryTime == time && this.queryDirection == direction;
      @*/
    public Query(int target, double time, Direction direction) {
        this.targetFloor = target;
        this.queryTime = time;
        this.queryDirection = direction;
    }

    /*@
      @ assignable this.targetFloor,this.queryTime,this.queryDirection;
      @ ensures
      @ this.targetFloor == target && this.queryTime == time && this.queryDirection == Direction.NONE;
      @*/
    public Query(int target, double time) {
        this(target, time, Direction.NONE);
    }

    public Query(String buf) throws Throwable {
        // buf Format : (FR/ER,num,UP/DOWN/NONE,time)
        int target;
        double time;
        Direction direction;
        String[] str = buf.split("[(,)]");
        if ("ER".equals(str[1]) != "NONE".equals(str[3])) {
            // only when str[1] is "ER", str[3] is "NONE"
            throw new Exception("Invalid Character Or Format.");
        }
        try {
            target = Integer.parseInt(str[2]);
        } catch (NumberFormatException except) {
            throw new Exception("Floor Number Out Of Range.");
        }
        if (str[3].equals("UP")) {
            direction = Direction.UP;
        }
        else if (str[3].equals("DOWN")) {
            direction = Direction.DOWN;
        }
        else if (str[3].equals("NONE")) {
            direction = Direction.NONE;
        }
        else {
            throw new Exception("Invalid Character Or Format.");
        }
        try {
            time = Long.parseLong(str[4]);
        } catch (NumberFormatException except) {
            throw new Exception("Time Number Out Of Range.");
        }
        targetFloor = target;
        queryTime = time;
        queryDirection = direction;
    }

    public String toString() {
        if (queryDirection == Direction.NONE) {
            return "(ER, " + targetFloor + ", " + queryTime + ")";
        } else {
            return "(FR, " + targetFloor + ", "
                    + queryDirection + ", " + queryTime + ")";
        }
    }

    public /*@ pure @*/ int getTarget() {
        return this.targetFloor;
    }

    public /*@ pure @*/ double getTime() {
        return this.queryTime;
    }

    public /*@ pure @*/ Direction getDirection() {
        return this.queryDirection;
    }
}