package net.deechael.dynamichat.entity;

import net.deechael.dynamichat.api.Context;
import net.deechael.dynamichat.api.MuteMessage;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class ContextEntity implements Context {

    private final List<MuteMessage> messages;

    public ContextEntity(List<MuteMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int length() {
        return messages.size();
    }

    @NotNull
    @Override
    public ContextIteratorImpl iterator() {
        return new ContextIteratorImpl(messages);
    }

    public static class ContextIteratorImpl implements ContextIterator {

        private final List<MuteMessage> iterator;

        private int index = 0;

        public ContextIteratorImpl(List<MuteMessage> iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasPrevious() {
            return this.index > 0;
        }

        @Override
        public MuteMessage previous() {
            if (!hasNext())
                throw new IndexOutOfBoundsException("Index " + index + " out of size");
            index--;
            return this.iterator.get(index);
        }

        @Override
        public int getCurrentMessageIndex() {
            return DynamicChatManager.getMessageIndex(this.iterator.get(index).getId());
        }

        @Override
        public String getCurrentMessageId() {
            return this.iterator.get(index).getId();
        }

        @Override
        public boolean hasNext() {
            return index < iterator.size();
        }

        @Override
        public MuteMessage next() {
            if (!hasNext())
                throw new IndexOutOfBoundsException("Index " + index + " out of size");
            MuteMessage message = this.iterator.get(index);
            index++;
            return message;
        }

    }

}
