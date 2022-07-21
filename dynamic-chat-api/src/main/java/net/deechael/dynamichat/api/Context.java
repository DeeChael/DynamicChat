package net.deechael.dynamichat.api;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * A set of messages that have order
 */
public interface Context extends Iterable<MuteMessage> {

    /**
     * Get how many messages in the context
     *
     * @return amount
     */
    int length();

    /**
     * Iterate the context
     *
     * @return iterator
     */
    @NotNull
    @Override
    ContextIterator iterator();

    /**
     * Customized iterator
     */
    interface ContextIterator extends Iterator<MuteMessage> {

        /**
         * To check whether iterator has previous object
         *
         * @return status
         */
        boolean hasPrevious();

        /**
         * In contrast to Iterator#next()
         *
         * @return previous object
         */
        MuteMessage previous();

        /**
         * Get the index in recorded messages of current message object
         * @return the index in recorded messages
         */
        int getCurrentMessageIndex();

        /**
         * Get the id in recorded messages of current message object
         * @return the id in recorded messages
         */
        String getCurrentMessageId();

    }

}
