package dev.xylonity.knightquest.common.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Block item with the Knight Quest tooltip line (e.g. the Great Chalice's
 * "Offer the chalice great essence." hint).
 */
public class KQBlockItem extends BlockItem {

    private final String tooltipInfoName;

    public KQBlockItem(Block block, Properties properties, String tooltipInfoName) {
        super(block, properties);
        this.tooltipInfoName = tooltipInfoName;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @NotNull Item.TooltipContext context, @NotNull TooltipDisplay display, @NotNull Consumer<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        tooltipComponents.accept(Component.translatable("tooltip.item.knightquest." + tooltipInfoName));
        super.appendHoverText(stack, context, display, tooltipComponents, tooltipFlag);
    }

}
