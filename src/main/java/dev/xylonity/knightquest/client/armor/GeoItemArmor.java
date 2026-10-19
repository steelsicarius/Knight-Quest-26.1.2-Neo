package dev.xylonity.knightquest.client.armor;

import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.state.AnimationTest;
import com.geckolib.util.GeckoLibUtil;
import dev.xylonity.knightquest.common.item.KQArmorItem;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.function.Consumer;

/**
 * Gecko item armor. GeckoLib 5 port: instead of the old v4 GeoArmorItem + HumanoidModel
 * GeoRenderProvider, v5 armor items implement {@link GeoItem} and expose a
 * {@link com.geckolib.renderer.GeoArmorRenderer} through {@link GeoRenderProvider}.
 */
public class GeoItemArmor extends KQArmorItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final String resourceKey;

    public GeoItemArmor(ArmorMaterial material, ArmorType type, Properties properties, String resourceKey, boolean hasTooltip) {
        super(material, type, properties, hasTooltip);
        this.resourceKey = resourceKey;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoItemArmorRenderer renderer;

            @Override
            public com.geckolib.renderer.GeoArmorRenderer<?, ?> getGeoArmorRenderer(ItemStack itemStack, EquipmentSlot equipmentSlot) {
                if (this.renderer == null)
                    this.renderer = new GeoItemArmorRenderer(new GeoItemArmorModel(resourceKey));

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("controller", 0, this::predicate));
    }

    private PlayState predicate(AnimationTest<GeoItemArmor> animationTest) {
        animationTest.setAndContinue(RawAnimation.begin().then("idle", com.geckolib.animation.object.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
