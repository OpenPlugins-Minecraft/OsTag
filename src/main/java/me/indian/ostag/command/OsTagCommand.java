package me.indian.ostag.command;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandEnum;
import cn.nukkit.command.data.CommandParamType;
import cn.nukkit.command.data.CommandParameter;
import cn.nukkit.scheduler.NukkitRunnable;
import cn.nukkit.utils.Config;
import me.indian.ostag.OsTag;
import me.indian.ostag.form.Form;
import me.indian.ostag.util.MessageUtil;
import me.indian.ostag.util.Permissions;
import me.indian.ostag.util.PluginInfoUtil;

import java.util.ArrayList;
import java.util.List;

public class OsTagCommand extends Command {

    private final OsTag plugin;
    private final List<String> confirmations;
    private String lastException = "";

    public OsTagCommand(final OsTag plugin) {
        super("ostag", "ostag management", MessageUtil.colorize("&aUsage &b/ostag &8[version , reload , add <player>, update, menu]"));

        commandParameters.clear();
//        /ostag reload
        commandParameters.put("reload", new CommandParameter[]{
                CommandParameter.newEnum("reload", new CommandEnum("reload", "reload")),
        });
//        /ostag update
        commandParameters.put("update", new CommandParameter[]{
                CommandParameter.newEnum("update", new CommandEnum("update", "update"))
        });
//        /ostag version
        commandParameters.put("version", new CommandParameter[]{
                CommandParameter.newEnum("version", new CommandEnum("version", "version"))
        });
//        /ostag menu
        commandParameters.put("menu", new CommandParameter[]{
                CommandParameter.newEnum("menu", new CommandEnum("menu", "menu"))
        });
//        /ostag add <player>
        commandParameters.put("add", new CommandParameter[]{
                CommandParameter.newEnum("add", new CommandEnum("add", "add")),
                CommandParameter.newType("add", CommandParamType.TARGET)
        });
        this.plugin = plugin;
        this.confirmations = new ArrayList<>();
    }

    @Override
    public boolean execute(final CommandSender sender, final String s, final String[] args) {
        final Config config = this.plugin.getConfig();
        final List<String> advancedPlayers = config.getStringList("advanced-players");

        if (args.length == 0) {
            sender.sendMessage(this.usageMessage);
            return false;
        }
        if (args[0].equalsIgnoreCase("menu")) {
            if (sender instanceof Player) {
                final Player player = (Player) sender;
                if (!plugin.formConstructor) {
                    if (player.isOp()) {
                        player.sendMessage(MessageUtil.colorize("&cYou don't have &bFormConstructor&c plugin !"));
                        player.sendMessage(MessageUtil.colorize("&cthis is required for&b Mention Sound&c to work! !"));
                        player.sendMessage(MessageUtil.colorize("&cDownload it from here&b https://github.com/OpenPlugins-Minecraft/OsTag/tree/main/libs!"));
                    } else {
                        player.sendMessage(MessageUtil.colorize("&cYThis server don't have &bFormConstructor&b plugin !"));
                        player.sendMessage(MessageUtil.colorize("&cthis is required for&b Mention Sound&c to work! !"));
                    }
                    return false;
                }
                new Form(player).runOstagFrom();
            } else {
                sender.sendMessage(MessageUtil.colorize("&cThis command only is for a player!"));
            }
            return false;
        }
        if (args[0].equalsIgnoreCase("version") || args[0].equalsIgnoreCase("v")) {
            final PluginInfoUtil infoUtil = new PluginInfoUtil(sender, true);
            if (sender.hasPermission(Permissions.ADMIN)) {
                for (final String adminInfo : infoUtil.getAdminInfo()) {
                    sender.sendMessage(MessageUtil.colorize(adminInfo));
                }
            } else {
                for (final String normalInfo : infoUtil.getNormalInfo()) {
                    sender.sendMessage(MessageUtil.colorize(normalInfo));
                }
            }
            return false;
        }
        if (sender.hasPermission(Permissions.ADMIN)) {
            if (args[0].equalsIgnoreCase("add")) {
                try {
                    final Player target = Server.getInstance().getPlayer(args[1]);
                    if (target == null) {
                        sender.sendMessage(MessageUtil.colorize("&cplayer &6" + args[1] + "&c does not exist"));
                        return false;
                    }
                    if (advancedPlayers.contains(target.getDisplayName())) {
                        advancedPlayers.remove(target.getDisplayName());
                        sender.sendMessage(MessageUtil.colorize("&6" + target.getDisplayName() + " &chas been removed from advanced player list"));
                        config.set("advanced-players", advancedPlayers);
                    } else {
                        advancedPlayers.add(target.getDisplayName());
                        sender.sendMessage(MessageUtil.colorize("&6" + target.getDisplayName() + " &ahas been added to advanced player list"));
                        config.set("advanced-players", advancedPlayers);
                    }
                } catch (final Exception e) {
                    sender.sendMessage(MessageUtil.colorize("&cYou must give player name! "));
                }
            }
            if (args[0].equalsIgnoreCase("update")) {
                if (this.confirmations.contains(sender.getName())) {
                    this.confirmations.remove(sender.getName());
                    sender.sendMessage(MessageUtil.colorize("&aAction confirmed"));
                    this.plugin.getUpdateUtil().manualUpDate(sender);
                } else {
                    this.confirmations.add(sender.getName());
                    sender.sendMessage(MessageUtil.colorize("&cAre you sure you want to confirm this action? enter the command again to confirm, you have 30 seconds"));
                    this.timeRemove(sender);
                }
            }
            if (args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("r")) {
                reloadConfig(sender);
            }
            config.save();
        } else {
            sender.sendMessage(MessageUtil.colorize("&cYou don't have permissions"));
        }
        return false;
    }

    public String getLastException() {
        return this.lastException;
    }

    public void reloadConfig(final CommandSender sender) {
        try {
            final long millisActualTime = System.currentTimeMillis();
            plugin.getConfig().reload();
            sender.sendMessage(MessageUtil.colorize("&aConfig Reloaded"));
            final long executionTime = System.currentTimeMillis() - millisActualTime;
            this.lastException = "";
            sender.sendMessage(MessageUtil.colorize("&aReloaded in &b" + executionTime + " &ams"));
        } catch (final Exception exception) {
            sender.sendMessage(MessageUtil.colorize("&cCan't reload config , check console or reload menu to see error"));
            this.lastException = exception.getMessage();
            this.plugin.getLogger().error(exception.getMessage());
        }
    }

    private void timeRemove(final CommandSender sender) {
        new NukkitRunnable() {
            @Override
            public void run() {
                if (OsTagCommand.this.confirmations.contains(sender.getName())) {
                    OsTagCommand.this.confirmations.remove(sender.getName());
                    sender.sendMessage(MessageUtil.colorize("&cYour time to confirm has expired"));
                }
            }
        }.runTaskLater(this.plugin, 30 * 20);
    }
}