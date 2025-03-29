package fr.yanis.rivrs.commands;

import fr.yanis.rivrs.RMain;
import fr.yanis.rivrs.manager.PosManager;
import fr.yanis.rivrs.manager.ZoneManager;
import fr.yanis.rivrs.utils.Cuboid;
import fr.yanis.rivrs.utils.LocationSerializer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class CommandZone implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if (!(sender instanceof Player player))
            return false;

        if (args.length > 0)
            return false;

        UUID playerUUID = player.getUniqueId();

        if (!(PosManager.contains(playerUUID))){
            player.sendMessage("§cVous devez d'abord définir vos positions avec /setpos 1 et /setpos 2 !");
            return true;
        }

        if(PosManager.getFirst(player.getUniqueId()).isEmpty()){
            player.sendMessage("§cVous devez d'abord définir votre position 1 avec /setpos 1 !");
            return true;
        }

        if(PosManager.getSecond(player.getUniqueId()).isEmpty()){
            player.sendMessage("§cVous devez d'abord définir votre position 2 avec /setpos 2 !");
            return true;
        }

        String pos1 = PosManager.getFirst(player.getUniqueId());
        String pos2 = PosManager.getSecond(player.getUniqueId());

        new ZoneManager(new Cuboid(LocationSerializer.deserialize(pos1), LocationSerializer.deserialize(pos2)), RMain.getInstance().getConfig().getInt("game.points_interval"));

        return false;
    }

}
