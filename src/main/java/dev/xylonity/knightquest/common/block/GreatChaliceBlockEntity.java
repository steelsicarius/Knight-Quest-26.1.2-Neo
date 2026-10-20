package dev.xylonity.knightquest.common.block;

import dev.xylonity.knightquest.registry.KnightQuestBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

/**
 * Port of KnightLib's Great Chalice block entity (charges + chaotic state).
 * The chalice holds up to {@link #MAX_CHARGES} charges; a full chalice can be
 * attuned with chaotic essence (to activate weapons) or drained by a radiant
 * essence (summoning the Netherman) or empty goblets (gathering essence).
 */
public class GreatChaliceBlockEntity extends BlockEntity {

    public static final int MAX_CHARGES = 4;

    private int charges;
    private boolean chaotic;

    public GreatChaliceBlockEntity(BlockPos pos, BlockState blockState) {
        super(KnightQuestBlockEntities.GREAT_CHALICE.get(), pos, blockState);
    }

    public int getCharges() {
        return this.charges;
    }

    public boolean isFull() {
        return this.charges >= MAX_CHARGES;
    }

    public boolean isChaotic() {
        return this.chaotic;
    }

    public void setCharges(int charges) {
        this.charges = Math.max(0, Math.min(MAX_CHARGES, charges));
        setChanged();
        sync();
    }

    public void addCharges(int amount) {
        setCharges(this.charges + amount);
    }

    public void setChaotic(boolean chaotic) {
        this.chaotic = chaotic;
        setChanged();
        sync();
    }

    private void sync() {
        if (this.level != null) {
            this.level.sendBlockUpdated(this.worldPosition, this.getBlockState(), this.getBlockState(), 3);
        }
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        output.putInt("Charges", this.charges);
        output.putBoolean("Chaotic", this.chaotic);
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        this.charges = input.getIntOr("Charges", 0);
        this.chaotic = input.getBooleanOr("Chaotic", false);
    }

}
