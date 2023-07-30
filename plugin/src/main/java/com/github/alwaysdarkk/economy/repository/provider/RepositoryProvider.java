package com.github.alwaysdarkk.economy.repository.provider;

import com.henryfabio.sqlprovider.connector.SQLConnector;
import com.henryfabio.sqlprovider.connector.type.SQLDatabaseType;
import com.henryfabio.sqlprovider.connector.type.impl.MySQLDatabaseType;
import com.henryfabio.sqlprovider.connector.type.impl.SQLiteDatabaseType;
import lombok.Data;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;

@Data(staticConstructor = "of")
public class RepositoryProvider {

    private final Plugin plugin;

    public SQLConnector setup(String databaseType) {
        final FileConfiguration fileConfiguration = plugin.getConfig();
        final ConfigurationSection databaseConfiguration = fileConfiguration.getConfigurationSection("database");

        final String sqlType = databaseType != null ? databaseType : databaseConfiguration.getString("type");

        SQLConnector sqlConnector;

        switch (sqlType) {
            case "mysql": {
                final ConfigurationSection mysqlSection = databaseConfiguration.getConfigurationSection("mysql");
                sqlConnector = mysqlDatabaseType(mysqlSection).connect();
                return sqlConnector;
            }

            case "sqlite": {
                final ConfigurationSection sqliteSection = databaseConfiguration.getConfigurationSection("sqlite");
                sqlConnector = sqliteDatabaseType(sqliteSection).connect();
                return sqlConnector;
            }

            default: {
                plugin.getLogger().severe("database type invalid.");
                return null;
            }
        }
    }

    private SQLDatabaseType sqliteDatabaseType(ConfigurationSection configurationSection) {
        return SQLiteDatabaseType.builder()
                .file(new File(plugin.getDataFolder(), configurationSection.getString("file")))
                .build();
    }

    private SQLDatabaseType mysqlDatabaseType(ConfigurationSection configurationSection) {
        return MySQLDatabaseType.builder()
                .address(configurationSection.getString("address"))
                .username(configurationSection.getString("username"))
                .password(configurationSection.getString("password"))
                .database(configurationSection.getString("database"))
                .build();
    }
}