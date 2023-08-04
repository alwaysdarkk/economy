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

public class SetSubCommand extends CustomCommand {

    private final EconomyUserCache userCache;
    private final EconomyRepository economyRepository;

    public SetSubCommand(EconomyUserCache userCache, EconomyRepository economyRepository) {
        super("set", "economy.admin", false, "definir");
        this.userCache = userCache;
        this.economyRepository = economyRepository;
    }

    @Override
    protected void onCommand(CommandSender commandSender, String[] arguments) {
        if (arguments.length != 2) {
            commandSender.sendMessage("§cUtilize /money set <jogador> <quantia>.");
            return;
        }

        final Player target = Bukkit.getPlayer(arguments[0]);

        if (target == null) {
            commandSender.sendMessage("§cEste jogador não foi encontrado.");
            return;
        }

        final EconomyUser user = userCache.get(target.getName());

        if (user == null) return;

        final Double amount = NumberParser.tryParseDouble(arguments[1]);

        if (NumberParser.isInvalid(amount)) {
            commandSender.sendMessage("§cA quantia inserida é inválida.");
            return;
        }

        user.setAmount(amount);
        economyRepository.updateOne(user);

        commandSender.sendMessage(String.format(
                "§aYeah! Você definiu o saldo de §f%s §apara §f%s§a.",
                target.getName(), NumberFormatter.format(amount)));
    }
}