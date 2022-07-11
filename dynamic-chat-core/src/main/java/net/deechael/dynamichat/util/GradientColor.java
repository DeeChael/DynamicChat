package net.deechael.dynamichat.util;

import net.md_5.bungee.api.ChatColor;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class GradientColor {

    private Color from;

    private Color to;

    public GradientColor(Color from, Color to) {
        this.from = from;
        this.to = to;
    }

    public static void foreach(Color from, Color to, int amount, BiConsumer<Integer, ChatColor> method) {
        new GradientColor(from, to).foreach(amount, method);
    }

    public static Color[] gradient(Color c1, Color c2, int amount) {
        List<Color> colors = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            float ratio = (float) i / (float) amount;
            int red = (int) (c2.getRed() * ratio + c1.getRed() * (1 - ratio));
            int green = (int) (c2.getGreen() * ratio +
                    c1.getGreen() * (1 - ratio));
            int blue = (int) (c2.getBlue() * ratio +
                    c1.getBlue() * (1 - ratio));
            colors.add(new Color(red, green, blue));
        }
        return colors.toArray(new Color[0]);
    }

    public static GradientColor create(Color from, Color to) {
        return new GradientColor(from, to);
    }

    public Color getFrom() {
        return from;
    }

    public void setFrom(Color from) {
        this.from = from;
    }

    public Color getTo() {
        return to;
    }

    public void setTo(Color to) {
        this.to = to;
    }

    public String applyColor(String text) {
        final char[] charArray = text.toCharArray();
        StringBuilder finalString = new StringBuilder();
        Color[] colors = gradient(this.from, this.to, text.length());
        for (int i = 0; i < colors.length; i++) {
            finalString.append(ChatColor.of(colors[i]).toString()).append(charArray[i]);
        }
        return finalString.toString();
    }

    public void foreach(int amount, BiConsumer<Integer, ChatColor> method) {
        Color[] colors = gradient(from, to, amount);
        for (int i = 0; i < colors.length; i++) {
            method.accept(i, ChatColor.of(colors[i]));
        }
    }

}
