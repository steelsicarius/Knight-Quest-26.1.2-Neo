package dev.xylonity.knightquest.common.block;

import com.geckolib.animatable.GeoBlockEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.RawAnimation;
import com.geckolib.util.GeckoLibUtil;
import dev.xylonity.knightquest.registry.KnightQuestBlockEntities;
import dev.xylonity.knightquest.registry.KnightQuestParticles;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * Port of KnightLib's {@code GreatChaliceBlockEntity}: holds the essence charges and the
 * chalice state (EMPTY/NORMAL/CHAOTIC), ticks ambient particles and drives the geo model
 * (the "liquid" bone is raised by the renderer according to the charges).
 */
public class GreatChaliceBlockEntity extends BlockEntity implements GeoBlockEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private int charges;
    private int prevCharges;
    private GreatChaliceState state;
    private long tickcount;

    public GreatChaliceBlockEntity(BlockPos pos, BlockState state) {
        super(KnightQuestBlockEntities.GREAT_CHALICE.get(), pos, state);
        this.state = GreatChaliceState.EMPTY;
        this.charges = 0;
        this.prevCharges = 0;
        this.tickcount = 0;
    }

    public int getCharges() {
        return this.charges;
    }

    public void setCharges(int charges) {
        this.charges = Math.min(charges, IGreatChaliceInteractable.MAX_CHARGES);

        if (this.charges == 0) setState(GreatChaliceState.EMPTY);

        this.prevCharges = this.charges;
        setChanged();
        sync();
    }

    public GreatChaliceState getState() {
        return this.state;
    }

    public boolean isFull() {
        return this.getCharges() == IGreatChaliceInteractable.MAX_CHARGES;
    }

    public boolean isChaotic() {
        return this.state == GreatChaliceState.CHAOTIC;
    }

    public void setState(GreatChaliceState newState) {
        if (this.state == newState) return;
        this.state = newState;
        setChanged();
        sync();
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("Charges", this.charges);
        output.putInt("PrevCharges", this.prevCharges);
        output.putString("State", this.state.getSerializedName());
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.charges = input.getIntOr("Charges", 0);
        this.prevCharges = input.getIntOr("PrevCharges", 0);
        this.state = GreatChaliceState.byName(input.getStringOr("State", "empty"));
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T be) {
        if (!(be instanceof GreatChaliceBlockEntity chalice)) {
            return;
        }

        if (chalice.charges == IGreatChaliceInteractable.MAX_CHARGES && chalice.tickcount % 15 == 0) {
            chalice.spawnSpecialParticles();
        }

        if (chalice.charges == IGreatChaliceInteractable.MAX_CHARGES && chalice.tickcount % 5 == 0) {
            chalice.spawnChaoticParticles();
        }

        if (chalice.tickcount % 5 == 0) {
            chalice.spawnProgressiveParticles(pos);
        }

        chalice.tickcount++;
    }

    private void spawnSpecialParticles() {
        if (level instanceof ServerLevel sv && getState() == GreatChaliceState.NORMAL) {
            for (int i = 0; i < 2; i++) {
                double dx = (Math.random() - 0.5) * 0.5;
                double dy = (Math.random() - 0.5) * 0.5;
                double dz = (Math.random() - 0.5) * 0.5;
                if (i % 3 == 0) {
                    sv.sendParticles(KnightQuestParticles.STARSET.get(),
                            this.getBlockPos().getX() + 0.5,
                            this.getBlockPos().getY() + 0.5 * Math.random(),
                            this.getBlockPos().getZ() + 0.5,
                            1, dx, dy, dz, 0.35);
                }
            }
        }
    }

    private void spawnChaoticParticles() {
        if (level instanceof ServerLevel sv && getState() == GreatChaliceState.CHAOTIC) {
            for (int i = 0; i < 2; i++) {
                double dx = (Math.random() - 0.5) * 0.5;
                double dy = (Math.random() - 0.5) * 0.5;
                double dz = (Math.random() - 0.5) * 0.5;
                sv.sendParticles(ParticleTypes.WARPED_SPORE,
                        this.getBlockPos().getX() + 0.5,
                        this.getBlockPos().getY() + 0.5 * Math.random(),
                        this.getBlockPos().getZ() + 0.5,
                        1, dx, dy, dz, 0.3);
            }
            for (int i = 0; i < 2; i++) {
                double dx = (Math.random() - 0.5) * 0.5;
                double dy = (Math.random() - 0.5) * 0.5;
                double dz = (Math.random() - 0.5) * 0.5;
                sv.sendParticles(ParticleTypes.CRIMSON_SPORE,
                        this.getBlockPos().getX() + 0.5,
                        this.getBlockPos().getY() + 0.5 * Math.random(),
                        this.getBlockPos().getZ() + 0.5,
                        1, dx, dy, dz, 0.25);
            }
        }
    }

    private void spawnProgressiveParticles(BlockPos pos) {
        if (level instanceof ServerLevel sv) {
            for (int i = 0; i < this.charges; i++) {
                if (sv.getRandom().nextFloat() < 0.10f) {
                    sv.sendParticles(net.minecraft.core.particles.SpellParticleOption.create(ParticleTypes.EFFECT, -1, 1.0F),
                            pos.getX() + 0.5 + (sv.getRandom().nextDouble() - 0.5) * 0.9,
                            pos.getY() + ((double) this.getCharges() / 12) + sv.getRandom().nextDouble() * 0.5,
                            pos.getZ() + 0.5 + (sv.getRandom().nextDouble() - 0.5) * 0.9,
                            1, 0.0, 0.1, 0.0, 0.0);
                }
            }
        }
    }

    public void sync() {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.getChunkSource().chunkMap.getPlayers(new ChunkPos(this.worldPosition.getX() >> 4, this.worldPosition.getZ() >> 4), false)
                    .forEach(p -> p.connection.send(net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this)));
        }
    }

    @Override
    public net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket getUpdatePacket() {
        return net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public net.minecraft.nbt.CompoundTag getUpdateTag(net.minecraft.core.HolderLookup.Provider registries) {
        net.minecraft.nbt.CompoundTag tag = super.getUpdateTag(registries);
        tag.putInt("Charges", this.charges);
        tag.putInt("PrevCharges", this.prevCharges);
        tag.putString("State", this.state.getSerializedName());
        return tag;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("main", 0, state ->
                state.setAndContinue(RawAnimation.begin().thenLoop("idle"))));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

}
