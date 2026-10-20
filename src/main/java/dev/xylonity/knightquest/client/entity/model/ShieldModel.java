package dev.xylonity.knightquest.client.entity.model;

import com.geckolib.renderer.base.GeoRenderState;
import dev.xylonity.knightquest.KnightQuestCommon;
import dev.xylonity.knightquest.common.entity.entities.GhastlingEntity;
import net.minecraft.resources.Identifier;

/**
 * GeckoLib 5 model. Uses the shared helmet animation (geckolib/animations/entity/helmet.animation.json).
 */
public class ShieldModel extends KQEntityModel<GhastlingEntity> {

    public ShieldModel() {
        super("shield");
    }

    @Override
    public Identifier getAnimationResource(GhastlingEntity animatable) {
        return Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, "entity/helmet");
    }
}
