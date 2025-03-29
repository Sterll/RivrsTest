package fr.yanis.rivrs.commands;

import fr.yanis.rivrs.manager.ScoreManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class CommandCount implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if(!(sender instanceof Player player))
            return false;


        switch (args.length){
            case 0:
                player.sendMessage("§cVeuillez spécifier un joueur");
                return true;
            case 1:
                player.sendMessage("§cVeuillez spécifier un nombre");
                return true;
            case 2:
                final Player target = player.getServer().getPlayer(args[0]);

                if(target == null){
                    player.sendMessage("§cLe joueur n'est pas connecté");
                    return true;
                }

                int count;

                try {
                    count = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    player.sendMessage("§cVeuillez spécifier un nombre valide");
                    return true;
                }

                player.sendMessage("§aVous avez ajouté " + count + " points à " + target.getName());
                target.sendMessage("§aVous avez reçu " + count + " points de la part de " + player.getName());

                ScoreManager.getScoreManager(target.getUniqueId()).addScore(count);

                return true;
        }

        return false;
    }

}
