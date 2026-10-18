package dev.xylonity.knightquest.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class KnightQuestItem extends Item {

    private final String tooltipInfoName;

    public KnightQuestItem(Properties properties, String tooltipInfoName) {
        super(properties);
        this.tooltipInfoName = tooltipInfoName;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack pStack, @NotNull Item.TooltipContext pContext, @NotNull TooltipDisplay pTooltipDisplay, @NotNull Consumer<Component> pTooltipComponents, @NotNull TooltipFlag pTooltipFlag) {
        pTooltipComponents.accept(Component.translatable("tooltip.item.knightquest." + tooltipInfoName));

        super.appendHoverText(pStack, pContext, pTooltipDisplay, pTooltipComponents, pTooltipFlag);
    }

}
