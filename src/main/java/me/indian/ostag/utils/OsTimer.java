package me.indian.ostag.utils;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;

import java.util.List;

public class OsTimer extends Task implements Runnable, Listener {

    private static final OsTag plugin = OsTag.getInstance();
    private static final Config conf = plugin.getConfig();

    @Override
    public void onRun(int i) {
        for (Player all : Server.getInstance().getOnlinePlayers().values()) {
            addOsTag(all);
        }
    }

    @SuppressWarnings("unused")
    @EventHandler
    public void playerJoin(PlayerJoinEvent event) {
        final Player player = event.getPlayer();
        addOsTag(player);
    }

    private void addOsTag(Player player) {
        List<String> advancedPlayers = conf.getStringList("advanced-players");
        List<String> disabledWorld = conf.getStringList("disabled-worlds");
        for (String dis : disabledWorld) {
            if (player.getLevel().getName().equalsIgnoreCase(dis)) {
                //disabled worlds is a experimental option, maybe not good working
                return;
            }
        }
        if (advancedPlayers.contains(player.getDisplayName())) {
            OsTagAdd.addDevAdvanced(player);
        } else {
            OsTagAdd.addDevNormal(player);
        }
    }
}