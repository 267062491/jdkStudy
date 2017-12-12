package jdk.thread;

/**
 * Description join() 方法学习
 * Created by ygd on 2017/12/11.
 *
 * join():让“主线程”等待“子线程”结束之后才能继续运行。
 */
public class ThreadDemo4 {

    public static void main(String[] args) throws InterruptedException {
        ThreadOne threadOne = new ThreadOne();
        Thread thread = new Thread(threadOne,"线程1");
        Thread thread2 = new Thread(threadOne,"线程2");
        System.out.println("主线程开始执行");
        thread.start();
        thread.join();
        thread2.start();
        thread2.join();
        System.out.println("主线程继续执行");
    }

    public static class ThreadOne implements Runnable{

        public void run() {
            System.out.println("子线程开始执行"+Thread.currentThread().getName());

            try {

                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("子线程继续执行"+Thread.currentThread().getName());
        }
    }
}
