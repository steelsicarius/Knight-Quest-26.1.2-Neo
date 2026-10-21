package dev.xylonity.knightquest.common.block;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Defines how an item should interact with the Great Chalice.
 * Port of KnightLib's {@code IGreatChaliceInteractable}.
 */
public interface IGreatChaliceInteractable {

    /** Great chalice maximum charges */
    int MAX_CHARGES = 12;

    /**
     * Returns the number of charges to add/remove to the chalice.
     */
    int getChargesToApply();

    /**
     * Determines how many items should be consumed from this stack during the interaction.
     */
    default int shrinkItemAmount() {
        return 1;
    }

    /**
     * Determines if this item can interact with the chalice in its current state.
     * Charge amount and the chalice state must be checked here.
     */
    boolean canInteract(GreatChaliceBlockEntity chalice, Level level, Player player);

    /**
     * Called immediately before the main interaction logic is processed.
     */
    default void onPreInteraction(GreatChaliceBlockEntity chalice, Player player, Level level, BlockHitResult hit) {
        ;;
    }

    /**
     * Called after the main interaction logic has been successfully processed.
     */
    default void onPostInteraction(GreatChaliceBlockEntity chalice, Player player, Level level, BlockHitResult hit) {
        ;;
    }

    /**
     * Provides a set of item stacks to spawn as rewards when the interaction completes.
     */
    default @NotNull Set<ItemStack> getRewards() {
        return Set.of();
    }

    /**
     * Provides a set of sound effects to play when the interaction occurs.
     */
    default @NotNull Set<SoundEvent> getInteractionSounds() {
        return Set.of();
    }

    /**
     * Returns the sound to play if the chalice reaches its maximum capacity.
     */
    default @NotNull SoundEvent getFullChargeSound() {
        return net.minecraft.sounds.SoundEvents.PLAYER_LEVELUP;
    }

}
