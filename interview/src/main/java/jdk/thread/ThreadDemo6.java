package jdk.thread;

/**
 * Description 学习interrupt
 * Created by ygd on 2017/12/11.
 *
 * 通过外部设置变量，来中断线程
 * 因为当线程内部执行循环逻辑等，不能执行到wait 、sleep 、join 时，即使设置了interrupt也是不能中断线程抛出异常的
 * 这个时候，就需要退出循环，继续向下执行，执行到wait 、sleep 、join 然后抛出InterruptedException
 */
public class ThreadDemo6 {


    public static void main(String[] args) throws InterruptedException {
        Monitor monitor = new Monitor() ;

        ThreadA threadA1 = new ThreadA(monitor);
        threadA1.setThreadName("线程A1");
//        ThreadA threadA2 = new ThreadA(monitor);
//        threadA2.setThreadName("线程A2");
        ThreadB threadB = new ThreadB(threadA1,"线程B1");
        threadA1.start();
        Thread.sleep(1000);
//        threadA2.start();
        Thread thread = new Thread(threadB);
        thread.start();
    }


    public static class ThreadA extends Thread{

        public ThreadA(Monitor monitor){
            this.monitor = monitor ;
        }
        private Monitor monitor ;
        private String threadName ;
        private volatile Boolean interruptFlag = false;
        @Override
        public void run() {
            super.run();
            synchronized (this.monitor){
                try {
                    System.out.println("线程A开始执行呢"+this.getThreadName());
                    for(int i = 0 ;i <100000 ; i++){
                        if(this.getInterruptFlag()){
                            System.out.println("interruptFlag"+this.getInterruptFlag());
                            System.out.println("要中断了");
                            this.setInterruptFlag(false);
                            break;
                        }
                    }
                    this.monitor.wait();// 这是重点， wait的对象和synchronized要是同一个对象， 切记！！！切记！！！
                    System.out.println("线程A被wait了"+this.getThreadName());
                } catch (InterruptedException e) {
                    System.out.println("ThreadA被中断，抛出异常了"+this.getThreadName());
                    e.printStackTrace();
                }
            }
        }

        public String getThreadName() {
            return threadName;
        }

        public void setThreadName(String threadName) {
            this.threadName = threadName;
        }

        public Boolean getInterruptFlag() {
            return interruptFlag;
        }

        public void setInterruptFlag(Boolean interruptFlag) {
            this.interruptFlag = interruptFlag;
        }
    }


    public static class ThreadB implements Runnable{

        public ThreadB(ThreadA threadA , String threadName){
            this.threadA = threadA ;
            this.threadName = threadName ;
        }

        private ThreadA threadA ;

        private String threadName ;

        public void run() {
            System.out.println("线程B开始执行了"+this.getThreadName());
            threadA.interrupt();
            threadA.setInterruptFlag(true);
            System.out.println("线程B中断了线程A之后，继续执行");
        }

        public ThreadA getThreadA() {
            return threadA;
        }

        public void setThreadA(ThreadA threadA) {
            this.threadA = threadA;
        }

        public String getThreadName() {
            return threadName;
        }

        public void setThreadName(String threadName) {
            this.threadName = threadName;
        }
    }

    public static class Monitor{}


}
