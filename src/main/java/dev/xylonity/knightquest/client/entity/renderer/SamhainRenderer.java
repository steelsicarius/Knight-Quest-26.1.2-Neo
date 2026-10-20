package dev.xylonity.knightquest.client.entity.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
import dev.xylonity.knightquest.client.entity.model.SamhainModel;
import dev.xylonity.knightquest.common.entity.entities.SamhainEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

/**
 * GeckoLib 5 renderer for SamhainEntity.
 * <p>
 * Port note: the v4 render() scaling is applied via {@code withScale}; the v4
 * setCustomAnimations head tracking is not yet ported (GeckoLib 5 bone adjustment needs
 * the runtime-injected GeoRenderState; see README).
 */
public class SamhainRenderer extends GeoEntityRenderer<SamhainEntity, EntityRenderState> {

    public SamhainRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SamhainModel());

        this.withScale(0.7f, 0.7f);    }
}
