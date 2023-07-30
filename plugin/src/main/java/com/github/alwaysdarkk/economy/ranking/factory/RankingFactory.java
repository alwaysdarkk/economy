package com.github.alwaysdarkk.economy.ranking.factory;

import com.github.alwaysdarkk.economy.EconomyPlugin;
import com.github.alwaysdarkk.economy.api.data.EconomyUser;
import com.github.alwaysdarkk.economy.api.event.RankingUpdateEvent;
import com.github.alwaysdarkk.economy.repository.EconomyRepository;
import com.google.common.collect.Sets;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RankingFactory {

    private final EconomyRepository repository;

    private final Set<EconomyUser> cachedRanking = Sets.newLinkedHashSetWithExpectedSize(10);

    public RankingFactory(EconomyRepository repository, ConfigurationSection configurationSection) {
        this.repository = repository;

        final int updateTime = configurationSection.getInt("update-time");

        Bukkit.getScheduler()
                .runTaskTimerAsynchronously(
                        EconomyPlugin.getInstance(), this::updateRanking, 0L, TimeUnit.MINUTES.toMillis(updateTime));
    }

    public void updateRanking() {
        cachedRanking.clear();
        cachedRanking.addAll(repository.fetchRanking());

        final RankingUpdateEvent rankingUpdateEvent = new RankingUpdateEvent(cachedRanking);
        Bukkit.getPluginManager().callEvent(rankingUpdateEvent);
    }
}