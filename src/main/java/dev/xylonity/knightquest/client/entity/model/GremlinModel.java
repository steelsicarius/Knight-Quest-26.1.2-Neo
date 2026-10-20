package dev.xylonity.knightquest.client.entity.model;

import com.geckolib.renderer.base.GeoRenderState;
import dev.xylonity.knightquest.KnightQuestCommon;
import dev.xylonity.knightquest.common.entity.entities.GremlinEntity;
import net.minecraft.resources.Identifier;

/**
 * GeckoLib 5 model. The angry texture on second phase is resolved from live entity state
 * in addAdditionalStateData.
 */
public class GremlinModel extends KQEntityModel<GremlinEntity> {

    private static final Identifier ANGRY_TEXTURE = Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, "textures/entity/gremlin_angry.png");

    public GremlinModel() {
        super("gremlin");
    }

    @Override
    public void addAdditionalStateData(GremlinEntity animatable, Object renderData, GeoRenderState renderState) {
        super.addAdditionalStateData(animatable, renderData, renderState);

        if (animatable.getPhase() == 2) {
            renderState.addGeckolibData(TEXTURE_OVERRIDE, ANGRY_TEXTURE);
        }
    }
}
