package com.someecho.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : linghan.ma
 * @Package com.someecho.service
 * @Description:
 * @date Date : 2020年03月22日 2:53 PM
 **/
@Repository
@Slf4j
public class FooService {

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Transactional
    public void queryForUpdate(){
       Integer result =  jdbcTemplate.queryForObject("select id from foo where id = 1 for update",Integer.class);
       log.info("result============="+result);
       try{
           Thread.sleep(2000);
       }catch (InterruptedException e){

       }
    }
}
