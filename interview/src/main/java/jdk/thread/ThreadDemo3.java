package jdk.thread;

/**
 * Description sleep 学习
 * Created by ygd on 2017/12/11.
 *
 * sleep() 就是让线程停止多少毫秒，不执行后续操作，直到达到时间，sleep会阻塞线程，会一直占用这synchronized锁，但是释放CPU资源
 */
public class ThreadDemo3 {


    public static void main(String[] args) {
        ThreadOne threadOne = new ThreadOne();
        Thread thread1 = new Thread(threadOne,"thread1");
        Thread thread2 = new Thread(threadOne,"thread2");
        Thread thread3 = new Thread(threadOne,"thread3");
        Thread thread4 = new Thread(threadOne,"thread4");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }

    public static class ThreadOne implements Runnable{

        public void run() {
            synchronized (this){
                System.out.println("线程"+Thread.currentThread().getName()+"开始执行");
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("线程"+Thread.currentThread().getName()+"结束执行");
            }
        }
    }
}
