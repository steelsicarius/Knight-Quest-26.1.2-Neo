package dev.xylonity.knightquest.common.item.weapons;

import dev.xylonity.knightquest.common.item.KQWeaponItem;
import dev.xylonity.knightquest.config.values.KQConfigValues;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;

public class KhopeshWeapon extends KQWeaponItem {

    public KhopeshWeapon(ToolMaterial material, float attackBaseline, float speed, Properties pProperties) {
        super(material, attackBaseline, speed, pProperties);
    }

    @Override
    public void interaction(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        CustomData data = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);

        // Port note: 1.21.1 wrote "LastUsed" here (already written by KQWeaponItem#use) and never
        // wrote "KhopeshActive", so the tooltip-promised reflection window could never trigger.
        // The active now arms the window that KQArmorEvents' reflection check reads.
        data = data.update(tag -> {
            tag.putLong("LastUsed", level.getGameTime());
            tag.putLong("KhopeshActive", level.getGameTime());
        });
        stack.set(DataComponents.CUSTOM_DATA, data);
    }

    @Override
    public int getCooldownTicks() {
        return KQConfigValues.COOLDOWN_KHOPESH.get();
    }

    @Override
    public String getName() {
        return "khopesh";
    }

    @Override
    protected boolean isEnabled() {
        return KQConfigValues.KHOPESH.get();
    }
}
