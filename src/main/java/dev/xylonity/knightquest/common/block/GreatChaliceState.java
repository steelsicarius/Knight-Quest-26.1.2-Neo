package dev.xylonity.knightquest.common.block;

import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

/**
 * Defines multiple states the chalice can permute between.
 * Port of KnightLib's {@code GreatChaliceState}.
 */
public enum GreatChaliceState implements StringRepresentable {
    EMPTY, NORMAL, CHAOTIC;

    @Override
    public @NotNull String getSerializedName() {
        return name().toLowerCase();
    }

    public static GreatChaliceState byName(String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (Exception e) {
            return NORMAL;
        }
    }
}
