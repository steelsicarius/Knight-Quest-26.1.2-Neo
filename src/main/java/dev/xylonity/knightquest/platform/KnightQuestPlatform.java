package dev.xylonity.knightquest.platform;

import dev.xylonity.knightquest.registry.KnightQuestItems;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

/**
 * Platform abstraction (originally multi-loader; now NeoForge-only, kept for parity with the
 * upstream project layout). Item factories receive the {@link Item.Properties} with the
 * registry id already applied (26.1.2 requires {@code Properties#setId} before construction).
 */
public interface KnightQuestPlatform {

    <T extends Item> Supplier<T> registerItem(String id, Function<Item.Properties, T> item);
    <T extends SoundEvent> Supplier<T> registerSound(String id, Supplier<T> sound);
    <T extends ParticleType<?>> Supplier<T> registerParticle(String id, boolean overrideLimiter);
    <T extends Item> Supplier<T> registerSpecificItem(String id, UnaryOperator<Item.Properties> properties, KnightQuestItems.KQItemType type);
    <T extends Item> Supplier<T> registerArmorItem(String id, ArmorMaterial armorMaterial, ArmorType armorType, boolean containsTooltip, UnaryOperator<Item.Properties> properties, int durabilityAmount);
    <T extends Item> Supplier<T> registerGeoArmorItem(String id, ArmorMaterial armorMaterial, ArmorType armorType, boolean containsTooltip, boolean containsExtraTooltip, UnaryOperator<Item.Properties> properties, int durabilityAmount);
    <T extends Item> Supplier<T> registerSwordItem(String id, ToolMaterial itemMaterial, UnaryOperator<Item.Properties> properties, float attackBaseline, float speedMalus, boolean containsTooltip);
    <T extends Item> Supplier<T> registerAxeItem(String id, ToolMaterial itemMaterial, UnaryOperator<Item.Properties> properties, float extraDamageBoost, float attackSpeedMalus);
    <T extends Entity> Supplier<EntityType<T>> registerEntity(String id, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height);
    <T extends Mob> Supplier<Item> registerSpawnEggItem(String id, Supplier<EntityType<T>> entity);
    <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(String id, Supplier<T> tab);
    <T extends Item> Supplier<T> registerMusicDisc(String id, int signal, Supplier<SoundEvent> soundEvent, UnaryOperator<Item.Properties> properties, int length);

    Supplier<Item> getGreatEssence();
    Supplier<Item> getSmallEssence();
    Supplier<ParticleOptions> getStartsetParticle();
    Supplier<Item> getPaladinSword();

    CreativeModeTab.Builder creativeTabBuilder();

}
