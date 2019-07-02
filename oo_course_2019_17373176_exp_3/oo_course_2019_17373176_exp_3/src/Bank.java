import java.util.Objects;

public class Bank {
    // 假设一个账户有5000块钱
    static final double totalMoney = 5000;

    private double balance = totalMoney;

    public double getBalance() {
        return balance;
    }

    // 柜台Counter取钱
    public void Counter(double money) {
        this.balance -= money;
        System.out.println("Counter 取走了" + money + "还剩下" + (this.balance));
    }

    // ATM取钱
    public void ATM(double money) {
        this.balance -= money;
        System.out.println("ATM 取走了" + money + "还剩下" + (this.balance));
    }

    // 手机App取钱
    public void App(double money) {
        this.balance -= money;
        System.out.println("App 取走了" + money + "还剩下" + (this.balance));
    }

    public synchronized void withdraw(double money, String mode) throws Exception {
        if (money > this.balance) {
            throw new Exception(mode + "请求取款金额" +  money
                    + ",余额只剩" + this.balance + "," + "取款失败");
        }
        else if (Objects.equals(mode,"Counter")) {
            Counter(money);
        }
        else if (Objects.equals(mode,"ATM")) {
            ATM(money);
        }
        else {
            App(money);
        }
    }
}