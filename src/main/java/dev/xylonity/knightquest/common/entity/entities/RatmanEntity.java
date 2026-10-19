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
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.golem.IronGolem;
import net.minecraft.world.entity.animal.turtle.Turtle;
import net.minecraft.world.entity.monster.skeleton.Skeleton;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Random;

public class RatmanEntity extends Skeleton implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Level serverWorld;
    private boolean summoned = false;
    private int counter = 0;
    private int arrowRotation = 50;
    private static final EntityDataAccessor<Boolean> ATTACK1 = SynchedEntityData.defineId(RatmanEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> VARIATION = SynchedEntityData.defineId(RatmanEntity.class, EntityDataSerializers.INT);

    public RatmanEntity(EntityType<? extends Skeleton> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        serverWorld = pLevel;
        if (!serverWorld.isClientSide()) {
            boolean attack1 = new Random().nextBoolean();
            this.entityData.set(ATTACK1, attack1);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(ATTACK1, false);
        pBuilder.define(VARIATION, 1);
    }

    @Override
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);
        output.putBoolean("attack1", this.getAttack1());
        output.putInt("Variant", this.getVariation());
    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);
        this.setAttack1(input.getBooleanOr("attack1", false));
        this.entityData.set(VARIATION, input.getIntOr("Variant", 1));
    }

    public boolean getAttack1() {
        return this.entityData.get(ATTACK1);
    }
    public int getVariation() {
        return this.entityData.get(VARIATION);
    }

    public void setAttack1(boolean attack1) {
        this.entityData.set(ATTACK1, attack1);
    }
    public void setVariation(int variation) {
        this.entityData.set(VARIATION, variation);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Skeleton.createAttributes()
                .add(Attributes.MAX_HEALTH, 22.0D)
                .add(Attributes.ATTACK_DAMAGE, 4f)
                .add(Attributes.ATTACK_SPEED, 1.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.5f);
    }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(1, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(2, new RangedBowAttackGoal<>(this, 0.7D, 10, 15.0f));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>("controller", 0, this::predicate));
        controllerRegistrar.add(new AnimationController<>("attackcontroller", 0, this::attackPredicate));
    }

    private PlayState attackPredicate(AnimationTest<RatmanEntity> event) {

        if (isUsingItem()) {
            event.controller().setAnimation(RawAnimation.begin().thenPlay("attack"));
        }

        return PlayState.CONTINUE;
    }

    private PlayState predicate(AnimationTest<RatmanEntity> event) {

        if (event.isMoving()) {
            event.setAndContinue(RawAnimation.begin().thenLoop("walk"));
        } else {
            event.setAndContinue(RawAnimation.begin().thenLoop("idle"));
        }

        return PlayState.CONTINUE;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected SoundEvent getSwimSound() {
        return SoundEvents.AXOLOTL_SWIM;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.FOX_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return SoundEvents.FOX_HURT;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.FOX_AMBIENT;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        this.playSound(SoundEvents.WOLF_STEP.value(), 0.15F, 1.0F);
    }

    @Override
    public void tick() {
        super.tick();
        if (getAttack1()) {
            if (!summoned && this.getHealth() < this.getMaxHealth() * 0.5) {

                if (counter < 10 && counter % 2 == 0) {
                    for (int i = 0; i < 2; i++) {
                        shootArrow(arrowRotation += (int) (360f / 10));
                    }
                }
                counter++;
                if (counter >= 10) {
                    summoned = true;
                }
            }
        } else {
            if (!summoned && this.getHealth() < this.getMaxHealth() * 0.5) {
                smokeBomb();
                summoned = true;
            }

        }
    }

    private void shootArrow(int angle) {

        double arrowX = Math.cos(Math.toRadians(angle));
        double arrowY = getEyeY();
        double arrowZ = Math.sin(Math.toRadians(angle));

        Arrow arrow = new Arrow(serverWorld, this, new ItemStack(Items.ARROW), new ItemStack(Items.CROSSBOW));
        arrow.setPos(getX() + arrowX, arrowY - 1.5, getZ() + arrowZ);

        double velX = Math.cos(Math.toRadians(angle));
        double velZ = Math.sin(Math.toRadians(angle));
        arrow.setDeltaMovement(velX, 0.3, velZ);

        switch (getVariation()) {
            case 1 -> arrow.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0));
            case 2 -> arrow.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));
            case 3 -> arrow.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0));
            default -> arrow.igniteForSeconds(10);
        }

        serverWorld.playSound(null, this.blockPosition(), SoundEvents.DISPENSER_LAUNCH, SoundSource.HOSTILE, 0.75F, 1.0F);

        serverWorld.addFreshEntity(arrow);

    }

    private void smokeBomb() {

        double radius = 2.5;
        serverWorld.playSound(null, this.blockPosition(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.HOSTILE, 1.0F, 1.0F);

        for (int i = 0; i < 450; i++) {
            double u = random.nextDouble();
            double v = random.nextDouble();
            double theta = 2 * Math.PI * u;
            double phi = Math.acos(2 * v - 1);

            double scale = Math.cbrt(random.nextDouble());

            double x = getX() + (radius * scale * Math.sin(phi) * Math.cos(theta));
            double y = getY() + (radius * scale * Math.sin(phi) * Math.sin(theta));
            double z = getZ() + (radius * scale * Math.cos(phi));

            serverWorld.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, x, y, z, 0, 0.0, 0.0);
        }

        this.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 100, 1));

    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, EntitySpawnReason pSpawnType, @Nullable SpawnGroupData pSpawnGroupData) {
        setVariation(new Random().nextInt(4) + 1);

        AttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.setBaseValue(KQConfigValues.RATMAN_MAX_HEALTH.get());
            this.setHealth(KQConfigValues.RATMAN_MAX_HEALTH.get().floatValue());
        }

        return super.finalizeSpawn(pLevel, pDifficulty, pSpawnType, pSpawnGroupData);
    }

}
