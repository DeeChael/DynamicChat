package net.deechael.dynamichat.temp;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import net.deechael.dynamichat.DyChatPlugin;
import org.bukkit.entity.Player;

// TODO

/**
 * Actually if I want I can totally finish this, but I don't think it is worth
 *
 * If you can implement this, just fork the project and implement then send a pull request
 *
 * If you want to implement the feature of deleting the message that has been sent
 * This is what you should do:
 * 1. Implement RecallSolver#onPacketSending(PacketEvent)
 * 2. Store every message and mark the receiver
 * 3. Send 20 lines empty message then resend 50 latest messages, and you should come up with an idea about how to prevent record these 70 messages
 *
 * Store this records in a sqlite file
 * Create a table name player_code (`uuid` TEXT, `code` TEXT)
 * uuid is the player's UUID
 * code is a random string with the length of 16 which is made from 26 lowercase letters and 26 uppercase letters
 * The code cannot be repeated
 *
 * Creating table for each player
 * the name of the table is the code above
 * if player's uuid not in table "player_code", first generate a code for the player then create a table for the player
 * The content in player's table: (`index` BIGINT, `content` TEXT)
 * index should be set as auto increasing
 * content should be the result the player saw, including the format of chatting
 *
 * I won't implement this by myself because I don't like this feature and there may be a bug that when you are resending the 50 messages,
 * if there are others plugins sending messages, it will cause a problem that there are some messages that
 * are not here appearing among the resent messages
 */
public class RecallSolver extends PacketAdapter {

    public RecallSolver() {
        super(DyChatPlugin.getInstance(), ListenerPriority.LOWEST, PacketType.Play.Server.CHAT);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        Player player = event.getPlayer();
        PacketContainer packetContainer = event.getPacket();
    }

}
