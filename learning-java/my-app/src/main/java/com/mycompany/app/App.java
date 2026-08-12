package com.mycompany.app;

import java.util.Map;
import java.math.BigDecimal;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        Person a = new Person("a");
        Person x = new Person("x");
        Person y = new Person("y");
        Person z = new Person("z");

        Expense first = new Expense(a, new BigDecimal("80"), Map.of(x, new BigDecimal("10"), y, new BigDecimal("20"), z, new BigDecimal("50")));
        Expense second = new Expense(z, new BigDecimal("20"), Map.of(x, new BigDecimal("5"), y, new BigDecimal("5"), a, new BigDecimal("5"), z, new BigDecimal("5")));
        Expense third = new Expense(y, new BigDecimal("100"), Map.of(y, new BigDecimal("50"), z, new BigDecimal("50")));
        Expense fourth = new Expense(x, new BigDecimal("10"), Map.of(x, new BigDecimal("10")));



        Map<Person, BigDecimal> balances = BalanceCalculator.calculate(List.of(first, second, third, fourth));
        System.out.println(balances);
    }
}
