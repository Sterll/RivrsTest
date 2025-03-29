package fr.yanis.rivrs.commands;

import fr.yanis.rivrs.manager.PosManager;
import fr.yanis.rivrs.utils.LocationSerializer;
import it.unimi.dsi.fastutil.Pair;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CommandPos implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if(!(sender instanceof Player player))
            return false;

        switch (args.length){
            case 0:
                return false;
            case 1:
                try {
                    int posn = Integer.parseInt(args[0]);
                    UUID uuid = player.getUniqueId();

                    String locSerialized = LocationSerializer.serialize(player.getLocation());

                    if (posn == 1){
                        if (!PosManager.contains(uuid)){
                            PosManager.set(uuid, locSerialized, "");
                        } else {
                            PosManager.setFirst(uuid, locSerialized);
                        }
                        player.sendMessage("§aLa position 1 a été définie !");
                        return true;
                    }

                    if (posn == 2){
                        if (!PosManager.contains(uuid)){
                            PosManager.set(uuid, "", locSerialized);
                        } else {
                            PosManager.setSecond(uuid, locSerialized);
                        }
                        player.sendMessage("§aLa position 2 a été définie !");
                        return true;
                    }

                } catch (NumberFormatException e) {
                    return false;
                }
                break;
        }
        return false;
    }

}
