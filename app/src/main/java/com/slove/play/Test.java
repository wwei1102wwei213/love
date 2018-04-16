package com.slove.play;

/**
 * Created by Administrator on 2018/4/4 0004.
 */

public class Test {

    public static void main(String[] args){
        int f = 0x00;
        f = f & 2;
        System.out.println("f===========>"+(f & 2));
        System.out.println("f===========>"+(f & 4));
        System.out.println("f===========>"+(f & 8));
        System.out.println("f===========>"+(f & 16));
        System.out.println("f===========>"+(f & 32));
        System.out.println("f===========>"+(f & 64));
        f = f & 4;
        System.out.println("f===========>"+f);
        f = f & 8;
        System.out.println("f===========>"+f);
        f = f & 16;
        System.out.println("f===========>"+f);
        f = f & 32;
        System.out.println("f===========>"+f);
        f = f & 64;
        System.out.println("f===========>"+f);
    }

}
