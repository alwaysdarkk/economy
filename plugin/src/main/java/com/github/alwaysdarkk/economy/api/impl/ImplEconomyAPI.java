package com.github.alwaysdarkk.economy.api.impl;

import com.github.alwaysdarkk.economy.api.EconomyAPI;
import com.github.alwaysdarkk.economy.api.data.EconomyUser;
import com.github.alwaysdarkk.economy.cache.EconomyUserCache;
import com.github.alwaysdarkk.economy.repository.EconomyRepository;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@RequiredArgsConstructor
public class ImplEconomyAPI implements EconomyAPI {

    private final EconomyUserCache userCache;
    private final EconomyRepository repository;

    @Override
    public Optional<EconomyUser> getUser(@NotNull String name) {
        return Optional.of(userCache.get(name));
    }

    @Override
    public void addAmount(@NotNull EconomyUser user, double amount) {
        user.addAmount(amount);
        repository.updateOne(user);
    }

    @Override
    public void removeAmount(@NotNull EconomyUser user, double amount) {
        user.removeAmount(amount);
        repository.updateOne(user);
    }

    @Override
    public void setAmount(@NotNull EconomyUser user, double amount) {
        user.setAmount(amount);
        repository.updateOne(user);
    }
}