package com.github.alwaysdarkk.economy.repository.adapter;

import com.github.alwaysdarkk.economy.api.data.EconomyUser;
import com.henryfabio.sqlprovider.executor.adapter.SQLResultAdapter;
import com.henryfabio.sqlprovider.executor.result.SimpleResultSet;

public class EconomyUserAdapter implements SQLResultAdapter<EconomyUser> {

    @Override
    public EconomyUser adaptResult(SimpleResultSet resultSet) {
        final String name = resultSet.get("name");
        final double balance = resultSet.get("balance");

        return EconomyUser.builder().name(name).balance(balance).build();
    }
}