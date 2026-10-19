package dev.xylonity.knightquest.common.item;

import dev.xylonity.knightquest.config.values.KQConfigValues;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Base class for the six Knight Quest weapons.
 * <p>
 * Port notes (26.1.2):
 * <ul>
 *     <li>{@code SwordItem} no longer exists; weapons are plain items built with
 *     {@code Properties#sword(ToolMaterial, attackBaseline, speed)}.</li>
 *     <li>KnightLib's Great Chalice activation is not available in this port, so weapons
 *     are considered always activated (see README).</li>
 *     <li>{@code use} now returns the unified {@link InteractionResult}.</li>
 * </ul>
 */
public abstract class KQWeaponItem extends Item {

    public KQWeaponItem(ToolMaterial material, float attackBaseline, float speed, Properties properties) {
        super(properties.sword(material, attackBaseline, speed));
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        CustomData customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
        CompoundTag dataTag = customData.copyTag();

        long currentTime = level.getGameTime();
        long lastUsed = dataTag.getLongOr("LastUsed", 0L);
        int cooldownTicks = getCooldownTicks();

        if (currentTime - lastUsed >= cooldownTicks) {
            interaction(level, player, hand);

            if (!level.isClientSide()) {
                dataTag.putLong("LastUsed", currentTime);
                stack.set(DataComponents.CUSTOM_DATA, CustomData.of(dataTag));
                player.getCooldowns().addCooldown(stack, cooldownTicks);
            }

            return level.isClientSide() ? InteractionResult.SUCCESS : InteractionResult.CONSUME;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @NotNull Item.TooltipContext pContext, @NotNull TooltipDisplay pTooltipDisplay, @NotNull Consumer<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag) {

        if (isEnabled()) {
            pTooltipComponents.accept(Component.translatable("tooltip.weapon.knightquest." + getName()));

            pTooltipComponents.accept(Component.translatable("tooltip.weapon.knightquest." + getName() + ".active",
                    KQConfigValues.SPEED_TICKS_KUKRI.get() / 20F,
                    KQConfigValues.INV_TICKS_PALADIN.get() / 20F,
                    (int) Math.floor(KQConfigValues.EXTRA_DAMAGE_UCHIGATANA.get() * 100),
                    KQConfigValues.REFLECTION_TIME_KHOPESH.get() / 20F,
                    KQConfigValues.TICKS_CLEAVER.get() / 20));
            pTooltipComponents.accept(Component.translatable("tooltip.weapon.knightquest." + getName() + ".passive",
                    (int) Math.floor(KQConfigValues.EXTRA_DAMAGE_PASSIVE_UCHIGATANA.get() * 100),
                    (int) Math.floor(KQConfigValues.ENEMY_HEALTH_PASSIVE_UCHIGATANA.get() * 100),
                    (int) Math.floor(KQConfigValues.CHANCE_BURN_KHOPESH.get() * 100),
                    KQConfigValues.REGEN_HP_PALADIN.get(),
                    KQConfigValues.REGEN_TICKS_PALADIN.get() / 20,
                    (int) Math.floor(KQConfigValues.REGEN_MAX_PALADIN.get().floatValue() * 100),
                    (int) Math.floor(KQConfigValues.EXTRA_DAMAGE_PASSIVE_CLEAVER.get() * 100),
                    (int) Math.floor(KQConfigValues.ENEMY_HEALTH_PASSIVE_CLEAVER.get() * 100)));
        }

        super.appendHoverText(pStack, pContext, pTooltipDisplay, pTooltipComponents, pTooltipFlag);
    }

    abstract public void interaction(Level level, Player player, InteractionHand hand);

    abstract public int getCooldownTicks();

    abstract public String getName();

    protected abstract boolean isEnabled();
}
