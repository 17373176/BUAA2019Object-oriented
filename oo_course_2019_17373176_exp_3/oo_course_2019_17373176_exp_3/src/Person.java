public class Person extends Thread {

    static final long sleepTime = 100;

    // 创建银行对象
    private Bank bank;
    private String mode;
    private double money;

    // 通过构造器传入银行对象
    public Person(Bank bank, String mode, double money) {
        this.bank = bank;
        this.mode = mode;
        this.money = money;
    }

    //重写run方法，实现取钱操作
    @Override
    public void run() {
        while (bank.getBalance() > 0) {
            try {
                bank.withdraw(this.money,mode);
            }
            catch (Exception e1) {
                e1.printStackTrace();
            }
            try {
                Thread.sleep(sleepTime);// 取完休息
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}