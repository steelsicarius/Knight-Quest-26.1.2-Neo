package dev.xylonity.knightquest.common.item.blockitem;

import com.geckolib.animatable.GeoItem;
import com.geckolib.animatable.client.GeoRenderProvider;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.model.GeoModel;
import com.geckolib.renderer.GeoItemRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.util.GeckoLibUtil;
import dev.xylonity.knightquest.KnightQuestCommon;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Block item for the Great Chalice: renders the same geo model as the block entity
 * (original KnightLib used a builtin/entity item model driven by the BER).
 */
public class GreatChaliceBlockItem extends BlockItem implements GeoItem {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public GreatChaliceBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private GeoItemRenderer<GreatChaliceBlockItem> renderer;

            @Override
            public GeoItemRenderer<?> getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new GeoItemRenderer<>(new ItemModel());

                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new com.geckolib.animation.AnimationController<>("main", 0, state ->
                state.setAndContinue(com.geckolib.animation.RawAnimation.begin().thenLoop("idle"))));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.accept(Component.translatable("tooltip.item.knightquest.great_chalice"));
        super.appendHoverText(stack, context, display, tooltipComponents, tooltipFlag);
    }

    /**
     * Shares the block's geo/animation/texture assets (geckolib/models/block/...).
     */
    public static class ItemModel extends GeoModel<GreatChaliceBlockItem> {
        private static final Identifier MODEL = Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, "block/great_chalice");
        private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, "textures/block/great_chalice.png");

        @Override
        public Identifier getModelResource(GeoRenderState renderState) {
            return MODEL;
        }

        @Override
        public Identifier getTextureResource(GeoRenderState renderState) {
            return TEXTURE;
        }

        @Override
        public Identifier getAnimationResource(GreatChaliceBlockItem animatable) {
            return MODEL;
        }
    }
}
