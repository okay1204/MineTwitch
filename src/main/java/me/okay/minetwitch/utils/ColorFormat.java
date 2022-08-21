package me.okay.minetwitch.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;

public class ColorFormat {
    private static Pattern ampersandHexPattern = Pattern.compile("&#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})");

    // returns the string colored by &
    public static String colorize(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static String formatHexCodes(String string) {
        Matcher matcher = ampersandHexPattern.matcher(string);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            String hex = matcher.group(1);
            matcher.appendReplacement(sb, ChatColor.of("#" + hex).toString());
        }

        matcher.appendTail(sb);
        return sb.toString();
    }
}
