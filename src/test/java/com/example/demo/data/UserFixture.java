package com.example.demo.data;

import org.springframework.stereotype.Component;

@Component
public class UserFixture {

    private long idGenerator = 0;

    public User create(String name) {
        return new User(++idGenerator, name);
    }
}
