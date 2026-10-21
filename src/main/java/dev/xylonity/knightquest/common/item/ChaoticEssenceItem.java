package dev.xylonity.knightquest.common.item;

import dev.xylonity.knightquest.common.block.GreatChaliceBlockEntity;
import dev.xylonity.knightquest.common.block.GreatChaliceState;
import dev.xylonity.knightquest.common.block.IGreatChaliceInteractable;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Port of KnightLib interop: offering chaotic essence to a filled (NORMAL) chalice attunes
 * it with chaotic essence, enabling a weapon's abilities upon interaction.
 */
public class ChaoticEssenceItem extends KnightQuestItem implements IGreatChaliceInteractable {

    public ChaoticEssenceItem(Properties properties, String tooltipInfoName) {
        super(properties, tooltipInfoName);
    }

    @Override
    public int getChargesToApply() {
        return 0;
    }

    @Override
    public boolean canInteract(GreatChaliceBlockEntity chalice, Level level, Player player) {
        return chalice.isFull() && chalice.getState() == GreatChaliceState.NORMAL;
    }

    @Override
    public void onPostInteraction(GreatChaliceBlockEntity chalice, Player player, Level level, BlockHitResult hit) {
        chalice.setState(GreatChaliceState.CHAOTIC);
    }

    @Override
    public @NotNull Set<SoundEvent> getInteractionSounds() {
        return Set.of(SoundEvents.EVOKER_PREPARE_SUMMON);
    }

}
