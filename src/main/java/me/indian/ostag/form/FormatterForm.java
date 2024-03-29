package me.indian.ostag.form;

import cn.nukkit.Player;
import cn.nukkit.level.Sound;
import cn.nukkit.utils.Config;
import com.formconstructor.form.CustomForm;
import com.formconstructor.form.ModalForm;
import com.formconstructor.form.SimpleForm;
import com.formconstructor.form.element.SelectableElement;
import com.formconstructor.form.element.custom.Dropdown;
import com.formconstructor.form.element.custom.Input;
import com.formconstructor.form.element.custom.Label;
import com.formconstructor.form.element.custom.StepSlider;
import com.formconstructor.form.element.custom.Toggle;
import com.formconstructor.form.element.simple.ImageType;
import java.util.ArrayList;
import java.util.List;
import me.indian.ostag.OsTag;
import me.indian.ostag.config.PlayerSettingsConfig;
import me.indian.ostag.util.MessageUtil;
import me.indian.ostag.util.Permissions;

public class FormatterForm {

    private final OsTag plugin;
    private final Form mainForm;
    private final Config config;
    private final Player player;
    private List<String> blockedWords;
    private final PlayerSettingsConfig playerSettingsConfig;

    public FormatterForm(final Form mainForm, final Config config) {
        this.plugin = OsTag.getInstance();
        this.mainForm = mainForm;
        this.config = config;
        this.player = this.mainForm.getFormPlayer();
        this.blockedWords = config.getStringList("BlackWords");
        this.playerSettingsConfig = this.plugin.getPlayerSettingsConfig();
    }

    public void formatterSettings() {
        final SimpleForm form = new SimpleForm("Formatter Settings");

        if (this.player.hasPermission(Permissions.ADMIN)) {
            form.addButton("Chat Format", ImageType.PATH, "textures/ui/editIcon", (p, button) -> this.messageFormat());
        }
        form.addButton("Mention", ImageType.PATH, "textures/ui/icon_bell", (p, button) -> this.mentionModal())
                .addButton("Private messages", ImageType.PATH, "textures/ui/chat_send", (p, button) -> this.msgModal());
        if (this.player.hasPermission(Permissions.ADMIN)) {
            form.addButton("Cooldown", ImageType.PATH, "textures/ui/timer", (p, button) -> this.cooldown())
                    .addButton("Censorship", ImageType.PATH, "textures/ui/mute_on", (p, button) -> this.censorShip());
        }

        this.mainForm.addCloseButton(form);
        form.setNoneHandler(p -> this.mainForm.openSettings());
        form.send(this.player);
    }

    private void messageFormat() {
        final CustomForm form = new CustomForm("Chat Format");
        final boolean andForAll = this.config.getBoolean("And-for-all");
        final boolean breaksBetweenMessages = this.config.getBoolean("break-between-messages.enable");

        form.addElement(new Input("message_format")
                        .setPlaceholder(MessageUtil.colorize("&lMessage format"))
                        .setDefaultValue(this.config.getString("message-format")))
                .addElement(new Label(MessageUtil.colorize("&lAdditional chat features")))
                .addElement("and-for-all", new Toggle("And for all", andForAll))
                .addElement("breaks", new Toggle("Breaks between messages", breaksBetweenMessages));

        form.setHandler((p, response) -> {
            final String messageFormat = response.getInput("message_format").getValue();
            if (!messageFormat.isEmpty()) {
                this.config.set("message-format", messageFormat);
            }

            final boolean finalAndForAll = response.getToggle("and-for-all").getValue();
            if (finalAndForAll != andForAll) {
                this.config.set("And-for-all", finalAndForAll);
            }

            final boolean finalBreaksBetweenMessages = response.getToggle("breaks").getValue();
            if (finalBreaksBetweenMessages != breaksBetweenMessages) {
                this.config.set("break-between-messages.enable", finalBreaksBetweenMessages);
            }

            this.config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&a edited&b " + form.getTitle());
            formatterSettings();
        });
        form.setNoneHandler(p -> formatterSettings());
        form.send(this.player);
    }

