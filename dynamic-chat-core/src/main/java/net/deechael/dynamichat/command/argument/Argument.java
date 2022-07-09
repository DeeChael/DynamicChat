package net.deechael.dynamichat.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;

public interface Argument {

    /**
     * Get argument type in nms
     *
     * @return argument type
     */
    ArgumentType<Object> argument();

    /**
     * Get argument
     *
     * @param commandContext command sender
     * @param name           argument name
     * @return argument
     */
    Object get(CommandContext<Object> commandContext, String name);

}
