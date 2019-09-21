package com.draymond.redislock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 解决redis分布式锁的问题
 */

@Controller
public class IndexController2 {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/deduct_stock")
    public String deductStock() {
        String keylock = "keylock";

        Boolean lock = stringRedisTemplate.opsForValue().setIfAbsent(keylock, "lock");

        if (!lock) {
            return "error";
        } else {
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if (stock > 0) {
                int lockStock = stock - 1;
                stringRedisTemplate.opsForValue().set("stock", lockStock + "");
                System.out.println("扣减成功，剩余存款为" + lockStock);
            } else {
                System.out.println("扣减失败，存款不足!");
            }

            stringRedisTemplate.delete("keylock");

            return "end";
        }
    }
}
