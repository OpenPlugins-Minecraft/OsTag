package me.indian.pl;

import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.plugin.PluginManager;
import com.creeperface.nukkit.placeholderapi.api.PlaceholderAPI;
import me.indian.pl.commands.OsTagCommand;
import me.indian.pl.commands.TestttCommand;
import me.indian.pl.listeners.CpsListener;
import me.indian.pl.listeners.Formater;
import me.indian.pl.listeners.InputListener;
import me.indian.pl.others.OsTagMetrics;
import me.indian.pl.utils.*;

public class OsTag extends PluginBase implements Listener {

    public static boolean luckPerm = false;

    public static boolean papKot = false;

    private static OsTag instance;

    public static OsTag getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        long millisActualTime = System.currentTimeMillis();
        instance = this;
        //some informations
        /*
        None
         */
        PluginManager pm = getServer().getPluginManager();
        //plugins info
        if (pm.getPlugin("LuckPerms") == null) {
            getLogger().warning(ColorUtil.replaceColorCode("&cYou don't have lucky perms , ChatFormating don't corectly work"));
        } else {
            luckPerm = true;
        }
        if (pm.getPlugin("PlaceholderAPI") == null && pm.getPlugin("KotlinLib") == null) {
            getLogger().info(ColorUtil.replaceColorCode("&cYou don't have PlaceholderAPI or kotlin lib,placeholders from \"PlaceholderAPI\" will not work"));
        } else {
            papKot = true;
            registerPlaceholders();
        }

        saveDefaultConfig();

        new OtherUtils();
        new PlayerInfoUtil();

        //register some events

        pm.registerEvents(new CpsListener() , this);
        pm.registerEvents(new InputListener(), this);

        //register some commands

        ((PluginCommand<?>) this.getCommand("ostag")).setExecutor(new OsTagCommand(this));
        ((PluginCommand<?>) this.getCommand("tto")).setExecutor(new TestttCommand(this));



        if (this.getConfig().getBoolean("OsTag")) {
            new OsTagAdd();
            new OsTimer();
            pm.registerEvents(new OsTimer(), this);

            int refreshTime =  getConfig().getInt("refresh-time");

            if(refreshTime <= 0){
                refreshTime = 1;
                getConfig().set("refresh-time", 1);
                this.getConfig().save();
                getLogger().warning(ColorUtil.replaceColorCode("&cRefresh time must be higer than &b0 &c,we will set it up for you!"));
            }

            this.getServer().getScheduler().scheduleRepeatingTask(new OsTimer(), 20 * refreshTime);
        }
        if (this.getConfig().getBoolean("ChatFormater")) {
            pm.registerEvents(new Formater(this), this);
        }

        new OsTagMetrics();
        OsTagMetrics.metricsStart();

        sendOnEnableInfo("admin", getServer().getConsoleSender());

        long executionTime = System.currentTimeMillis() - millisActualTime;
        getLogger().info(ColorUtil.replaceColorCode("&aStarted in &b" + executionTime + " &ams"));
    }


    public void sendOnEnableInfo(String type, CommandSender sender) {

        String ver = this.getDescription().getVersion();
        String aut = String.valueOf(this.getDescription().getAuthors());
        String verNuk = this.getServer().getNukkitVersion();
        String servVer = this.getServer().getVersion();
        String apiVer = this.getServer().getApiVersion();

        switch (type) {
            case "admin":
                sender.sendMessage(ColorUtil.replaceColorCode("&b-------------------------------"));
                sender.sendMessage(ColorUtil.replaceColorCode("&aOsTag version:&3 " + ver));
                sender.sendMessage(ColorUtil.replaceColorCode("&aPlugin by:&6 " + aut.replace("[", "").replace("]", "")));
                sender.sendMessage(ColorUtil.replaceColorCode("&aNukkit Version:&3 " + verNuk));
                sender.sendMessage(ColorUtil.replaceColorCode("&aNukkit Api Version:&3 " + apiVer));
                sender.sendMessage(ColorUtil.replaceColorCode("&aServer Version:&3 " + servVer));
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
                sender.sendMessage(ColorUtil.replaceColorCode("&aOsTag version:&3 " + ver));
                sender.sendMessage(ColorUtil.replaceColorCode("&aPlugin by:&6 " + aut.replace("[", "").replace("]", "")));
                sender.sendMessage(ColorUtil.replaceColorCode("&aServer Version:&3 " + servVer));
                sender.sendMessage(ColorUtil.replaceColorCode(" "));
                sender.sendMessage(ColorUtil.replaceColorCode("&1Modules"));
                sender.sendMessage(ColorUtil.replaceColorCode("&aFormater&3: " + OtherUtils.getFormaterStatus()));
                sender.sendMessage(ColorUtil.replaceColorCode("&aOsTag&3: " + OtherUtils.getOsTagStatus()));
                sender.sendMessage(ColorUtil.replaceColorCode(" "));
                sender.sendMessage(ColorUtil.replaceColorCode("&b-------------------------------"));
                break;
        }
    }

    public void registerPlaceholders() {
        PlaceholderAPI api = PlaceholderAPI.getInstance();
        String prefix = "ostag_";

        api.builder(prefix +"cps", Integer.class)
                .visitorLoader(entry -> {
                    return CpsListener.getCPS(entry.getPlayer());
                })
                .build();

        api.builder(prefix +"test", String.class)
                .visitorLoader(entry -> {
                    return "test placeholder";
                })
                .build();
        api.builder(prefix +"device", String.class)
                .visitorLoader(entry -> {
                    return PlayerInfoUtil.getDevice(entry.getPlayer());
                })
                .build();
        api.builder(prefix +"controler", String.class)
                .visitorLoader(entry -> {
                    return PlayerInfoUtil.getControler(entry.getPlayer());
                })
                .build();
        api.builder(prefix +"prefix", String.class)
                .visitorLoader(entry -> {
                    return PlayerInfoUtil.getLuckPermPrefix(entry.getPlayer());
                })
                .build();
        api.builder(prefix +"suffix", String.class)
                .visitorLoader(entry -> {
                    return PlayerInfoUtil.getLuckPermSufix(entry.getPlayer());
                })
                .build();
        api.builder(prefix +"version", String.class)
                .visitorLoader(entry -> {
                    return entry.getPlayer().getLoginChainData().getGameVersion();
                })
                .build();
    }
}
