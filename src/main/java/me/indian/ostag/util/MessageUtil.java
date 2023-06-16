package me.indian.ostag.util;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.utils.TextFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MessageUtil {

    private static final Server server = Server.getInstance();

    public static String colorize(final String msg) {
        return TextFormat.colorize('&', msg);
    }

    public static String listToSpacedString(List<String> lista) {
        if (lista == null) {
            return "";
        }
        return String.join("\n", lista);
    }

    public static String listToString(List<String> lista) {
        if (lista == null) {
            return "";
        }
        return String.join("- ", lista);
    }


    public static List<String> stringToList(String tekst) {
        if (tekst == null || tekst.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(tekst.split("- "));
    }

    public static void playerCommand(final Player player, final String command) {
        server.dispatchCommand(player, command);
    }

    public static void consoleCommand(final String command) {
        server.dispatchCommand(server.getConsoleSender(), command);
    }

    public static void sendMessageToAll(final String msg) {
        for (final Player all : server.getOnlinePlayers().values()) {
            all.sendMessage(colorize(msg));
        }
    }
}