    private void cooldown() {
        final CustomForm form = new CustomForm("Cooldown Settings");
        final boolean enabled = this.config.getBoolean("cooldown.enable");
        final List<SelectableElement> elements = new ArrayList<>();
        final int time = Math.toIntExact(this.plugin.getConfig().getLong("cooldown.delay"));

        for (int i = 1; i <= 50; i++) {
            final SelectableElement element = new SelectableElement(String.valueOf(i), i);
            elements.add(element);
        }
        form.addElement(new Label(MessageUtil.colorize("&aEnable cooldown")))
                .addElement("cooldown_enable", new Toggle("Cooldown", enabled));

        if (enabled) {

            form.addElement("delay", new StepSlider(MessageUtil.colorize("&aCooldown time"), elements, time - 1))
                    .addElement(new Input("message")
                            .setPlaceholder(MessageUtil.colorize("&aCooldown message"))
                            .setDefaultValue(this.config.getString("cooldown.message")))
                    
                    .addElement(new Input("over")
                            .setPlaceholder(MessageUtil.colorize("&aCooldown over message"))
                            .setDefaultValue(this.config.getString("cooldown.over")))
                    
                    .addElement(new Input("disabled")
                            .setPlaceholder(MessageUtil.colorize("&aCooldown disabled message"))
                            .setDefaultValue(this.config.getString("cooldown.disabled")))
                    
                    .addElement(new Input("bypass")
                            .setPlaceholder(MessageUtil.colorize("&aCooldown bypass message"))
                            .setDefaultValue(this.config.getString("cooldown.bypass")));
        }

        form.setHandler((p, response) -> {
            this.config.set("cooldown.enable", response.getToggle("cooldown_enable").getValue());
            if (enabled) {
                final SelectableElement element = response.getStepSlider("delay").getValue();

                if (element.getValue() != null && element.getValue(Integer.class) != time) {
                    this.config.set("cooldown.delay", Long.valueOf(element.getValue(Integer.class)));
                }
                this.config.set("cooldown.message", response.getInput("message").getValue());
                this.config.set("cooldown.over", response.getInput("over").getValue());
                this.config.set("cooldown.disabled", response.getInput("disabled").getValue());
                this.config.set("cooldown.bypass", response.getInput("bypass").getValue());
            }
            this.config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&a edited&b " + form.getTitle());
            formatterSettings();
        });

        form.setNoneHandler(p -> formatterSettings());
        form.send(this.player);
    }

    private void censorShip() {
        final CustomForm form = new CustomForm("CensorShip Settings");
        final boolean enabled = this.config.getBoolean("censorship.enable");

        form.addElement(new Label(MessageUtil.colorize("&aEnable censor ship")))
                .addElement("censor_enable", new Toggle("Censorship", enabled));

        if (enabled) {
            form.addElement(new Input("censor")
                            .setName(MessageUtil.colorize("&aCensorShip char"))
                            .setDefaultValue(this.config.getString("censorship.word"))
            );

            form.addElement(new Label(MessageUtil.colorize("&aBlocked words")));
            for (int i = 0; i < this.blockedWords.size(); i++) {
                form.addElement(new Input("blackwords_" + i)
                                .setName((i + 1) + ".")
                                .setDefaultValue(this.blockedWords.get(i))
                );
            }
            form.addElement(new Input("blockword")
                            .setName(MessageUtil.colorize("&lBlock an word"))
                            .setDefaultValue("ExampleWord")
            );
        }

        form.setHandler((p, response) -> {
            this.config.set("censorship.enable", response.getToggle("censor_enable").getValue());
            if (enabled) {
                final List<String> finalBlocked = new ArrayList<>();
                this.config.set("censorship.word", response.getInput("censor").getValue().charAt(0));

                for (int i = 0; i < this.blockedWords.size(); i++) {
                    final String word = response.getInput("blackwords_" + i).getValue();
                    if (!word.isEmpty()) {
                        finalBlocked.add(word);
                    }
                }

                final String blockedWord = response.getInput("blockword").getValue();
                if (!blockedWord.isEmpty() && !blockedWord.equalsIgnoreCase("ExampleWord")) {
                    finalBlocked.add(blockedWord);
                }

                this.blockedWords = finalBlocked;
                this.config.set("BlackWords", this.blockedWords);
            }
            this.config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&a edited&b " + form.getTitle());
            formatterSettings();
        });

        form.setNoneHandler(p -> formatterSettings());
        form.send(this.player);
    }

    private void msgModal() {
        final ModalForm form = new ModalForm("Private messages admin settings");

        form.setContent("Open msg admin settings?\n")
                .setPositiveButton("Yes")
                .setNegativeButton("No");

        if (!this.player.hasPermission(Permissions.ADMIN)) {
            this.msgNormal();
            return;
        }
        if (!this.plugin.msg) {
            this.mentionAdminForm();
            return;
        }

        form.setNoneHandler(p -> this.formatterSettings());

        form.setHandler((p, result) -> {
            if (result) {
                this.msgAdmin();
            } else {
                this.msgNormal();
            }
        });
        form.send(this.player);
    }

