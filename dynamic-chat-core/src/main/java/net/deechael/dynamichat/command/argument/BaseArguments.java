package net.deechael.dynamichat.command.argument;

import com.mojang.brigadier.arguments.*;

public final class BaseArguments {

    public static ArgumentType<String> string() {
        return StringArgumentType.string();
    }

    public static ArgumentType<String> stringWord() {
        return StringArgumentType.word();
    }

    public static ArgumentType<String> greedyString() {
        return StringArgumentType.greedyString();
    }

    public static ArgumentType<Integer> integer() {
        return integer(-2147483648);
    }

    public static ArgumentType<Integer> integer(int min) {
        return integer(min, 2147483647);
    }

    public static ArgumentType<Integer> integer(int min, int max) {
        return IntegerArgumentType.integer(min, max);
    }

    public static ArgumentType<Boolean> bool() {
        return BoolArgumentType.bool();
    }

    public static ArgumentType<Double> doubleArg() {
        return doubleArg(-1.7976931348623157E308D);
    }

    public static ArgumentType<Double> doubleArg(double min) {
        return doubleArg(min, 1.7976931348623157E308D);
    }

    public static ArgumentType<Double> doubleArg(double min, double max) {
        return DoubleArgumentType.doubleArg(min, max);
    }

    public static ArgumentType<Long> longArg() {
        return longArg(-9223372036854775808L);
    }

    public static ArgumentType<Long> longArg(long min) {
        return longArg(min, 9223372036854775807L);
    }

    public static ArgumentType<Long> longArg(long min, long max) {
        return LongArgumentType.longArg(min, max);
    }

    public static ArgumentType<Float> floatArg() {
        return floatArg(-3.4028235E38F);
    }

    public static ArgumentType<Float> floatArg(float min) {
        return floatArg(min, 3.4028235E38F);
    }

    public static ArgumentType<Float> floatArg(float min, float max) {
        return FloatArgumentType.floatArg(min, max);
    }

}
