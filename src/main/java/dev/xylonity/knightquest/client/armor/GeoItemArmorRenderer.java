package dev.xylonity.knightquest.client.armor;

import com.geckolib.renderer.GeoArmorRenderer;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;

public class GeoItemArmorRenderer extends GeoArmorRenderer<GeoItemArmor, HumanoidRenderState> {

    public GeoItemArmorRenderer(GeoItemArmorModel model) {
        super(model);
    }

}
