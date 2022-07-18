package net.deechael.dynamichat.api;

import net.deechael.dynamichat.object.Punishment;
import net.deechael.dynamichat.object.Time;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;

import java.util.Date;
import java.util.List;

public interface User {

    void sendMessage(String message);

    void sendMessage(BaseComponent... components);

    void sendMessage(ComponentBuilder builder);

    void whisper(User another, String message);

    void say(String message);

    void chat(String message);

    Channel getCurrent();

    String getName();

    String getDisplayName();

    List<? extends Channel> getAvailable();

    CommandSender getSender();

    void moveTo(Channel channel);

    void available(Channel channel);

    void unavailable(Channel channel);

    void banIP();

    void unbanIP();

    void unban();

    void unmute();

    void ban();

    void ban(Time time);

    void ban(Date unbanDate);

    void ban(String reason);

    void ban(Time time, String reason);

    void ban(Date unbanDate, String reason);

    void kick();

    void kick(String reason);

    void mute();

    void mute(Time time);

    void mute(Date unmuteDate);

    void mute(String reason);

    void mute(Time time, String reason);

    void mute(Date unmuteDate, String reason);

    List<? extends Punishment> getBanHistory();

    List<? extends Punishment> getMuteHistory();

    List<? extends Punishment> getPunishHistory();

    boolean isBanned();

    boolean isMuted();

    Punishment getBan(String id);

    Punishment getMute(String id);

}
