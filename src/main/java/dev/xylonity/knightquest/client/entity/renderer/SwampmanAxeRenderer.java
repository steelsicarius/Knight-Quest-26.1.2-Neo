package dev.xylonity.knightquest.client.entity.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
import dev.xylonity.knightquest.client.entity.model.SwampmanAxeModel;
import dev.xylonity.knightquest.common.entity.entities.SwampmanAxeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

/**
 * GeckoLib 5 renderer for the thrown swampman axe.
 * <p>
 * Port note: the v4 render() custom orientation/shake is not yet ported (GeckoLib 5
 * render-state based pipeline; see README) - the axe is rendered with the standard
 * entity rotations.
 */
public class SwampmanAxeRenderer extends GeoEntityRenderer<SwampmanAxeEntity, EntityRenderState> {

    public SwampmanAxeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SwampmanAxeModel());
    }
}
