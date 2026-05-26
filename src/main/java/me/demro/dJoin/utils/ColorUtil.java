package me.demro.dJoin.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public final class ColorUtil {

    private static final Map<Character, TextColor> LEGACY_COLOR_MAP = new HashMap<>();

    static {
        // Legacy colors MineCraft
        LEGACY_COLOR_MAP.put('0', TextColor.color(0x000000)); // black
        LEGACY_COLOR_MAP.put('1', TextColor.color(0x0000AA)); // dark_blue
        LEGACY_COLOR_MAP.put('2', TextColor.color(0x00AA00)); // dark_green
        LEGACY_COLOR_MAP.put('3', TextColor.color(0x00AAAA)); // dark_aqua
        LEGACY_COLOR_MAP.put('4', TextColor.color(0xAA0000)); // dark_red
        LEGACY_COLOR_MAP.put('5', TextColor.color(0xAA00AA)); // dark_purple
        LEGACY_COLOR_MAP.put('6', TextColor.color(0xFFAA00)); // gold
        LEGACY_COLOR_MAP.put('7', TextColor.color(0xAAAAAA)); // gray
        LEGACY_COLOR_MAP.put('8', TextColor.color(0x555555)); // dark_gray
        LEGACY_COLOR_MAP.put('9', TextColor.color(0x5555FF)); // blue
        LEGACY_COLOR_MAP.put('a', TextColor.color(0x55FF55)); // green
        LEGACY_COLOR_MAP.put('b', TextColor.color(0x55FFFF)); // aqua
        LEGACY_COLOR_MAP.put('c', TextColor.color(0xFF5555)); // red
        LEGACY_COLOR_MAP.put('d', TextColor.color(0xFF55FF)); // light_purple
        LEGACY_COLOR_MAP.put('e', TextColor.color(0xFFFF55)); // yellow
        LEGACY_COLOR_MAP.put('f', TextColor.color(0xFFFFFF)); // white
    }

    private ColorUtil() {}

    /**
     * Converts a string with the color codes & (and HEX &#RRGGBB) to Component.
     * Standard codes are supported: &0-9, &a-f, as well as &l, &m, &n, &O, &r.
     */
    public static Component parse(String input) {
        if (input == null || input.isEmpty()) return Component.empty();

        TextComponent.Builder builder = Component.text();
        Style currentStyle = Style.empty();
        StringBuilder textBuffer = new StringBuilder();
        int len = input.length();
        int i = 0;

        while (i < len) {
            char c = input.charAt(i);
            if (c == '&' && i + 1 < len) {
                // The formatting code is found, we reset the accumulated text
                if (textBuffer.length() > 0) {
                    builder.append(Component.text(textBuffer.toString()).style(currentStyle));
                    textBuffer.setLength(0);
                }

                char code = input.charAt(i + 1);
                if (code == '#') {
                    // HEX-color: &#RRGGBB
                    if (i + 7 <= len && input.substring(i + 2, i + 8).matches("[0-9a-fA-F]{6}")) {
                        String hex = input.substring(i + 2, i + 8);
                        try {
                            TextColor color = TextColor.fromHexString("#" + hex);
                            currentStyle = currentStyle.color(color);
                        } catch (IllegalArgumentException ignored) {
                            // invalid HEX-color
                            textBuffer.append("&#").append(hex);
                        }
                        i += 8;
                        continue;
                    } else {
                        // incorrect HEX-color
                        textBuffer.append("&#");
                        i += 2;
                        continue;
                    }
                } else {
                    // Legacy code processing
                    ChatColor legacy = ChatColor.getByChar(code);
                    if (legacy != null) {
                        if (legacy == ChatColor.BOLD) {
                            currentStyle = currentStyle.decoration(TextDecoration.BOLD, true);
                        } else if (legacy == ChatColor.ITALIC) {
                            currentStyle = currentStyle.decoration(TextDecoration.ITALIC, true);
                        } else if (legacy == ChatColor.UNDERLINE) {
                            currentStyle = currentStyle.decoration(TextDecoration.UNDERLINED, true);
                        } else if (legacy == ChatColor.STRIKETHROUGH) {
                            currentStyle = currentStyle.decoration(TextDecoration.STRIKETHROUGH, true);
                        } else if (legacy == ChatColor.MAGIC) {
                            currentStyle = currentStyle.decoration(TextDecoration.OBFUSCATED, true);
                        } else if (legacy == ChatColor.RESET) {
                            currentStyle = Style.empty();
                        } else {
                            // Color
                            TextColor color = LEGACY_COLOR_MAP.get(code);
                            if (color != null) {
                                currentStyle = currentStyle.color(color);
                            }
                        }
                        i += 2;
                        continue;
                    } else {
                        // Unknown color code
                        textBuffer.append('&').append(code);
                        i += 2;
                        continue;
                    }
                }
            } else {
                textBuffer.append(c);
                i++;
            }
        }

        if (textBuffer.length() > 0) {
            builder.append(Component.text(textBuffer.toString()).style(currentStyle));
        }

        return builder.build();
    }
}