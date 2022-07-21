package net.deechael.dynamichat.api;

import java.util.UUID;

/**
 * Player user
 */
public interface PlayerUser extends User {

    /**
     * The UUID of the player
     * @return uuid
     */
    UUID getUniqueId();

}
