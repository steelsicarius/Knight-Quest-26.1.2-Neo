package dev.xylonity.knightquest.common.item;

import dev.xylonity.knightquest.common.block.GreatChaliceBlockEntity;
import dev.xylonity.knightquest.common.block.GreatChaliceState;
import dev.xylonity.knightquest.common.block.IGreatChaliceInteractable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Port of KnightLib's {@code SmallEssenceItem}: offers 1 charge to the chalice.
 */
public class SmallEssenceItem extends KnightQuestItem implements IGreatChaliceInteractable {

    public SmallEssenceItem(Properties properties, String tooltipInfoName) {
        super(properties, tooltipInfoName);
    }

    @Override
    public int getChargesToApply() {
        return 1;
    }

    @Override
    public boolean canInteract(GreatChaliceBlockEntity chalice, Level level, Player player) {
        return chalice.getState() == GreatChaliceState.NORMAL || chalice.getState() == GreatChaliceState.EMPTY;
    }

    @Override
    public void onPostInteraction(GreatChaliceBlockEntity chalice, Player player, Level level, BlockHitResult hit) {
        if (chalice.getState() == GreatChaliceState.EMPTY) chalice.setState(GreatChaliceState.NORMAL);
    }

}
