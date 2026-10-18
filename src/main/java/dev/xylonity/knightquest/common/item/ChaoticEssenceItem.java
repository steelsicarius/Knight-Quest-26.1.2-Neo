package dev.xylonity.knightquest.common.item;

/**
 * Chaotic essence item.
 * <p>
 * Port note: the original implementation relied on KnightLib's Great Chalice interop
 * ({@code IGreatChaliceInteractable}). KnightLib is not available for this port, so the
 * interop behaviour has been dropped and the item behaves as a regular KQ item (see README).
 */
public class ChaoticEssenceItem extends KnightQuestItem {

    public ChaoticEssenceItem(Properties properties, String tooltipInfoName) {
        super(properties, tooltipInfoName);
    }

}
