package jdk.baseType;

/**
 *
 * 这里主要调用的都是native方法，没什么能看到的，还有toString是调用的sun.misc 包里的东西， 也没有必要看
 *
 *
 * 对于float和double数据类型精度丢失问题，在‘Effective Java’一书中也提到：float和double只能用来做科学计算或者工程计算（why?），
 * 在商业计算中我们要用java.math.BigDecimal。如果我们需要精确计算，非要用String来构建BigDecimal不可！
 *
 * http://blog.csdn.net/bonjean/article/details/51475363
 *
 *
 * float 和 double 都存在减法减不近的情况，例如如下main函数中的
 *
 * Description Float类的源码学习记录
 * Created by ygd on 2017/12/7.
 */
public class JdkFloat {

    public static void main(String[] args) {
        System.out.println(12.0f-11.9f);
        System.out.println(12.0d-11.9d);
    }
}
