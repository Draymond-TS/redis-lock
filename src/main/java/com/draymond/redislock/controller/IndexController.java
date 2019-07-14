package com.draymond.redislock.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @RequestMapping("/deduct_stock")
    public String deductStock(){
        //synchronized在nginx分开给每个不同的Tomcat服务器的时候，并不起作用,因为他们对应的JVM虚拟机不一样，分别是两个不同的服务器
        synchronized (this){
            int stock = Integer.parseInt(stringRedisTemplate.opsForValue().get("stock"));
            if(stock>0){
                int lockStock=stock-1;
                stringRedisTemplate.opsForValue().set("stock",lockStock+"");
                System.out.println("扣减成功，剩余存款为"+lockStock);
            }else{
                System.out.println("扣减失败，存款不足!");
            }
        }
        return "end";
    }
}
