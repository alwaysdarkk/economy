package com.github.alwaysdarkk.economy.user.factory;

import com.github.alwaysdarkk.economy.api.data.EconomyUser;
import com.github.alwaysdarkk.economy.cache.EconomyUserCache;
import com.github.alwaysdarkk.economy.repository.EconomyRepository;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class EconomyUserFactory {

    private final EconomyRepository repository;
    private final EconomyUserCache userCache;

    public void load(@NotNull Player player) {
        EconomyUser user = repository.selectOne(player.getName());

        if (user == null) {
            user = createDefault(player);
            repository.insertOne(user);
        }

        userCache.put(user.getName(), user);
    }

    private EconomyUser createDefault(@NotNull Player player) {
        return EconomyUser.builder().name(player.getName()).build();
    }
}