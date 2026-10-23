package dev.xylonity.knightquest.common.block;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.context.UseOnContext;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Port of KnightLib's {@code IHomunculusInteractable}: defines how a block should interact
 * with the Homunculus item when used on it.
 */
public interface IHomunculusInteractable {

    /**
     * Determines how many items should be consumed from the item stack during the interaction.
     *
     * @return the number of items to remove
     */
    default int shrinkItemAmount() {
        return 1;
    }

    /**
     * Determines if this item can interact with the block in its current state.
     *
     * @param context the context of the interaction
     * @return true if the interaction should proceed, false if not
     */
    boolean canInteract(UseOnContext context);

    /**
     * Called immediately before the main interaction logic is processed.
     *
     * @param context the context of the interaction
     */
    default void onPreInteraction(UseOnContext context) {
        ;;
    }

    /**
     * Called after the main interaction logic has been successfully processed.
     * This won't be triggered if the interaction isn't successful.
     *
     * @param context the context of the interaction
     */
    default void onPostInteraction(UseOnContext context) {
        ;;
    }

    /**
     * Provides a set of sound effects to play when the interaction occurs.
     * All sounds will be played at the block's location.
     *
     * @return a set of sounds to play
     */
    default @NotNull Set<SoundEvent> getInteractionSounds() {
        return Set.of();
    }

}
