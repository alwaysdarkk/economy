package com.github.alwaysdarkk.economy;

import com.github.alwaysdarkk.economy.api.EconomyAPI;
import com.github.alwaysdarkk.economy.api.impl.ImplEconomyAPI;
import com.github.alwaysdarkk.economy.cache.EconomyUserCache;
import com.github.alwaysdarkk.economy.command.CustomCommand;
import com.github.alwaysdarkk.economy.command.impl.MoneyCommand;
import com.github.alwaysdarkk.economy.ranking.factory.RankingFactory;
import com.github.alwaysdarkk.economy.ranking.listener.RankingUpdateListener;
import com.github.alwaysdarkk.economy.repository.EconomyRepository;
import com.github.alwaysdarkk.economy.repository.provider.RepositoryProvider;
import com.github.alwaysdarkk.economy.user.factory.EconomyUserFactory;
import com.github.alwaysdarkk.economy.user.listener.UserConnectionListener;
import com.github.alwaysdarkk.economy.vault.VaultEconomyHook;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import lombok.SneakyThrows;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;

public class EconomyPlugin extends JavaPlugin {

    public static EconomyPlugin getInstance() {
        return getPlugin(EconomyPlugin.class);
    }

    @Override
    public void onLoad() {
        saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        final SQLConnector sqlConnector = RepositoryProvider.of(this).setup(null);
        final SQLExecutor sqlExecutor = new SQLExecutor(sqlConnector);

        final EconomyRepository repository = new EconomyRepository(sqlExecutor);
        repository.createTable();

        final EconomyUserCache userCache = new EconomyUserCache();
        final EconomyUserFactory userFactory = new EconomyUserFactory(repository, userCache);

        Bukkit.getServer()
                .getServicesManager()
                .register(
                        Economy.class,
                        new VaultEconomyHook(userCache, repository, userFactory),
                        this,
                        ServicePriority.Highest);

        final RankingFactory rankingFactory =
                new RankingFactory(repository, getConfig().getConfigurationSection("settings"));

        Bukkit.getPluginManager().registerEvents(new UserConnectionListener(userFactory, userCache), this);
        Bukkit.getPluginManager().registerEvents(new RankingUpdateListener(rankingFactory), this);

        registerCommand(new MoneyCommand(userCache, repository, rankingFactory));

        Bukkit.getServicesManager()
                .register(
                        EconomyAPI.class,
                        new ImplEconomyAPI(userCache, repository, rankingFactory),
                        this,
                        ServicePriority.Highest);
    }

    @SneakyThrows
    private void registerCommand(CustomCommand... customCommands) {
        final Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        commandMapField.setAccessible(true);

        final CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());

        for (CustomCommand customCommand : customCommands) commandMap.register(customCommand.getName(), customCommand);
    }
}