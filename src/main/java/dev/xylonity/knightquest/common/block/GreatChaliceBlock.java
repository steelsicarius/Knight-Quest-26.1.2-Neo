package dev.xylonity.knightquest.common.block;

import dev.xylonity.knightquest.KnightQuest;
import dev.xylonity.knightquest.common.item.KQWeaponItem;
import dev.xylonity.knightquest.registry.KnightQuestBlocks;
import dev.xylonity.knightquest.registry.KnightQuestEntities;
import dev.xylonity.knightquest.registry.KnightQuestItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

/**
 * Reimplementation of KnightLib's Great Chalice for the standalone port.
 * <p>
 * Charge cycle (see the shipped lang tooltips):
 * <ul>
 *     <li>Offer great essence (+2) or small essence (+1) until the chalice is full (4 charges).</li>
 *     <li>Offer chaotic essence to a full chalice -> the chalice is now filled with chaotic
 *     essence and can activate a Knight Quest weapon's abilities.</li>
 *     <li>Interact with a Knight Quest weapon on a chaotic-filled chalice -> the weapon is
 *     activated and the chalice is emptied.</li>
 *     <li>Offer radiant essence to a chaotic-filled chalice -> summons the Netherman.</li>
 *     <li>Offer an empty goblet -> gathers essence into a filled goblet (used in armor recipes).</li>
 * </ul>
 */
public class GreatChaliceBlock extends Block implements EntityBlock {

    /** Hollow cup: wide base slab + open-top walls (matches the 3D model). */
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(1, 0, 1, 15, 2, 15),
            Block.box(2, 2, 2, 14, 13, 14));

    public GreatChaliceBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext context) {
        return SHAPE;
    }


    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new GreatChaliceBlockEntity(pos, state);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof GreatChaliceBlockEntity chalice) {
            ItemStack held = stack.getItem() == KnightQuestItems.GREAT_ESSENCE.get() ? stack
                    : stack;

            if (stack.is(KnightQuestItems.GREAT_ESSENCE.get()) && !chalice.isFull()) {
                chalice.addCharges(2);
                stack.consume(1, player);
                playChime(level, pos);
                return InteractionResult.SUCCESS;
            }

            if (stack.is(KnightQuestItems.SMALL_ESSENCE.get()) && !chalice.isFull()) {
                chalice.addCharges(1);
                stack.consume(1, player);
                playChime(level, pos);
                return InteractionResult.SUCCESS;
            }

            if (stack.is(KnightQuestItems.CHAOTIC_ESSENCE.get()) && chalice.isFull() && !chalice.isChaotic()) {
                chalice.setChaotic(true);
                stack.consume(1, player);
                level.playSound(null, pos, SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }

            if (stack.is(KnightQuestItems.RADIANT_ESSENCE.get()) && chalice.isFull() && !chalice.isChaotic()) {
                chalice.setCharges(0);
                chalice.setChaotic(false);
                stack.consume(1, player);
                level.playSound(null, pos, SoundEvents.BREWING_STAND_BREW, SoundSource.BLOCKS, 1.0F, 1.0F);

                if (level instanceof ServerLevel serverLevel) {
                    var netherman = KnightQuestEntities.NETHERMAN.get().create(serverLevel, EntitySpawnReason.MOB_SUMMONED);
                    if (netherman != null) {
                        Vec3 center = pos.getCenter();
                        netherman.snapTo(center.x, center.y, center.z, 0.0F, 0.0F);
                        serverLevel.addFreshEntity(netherman);
                    }
                }

                return InteractionResult.SUCCESS;
            }

            if (stack.is(KnightQuestItems.EMPTY_GOBLET.get()) && chalice.getCharges() > 0) {
                chalice.addCharges(-1);
                stack.consume(1, player);
                ItemStack goblet = new ItemStack(KnightQuestItems.FILLED_GOBLET.get());
                if (!player.getInventory().add(goblet)) {
                    player.drop(goblet, false);
                }
                level.playSound(null, pos, SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1.0F, 1.0F);
                return InteractionResult.SUCCESS;
            }

            if (stack.getItem() instanceof KQWeaponItem && chalice.isFull() && chalice.isChaotic()) {
                if (!KQWeaponItem.isActivated(stack)) {
                    chalice.setCharges(0);
                    chalice.setChaotic(false);
                    KQWeaponItem.setActivated(stack, true);
                    level.playSound(null, pos, SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.BLOCKS, 1.0F, 1.0F);
                    return InteractionResult.SUCCESS;
                }

                return InteractionResult.FAIL;
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    private static void playChime(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

}
