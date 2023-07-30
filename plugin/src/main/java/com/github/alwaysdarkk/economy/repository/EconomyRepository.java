package com.github.alwaysdarkk.economy.repository;

import com.github.alwaysdarkk.economy.api.data.EconomyUser;
import com.github.alwaysdarkk.economy.repository.adapter.EconomyUserAdapter;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class EconomyRepository {

    private final SQLExecutor sqlExecutor;

    public void createTable() {
        final String QUERY =
                "CREATE TABLE IF NOT EXISTS economy(name VARCHAR(16) NOT NULL PRIMARY KEY, double amount NOT NULL)";
        sqlExecutor.updateQuery(QUERY);
    }

    public void insertOne(EconomyUser user) {
        final String QUERY = "INSERT INTO economy VALUES(?,?)";
        CompletableFuture.runAsync(() -> sqlExecutor.updateQuery(QUERY, simpleStatement -> {
            simpleStatement.set(1, user.getName());
            simpleStatement.set(2, user.getBalance());
        }));
    }

    public void updateOne(EconomyUser user) {
        final String QUERY = "UPDATE economy SET balance = ? WHERE name = ?";
        CompletableFuture.runAsync(() -> sqlExecutor.updateQuery(QUERY, simpleStatement -> {
            simpleStatement.set(1, user.getBalance());
            simpleStatement.set(2, user.getName());
        }));
    }

    public EconomyUser selectOne(String name) {
        final String QUERY = "SELECT * FROM economy WHERE name = ?";
        return sqlExecutor.resultOneQuery(
                QUERY, simpleStatement -> simpleStatement.set(1, name), EconomyUserAdapter.class);
    }
}