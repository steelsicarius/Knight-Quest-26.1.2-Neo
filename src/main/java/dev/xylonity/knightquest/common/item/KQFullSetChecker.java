package dev.xylonity.knightquest.common.item;

import dev.xylonity.knightquest.config.values.KQConfigValues;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorMaterial;

public class KQFullSetChecker {

    public static boolean hasFullSetOn(Player player, ArmorMaterial material) {
        int requiredPieces = KQConfigValues.REQUIRED_ARMOR_PIECES.get();
        int equippedPieces = 0;

        for (EquipmentSlot slot : new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET}) {
            ItemStack armorStack = player.getItemBySlot(slot);
            if (!armorStack.isEmpty() && armorStack.getItem() instanceof KQArmorItem armorItem) {
                if (armorItem.getMaterial() == material) {
                    equippedPieces++;
                }
            }
        }

        return equippedPieces >= requiredPieces;
    }

}
