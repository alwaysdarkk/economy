package com.github.alwaysdarkk.economy;

import com.github.alwaysdarkk.economy.api.EconomyAPI;
import com.github.alwaysdarkk.economy.api.impl.ImplEconomyAPI;
import com.github.alwaysdarkk.economy.cache.EconomyUserCache;
import com.github.alwaysdarkk.economy.repository.EconomyRepository;
import com.github.alwaysdarkk.economy.repository.provider.RepositoryProvider;
import com.github.alwaysdarkk.economy.user.factory.EconomyUserFactory;
import com.github.alwaysdarkk.economy.user.listener.UserConnectionListener;
import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;

public class EconomyPlugin extends JavaPlugin {

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

        Bukkit.getPluginManager().registerEvents(new UserConnectionListener(userFactory, userCache), this);

        Bukkit.getServicesManager()
                .register(EconomyAPI.class, new ImplEconomyAPI(userCache, repository), this, ServicePriority.Highest);
    }
}