package dev.xylonity.knightquest.client.entity.model;

import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.model.DefaultedEntityGeoModel;
import com.geckolib.renderer.base.GeoRenderState;
import dev.xylonity.knightquest.KnightQuestCommon;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;

/**
 * Base Knight Quest entity model (GeckoLib 5).
 * <p>
 * Uses the default GeckoLib 5 paths ({@code geckolib/models/entity/<name>.geo.json},
 * {@code geckolib/animations/entity/<name>.animation.json}, {@code textures/entity/<name>.png}).
 * <p>
 * Live-entity-driven render data (dynamic textures, head rotations, baby state) is published
 * into the render state here, because v5 models only receive the immutable render state in the
 * resource getters (this replaces the v4 {@code setCustomAnimations}/{@code getTextureLocation} hooks).
 */
public abstract class KQEntityModel<T extends com.geckolib.animatable.GeoAnimatable> extends DefaultedEntityGeoModel<T> {

    public static final DataTicket<Identifier> TEXTURE_OVERRIDE = DataTicket.create("knightquest:texture_override", Identifier.class);
    public static final DataTicket<Identifier> MODEL_OVERRIDE = DataTicket.create("knightquest:model_override", Identifier.class);
    public static final DataTicket<Float> HEAD_PITCH = DataTicket.create("knightquest:head_pitch", Float.class);
    public static final DataTicket<Float> HEAD_YAW = DataTicket.create("knightquest:head_yaw", Float.class);
    public static final DataTicket<Boolean> IS_BABY = DataTicket.create("knightquest:is_baby", Boolean.class);

    protected KQEntityModel(String name) {
        super(Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, name));
    }

    @Override
    public void addAdditionalStateData(T animatable, Object renderData, GeoRenderState renderState) {
        super.addAdditionalStateData(animatable, renderData, renderState);

        if (animatable instanceof LivingEntity living) {
            renderState.addGeckolibData(HEAD_PITCH, living.getXRot());
            float headYaw = Mth.rotLerp(1.0F, living.yHeadRotO, living.yHeadRot) - living.yBodyRot;
            renderState.addGeckolibData(HEAD_YAW, headYaw);
            renderState.addGeckolibData(IS_BABY, living.isBaby());
        }
    }

    @Override
    public Identifier getTextureResource(GeoRenderState renderState) {
        Identifier override = renderState.<Identifier>getOrDefaultGeckolibData(TEXTURE_OVERRIDE, (Identifier) null);
        return override != null ? override : super.getTextureResource(renderState);
    }

    @Override
    public Identifier getModelResource(GeoRenderState renderState) {
        Identifier override = renderState.<Identifier>getOrDefaultGeckolibData(MODEL_OVERRIDE, (Identifier) null);
        return override != null ? override : super.getModelResource(renderState);
    }

}
