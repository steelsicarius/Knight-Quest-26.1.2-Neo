package dev.xylonity.knightquest.datagen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.xylonity.knightquest.config.values.KQConfigValues;
import dev.xylonity.knightquest.registry.KnightQuestItems;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Random;

/**
 * Port note (26.1.2): {@link LootModifier} now carries a priority field; the codec records
 * conditions + priority, and the base class already evaluates all conditions in
 * {@code apply()}, so {@code doApply} no longer re-checks them.
 */
public class KQAddItemModifier extends LootModifier {

    public static final MapCodec<KQAddItemModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            codecStart(inst).and(BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(KQAddItemModifier::getItem))
                    .and(Codec.FLOAT.fieldOf("chance").forGetter(KQAddItemModifier::getChance))
                    .apply(inst, KQAddItemModifier::new));

    private final Item item;
    private float chance;

    public KQAddItemModifier(LootItemCondition[] conditionsIn, int priority, Item item, float chance) {
        super(conditionsIn, priority);
        this.item = item;
        this.chance = chance;
    }

    public KQAddItemModifier(LootItemCondition[] conditionsIn, Item item, float chance) {
        this(conditionsIn, IGlobalLootModifier.DEFAULT_PRIORITY, item, chance);
    }

    public Item getItem() {
        return item;
    }

    public float getChance() {
        return chance;
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (item == KnightQuestItems.RATMAN_EYE.get() && new Random().nextFloat() <= KQConfigValues.DROP_CHANCE_RATMAN_EYE.get().floatValue()) {
            generatedLoot.add(new ItemStack(this.item));
        }

        if (item == KnightQuestItems.LIZZY_SCALE.get() && new Random().nextFloat() <= KQConfigValues.DROP_CHANCE_LIZZY_SCALE.get().floatValue()) {
            generatedLoot.add(new ItemStack(this.item));
        }

        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

}
