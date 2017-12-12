package jdk.thread;

import java.text.MessageFormat;

/**
 * Description 学习wait 和 notify
 * Created by ygd on 2017/12/11.
 *
 *
 * 1、wait 和 notify/notifyAll 方法必须在synchronized 中执行
 * 2、wait方法执行之后，立刻释放当前线程只有的锁，wait方法之后的代码不继续执行，jvm虚拟机记录线程当前执行的位置，下次获得锁之后，继续从改位置执行
 * 3、notify/notifyAll方法，唤醒其他线程继续执行，但是并不会立刻释放当前线程持有的锁，直到当前线程退出synchronized,才会释放线程锁，
 *      也就是notify后的代码并且在synchronized中会继续执行
 *      如果是在notify后，但是不在synchronized中的代码，就不能保证一定会顺序执行，因为当程序退出synchronized，锁已经被释放，其他线程就有机会抢占到cpu资源，就有机会获取到synchronized了
 * 4、monitor的概念，Java中的每个对象都有一个监视器，来监测并发代码的重入。在非多线程编码时该监视器不发挥作用，反之如果在synchronized 范围内，监视器发挥作用。
 *      wait/notify必须存在于synchronized块中。并且，这三个关键字针对的是同一个监视器（某对象的监视器）。这意味着wait之后，其他线程可以进入同步块执行。
 *      当某代码并不持有监视器的使用权时（如图中5的状态，即脱离同步块）去wait或notify，会抛出java.lang.IllegalMonitorStateException。也包括在synchronized块中去调用另一个对象的wait/notify，因为不同对象的监视器不同，同样会抛出此异常。
 *
 * 论坛帖子：
 * notify() 只是去通知其他的线程，但是synchronized 方法里面的代码还是会执行完毕的。
    synchronized方法本来就加了锁。代码的执行跟你的notify()也无关，代码的执行是跟你的synchronized绑定一起而已。
 追问
 方法调用notify之后就会释放锁，此时两个线程同时争夺锁，如果上面的线程没抢到，return不是执行不了了吗
 追答
 执行了notify方法后，虽然释放了锁，但是该线程正执行着这个方法，持有着这把锁，其他线程虽被唤醒，但是仍无法获得锁。直到该线程退出synchronized这个方法（即执行完），其他线程才有机会去抢夺，去获得锁继续执行。
 追问
 《虽然释放了锁，但是该线程正执行着这个方法，持有着这把锁》不矛盾吗
 追答
 应该这么说，该线程调用notify的时候，该线程正持有锁，因此，其他线程虽被唤醒，但是仍无法获得锁。直到该线程退出synchronized这个方法（即执行完），释放锁后，其他线程才有机会去抢夺，去获得锁继续执行。
 */
public class ThreadDemo2 {


    public static void main(String[] args) throws InterruptedException {
        Goods goods=new Goods();
        Pruducter pruducter = new Pruducter();
        Consumer consumer = new Consumer();
        pruducter.setGoods(goods);
        consumer.setGoods(goods);

        Thread pruductThread = new Thread(pruducter);
        Thread consumerThread = new Thread(consumer);

        pruductThread.start();
        Thread.sleep(1000);
        consumerThread.start();
    }


    public static class Pruducter implements Runnable{

        private Goods  goods ;

        public Goods getGoods() {
            return goods;
        }

        public void setGoods(Goods goods) {
            this.goods = goods;
        }

        public void run() {
            for (int i = 0 ; i<5 ;i++){
                synchronized (this.getGoods()){
                    if(this.getGoods().getNum()>0){
                        try {
                            System.out.println("当前商品太多了，我要休息一会了"+i);
                            this.getGoods().wait();// 这里特别要注意， synchronized的对象和wait的对象要是一个对象，要不然就报错（IllegalMonitorStateException） ， 因为wait是Object的方法
                            System.out.println("消费者告诉我，商品没有了， 我要开始干活了"+i);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    if(i%2==0){
                        System.out.println("消费者告诉我，商品没有了， 我要开始干活了，生产水开始"+i);
                        goods.setPinPai("哇哈哈");
                        try {
                             Thread.sleep(1000);
                         } catch (InterruptedException e) {
                             e.printStackTrace();
                         }
                        goods.setName("矿泉水");
                        System.out.println("消费者告诉我，商品没有了， 我要开始干活了，生产水结束"+i);
                    }else{
                        System.out.println("消费者告诉我，商品没有了， 我要开始干活了，生产小馒头开始"+i);
                        goods.setPinPai("旺仔");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        goods.setName("小馒头");
                        System.out.println("消费者告诉我，商品没有了， 我要开始干活了，生产小馒头结束"+i);
                    }

                    goods.setNum(goods.getNum()+1);
                    System.out.println(MessageFormat.format("生产了{0}{1}",this.getGoods().getPinPai(),this.getGoods().getName())+i);
                    this.getGoods().notify();
                    try {
                        System.out.println("notify之后，的系统还会执行么,sleep之前"+i);
                        Thread.sleep(1000);
                        System.out.println("notify之后，的系统还会执行么"+i);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                System.out.println("synchronized外，notify之后，的系统还会执行么,sleep之前"+i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("synchronized外，notify之后，的系统还会执行么"+i);
            }
        }
    }


    public static class Consumer implements Runnable{

        private Goods goods ;

        public Goods getGoods() {
            return goods;
        }

        public void setGoods(Goods goods) {
            this.goods = goods;
        }

        public void run() {
            for(int i=0 ; i<5; i++){
                synchronized (this.getGoods()){
                    System.out.println("消费者开始消费了");
                    if(this.getGoods().getNum()<=0){
                        try {
                            this.getGoods().wait();// 这可是重点啊 这里wait的是 goods ,wait方法是object的
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    this.getGoods().setNum(this.getGoods().getNum()-1);
                    System.out.println("取走了"+this.getGoods().getPinPai()+this.getGoods().getName());
                    this.getGoods().notify();
                }
            }
        }
    }

    public static class Goods{
        private String pinPai ;

        private String name ;

        private Integer num = 0 ;

        public String getPinPai() {
            return pinPai;
        }

        public void setPinPai(String pinPai) {
            this.pinPai = pinPai;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }
    }

}
