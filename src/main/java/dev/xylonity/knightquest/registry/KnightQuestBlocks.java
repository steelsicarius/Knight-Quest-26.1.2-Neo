package dev.xylonity.knightquest.registry;

import dev.xylonity.knightquest.KnightQuest;
import dev.xylonity.knightquest.common.block.GreatChaliceBlock;
import dev.xylonity.knightquest.common.item.blockitem.GreatChaliceBlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Block registry. The Great Chalice is the standalone port of KnightLib's block
 * (its block entity holds the essence charges and the chaotic state).
 */
public class KnightQuestBlocks {

    public static void init() { ;; }

    public static final Supplier<Block> GREAT_CHALICE = registerBlock("great_chalice",
            p -> new GreatChaliceBlock(p.mapColor(MapColor.METAL)
                    .strength(3.0F, 6.0F)
                    .sound(SoundType.COPPER)
                    .noOcclusion()
                    .requiresCorrectToolForDrops()));

    public static final Supplier<Item> GREAT_CHALICE_ITEM = registerBlockItem("great_chalice", GREAT_CHALICE);

    private static Supplier<Block> registerBlock(String name, Function<BlockBehaviour.Properties, Block> factory) {
        return KnightQuest.BLOCKS.registerBlock(name, factory, () -> BlockBehaviour.Properties.of());
    }

    private static Supplier<Item> registerBlockItem(String name, Supplier<Block> block) {
        return KnightQuest.ITEMS.registerItem(name, p -> new GreatChaliceBlockItem(block.get(), p));
    }

}
