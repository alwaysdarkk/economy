package com.github.alwaysdarkk.economy.api;

import com.github.alwaysdarkk.economy.api.data.EconomyUser;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public interface EconomyAPI {

    /**
     * Get the user {@link EconomyUser}
     * @param name the name of target user
     * @return The {@link EconomyUser} corresponding to the name
     */
    Optional<EconomyUser> getUser(@NotNull String name);

    /**
     * Adds a certain amount to a user
     * @param user {@link EconomyUser} the target user
     * @param amount {@link Double} the amount
     */
    void addAmount(@NotNull EconomyUser user, double amount);

    /**
     * Removes a certain amount of a user
     * @param user {@link EconomyUser} the target user
     * @param amount {@link Double} the amount
     */
    void removeAmount(@NotNull EconomyUser user, double amount);

    /**
     * Sets a certain amount to a user
     * @param user {@link EconomyUser} the target user
     * @param amount {@link Double} the amount
     */
    void setAmount(@NotNull EconomyUser user, double amount);
}