package com.card.nico.deposit.layers.http;

import com.card.nico.deposit.layers.core.Company;
import com.card.nico.deposit.layers.core.Employee;
import com.card.nico.deposit.layers.core.GiftDeposit;
import com.card.nico.deposit.layers.core.MoneyAmount;
import com.card.nico.deposit.layers.core.ports.in.CompanyUseCase;
import com.card.nico.deposit.layers.core.ports.in.DepositUseCase;
import com.card.nico.deposit.layers.core.ports.in.EmployeeUseCase;
import com.card.nico.deposit.layers.database.tooling.DBTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DBTest
@AutoConfigureMockMvc
class GiftDepositControllerTest {

    @Inject
    private MockMvc mockMvc;

    @Inject
    private CompanyUseCase companies;

    @Inject
    private EmployeeUseCase employees;

    @Inject
    private DepositUseCase deposits;

    @Test
    @DisplayName("Unauthenticated call to /gift-deposits should be unauthorized")
    void listUnauthenticated() throws Exception {
        this.mockMvc
                .perform(get("/gift-deposits"))
                .andExpect(
                        status().isUnauthorized());
    }

    @Test
    @DisplayName("Authenticated call to /gift-deposits succeeds")
    void listAuthenticated() throws Exception {
        prepareGiftDepositData("Sony", "Jean-Pierre",
                MoneyAmount.of(166000d, "JPY"));

        this.mockMvc
                .perform(get("/gift-deposits")
                        .with(authenticatedUser())
                )
                .andExpectAll(
                        status().isOk(),
                        jsonPath("$._embedded.representations[0].amount")
                                .value("166000.00JPY"),
                        jsonPath("$._embedded.representations[0]._links.employee.href",
                                containsString("/employees/Jean-Pierre")),
                        jsonPath("$._embedded.representations[0]._links.company.href",
                                containsString("/companies/Sony"))
                );
    }

    private static SecurityMockMvcRequestPostProcessors.UserRequestPostProcessor authenticatedUser() {
        return user("admin")
                .password("pass")
                .roles("USER", "ADMIN");
    }

    private void prepareGiftDepositData(String companyName, String employeeName, MoneyAmount amount) {
        employees.save(new Employee(employeeName));
        Employee employee = employees.findByName(employeeName).orElseThrow();
        companies.save(new Company(
                companyName,
                MoneyAmount.of(amount.amount().add(BigDecimal.TEN).doubleValue(), amount.currency().getCurrencyCode()),
                Set.of(employee)));
        Company company = companies.findByName(companyName).orElseThrow();
        deposits.type("GIFT")
                .save(new GiftDeposit(
                        -1L,
                        amount,
                        company,
                        employee,
                        LocalDateTime.now(), null));
    }
}