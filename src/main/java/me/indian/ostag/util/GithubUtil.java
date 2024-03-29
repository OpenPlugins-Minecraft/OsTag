package me.indian.ostag.util;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.plugin.PluginLogger;
import me.indian.ostag.OsTag;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.TreeMap;

public class GithubUtil {

    private static final OsTag plugin = OsTag.getInstance();
    private static final PluginLogger logger = plugin.getLogger();
    private static final Map<Integer, String> versions = new TreeMap<>();
    private static final StringBuilder response = new StringBuilder();
    private static final String debugPrefix = MessageUtil.colorize(plugin.publicDebugPrefix + "&8[&dLatest tag&8] ");
    private static final String current = OsTag.getInstance().getDescription().getVersion();
    private static final String errorMessage = "&cCan't get latest tag";

    public static String getFastTagInfo() {
        final String latest = getLatestTag();
        if (latest.equals(errorMessage)) {
            return MessageUtil.colorize(errorMessage);
        }
        if (current.equals(latest)) {
            return MessageUtil.colorize("&etrue");
        } else {
            return MessageUtil.colorize("&4false");
        }
    }

    public static String checkTagCompatibility() {
        final String latest = getLatestTag();
        String tag = "&aYou are running latest version";
        if (latest.equals(errorMessage)) {
            return MessageUtil.colorize(errorMessage);
        }
        if (!current.equals(latest)) {
            tag = "&aNew update available, your version &b" + current + "&a latest version &b" + latest;
            plugin.getUpdateUtil().setDownloadStatus(MessageUtil.colorize("&aYou can download latest version"));
            // add /ostag update info
        }
        return MessageUtil.colorize(tag);
    }

    public static String getLatestTag() {
        try {
            final URL url = new URL("https://api.github.com/repos/OpenPlugins-Minecraft/OsTag/tags");
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/vnd.github.v3+json");

            final int responseCode = connection.getResponseCode();
            if (!(responseCode == HttpURLConnection.HTTP_OK)) {
                if (plugin.debug) {
                    logger.error(MessageUtil.colorize(debugPrefix + "&cCan't get latest tag, HTTP response code: " + responseCode));
                }
                return errorMessage;
            }

            final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();

            return parseLatestTagFromJson(response.toString());
        } catch (final Exception e) {
            if (plugin.debug) {
                e.printStackTrace();
            }
            return errorMessage;
        }
    }

    public static String getBehindCount(final CommandSender sender) {
        final int index = getBehindIntCount(sender);

        if (index == -1) {
            return MessageUtil.colorize(" &5(&cBehind more than 10 version&5)");
        }
        if (index == 0) {
            return "";
        }

        return MessageUtil.colorize(" &5(&c" + index + " &dVersions behind&5)");
    }


    public static int getBehindIntCount(final CommandSender sender) {
        if (response.toString().isEmpty()) return 0;
        final String[] tags = response.toString().replaceAll("[\\[\\]{}\"]", "").split(",");

        int counter = 0;
        int index = -1;

        for (int i = 0; i < 66; i += 6) {
            versions.put(counter, tags[i].split(":")[1]);
            counter++;
        }

        if (plugin.debug && !(sender instanceof Player)) {
            for (final Map.Entry<Integer, String> entry : versions.entrySet()) {
                logger.info(debugPrefix + entry.getKey() + ": " + entry.getValue());
            }
        }

        for (final Map.Entry<Integer, String> entry : versions.entrySet()) {
            if (entry.getValue().equals(current)) {
                index = entry.getKey();
                break;
            }
        }

        return index;
    }


    private static String parseLatestTagFromJson(final String json) {
        final String[] tags = json.replaceAll("[\\[\\]{}\"]", "").split(",");
        return tags[0].split(":")[1];
    }
}
