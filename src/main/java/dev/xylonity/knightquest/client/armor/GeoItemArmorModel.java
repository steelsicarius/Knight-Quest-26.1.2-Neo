package dev.xylonity.knightquest.client.armor;

import com.geckolib.model.DefaultedItemGeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import dev.xylonity.knightquest.KnightQuestCommon;
import net.minecraft.resources.Identifier;

/**
 * GeckoLib 5 armor model. Default paths now live under {@code geckolib/models/item/} and
 * {@code geckolib/animations/item/}; the texture stays at {@code textures/armor/}.
 */
public class GeoItemArmorModel extends DefaultedItemGeoModel<GeoItemArmor> {

    private final String resourceKey;

    public GeoItemArmorModel(String resourceKey) {
        super(Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, resourceKey));
        this.resourceKey = resourceKey;
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        return Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, "textures/armor/" + resourceKey + ".png");
    }

    @Override
    public Identifier getAnimationResource(GeoItemArmor animatable) {
        return Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, "geckolib/animations/item/helmet.animation.json");
    }

}
