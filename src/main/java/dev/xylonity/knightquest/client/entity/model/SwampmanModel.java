package dev.xylonity.knightquest.client.entity.model;

import com.geckolib.renderer.base.GeoRenderState;
import dev.xylonity.knightquest.KnightQuestCommon;
import dev.xylonity.knightquest.common.entity.entities.SwampmanEntity;
import net.minecraft.resources.Identifier;

/**
 * GeckoLib 5 model. The phase-2 texture is resolved from live entity state
 * in addAdditionalStateData.
 */
public class SwampmanModel extends KQEntityModel<SwampmanEntity> {

    private static final Identifier PHASE2_TEXTURE = Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, "textures/entity/swampman_2.png");

    public SwampmanModel() {
        super("swampman");
    }

    @Override
    public void addAdditionalStateData(SwampmanEntity animatable, Object renderData, GeoRenderState renderState) {
        super.addAdditionalStateData(animatable, renderData, renderState);

        if (animatable.getPhase() == 2) {
            renderState.addGeckolibData(TEXTURE_OVERRIDE, PHASE2_TEXTURE);
        }
    }
}
