package jdk.thread;

/**
 * Description interrupt() 学习记录
 * Created by ygd on 2017/12/11.
 * 中断sleep
 * 在ThreadTwo里面，调用了threadSleep的interrupt方法，导致sleep线程被中断，并在sleep线程中抛出InterruptedException
 * 哪个线程被中断，哪个线程抛出异常
 *
 * 知识点1：sleep,wait,join 是哪个方法阻塞时，进行中断
 * interrupt() 方法只是改变中断状态而已，它不会中断一个正在运行的线程。
 * 这一方法实际完成的是，给受阻塞的线程发出一个中断信号，这样受阻线程就得以退出阻塞的状态。
 * 更确切的说，如果线程被Object.wait, Thread.join和Thread.sleep三种方法之一阻塞，此时调用该线程的interrupt()方法，
 * 那么该线程将抛出一个 InterruptedException中断异常（该线程必须事先预备好处理此异常），从而提早地终结被阻塞状态。
 * 如果线程没有被阻塞，这时调用 interrupt()将不起作用，直到执行到wait(),sleep(),join()时,才马上会抛出 InterruptedException。
   线程A在执行sleep,wait,join时,线程B调用线程A的interrupt方法,的确这一个时候A会有 InterruptedException 异常抛出来。
   但这其实是在sleep,wait,join这些方法内部会不断检查中断状态的值,而自己抛出的InterruptedException。
   如果线程A正在执行一些指定的操作时如值,for,while,if,调用方法等,不会去检查中断状态,则线程A不会抛出 InterruptedException,而会一直执行着自己的操作，
   这种情况下就需要人工去调用

 注意:
 当线程A执行到wait(),sleep(),join()时,抛出InterruptedException后，中断状态已经被系统复位了，线程A调用Thread.interrupted()返回的是false。
 如果线程被调用了interrupt()，此时该线程并不在wait(),sleep(),join()时，下次执行wait(),sleep(),join()时，一样会抛出InterruptedException，当然抛出后该线程的中断状态也会被系统复位。
 （总结一下：调用interrupt()方法，立刻改变的是中断状态，但如果不是在阻塞态，就不会抛出异常；如果在进入阻塞态后，中断状态为已中断，就会立刻抛出异常）
 *
 * 中断线程最好的，最受推荐的方式是，使用共享变量（shared variable）发出信号，告诉线程必须停止正在运行的任务。
 * 线程必须周期性的核查这一变量（尤其在冗余操作期间），然后有秩序地中止任务,请确认将共享变量定义成volatile
 *
 * 知识点2:IO阻塞进行中断 TODO 这个还没找到好的资料和例子，这可以往后放放，和java io socket一起看
 *
 * 知识点3:
 */
public class ThreadDemo5 {

    public static void main(String[] args) throws InterruptedException {
        ThreadOne threadOne = new ThreadOne();
        ThreadTwo threadTwo = new ThreadTwo(threadOne,"线程2");
        Thread thread2 = new Thread(threadTwo);
        threadOne.start();
        Thread.sleep(1000);
        thread2.start();

    }

    public static class ThreadOne extends Thread{

        public void run() {
            try {
                System.out.println("sleep线程开始执行了"+Thread.currentThread().getName());
                Thread.sleep(100000);
                System.out.println("sleep的时候进行了Interrupt,那么这还执行么"+Thread.currentThread().getName());
            } catch (InterruptedException e) {
                System.out.println("sleep被中断了"+Thread.currentThread().getName());
                e.printStackTrace();
            }
        }
    }


    public static class ThreadTwo implements Runnable{

        private ThreadOne threadOne ;

        private String threadName ;

        public ThreadTwo(ThreadOne threadOne , String threadName){
            this.threadOne = threadOne ;
            this.threadName = threadName ;
        }
        public void run() {
            System.out.println("ThreadTwo线程正在执行"+this.getThreadName());
            threadOne.interrupt();
            System.out.println("ThreadTwo线程执行了ThreadOne的interrupt"+this.getThreadName());
        }

        public ThreadOne getThreadOne() {
            return threadOne;
        }

        public void setThreadOne(ThreadOne threadOne) {
            this.threadOne = threadOne;
        }

        public String getThreadName() {
            return threadName;
        }

        public void setThreadName(String threadName) {
            this.threadName = threadName;
        }
    }

}
