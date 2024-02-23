CREATE TABLE employee
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name TEXT,
    version BIGINT
);

CREATE TABLE company
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    name TEXT,
    balance DECIMAL(30,2) NOT NULL DEFAULT 0,
    currency TEXT NOT NULL DEFAULT 'EUR',
    version BIGINT
);

CREATE TABLE employee_company
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    employee_id BIGINT NOT NULL,
    company_id BIGINT NOT NULL,
    version BIGINT,
    CONSTRAINT fk_employee_company_employee_id FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE,
    CONSTRAINT fk_employee_company_company_id FOREIGN KEY (company_id) REFERENCES company(id) ON DELETE CASCADE,
    CONSTRAINT uc_employee_company_employee_company UNIQUE (employee_id, company_id)
);

CREATE TABLE gift_deposit
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    employee_id BIGINT NOT NULL,
    amount DECIMAL(30,2) NOT NULL DEFAULT 0,
    currency TEXT NOT NULL DEFAULT 'EUR',
    creation_date DATETIME,
    expiration_date DATE,
    version BIGINT,
    CONSTRAINT fk_gift_deposit_employee_id FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE
);

CREATE TABLE meal_deposit
(
    id BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    employee_id BIGINT NOT NULL,
    amount DECIMAL(30,2) NOT NULL DEFAULT 0,
    currency TEXT NOT NULL DEFAULT 'EUR',
    expiration_date DATE,
    creation_date DATETIME,
    version BIGINT,
    CONSTRAINT fk_meal_deposit_employee_id FOREIGN KEY (employee_id) REFERENCES employee(id) ON DELETE CASCADE
);