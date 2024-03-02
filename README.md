# Getting Started

## Run the app

`./mvnw clean spring-boot:run`

## Login

URL is : `http://localhost:8088`

![Spring security login screen](doc/img/login.png?raw=true)

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/light-theme/info.svg">
>   <img alt="Info" src="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/dark-theme/info.svg">
> </picture><br>
>
> Username : `demo`<br/>
> Password : `demo`

## In memory database H2 console

URL is : `http://localhost:8088/h2-console`

![H2 Console](doc/img/h2-console.png?raw=true)

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/light-theme/info.svg">
>   <img alt="Info" src="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/dark-theme/info.svg">
> </picture><br>
>
> Driver Class : `org.h2.Driver`<br/>
> JDBC URL : `jdbc:h2:mem:mydb`<br/>
> User Name : `sa`<br/>
> No password needed.

## HAL explorer

URL is : `http://localhost:8088/explorer`

![HAL Explorer](doc/img/hal-explorer.png?raw=true)

API URI index is : `/`

Set it in the "**Edit Headers**" input field and then click the "**Go!**" button.

`companies`, `employees`, `gift-deposits` and `meal-deposits` links should be available :

![HAL Explorer : API index](doc/img/hal-explorer-index.png?raw=true)

### Create a company

Go to the `companies` link by clicking on the "**<**" HTTP Request button near `companies`<br/>or by setting `/companies` in the "**Edit Headers**" input field and then click the "**Go!**" button.

![HAL Explorer : companies link](doc/img/hal-explorer-companies.png?raw=true)

Now click on the HTTP Request "**+**" button near `default` to perform a *POST* action :

![HAL Explorer : companies POST](doc/img/hal-explorer-companies-post.png?raw=true)

The `amount` attribute is the company's balance amount.<br/>
The `currencyCode` attribute is the company's balance currency code.

You can already create some employees for that company here if you want, but it's not mandatory.

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/light-theme/warning.svg">
>   <img alt="Warning" src="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/dark-theme/warning.svg">
> </picture><br>
>
> The HAL explorer sets **"** around the employeesNames array instead of each name, you have to modify the body so that it should be :<br/>
> `["John", "Peter", "Nicolas"]`

Click the "**Go!**" button when everything is set.

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/light-theme/success.svg">
>   <img alt="Success" src="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/dark-theme/success.svg">
> </picture><br>
>
> Company should have been created, follow the `/companies` link again in the "**Edit Headers**" to see the result.

![HAL Explorer : company created](doc/img/hal-explorer-companies-created.png?raw=true)

From there you can follow the company's employees links to have access to each employee information (i.e. their names).

The "**>**" button in the *Embedded Resources HAL-FORMS Template Elements* that is now appearing is a way to make a *PUT* action in order to add an employee to this company.

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/light-theme/warning.svg">
>   <img alt="Warning" src="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/dark-theme/warning.svg">
> </picture><br>
>
> The employee must have been created before as we will see in the next section.

### Create an employee

Set `/employees` in the "**Edit Headers**" input field and then click the "**Go!**" button.

Now click on the HTTP Request "**+**" button near `default` to perform a *POST* action :

![HAL Explorer : employees post](doc/img/hal-explorer-employees-post.png?raw=true)

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/light-theme/success.svg">
>   <img alt="Success" src="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/dark-theme/success.svg">
> </picture><br>
>
> Employee should have been created, follow the `/employees` link again in the "**Edit Headers**" to see the result.

![HAL Explorer : employees post](doc/img/hal-explorer-employees-created.png?raw=true)

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/light-theme/info.svg">
>   <img alt="Info" src="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/dark-theme/info.svg">
> </picture><br>
>
> The newly created employee doesn't have a `company` relation yet, you can define it with the company's *PUT* action seen in the previous section.

### Create a deposit

Now that we have a company and some employees we can make deposits for them.

Set `/gift-deposits` (or `/meal-deposits`) in the "**Edit Headers**" input field and then click the "**Go!**" button.

Now click on the HTTP Request "**+**" button near `default` to perform a *POST* action :

![HAL Explorer : employees post](doc/img/hal-explorer-deposits-post.png?raw=true)

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/light-theme/success.svg">
>   <img alt="Success" src="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/dark-theme/success.svg">
> </picture><br>
>
> Employee should have been created, follow the `/gift-deposits` (or `/meal-deposits`) link again in the "**Edit Headers**" to see the result.

![HAL Explorer : employees post](doc/img/hal-explorer-deposits-created.png?raw=true)

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/light-theme/info.svg">
>   <img alt="Info" src="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/dark-theme/info.svg">
> </picture><br>
>
> From there you can follow the `employee` or `company` relation of each deposit.

Let's check the company's balance after this deposit by following the `company` :

![HAL Explorer : employees post](doc/img/hal-explorer-company-balance.png?raw=true)

> <picture>
>   <source media="(prefers-color-scheme: light)" srcset="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/light-theme/note.svg">
>   <img alt="Note" src="https://raw.githubusercontent.com/Mqxx/GitHub-Markdown/main/blockquotes/badge/dark-theme/note.svg">
> </picture><br>
>
> All the *GET*, *POST* and *PUT* actions made with the HAL explorer can also be performed by tools like Postman or cURL.

# Reference Documentation
For further reference about the used technologies, please consider the following sections:


* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#web)
* [Rest Repositories](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#howto.data-access.exposing-spring-data-repositories-as-rest)
* [Spring HATEOAS](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#web.spring-hateoas)
* [Spring Security](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#web.security)
* [Spring Data JPA](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#data.sql.jpa-and-spring-data)
* [Flyway Migration](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#howto.data-initialization.migration-tool.flyway)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/3.2.2/reference/htmlsingle/index.html#actuator)