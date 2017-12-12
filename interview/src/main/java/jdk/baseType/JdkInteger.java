package jdk.baseType;

import java.lang.reflect.Field;

/**
 * 知识点1：java数据传递的方式
 *  java有两种数据传递的方法，分别是值传递、引用传递
 *
 * 知识点2： 什么是值传递
 *  值传递，就是传递的是这个值的一个副本，传递的并不是本身，因此，我们修改了方法里面的变量的值，并不会改变外面的值
 *
 * 知识点3：什么是引用传递
 *  引用传递，顾名思义，传递的是引用，也就是地址，传递的是指向值的地址的指针，java中没有指针的概念，所以传递的就是指向值的地址
 *  什么情况是引用传递，一般对象都是引用传递，集合是引用传递，但并是不所有的对象都是引用传递，例如Integer ,String,Double 还有其他几个基本类型的封装类， 因为他们都是fianl的，不能被改变
 *
 *
 * 知识点4：拆箱和装箱
 *  所谓装箱，就是把基本类型用他们相对应的引用类型包装起来，使他们可以具有对象的特质，如我们可以把int型包装成Integer类的对象，或者把double包装成Double等等。
 *  所谓拆箱，就是跟装箱的方向相反，将Integer及Double这样的引用类型的对象重新简化为值类型的数据。
 *  J2SE5.0后提供了自动装箱和拆箱的功能，此功能事实上是编译器来帮忙，编译器在编译时依照编写的方法，决定是否进行装箱或拆箱动作。
 *  自动装箱的过程：每当需要一种类型的对象时，这种基本来兴就自动的封装到与它相同类型的包装中。
 *  自动拆箱的过程：每当需要一个值时，被装箱对象中的值就被自动的提取出来，没必要再去调用intValue()和doubleValue()方法。
 *  自动装箱只需将该值赋给一个类型包装器引用，java会自动创建一个对象
 *
 *
 * 知识点5：toString()方法， 这个方法写的非常好，值得详细看看 TODO
 * Description Integer类的源码学习记录
 * Created by ygd on 2017/12/6.
 */
public class JdkInteger {

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        JdkInteger.swapTwoIntegerValue();
        JdkInteger.integerCompare();
    }

    /**
     * 交换两个integer的值
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static void swapTwoIntegerValue() throws NoSuchFieldException, IllegalAccessException {
        Integer a = 1 ;
        Integer b = 2 ;
        System.out.println("before swap:a="+a+",b="+b);
        swap(a,b);
        System.out.println("after swap:a="+a+",b="+b);
    }

    /**
     * 主方法第一了两个Integer的变量，通过调用swap方法后，
     * 使这个两个变量交互数据，然后控制台打印交换前和交换后的值
     * @param a
     * @param b
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    private static void swap(Integer a , Integer b) throws NoSuchFieldException, IllegalAccessException {
        Field field = Integer.class.getDeclaredField("value");
        field.setAccessible(true);
        int temp = a.intValue();
        field.set(a,b);
        field.set(b,new Integer(temp));
    }

    /**
     * 首先要明确一点，对象之间的==是比较内存地址，常数之间的比较是数值比较。
     * integer 的各种比较输出的结果， 比较全了
     */
    private static void integerCompare() {
        Integer num1 = new Integer(100);
        Integer num2 = new Integer(100);
        System.out.println(num1 == num2);//false,因为这两个对象是独立创建的，有自己的内存空间和地址。
        Integer num3 = 100;
        Integer num4 = 100;
        System.out.println(num3 == num4);//true，常数之间比较数值。
        Integer num5 = 128;
        Integer num6 = 128;
        System.out.println(num5 == num6);//false，自动装箱成对象，但是超过了默认的缓存范围，同第一个。如果是127就是true。
        Integer num7 = 100;
        Integer num8 = new Integer(100);
        System.out.println(num7 == num8);//false，两个对象之间比较内存地址，不同的是num7通过自动装箱调用valueOf方法，指向缓存的100，而num8是指向自己内存空间里的100.
        int num9 = 100;
        Integer num10 = new Integer(100);
        System.out.println(num9 == num10);//true，Integer对象和int比较时，Integer会自动拆箱（intValue方法）成为int，变成两个数值比较。
        Integer num11 = 100;
        System.out.println(num9 == num11);//true，num11通过自动装箱调用valueOf方法指向缓存中的100，比较的时候缓存中的100对象自动拆箱成为数值100.
    }

}
