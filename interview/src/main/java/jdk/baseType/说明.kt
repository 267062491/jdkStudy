package jdk.baseType

/**
 * Description 包说明，和全局说明
 * Created by ygd on 2017/12/7.
 *
 * 1、包说明
 *  该包下记录的是java8大基本类型的学习源码的过程，八大基本类型分为四大类
 *  https://www.cnblogs.com/simplefrog/archive/2012/07/15/2592011.html
 *  第一类:逻辑型boolean
 *  第二类:文本型char
 *      字符常量为单引号括起来的单个字符  例如 char c='a' ; char b = '中' ;
 *      java采用Unicode编码，每个字符占两个字节，因此可以用16进制编码形式表示
 *  第三类:整数型(byte、short、int、long)
 *      byte 1个字节 ， -128~127
 *      short 2个字节   -32768~32767 （-2^15~2^15-1）
 *      int 4个字节 -2147483648~2147483647共10位 （-2^31~2^31-1）
 *      long 8个字节 -9223372036854775808~9223372036854775807共19位 （-2^63~2^63-1）
 *
 *  第四类:浮点型(float、double)
 *
 * byte 1字节
 * short 2字节
 * int 4字节
 * long 8字节
 * float 4字节
 * double 8字节
 * char 2字节
 * boolean 1字节
 *
 * 2、有缓存的类
 *  Byte 、Short、Integer 、 Long
 */
