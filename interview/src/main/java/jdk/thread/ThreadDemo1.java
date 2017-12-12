package jdk.thread;

import java.text.MessageFormat;

/**
 * Description 线程最基本的启动
 * Created by ygd on 2017/12/8.
 */
public class ThreadDemo1 {



    static class ThreadOne implements Runnable{

        public void run() {
            System.out.println(MessageFormat.format("{0}开始执行了",Thread.currentThread().getName()));
        }
    }

    public static void main(String[] args) {
        ThreadOne threadOne = new ThreadOne();
        Thread thread0 = new Thread(threadOne,"线程0");
        Thread thread1 = new Thread(threadOne,"线程1");
        Thread thread2 = new Thread(threadOne,"线程2");

        thread0.start();
        thread1.start();
        thread2.start();
    }
}