    private void msgAdmin() {
        final CustomForm form = new CustomForm("Private messages admin Settings");

        form.addElement(new Label(MessageUtil.colorize("&aEnable cooldown")))
                .addElement("console_logs", new Toggle("Console logs", this.config.getBoolean("Msg.console-logs")));

        form.addElement(new Input("no-one")
                                .setName(MessageUtil.colorize("&aNo one message"))
                                .setDefaultValue(this.config.getString("Msg.no-one")))

                .addElement(new Input("null")
                                .setName(MessageUtil.colorize("&aNull Recipitent"))
                                .setDefaultValue(this.config.getString("Msg.null-recipient")))

                .addElement(new Input("cant-msg")
                                .setName(MessageUtil.colorize("&aCant message"))
                                .setDefaultValue(this.config.getString("Msg.cant-msg")))

                .addElement(new Input("ignored")
                                .setName(MessageUtil.colorize("&aIgnored message"))
                                .setDefaultValue(this.config.getString("Msg.ignored")))

                .addElement(new Input("un-ignored")
                                .setName(MessageUtil.colorize("&aUnIgnored message"))
                                .setDefaultValue(this.config.getString("Msg.un-ignored")))

                .addElement(new Input("you-disabled")
                                .setName(MessageUtil.colorize("&aYou has disabled message"))
                                .setDefaultValue(this.config.getString("Msg.you-disabled")))

                .addElement(new Input("has-disabled")
                                .setName(MessageUtil.colorize("&aHim has disabled message"))
                                .setDefaultValue(this.config.getString("Msg.has-disabled")))

                .addElement(new Input("to-player")
                                .setName(MessageUtil.colorize("&aTo player prefix"))
                                .setDefaultValue(this.config.getString("Msg.to-player")))

                .addElement(new Input("from-player")
                                .setName(MessageUtil.colorize("&aFrom player prefix"))
                                .setDefaultValue(this.config.getString("Msg.from-player")));


        form.setHandler((p, response) -> {
            this.config.set("Msg.console-logs", response.getToggle("console_logs").getValue());

            this.config.set("Msg.no-one", response.getInput("no-one").getValue());
            this.config.set("Msg.null-recipient", response.getInput("null").getValue());
            this.config.set("Msg.cant-msg", response.getInput("cant-msg").getValue());
            this.config.set("Msg.ignored", response.getInput("ignored").getValue());
            this.config.set("Msg.un-ignored", response.getInput("un-ignored").getValue());
            this.config.set("Msg.you-disabled", response.getInput("you-disabled").getValue());
            this.config.set("Msg.has-disabled", response.getInput("has-disabled").getValue());
            this.config.set("Msg.to-player", response.getInput("to-player").getValue());
            this.config.set("Msg.from-player", response.getInput("from-player").getValue());


            this.config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&a edited&b " + form.getTitle());
            formatterSettings();
        });

        form.setNoneHandler(p -> formatterSettings());
        form.send(this.player);
    }

