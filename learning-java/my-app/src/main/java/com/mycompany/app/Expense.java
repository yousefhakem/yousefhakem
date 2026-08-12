package com.mycompany.app;

import java.util.Map;
import java.math.BigDecimal;

public class Expense{
    private Person paidBy;
    private BigDecimal amount;
    private Map<Person, BigDecimal> shares;

    public Expense(Person paidBy, BigDecimal amount, Map<Person, BigDecimal> shares){
        this.paidBy = paidBy;
        this.amount = amount;
        this.shares = shares;
    }

    public Map<Person, BigDecimal> getBalances() {
        Map<Person, BigDecimal> balances = new java.util.HashMap<>();

        balances.put(this.paidBy, amount);

        for (Map.Entry<Person, BigDecimal> entry : shares.entrySet()) {
            Person person = entry.getKey();
            BigDecimal share = entry.getValue();
            BigDecimal balance;



            if (person.equals(paidBy)) {
                balance = amount.subtract(share);
            } else {
                balance = share.negate();
            }

            balances.put(person, balance);
        }

        return balances;
    }

}