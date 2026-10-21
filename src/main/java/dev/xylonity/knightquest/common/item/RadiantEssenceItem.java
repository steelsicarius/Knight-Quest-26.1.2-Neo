package dev.xylonity.knightquest.common.item;

import dev.xylonity.knightquest.common.block.GreatChaliceBlockEntity;
import dev.xylonity.knightquest.common.block.GreatChaliceState;
import dev.xylonity.knightquest.common.block.IGreatChaliceInteractable;
import dev.xylonity.knightquest.registry.KnightQuestEntities;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Port of KnightLib interop: offering radiant essence to a filled (NORMAL) chalice drains it
 * and summons the Netherman at the chalice position.
 */
public class RadiantEssenceItem extends KnightQuestItem implements IGreatChaliceInteractable {

    public RadiantEssenceItem(Properties properties, String tooltipInfoName) {
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
        chalice.setCharges(0);

        var netherman = KnightQuestEntities.NETHERMAN.get().create(level, EntitySpawnReason.MOB_SUMMONED);
        if (netherman != null) {
            netherman.snapTo(chalice.getBlockPos().getCenter());
            level.addFreshEntity(netherman);
        }
    }

    @Override
    public @NotNull Set<SoundEvent> getInteractionSounds() {
        return Set.of(SoundEvents.BREWING_STAND_BREW);
    }

}
