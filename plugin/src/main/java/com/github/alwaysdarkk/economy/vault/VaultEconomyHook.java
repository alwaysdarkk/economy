package com.github.alwaysdarkk.economy.vault;

import com.github.alwaysdarkk.economy.api.data.EconomyUser;
import com.github.alwaysdarkk.economy.cache.EconomyUserCache;
import com.github.alwaysdarkk.economy.repository.EconomyRepository;
import com.github.alwaysdarkk.economy.user.factory.EconomyUserFactory;
import com.github.alwaysdarkk.economy.util.NumberFormatter;
import com.github.alwaysdarkk.economy.util.NumberParser;
import lombok.RequiredArgsConstructor;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.OfflinePlayer;

@RequiredArgsConstructor
public class VaultEconomyHook extends EconomyWrapper {

    private final EconomyUserCache userCache;
    private final EconomyRepository repository;
    private final EconomyUserFactory userFactory;

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return "economy";
    }

    @Override
    public int fractionalDigits() {
        return 0;
    }

    @Override
    public String format(double amount) {
        return NumberFormatter.format(amount);
    }

    @Override
    public String currencyNamePlural() {
        return "coins";
    }

    @Override
    public String currencyNameSingular() {
        return "coin";
    }

    @Override
    public boolean hasAccount(OfflinePlayer player) {
        return userCache.containsKey(player.getName());
    }

    @Override
    public double getBalance(OfflinePlayer player) {
        final EconomyUser user = userCache.get(player.getName());

        if (user == null) return 0.0;

        return user.getBalance();
    }

    @Override
    public boolean has(OfflinePlayer player, double amount) {
        if (NumberParser.isInvalid(amount)) return false;

        final EconomyUser user = userCache.get(player.getName());

        if (user == null) return false;

        return user.hasAmount(amount);
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer player, double initialAmount) {
        if (NumberParser.isInvalid(initialAmount))
            return new EconomyResponse(initialAmount, 0, EconomyResponse.ResponseType.FAILURE, "Valor inválido");

        final EconomyUser user = userCache.get(player.getName());

        if (user == null) return null;

        user.removeAmount(initialAmount);
        repository.updateOne(user);

        return new EconomyResponse(
                initialAmount,
                user.getBalance(),
                EconomyResponse.ResponseType.SUCCESS,
                "Operação realizada com sucesso.");
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer player, double initialAmount) {
        if (NumberParser.isInvalid(initialAmount))
            return new EconomyResponse(initialAmount, 0, EconomyResponse.ResponseType.FAILURE, "Valor inválido");

        final EconomyUser user = userCache.get(player.getName());

        if (user == null) return null;

        user.addAmount(initialAmount);
        repository.updateOne(user);

        return new EconomyResponse(
                initialAmount,
                user.getBalance(),
                EconomyResponse.ResponseType.SUCCESS,
                "Operação realizada com sucesso.");
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer player) {
        userFactory.load(player.getPlayer());
        return true;
    }
}