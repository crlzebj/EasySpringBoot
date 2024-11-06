package com.zjx.EasySpringBoot;

public class Test {
    public static void main(String[] args) {
        try{
            Class.forName("com.zjx.EasySpringBoot.util.CodeGenerator");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
