package dev.xylonity.knightquest.common.item;

import dev.xylonity.knightquest.common.block.GreatChaliceBlockEntity;
import dev.xylonity.knightquest.common.block.GreatChaliceState;
import dev.xylonity.knightquest.common.block.IGreatChaliceInteractable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

/**
 * Port of KnightLib's {@code GreatEssenceItem}: offers 4 charges to the chalice.
 */
public class GreatEssenceItem extends KnightQuestItem implements IGreatChaliceInteractable {

    public GreatEssenceItem(Properties properties, String tooltipInfoName) {
        super(properties, tooltipInfoName);
    }

    @Override
    public int getChargesToApply() {
        return 4;
    }

    @Override
    public boolean canInteract(GreatChaliceBlockEntity chalice, Level level, Player player) {
        return chalice.getState() == GreatChaliceState.NORMAL || chalice.getState() == GreatChaliceState.EMPTY;
    }

    @Override
    public void onPostInteraction(GreatChaliceBlockEntity chalice, Player player, Level level, BlockHitResult hit) {
        if (chalice.getState() == GreatChaliceState.EMPTY) chalice.setState(GreatChaliceState.NORMAL);
    }

    @Override
    public int shrinkItemAmount() {
        return 1;
    }

    @NotNull
    @Override
    public ItemStack getDefaultInstance() {
        return super.getDefaultInstance();
    }
}
