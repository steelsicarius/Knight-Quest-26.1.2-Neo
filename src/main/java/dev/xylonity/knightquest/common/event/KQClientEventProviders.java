package dev.xylonity.knightquest.common.event;

import dev.xylonity.knightquest.KnightQuest;
import dev.xylonity.knightquest.client.blockentity.renderer.GreatChaliceRenderer;
import dev.xylonity.knightquest.client.entity.renderer.*;
import dev.xylonity.knightquest.common.particle.*;
import dev.xylonity.knightquest.registry.KnightQuestEntities;
import dev.xylonity.knightquest.registry.KnightQuestParticles;
import dev.xylonity.knightquest.common.block.GreatChaliceBlockEntity;
import dev.xylonity.knightquest.registry.KnightQuestBlockEntities;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

@EventBusSubscriber(modid = KnightQuest.MOD_ID, value = Dist.CLIENT)
public class KQClientEventProviders {

    @SubscribeEvent
    public static void registerParticleFactories(final RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(KnightQuestParticles.GREMLIN_PARTICLE.get(), GremlinParticle.Provider::new);
        event.registerSpriteSet(KnightQuestParticles.YELLOW_PARTICLE.get(), YellowParticle.Provider::new);
        event.registerSpriteSet(KnightQuestParticles.GHOSTY_PARTICLE.get(), GhostyParticle.Provider::new);
        event.registerSpriteSet(KnightQuestParticles.SNOWFLAKE_PARTICLE.get(), SnowflakeParticle.Provider::new);
        event.registerSpriteSet(KnightQuestParticles.POISON_PARTICLE.get(), PoisonParticle.Provider::new);
        event.registerSpriteSet(KnightQuestParticles.POISON_CLOUD_PARTICLE.get(), PoisonCloudParticle.Provider::new);
        event.registerSpriteSet(KnightQuestParticles.STARSET.get(), YellowParticle.Provider::new);
    }

    @SubscribeEvent
    public static void registerBlockEntityRenderers(net.neoforged.fml.event.lifecycle.FMLClientSetupEvent event) {
        event.enqueueWork(() -> BlockEntityRenderers.register(
                KnightQuestBlockEntities.GREAT_CHALICE.get(), GreatChaliceRenderer::new));
    }

    @SubscribeEvent
    public static void registerEntityRenderers(net.neoforged.fml.event.lifecycle.FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            EntityRenderers.register(KnightQuestEntities.GREMLIN.get(), GremlinRenderer::new);
            EntityRenderers.register(KnightQuestEntities.ELDBOMB.get(), EldBombRenderer::new);
            EntityRenderers.register(KnightQuestEntities.ELDKNIGHT.get(), EldKnightRenderer::new);
            EntityRenderers.register(KnightQuestEntities.SWAMPMAN.get(), SwampmanRenderer::new);
            EntityRenderers.register(KnightQuestEntities.FALLEN_KNIGHT.get(), FallenKnightRenderer::new);
            EntityRenderers.register(KnightQuestEntities.RATMAN.get(), RatmanRenderer::new);
            EntityRenderers.register(KnightQuestEntities.SAMHAIN.get(), SamhainRenderer::new);
            EntityRenderers.register(KnightQuestEntities.LIZZY.get(), LizzyRenderer::new);
            EntityRenderers.register(KnightQuestEntities.BADPATCH.get(), BadPatchRenderer::new);
            EntityRenderers.register(KnightQuestEntities.SHIELD.get(), ShieldRenderer::new);
            EntityRenderers.register(KnightQuestEntities.GHOSTY.get(), GhostyRenderer::new);
            EntityRenderers.register(KnightQuestEntities.NETHERMAN.get(), NethermanRenderer::new);
            EntityRenderers.register(KnightQuestEntities.NETHERMAN_PROJECTILE_CHARGE.get(), NethermanProjectileChargeRenderer::new);
            EntityRenderers.register(KnightQuestEntities.NETHERMAN_CLONE.get(), NethermanCloneRenderer::new);
            EntityRenderers.register(KnightQuestEntities.SWAMPMAN_AXE.get(), SwampmanAxeRenderer::new);
        });
    }

}
