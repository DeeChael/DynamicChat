package net.deechael.dynamichat.api;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public interface Context extends Iterable<MuteMessage> {

    int length();

    @NotNull
    @Override
    ContextIterator iterator();

    interface ContextIterator extends Iterator<MuteMessage> {

        boolean hasPrevious();

        MuteMessage previous();

        int getCurrentMessageIndex();

        String getCurrentMessageId();

    }

}
