package dev.xylonity.knightquest.client.entity.model;

import com.geckolib.renderer.base.GeoRenderState;
import dev.xylonity.knightquest.KnightQuestCommon;
import dev.xylonity.knightquest.common.entity.entities.EldBombEntity;
import net.minecraft.resources.Identifier;

/**
 * GeckoLib 5 model. The white-flash texture while swelling is resolved from live entity
 * state in addAdditionalStateData (v5 render-state based, replacing the old
 * renderer getTextureLocation hook).
 */
public class EldBombModel extends KQEntityModel<EldBombEntity> {

    private static final Identifier DEFAULT_TEXTURE = Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, "textures/entity/eldbomb.png");
    private static final Identifier WHITE_TEXTURE = Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, "textures/entity/eldbomb_white.png");

    public EldBombModel() {
        super("eldbomb");
    }

    @Override
    public void addAdditionalStateData(EldBombEntity animatable, Object renderData, GeoRenderState renderState) {
        super.addAdditionalStateData(animatable, renderData, renderState);

        if (animatable.getSwell() > 10 && (animatable.tickCount / 5) % 2 == 0) {
            renderState.addGeckolibData(TEXTURE_OVERRIDE, WHITE_TEXTURE);
        }
    }
}
