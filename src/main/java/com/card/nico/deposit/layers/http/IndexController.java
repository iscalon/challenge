package com.card.nico.deposit.layers.http;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;


@RestController
class IndexController {

    @GetMapping("/")
    public RepresentationModel<?> index() {
        RepresentationModel<?> index = new RepresentationModel<>();

        Link giftDepositsLink = linkTo(methodOn(GiftDepositController.class).list())
                .withRel("gift-deposits");
        Link mealDepositsLink = linkTo(methodOn(MealDepositController.class).list())
                .withRel("meal-deposits");
        Link companiesLink = linkTo(methodOn(CompanyController.class).list())
                .withRel("companies");
        Link employeesLink = linkTo(methodOn(EmployeeController.class).list())
                .withRel("employees");

        index.add(
                companiesLink,
                employeesLink,
                giftDepositsLink,
                mealDepositsLink
        );

        return index;
    }
}
