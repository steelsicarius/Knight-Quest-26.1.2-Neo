package dev.xylonity.knightquest.common.material;

import com.google.common.collect.Maps;
import dev.xylonity.knightquest.KnightQuest;
import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.item.equipment.EquipmentAssets;

import java.util.Map;

/**
 * Armor materials for Knight Quest. Port of the old registered {@code Holder<ArmorMaterial>} set
 * to plain {@link ArmorMaterial} records (MC 1.21.5+ removed the armor material registry; armor
 * materials are now applied directly through {@code Item.Properties#humanoidArmor}).
 * <p>
 * Defense map order: (boots, leggings, chestplate, helmet, body).
 * The durability value equals the old per-piece durability multiplier (35, 40 or 20 for tunics).
 */
public class KQArmorMaterials {

    public static final ArmorMaterial APPLE_SET = create("apple", 3, 6, 8, 3, 35, 20, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.5F, 0.05F);
    public static final ArmorMaterial BAMBOOSET_BLUE = create("bamboo_blue", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial BAMBOOSET_GREEN = create("bamboo_green", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial BAMBOOSET = create("bamboo", 3, 6, 8, 3, 35, 20, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.5F, 0.05F);
    public static final ArmorMaterial BATSET = create("bat", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial BLAZESET = create("blaze", 3, 6, 8, 3, 35, 20, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.5F, 0.05F);
    public static final ArmorMaterial BOWSET = create("bow", 3, 6, 8, 3, 35, 20, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.5F, 0.05F);
    public static final ArmorMaterial HORNSET = create("horn", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial CREEPERSET = create("creeper", 3, 6, 8, 3, 35, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial DEEPSLATESET = create("deepslate", 3, 6, 8, 3, 40, 20, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.5F, 0.05F);
    public static final ArmorMaterial DRAGONSET = create("dragon", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial ENDERMANSET = create("enderman", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial EVOKERSET = create("evoker", 3, 6, 8, 3, 35, 20, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.5F, 0.05F);
    public static final ArmorMaterial FORZESET = create("forze", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial HOLLOWSET = create("hollow", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial NETHERSET = create("nether", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial VETERANSET = create("veteran", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial PATHSET = create("path", 3, 6, 8, 3, 35, 20, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.5F, 0.05F);
    public static final ArmorMaterial PHANTOMSET = create("phantom", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial SEASET = create("sea", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial SHIELDSET = create("shield", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial SILVERSET = create("silver", 3, 6, 8, 3, 35, 20, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.5F, 0.05F);
    public static final ArmorMaterial SILVERFISHSET = create("silverfish", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial SKELETONSET = create("skeleton", 3, 6, 8, 3, 35, 20, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.5F, 0.05F);
    public static final ArmorMaterial SPIDERSET = create("spider", 3, 6, 8, 3, 35, 20, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.5F, 0.05F);
    public static final ArmorMaterial WARLORDSET = create("warlord", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial STRAWHATSET = create("strawhat", 3, 6, 8, 3, 35, 20, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.5F, 0.05F);
    public static final ArmorMaterial PIRATESET = create("pirate", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial CONQUISTADORSET = create("conquistador", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial ZOMBIESET = create("zombie", 3, 6, 8, 3, 35, 20, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.5F, 0.05F);
    public static final ArmorMaterial HUSKSET = create("husk", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial WITHERSET = create("wither", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial SQUIRESET = create("squire", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial TUNIC_BLUE = create("tunic_blue", 2, 5, 5, 2, 20, 25, SoundEvents.ARMOR_EQUIP_LEATHER, 0.5f, 0F);
    public static final ArmorMaterial TUNIC_GREEN = create("tunic_green", 2, 5, 5, 2, 20, 25, SoundEvents.ARMOR_EQUIP_LEATHER, 0.5f, 0F);
    public static final ArmorMaterial TUNIC_YELLOW = create("tunic_yellow", 2, 5, 5, 2, 20, 25, SoundEvents.ARMOR_EQUIP_LEATHER, 0.5f, 0F);
    public static final ArmorMaterial TUNIC_RED = create("tunic_red", 2, 5, 5, 2, 20, 25, SoundEvents.ARMOR_EQUIP_LEATHER, 0.5f, 0F);
    public static final ArmorMaterial TUNIC_SEA = create("tunic_sea", 2, 5, 5, 2, 20, 25, SoundEvents.ARMOR_EQUIP_LEATHER, 0.5f, 0F);
    public static final ArmorMaterial CHAINMAIL = create("chainmail", 2, 5, 5, 2, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial WITCH = create("witch", 3, 6, 8, 3, 35, 20, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.5F, 0.05F);
    public static final ArmorMaterial POLAR = create("polar", 3, 6, 8, 3, 35, 20, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.5F, 0.05F);
    public static final ArmorMaterial SHINOBI = create("shinobi", 3, 6, 8, 3, 35, 20, SoundEvents.ARMOR_EQUIP_DIAMOND, 2.5F, 0.05F);
    public static final ArmorMaterial SKULK = create("skulk", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);
    public static final ArmorMaterial TENGU = create("tengu", 3, 6, 8, 3, 40, 25, SoundEvents.ARMOR_EQUIP_NETHERITE, 4F, 0.1F);

    /** Item tag used as the repair material for every KQ armor piece (holds knightquest:great_essence). */
    public static final TagKey<Item> GREAT_ESSENCE_TAG = KQItemMaterials.GREAT_ESSENCE_TAG;

    /** Maps each material to its config/tooltip key name (e.g. "apple", "bamboo_blue"). */
    private static final Map<ArmorMaterial, String> KEY_NAMES = Maps.newIdentityHashMap();

    public static void init() { ;; }

    public static String getKeyNameFromMaterial(ArmorMaterial material) {
        return KEY_NAMES.getOrDefault(material, material.toString());
    }

    private static ArmorMaterial create(String name, int boots, int leggings, int chestplate, int helmet, int durability, int enchantmentValue, Holder<SoundEvent> equipSound, float toughness, float knockbackResistance) {
        Map<ArmorType, Integer> defense = Maps.newEnumMap(Map.of(
                ArmorType.BOOTS, boots,
                ArmorType.LEGGINGS, leggings,
                ArmorType.CHESTPLATE, chestplate,
                ArmorType.HELMET, helmet,
                ArmorType.BODY, 0));
        ResourceKey<EquipmentAsset> assetId = ResourceKey.create(EquipmentAssets.ROOT_ID, Identifier.fromNamespaceAndPath(KnightQuest.MOD_ID, name));
        ArmorMaterial material = new ArmorMaterial(durability, defense, enchantmentValue, equipSound, toughness, knockbackResistance, GREAT_ESSENCE_TAG, assetId);
        KEY_NAMES.put(material, name);
        return material;
    }

}
