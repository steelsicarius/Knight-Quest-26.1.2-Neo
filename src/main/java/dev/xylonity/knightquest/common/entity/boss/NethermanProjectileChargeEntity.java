package dev.xylonity.knightquest.common.entity.boss;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.state.AnimationTest;
import com.geckolib.util.GeckoLibUtil;
import dev.xylonity.knightquest.config.values.KQConfigValues;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class NethermanProjectileChargeEntity extends AbstractNethermanProjectile implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private int explosionTimer;
    private boolean shouldGoDown;

    public NethermanProjectileChargeEntity(EntityType<? extends AbstractNethermanProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setNoGravity(true);
        this.explosionTimer = this.random.nextInt(100) + 20;
        if (!level().isClientSide()) this.shouldGoDown = this.random.nextBoolean();
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide()) {
            this.explosionTimer--;

            if (this.explosionTimer <= 0) {
                this.explode();
            }
        }

        if (shouldGoDown && !level().isClientSide()) this.setDeltaMovement(this.getDeltaMovement().add(0.0, -0.05, 0.0));
    }

    @Override
    protected void onHitBlock(@NotNull BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if (!level().isClientSide()) explode();
    }

    private void explode() {
        this.level().explode(this, this.getX(), this.getY(), this.getZ(), KQConfigValues.NETHERMAN_PROJECTILE_EXPLOSION_RADIUS.get().floatValue(), Level.ExplosionInteraction.MOB);
        this.discard();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("rotateController", 0, this::rotatePredicate));
        controllers.add(new AnimationController<>("coreController", 0, this::corePredicate));
    }

    private PlayState corePredicate(AnimationTest<NethermanProjectileChargeEntity> event) {

        if (tickCount < 10)
            event.setAndContinue(RawAnimation.begin().thenPlay("summon"));
        else if (explosionTimer < 11)
            event.setAndContinue(RawAnimation.begin().thenPlay("die"));

        return PlayState.CONTINUE;
    }

    private PlayState rotatePredicate(AnimationTest<NethermanProjectileChargeEntity> event) {

        event.setAndContinue(RawAnimation.begin().thenLoop("rotate"));

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

}
