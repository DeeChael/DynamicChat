package net.deechael.dynamichat.api;

import net.deechael.dynamichat.annotation.Included;
import net.deechael.dynamichat.annotation.NotIncluded;
import net.deechael.dynamichat.object.Time;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public interface ChatManager {

    Channel getChannel(String name);

    List<? extends Channel> getChannels();

    List<? extends TemporaryChannel> getTemporaryChannels();

    Channel getGlobal();

    TemporaryChannel createTemporaryChannel(@Nullable String displayName, @Nullable String format);

    void registerButton(int index, MessageButton button);

    Map<Integer, ? extends MessageButton> getButtons();

    int getButtonMaxIndex();

    boolean languageFollowClient();

    String getDefaultLanguage();

    int getIndex(MuteMessage message);

    MuteMessage getMessageByIndex(int index);

    MuteMessage getMessageById(String id);

    Context getContext(@Included int startIndex, @NotIncluded int endIndex);

    Context getContext(MuteMessage startMessage, int amount);

    int recordedMessages();

    Time createTime(int years, int months, int weeks, int days, int hours, int minutes, int seconds);

    Time createTime(long seconds);

    Time parseTime(String timeString);

    BanIPManager getBanIPManager();

}
