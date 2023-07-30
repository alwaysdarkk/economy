package com.github.alwaysdarkk.economy.api.event;

import com.github.alwaysdarkk.economy.api.data.EconomyUser;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class RankingUpdateEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    private final Set<EconomyUser> cachedRanking;

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}