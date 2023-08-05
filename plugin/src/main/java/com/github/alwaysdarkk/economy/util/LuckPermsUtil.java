package com.github.alwaysdarkk.economy.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.cacheddata.CachedDataManager;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.model.user.UserManager;
import net.luckperms.api.query.QueryOptions;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

@UtilityClass
public class LuckPermsUtil {

    private final LuckPerms PROVIDER = LuckPermsProvider.get();

    public String getTagWithName(OfflinePlayer offlinePlayer) {
        final CachedMetaData metaData = getCachedMetadata(offlinePlayer.getName());
        return metaData != null && metaData.getPrefix() != null
                ? translateAlternateColorCodes('&', metaData.getPrefix()) + offlinePlayer.getName()
                : "";
    }

    public String getOfflineTagWithName(String playerName) {
        final OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(playerName);
        return offlinePlayer == null ? "" : getOfflineTagWithName(offlinePlayer);
    }

    @SneakyThrows
    public String getOfflineTagWithName(OfflinePlayer offlinePlayer) {
        final UUID uniqueId = offlinePlayer.getUniqueId();
        final UserManager userManager = PROVIDER.getUserManager();

        if (userManager.isLoaded(uniqueId)) {
            return getTagWithName(offlinePlayer);
        }

        final User user = userManager.loadUser(uniqueId).get();
        final CachedDataManager cachedData = user.getCachedData();
        final CachedMetaData metaData = cachedData.getMetaData();
        return metaData.getPrefix() != null
                ? translateAlternateColorCodes('&', metaData.getPrefix()) + offlinePlayer.getName()
                : "";
    }

    private CachedMetaData getCachedMetadata(String userName) {
        final User user = PROVIDER.getUserManager().getUser(userName);
        if (user == null) {
            return null;
        }

        final Group group = PROVIDER.getGroupManager().getGroup(user.getPrimaryGroup());
        return group == null ? null : group.getCachedData().getMetaData(QueryOptions.nonContextual());
    }
}