package com.example.demo;

import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class DemoApplicationTests {

	@Inject private DemoComponent demoComponent;

	@Test
	void contextLoads() {
		assertThat(demoComponent.getName()).isEqualTo("com.example.demo.DemoComponent");
	}

}
