package dev.xylonity.knightquest.common.item;

/**
 * Radiant essence item.
 * <p>
 * Port note: the original implementation relied on KnightLib's Great Chalice interop
 * ({@code IGreatChaliceInteractable}) to summon the Netherman. KnightLib is not available
 * for this port, so the interop behaviour has been dropped and the item behaves as a
 * regular KQ item (see README).
 */
public class RadiantEssenceItem extends KnightQuestItem {

    public RadiantEssenceItem(Properties properties, String tooltipInfoName) {
        super(properties, tooltipInfoName);
    }

}
