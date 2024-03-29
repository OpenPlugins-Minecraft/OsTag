### **Version: 1.4.8** <br/>
**09.12.2022** <br/>

The plugin now has so many naming changes, see the plugin page on CloudBurst to know them all and get it right <br/>
Changed name from `OstagPNX` to `OsTag` <br/>
Changed permision names <br/>

Permisions: <br/>
`ostag.admin` - all comands and colors in chat <br/>
`ostag.colors` - allow to use `§` in chat <br/>

Added support for Nukkit (Vanilla) (required java 8 or higer) <br/>
Added Ping colors Added in config.yml <br/>
Added colors improvement in `/ostag ver` and start message <br/>
Added showing plugin activation speed <br/>
Now you can use `/ostag r` for reload plugin <br/>
Now you can use `&` for colors in config <br/>
Now you can use `&` for colors in chat if you have `ostag.colors` and disable `§` in chat <br/>
When the plugin encounters an error while reloading the config, it will inform about it and show an error in the
console <br/>

------------------------------------------------------------------------

### **11.12.2022** <br/>
**Version: 1.4.8.01** <br/>
Now if you want everyone to be able to use `&` by default instead of `§` set `and-for-all` to true <br/>
Still canot use `§` in chat , I try to fix it <br/>
Now  `<suffix>` `<prefix>` `<groupDisName>` support `&` in chat <br/>
Now you can use <xp> in name and chat <br/>
Block `\n` in chat formating <br/>
Changed `message.format` to `message-format` <br/>

----------------------------------------------------------------------------

### **Version: 1.4.9** <br/>
**Started in 11.12.2022 released on the day 12.12.2022**  <br/>

Config look improvements <br/>
When you just type only `/ostag` it will bring up a list of parameters you can use <br/>
Added `<unique-description>` using it in the `Players` section you can add an emoji or some text for this player
only <br/>
How to create custom emojis? Visit this page https://wiki.bedrock.dev/concepts/emojis.html#custom-emoji <br/>
How to make player face emoji? Visit this page https://mcskins.top/avatar-maker <br/>
Changed some naming in config again! <br/>

```yml
cooldown.enable: true
cooldown.delay: 10
cooldown.message: "aYou must wait &b<left> &eSeconds&a"
```

Changed to <br/>

```yml
cooldown:
  enable: true
  delay: 10
  message: "aYou must wait &b<left> &eSeconds&a!"
```

And <br/>

```yml
enable-censorship: true
censorship: "Bread" 
```

Changed to

```yml
censorship:
  word: "Bread"
  enable: true 
```

Added breaks between messages <br/>

-----------------------------------

### **Version: 1.4.9.01** <br/>
**Released on the day 15.12.2022** <br/>

No breaks between messages when player was operator, fixed! <br/>
some improvements in code <br/>

-----------------------------------

### **1.4.9.02** <br/>
**Released on the day 17.12.2022** <br/>

Code improvements <br/>
Now `§` working in chat :D <br/>
Added disable ostag in a given world! if the player logs into a world where it is allowed, the name and information will
remain until no other plugin changes it or enters a allowed world <br/>
Factions plugin support (Placeholder `<faction>`) <br/>

---------------------------------

### **1.5.0** <br/>
**Released on the day 18.12.2022** <br/>

Added PlaceholderAPI support <br/>
Removed `<faction>` placeholder <br/>
Want to use OsTag but don't want to use PlaceholderAPI? You can still use it without the placeholder api

-----------------------------------

### **1.5.1** <br/>
**Released on the day 18.12.2022** <br/>

Code improvements <br/>
Bug fixes (`<device>` placeholder) <br/>

------------------------------------

### **1.5.2** <br/>
**Released on the day 13.12.2022** <br/>

Big code improvements <br/>
Now add `Windows Phone` in <device> placeholder <br/>
Now when you move away from the player you only see the name line <br/>
Added placeholders that already existed but can now be used in other plugins using PlaceholderAPI <br/>
`%ostag_cps%`
`%ostag_test%`
`%ostag_device%`
`%ostag_controler%`
`%ostag_prefix%`
`%ostag_suffix%`  <br/>
Delated <deathskull> placeholder , now this placeholder is in DeathSkull plugin <br/>

-----------------------------------------------

### **1.5.3** <br/>
**Released on the day 03.01.2023** <br/>

Added better support for `PowerNukkit` <br/>
Added in `<gamemode>` placeholder spectator detect <br/>
Code improvements <br/>
Added `%ostag_group%` placeholder to papi <br/>
changed `PowerNukkiX-movement-server` to `movement-server` <br/>

----------------------------------------------

### **1.5.4** <br/>
**Released on the day 22.02.2023** <br/>

