package dev.xylonity.knightquest.client.entity.model;

import com.geckolib.renderer.base.GeoRenderState;
import dev.xylonity.knightquest.KnightQuestCommon;
import dev.xylonity.knightquest.common.entity.entities.RatmanEntity;
import net.minecraft.resources.Identifier;

/**
 * GeckoLib 5 model. The per-variation texture is resolved from live entity state
 * in addAdditionalStateData.
 */
public class RatmanModel extends KQEntityModel<RatmanEntity> {

    public RatmanModel() {
        super("ratman");
    }

    @Override
    public void addAdditionalStateData(RatmanEntity animatable, Object renderData, GeoRenderState renderState) {
        super.addAdditionalStateData(animatable, renderData, renderState);

        renderState.addGeckolibData(TEXTURE_OVERRIDE,
                Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, "textures/entity/ratman" + animatable.getVariation() + ".png"));
    }
}
