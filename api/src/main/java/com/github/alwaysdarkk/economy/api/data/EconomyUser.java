package com.github.alwaysdarkk.economy.api.data;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EconomyUser {

    private final String name;

    @Builder.Default
    private double balance = 0.0;

    public void addAmount(double amount) {
        this.balance += amount;
    }

    public void removeAmount(double amount) {
        this.balance -= amount;
    }

    public void setAmount(double amount) {
        this.balance = amount;
    }

    public boolean hasAmount(double amount) {
        return this.balance >= amount;
    }
}