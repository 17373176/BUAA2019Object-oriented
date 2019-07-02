//CalNum.java
package demo1;

public class CalNum {

    static final int counterNum = 10;
    static final int maxNum = 100;

    /** 测试程序入口**/
    public static void main(String[] args) {
        //生产者消费者线程锁
        Object producerMonitor = new Object();
        Object consumerMonitor = new Object();

        //设定一个足够大100的容器
        Container<Customer> container = new Container<Customer>(maxNum);

        //生产者线程启动
        new Thread(new Producer(producerMonitor,consumerMonitor,container)).start();
        //消费者线程启动，模拟10个柜台
        for (int i = 1;i <= counterNum;i++) {
            new Thread(new Consumer(producerMonitor,consumerMonitor,container,i)).start();
        }
    }
}