package me.indian.ostag;

import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginDescription;
import cn.nukkit.plugin.PluginManager;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import me.indian.ostag.commands.OsTagCommand;
import me.indian.ostag.commands.TestttCommand;
import me.indian.ostag.listeners.CpsListener;
import me.indian.ostag.listeners.Formater;
import me.indian.ostag.listeners.InputListener;
import me.indian.ostag.others.OsTagMetrics;
import me.indian.ostag.utils.ColorUtil;
import me.indian.ostag.utils.OsTimer;
import me.indian.ostag.utils.OtherUtils;
import me.indian.ostag.utils.PlayerInfoUtil;

public class OsTag extends PluginBase {

    public static boolean luckPerm = false;
    public static boolean papKot = false;
    public static boolean serverMovement;
    private Formater formater;
    private static OsTag instance;

    public static OsTag getInstance() {
        return instance;
    }

    public Formater getFormater() {
        return this.formater;
    }

    @Override
    public void onEnable() {
        final long millisActualTime = System.currentTimeMillis();
        instance = this;
        this.formater = new Formater(this);
        final PluginManager pm = getServer().getPluginManager();
        if (pm.getPlugin("LuckPerms") == null) {
            getLogger().warning(ColorUtil.replaceColorCode("&cYou don't have lucky perms , ChatFormatting don't correctly work"));
        } else {
            luckPerm = true;
        }
        if (pm.getPlugin("PlaceholderAPI") == null && pm.getPlugin("KotlinLib") == null) {
            getLogger().info(ColorUtil.replaceColorCode("&cYou don't have PlaceholderAPI or kotlin lib,placeholders from &b\"PlaceholderAPI\"&c will not work"));
        } else {
            papKot = true;
            registerPlaceholders();
        }
        saveDefaultConfig();
        if (getConfig().getBoolean("Disable")) {
            getLogger().warning(ColorUtil.replaceColorCode("&4Disabling plugin due to disable in config"));
            pm.disablePlugin(this);
            return;
        }
        serverMovement = getConfig().getBoolean("movement-server");
        someNewInfo();
        pm.registerEvents(new CpsListener(), this);
        if (serverMovement) {
            pm.registerEvents(new InputListener(), this);
        }
        ((PluginCommand<?>) getCommand("ostag")).setExecutor(new OsTagCommand(this));
        ((PluginCommand<?>) getCommand("tto")).setExecutor(new TestttCommand(this));
        if (getConfig().getBoolean("OsTag")) {
            pm.registerEvents(new OsTimer(), this);
            int refreshTime = getConfig().getInt("refresh-time");
            if (refreshTime <= 0) {
                refreshTime = 1;
                getConfig().set("refresh-time", 1);
                getConfig().save();
                getLogger().warning(ColorUtil.replaceColorCode("&cRefresh time must be higer than &b0 &c,we will set it up for you!"));
            }
            getServer().getScheduler().scheduleRepeatingTask(new OsTimer(), 20 * refreshTime);
        }
        if (this.getConfig().getBoolean("ChatFormater")) {
            pm.registerEvents(new Formater(this), this);
        }
        OsTagMetrics.metricsStart();
        sendOnEnableInfo("admin", getServer().getConsoleSender());
        betaDetect();
        long executionTime = System.currentTimeMillis() - millisActualTime;
        getLogger().info(ColorUtil.replaceColorCode("&aStarted in &b" + executionTime + " &ams"));
    }


