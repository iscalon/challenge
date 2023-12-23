package com.example.demo;

import org.springframework.stereotype.Component;

@Component
public class DemoComponent {

    public String getName() {
        return this.getClass().getName();
    }
}
