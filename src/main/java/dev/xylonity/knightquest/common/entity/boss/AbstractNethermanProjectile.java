package dev.xylonity.knightquest.common.entity.boss;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AbstractNethermanProjectile extends Projectile {
    public double xPower;
    public double yPower;
    public double zPower;

    protected AbstractNethermanProjectile(EntityType<? extends AbstractNethermanProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    /**
     * Checks if the entity is in range to render.
     */

    public boolean shouldRenderAtSqrDistance(double distance) {
        double size = this.getBoundingBox().getSize() * 4.0D;
        size = Double.isNaN(size) ? 4.0D : size * 64.0D;
        return distance < size * size;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {

    }

    /**
     * Called to update the entity's position/logic.
     */

    public void tick() {
        Entity entity = this.getOwner();
        if (this.level().isClientSide() || (entity == null || !entity.isRemoved()) && this.level().hasChunkAt(this.blockPosition())) {
            super.tick();

            HitResult hitresult = ProjectileUtil.getHitResultOnMoveVector(this, this::canHitEntity);
            if (hitresult.getType() != HitResult.Type.MISS) {
                this.onHit(hitresult);
            }

            Vec3 vec3 = this.getDeltaMovement();
            double d0 = this.getX() + vec3.x;
            double d1 = this.getY() + vec3.y;
            double d2 = this.getZ() + vec3.z;
            ProjectileUtil.rotateTowardsMovement(this, 0.2F);
            float f = this.getInertia();
            if (this.isInWater()) {
                for(int i = 0; i < 4; ++i) {
                    this.level().addParticle(ParticleTypes.BUBBLE, d0 - vec3.x * 0.25D, d1 - vec3.y * 0.25D, d2 - vec3.z * 0.25D, vec3.x, vec3.y, vec3.z);
                }

                f = 0.8F;
            }

            this.setDeltaMovement(vec3.add(this.xPower, this.yPower, this.zPower).scale(f));
            this.level().addParticle(this.getTrailParticle(), d0, d1 + 0.5D, d2, 0.0D, 0.0D, 0.0D);
            this.setPos(d0, d1, d2);
        } else {
            this.discard();
        }
    }

    protected boolean canHitEntity(@NotNull Entity entity) {
        return super.canHitEntity(entity) && !entity.noPhysics;
    }

    protected ParticleOptions getTrailParticle() {
        return ParticleTypes.SMOKE;
    }

    /**
     * Return the motion factor for this projectile. The factor is multiplied by the original motion.
     */

    protected float getInertia() {
        return 0.95F;
    }

    public void addAdditionalSaveData(@NotNull ValueOutput pOutput) {
        super.addAdditionalSaveData(pOutput);
        pOutput.store("power", Codec.DOUBLE.listOf(), List.of(this.xPower, this.yPower, this.zPower));
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */

    public void readAdditionalSaveData(@NotNull ValueInput pInput) {
        super.readAdditionalSaveData(pInput);
        pInput.read("power", Codec.DOUBLE.listOf()).ifPresent(list -> {
            if (list.size() == 3) {
                this.xPower = list.get(0);
                this.yPower = list.get(1);
                this.zPower = list.get(2);
            }
        });
    }

    /**
     * Returns {@code true} if other Entities should be prevented from moving through this Entity.
     */

    public boolean isPickable() {
        return true;
    }

    public float getPickRadius() {
        return 1.0F;
    }

    /**
     * Called when the entity is attacked.
     */

    public boolean hurtServer(@NotNull ServerLevel pLevel, @NotNull DamageSource pSource, float pAmount) {
        if (this.isInvulnerableToBase(pSource)) {
            return false;
        } else {
            this.markHurt();
            Entity entity = pSource.getEntity();
            if (entity != null) {
                if (!this.level().isClientSide()) {
                    Vec3 vec3 = entity.getLookAngle();
                    this.setDeltaMovement(vec3);
                    this.xPower = vec3.x * 0.1D;
                    this.yPower = vec3.y * 0.1D;
                    this.zPower = vec3.z * 0.1D;
                    this.setOwner(entity);
                }

                return true;
            } else {
                return false;
            }
        }
    }

    public float getLightLevelDependentMagicValue() {
        return 1.0F;
    }

    public @NotNull ClientboundAddEntityPacket getAddEntityPacket() {
        Entity entity = this.getOwner();
        int i = entity == null ? 0 : entity.getId();
        return new ClientboundAddEntityPacket(this.getId(), this.getUUID(), this.getX(), this.getY(), this.getZ(), this.getXRot(), this.getYRot(), this.getType(), i, new Vec3(this.xPower, this.yPower, this.zPower), 0.0D);
    }

    public void recreateFromPacket(@NotNull ClientboundAddEntityPacket pPacket) {
        super.recreateFromPacket(pPacket);
        double d0 = pPacket.getMovement().x;
        double d1 = pPacket.getMovement().y;
        double d2 = pPacket.getMovement().z;
        double d3 = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
        if (d3 != 0.0D) {
            this.xPower = d0 / d3 * 0.1D;
            this.yPower = d1 / d3 * 0.1D;
            this.zPower = d2 / d3 * 0.1D;
        }

    }
}
