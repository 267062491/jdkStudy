package jdk.thread

/**
 * Description 记录java多线程的学习过程
 * Created by ygd on 2017/12/8.
 *
 * 知识点1：线程的5个状态
 *  New(新建状态):
 *  Runnable(就绪状态):
 *  Running(运行状态):
 *  Block(阻塞状态): 所谓阻塞状态是正在运行的线程没有运行结束，暂时让出CPU，这时其他处于就绪状态的线程就可以获得CPU时间，进入运行状态。
 *      Block():调用join()和sleep()方法，sleep()时间结束或被打断，join()中断,IO完成都会回到Runnable状态，等待JVM的调度。
 *      Wait Block Pool(等待池):调用wait()，使该线程处于等待池(wait blocked pool),直到notify()/notifyAll()，线程被唤醒被放到锁定池(lock blocked pool )，释放同步锁使线程回到可运行状态（Runnable）
 *      Lock Block Pool(锁定池):对Running状态的线程加同步锁(Synchronized)使其进入(lock blocked pool ),同步锁被释放进入可运行状态(Runnable)
 *  Dead(死亡状态):
 *
 * 知识点2：线程切换状态用到的9个方法
 *  1、start():启动线程的唯一方法，是Thread类里的方法；使线程进入Runnable状态
 *  2、wait():Object类中的方法 ，使线程进入Wait Block Pool
 *      要确保调用wait()方法的时候拥有锁，即，wait()方法的调用必须放在synchronized方法或synchronized块中。wait()方法应该是被拥有对象的锁的线程所调用，***会释放掉线程拥有的锁***
 *  3、notify():Object类中的方法，是线程由Wait Block Pool 进入到 Lock Block Pool ，应用条件和wait()方法一样，和wait配合使用，***不会立刻会释放掉线程拥有的锁，当线程退出synchronized的时候，释放掉线程拥有的锁***
 *  4、notifyAll():Object类中的方法，是线程由Wait Block Pool 进入到 Lock Block Pool ，应用条件和wait()方法一样，和wait配合使用，***不会立刻会释放掉线程拥有的锁，当线程退出synchronized的时候，释放掉线程拥有的锁***
 *  5、interrupt():Thread类中的方法；
 *  6、join():Thread类中的方法，线程由running进入到Blocked状态；让“主线程”等待“子线程”结束之后才能继续运行;
 *  7、sleep():Thread类中的方法，线程由running进入到blocked状态，使当前线程阻塞一定的时间，如果在synchronized中，不会释放锁，但是会释放cpu资源给其他线程使用；当sleep时间结束后，线程由blocked进入到runnable状态
 *  8、yield():Thread类中的方法，线程由running进入到runnable状态，如果在synchronized中，不会释放锁，但是会释放cpu资源给其他线程使用
 *      如果你觉得一个线程不是那么重要，或者优先级非常低，而且又害怕它会占用太多的CPU资源，那么可以在适当的时候调用Thread.yield()，给予其他重要线程更多的工作机会。
 *      这个方法很不常用，甚至都可以不用
 *  9、run() : 是Runnable接口里的方法， Thread类实现了Runnable接口 ；该方法不能在外部调用，是启动线程之后线程内部自动调用的方法；使线程进入Running状态
 *  10、synchronized :这是一个关键字，我们也把他给归结到方法里吧，因为他也可以改变线程的状态，例如 ，线程试图得到一个锁，而该锁正被其他线程持有，那么线程没有获取到锁，就会进入Lock Block Pool 里面
 *
 *  知识点3：线程池  https://www.cnblogs.com/trust-freedom/p/6681948.html 好文章
 *      3.1、线程池的状态
     *      1、RUNNING：-1<<COUNT_BITS，即高3位为1，低29位为0，该状态的线程池会接收新任务，也会处理在阻塞队列中等待处理的任务
            2、SHUTDOWN：0<<COUNT_BITS，即高3位为0，低29位为0，该状态的线程池不会再接收新任务，但还会处理已经提交到阻塞队列中等待处理的任务
            3、STOP：1<<COUNT_BITS，即高3位为001，低29位为0，该状态的线程池不会再接收新任务，不会处理在阻塞队列中等待的任务，而且还会中断正在运行的任务
            4、TIDYING：2<<COUNT_BITS，即高3位为010，低29位为0，所有任务都被终止了，workerCount为0，为此状态时还将调用terminated()方法
            5、TERMINATED：3<<COUNT_BITS，即高3位为100，低29位为0，terminated()方法调用完成后变成此状态
            这些状态均由int型表示，大小关系为 RUNNING<SHUTDOWN<STOP<TIDYING<TERMINATED，这个顺序基本上也是遵循线程池从 运行 到 终止这个过程。
 *      3.2、线程池的参数
 *          int corePoolSize:核心线程池大小，线程池中的数量poolSize(workers.size()，workers就是一个map)超过这个数，则把线程放入workQueue
 *          int maximumPoolSize:最大线程数量，如果workQueue也已经满了，就会继续创建新的线程，直到poolSize=maximumPoolSize
 *          long keepAliveTime:线程存活时间
 *          TimeUnit unit:线程存活时间的单位
 *          BlockingQueue<Runnable> workQueue:阻塞的缓存队列,在创建的时候，根据实际需要传入需要的队列就可以
 *          ThreadFactory threadFactory:创建线程的工厂
 *              1、默认可以使用Executors里面的defaultThreadFactory
 *              2、也可是自己implements ThreadFactory创建工厂，在工厂中创建thread
 *          RejectedExecutionHandler handler:线程不执行时的拒绝策略
 *              1、系统自带的：CallerRunsPolicy：直接调用r.run()方法，好流氓啊，不排队了，直接走后面
 *              2、系统自带的：AbortPolicy：处理程序遭到拒绝将抛出运行时 RejectedExecutionException.这种策略直接抛出异常，丢弃任务。（jdk默认策略，队列满并线程满时直接拒绝添加新任务，并抛出异常，所以说有时候放弃也是一种勇气，为了保证后续任务的正常进行，丢弃一些也是可以接收的，记得做好记录）
 *              3、系统自带的：DiscardPolicy：不能执行的任务将被删除.DiscardPolicy：不能执行的任务将被删除
 *              4、系统自带的：DiscardOldestPolicy：如果执行程序尚未关闭，则位于工作队列头部的任务将被删除，然后重试执行程序（如果再次失败，则重复此过程）.该策略就稍微复杂一些，在pool没有关闭的前提下首先丢掉缓存在队列中的最早的任务，然后重新尝试运行该任务。这个策略需要适当小心
 *              5、自定义，自己实现RejectedExecutionHandler 接口:可以把这些不能被执行的写入缓存，然后监控线程池，当有空闲资源的 时候，在从缓存里取出来执行
 *
 *
 *  知识点4：threadLocal
 *
 *  知识点五：锁 、lock ，reentrantLock
 *
 *  知识点六：Atomic原子类
 *
 *  知识点七：Concurrent容器
 *
 *  知识点八：设计一个多线程的程序，并实现
 *
 *
 */