    public void sendOnEnableInfo(String type, CommandSender sender) {
        PluginDescription descriptor = this.getDescription();
        Server server = this.getServer();

        String pluginVersion = descriptor.getVersion();
        String authors = String.valueOf(descriptor.getAuthors()).replace("[", "").replace("]", "");
        String nukkitVersion = server.getNukkitVersion();
        String serverVersion = server.getVersion();
        String apiVersion = server.getApiVersion();
        switch (type) {
            case "admin":
                sender.sendMessage(ColorUtil.replaceColorCode("&b-------------------------------"));
                sender.sendMessage(ColorUtil.replaceColorCode("&aOsTag version:&3 " + pluginVersion));
                sender.sendMessage(ColorUtil.replaceColorCode("&aPlugin by:&6 " + authors));
                sender.sendMessage(ColorUtil.replaceColorCode("&aNukkit Version:&3 " + nukkitVersion));
                sender.sendMessage(ColorUtil.replaceColorCode("&aNukkit Api Version:&3 " + apiVersion));
                sender.sendMessage(ColorUtil.replaceColorCode("&aServer Version:&3 " + serverVersion));
                sender.sendMessage(ColorUtil.replaceColorCode(" "));
                sender.sendMessage(ColorUtil.replaceColorCode("&1Modules"));
                sender.sendMessage(ColorUtil.replaceColorCode("&aFormater&3: " + OtherUtils.getFormaterStatus()));
                sender.sendMessage(ColorUtil.replaceColorCode("&aOsTag&3: " + OtherUtils.getOsTagStatus()));
                sender.sendMessage(ColorUtil.replaceColorCode(" "));
                sender.sendMessage(ColorUtil.replaceColorCode("&1Plugins"));
                sender.sendMessage(ColorUtil.replaceColorCode("&aLuckPerms&3: " + OtherUtils.getLuckPermStatus()));
                sender.sendMessage(ColorUtil.replaceColorCode("&aKotlinLib & PlaceholderAPI&3: " + OtherUtils.getKotOrPapiStatus()));
                sender.sendMessage(ColorUtil.replaceColorCode(" "));
                sender.sendMessage(ColorUtil.replaceColorCode("&b-------------------------------"));
                break;
            case "normal":
                sender.sendMessage(ColorUtil.replaceColorCode("&b-------------------------------"));
                sender.sendMessage(ColorUtil.replaceColorCode("&aOsTag version:&3 " + pluginVersion));
                sender.sendMessage(ColorUtil.replaceColorCode("&aPlugin by:&6 " + authors));
                sender.sendMessage(ColorUtil.replaceColorCode("&aServer Version:&3 " + serverVersion));
                sender.sendMessage(ColorUtil.replaceColorCode(" "));
                sender.sendMessage(ColorUtil.replaceColorCode("&1Modules"));
                sender.sendMessage(ColorUtil.replaceColorCode("&aFormater&3: " + OtherUtils.getFormaterStatus()));
                sender.sendMessage(ColorUtil.replaceColorCode("&aOsTag&3: " + OtherUtils.getOsTagStatus()));
                sender.sendMessage(ColorUtil.replaceColorCode(" "));
                sender.sendMessage(ColorUtil.replaceColorCode("&b-------------------------------"));
                break;
            default:
                sender.sendMessage("Unknown OnEnableInfo type");
                break;
        }
    }

    private void registerPlaceholders() {
        PlaceholderAPI api = PlaceholderAPI.getInstance();
        String prefix = "ostag_";
        api.builder(prefix + "cps", Integer.class)
                .visitorLoader(entry -> CpsListener.getCPS(entry.getPlayer()))
                .build();
        api.builder(prefix + "cooldown", String.class)
                .visitorLoader(entry -> getFormater().cooldown(entry.getPlayer()))
                .build();
        api.builder(prefix + "device", String.class)
                .visitorLoader(entry -> PlayerInfoUtil.getDevice(entry.getPlayer()))
                .build();
        api.builder(prefix + "controller", String.class)
                .visitorLoader(entry -> PlayerInfoUtil.getController(entry.getPlayer()))
                .build();
        api.builder(prefix + "preffix", String.class)
                .visitorLoader(entry -> PlayerInfoUtil.getLuckPermPreffix(entry.getPlayer()))
                .build();
        api.builder(prefix + "suffix", String.class)
                .visitorLoader(entry -> PlayerInfoUtil.getLuckPermSuffix(entry.getPlayer()))
                .build();
        api.builder(prefix + "group", String.class)
                .visitorLoader(entry -> PlayerInfoUtil.getLuckPermGroupDisName(entry.getPlayer()))
                .build();
        api.builder(prefix + "version", String.class)
                .visitorLoader(entry -> entry.getPlayer().getLoginChainData().getGameVersion())
                .build();
    }

    private void betaDetect() {
        if (this.getDescription().getVersion().contains("Beta") || this.getDescription().getVersion().contains("beta")) {
            getLogger().warning(ColorUtil.replaceColorCode("&4You are running beta version, it may not be stable"));
        }
    }

    private void someNewInfo() {
        getLogger().warning(ColorUtil.replaceColorCode("&4If you used old versions, remove config to generate a new one!!!"));
        getLogger().info(ColorUtil.replaceColorCode("&aNow we have so many naming fixes, see changelog "));
    }
}
