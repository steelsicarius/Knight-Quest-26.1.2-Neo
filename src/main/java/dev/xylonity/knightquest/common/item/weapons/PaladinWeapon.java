package dev.xylonity.knightquest.common.item.weapons;

import dev.xylonity.knightquest.common.item.KQWeaponItem;
import dev.xylonity.knightquest.config.values.KQConfigValues;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;

public class PaladinWeapon extends KQWeaponItem {

    public PaladinWeapon(ToolMaterial material, float attackBaseline, float speed, Properties pProperties) {
        super(material, attackBaseline, speed, pProperties);
    }

    @Override
    public void interaction(Level level, Player player, InteractionHand hand) {
        player.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, KQConfigValues.INV_TICKS_PALADIN.get(), 5, false, false));
    }

    @Override
    public int getCooldownTicks() {
        return KQConfigValues.COOLDOWN_PALADIN.get();
    }

    @Override
    public String getName() {
        return "paladin";
    }

    @Override
    protected boolean isEnabled() {
        return KQConfigValues.PALADIN.get();
    }

}
