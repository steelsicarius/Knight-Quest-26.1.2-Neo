package dev.xylonity.knightquest.common.ai.navigator;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.level.pathfinder.PathFinder;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

/**
 * Port note (26.1.2): vanilla {@link Path} became a final class, so the old node-position
 * caching subclass is no longer possible. The finder simply delegates to the vanilla
 * implementation (functionally identical, without the per-node position cache).
 */
public class BonusPathFinder extends PathFinder {

    public BonusPathFinder(NodeEvaluator processor, int maxVisitedNodes) {
        super(processor, maxVisitedNodes);
    }

    @Override
    public Path findPath(@NotNull PathNavigationRegion region, @NotNull Mob mob, @NotNull Set<BlockPos> targetPositions, float maxRange, int accuracy, float searchDepthMultiplier) {
        return super.findPath(region, mob, targetPositions, maxRange, accuracy, searchDepthMultiplier);
    }

}