    private void msgNormal() {
        final CustomForm form = new CustomForm("Private messages Settings");
        final List<String> ignored = this.playerSettingsConfig.getIgnoredPlayers(this.player.getName());
        final boolean msg = this.playerSettingsConfig.hasEnabledMsg(this.player);
        final boolean privateMsg = this.playerSettingsConfig.hasEnabledPrivateMsg(this.player);

        form.addElement(MessageUtil.colorize("&aEnable/disable private messages"))
                .addElement("msg_enable", new Toggle("Private Messages", msg));

        if (msg) {
            if (this.player.hasPermission(Permissions.ADMIN)) {
                form.addElement("private_msg_enable", new Toggle("Private Players Messages", msg));
            }

            if (!ignored.isEmpty()) {
                form.addElement(new Label(MessageUtil.colorize("&lIgnored Players")));
            }
            for (final String ignoredPlayer : ignored) {
                form.addElement("ignored_players_" + ignoredPlayer, new Toggle(ignoredPlayer, this.playerSettingsConfig.isIgnored(this.player, ignoredPlayer)));
            }

            form.addElement(new Label(MessageUtil.colorize("&lDetected Players")));
            for (final Player all : this.plugin.getServer().getOnlinePlayers().values()) {
                if (all == this.player) continue;
                final String detectedPlayerName = all.getName();
                if (this.playerSettingsConfig.isIgnored(this.player, detectedPlayerName)) {
                    continue;
                }
                form.addElement("detected_players_" + detectedPlayerName, new Toggle(detectedPlayerName, this.playerSettingsConfig.isIgnored(this.player, detectedPlayerName)));
            }

        }


        form.setHandler((p, response) -> {
            final boolean finalMsg = response.getToggle("msg_enable").getValue();
            if (finalMsg != msg) {
                this.playerSettingsConfig.setEnabledMsg(this.player, finalMsg);
            }

            if (msg) {
                if (this.player.hasPermission(Permissions.ADMIN)) {
                    final boolean finalPrivateMsg = response.getToggle("private_msg_enable").getValue();
                    if (finalPrivateMsg != privateMsg) {
                        this.playerSettingsConfig.setEnabledMsg(this.player, finalPrivateMsg);
                    }
                }

                for (final String ignoredPlayer : ignored) {
                    if (response.getToggle("ignored_players_" + ignoredPlayer).getValue()) {
                        this.playerSettingsConfig.ignorePlayer(player, ignoredPlayer);
                    } else {
                        this.playerSettingsConfig.unIgnorePlayer(player, ignoredPlayer);
                    }
                }

                for (final Player all : this.plugin.getServer().getOnlinePlayers().values()) {
                    if (all == this.player) continue;
                    final String detectedPlayerName = all.getName();

                    final Toggle toggle = response.getToggle("detected_players_" + detectedPlayerName);
                    if (toggle != null && toggle.getValue()) {
                        this.playerSettingsConfig.ignorePlayer(player, detectedPlayerName);
                    } else {
                        this.playerSettingsConfig.unIgnorePlayer(player, detectedPlayerName);
                    }
                }
            }
            this.config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&a edited&b " + form.getTitle());
            formatterSettings();
        });

        form.setNoneHandler(p -> formatterSettings());
        form.send(this.player);
    }


    private void mentionModal() {
         /*
           This is for Mention Sound function and it is still experimental
         */

        final ModalForm form = new ModalForm("Mention admin settings");

        form.setContent("Open mention admin settings?\n")
                .setPositiveButton("Yes")
                .setNegativeButton("No");

        if (!this.player.hasPermission(Permissions.ADMIN)) {
            this.mentionNormal();
            return;
        }
        if (!this.playerSettingsConfig.mentionSoundFunctionEnabled()) {
            this.mentionAdminForm();
            return;
        }

        form.setNoneHandler(p -> this.formatterSettings());

        form.setHandler((p, result) -> {
            if (result) {
                this.mentionAdminForm();
            } else {
                this.mentionNormal();
            }
        });
        form.send(this.player);
    }

    private void mentionAdminForm() {
          /*
           This is for Mention Sound function and it is still experimental
         */

        final CustomForm form = new CustomForm("Mentions Admin Settings");
        final boolean functionEnabled = this.playerSettingsConfig.mentionSoundFunctionEnabled();
        final int soundsValue = this.playerSettingsConfig.getSoundsValue();
        final List<SelectableElement> maxSounds = new ArrayList<>();


        if (this.player.hasPermission(Permissions.ADMIN)) {
            form.addElement(MessageUtil.colorize("&4Admin Settings"))
                    .addElement("mentionsound", new Toggle("Mention Sound Function", this.playerSettingsConfig.mentionSoundFunctionEnabled()));
            if (functionEnabled) {
                for (int i = 1; i <= soundsValue + 100; i++) {
                    final SelectableElement element = new SelectableElement(String.valueOf(i), i);
                    maxSounds.add(element);
                }
                form.addElement("maxsounds", new StepSlider("Sounds value", maxSounds, soundsValue - 1));
            }
        }

        form.setHandler((p, response) -> {
            final boolean finalFunctionEnabled = response.getToggle("mentionsound").getValue();
            if (this.player.hasPermission(Permissions.ADMIN)) {
                if (finalFunctionEnabled != functionEnabled) {
                    this.playerSettingsConfig.setMentionSoundFunctionEnabled(finalFunctionEnabled);
                }
                if (functionEnabled) {
                    final SelectableElement sounds = response.getStepSlider("maxsounds").getValue();
                    if (sounds.getValue() != null && sounds.getValue(Integer.class) != soundsValue) {
                        this.playerSettingsConfig.setSoundsValue(sounds.getValue(Integer.class));
                    }
                }
            }

            this.config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            this.mainForm.formLogger("&aPlayer&6 " + p.getName() + "&a edited&b " + form.getTitle());
            formatterSettings();
        });
        form.setNoneHandler(p -> formatterSettings());
        form.send(this.player);
    }

