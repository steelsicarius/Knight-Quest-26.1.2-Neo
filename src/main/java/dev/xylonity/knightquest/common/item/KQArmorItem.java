package dev.xylonity.knightquest.common.item;

import dev.xylonity.knightquest.common.material.KQArmorMaterials;
import dev.xylonity.knightquest.config.values.KQConfigValues;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.item.equipment.ArmorType;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Knight Quest armor item. Port notes (26.1.2):
 * <ul>
 *     <li>{@code ArmorItem} no longer exists; armor items are plain items whose properties are
 *     built with {@code Item.Properties#humanoidArmor(ArmorMaterial, ArmorType)}.</li>
 *     <li>{@code inventoryTick} is now server-side only and takes a {@code ServerLevel}.</li>
 *     <li>Set passives now read the config live (the old enum captured values at class-load
 *     time, before the config file was parsed).</li>
 * </ul>
 */
public class KQArmorItem extends Item {

    /**
     * Dual ConcurrentHashMap that inherits old declared effects when a new set is equipped, preventing
     * incompatibility when either post-indirect or pre-direct status effects are applied.
     */

    public static final Map<UUID, Map<ArmorMaterial, Boolean>> effectAppliedByArmorMap = new ConcurrentHashMap<>();

    private final ArmorMaterial material;
    private final String bonusTooltip;
    private final boolean hasTooltip;

    public KQArmorItem(ArmorMaterial material, ArmorType type, Properties settings, boolean hasTooltip) {
        super(settings);
        this.material = material;
        this.bonusTooltip = KQArmorMaterials.getKeyNameFromMaterial(material);
        this.hasTooltip = hasTooltip;
    }

    public ArmorMaterial getMaterial() {
        return this.material;
    }

    /**
     * Enables or disables tooltips based on the "enable" boolean value in the configuration file
     */

    public enum ArmorSet {
        DEEPSLATE(KQConfigValues.DEEPSLATESET::get),
        EVOKER(KQConfigValues.EVOKERSET::get),
        SQUIRE(KQConfigValues.SQUIRESET::get),
        BLAZE(KQConfigValues.BLAZESET::get),
        DRAGON(KQConfigValues.DRAGONSET::get),
        BAMBOO_GREEN(KQConfigValues.BAMBOOSET_GREEN::get),
        SHINOBI(KQConfigValues.SHINOBI::get),
        BAMBOO(KQConfigValues.BAMBOOSET::get),
        PATH(KQConfigValues.PATHSET::get),
        BOW(KQConfigValues.BOWSET::get),
        BAT(KQConfigValues.BATSET::get),
        SHIELD(KQConfigValues.SHIELDSET::get),
        PHANTOM(KQConfigValues.PHANTOMSET::get),
        HORN(KQConfigValues.HORNSET::get),
        SEA(KQConfigValues.SEASET::get),
        PIRATE(KQConfigValues.PIRATESET::get),
        SPIDER(KQConfigValues.SPIDERSET::get),
        NETHER(KQConfigValues.NETHERSET::get),
        SKULK(KQConfigValues.SKULK::get),
        STRAWHAT(KQConfigValues.STRAWHATSET::get),
        ENDERMAN(KQConfigValues.ENDERMANSET::get),
        VETERAN(KQConfigValues.VETERANSET::get),
        FORZE(KQConfigValues.FORZESET::get),
        CREEPER(KQConfigValues.CREEPERSET::get),
        POLAR(KQConfigValues.POLAR::get),
        SILVER(KQConfigValues.SILVERSET::get),
        HOLLOW(KQConfigValues.HOLLOWSET::get),
        WITHER(KQConfigValues.WITHERSET::get),
        APPLE(KQConfigValues.APPLE_SET::get),
        CONQUISTADOR(KQConfigValues.CONQUISTADORSET::get),
        WITCH(KQConfigValues.WITCH::get),
        TENGU(KQConfigValues.TENGU_HELMET::get),
        HUSK(KQConfigValues.HUSKSET::get),
        BAMBOO_BLUE(KQConfigValues.BAMBOOSET_BLUE::get),
        WARLORD(KQConfigValues.WARLORDSET::get),
        ZOMBIE(KQConfigValues.ZOMBIESET::get),
        SILVERFISH(KQConfigValues.SILVERFISHSET::get),
        SKELETON(KQConfigValues.SKELETONSET::get);

        private final Supplier<Boolean> configValue;

        ArmorSet(Supplier<Boolean> configValue) {
            this.configValue = configValue;
        }

        public boolean isEnabled() {
            return Boolean.TRUE.equals(this.configValue.get());
        }
    }

    private boolean isArmorSetConfigEnabled(String bonusTooltip) {
        try {
            ArmorSet armorSet = ArmorSet.valueOf(bonusTooltip.toUpperCase());
            return armorSet.isEnabled();
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Adds `passive ability` tooltips to each KQArmorItem corresponding to each registered armor set. Uses the
     * same name as the material registered for each individual armor set, as they are essentially identical.
     */

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> tooltipConsumer, @NotNull TooltipFlag tooltipFlag) {
        Consumer<Component> tooltipComponents = tooltipConsumer;

        if (hasTooltip && isArmorSetConfigEnabled(bonusTooltip))
            if (!Objects.equals(bonusTooltip, "chainmail") && !Objects.equals(bonusTooltip, "tengu")) {
                if (KQConfigValues.REQUIRED_ARMOR_PIECES.get() < 4) {
                    tooltipComponents.accept(Component.translatable("tooltip.item.knightquest.set_bonus"));
                } else {
                    tooltipComponents.accept(Component.translatable("tooltip.item.knightquest.full_set_bonus"));
                }

                tooltipComponents.accept(Component.translatable("tooltip.item.knightquest." + bonusTooltip + "_helmet.bonus",
                        Component.literal("-" + (int) Math.floor(KQConfigValues.EVOKER_DARKNESS_CHANCE.get().floatValue() * 100) + "%").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY),
                        Component.literal("-" + (int) Math.floor(KQConfigValues.BLAZE_FIRE_CHANCE.get().floatValue() * 100) + "%").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY),
                        Component.literal("-" + ((int) Math.floor(KQConfigValues.DRAGONSET_DAMAGE_MULTIPLIER.get().floatValue() * 100 - 100)) + "%").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY),
                        Component.literal(String.valueOf(KQConfigValues.SKULK_MAX_LIGHT_LEVEL.get())).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY),
                        Component.literal("-" + (int) Math.floor(KQConfigValues.CHANCE_ENDERMANSET.get().floatValue() * 100) + "%").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY),
                        Component.literal(String.valueOf(KQConfigValues.TELEPORT_RADIUS_ENDERMANSET.get())).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY),
                        Component.literal("-" + (int) Math.floor(KQConfigValues.FORZESET_DEFLECT_CHANCE.get().floatValue() * 100) + "%").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY),
                        Component.literal((100 - KQConfigValues.CREEPER_EXPLOSION_DAMAGE_MULTIPLIER.get() * 100) + "%").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY),
                        Component.literal("-" + (int) Math.floor(KQConfigValues.SILVERSET_BURN_CHANCE.get().floatValue() * 100) + "%").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY),
                        Component.literal((int) Math.floor(KQConfigValues.HOLLOWSET_HEALING_MULTIPLIER.get().floatValue() * 100) + "%").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY),
                        Component.literal("-" + (int) Math.floor(KQConfigValues.WITHERSET_WITHER_CHANCE.get().floatValue() * 100) + "%").withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY),
                        Component.literal(String.valueOf(Math.floor(KQConfigValues.ZOMBIESET_HEALING_AMOUNT.get().floatValue()))).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY),
                        Component.literal(String.valueOf(KQConfigValues.ZOMBIESET_HEALING_TICKS.get() / 20)).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY),
                        Component.literal(String.valueOf(KQConfigValues.SILVERFISH_EFFECT_MAX_HEIGHT.get())).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)
                ));
            } else if (Objects.equals(bonusTooltip, "tengu")) {
                tooltipComponents.accept(Component.translatable("tooltip.item.knightquest.full_helmet_bonus"));
                tooltipComponents.accept(Component.translatable("tooltip.item.knightquest." + bonusTooltip + "_helmet.bonus"));
            }

        super.appendHoverText(stack, context, display, tooltipConsumer, tooltipFlag);
    }

    /**
     * Declaration of static effects for certain armors.
     */

    private static final MobEffectInstance SHIELD_ARMOR = new MobEffectInstance (MobEffects.RESISTANCE, -1, 0, false, false, true);
    private static final MobEffectInstance BAT_ARMOR = new MobEffectInstance (MobEffects.NIGHT_VISION, -1, 0, false, false, true);
    private static final MobEffectInstance PATH_ARMOR = new MobEffectInstance (MobEffects.SPEED, -1, 1, false, true, true);
    private static final MobEffectInstance BOW_ARMOR = new MobEffectInstance (MobEffects.SPEED, -1, 1, false, false, true);
    private static final MobEffectInstance HORN_ARMOR = new MobEffectInstance (MobEffects.STRENGTH, 400, 1, false, false, true);
    private static final MobEffectInstance SEA_ARMOR = new MobEffectInstance (MobEffects.DOLPHINS_GRACE, -1, 0, false, false, true);
    private static final MobEffectInstance PIRATE_ARMOR = new MobEffectInstance (MobEffects.LUCK, -1, 0, false, false, true);
    private static final MobEffectInstance SPIDER_ARMOR = new MobEffectInstance (MobEffects.JUMP_BOOST, -1, 1, false, false, false);
    private static final MobEffectInstance PHANTOM_ARMOR =  new MobEffectInstance (MobEffects.SPEED, -1, 0, false, false, true);
    private static final MobEffectInstance NETHER_ARMOR =  new MobEffectInstance (MobEffects.FIRE_RESISTANCE, -1, 0, false, false, true);
    private static final MobEffectInstance SKULK_ARMOR =  new MobEffectInstance (MobEffects.RESISTANCE, -1, 1, false, false, true);
    private static final MobEffectInstance STRAWHAT_ARMOR =  new MobEffectInstance (MobEffects.WATER_BREATHING, -1, 0, false, false, true);

    /**
     * Ticks the inventory of each player. (Server-side only in 26.x.)
     */

    @Override
    public void inventoryTick(@NotNull ItemStack stack, ServerLevel level, @NotNull Entity entity, EquipmentSlot slotId) {
        if (entity instanceof Player player) {

            UUID playerUUID = player.getUUID();

            if (KQConfigValues.PATHSET.get())
                if (KQFullSetChecker.hasFullSetOn(player, KQArmorMaterials.PATHSET) && level.isBrightOutside()) {
                    if (!Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.PATHSET, false))) {
                        player.addEffect(PATH_ARMOR);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.PATHSET, true);
                    }
                } else {
                    if (Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.PATHSET, false))) {
                        player.removeEffect(MobEffects.SPEED);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.PATHSET, false);
                    }
                }

            if (KQConfigValues.BOWSET.get())
                if (KQFullSetChecker.hasFullSetOn(player, KQArmorMaterials.BOWSET) && player.getMainHandItem().getItem() instanceof ProjectileWeaponItem) {
                    if (!Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.BOWSET, false))) {
                        player.addEffect(BOW_ARMOR);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.BOWSET, true);
                    }
                } else {
                    if (Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.BOWSET, false))) {
                        player.removeEffect(MobEffects.SPEED);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.BOWSET, false);
                    }
                }

            if (KQConfigValues.BATSET.get())
                if (KQFullSetChecker.hasFullSetOn(player, KQArmorMaterials.BATSET) && level.isDarkOutside()) {
                    if (!Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.BATSET, false))) {
                        player.addEffect(BAT_ARMOR);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.BATSET, true);
                    }
                } else {
                    if (Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.BATSET, false))) {
                        player.removeEffect(MobEffects.NIGHT_VISION);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.BATSET, false);
                    }
                }

            if (KQConfigValues.SHIELDSET.get())
                if (KQFullSetChecker.hasFullSetOn(player, KQArmorMaterials.SHIELDSET)) {
                    if (!Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.SHIELDSET, false))) {
                        player.addEffect(SHIELD_ARMOR);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.SHIELDSET, true);
                    }
                } else {
                    if (Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.SHIELDSET, false))) {
                        player.removeEffect(MobEffects.RESISTANCE);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.SHIELDSET, false);
                    }
                }

            if (KQConfigValues.PHANTOMSET.get())
                if (KQFullSetChecker.hasFullSetOn(player, KQArmorMaterials.PHANTOMSET) && level.isDarkOutside()) {
                    if (!Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.PHANTOMSET, false))) {
                        player.addEffect(PHANTOM_ARMOR);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.PHANTOMSET, true);
                    }
                } else {
                    if (Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.PHANTOMSET, false))) {
                        player.removeEffect(MobEffects.SPEED);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.PHANTOMSET, false);
                    }
                }

            if (KQConfigValues.HORNSET.get())
                if (KQFullSetChecker.hasFullSetOn(player, KQArmorMaterials.HORNSET) && player.getLastAttacker() != null) {
                    if (!Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.HORNSET, false))) {
                        player.addEffect(HORN_ARMOR);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.HORNSET, true);
                    }
                } else {
                    if (Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.HORNSET, false))) {
                        player.removeEffect(MobEffects.STRENGTH);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.HORNSET, false);
                    }
                }

            if (KQConfigValues.SEASET.get())
                if (KQFullSetChecker.hasFullSetOn(player, KQArmorMaterials.SEASET) && player.isUnderWater()) {
                    if (!Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.SEASET, false))) {
                        player.addEffect(SEA_ARMOR);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.SEASET, true);
                    }
                } else {
                    if (Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.SEASET, false))) {
                        player.removeEffect(MobEffects.DOLPHINS_GRACE);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.SEASET, false);
                    }
                }

            if (KQConfigValues.PIRATESET.get())
                if (KQFullSetChecker.hasFullSetOn(player, KQArmorMaterials.PIRATESET)) {
                    if (!Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.PIRATESET, false))) {
                        player.addEffect(PIRATE_ARMOR);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.PIRATESET, true);
                    }
                } else {
                    if (Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.PIRATESET, false))) {
                        player.removeEffect(MobEffects.LUCK);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.PIRATESET, false);
                    }
                }

            if (KQConfigValues.SPIDERSET.get())
                if (KQFullSetChecker.hasFullSetOn(player, KQArmorMaterials.SPIDERSET) && player.isShiftKeyDown()) {
                    if (!Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.SPIDERSET, false))) {
                        player.addEffect(SPIDER_ARMOR);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.SPIDERSET, true);
                    }
                } else {
                    if (Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.SPIDERSET, false))) {
                        player.removeEffect(MobEffects.JUMP_BOOST);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.SPIDERSET, false);
                    }
                }

            if (KQConfigValues.NETHERSET.get())
                if (KQFullSetChecker.hasFullSetOn(player, KQArmorMaterials.NETHERSET)) {
                    if (!Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.NETHERSET, false))) {
                        player.addEffect(NETHER_ARMOR);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.NETHERSET, true);
                    }
                } else {
                    if (Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.NETHERSET, false))) {
                        player.removeEffect(MobEffects.FIRE_RESISTANCE);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.NETHERSET, false);
                    }
                }

            if (KQConfigValues.SKULK.get())
                if (KQFullSetChecker.hasFullSetOn(player, KQArmorMaterials.SKULK) && player.level().getMaxLocalRawBrightness(player.blockPosition()) <= KQConfigValues.SKULK_MAX_LIGHT_LEVEL.get()) {
                    if (!Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.SKULK, false))) {
                        player.addEffect(SKULK_ARMOR);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.SKULK, true);
                    }
                } else {
                    if (Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.SKULK, false))) {
                        player.removeEffect(MobEffects.RESISTANCE);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.SKULK, false);
                    }
                }

            if (KQConfigValues.STRAWHATSET.get())
                if (KQFullSetChecker.hasFullSetOn(player, KQArmorMaterials.STRAWHATSET) && player.isUnderWater()) {
                    if (!Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.STRAWHATSET, false))) {
                        player.addEffect(STRAWHAT_ARMOR);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.STRAWHATSET, true);
                    }
                } else {
                    if (Boolean.TRUE.equals(effectAppliedByArmorMap.computeIfAbsent(playerUUID, k -> new ConcurrentHashMap<>()).getOrDefault(KQArmorMaterials.STRAWHATSET, false))) {
                        player.removeEffect(MobEffects.WATER_BREATHING);
                        effectAppliedByArmorMap.get(playerUUID).put(KQArmorMaterials.STRAWHATSET, false);
                    }
                }

        }

        super.inventoryTick(stack, level, entity, slotId);
    }
}
