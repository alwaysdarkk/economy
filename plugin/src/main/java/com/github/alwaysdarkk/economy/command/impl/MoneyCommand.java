package com.github.alwaysdarkk.economy.command.impl;

import com.github.alwaysdarkk.economy.api.data.EconomyUser;
import com.github.alwaysdarkk.economy.cache.EconomyUserCache;
import com.github.alwaysdarkk.economy.command.CustomCommand;
import com.github.alwaysdarkk.economy.command.impl.subcommand.*;
import com.github.alwaysdarkk.economy.ranking.factory.RankingFactory;
import com.github.alwaysdarkk.economy.repository.EconomyRepository;
import com.github.alwaysdarkk.economy.util.NumberFormatter;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommand extends CustomCommand {

    private final EconomyUserCache userCache;

    public MoneyCommand(
            EconomyUserCache userCache, EconomyRepository economyRepository, RankingFactory rankingFactory) {
        super("money", null, false, "coins");

        registerSubCommands(
                new AddSubCommand(userCache, economyRepository),
                new RemoveSubCommand(userCache, economyRepository),
                new SetSubCommand(userCache, economyRepository),
                new PaySubCommand(userCache, economyRepository),
                new RankingSubCommand(rankingFactory));

        this.userCache = userCache;
    }

    @Override
    protected void onCommand(CommandSender commandSender, String[] arguments) {
        if (arguments.length > 1) {
            commandSender.sendMessage("§cUtilize /money [jogador].");
            return;
        }

        if (arguments.length == 0) {
            if (!(commandSender instanceof Player)) return;

            final Player player = (Player) commandSender;
            final EconomyUser user = userCache.get(player.getName());

            if (user == null) return;

            player.sendMessage(
                    String.format("§aVocê possui §f%s §ade coins.", NumberFormatter.format(user.getBalance())));
            return;
        }

        final Player target = Bukkit.getPlayer(arguments[0]);

        if (target == null) {
            commandSender.sendMessage("§cEste jogador não foi encontrado.");
            return;
        }

        final EconomyUser user = userCache.get(target.getName());

        if (user == null) return;

        commandSender.sendMessage(String.format(
                "§aO jogador §f%s §apossui §f%s §ade coins.",
                target.getName(), NumberFormatter.format(user.getBalance())));
    }
}