package com.mycompany.app;

import java.util.Map;
import java.math.BigDecimal;
import java.util.List;
import java.util.HashMap;


public class BalanceCalculator {

    public static Map<Person, BigDecimal> calculate(List<Expense> expenses){
        Map<Person, BigDecimal> balances = new HashMap<>();

        for (Expense expense : expenses) {
            Map<Person, BigDecimal> expenseBalances = expense.getBalances();

            for (Map.Entry<Person, BigDecimal> entry : expenseBalances.entrySet()) {
                Person person = entry.getKey();
                BigDecimal balance = entry.getValue();

                if (balances.containsKey(person)) {
                    balances.put(person, balances.get(person).add(balance));
                } else {
                    balances.put(person, balance);
                }
            }
        }

        return balances;
    }
}