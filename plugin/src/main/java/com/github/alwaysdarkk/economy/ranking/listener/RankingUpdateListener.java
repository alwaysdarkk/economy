package com.github.alwaysdarkk.economy.ranking.listener;

import com.github.alwaysdarkk.economy.api.data.EconomyUser;
import com.github.alwaysdarkk.economy.api.event.RankingUpdateEvent;
import com.github.alwaysdarkk.economy.ranking.factory.RankingFactory;
import com.github.alwaysdarkk.economy.util.LuckPermsUtil;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Optional;

@RequiredArgsConstructor
public class RankingUpdateListener implements Listener {

    private final RankingFactory rankingFactory;

    @EventHandler
    private void onUpdate(RankingUpdateEvent event) {
        final Optional<EconomyUser> optionalTycoon = rankingFactory.getTycoon();

        if (!optionalTycoon.isPresent()) return;

        final EconomyUser tycoon = optionalTycoon.get();

        Bukkit.getOnlinePlayers()
                .forEach(onlinePlayer -> onlinePlayer.sendMessage(new String[] {
                    "",
                    "§a§l MAGNATA!",
                    "§f O money top acaba de atualizar!",
                    "",
                    "§8 ▸ §fNovo magnata: §7" + LuckPermsUtil.getOfflineTagWithName(tycoon.getName()),
                    ""
                }));
    }
}