package dev.xylonity.knightquest.common.item;

import dev.xylonity.knightquest.common.block.GreatChaliceBlockEntity;
import dev.xylonity.knightquest.common.block.GreatChaliceState;
import dev.xylonity.knightquest.common.block.IGreatChaliceInteractable;
import dev.xylonity.knightquest.registry.KnightQuestItems;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Port of KnightLib's {@code EmptyGrailItem}: drains a full chalice into a filled goblet
 * (the filled goblet is used in the armor recipes).
 */
public class EmptyGobletItem extends KnightQuestItem implements IGreatChaliceInteractable {

    public EmptyGobletItem(Properties properties, String tooltipInfoName) {
        super(properties, tooltipInfoName);
    }

    @Override
    public int getChargesToApply() {
        return -IGreatChaliceInteractable.MAX_CHARGES;
    }

    @Override
    public boolean canInteract(GreatChaliceBlockEntity chalice, Level level, Player player) {
        return chalice.isFull() && chalice.getState() == GreatChaliceState.NORMAL;
    }

    @Override
    public @NotNull Set<ItemStack> getRewards() {
        return Set.of(new ItemStack(KnightQuestItems.FILLED_GOBLET.get()));
    }

    @Override
    public @NotNull Set<net.minecraft.sounds.SoundEvent> getInteractionSounds() {
        return Set.of(SoundEvents.BREWING_STAND_BREW);
    }

    @Override
    public void onPostInteraction(GreatChaliceBlockEntity chalice, Player player, Level level, BlockHitResult hit) {
        if (level instanceof ServerLevel sv) {
            RandomSource rand = sv.getRandom();
            for (int i = 0; i < 25; i++) {
                double px, py, pz;
                double r = 0.7 * Math.sqrt(rand.nextDouble());
                double t = rand.nextDouble() * Math.PI * 2;
                px = chalice.getBlockPos().getX() + 0.5 + r * Math.cos(t);
                pz = chalice.getBlockPos().getZ() + 0.5 + r * Math.sin(t);
                py = chalice.getBlockPos().getY() + 1.0 + rand.nextDouble() * 0.3;
                sv.sendParticles(net.minecraft.core.particles.SpellParticleOption.create(ParticleTypes.EFFECT, -1, 1.0F), px, py, pz, 1, 0.0, 0.0, 0.0, 0.0);
            }
        }
    }

}
