package com.someecho.joinpoint.manager.impl;

import com.someecho.joinpoint.manager.AdviceManager;
import org.springframework.stereotype.Component;

/**
 * @author : linghan.ma
 * @Package joinpoint
 * @Description:
 * @date Date : 2020年03月11日 11:50 PM
 **/
@Component("adviceManagerImpl")
public class AdviceManagerImpl  implements AdviceManager{

    @Override
    public String print(String str){
        if(str.equals("throw")){
            throw new RuntimeException("ex throw");
        }
        System.out.println("hello"+str);
        return "success";
    }
}
