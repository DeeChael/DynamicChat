package net.deechael.dynamichat.command.argument;

import com.mojang.brigadier.arguments.ArgumentType;

public class EzArgumentTypes {

    public static final ArgumentType<?> CHAT = ArgumentChat.argumentType();

    public static final ArgumentType<?> ATTRIBUTE = ArgumentAttribute.argumentType();

    public static final ArgumentType<?> BLOCK = ArgumentBlock.argumentType();

    public static final ArgumentType<?> LOCATION_BLOCK = ArgumentBlockLocation.argumentType();

    public static final ArgumentType<?> LOCATION = ArgumentLocation.argumentType();

    public static final ArgumentType<?> CHAT_COLOR = ArgumentChatColor.argumentType();

    public static final ArgumentType<?> ENCHANTMENT = ArgumentEnchantment.argumentType();

    public static final ArgumentType<?> ENTITIES_SELECTOR = ArgumentEntities.argumentType();

    public static final ArgumentType<?> ENTITY_SINGLE = ArgumentEntity.argumentType();

    public static final ArgumentType<?> ENTITY_TYPE = ArgumentEntityType.argumentType();

    public static final ArgumentType<?> ITEM_STACK = ArgumentItemStack.argumentType();

    public static final ArgumentType<?> NBT_TAG = ArgumentNBTTag.argumentType();

    public static final ArgumentType<?> OFFLINE_PLAYERS_SELECTOR = ArgumentOfflinePlayer.argumentType();

    public static final ArgumentType<?> PLAYERS_SELECTOR = ArgumentPlayer.argumentType();

    public static final ArgumentType<?> POTION_EFFECT_TYPE = ArgumentPotionEffectType.argumentType();

    public static final ArgumentType<?> UUID = ArgumentUUID.argumentType();

    public static final ArgumentType<?> WORLD = ArgumentWorld.argumentType();

    public static final ArgumentType<?> STRING = BaseArguments.string();

    public static final ArgumentType<?> WORD = BaseArguments.string();

    public static final ArgumentType<?> STRING_GREEDY = BaseArguments.greedyString();

    public static final ArgumentType<?> INTEGER = BaseArguments.integer();

    public static final ArgumentType<?> BOOLEAN = BaseArguments.bool();

    public static final ArgumentType<?> DOUBLE = BaseArguments.doubleArg();

    public static final ArgumentType<?> LONG = BaseArguments.longArg();

    public static final ArgumentType<?> FLOAT = BaseArguments.floatArg();

    public static ArgumentType<?> integer(int min) {
        return BaseArguments.integer(min, 2147483647);
    }

    public static ArgumentType<?> integer(int min, int max) {
        return BaseArguments.integer(min, max);
    }

    public static ArgumentType<?> doubleArg(double min) {
        return BaseArguments.doubleArg(min, 1.7976931348623157E308D);
    }

    public static ArgumentType<?> doubleArg(double min, double max) {
        return BaseArguments.doubleArg(min, max);
    }

    public static ArgumentType<?> longArg(long min) {
        return BaseArguments.longArg(min, 9223372036854775807L);
    }

    public static ArgumentType<?> longArg(long min, long max) {
        return BaseArguments.longArg(min, max);
    }

    public static ArgumentType<?> floatArg(float min) {
        return BaseArguments.floatArg(min, 3.4028235E38F);
    }

    public static ArgumentType<?> floatArg(float min, float max) {
        return BaseArguments.floatArg(min, max);
    }

}
