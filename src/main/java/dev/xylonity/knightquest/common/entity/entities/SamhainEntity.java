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
import dev.xylonity.knightquest.common.entity.entities.ai.MoveToPumpkinGoal;
import dev.xylonity.knightquest.common.entity.entities.ai.RangedAttackGoal;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.*;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class SamhainEntity extends TamableAnimal implements GeoEntity, RangedAttackMob {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(SamhainEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SIT_VARIATION = SynchedEntityData.defineId(SamhainEntity.class, EntityDataSerializers.INT);

    public SamhainEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setCanPickUpLoot(true);
    }


    public static AttributeSupplier.Builder setAttributes() {
        return TamableAnimal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0D)
                .add(Attributes.ATTACK_DAMAGE, 0.5f)
                .add(Attributes.ATTACK_SPEED, 2.0f)
                .add(Attributes.MOVEMENT_SPEED, 0.5f);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new RangedAttackGoal<>(this, 0.7D, 10, 15.0f));
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 0.6D, true));
        this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 0.6D, 6.0F, 2.0F));
        this.goalSelector.addGoal(5, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(7, new MoveToPumpkinGoal(this, 0.68F));

        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>("controller", 0, this::predicate));
        controllerRegistrar.add(new AnimationController<>("attackcontroller", 0, this::attackPredicate));
    }

    private PlayState attackPredicate(AnimationTest<SamhainEntity> event) {

        if (this.isUsingItem() && this.getMainHandItem().getItem() instanceof ProjectileWeaponItem) {
            event.controller().setAnimation(RawAnimation.begin().thenPlay("bow_attack"));
        } else if (this.swinging && event.controller().hasAnimationFinished()) {
            event.setAndContinue(RawAnimation.begin().thenPlay("attack"));
            this.swinging = false;
        }

        return PlayState.CONTINUE;
    }

    private PlayState predicate(AnimationTest<SamhainEntity> event) {
        if (this.isSitting()) {
            String sitVariation = getSitVariation() == 0 ? "sit" : getSitVariation() == 1 ? "sit3" : "sit2";
            event.setAndContinue(RawAnimation.begin().thenLoop(sitVariation));
        } else if (event.isMoving()) {
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
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand hand) {
        ItemStack itemstack = player.getItemInHand(hand);
        Item itemForTaming = KnightQuestCommon.COMMON_PLATFORM.getGreatEssence().get();
        Item item = itemstack.getItem();

        /*
         * Consumes a single Great Essence item and tames the Samhain.
         */

        if (item == itemForTaming && !isTame()) {
            if (this.level().isClientSide()) {
                return InteractionResult.CONSUME;
            } else {
                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

                if (!this.level().isClientSide()) {
                    super.tame(player);
                    this.navigation.recomputePath();
                    this.setTarget(null);
                    this.level().broadcastEntityEvent(this, (byte)7);
                    setSitting(true);
                }

                setSitVariation(getRandom().nextInt(0, 3));

                return InteractionResult.SUCCESS;
            }
        }

        /*
         * Handles the actions when the Samhain is tamed.
         */

        if (isTame() && !this.level().isClientSide() && hand == InteractionHand.MAIN_HAND && getOwner() == player) {
            if ((itemstack.getItem().equals(KnightQuestCommon.COMMON_PLATFORM.getGreatEssence().get()) || itemstack.getItem().equals(KnightQuestCommon.COMMON_PLATFORM.getSmallEssence().get()))
                    && this.getHealth() < this.getMaxHealth()) {

                if (itemstack.getItem().equals(KnightQuestCommon.COMMON_PLATFORM.getGreatEssence().get())) {
                    this.heal(16.0F);
                } else if (itemstack.getItem().equals(KnightQuestCommon.COMMON_PLATFORM.getSmallEssence().get())) {
                    this.heal(4.0F);
                }

                if (!player.getAbilities().instabuild) {
                    itemstack.shrink(1);
                }

            } else if (player.isShiftKeyDown()) {
                removeArmor(player);
                removeItem(player);
            } else {
                setSitting(!isSitting());
                setSitVariation(getRandom().nextInt(0, 3));
            }

            return InteractionResult.SUCCESS;
        }

        if (itemstack.getItem() == itemForTaming) {
            return InteractionResult.PASS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    protected void pickUpItem(ServerLevel level, ItemEntity itemEntity) {
        ItemStack itemStack = itemEntity.getItem();
        Item item = itemStack.getItem();

        if ((item instanceof AxeItem || item instanceof ProjectileWeaponItem) && isTame()) {
            EquipmentSlot slot = this.getEquipmentSlotForItem(itemStack);
            ItemStack currentItem = this.getItemInHand(InteractionHand.MAIN_HAND);

            if (currentItem.isEmpty()) {
                this.setItemSlot(slot, itemStack.split(1));

                if (itemStack.isEmpty()) {
                    itemEntity.discard();
                }
            }
        } else {
            Equippable equippable = itemStack.get(DataComponents.EQUIPPABLE);
            if (equippable != null && equippable.slot().isArmor() && isTame()) {
                EquipmentSlot slot = equippable.slot();
                ItemStack currentArmor = this.getItemBySlot(slot);

                if (currentArmor.isEmpty()) {
                    this.setItemSlot(slot, itemStack.split(1));

                    if (itemStack.isEmpty()) {
                        itemEntity.discard();
                    }
                }
            }
        }

    }

    @Override
    public void readAdditionalSaveData(@NotNull ValueInput input) {
        super.readAdditionalSaveData(input);

        // GeckoLib-independent: owner + sitting are persisted natively by TamableAnimal
        setSitting(this.isOrderedToSit());
        setSitVariation(input.getIntOr("sitVariation", 0));

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                input.read(slot.getName(), ItemStack.CODEC).ifPresent(stack -> {
                    if (!stack.isEmpty()) {
                        this.setItemSlot(slot, stack);
                    }
                });
            }
        }

    }

    @Override
    public boolean isFood(@NotNull ItemStack itemStack) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(@NotNull ValueOutput output) {
        super.addAdditionalSaveData(output);

        output.putInt("sitVariation", this.getSitVariation());

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                ItemStack stack = this.getItemBySlot(slot);
                if (!stack.isEmpty()) {
                    output.store(slot.getName(), ItemStack.CODEC, stack);
                }
            }
        }

    }

    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder pBuilder) {
        super.defineSynchedData(pBuilder);
        pBuilder.define(SITTING, false);
        pBuilder.define(SIT_VARIATION, 0);
    }

    private void setSitVariation(int sitVariation) {
        this.entityData.set(SIT_VARIATION, sitVariation);
    }

    private int getSitVariation() {
        return this.entityData.get(SIT_VARIATION);
    }

    private void removeArmor(Player pPlayer) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() == EquipmentSlot.Type.HUMANOID_ARMOR) {
                ItemStack armorStack = this.getItemBySlot(slot);

                if (!armorStack.isEmpty()) {
                    boolean addedToInventory = pPlayer.getInventory().add(armorStack);

                    if (!addedToInventory) {
                        pPlayer.spawnAtLocation((ServerLevel) pPlayer.level(), armorStack);
                    }

                    this.setItemSlot(slot, ItemStack.EMPTY);
                }
            }
        }
    }

    private void removeItem(Player pPlayer) {
        ItemStack itemStack = this.getItemInHand(InteractionHand.MAIN_HAND);

        if (!itemStack.isEmpty()) {
            boolean addedToInventory = pPlayer.getInventory().add(itemStack);

            if (!addedToInventory) {
                pPlayer.spawnAtLocation((ServerLevel) pPlayer.level(), itemStack);
            }

            this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
        }
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
        this.setOrderedToSit(sitting);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    @Override
    public void setTame(boolean pTame, boolean pApplyTamingSideEffects) {
        super.setTame(pTame, pApplyTamingSideEffects);
        if (pTame) {
            Objects.requireNonNull(getAttribute(Attributes.MAX_HEALTH)).setBaseValue(20.0D);
            Objects.requireNonNull(getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(0.5D);
            Objects.requireNonNull(getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.5f);
            this.setHealth(20.0F);
        } else {
            Objects.requireNonNull(getAttribute(Attributes.MAX_HEALTH)).setBaseValue(8.0D);
            Objects.requireNonNull(getAttribute(Attributes.ATTACK_DAMAGE)).setBaseValue(0.5D);
            Objects.requireNonNull(getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.5f);
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return null;
    }

    @Override
    protected @NotNull SoundEvent getSwimSound() {
        return SoundEvents.AXOLOTL_SWIM;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ALLAY_DEATH;
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return SoundEvents.ALLAY_HURT;
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        this.playSound(SoundEvents.WOLF_STEP.value(), 0.15F, 1.0F);
    }

    protected AbstractArrow createArrowProjectile(ItemStack arrowStack, float pullProgress) {
        return ProjectileUtil.getMobArrow(this, arrowStack, pullProgress, this.getMainHandItem());
    }

    @Override
    public void performRangedAttack(@NotNull LivingEntity pTarget, float pVelocity) {
        ItemStack itemstack = this.getProjectile(this.getMainHandItem());
        AbstractArrow abstractarrow = this.getArrow(itemstack, pVelocity);
        if (this.getMainHandItem().getItem() instanceof ProjectileWeaponItem)
            abstractarrow = this.createArrowProjectile(itemstack, pVelocity);
        double d0 = pTarget.getX() - this.getX();
        double d1 = pTarget.getY(0.34D) - abstractarrow.getY();
        double d2 = pTarget.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);
        abstractarrow.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(abstractarrow);
    }

    protected AbstractArrow getArrow(ItemStack pArrowStack, float pVelocity) {
        return ProjectileUtil.getMobArrow(this, pArrowStack, pVelocity, this.getMainHandItem());
    }
}
