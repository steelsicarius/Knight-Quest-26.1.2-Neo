package dev.xylonity.knightquest.registry;

import dev.xylonity.knightquest.KnightQuest;
import dev.xylonity.knightquest.common.item.weapons.*;
import dev.xylonity.knightquest.common.material.KQItemMaterials;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class KnightQuestWeapons {

    public static final Supplier<Item> PALADIN_SWORD = register("paladin_sword", p -> new PaladinWeapon(KQItemMaterials.PALADIN, -1f, -2.6f, p.fireResistant()));
    public static final Supplier<Item> NAIL = register("nail_glaive", p -> new NailWeapon(KQItemMaterials.NAIL, -1f, -2.4f, p.fireResistant()));
    public static final Supplier<Item> UCHIGATANA = register("uchigatana_katana", p -> new UchigatanaWeapon(KQItemMaterials.UCHIGATANA, -1f, -2.2f, p.fireResistant()));
    public static final Supplier<Item> KUKRI = register("kukri_dagger", p -> new KukriWeapon(KQItemMaterials.KUKRI, -1f, -1f, p));
    public static final Supplier<Item> KHOPESH = register("khopesh_claymore", p -> new KhopeshWeapon(KQItemMaterials.KHOPESH, -1f, -2.6f, p.fireResistant()));
    public static final Supplier<Item> CLEAVER = register("cleaver", p -> new CleaverWeapon(KQItemMaterials.CLEAVER, -1f, -3.2f, p.fireResistant()));

    private static Supplier<Item> register(String id, java.util.function.Function<Item.Properties, Item> item) {
        return KnightQuest.ITEMS.registerItem(id, item);
    }

}
