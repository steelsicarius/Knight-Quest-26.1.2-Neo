package dev.xylonity.knightquest.client.blockentity.renderer;

import com.geckolib.animation.state.BoneSnapshot;
import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.model.DefaultedBlockGeoModel;
import com.geckolib.renderer.GeoBlockRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import com.geckolib.renderer.base.RenderPassInfo;
import dev.xylonity.knightquest.KnightQuestCommon;
import dev.xylonity.knightquest.common.block.GreatChaliceBlockEntity;
import dev.xylonity.knightquest.common.block.GreatChaliceState;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.resources.Identifier;

/**
 * Port of KnightLib's {@code GreatChaliceRenderer}: the geo model renders the cup and the
 * "liquid" bone rises with the charges (hidden at 0, +8px at max 12) and the texture swaps
 * to the chaotic variant while attuned.
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class GreatChaliceRenderer extends GeoBlockRenderer<GreatChaliceBlockEntity, BlockEntityRenderState> {

    public static final DataTicket<Integer> CHARGES = DataTicket.create("knightquest:chalice_charges", Integer.class);
    public static final DataTicket<Boolean> CHAOTIC = DataTicket.create("knightquest:chalice_chaotic", Boolean.class);

    private static final Identifier NORMAL_TEXTURE = Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, "textures/block/great_chalice.png");
    private static final Identifier CHAOTIC_TEXTURE = Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, "textures/block/great_chalice_chaotic.png");

    public GreatChaliceRenderer(BlockEntityRendererProvider.Context context) {
        super(context, new Model());
    }

    @Override
    public void captureDefaultRenderState(GreatChaliceBlockEntity blockEntity, Void renderData, BlockEntityRenderState renderState, float partialTick) {
        super.captureDefaultRenderState(blockEntity, renderData, renderState, partialTick);
        ((GeoRenderState) (Object) renderState).addGeckolibData(CHARGES, blockEntity.getCharges());
        ((GeoRenderState) (Object) renderState).addGeckolibData(CHAOTIC, blockEntity.getState() == GreatChaliceState.CHAOTIC);
    }

    @Override
    public void adjustModelBonesForRender(RenderPassInfo info, com.geckolib.renderer.base.BoneSnapshots snapshots) {
        super.adjustModelBonesForRender(info, snapshots);

        GeoRenderState renderState = info.renderState();
        int charges = renderState.getOrDefaultGeckolibData(CHARGES, 0);

        snapshots.get("liquid").ifPresent((Object liquid) -> ((BoneSnapshot) liquid).skipRender(charges <= 0));
        if (charges > 0) {
            snapshots.get("liquid").ifPresent((Object liquid) -> ((BoneSnapshot) liquid).setTranslation(0f, (charges / 12f) * 8f, 0f));
        }
    }

    public static class Model extends DefaultedBlockGeoModel<GreatChaliceBlockEntity> {

        public Model() {
            super(Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, "great_chalice"));
        }

        @Override
        public Identifier getTextureResource(GeoRenderState renderState) {
            Boolean chaotic = renderState.<Boolean>getOrDefaultGeckolibData(CHAOTIC, (Boolean) null);
            return Boolean.TRUE.equals(chaotic) ? CHAOTIC_TEXTURE : NORMAL_TEXTURE;
        }
    }
}
