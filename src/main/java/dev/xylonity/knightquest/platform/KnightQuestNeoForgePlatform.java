package dev.xylonity.knightquest.platform;

import dev.xylonity.knightquest.KnightQuest;
import dev.xylonity.knightquest.client.armor.GeoItemArmor;
import dev.xylonity.knightquest.common.item.ChaoticEssenceItem;
import dev.xylonity.knightquest.common.item.KQArmorItem;
import dev.xylonity.knightquest.common.item.KnightQuestItem;
import dev.xylonity.knightquest.common.item.RadiantEssenceItem;
import dev.xylonity.knightquest.registry.KnightQuestItems;
import dev.xylonity.knightquest.registry.KnightQuestParticles;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.ToolMaterial;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class KnightQuestNeoForgePlatform implements KnightQuestPlatform {

    private static final String TOOLTIP_ITEM_PATH = "tooltip.item.knightquest.";

    @Override
    public Supplier<Item> getGreatEssence() {
        return KnightQuestItems.GREAT_ESSENCE;
    }

    @Override
    public Supplier<Item> getSmallEssence() {
        return KnightQuestItems.SMALL_ESSENCE;
    }

    @Override
    public Supplier<ParticleOptions> getStartsetParticle() {
        return KnightQuestParticles.STARSET::get;
    }

    @Override
    public Supplier<Item> getPaladinSword() {
        return KnightQuestItems.PALADIN_SWORD;
    }

    @Override
    public CreativeModeTab.Builder creativeTabBuilder() {
        return CreativeModeTab.builder();
    }

    @Override
    public <T extends Item> Supplier<T> registerItem(String id, Supplier<T> item) {
        return KnightQuest.ITEMS.register(id, item);
    }

    @Override
    public <T extends Mob> Supplier<Item> registerSpawnEggItem(String id, Supplier<EntityType<T>> entity) {
        return registerItem(id, () -> new SpawnEggItem(new Item.Properties().spawnEgg(entity.get())));
    }

    @Override
    public <T extends SoundEvent> Supplier<T> registerSound(String id, Supplier<T> sound) {
        return KnightQuest.SOUNDS.register(id, sound);
    }

    @Override
    public <T extends ParticleType<?>> Supplier<T> registerParticle(String id, boolean overrideLimiter) {
        return KnightQuest.PARTICLES.register(id, () -> (T) new SimpleParticleType(overrideLimiter));
    }

    @Override
    public <T extends Item> Supplier<T> registerSpecificItem(String id, Item.Properties properties, KnightQuestItems.KQItemType type) {
        if (type == KnightQuestItems.KQItemType.CHAOTIC_ESSENCE) {
            return (Supplier<T>) registerItem(id, () -> new ChaoticEssenceItem(properties, id));
        } else if (type == KnightQuestItems.KQItemType.RADIANT_ESSENCE) {
            return (Supplier<T>) registerItem(id, () -> new RadiantEssenceItem(properties, id));
        }

        return (Supplier<T>) registerItem(id, () -> new KnightQuestItem(properties, id));
    }

    @Override
    public <T extends Item> Supplier<T> registerGeoArmorItem(String id, ArmorMaterial armorMaterial, ArmorType armorType, boolean containsTooltip, boolean containsExtraTooltip, Item.Properties properties, int durabilityAmount) {
        if (containsExtraTooltip)
            return (Supplier<T>) KnightQuest.ITEMS.register(id, () -> new GeoItemArmor(armorMaterial, armorType, properties.humanoidArmor(armorMaterial, armorType), id, containsTooltip) {
                @Override
                public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull TooltipDisplay display, @NotNull java.util.function.Consumer<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
                    tooltipComponents.accept(Component.translatable(TOOLTIP_ITEM_PATH + id));
                    super.appendHoverText(stack, context, display, tooltipComponents, tooltipFlag);
                }
            });
        else
            return (Supplier<T>) KnightQuest.ITEMS.register(id, () -> new GeoItemArmor(armorMaterial, armorType, properties.humanoidArmor(armorMaterial, armorType), id, containsTooltip));
    }

    @Override
    public <T extends Item> Supplier<T> registerArmorItem(String id, ArmorMaterial armorMaterial, ArmorType armorType, boolean containsTooltip, Item.Properties properties, int durabilityAmount) {
        return (Supplier<T>) KnightQuest.ITEMS.register(id, () -> new KQArmorItem(armorMaterial, armorType, properties.humanoidArmor(armorMaterial, armorType), containsTooltip));
    }

    @Override
    public <T extends Item> Supplier<T> registerSwordItem(String id, ToolMaterial itemMaterial, Item.Properties properties, float attackBaseline, float speedMalus, boolean containsTooltip) {
        if (containsTooltip)
            return (Supplier<T>) KnightQuest.ITEMS.register(id, () -> new Item(properties.sword(itemMaterial, attackBaseline, speedMalus)) {
                @Override
                public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull TooltipDisplay display, @NotNull java.util.function.Consumer<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
                    tooltipComponents.accept(Component.translatable(TOOLTIP_ITEM_PATH + id));
                    super.appendHoverText(stack, context, display, tooltipComponents, tooltipFlag);
                }
            });
        else
            return (Supplier<T>) KnightQuest.ITEMS.register(id, () -> new Item(properties.sword(itemMaterial, attackBaseline, speedMalus)));
    }

    @Override
    public <T extends Item> Supplier<T> registerAxeItem(String id, ToolMaterial itemMaterial, Item.Properties properties, float damageBoost, float speedMalus) {
        return (Supplier<T>) KnightQuest.ITEMS.register(id, () -> new AxeItem(itemMaterial, damageBoost, speedMalus, properties));
    }

    @Override
    public <T extends Entity> Supplier<EntityType<T>> registerEntity(String id, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height) {
        return KnightQuest.ENTITY.register(id, regId -> EntityType.Builder.of(factory, category).sized(width, height).build(ResourceKey.create(Registries.ENTITY_TYPE, regId)));
    }

    @Override
    public <T extends CreativeModeTab> Supplier<T> registerCreativeModeTab(String id, Supplier<T> tab) {
        return KnightQuest.CREATIVE_TABS.register(id, tab);
    }

    @Override
    public <T extends Item> Supplier<T> registerMusicDisc(String id, int signal, Supplier<SoundEvent> soundEvent, Item.Properties properties, int length) {
        return (Supplier<T>) registerItem(id, () -> new Item(properties.jukeboxPlayable(ResourceKey.create(Registries.JUKEBOX_SONG, Identifier.fromNamespaceAndPath(KnightQuest.MOD_ID, "the_architect_of_chaos")))));
    }

}
