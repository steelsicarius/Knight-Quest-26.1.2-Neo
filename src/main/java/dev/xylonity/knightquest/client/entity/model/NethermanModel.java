package dev.xylonity.knightquest.client.entity.model;

import com.geckolib.renderer.base.GeoRenderState;
import dev.xylonity.knightquest.KnightQuestCommon;
import dev.xylonity.knightquest.common.entity.boss.NethermanEntity;
import net.minecraft.resources.Identifier;

/**
 * GeckoLib 5 model for the Netherman boss. The geo model, texture and animation all switch
 * with the boss phase; the model/texture choices are resolved from live entity state in
 * addAdditionalStateData, while the animation can be computed directly from the animatable.
 */
public class NethermanModel extends KQEntityModel<NethermanEntity> {

    private static final String TEXTURE_PATH = "textures/entity/";

    public NethermanModel() {
        super("netherman");
    }

    @Override
    public void addAdditionalStateData(NethermanEntity animatable, Object renderData, GeoRenderState renderState) {
        super.addAdditionalStateData(animatable, renderData, renderState);

        // Model
        String model;
        if (animatable.isDeadOrDying()) {
            model = "netherman_magic";
        } else {
            model = switch (animatable.getPhase()) {
                case 1 -> "netherman_fire";
                case 2 -> animatable.getCounterSwitchPhase2() == 130 ? "netherman_ice" : "netherman_fire";
                default -> animatable.getCounterSwitchPhase3() == 160 ? "netherman_magic" : "netherman_ice";
            };
        }
        renderState.addGeckolibData(MODEL_OVERRIDE, Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, "entity/" + model));

        // Texture
        Identifier texture;
        if (animatable.isDeadOrDying()) {
            texture = Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, TEXTURE_PATH + "netherman_magic.png");
        } else if (animatable.getPhase() == 2 && animatable.getCounterSwitchPhase2() < 130) {
            int c = animatable.getCounterSwitchPhase2();
            String path;
            if (c < 50) {
                path = TEXTURE_PATH + "netherman_fire.png";
            } else if (c < 60) {
                path = TEXTURE_PATH + "animated/netherman_switch_phase2_0.png";
            } else if (c < 70) {
                path = TEXTURE_PATH + "animated/netherman_switch_phase2_1.png";
            } else if (c < 80) {
                path = TEXTURE_PATH + "animated/netherman_switch_phase2_2.png";
            } else if (c < 90) {
                path = TEXTURE_PATH + "animated/netherman_switch_phase2_3.png";
            } else if (c < 100) {
                path = TEXTURE_PATH + "animated/netherman_switch_phase2_4.png";
            } else if (c < 110) {
                path = TEXTURE_PATH + "animated/netherman_switch_phase2_5.png";
            } else if (c < 120) {
                path = TEXTURE_PATH + "animated/netherman_switch_phase2_6.png";
            } else if (c < 130) {
                path = TEXTURE_PATH + "animated/netherman_switch_phase2_7.png";
            } else {
                path = TEXTURE_PATH + "netherman_ice.png";
            }
            texture = Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, path);
        } else if (animatable.getPhase() == 3 && animatable.getCounterSwitchPhase3() < 160) {
            int c = animatable.getCounterSwitchPhase3();
            String path;
            if (c < 50) {
                path = TEXTURE_PATH + "netherman_ice.png";
            } else if (c < 60) {
                path = TEXTURE_PATH + "animated/netherman_switch_phase3_0.png";
            } else if (c < 70) {
                path = TEXTURE_PATH + "animated/netherman_switch_phase3_1.png";
            } else if (c < 80) {
                path = TEXTURE_PATH + "animated/netherman_switch_phase3_2.png";
            } else if (c < 90) {
                path = TEXTURE_PATH + "animated/netherman_switch_phase3_3.png";
            } else if (c < 100) {
                path = TEXTURE_PATH + "animated/netherman_switch_phase3_4.png";
            } else if (c < 110) {
                path = TEXTURE_PATH + "animated/netherman_switch_phase3_5.png";
            } else if (c < 120) {
                path = TEXTURE_PATH + "animated/netherman_switch_phase3_6.png";
            } else if (c < 160) {
                path = TEXTURE_PATH + "animated/netherman_switch_phase3_7.png";
            } else {
                path = TEXTURE_PATH + "netherman_magic.png";
            }
            texture = Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, path);
        } else {
            texture = switch (animatable.getPhase()) {
                case 1 -> Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, TEXTURE_PATH + "netherman_fire.png");
                case 2 -> Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, TEXTURE_PATH + "netherman_ice.png");
                default -> Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, TEXTURE_PATH + "netherman_magic.png");
            };
        }
        renderState.addGeckolibData(TEXTURE_OVERRIDE, texture);
    }

    @Override
    public Identifier getAnimationResource(NethermanEntity animatable) {
        if (animatable.isDeadOrDying()) {
            return Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, "entity/netherman_magic");
        }

        if (animatable.getCounterSwitchPhase2() == 130 && animatable.getPhase() == 2
                || (animatable.getCounterSwitchPhase3() != 160) && animatable.getPhase() == 3) {
            return Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, "entity/netherman_ice");
        } else if (animatable.getCounterSwitchPhase3() == 160 && animatable.getPhase() == 3) {
            return Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, "entity/netherman_magic");
        }

        return Identifier.fromNamespaceAndPath(KnightQuestCommon.MOD_ID, "entity/netherman_fire");
    }
}
