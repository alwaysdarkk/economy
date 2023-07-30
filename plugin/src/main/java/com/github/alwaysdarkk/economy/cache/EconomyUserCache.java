package com.github.alwaysdarkk.economy.cache;

import com.github.alwaysdarkk.economy.api.data.EconomyUser;
import com.google.common.collect.Maps;
import lombok.experimental.Delegate;

import java.util.Map;

public class EconomyUserCache {

    @Delegate
    private final Map<String, EconomyUser> userMap = Maps.newHashMap();
}