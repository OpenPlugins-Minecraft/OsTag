package me.indian.pl.listeners;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.network.protocol.LevelSoundEventPacket;
import me.indian.pl.OsTag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CpsListener implements Listener {

    private final OsTag plugin;

    private static HashMap<Player, List<Long>> cps =  new HashMap<Player, List<Long>>();

    public CpsListener(OsTag plugin) {
        this.plugin = plugin;
    }



    // cps counter from https://github.com/GommeAWM/CPSCounter
    // witch permisions from author

    @EventHandler
    public void onPacket(DataPacketReceiveEvent event) {
        if (!(event.getPacket() instanceof LevelSoundEventPacket)) return;

        LevelSoundEventPacket packet = (LevelSoundEventPacket) event.getPacket();

        if (packet.sound != LevelSoundEventPacket.SOUND_ATTACK && packet.sound != LevelSoundEventPacket.SOUND_ATTACK_NODAMAGE &&
                packet.sound != LevelSoundEventPacket.SOUND_ATTACK_STRONG) return;

        List<Long> cpsList = cps.get(event.getPlayer());

        if (cpsList == null) {
            cpsList = new ArrayList<>();
        }

        cpsList.add(System.currentTimeMillis());
        cps.remove(event.getPlayer());
        cps.put(event.getPlayer(), cpsList);

    }
    public static int getCPS(Player player) {
        final List<Long> list = CpsListener.cps.get(player);
        if (list == null) {
            return 0;
        }
        list.removeIf(l -> l < System.currentTimeMillis() - 1000L);
        return list.size();
    }

    // cps counter from https://github.com/GommeAWM/CPSCounter
    // witch permisions from author


}