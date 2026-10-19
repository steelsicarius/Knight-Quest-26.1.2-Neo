package dev.xylonity.knightquest.common.entity.entities;

import com.geckolib.animatable.GeoEntity;
import com.geckolib.animatable.instance.AnimatableInstanceCache;
import com.geckolib.animatable.manager.AnimatableManager;
import com.geckolib.animation.AnimationController;
import com.geckolib.animation.object.PlayState;
import com.geckolib.animation.RawAnimation;
import com.geckolib.animation.state.AnimationTest;
import com.geckolib.util.GeckoLibUtil;
import dev.xylonity.knightquest.KnightQuestCommon;
import dev.xylonity.knightquest.config.values.KQConfigValues;
import dev.xylonity.knightquest.registry.KnightQuestEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SpellParticleOption;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class EldKnightEntity extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final Level serverWorld;
    private boolean summoned = false;
    private int counter;

    public EldKnightEntity(EntityType<? extends Monster> entityType, Level world) {
        super(entityType, world);
        serverWorld = world;
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Monster.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 90.0D)
                .add(Attributes.ATTACK_DAMAGE, 12f)
                .add(Attributes.ATTACK_SPEED, 0.4f)
                .add(Attributes.MOVEMENT_SPEED, 0.55f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8f);
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, EntitySpawnReason pSpawnType, @Nullable SpawnGroupData pSpawnGroupData) {

        AttributeInstance maxHealth = this.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.setBaseValue(KQConfigValues.ELDKNIGHT_MAX_HEALTH.get());
            this.setHealth(KQConfigValues.ELDKNIGHT_MAX_HEALTH.get().floatValue());
        }

        return super.finalizeSpawn(pLevel, pDifficulty, pSpawnType, pSpawnGroupData);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 0.45D, false));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 6.0F));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>("controller", 0, this::predicate));
        controllerRegistrar.add(new AnimationController<>("attackcontroller", 0, this::attackPredicate));
    }

    private PlayState attackPredicate(AnimationTest<EldKnightEntity> event) {

        // GeckoLib 5: no controller animation-state enum / forceAnimationReset;
        // restart the one-shot attack animation whenever the swing flag raises.
        if (this.swinging && event.controller().hasAnimationFinished()) {
            event.setAndContinue(RawAnimation.begin().thenPlay("attack"));
            this.swinging = false;
        }

        return PlayState.CONTINUE;
    }

    private PlayState predicate(AnimationTest<EldKnightEntity> event) {

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
    protected @NotNull SoundEvent getSwimSound() {
        return SoundEvents.AXOLOTL_SWIM;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        this.playSound(SoundEvents.IRON_GOLEM_STEP, 0.15F, 1.0F);
    }

    /**
     * Manages actions when the mob's health drops below half of its maximum:
     * generates minions, pushes nearby players away, and applies poison to players in the vicinity,
     * visually enhanced with `starset` particles.
     */

    @Override
    public void tick() {
        super.tick();
        counter++;
        if (!summoned && this.getHealth() < this.getMaxHealth() * 0.5) {
            summoned = true;
            summonMinions();

        }

        if (summoned && counter > 80) {

            if (KQConfigValues.POISON_ELDKNIGHT.get()) {
                summonParticle();
                poisonNearbyPlayers();
            }

            if (this.getHealth() < this.getMaxHealth() * 0.75) {
                this.heal(KQConfigValues.HEAL_ELDKNIGHT.get().floatValue());
            }

            counter = 0;
        }
    }

    @Override
    public boolean hurtServer(ServerLevel pLevel, DamageSource pSource, float pAmount) {

        if (pSource.is(DamageTypes.PLAYER_EXPLOSION) || pSource.is(DamageTypes.EXPLOSION)) {
            return super.hurtServer(pLevel, pSource, pAmount * 0.15f);
        }

        if (pSource.is(DamageTypes.ARROW)) {
            return super.hurtServer(pLevel, pSource, pAmount * 0.10f);
        }

        return super.hurtServer(pLevel, pSource, pAmount);
    }

    /**
     * Manages the second phase of the mob by summoning `NUM_ELDBOMB_ELDKNIGHT` minions
     * as defined in the common config file value, and pushes players nearby away.
     */

    private void summonMinions() {
        double distance = 3.0;
        double angle = KQConfigValues.HEAL_ELDKNIGHT.get().floatValue() != 0 ? Math.toRadians((double) 360 / KQConfigValues.NUM_ELDBOMB_ELDKNIGHT.get()) : Math.toRadians(120);
        boolean punch = false;

        for (int i = 0; i < KQConfigValues.NUM_ELDBOMB_ELDKNIGHT.get(); i++) {
            double xOffset = distance * Math.cos(angle * i);
            double zOffset = distance * Math.sin(angle * i);
            EldBombEntity entity = KnightQuestEntities.ELDBOMB.get().create(serverWorld, EntitySpawnReason.MOB_SUMMONED);
            if (entity != null) {
                BlockPos spawnPos = this.blockPosition().offset((int) xOffset, 0, (int) zOffset);

                if (!serverWorld.getBlockState(spawnPos).isAir()) {
                    for (int d = 1; d <= distance; d++) {
                        BlockPos adjustedPos = this.blockPosition().offset((int) (xOffset / d), 0, (int) (zOffset / d));
                        if (serverWorld.getBlockState(adjustedPos).isAir()) {
                            spawnPos = adjustedPos;
                            break;
                        }
                    }
                }

                entity.snapTo(spawnPos, 0.0F, 0.0F);
                serverWorld.addFreshEntity(entity);

                for (int j = 0; j < 20; j++) {
                    serverWorld.addParticle(SpellParticleOption.create(ParticleTypes.EFFECT, -1, 1.0F), spawnPos.getX() + 0.5 + serverWorld.getRandom().nextDouble() - 0.5,
                            spawnPos.getY() + 0.5 + serverWorld.getRandom().nextDouble() - 0.5,
                            spawnPos.getZ() + 0.5 + serverWorld.getRandom().nextDouble() - 0.5, 0, 0, 0);
                }

                if (!punch) {
                    serverWorld.getEntitiesOfClass(Player.class, entity.getBoundingBox().inflate(5)).forEach(player -> {
                        Vec3 direction = player.position().subtract(this.position()).normalize().scale(1.5);
                        player.push(direction.x, direction.y + 0.5, direction.z);
                    });
                    punch = true;
                }

            }

        }

        int particleCount = 120;
        double particleRadius = 4;

        for (int i = 0; i < particleCount; i++) {
            double angleOffset = (2 * Math.PI / particleCount) * i;
            double xParticleOffset = particleRadius * Math.cos(angleOffset);
            double zParticleOffset = particleRadius * Math.sin(angleOffset);
            serverWorld.addParticle(ParticleTypes.CLOUD, this.getX() + xParticleOffset, this.getY() - 0.1, this.getZ() + zParticleOffset, 0.0, 0.05, 0.0);
        }

        serverWorld.playSound(null, this.blockPosition(), SoundEvents.EVOKER_PREPARE_SUMMON, SoundSource.BLOCKS, 1f, 1f);
    }

    private void poisonNearbyPlayers() {
        this.serverWorld.getEntitiesOfClass(Player.class, this.getBoundingBox().inflate(3.5)).forEach(player ->
                player.addEffect(new MobEffectInstance(MobEffects.POISON, 200, 1)));
    }

    private void summonParticle() {
        serverWorld.addParticle(KnightQuestCommon.COMMON_PLATFORM.getStartsetParticle().get(), this.getX(), getY() - 0.48, getZ(), 4d, 0d, 0d);
    }

}
