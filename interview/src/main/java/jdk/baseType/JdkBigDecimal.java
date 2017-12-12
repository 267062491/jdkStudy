package jdk.baseType;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 *
 * 借用《Effactive Java》这本书中的话，float和double类型的主要设计目标是为了科学计算和工程计算。
 * 他们执行二进制浮点运算，这是为了在广域数值范围上提供较为精确的快速近似计算而精心设计的。
 * 然而，它们没有提供完全精确的结果，所以不应该被用于要求精确结果的场合。
 * 但是，商业计算往往要求结果精确，这时候BigDecimal就派上大用场啦。
 * (1)商业计算使用BigDecimal。
 * (2)尽量使用参数类型为String的构造函数。
 * (3) BigDecimal都是不可变的（immutable）的，在进行每一步运算时，都会产生一个新的对象，所以在做加减乘除运算时千万要保存操作后的值。
 * 缺点：计算速度上稍微慢了一些
 *
 * Description BigDecimal 类的源码学习记录
 * Created by ygd on 2017/12/7.
 */
public class JdkBigDecimal {

    public static void main(String[] args) throws IllegalAccessException {
        System.out.println(JdkBigDecimal.decimalFormat(JdkBigDecimal.divWithModel(6,10,4,BigDecimal.ROUND_UNNECESSARY),"0.0000"));
    }


    // 除法运算默认精度
    private static final int DEF_DIV_SCALE = 10;

    private JdkBigDecimal() {

    }

    /**
     * 精确加法
     */
    public static double add(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.add(b2).doubleValue();
    }

    /**
     * 精确减法
     */
    public static double sub(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.subtract(b2).doubleValue();
    }

    /**
     * 精确乘法
     */
    public static double mul(double value1, double value2) {
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.multiply(b2).doubleValue();
    }

    /**
     * 精确除法 使用默认精度
     */
    public static double div(double value1, double value2) throws IllegalAccessException {
        return div(value1, value2, DEF_DIV_SCALE);
    }

    /**
     * 精确除法
     * @param scale 精度
     */
    public static double div(double value1, double value2, int scale) throws IllegalAccessException {
        if(scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 除法，指定保留小数位数 ， 指定除不尽的情况的时候保留小数的模式
     * @param value1 除数
     * @param value2 被除数
     * @param scale 小数保留位数
     * @param model 除不尽的时候的保留小数的模式
     *  http://blog.csdn.net/growing_tree/article/details/51888689  参加这个文章
    *   public final static int ROUND_UP =           0;  // 不常用， 除了0 ，直接进位+1
        public final static int ROUND_DOWN =         1;  // 常用， 直接舍弃，不管是几
        public final static int ROUND_CEILING =      2;  // 不常用， 正数直接进位+1 ，负数直接舍弃，  如果是0，直接舍弃
        public final static int ROUND_FLOOR =        3;  // 不常用，和2相反， 负数直接进位+1 ，整数直接舍弃 ， 如果是0，直接舍弃
        public final static int ROUND_HALF_UP =      4;  // 常用， 四舍五入
        public final static int ROUND_HALF_DOWN =    5;  // 不常用， 五舍六入
        public final static int ROUND_HALF_EVEN =    6;  // 不常用，银行家舍入法 ，没太看明白
        public final static int ROUND_UNNECESSARY =  7;  // 不常用，除不尽就跑异常  java.lang.ArithmeticException: Rounding necessary
     * @return 返回计算后的结果
     * @throws IllegalAccessException
     */
    public static double divWithModel(double value1, double value2, int scale,int model) throws IllegalAccessException {
        if(scale < 0) {
            throw new IllegalAccessException("精确度不能小于0");
        }
        BigDecimal b1 = BigDecimal.valueOf(value1);
        BigDecimal b2 = BigDecimal.valueOf(value2);
        return b1.divide(b2, scale, model).doubleValue();
    }

    /**
     * 四舍五入
     * @param scale 小数点后保留几位
     */
    public static double round(double v, int scale) throws IllegalAccessException {
        return div(v, 1, scale);
    }

    /**
     * 比较大小
     */
    public static boolean equalTo(BigDecimal b1, BigDecimal b2) {
        if(b1 == null || b2 == null) {
            return false;
        }
        return 0 == b1.compareTo(b2);
    }

    /**
     * 把double数据强制保留几位小数
     * @param value 需要格式化的值
     * @param format 格式化的形式 例如 "0.00"
     * @return
     */
    public static String decimalFormat(double value , String format){
        if("".equals(format) || null == format){
            format = "0.00";
        }
        DecimalFormat decimalFormat = new DecimalFormat(format);
        return decimalFormat.format(value);
    }
}
