package com.zjx.EasySpringBoot;

import com.zjx.EasySpringBoot.util.FieldToPojoUtil;

public class Test {
    public static void main(String[] args) {
        try{
            Class.forName("com.zjx.EasySpringBoot.CodeGenerator");
        }
        catch (ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
