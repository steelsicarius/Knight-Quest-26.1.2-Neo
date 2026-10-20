package dev.xylonity.knightquest.client.entity.model;

import com.geckolib.constant.dataticket.DataTicket;
import com.geckolib.renderer.base.GeoRenderState;
import dev.xylonity.knightquest.KnightQuestCommon;
import dev.xylonity.knightquest.common.entity.entities.SwampmanAxeEntity;
import net.minecraft.resources.Identifier;

/**
 * GeckoLib 5 model for the thrown swampman axe. Publishes the remaining shake time so the
 * renderer can reproduce the v4 pre-impact shake.
 */
public class SwampmanAxeModel extends KQEntityModel<SwampmanAxeEntity> {

    public static final DataTicket<Float> SHAKE_TICKET = DataTicket.create("knightquest:swampman_axe_shake", Float.class);

    public SwampmanAxeModel() {
        super("swampman_axe");
    }

    @Override
    public void addAdditionalStateData(SwampmanAxeEntity animatable, Object renderData, GeoRenderState renderState) {
        super.addAdditionalStateData(animatable, renderData, renderState);

        renderState.addGeckolibData(SHAKE_TICKET, (float) animatable.shakeTime);
    }
}
