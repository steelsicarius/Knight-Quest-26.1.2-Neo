package dev.xylonity.knightquest.common.item;

import dev.xylonity.knightquest.common.block.IHomunculusInteractable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Port of KnightLib's {@code HomunculusItem}: a catalyst item that interacts with blocks
 * implementing {@link IHomunculusInteractable}.
 */
public class HomunculusItem extends Item {

    public HomunculusItem(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull InteractionResult useOn(@NotNull UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        ItemStack stack = context.getItemInHand();

        if (level.getBlockState(pos).getBlock() instanceof IHomunculusInteractable actor) {
            if (!actor.canInteract(context)) {
                return InteractionResult.FAIL;
            }

            actor.onPreInteraction(context);

            if (!level.isClientSide()) stack.shrink(actor.shrinkItemAmount());

            if (!actor.getInteractionSounds().isEmpty()) {
                for (SoundEvent sound : actor.getInteractionSounds()) {
                    level.playSound(null, pos, sound, SoundSource.BLOCKS, 1f, 1f);
                }
            }

            actor.onPostInteraction(context);

            return InteractionResult.SUCCESS;
        }

        return super.useOn(context);
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> tooltip, @NotNull TooltipFlag flag) {
        tooltip.accept(Component.translatable("tooltip.item.knightquest.homunculus"));
    }

}
