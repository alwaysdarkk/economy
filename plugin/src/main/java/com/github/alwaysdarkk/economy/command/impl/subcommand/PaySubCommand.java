package com.github.alwaysdarkk.economy.command.impl.subcommand;

import com.github.alwaysdarkk.economy.api.data.EconomyUser;
import com.github.alwaysdarkk.economy.cache.EconomyUserCache;
import com.github.alwaysdarkk.economy.command.CustomCommand;
import com.github.alwaysdarkk.economy.repository.EconomyRepository;
import com.github.alwaysdarkk.economy.util.NumberFormatter;
import com.github.alwaysdarkk.economy.util.NumberParser;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PaySubCommand extends CustomCommand {

    private final EconomyUserCache userCache;
    private final EconomyRepository economyRepository;

    public PaySubCommand(EconomyUserCache userCache, EconomyRepository economyRepository) {
        super("pay", null, true, "enviar");
        this.userCache = userCache;
        this.economyRepository = economyRepository;
    }

    @Override
    protected void onCommand(CommandSender commandSender, String[] arguments) {
        final Player player = (Player) commandSender;

        if (arguments.length != 2) {
            player.sendMessage("§cUtilize /money pay <jogador> <quantia>.");
            return;
        }

        final EconomyUser user = userCache.get(player.getName());

        if (user == null) return;

        final Player target = Bukkit.getPlayer(arguments[0]);

        if (target == null) {
            player.sendMessage("§cEste jogador não foi encontrado.");
            return;
        }

        final EconomyUser targetUser = userCache.get(target.getName());

        if (targetUser == null) return;

        final Double amount = NumberParser.tryParseDouble(arguments[1]);

        if (NumberParser.isInvalid(amount) || amount > user.getBalance()) {
            player.sendMessage("§cA quantia inserida é inválida.");
            return;
        }

        user.removeAmount(amount);
        targetUser.addAmount(amount);

        economyRepository.updateOne(user);
        economyRepository.updateOne(targetUser);

        player.sendMessage(String.format(
                "§aYeah! Você enviou §f%s §ade coins para §f%s§a.", NumberFormatter.format(amount), target.getName()));

        target.sendMessage(String.format(
                "§aYeah! Você recebeu §f%s §ade coins de §f%s§a.", NumberFormatter.format(amount), player.getName()));
    }
}