If you used old versions, remove config to generate a new one!!! <br/>
Some code improvements and so many code style fixes <br/>
Windows phone and gear vr removed from <device> placeholder , because not suported by mojang <br/>
`
IMPORTANT NEWS:
As of October 2020, Minecraft will no longer be updated or supported on Windows 10 Mobile, Gear VR, iOS 10 or lower & Android devices with less than 768MB of RAM.
`<br/>
Added in `<device>` placeholder `Linux` detect <br/>
Smal naming changes , check new config in
github (https://github.com/IndianBartonka/OsTag/blob/main/src/main/resources/config.yml) <br/>
You can choose whether to display name or score tag <br/>
Added food placeholders `<food>` and `<food_max>` <br/>
now `breaks between messages` don't require enable cooldown <br/>
Cooldown fix and cooldown placeholder `%ostag_cooldown%` <br/>

---------------------------

### **1.5.5** <br/>
**Released on the day 28.03.2023** <br/>

__Naming changes__: <br/>
Fixed naming `<controler>` and `%ostag_controler%` to `<controller>` `%ostag_controller%` and also fixed `<preffix>`
and `%ostag_preffix%` and `<unique_description>` <br/>
Changed in Modules section  `ChatFormater`to `ChatFormatter` in config <br/>

__New things__: <br/>
Added `PrefixesUtil` interface to faster changing plugin placeholder naming (my first interface ~IndianPL) <br/>
Added 35 and 5 levels in `<xp>` placeholder <br/>
Added `%ostag_xp%` placeholder for PlaceholderApi <br/>

__Fixes__: <br/>
Code style fixes and efficency <br/>
Fixed `Dedicated` in `<device>` placeholder <br/>
Fixed decimals in `<health>` placeholder <br/>
Cooldown fix <br/>

__Others__: <br/>
Cooldown disable info <br/>
Better code optymalization <br/>

---------------------------------------------------------------

##### **1.5.6** <br/>
**Released on the day 17.05.2023** <br/>

__Naming changes__: <br/>
none

__New things__: <br/>
Update checker (you can disable the update information when entering the player in config) (since 1.5.6.1) </br>
Now you can use (almost) any placeholder anywhere you want (except <msg>) (since 1.5.6.1) </br>
AutoUpdate - Download the latest version automatically if it possible (since 1.5.6.2) </br>
`/ostag update` - You can update the plugin manually (since 1.5.6.3) </br>
New metrics update system (since 1.5.6.3) </br>
Behind version counter in `/ostag v` (since 1.5.6.3) </br>
Where the execution time is more than 1000ms it has been changed to seconds (since 1.5.6.4) </br>
Added timed remove from list (ostag command) and map (input listener) since (1.5.6.4) </br>
Now `<name>` placeholder show player name , and new `<dis_name>` show player display name since (1.5.6.5) </br
Download Update percentage info (since 1.5.6.5) </br>
Auto download retry when auto download failed (one time) (since 1.5.6.5) </br>

__Fixes__:  <br/>
Code optymalization (since 1.5.6.1-1.5.6.5) </br>
Fixed multi-threading issues with latest version download (since 1.5.6.3-1.5.6.4) </br>
Fixed dumb runnable usage (in version 1.5.6.3), which duplicated the collected data for bstats (since 1.5.6.4) </br>
Better and safety method to get UpDateUtil (since 1.5.6.4) </br>
Better `UpDateUtil` info for sender (since 1.5.6.5) </br>

__Others__: <br/>
Permissions interface (since 1.5.6.1) </br>
Removed Prefixes interface because is un necessary, now we have ReplaceUtil (since 1.5.6.1) <br>

-------------------------------------------------
**Released on the day xx.xx.2023** <br/>

### **1.5.7** <br/>

__Naming changes__: <br/>
From `and-for-all` to `And-for-all` (Since 1.5.7.3)  <br/>
From `movement-server` to `Movement-server` (Since 1.5.7.3)  <br/>

__New things__: <br/>
Info how many kb have been downloaded on the download percentage (Since 1.5.7.1)  <br/>
Remove `ReplaceUtil` and add replace method in `PlayerInfoUtil` (Since 1.5.7.1)  <br/>
Disabling the entire "Ostag" module when "scoretag" and "nametag" are disabled (Since 1.5.7.1)  <br/>
Added more metrics (Since 1.5.7)  <br/>
Added finally working tab command completion (Since 1.5.7.2)  <br/>
Added better config , now scoretag is as a list (Since 1.5.7.3)  <br/>
Added `/ostag menu` to manage some things using the GUI (Since 1.5.7.3)  <br/>
Added new boolean in config `FormsDebug` If it is enabled, information about what a given player has done in forms is
sent (Since 1.5.7.3)  <br/>
New `OsTimer` system (Since 1.5.7.3)  <br/>
Added CpsLimiter (Since 1.5.7.4)  <br/>
**NOTE**

```
I adding he here because I don't think anyone knows that I added it in `GommeAWM/CPSCounter` in commit `/commit/78069051c10d3c6770d523edb48ec1d1a78cdabb`
```

Now `refresh time` is in ticks! (Since 1.5.7.4)  <br/>
New censhorship system , now censor word muste be char! (Since 1.5.7.4)  <br/>
Added sound when player mention system (FormConstructor plugin required) (Since 1.5.7.4)  <br/>
**New plugin version tagging system!
** [Check This](https://github.com/OpenPlugins-Minecraft/OsTag/blob/main/ChangeLog.md#general-info--)  (Since
1.5.7.4)  <br/>
Now Name and Score Tag reset to default when plugin get disabled (Since 1.5.7.5)  <br/>
Added simple `/msg` and `/ignore` for formatter (Since 1.5.7.5)  <br/>
Now if a player is inactive for 5 days after restarting the server he will be removed from `Players.yml` (Since 1.5.7.5)  <br/>

__Fixes__: <br/>
Code and code style fixes <br/>
Better auto redownload (Since 1.5.7.1)  <br/>
Fixed ostag command error when `/ostag add` player argument is null (Since 1.5.7.1)  <br/>
Fixed `/ostag version` commpatibliity command (Since 1.5.7.3)  <br/>
Now when dont have `FormConstructor` plugin `/ostag menu` command can't crash server (Since 1.5.7.5)  <br/>

__Others__: <br/>
New thing (Since x.x.x)  <br/>

--------------------------------------------------

<div align="center">

# __**General info**__  <br/>
</div>

* Versions will now be tagged normally!
* Beta will only be available on our discord server for volunteers




