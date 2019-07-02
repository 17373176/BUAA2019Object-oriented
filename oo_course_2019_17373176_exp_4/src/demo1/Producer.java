//Producer.java
package demo1;

import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * 生产者
 */
public class Producer implements Runnable {
    //生产容器
    private final Container<Customer> container;
    // 监听生产者线程
    private final Object producerMonitor;
    //监听消费者线程
    private final Object consumerMonitor;
    static final int produceSpeed = 300;



    public Producer(Object producerMonitor,Object consumerMonitor,
                    Container<Customer> container){
        this.producerMonitor = producerMonitor;
        this.consumerMonitor = consumerMonitor;
        this.container = container;
    }

    @Override
    public void run() {
        try {
            while (true) {
                produce();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //取号机生产等待的客户，注意模拟前后两个客户之间的时间间隔
    public void produce() throws InterruptedException {
        synchronized (this) {
            if (!container.isFull()) {
                Customer cus = new Customer();
                container.add(cus);
                System.out.println(String.format("%s号顾客正在等待服务",cus.toString()));
                Thread.sleep(produceSpeed);
                //notifyAll();
            } else {
                System.out.println(String.format("大厅容量已满暂停发号"));
                wait();
            }
        }
    }
}