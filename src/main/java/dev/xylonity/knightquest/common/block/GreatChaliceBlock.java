package dev.xylonity.knightquest.common.block;

import dev.xylonity.knightquest.common.item.KQWeaponItem;
import dev.xylonity.knightquest.registry.KnightQuestBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

/**
 * Port of KnightLib's {@code GreatChaliceBlock}: interaction is dispatched through
 * {@link IGreatChaliceInteractable} (implemented by the essence and goblet items).
 */
public class GreatChaliceBlock extends Block implements EntityBlock {

    private static final VoxelShape SHAPE = Stream.of(
            Block.box(0, 7, 0, 2, 16, 16),
            Block.box(14, 7, 0, 16, 16, 16),
            Block.box(0, 0, 0, 16, 2, 16),
            Block.box(0, 5, 0, 16, 7, 16),
            Block.box(2, 7, 0, 14, 16, 2),
            Block.box(2, 7, 14, 14, 16, 16),
            Block.box(4, 2, 4, 12, 5, 12)
    ).reduce((v1, v2) -> Shapes.join(v1, v2, BooleanOp.OR)).get();

    public GreatChaliceBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return KnightQuestBlockEntities.GREAT_CHALICE.get().create(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return type == KnightQuestBlockEntities.GREAT_CHALICE.get() ? GreatChaliceBlockEntity::tick : null;
    }

    @Override
    protected @NotNull InteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {

        if (level.isClientSide()) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if (!(level.getBlockEntity(pos) instanceof GreatChaliceBlockEntity chalice)) {
            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        // Weapon activation: a KQ weapon is attuned by a chaotic-filled chalice
        if (stack.getItem() instanceof KQWeaponItem weapon) {
            if (chalice.isFull() && chalice.getState() == GreatChaliceState.CHAOTIC
                    && !KQWeaponItem.isActivated(stack)) {
                chalice.setCharges(0);
                chalice.setState(GreatChaliceState.EMPTY);
                KQWeaponItem.setActivated(stack, true);
                level.playSound(null, pos, net.minecraft.sounds.SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.BLOCKS, 1f, 1f);
                return InteractionResult.SUCCESS;
            }

            return InteractionResult.TRY_WITH_EMPTY_HAND;
        }

        if (stack.getItem() instanceof IGreatChaliceInteractable actor) {
            if (!actor.canInteract(chalice, level, player)) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }

            actor.onPreInteraction(chalice, player, level, hit);

            int total = chalice.getCharges() + actor.getChargesToApply();
            if (total < 0 || total > IGreatChaliceInteractable.MAX_CHARGES) {
                return InteractionResult.TRY_WITH_EMPTY_HAND;
            }

            chalice.setCharges(total);
            if (!player.getAbilities().instabuild) stack.shrink(actor.shrinkItemAmount());

            if (!actor.getRewards().isEmpty()) {
                for (ItemStack reward : actor.getRewards()) {
                    if (!player.getInventory().add(reward)) {
                        spawnReward(level, pos, reward, player);
                    }
                }
            }

            if (!actor.getInteractionSounds().isEmpty()) {
                for (SoundEvent sound : actor.getInteractionSounds()) {
                    level.playSound(null, pos, sound, SoundSource.BLOCKS, 1f, 1f);
                }
            }

            if (chalice.getCharges() == IGreatChaliceInteractable.MAX_CHARGES) {
                level.playSound(null, pos, actor.getFullChargeSound(), SoundSource.BLOCKS, 1f, 1f);
            }

            actor.onPostInteraction(chalice, player, level, hit);

            return InteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hit);
    }

    private void spawnReward(Level level, BlockPos pos, ItemStack reward, Player player) {
        Entity entity = new ItemEntity(level, pos.getX() + 0.5, pos.getY() + player.getBbHeight() * 0.5, pos.getZ(), reward);
        level.addFreshEntity(entity);
    }

}
