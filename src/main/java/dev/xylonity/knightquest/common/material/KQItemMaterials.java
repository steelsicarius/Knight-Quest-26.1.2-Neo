package dev.xylonity.knightquest.common.material;

import dev.xylonity.knightquest.KnightQuest;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.item.Item;

/**
 * Tool materials for Knight Quest weapons. Port of the old {@code Tier}-based enum to the
 * data-driven {@link ToolMaterial} record introduced in MC 1.21.5+ (used here for 26.1.2).
 * <p>
 * The repair ingredient is now a plain {@link TagKey<Item>} instead of an {@code Ingredient},
 * so the tag {@code knightquest:great_essence} (declared in data) is used for repairs.
 */
public class KQItemMaterials {

    /** Item tag used as the repair material for every KQ weapon (holds knightquest:great_essence). */
    public static final TagKey<Item> GREAT_ESSENCE_TAG = ItemTags.create(Identifier.fromNamespaceAndPath(KnightQuest.MOD_ID, "great_essence"));

    public static final ToolMaterial PALADIN = create(2350, 0.5f, 10f);
    public static final ToolMaterial NAIL = create(2120, 0.5f, 9f);
    public static final ToolMaterial UCHIGATANA = create(2031, 0.5f, 8f);
    public static final ToolMaterial KUKRI = create(400, 0.5f, 4f);
    public static final ToolMaterial KHOPESH = create(2120, 0.5f, 9f);
    public static final ToolMaterial CLEAVER = create(2031, 0.5f, 12f);

    public static final ToolMaterial WATER_SWORD = create(850, 0.5f, 1.8f);
    public static final ToolMaterial STEEL_SWORD = create(300, 0.5f, 1.5f);
    public static final ToolMaterial WATER_AXE = create(850, 6.0f, 1.3f);
    public static final ToolMaterial STEEL_AXE = create(300, 6.5f, 4f);

    private static ToolMaterial create(int uses, float speed, float damage) {
        return new ToolMaterial(BlockTags.INCORRECT_FOR_NETHERITE_TOOL, uses, speed, damage, 15, GREAT_ESSENCE_TAG);
    }

}
