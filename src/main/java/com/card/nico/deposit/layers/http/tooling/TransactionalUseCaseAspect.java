package com.card.nico.deposit.layers.http.tooling;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import static java.util.Objects.requireNonNull;

@Component
@Aspect
public class TransactionalUseCaseAspect {

    private final TransactionalExecutor transactionalUseCaseExecutor;

    public TransactionalUseCaseAspect(TransactionalExecutor transactionalUseCaseExecutor) {
        this.transactionalUseCaseExecutor = requireNonNull(transactionalUseCaseExecutor);
    }

    @Around("@within(com.card.nico.deposit.layers.core.annotations.UseCase)")
    public Object transaction(ProceedingJoinPoint proceedingJoinPoint) {
        return transactionalUseCaseExecutor.executeTransactionally(() -> {
            try {
                return proceedingJoinPoint.proceed();
            } catch (Throwable e) {
                throw new IllegalArgumentException(e);
            }
        });
    }
}
