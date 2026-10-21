package dev.xylonity.knightquest.registry;

import dev.xylonity.knightquest.KnightQuestCommon;
import dev.xylonity.knightquest.common.item.ChaoticEssenceItem;
import dev.xylonity.knightquest.common.item.EmptyGobletItem;
import dev.xylonity.knightquest.common.item.GreatEssenceItem;
import dev.xylonity.knightquest.common.item.KnightQuestItem;
import dev.xylonity.knightquest.common.item.RadiantEssenceItem;
import dev.xylonity.knightquest.common.item.SmallEssenceItem;
import dev.xylonity.knightquest.common.material.KQArmorMaterials;
import dev.xylonity.knightquest.common.material.KQItemMaterials;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.*;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.equipment.ArmorType;

import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class KnightQuestItems {

    public static void init() { ;; }

    // Replacements for the KnightLib items used by the original mod
    public static final Supplier<Item> GREAT_ESSENCE = registerItem("great_essence", p -> new GreatEssenceItem(p.fireResistant().rarity(Rarity.RARE), "great_essence"));
    public static final Supplier<Item> SMALL_ESSENCE = registerItem("small_essence", p -> new SmallEssenceItem(p.fireResistant(), "small_essence"));

    public static final Supplier<Item> RADIANT_ESSENCE = registerSpecificItem("radiant_essence", p -> p, KQItemType.RADIANT_ESSENCE);
    public static final Supplier<Item> CHAOTIC_ESSENCE = registerSpecificItem("chaotic_essence", p -> p, KQItemType.CHAOTIC_ESSENCE);
    public static final Supplier<Item> EMPTY_GOBLET = registerItem("empty_goblet", p -> new EmptyGobletItem(p, "empty_goblet"));
    public static final Supplier<Item> FILLED_GOBLET = registerItem("filled_goblet", p -> new KnightQuestItem(p, "filled_goblet"));
    public static final Supplier<Item> RATMAN_EYE = registerItem("ratman_eye", p -> new KnightQuestItem(p, "ratman_eye"));
    public static final Supplier<Item> LIZZY_SCALE = registerItem("lizzy_scale", p -> new KnightQuestItem(p, "lizzy_scale"));

    public static final Supplier<Item> THE_ARCHITECT_OF_CHAOS_DISC = registerMusicDisc("music_disc_the_architect_of_chaos", 6, KnightQuestSounds.THE_ARCHITECT_OF_CHAOS, p -> p.stacksTo(1).rarity(Rarity.EPIC).fireResistant(), 3640);

    public static final Supplier<Item> PALADIN_SWORD = KnightQuestWeapons.PALADIN_SWORD;
    public static final Supplier<Item> NAIL = KnightQuestWeapons.NAIL;
    public static final Supplier<Item> UCHIGATANA = KnightQuestWeapons.UCHIGATANA;
    public static final Supplier<Item> KUKRI = KnightQuestWeapons.KUKRI;
    public static final Supplier<Item> KHOPESH = KnightQuestWeapons.KHOPESH;
    public static final Supplier<Item> CLEAVER = KnightQuestWeapons.CLEAVER;

    public static final Supplier<Item> STEEL_SWORD = registerSwordItem("steel_sword", KQItemMaterials.STEEL_SWORD, p -> p, 1f, -2.4f, false);
    public static final Supplier<Item> STEEL_AXE = registerAxeItem("steel_axe", KQItemMaterials.STEEL_AXE, p -> p, 4f, -3f);

    public static final Supplier<Item> APPLE_HELMET = registerGeoArmorItem("apple_helmet", KQArmorMaterials.APPLE_SET, ArmorType.HELMET, true, false, p -> p, 35);
    public static final Supplier<Item> APPLE_CHESTPLATE = registerArmorItem("apple_chestplate", KQArmorMaterials.APPLE_SET, ArmorType.CHESTPLATE, true, p -> p, 35);
    public static final Supplier<Item> APPLE_LEGGINGS = registerArmorItem("apple_leggings", KQArmorMaterials.APPLE_SET, ArmorType.LEGGINGS, true, p -> p, 35);
    public static final Supplier<Item> APPLE_BOOTS = registerArmorItem("apple_boots", KQArmorMaterials.APPLE_SET, ArmorType.BOOTS, true, p -> p, 35);

    public static final Supplier<Item> BAMBOO_BLUE_HELMET = registerGeoArmorItem("bamboo_blue_helmet", KQArmorMaterials.BAMBOOSET_BLUE, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> BAMBOO_BLUE_CHESTPLATE = registerArmorItem("bamboo_blue_chestplate", KQArmorMaterials.BAMBOOSET_BLUE, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> BAMBOO_BLUE_LEGGINGS = registerArmorItem("bamboo_blue_leggings", KQArmorMaterials.BAMBOOSET_BLUE, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> BAMBOO_BLUE_BOOTS = registerArmorItem("bamboo_blue_boots", KQArmorMaterials.BAMBOOSET_BLUE, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> BAMBOO_GREEN_HELMET = registerGeoArmorItem("bamboo_green_helmet", KQArmorMaterials.BAMBOOSET_GREEN, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> BAMBOO_GREEN_CHESTPLATE = registerArmorItem("bamboo_green_chestplate", KQArmorMaterials.BAMBOOSET_GREEN, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> BAMBOO_GREEN_LEGGINGS = registerArmorItem("bamboo_green_leggings", KQArmorMaterials.BAMBOOSET_GREEN, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> BAMBOO_GREEN_BOOTS = registerArmorItem("bamboo_green_boots", KQArmorMaterials.BAMBOOSET_GREEN, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> TENGU_HELMET = registerGeoArmorItem("tengu_helmet", KQArmorMaterials.TENGU, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> BAMBOO_HELMET = registerGeoArmorItem("bamboo_helmet", KQArmorMaterials.BAMBOOSET, ArmorType.HELMET, true, false, p -> p, 35);
    public static final Supplier<Item> BAMBOO_CHESTPLATE = registerArmorItem("bamboo_chestplate", KQArmorMaterials.BAMBOOSET, ArmorType.CHESTPLATE, true, p -> p, 35);
    public static final Supplier<Item> BAMBOO_LEGGINGS = registerArmorItem("bamboo_leggings", KQArmorMaterials.BAMBOOSET, ArmorType.LEGGINGS, true, p -> p, 35);
    public static final Supplier<Item> BAMBOO_BOOTS = registerArmorItem("bamboo_boots", KQArmorMaterials.BAMBOOSET, ArmorType.BOOTS, true, p -> p, 35);

    public static final Supplier<Item> BAT_HELMET = registerGeoArmorItem("bat_helmet", KQArmorMaterials.BATSET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> BAT_CHESTPLATE = registerArmorItem("bat_chestplate", KQArmorMaterials.BATSET, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> BAT_LEGGINGS = registerArmorItem("bat_leggings", KQArmorMaterials.BATSET, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> BAT_BOOTS = registerArmorItem("bat_boots", KQArmorMaterials.BATSET, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> BLAZE_HELMET = registerGeoArmorItem("blaze_helmet", KQArmorMaterials.BLAZESET, ArmorType.HELMET, true, false, p -> p, 35);
    public static final Supplier<Item> BLAZE_CHESTPLATE = registerArmorItem("blaze_chestplate", KQArmorMaterials.BLAZESET, ArmorType.CHESTPLATE, true, p -> p, 35);
    public static final Supplier<Item> BLAZE_LEGGINGS = registerArmorItem("blaze_leggings", KQArmorMaterials.BLAZESET, ArmorType.LEGGINGS, true, p -> p, 35);
    public static final Supplier<Item> BLAZE_BOOTS = registerArmorItem("blaze_boots", KQArmorMaterials.BLAZESET, ArmorType.BOOTS, true, p -> p, 35);

    public static final Supplier<Item> BOW_HELMET = registerGeoArmorItem("bow_helmet", KQArmorMaterials.BOWSET, ArmorType.HELMET, true, false, p -> p, 35);
    public static final Supplier<Item> BOW_CHESTPLATE = registerArmorItem("bow_chestplate", KQArmorMaterials.BOWSET, ArmorType.CHESTPLATE, true, p -> p, 35);
    public static final Supplier<Item> BOW_LEGGINGS = registerArmorItem("bow_leggings", KQArmorMaterials.BOWSET, ArmorType.LEGGINGS, true, p -> p, 35);
    public static final Supplier<Item> BOW_BOOTS = registerArmorItem("bow_boots", KQArmorMaterials.BOWSET, ArmorType.BOOTS, true, p -> p, 35);

    public static final Supplier<Item> HORN_HELMET = registerGeoArmorItem("horn_helmet", KQArmorMaterials.HORNSET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> HORN_CHESTPLATE = registerArmorItem("horn_chestplate", KQArmorMaterials.HORNSET, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> HORN_LEGGINGS = registerArmorItem("horn_leggings", KQArmorMaterials.HORNSET, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> HORN_BOOTS = registerArmorItem("horn_boots", KQArmorMaterials.HORNSET, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> CREEPER_HELMET = registerArmorItem("creeper_helmet", KQArmorMaterials.CREEPERSET, ArmorType.HELMET, true, p -> p, 35);
    public static final Supplier<Item> CREEPER_CHESTPLATE = registerArmorItem("creeper_chestplate", KQArmorMaterials.CREEPERSET, ArmorType.CHESTPLATE, true, p -> p, 35);
    public static final Supplier<Item> CREEPER_LEGGINGS = registerArmorItem("creeper_leggings", KQArmorMaterials.CREEPERSET, ArmorType.LEGGINGS, true, p -> p, 35);
    public static final Supplier<Item> CREEPER_BOOTS = registerArmorItem("creeper_boots", KQArmorMaterials.CREEPERSET, ArmorType.BOOTS, true, p -> p, 35);

    public static final Supplier<Item> DEEPSLATE_HELMET = registerGeoArmorItem("deepslate_helmet", KQArmorMaterials.DEEPSLATESET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> DEEPSLATE_CHESTPLATE = registerArmorItem("deepslate_chestplate", KQArmorMaterials.DEEPSLATESET, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> DEEPSLATE_LEGGINGS = registerArmorItem("deepslate_leggings", KQArmorMaterials.DEEPSLATESET, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> DEEPSLATE_BOOTS = registerArmorItem("deepslate_boots", KQArmorMaterials.DEEPSLATESET, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> DRAGON_HELMET = registerGeoArmorItem("dragon_helmet", KQArmorMaterials.DRAGONSET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> DRAGON_CHESTPLATE = registerArmorItem("dragon_chestplate", KQArmorMaterials.DRAGONSET, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> DRAGON_LEGGINGS = registerArmorItem("dragon_leggings", KQArmorMaterials.DRAGONSET, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> DRAGON_BOOTS = registerArmorItem("dragon_boots", KQArmorMaterials.DRAGONSET, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> ENDERMAN_HELMET = registerGeoArmorItem("enderman_helmet", KQArmorMaterials.ENDERMANSET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> ENDERMAN_CHESTPLATE = registerArmorItem("enderman_chestplate", KQArmorMaterials.ENDERMANSET, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> ENDERMAN_LEGGINGS = registerArmorItem("enderman_leggings", KQArmorMaterials.ENDERMANSET, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> ENDERMAN_BOOTS = registerArmorItem("enderman_boots", KQArmorMaterials.ENDERMANSET, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> EVOKER_HELMET = registerGeoArmorItem("evoker_helmet", KQArmorMaterials.EVOKERSET, ArmorType.HELMET, true, false, p -> p, 35);
    public static final Supplier<Item> EVOKER_CHESTPLATE = registerArmorItem("evoker_chestplate", KQArmorMaterials.EVOKERSET, ArmorType.CHESTPLATE, true, p -> p, 35);
    public static final Supplier<Item> EVOKER_LEGGINGS = registerArmorItem("evoker_leggings", KQArmorMaterials.EVOKERSET, ArmorType.LEGGINGS, true, p -> p, 35);
    public static final Supplier<Item> EVOKER_BOOTS = registerArmorItem("evoker_boots", KQArmorMaterials.EVOKERSET, ArmorType.BOOTS, true, p -> p, 35);

    public static final Supplier<Item> FORZE_HELMET = registerGeoArmorItem("forze_helmet", KQArmorMaterials.FORZESET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> FORZE_CHESTPLATE = registerGeoArmorItem("forze_chestplate", KQArmorMaterials.FORZESET, ArmorType.CHESTPLATE, true, false, p -> p, 40);
    public static final Supplier<Item> FORZE_LEGGINGS = registerArmorItem("forze_leggings", KQArmorMaterials.FORZESET, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> FORZE_BOOTS = registerArmorItem("forze_boots", KQArmorMaterials.FORZESET, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> HOLLOW_HELMET = registerGeoArmorItem("hollow_helmet", KQArmorMaterials.HOLLOWSET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> HOLLOW_CHESTPLATE = registerArmorItem("hollow_chestplate", KQArmorMaterials.HOLLOWSET, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> HOLLOW_LEGGINGS = registerArmorItem("hollow_leggings", KQArmorMaterials.HOLLOWSET, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> HOLLOW_BOOTS = registerArmorItem("hollow_boots", KQArmorMaterials.HOLLOWSET, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> NETHER_HELMET = registerGeoArmorItem("nether_helmet", KQArmorMaterials.NETHERSET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> NETHER_CHESTPLATE = registerArmorItem("nether_chestplate", KQArmorMaterials.NETHERSET, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> NETHER_LEGGINGS = registerArmorItem("nether_leggings", KQArmorMaterials.NETHERSET, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> NETHER_BOOTS = registerArmorItem("nether_boots", KQArmorMaterials.NETHERSET, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> VETERAN_HELMET = registerGeoArmorItem("veteran_helmet", KQArmorMaterials.VETERANSET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> VETERAN_CHESTPLATE = registerGeoArmorItem("veteran_chestplate", KQArmorMaterials.VETERANSET, ArmorType.CHESTPLATE, true, false, p -> p, 40);
    public static final Supplier<Item> VETERAN_LEGGINGS = registerGeoArmorItem("veteran_leggings", KQArmorMaterials.VETERANSET, ArmorType.LEGGINGS, true, false, p -> p, 40);
    public static final Supplier<Item> VETERAN_BOOTS = registerArmorItem("veteran_boots", KQArmorMaterials.VETERANSET, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> PATH_HELMET = registerGeoArmorItem("path_helmet", KQArmorMaterials.PATHSET, ArmorType.HELMET, true, false, p -> p, 35);
    public static final Supplier<Item> PATH_CHESTPLATE = registerArmorItem("path_chestplate", KQArmorMaterials.PATHSET, ArmorType.CHESTPLATE, true, p -> p, 35);
    public static final Supplier<Item> PATH_LEGGINGS = registerArmorItem("path_leggings", KQArmorMaterials.PATHSET, ArmorType.LEGGINGS, true, p -> p, 35);
    public static final Supplier<Item> PATH_BOOTS = registerArmorItem("path_boots", KQArmorMaterials.PATHSET, ArmorType.BOOTS, true, p -> p, 35);

    public static final Supplier<Item> PHANTOM_HELMET = registerGeoArmorItem("phantom_helmet", KQArmorMaterials.PHANTOMSET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> PHANTOM_CHESTPLATE = registerArmorItem("phantom_chestplate", KQArmorMaterials.PHANTOMSET, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> PHANTOM_LEGGINGS = registerArmorItem("phantom_leggings", KQArmorMaterials.PHANTOMSET, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> PHANTOM_BOOTS = registerArmorItem("phantom_boots", KQArmorMaterials.PHANTOMSET, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> SEA_HELMET = registerGeoArmorItem("sea_helmet", KQArmorMaterials.SEASET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> SEA_CHESTPLATE = registerArmorItem("sea_chestplate", KQArmorMaterials.SEASET, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> SEA_LEGGINGS = registerArmorItem("sea_leggings", KQArmorMaterials.SEASET, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> SEA_BOOTS = registerArmorItem("sea_boots", KQArmorMaterials.SEASET, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> SHIELD_HELMET = registerGeoArmorItem("shield_helmet", KQArmorMaterials.SHIELDSET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> SHIELD_CHESTPLATE = registerArmorItem("shield_chestplate", KQArmorMaterials.SHIELDSET, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> SHIELD_LEGGINGS = registerArmorItem("shield_leggings", KQArmorMaterials.SHIELDSET, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> SHIELD_BOOTS = registerArmorItem("shield_boots", KQArmorMaterials.SHIELDSET, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> SILVER_HELMET = registerGeoArmorItem("silver_helmet", KQArmorMaterials.SILVERSET, ArmorType.HELMET, true, false, p -> p, 35);
    public static final Supplier<Item> SILVER_CHESTPLATE = registerGeoArmorItem("silver_chestplate", KQArmorMaterials.SILVERSET, ArmorType.CHESTPLATE, true, false, p -> p, 35);
    public static final Supplier<Item> SILVER_LEGGINGS = registerArmorItem("silver_leggings", KQArmorMaterials.SILVERSET, ArmorType.LEGGINGS, true, p -> p, 35);
    public static final Supplier<Item> SILVER_BOOTS = registerArmorItem("silver_boots", KQArmorMaterials.SILVERSET, ArmorType.BOOTS, true, p -> p, 35);

    public static final Supplier<Item> SILVERFISH_HELMET = registerGeoArmorItem("silverfish_helmet", KQArmorMaterials.SILVERFISHSET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> SILVERFISH_CHESTPLATE = registerArmorItem("silverfish_chestplate", KQArmorMaterials.SILVERFISHSET, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> SILVERFISH_LEGGINGS = registerArmorItem("silverfish_leggings", KQArmorMaterials.SILVERFISHSET, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> SILVERFISH_BOOTS = registerArmorItem("silverfish_boots", KQArmorMaterials.SILVERFISHSET, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> SKELETON_HELMET = registerGeoArmorItem("skeleton_helmet", KQArmorMaterials.SKELETONSET, ArmorType.HELMET, true, false, p -> p, 35);
    public static final Supplier<Item> SKELETON_CHESTPLATE = registerArmorItem("skeleton_chestplate", KQArmorMaterials.SKELETONSET, ArmorType.CHESTPLATE, true, p -> p, 35);
    public static final Supplier<Item> SKELETON_LEGGINGS = registerArmorItem("skeleton_leggings", KQArmorMaterials.SKELETONSET, ArmorType.LEGGINGS, true, p -> p, 35);
    public static final Supplier<Item> SKELETON_BOOTS = registerArmorItem("skeleton_boots", KQArmorMaterials.SKELETONSET, ArmorType.BOOTS, true, p -> p, 35);

    public static final Supplier<Item> SPIDER_HELMET = registerGeoArmorItem("spider_helmet", KQArmorMaterials.SPIDERSET, ArmorType.HELMET, true, false, p -> p, 35);
    public static final Supplier<Item> SPIDER_CHESTPLATE = registerGeoArmorItem("spider_chestplate", KQArmorMaterials.SPIDERSET, ArmorType.CHESTPLATE, true, false, p -> p, 35);
    public static final Supplier<Item> SPIDER_LEGGINGS = registerArmorItem("spider_leggings", KQArmorMaterials.SPIDERSET, ArmorType.LEGGINGS, true, p -> p, 35);
    public static final Supplier<Item> SPIDER_BOOTS = registerArmorItem("spider_boots", KQArmorMaterials.SPIDERSET, ArmorType.BOOTS, true, p -> p, 35);

    public static final Supplier<Item> WARLORD_HELMET = registerGeoArmorItem("warlord_helmet", KQArmorMaterials.WARLORDSET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> WARLORD_CHESTPLATE = registerArmorItem("warlord_chestplate", KQArmorMaterials.WARLORDSET, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> WARLORD_LEGGINGS = registerArmorItem("warlord_leggings", KQArmorMaterials.WARLORDSET, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> WARLORD_BOOTS = registerArmorItem("warlord_boots", KQArmorMaterials.WARLORDSET, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> STRAWHAT_HELMET = registerGeoArmorItem("strawhat_helmet", KQArmorMaterials.STRAWHATSET, ArmorType.HELMET, true, false, p -> p, 35);
    public static final Supplier<Item> STRAWHAT_CHESTPLATE = registerArmorItem("strawhat_chestplate", KQArmorMaterials.STRAWHATSET, ArmorType.CHESTPLATE, true, p -> p, 35);
    public static final Supplier<Item> STRAWHAT_LEGGINGS = registerArmorItem("strawhat_leggings", KQArmorMaterials.STRAWHATSET, ArmorType.LEGGINGS, true, p -> p, 35);
    public static final Supplier<Item> STRAWHAT_BOOTS = registerArmorItem("strawhat_boots", KQArmorMaterials.STRAWHATSET, ArmorType.BOOTS, true, p -> p, 35);

    public static final Supplier<Item> PIRATE_HELMET = registerGeoArmorItem("pirate_helmet", KQArmorMaterials.PIRATESET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> PIRATE2_HELMET = registerGeoArmorItem("pirate2_helmet", KQArmorMaterials.PIRATESET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> PIRATE3_HELMET = registerGeoArmorItem("pirate3_helmet", KQArmorMaterials.PIRATESET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> PIRATE_CHESTPLATE = registerArmorItem("pirate_chestplate", KQArmorMaterials.PIRATESET, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> PIRATE_LEGGINGS = registerArmorItem("pirate_leggings", KQArmorMaterials.PIRATESET, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> PIRATE_BOOTS = registerArmorItem("pirate_boots", KQArmorMaterials.PIRATESET, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> CONQUISTADOR_HELMET = registerGeoArmorItem("conquistador_helmet", KQArmorMaterials.CONQUISTADORSET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> CONQUISTADOR2_HELMET = registerGeoArmorItem("conquistador2_helmet", KQArmorMaterials.CONQUISTADORSET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> CONQUISTADOR3_HELMET = registerGeoArmorItem("conquistador3_helmet", KQArmorMaterials.CONQUISTADORSET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> CONQUISTADOR_CHESTPLATE = registerArmorItem("conquistador_chestplate", KQArmorMaterials.CONQUISTADORSET, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> CONQUISTADOR_LEGGINGS = registerArmorItem("conquistador_leggings", KQArmorMaterials.CONQUISTADORSET, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> CONQUISTADOR_BOOTS = registerArmorItem("conquistador_boots", KQArmorMaterials.CONQUISTADORSET, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> ZOMBIE_HELMET = registerGeoArmorItem("zombie_helmet", KQArmorMaterials.ZOMBIESET, ArmorType.HELMET, true, false, p -> p, 35);
    public static final Supplier<Item> ZOMBIE_HELMET2 = registerGeoArmorItem("zombie_helmet2", KQArmorMaterials.ZOMBIESET, ArmorType.HELMET, true, false, p -> p, 35);
    public static final Supplier<Item> ZOMBIE_CHESTPLATE = registerArmorItem("zombie_chestplate", KQArmorMaterials.ZOMBIESET, ArmorType.CHESTPLATE, true, p -> p, 35);
    public static final Supplier<Item> ZOMBIE_LEGGINGS = registerArmorItem("zombie_leggings", KQArmorMaterials.ZOMBIESET, ArmorType.LEGGINGS, true, p -> p, 35);
    public static final Supplier<Item> ZOMBIE_BOOTS = registerArmorItem("zombie_boots", KQArmorMaterials.ZOMBIESET, ArmorType.BOOTS, true, p -> p, 35);

    public static final Supplier<Item> HUSK_HELMET = registerGeoArmorItem("husk_helmet", KQArmorMaterials.HUSKSET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> HUSK_HELMET2 = registerGeoArmorItem("husk_helmet2", KQArmorMaterials.HUSKSET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> HUSK_HELMET3 = registerGeoArmorItem("husk_helmet3", KQArmorMaterials.HUSKSET, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> HUSK_CHESTPLATE = registerArmorItem("husk_chestplate", KQArmorMaterials.HUSKSET, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> HUSK_LEGGINGS = registerArmorItem("husk_leggings", KQArmorMaterials.HUSKSET, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> HUSK_BOOTS = registerArmorItem("husk_boots", KQArmorMaterials.HUSKSET, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> WITHER_HELMET = registerArmorItem("wither_helmet", KQArmorMaterials.WITHERSET, ArmorType.HELMET, true, p -> p, 40);
    public static final Supplier<Item> WITHER_CHESTPLATE = registerArmorItem("wither_chestplate", KQArmorMaterials.WITHERSET, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> WITHER_LEGGINGS = registerArmorItem("wither_leggings", KQArmorMaterials.WITHERSET, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> WITHER_BOOTS = registerArmorItem("wither_boots", KQArmorMaterials.WITHERSET, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> SQUIRE_HELMET = registerGeoArmorItem("squire_helmet", KQArmorMaterials.SQUIRESET, ArmorType.HELMET, true, true, p -> p, 40);
    public static final Supplier<Item> SQUIRE_CHESTPLATE = registerArmorItem("squire_chestplate", KQArmorMaterials.SQUIRESET, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> SQUIRE_LEGGINGS = registerArmorItem("squire_leggings", KQArmorMaterials.SQUIRESET, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> SQUIRE_BOOTS = registerArmorItem("squire_boots", KQArmorMaterials.SQUIRESET, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> CHAINMAIL_HELMET = registerArmorItem("chainmail_helmet", KQArmorMaterials.CHAINMAIL, ArmorType.HELMET, true, p -> p, 40);
    public static final Supplier<Item> CHAINMAIL_HELMET2 = registerGeoArmorItem("chainmail_helmet2", KQArmorMaterials.CHAINMAIL, ArmorType.HELMET, false, false, p -> p, 40);
    public static final Supplier<Item> TUNIC_BLUE_LEGGINGS = registerArmorItem("tunic_blue_leggings", KQArmorMaterials.TUNIC_BLUE, ArmorType.LEGGINGS, false, p -> p, 20);
    public static final Supplier<Item> TUNIC_GREEN_LEGGINGS = registerArmorItem("tunic_green_leggings", KQArmorMaterials.TUNIC_GREEN, ArmorType.LEGGINGS, false, p -> p, 20);
    public static final Supplier<Item> TUNIC_YELLOW_LEGGINGS = registerArmorItem("tunic_yellow_leggings", KQArmorMaterials.TUNIC_YELLOW, ArmorType.LEGGINGS, false, p -> p, 20);
    public static final Supplier<Item> TUNIC_RED_LEGGINGS = registerArmorItem("tunic_red_leggings", KQArmorMaterials.TUNIC_RED, ArmorType.LEGGINGS, false, p -> p, 20);
    public static final Supplier<Item> TUNIC_SEA_LEGGINGS = registerArmorItem("tunic_sea_leggings", KQArmorMaterials.TUNIC_SEA, ArmorType.LEGGINGS, false, p -> p, 20);

    public static final Supplier<Item> WITCH_HELMET = registerGeoArmorItem("witch_helmet", KQArmorMaterials.WITCH, ArmorType.HELMET, true, false, p -> p, 35);
    public static final Supplier<Item> WITCH_CHESTPLATE = registerArmorItem("witch_chestplate", KQArmorMaterials.WITCH, ArmorType.CHESTPLATE, true, p -> p, 35);
    public static final Supplier<Item> WITCH_LEGGINGS = registerArmorItem("witch_leggings", KQArmorMaterials.WITCH, ArmorType.LEGGINGS, true, p -> p, 35);
    public static final Supplier<Item> WITCH_BOOTS = registerArmorItem("witch_boots", KQArmorMaterials.WITCH, ArmorType.BOOTS, true, p -> p, 35);

    public static final Supplier<Item> POLAR_HELMET = registerGeoArmorItem("polar_helmet", KQArmorMaterials.POLAR, ArmorType.HELMET, true, false, p -> p, 35);
    public static final Supplier<Item> POLAR_CHESTPLATE = registerArmorItem("polar_chestplate", KQArmorMaterials.POLAR, ArmorType.CHESTPLATE, true, p -> p, 35);
    public static final Supplier<Item> POLAR_LEGGINGS = registerArmorItem("polar_leggings", KQArmorMaterials.POLAR, ArmorType.LEGGINGS, true, p -> p, 35);
    public static final Supplier<Item> POLAR_BOOTS = registerArmorItem("polar_boots", KQArmorMaterials.POLAR, ArmorType.BOOTS, true, p -> p, 35);

    public static final Supplier<Item> SHINOBI_HELMET = registerGeoArmorItem("shinobi_helmet", KQArmorMaterials.SHINOBI, ArmorType.HELMET, true, false, p -> p, 35);
    public static final Supplier<Item> SHINOBI_CHESTPLATE = registerArmorItem("shinobi_chestplate", KQArmorMaterials.SHINOBI, ArmorType.CHESTPLATE, true, p -> p, 35);
    public static final Supplier<Item> SHINOBI_LEGGINGS = registerArmorItem("shinobi_leggings", KQArmorMaterials.SHINOBI, ArmorType.LEGGINGS, true, p -> p, 35);
    public static final Supplier<Item> SHINOBI_BOOTS = registerArmorItem("shinobi_boots", KQArmorMaterials.SHINOBI, ArmorType.BOOTS, true, p -> p, 35);

    public static final Supplier<Item> SKULK_HELMET = registerGeoArmorItem("skulk_helmet", KQArmorMaterials.SKULK, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> SKULK2_HELMET = registerGeoArmorItem("skulk2_helmet", KQArmorMaterials.SKULK, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> SKULK3_HELMET = registerGeoArmorItem("skulk3_helmet", KQArmorMaterials.SKULK, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> SKULK4_HELMET = registerGeoArmorItem("skulk4_helmet", KQArmorMaterials.SKULK, ArmorType.HELMET, true, false, p -> p, 40);
    public static final Supplier<Item> SKULK_CHESTPLATE = registerArmorItem("skulk_chestplate", KQArmorMaterials.SKULK, ArmorType.CHESTPLATE, true, p -> p, 40);
    public static final Supplier<Item> SKULK_LEGGINGS = registerArmorItem("skulk_leggings", KQArmorMaterials.SKULK, ArmorType.LEGGINGS, true, p -> p, 40);
    public static final Supplier<Item> SKULK_BOOTS = registerArmorItem("skulk_boots", KQArmorMaterials.SKULK, ArmorType.BOOTS, true, p -> p, 40);

    public static final Supplier<Item> GREMLIN_EGG = registerSpawnEggItem("gremlin_spawn_egg", KnightQuestEntities.GREMLIN);
    public static final Supplier<Item> ELD_BOMB_EGG = registerSpawnEggItem("eldbomb_spawn_egg", KnightQuestEntities.ELDBOMB);
    public static final Supplier<Item> ELD_KNIGHT_EGG = registerSpawnEggItem("eldknight_spawn_egg", KnightQuestEntities.ELDKNIGHT);
    public static final Supplier<Item> RATMAN_EGG = registerSpawnEggItem("ratman_spawn_egg", KnightQuestEntities.RATMAN);
    public static final Supplier<Item> SAMHAIN_EGG = registerSpawnEggItem("samhain_spawn_egg", KnightQuestEntities.SAMHAIN);
    public static final Supplier<Item> SWAMPMAN_EGG = registerSpawnEggItem("swampman_spawn_egg", KnightQuestEntities.SWAMPMAN);
    public static final Supplier<Item> LIZZY_EGG = registerSpawnEggItem("lizzy_spawn_egg", KnightQuestEntities.LIZZY);
    public static final Supplier<Item> BADPATCH_EGG = registerSpawnEggItem("bad_patch_spawn_egg", KnightQuestEntities.BADPATCH);
    public static final Supplier<Item> GHOSTY_EGG = registerSpawnEggItem("ghosty_spawn_egg", KnightQuestEntities.GHOSTY);
    public static final Supplier<Item> NETHERMAN_EGG = registerSpawnEggItem("netherman_spawn_egg", KnightQuestEntities.NETHERMAN);
    public static final Supplier<Item> FALLEN_KNIGHT_EGG = registerSpawnEggItem("fallen_knight_spawn_egg", KnightQuestEntities.FALLEN_KNIGHT);

    private static <T extends Item> Supplier<T> registerItem(String id, Function<Item.Properties, T> item) {
        return KnightQuestCommon.COMMON_PLATFORM.registerItem(id, item);
    }

    private static <T extends Mob> Supplier<Item> registerSpawnEggItem(String id, Supplier<EntityType<T>> entityType) {
        return KnightQuestCommon.COMMON_PLATFORM.registerSpawnEggItem(id, entityType);
    }

    private static <T extends Item> Supplier<T> registerMusicDisc(String id, int signal, Supplier<SoundEvent> soundEvent, UnaryOperator<Item.Properties> properties, int length) {
        return KnightQuestCommon.COMMON_PLATFORM.registerMusicDisc(id, signal, soundEvent, properties, length);
    }

    private static <T extends Item> Supplier<T> registerSwordItem(String id, ToolMaterial itemMaterial, UnaryOperator<Item.Properties> properties, float attackBaseline, float speedMalus, boolean containsTooltip) {
        return KnightQuestCommon.COMMON_PLATFORM.registerSwordItem(id, itemMaterial, properties, attackBaseline, speedMalus, containsTooltip);
    }

    private static <T extends Item> Supplier<T> registerAxeItem(String id, ToolMaterial itemMaterial, UnaryOperator<Item.Properties> properties, float extraDamageBoost, float speedMalus) {
        return KnightQuestCommon.COMMON_PLATFORM.registerAxeItem(id, itemMaterial, properties, extraDamageBoost, speedMalus);
    }

    private static <T extends Item> Supplier<T> registerArmorItem(String id, net.minecraft.world.item.equipment.ArmorMaterial armorMaterial, ArmorType armorType, boolean containsTooltip, UnaryOperator<Item.Properties> properties, int durabilityAmount) {
        return KnightQuestCommon.COMMON_PLATFORM.registerArmorItem(id, armorMaterial, armorType, containsTooltip, properties, durabilityAmount);
    }

    private static <T extends Item> Supplier<T> registerGeoArmorItem(String id, net.minecraft.world.item.equipment.ArmorMaterial armorMaterial, ArmorType armorType, boolean containsTooltip, boolean containsExtraTooltip, UnaryOperator<Item.Properties> properties, int durabilityAmount) {
        return KnightQuestCommon.COMMON_PLATFORM.registerGeoArmorItem(id, armorMaterial, armorType, containsTooltip, containsExtraTooltip, properties, durabilityAmount);
    }

    private static <T extends Item> Supplier<T> registerSpecificItem(String id, UnaryOperator<Item.Properties> properties, KQItemType type) {
        return KnightQuestCommon.COMMON_PLATFORM.registerSpecificItem(id, properties, type);
    }








    public enum KQItemType {
        CHAOTIC_ESSENCE,
        RADIANT_ESSENCE
    }

}
