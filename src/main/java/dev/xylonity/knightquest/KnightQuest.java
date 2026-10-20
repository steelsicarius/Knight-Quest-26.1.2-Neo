package dev.xylonity.knightquest;

import com.mojang.logging.LogUtils;
import dev.xylonity.knightquest.KnightQuestCommon;
import dev.xylonity.knightquest.config.KnightQuestCommonConfigs;
import dev.xylonity.knightquest.datagen.KQLootModifiers;
import dev.xylonity.knightquest.registry.KnightQuestCreativeModeTabs;
import dev.xylonity.knightquest.registry.KnightQuestItems;
import dev.xylonity.knightquest.registry.KnightQuestEntities;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;

/**
 * Knight Quest - port to NeoForge 26.1.2 (Minecraft 26.1.2).
 * Ported from Knight-Quest-1.21-multi (NeoForge 21.1.x).
 */
@Mod(KnightQuest.MOD_ID)
public class KnightQuest {

    public static final String MOD_ID = KnightQuestCommon.MOD_ID;
    public static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(KnightQuest.MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(KnightQuest.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, KnightQuest.MOD_ID);
    public static final DeferredRegister<ParticleType<?>> PARTICLES = DeferredRegister.create(Registries.PARTICLE_TYPE, KnightQuest.MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, KnightQuest.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITY = DeferredRegister.create(Registries.ENTITY_TYPE, KnightQuest.MOD_ID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, KnightQuest.MOD_ID);

    public KnightQuest(IEventBus modEventBus, ModContainer modContainer) {

        KQLootModifiers.LOOT_MODIFIER_SERIALIZERS.register(modEventBus);

        SOUNDS.register(modEventBus);
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        CREATIVE_TABS.register(modEventBus);
        PARTICLES.register(modEventBus);
        ENTITY.register(modEventBus);

        modEventBus.<EntityAttributeCreationEvent>addListener(event -> KnightQuestEntities.registerEntityAttributes(event::put));

        modContainer.registerConfig(ModConfig.Type.COMMON, KnightQuestCommonConfigs.SPEC, "knightquest.toml");
        modEventBus.addListener(this::onCommonSetup);

        KnightQuestCreativeModeTabs.registerWeaponItem(() -> KnightQuestItems.PALADIN_SWORD.get());
        KnightQuestCreativeModeTabs.registerWeaponItem(() -> KnightQuestItems.KHOPESH.get());
        KnightQuestCreativeModeTabs.registerWeaponItem(() -> KnightQuestItems.CLEAVER.get());
        KnightQuestCreativeModeTabs.registerWeaponItem(() -> KnightQuestItems.KUKRI.get());
        KnightQuestCreativeModeTabs.registerWeaponItem(() -> KnightQuestItems.UCHIGATANA.get());
        KnightQuestCreativeModeTabs.registerWeaponItem(() -> KnightQuestItems.NAIL.get());

        KnightQuestCommon.init();

    }

    private void onCommonSetup(ModConfigEvent e) {
        if (e.getConfig().getType() == ModConfig.Type.COMMON) {
            KnightQuestCommonConfigs.assignValues();
        }
    }

}
