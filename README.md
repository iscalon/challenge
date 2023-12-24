# Backend Challenge

Main classes are :
* `com.example.demo.business.Deposits` to perform deposits.
* `com.example.demo.business.UserBalances` to compute user balances.

Test classes are :
* For integration tests :
  * `com.example.demo.business.DepositsTest`
  * `com.example.demo.business.UserBalancesTest`
* For unit tests :
  * `com.example.demo.business.UserBalancesUnitTest`

<br />
<br />

To run tests :

* On windows :
```shell
.\mvnw.cmd clean test
```
* On Linux :
```shell
./mvnw clean test
```