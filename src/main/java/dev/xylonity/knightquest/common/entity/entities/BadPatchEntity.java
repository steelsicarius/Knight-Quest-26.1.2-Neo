package dev.xylonity.knightquest.common.entity.entities;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.state.AnimationTest;
import com.geckolib.util.GeckoLibUtil;
import dev.xylonity.knightquest.config.values.KQConfigValues;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BadPatchEntity extends Monster implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public BadPatchEntity(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 7.0D)
                .add(Attributes.ATTACK_DAMAGE, 4f)
                .add(Attributes.ATTACK_SPEED, 0.2f)
                .add(Attributes.MOVEMENT_SPEED, 0.5f)
                .add(Attributes.FOLLOW_RANGE, 35.0)
                // 26.x: TemptGoal reads the tempt_range attribute; no longer implied by createMobAttributes
                .add(Attributes.TEMPT_RANGE, 10.0);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, EntitySpawnReason pSpawnType, @Nullable SpawnGroupData pSpawnGroupData) {

        AttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.setBaseValue(KQConfigValues.BADPATCH_MAX_HEALTH.get());
            this.setHealth(KQConfigValues.BADPATCH_MAX_HEALTH.get().floatValue());
        }

        return super.finalizeSpawn(pLevel, pDifficulty, pSpawnType, pSpawnGroupData);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.5f, true));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new TemptGoal(this, 0.5f, Ingredient.of(new ItemLike[]{Items.WHEAT}), false));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.5f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>("controller", 0, this::predicate));
        controllerRegistrar.add(new AnimationController<>("attackcontroller", 0, this::attackPredicate));
    }

    private PlayState attackPredicate(AnimationTest<BadPatchEntity> event) {

        // GeckoLib 5: no controller animation-state enum / forceAnimationReset;
        // restart the one-shot attack animation whenever the swing flag raises.
        if (this.swinging && event.controller().hasAnimationFinished()) {
            event.setAndContinue(RawAnimation.begin().thenPlay("attack"));
            this.swinging = false;
        }

        return PlayState.CONTINUE;
    }

    private PlayState predicate(AnimationTest<BadPatchEntity> event) {

        if (event.isMoving()) {
            event.setAndContinue(RawAnimation.begin().thenLoop("walk"));
        } else {
            event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }

        if (this.isDeadOrDying()) {
            event.setAndContinue(RawAnimation.begin().thenLoop("death"));
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected @NotNull SoundEvent getSwimSound() {
        return SoundEvents.AXOLOTL_SWIM;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.DONKEY_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return SoundEvents.WARDEN_HURT;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        this.playSound(SoundEvents.WOLF_STEP.value(), 0.15F, 1.0F);
    }

}
