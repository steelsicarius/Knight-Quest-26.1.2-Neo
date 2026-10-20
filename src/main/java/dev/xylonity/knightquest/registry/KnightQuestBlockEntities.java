package dev.xylonity.knightquest.registry;

import dev.xylonity.knightquest.KnightQuest;
import dev.xylonity.knightquest.common.block.GreatChaliceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class KnightQuestBlockEntities {

    public static void init() { ;; }

    public static final Supplier<BlockEntityType<GreatChaliceBlockEntity>> GREAT_CHALICE =
            KnightQuest.BLOCK_ENTITIES.register("great_chalice",
                    () -> new BlockEntityType<>(GreatChaliceBlockEntity::new, KnightQuestBlocks.GREAT_CHALICE.get()));

}
