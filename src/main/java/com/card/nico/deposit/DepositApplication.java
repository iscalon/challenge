package com.card.nico.deposit;

import com.card.nico.deposit.layers.core.annotations.DomainComponent;
import com.card.nico.deposit.layers.core.annotations.UseCase;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication()
@ComponentScan(includeFilters =
@ComponentScan.Filter(type = FilterType.ANNOTATION,
		classes = {UseCase.class, DomainComponent.class}))
@EnableJpaRepositories(considerNestedRepositories = true)
@EnableAspectJAutoProxy
public class DepositApplication {

	public static void main(String[] args) {
		SpringApplication.run(DepositApplication.class, args);
	}
}
