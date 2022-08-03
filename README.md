# DynamicChat
 A very useful chat plugin for Minecraft Bukkit you must have!

**!ATTENTION!: This plugin is only available for Minecraft 1.19+**

## Core
### DynamicChat Bukkit
Core plugin, should be installed to the server you want to use DynamicChat
### DynamicChat Bungee (WIP) & DynamicChat Velocity (WIP)
Proxy plugin, if your servers are using proxy to connect multiple servers, you should install one of them according to your proxy server and set the "proxy-mode" to "true" in the config.yml file

## Official Extensions
1. Blacklist
2. Report
3. Mute And Ban

## Future Plan
1. Official Extension: Friend
2. Official Extension: Party
3. Official Extension: Reply
4. Proxy plugin

## How to Use

### For server
This plugin now is NOT AVAILABLE for the server using proxy like BungeeCord or Velocity \
They will be supported in the future

1. Download the plugin
2. Download DCG-loader (Dependency): https://www.spigotmc.org/resources/dynamic-class-generator.101584/
3. Download PlaceholderAPI (Dependency): https://www.spigotmc.org/resources/placeholderapi.6245/ (Will be soft-dependency in the future)
4. Move three plugins to your server plugins directory
5. Start the server
6. Install player extension to PlaceholderAPI with "/papi ecloud download player" command
7. Reload PlaceholderAPI with "/papi reload" command

### For developers
#### 1.Add dynamichat-api to your project
Maven
```xml
<dependency>
    <groupId>net.deechael</groupId>
    <artifactId>dynamic-chat-api</artifactId>
    <version>1.00.0</version>
</dependency>
```
Gradle
```groovy
implementation 'net.deechael:dynamic-chat-api:1.00.0'
```

#### 2.Then start coding

```java
package com.example;

import net.deechael.dynamichat.api.BukkitChatManager;
import net.deechael.dynamichat.api.Channel;
import net.deechael.dynamichat.api.Context;
import net.deechael.dynamichat.api.MessageButton;
import net.deechael.dynamichat.api.MuteMessage;
import net.deechael.dynamichat.api.TemporaryChannel;
import net.deechael.dynamichat.api.User;
import net.deechael.dynamichat.event.ChannelSwitchEvent;
import net.deechael.dynamichat.event.CommandSayEvent;
import net.deechael.dynamichat.event.UserChatEvent;
import net.deechael.dynamichat.event.CommandSayEvent;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public class ExamplePlugin extends JavaPlugin implements Listener {
    
    @EventHandler
    public void onChat(UserChatEvent event) {
        // Listening to user chatting
    }

    @EventHandler
    public void onWhisper(WhisperEvent event) {
        // Listening to user whisper to another user
    }

    @EventHandler
    public void onSay(CommandSayEvent event) {
        // Fired when a user invoking "/say" command
    }


    @EventHandler
    public void onSwitch(ChannelSwitchEvent event) {
        // Fired when a player switch channel with "/channel" command
    }

    @Override
    public void onEnable() {
        // Get access to the API with ChatManager
        // Now the API is only available on Bukkit, but there will be Bungee and Velocity in the future
        BukkitChatManager chatManager = BukkitChatManager.getManager();
        List<Channel> channels = chatManager.getChannels(); // Get the channels that are registered in the config file
        Channel chn = chatManager.getChannel("test"); // Get the channel with its name, null if not exists
        Channel global = chatManager.getGlobal(); // Get the global channel
        TemporaryChannel temporaryChannel = chatManager.createTemporaryChannel("display name, nullable", "chat format, nullable"); // Create temporary channel, if you are developing a party plugin, you can use this to make party chat
        chatManager.registerButton(20, new MessageButton() {

            // Register the message button
            // Message buttons will display when a player clicked a message
            // The index cannot repeat, don't use the index in [0, 19], these are for official extensions!
            
            
            // The display text, null means not display to the player
            @Override
            public String display(User clicker, Message message) {
                return "§1§l[Example Button]";
            }

            // Will be invoked when the player clicked the button
            @Override
            public void click(User clicker, Message message) {
                clicker.sendMessage("Hello, world!");
            }

            // The text will display when player move there cursor on the text, null means not display any text
            @Override
            public String hover(User clicker, Message message) {
                return "§bClick to show some thing!";
            }
        });
        
        Map<Integer, ? extends MessageButton> buttons = chatManager.getButtons(); // Get all registered buttons, key: index, value: button
        int maxButtonIndex = chatManager.getButtonMaxIndex(); // If you want to make your message button the last, you can use this command to get the last index then plus 1
        boolean languageFollowClient = chatManager.languageFollowClient(); // You can get the locale of a player, if this is true, you should send the message according to the locale of the player instead of the default language of the server
        String defaultLanguage = chatManager.getDefaultLanguage(); // The default language of the plugin, initial "en_us", can be modified by the server owner
        int recordedMessage = chatManager.recordedMessages(); // Get how many messages have been recorded
        MuteMessage messageObject = chatManager.getMessageByIndex(2); // 2 is the index of the message, the index should equal be bigger than 0 and smaller than the amount of recorded messages
        
        Context context = chatManager.getContext(messageObject, 10);
        // Context is a collection of message sorted by the order of the time when they were sent
        // There are two methods to get the context
        // 1.From an index to the other index
        // 2.Start from a MuteMessage object, with how many messages you want to get
        
        Time time = chatManager.createTime(years, months, weeks, days, hours, minutes, seconds); // Create Time object, Time is used for ban and mute
        Time parsedTime = chatManager.parseTime("1y2mo3wk4d5h6min7s" /* Means 1 year 2 months 3 weeks 4 days 5 hours 6 minutes 7 seconds*/); // Get a time object by parsing the string
        BanIPManager banIPManager = chatManager.getBanIPManager(); // To ban and unban ip, this will only work when DynamicChat-MuteAndBan extension is installed
        
        
    }

}
```

#### 3.PlaceholderAPI placeholders
| Name                                       | Placeholder                        |
|--------------------------------------------|------------------------------------|
| The name of player current channel         | %dynamichat_currentChannel%        |
| The display name of player current channel | %dynamichat_currentChannelDisplay% |
