package jdk.baseType;

/**
 * Byte 中基本上都是调用的Integer中的方法
 * http://blog.csdn.net/liangzi321321/article/details/70197767
 * Description Byte类的源码学习记录
 * Created by ygd on 2017/12/7.
 */
public class JdkByte {

    public static void main(String[] args) {
        Byte b = new Byte("1");
        System.out.println(b);
        byte s = 1 ;
        Byte b1 = new Byte(s);
    }
}
