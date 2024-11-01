package com.card.nico.deposit.layers.core.ports.in;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.Deposit;
import com.card.nico.deposit.layers.core.MoneyAmount;
import com.card.nico.deposit.layers.core.annotations.UseCase;
import com.card.nico.deposit.layers.core.exceptions.CompanyNotFoundException;
import com.card.nico.deposit.layers.core.exceptions.NotAnEmployeeException;
import com.card.nico.deposit.layers.core.ports.out.CompanyStore;
import com.card.nico.deposit.layers.core.ports.out.TransactionPort;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

@UseCase
class DepositPerformerFactory implements DepositPerformer {

    private final Map<String, DepositStrategy> depositStrategies;
    private final CompanyStore companyStore;
    private final TransactionPort transactionPort;

    DepositPerformerFactory(List<DepositStrategy> depositStrategies,
                            CompanyStore companyStore, TransactionPort transactionPort) {
        this.depositStrategies = requireNonNull(depositStrategies).stream()
                .collect(Collectors.toMap(DepositStrategy::depositType,
                        Function.identity(),
                        (s1, s2) -> s1));
        this.companyStore = requireNonNull(companyStore);
        this.transactionPort = requireNonNull(transactionPort);
    }

    @Override
    public DepositKindPerformer type(String depositType) {
        DepositStrategy depositStrategy = getDepositStrategyOrFail(depositType);
        return new InnerDepositKindPerformer(companyStore, depositStrategy, transactionPort);
    }

    private DepositStrategy getDepositStrategyOrFail(String depositType) {
        return Optional.ofNullable(depositType)
                .map(depositStrategies::get)
                .orElseThrow();
    }

    /**
     *
     */
    static class InnerDepositKindPerformer implements DepositKindPerformer {

        private final CompanyStore companyStore;
        private final DepositStrategy depositStrategy;
        private final TransactionPort transactionPort;

        InnerDepositKindPerformer(CompanyStore companyStore,
                                  DepositStrategy depositStrategy, TransactionPort transactionPort) {
            this.companyStore = requireNonNull(companyStore);
            this.depositStrategy = requireNonNull(depositStrategy);
            this.transactionPort = requireNonNull(transactionPort);
        }

        @Override
        public CompanyDepositKindPerformer from(String companyName) {
            Company company = this.companyStore.findByName(companyName)
                    .orElseThrow(() -> new CompanyNotFoundException(companyName));
            return new InnerCompanyDepositKindPerformer(companyStore, depositStrategy, company, transactionPort);
        }

        @Override
        public Optional<Deposit> findById(Long id) {
            return transactionPort.executeTransactionally(() ->
                    depositStrategy.findById(id));
        }

        @Override
        public <T extends Deposit> List<T> findAll() {
            return transactionPort.executeTransactionally(depositStrategy::findAll);
        }

        @Override
        public <T extends Deposit> List<T> findByEmployeeName(String employeeName) {
            return transactionPort.executeTransactionally(() ->
                    depositStrategy.findByEmployeeName(employeeName));
        }
    }

    /**
     *
     */
    static class InnerCompanyDepositKindPerformer implements CompanyDepositKindPerformer {

        private final CompanyStore companyStore;
        private final DepositStrategy depositStrategy;
        private final Company company;
        private final TransactionPort transactionPort;

        InnerCompanyDepositKindPerformer(CompanyStore companyStore,
                                         DepositStrategy depositStrategy, Company company,
                                         TransactionPort transactionPort) {
            this.companyStore = requireNonNull(companyStore);
            this.depositStrategy = requireNonNull(depositStrategy);
            this.company = requireNonNull(company);
            this.transactionPort = requireNonNull(transactionPort);
        }

        @Override
        public CompanyToEmployeeDepositKindPerformer to(String employeeName) {
            assertEmployeeBelongsToCompany(employeeName);
            return new InnerCompanyToEmployeeDepositKindPerformer(companyStore, depositStrategy, company, employeeName, transactionPort);
        }

        private void assertEmployeeBelongsToCompany(String employeeName) {
            if (company.employees().stream().noneMatch(candidate -> Objects.equals(candidate.name(), employeeName))) {
                throw new NotAnEmployeeException(company.name(), employeeName);
            }
        }
    }

    /**
     *
     */
    static class InnerCompanyToEmployeeDepositKindPerformer implements CompanyToEmployeeDepositKindPerformer {

        private final CompanyStore companyStore;
        private final DepositStrategy depositStrategy;
        private final Company company;
        private final String employeeName;
        private final TransactionPort transactionPort;

        InnerCompanyToEmployeeDepositKindPerformer(CompanyStore companyStore,
                                                   DepositStrategy depositStrategy, Company company,
                                                   String employeeName, TransactionPort transactionPort) {
            this.companyStore = requireNonNull(companyStore);
            this.depositStrategy = requireNonNull(depositStrategy);
            this.company = requireNonNull(company);
            this.employeeName = requireNonNull(employeeName);
            this.transactionPort = requireNonNull(transactionPort);
        }

        @Override
        public Deposit doDeposit(MoneyAmount amount) {
            return transactionPort.executeTransactionally(() -> {
                Deposit deposit = depositStrategy.createDeposit(company.name(), employeeName, amount);
                // Update company's balance
                companyStore.save(
                        new Company(company.name(), company.balance().minus(amount), company.employees()));

                return deposit;
            });
        }
    }
}
