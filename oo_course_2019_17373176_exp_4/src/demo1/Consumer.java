//Consumer.java
package demo1;

import java.util.Random;
import java.util.concurrent.TimeUnit;
// 消费者
public class Consumer implements Runnable {
    private int windowId;
    //模拟生产容器
    private final Container<Customer> container;
    //监听生产者线程
    private final Object producerMonitor;
    //监听消费者线程
    private final Object consumerMonitor;
    static final int consumeSpeed = 10000;

    public Consumer( Object producerMonitor,Object consumerMonitor,
                     Container<Customer> container,int id ) {
        this.producerMonitor = producerMonitor;
        this.consumerMonitor = consumerMonitor;
        this.container = container;
        this.windowId = id;
    }

    @Override
    public void run() {
        try {
            while (true) {
                consume();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //消费
    public void consume() throws InterruptedException {
        synchronized (this.container) {
            if (!container.isEmpty()) {
                Customer cus = container.get();
                System.out.println(String.format("%s号顾客请到%s号窗口",cus.toString(),windowId));
                //cus.count();
                Random rand = new Random();
                TimeUnit.MILLISECONDS.sleep(rand.nextInt(10000));
                //Thread.sleep(consumeSpeed);
            } else {
                wait();
            }
        }
    }
}