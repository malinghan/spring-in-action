package com.someecho.data.repository;

import com.someecho.data.Coffee;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @author : linghan.ma
 * @Package com.someecho.data.repository
 * @Description:
 * @date Date : 2020年03月22日 9:54 PM
 **/
public interface CoffeeRepository extends MongoRepository<Coffee, String> {
    /**
     * 通过name查询
     * @param name
     * @return
     */
    List<Coffee> findByName(String name);
}
