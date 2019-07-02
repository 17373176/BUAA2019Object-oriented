package elevator;
//interface

public interface SchedulableCarrier {
    public static final double moveTime = 0.5;
    public static final double callTime = 1.0;

    public abstract boolean moveUp();

    public abstract boolean moveDown();

    public abstract boolean callOpenAndClose();

}