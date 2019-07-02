package elevator;

class UnsortedException extends Exception {
    private static final long serialVersionUID = 1L;

    public UnsortedException(String message) {
        super(message);
    }
}