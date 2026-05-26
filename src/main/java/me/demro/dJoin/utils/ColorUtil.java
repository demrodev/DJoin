package me.demro.dJoin.utils;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.Map;

public final class ColorUtil {

    private static final Map<Character, TextColor> LEGACY_COLOR_MAP = new HashMap<>();

    static {
        LEGACY_COLOR_MAP.put('0', TextColor.color(0x000000));
        LEGACY_COLOR_MAP.put('1', TextColor.color(0x0000AA));
        LEGACY_COLOR_MAP.put('2', TextColor.color(0x00AA00));
        LEGACY_COLOR_MAP.put('3', TextColor.color(0x00AAAA));
        LEGACY_COLOR_MAP.put('4', TextColor.color(0xAA0000));
        LEGACY_COLOR_MAP.put('5', TextColor.color(0xAA00AA));
        LEGACY_COLOR_MAP.put('6', TextColor.color(0xFFAA00));
        LEGACY_COLOR_MAP.put('7', TextColor.color(0xAAAAAA));
        LEGACY_COLOR_MAP.put('8', TextColor.color(0x555555));
        LEGACY_COLOR_MAP.put('9', TextColor.color(0x5555FF));
        LEGACY_COLOR_MAP.put('a', TextColor.color(0x55FF55));
        LEGACY_COLOR_MAP.put('b', TextColor.color(0x55FFFF));
        LEGACY_COLOR_MAP.put('c', TextColor.color(0xFF5555));
        LEGACY_COLOR_MAP.put('d', TextColor.color(0xFF55FF));
        LEGACY_COLOR_MAP.put('e', TextColor.color(0xFFFF55));
        LEGACY_COLOR_MAP.put('f', TextColor.color(0xFFFFFF));
    }

    private ColorUtil() {}

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
                if (textBuffer.length() > 0) {
                    builder.append(Component.text(textBuffer.toString()).style(currentStyle));
                    textBuffer.setLength(0);
                }

                char code = input.charAt(i + 1);
                if (code == '#') {
                    if (i + 7 <= len && input.substring(i + 2, i + 8).matches("[0-9a-fA-F]{6}")) {
                        String hex = input.substring(i + 2, i + 8);
                        try {
                            TextColor color = TextColor.fromHexString("#" + hex);
                            currentStyle = currentStyle.color(color);
                        } catch (IllegalArgumentException ignored) {}
                        i += 8;
                        continue;
                    } else {
                        textBuffer.append("&#");
                        i += 2;
                        continue;
                    }
                } else {
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
                            TextColor color = LEGACY_COLOR_MAP.get(code);
                            if (color != null) {
                                currentStyle = currentStyle.color(color);
                            }
                        }
                        i += 2;
                        continue;
                    } else {
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

    // Part of shit-code
    public static String parseToString(String input) {
        return LegacyComponentSerializer.legacySection().serialize(parse(input));
    }
}