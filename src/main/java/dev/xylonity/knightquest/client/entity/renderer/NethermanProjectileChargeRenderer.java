package dev.xylonity.knightquest.client.entity.renderer;

import com.geckolib.renderer.GeoEntityRenderer;
import dev.xylonity.knightquest.client.entity.model.NethermanProjectileChargeEntityModel;
import dev.xylonity.knightquest.common.entity.boss.NethermanProjectileChargeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;

/**
 * GeckoLib 5 renderer for the netherman projectile charge.
 * <p>
 * Port note: the v4 render() x2 scaling is applied via {@code withScale}.
 */
public class NethermanProjectileChargeRenderer extends GeoEntityRenderer<NethermanProjectileChargeEntity, EntityRenderState> {

    public NethermanProjectileChargeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new NethermanProjectileChargeEntityModel());

        this.withScale(2.0f, 2.0f);
    }
}