    private void mentionNormal() {
          /*
           This is for Mention Sound function and it is still experimental
         */

        final CustomForm form = new CustomForm("Mentions Settings");
        final boolean functionEnabled = this.playerSettingsConfig.mentionSoundFunctionEnabled();
        final boolean playerEnabled = this.playerSettingsConfig.hasEnabledMentions(this.player);
        final boolean title = this.playerSettingsConfig.hasEnabledTitle(this.player);
        final int soundsValue = this.playerSettingsConfig.getSoundsValue();
        final List<SelectableElement> elements = new ArrayList<>();
        final int currentSoundIndex = this.playerSettingsConfig.getSoundIndex(this.playerSettingsConfig.getMentionSound(this.player));

        if (!functionEnabled) {
            this.player.sendMessage(MessageUtil.colorize("&cThis function disabled by Admin"));
            this.formatterSettings();
            return;
        }

        if (playerEnabled) {
            int counter = 0;
            for (final Sound sound : Sound.values()) {
                final SelectableElement element = new SelectableElement(sound.name(), counter);
                elements.add(element);
                counter++;
                if (counter == soundsValue) {
                    if (currentSoundIndex > counter) {
                        final SelectableElement currentSound = new SelectableElement(this.playerSettingsConfig.getSoundByIndex(currentSoundIndex), counter);
                        this.playerSettingsConfig.setPlayerCustomIndex(this.player, counter);
                        elements.add(currentSound);
                    }
                    break;
                }
            }
        }

        form.addElement(new Label(MessageUtil.colorize("&aEnable mentions")))
                .addElement("mentions_enable", new Toggle("Mention", playerEnabled));

        if (playerEnabled) {
            form.addElement(new Label(MessageUtil.colorize("&aEnable title info")))
                    .addElement("title_info_enable", new Toggle("Title info", title))
                    .addElement(new Label(MessageUtil.colorize("&aYour mention sound: &b" + this.playerSettingsConfig.getMentionSound(this.player))))
                    .addElement("soundname", new Dropdown(MessageUtil.colorize("&aMention Sound"), elements, this.playerSettingsConfig.getPlayerCustomIndex(this.player)))
                    .addElement("indexes", new Toggle("Show Indexes", false))
                    .addElement("customsound",
                              new Input()
                                    .setName(MessageUtil.colorize("&aAdd sound by index"))
                    );
        }

        form.setHandler((p, response) -> {
            final boolean finalPlayerEnabled = response.getToggle("mentions_enable").getValue();
            if (finalPlayerEnabled != playerEnabled) {
                this.playerSettingsConfig.setEnabledMentions(this.player, finalPlayerEnabled);
            }

            if (playerEnabled) {
                final boolean finalTitle = response.getToggle("title_info_enable").getValue();
                final SelectableElement element = response.getDropdown("soundname").getValue();
                this.playerSettingsConfig.setEnabledTitle(this.player, finalTitle);
                if (element.getValue() != null) {
                    if (element.getValue(Integer.class) != this.playerSettingsConfig.getPlayerCustomIndex(this.player)) {
                        this.playerSettingsConfig.setMentionSound(this.player, this.playerSettingsConfig.getSoundByIndex(element.getValue(Integer.class)));
                    }
                }

                final String customSound = response.getInput("customsound").getValue();
                if (customSound != null) {
                    int index;
                    try {
                        index = Integer.parseInt(customSound);
                        this.playerSettingsConfig.setMentionSound(this.player, this.playerSettingsConfig.getSoundByIndex(index));
                    } catch (final NumberFormatException ex) {
                        this.player.sendMessage(MessageUtil.colorize("&cIndex must be an integer"));
                    }
                }
                if (response.getToggle("indexes").getValue()) {
                    this.customIndexes();
                }
            }
            this.config.save();
            p.sendMessage(MessageUtil.colorize("&aSaved changes"));
            this.formatterSettings();
        });
        form.setNoneHandler(p -> this.formatterSettings());
        form.send(this.player);
    }

    public void customIndexes() {
        final SimpleForm form = new SimpleForm("All sound indexes");

        for (final Sound sound : Sound.values()) {
            final String name = sound.name();
            final int index = this.playerSettingsConfig.getSoundIndex(name);

            if (this.playerSettingsConfig.getMentionSound(player).equalsIgnoreCase(name)) {
                form.addContent(MessageUtil.colorize("&b" + index + " &6" + name + " &d(&bYour&d)\n"));
            } else {
                form.addContent(MessageUtil.colorize("&b" + index + " &a" + name + "\n"));
            }
        }

        this.mainForm.addCloseButton(form);
        form.setNoneHandler((p) -> this.mentionNormal());
        form.send(player);
    }
}