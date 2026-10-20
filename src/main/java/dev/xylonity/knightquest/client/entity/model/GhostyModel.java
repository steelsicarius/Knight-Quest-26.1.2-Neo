package dev.xylonity.knightquest.client.entity.model;

import com.geckolib.renderer.base.GeoRenderState;
import dev.xylonity.knightquest.KnightQuestCommon;
import dev.xylonity.knightquest.common.entity.entities.GhostyEntity;
import net.minecraft.resources.Identifier;

/**
 * GeckoLib 5 model. Uses the shared helmet animation (geckolib/animations/entity/helmet.animation.json).
 */
public class GhostyModel extends KQEntityModel<GhostyEntity> {

    public GhostyModel() {
        super("ghosty");
    }

    @Override
    public Identifier getAnimationResource(GhostyEntity animatable) {
        return Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, "entity/helmet");
    }
}
