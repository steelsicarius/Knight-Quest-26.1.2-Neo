package dev.xylonity.knightquest.common.event;

import dev.xylonity.knightquest.KnightQuest;
import dev.xylonity.knightquest.common.item.KQWeaponItem;
import dev.xylonity.knightquest.common.item.weapons.NailWeapon;
import dev.xylonity.knightquest.config.values.KQConfigValues;
import dev.xylonity.knightquest.registry.KnightQuestItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.EntityTickEvent;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Port of the client-side double jump from the 1.21.1 {@code KQArmorEvents#onLivingTick}:
 * wearing the Tengu helmet (or holding an activated Nail) grants one mid-air jump while the
 * jump key is held, with a cloud ring effect. It now lives in its own client-only event class
 * (the 26.x project pattern, see {@code KQClientEventProviders}) instead of a
 * {@code FMLEnvironment.dist}-guarded method inside a common handler, and only the local
 * player is handled (the 1.21.1 code would visually double-jump remote players too).
 */
@EventBusSubscriber(modid = KnightQuest.MOD_ID, value = Dist.CLIENT)
public class KQDoubleJumpEvents {

    /**
     * Tracks the mid-air jump availability for the local player, resetting when on ground.
     */
    private static final Map<UUID, Boolean> DOUBLE_JUMP_STATES = new ConcurrentHashMap<>();

    @SubscribeEvent
    public static void onLocalPlayerTick(EntityTickEvent.Post event) {
        if (!(event.getEntity() instanceof LocalPlayer player)) return;

        if (!(KQConfigValues.TENGU_HELMET.get() || KQConfigValues.NAIL.get())) return;

        ItemStack helmet = player.getItemBySlot(EquipmentSlot.HEAD);
        ItemStack stack = player.getMainHandItem();

        boolean wearingTengu = helmet.getItem() == KnightQuestItems.TENGU_HELMET.get();
        boolean holdingActivatedNail = stack.getItem() instanceof NailWeapon && KQWeaponItem.isActivated(stack);

        if (!(wearingTengu || holdingActivatedNail)) return;

        boolean canDoubleJump = DOUBLE_JUMP_STATES.getOrDefault(player.getUUID(), true);

        if (!player.onGround() && player.getDeltaMovement().y < 0 && canDoubleJump) {
            if (Minecraft.getInstance().options.keyJump.isDown()) {
                DOUBLE_JUMP_STATES.put(player.getUUID(), false);

                for (int i = 0; i < 360; i += 60) {
                    double angleRadians = Math.toRadians(i);

                    double particleX = player.getX() + 0.4 * Math.cos(angleRadians);
                    double particleZ = player.getZ() + 0.4 * Math.sin(angleRadians);

                    player.level().addParticle(ParticleTypes.CLOUD, particleX, player.getY(), particleZ, 0d, 0.35d, 0d);
                }

                player.jumpFromGround();
            }
        }

        if (player.onGround())
            DOUBLE_JUMP_STATES.put(player.getUUID(), true);
    }
}
