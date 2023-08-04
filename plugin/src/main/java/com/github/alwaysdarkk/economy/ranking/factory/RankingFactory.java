package com.github.alwaysdarkk.economy.ranking.factory;

import com.github.alwaysdarkk.economy.EconomyPlugin;
import com.github.alwaysdarkk.economy.api.data.EconomyUser;
import com.github.alwaysdarkk.economy.api.event.RankingUpdateEvent;
import com.github.alwaysdarkk.economy.repository.EconomyRepository;
import com.github.alwaysdarkk.economy.util.NumberFormatter;
import com.google.common.collect.Sets;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class RankingFactory {

    private final EconomyRepository repository;

    private final Set<EconomyUser> cachedRanking = Sets.newLinkedHashSetWithExpectedSize(10);
    private final StringBuilder stringBuilder = new StringBuilder();

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

        updateMessage();

        final RankingUpdateEvent rankingUpdateEvent = new RankingUpdateEvent(cachedRanking);
        Bukkit.getPluginManager().callEvent(rankingUpdateEvent);
    }

    public Optional<EconomyUser> getTycoon() {
        return this.cachedRanking.stream().filter(Objects::nonNull).findFirst();
    }

    private void updateMessage() {
        stringBuilder.append("\n").append("§a TOP §l10 §ajogadores mais ricos").append("\n \n");

        final AtomicInteger position = new AtomicInteger(0);
        cachedRanking.stream().filter(Objects::nonNull).forEach(user -> stringBuilder
                .append(String.format(
                        " §f%s° %s: §7%s",
                        position.getAndIncrement(), user.getName(), NumberFormatter.format(user.getBalance())))
                .append("\n"));

        stringBuilder.append("\n");
    }
}