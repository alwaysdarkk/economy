package com.github.alwaysdarkk.economy.command.impl.subcommand;

import com.github.alwaysdarkk.economy.command.CustomCommand;
import com.github.alwaysdarkk.economy.ranking.factory.RankingFactory;
import org.bukkit.command.CommandSender;

public class RankingSubCommand extends CustomCommand {

    private final RankingFactory rankingFactory;

    public RankingSubCommand(RankingFactory rankingFactory) {
        super("ranking", null, false, "top");
        this.rankingFactory = rankingFactory;
    }

    @Override
    protected void onCommand(CommandSender commandSender, String[] arguments) {
        if (rankingFactory.getCachedRanking().isEmpty()) {
            commandSender.sendMessage("§cO ranking está atualizando...");
            return;
        }

        commandSender.sendMessage(rankingFactory.getStringBuilder().toString());
    }
}