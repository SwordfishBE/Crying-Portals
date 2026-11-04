package net.crying_portals;

import net.fabricmc.fabric.api.gametest.v1.GameTest;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.portal.PortalShape;

public class PortalGameTests {
    private static final BlockPos ORIGIN = new BlockPos(2, 2, 2);

    private BlockPos position(Direction.Axis axis, int horizontal, int vertical) {
        return ORIGIN.offset(axis == Direction.Axis.X ? horizontal : 0, vertical,
                axis == Direction.Axis.Z ? horizontal : 0);
    }

    private void frame(GameTestHelper h, Direction.Axis axis, int kind) {
        for (int x = -1; x <= 2; x++) {
            for (int y = -1; y <= 3; y++) {
                boolean edge = x == -1 || x == 2 || y == -1 || y == 3;
                Block block = !edge ? Blocks.AIR : kind == 0 ? Blocks.OBSIDIAN
                        : kind == 1 || (x + y) % 2 == 0 ? Blocks.CRYING_OBSIDIAN : Blocks.OBSIDIAN;
                h.setBlock(position(axis, x, y), block);
            }
        }
    }

    private void light(GameTestHelper h, Direction.Axis axis, int kind) {
        frame(h, axis, kind);
        // Use the side of the frame: unlike the floor, air here cannot support fire.
        BlockPos ignition = position(axis, 0, 1);
        Direction side = axis == Direction.Axis.X ? Direction.SOUTH : Direction.EAST;
        h.assertTrue(BaseFireBlock.canBePlacedAt(h.getLevel(), h.absolutePos(ignition), side),
                "The normal ignition placement check must accept the frame");
        h.setBlock(ignition, Blocks.FIRE);
        for (int x = 0; x < 2; x++) for (int y = 0; y < 3; y++) {
            BlockPos pos = position(axis, x, y);
            h.assertBlockPresent(Blocks.NETHER_PORTAL, pos);
            h.assertBlockProperty(pos, NetherPortalBlock.AXIS, axis);
        }
    }

    @GameTest public void cryingFrameX(GameTestHelper h) { light(h, Direction.Axis.X, 1); h.succeed(); }
    @GameTest public void cryingFrameZ(GameTestHelper h) { light(h, Direction.Axis.Z, 1); h.succeed(); }
    @GameTest public void mixedFrameX(GameTestHelper h) { light(h, Direction.Axis.X, 2); h.succeed(); }
    @GameTest public void mixedFrameZ(GameTestHelper h) { light(h, Direction.Axis.Z, 2); h.succeed(); }
    @GameTest public void vanillaFrame(GameTestHelper h) { light(h, Direction.Axis.X, 0); h.succeed(); }

    private void useIgnitionItem(GameTestHelper h, Item item) {
        frame(h, Direction.Axis.X, 1);
        var player = h.makeMockPlayer(GameType.CREATIVE);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(item));
        BlockPos side = h.absolutePos(position(Direction.Axis.X, -1, 1));
        item.useOn(new UseOnContext(player, InteractionHand.MAIN_HAND,
                new BlockHitResult(Vec3.atCenterOf(side), Direction.EAST, side, false)));
        h.assertBlockPresent(Blocks.NETHER_PORTAL, ORIGIN);
        h.assertBlockPresent(Blocks.NETHER_PORTAL, ORIGIN.above(2));
        h.succeed();
    }

    @GameTest public void flintAndSteel(GameTestHelper h) { useIgnitionItem(h, Items.FLINT_AND_STEEL); }
    @GameTest public void fireCharge(GameTestHelper h) { useIgnitionItem(h, Items.FIRE_CHARGE); }

    @GameTest public void additiveTag(GameTestHelper h) {
        h.assertTrue(Blocks.OBSIDIAN.defaultBlockState().is(BlockTags.NETHER_PORTAL_FRAME), "Vanilla frame remains valid");
        h.assertTrue(Blocks.CRYING_OBSIDIAN.defaultBlockState().is(BlockTags.NETHER_PORTAL_FRAME), "Crying frame is added");
        h.assertFalse(Blocks.STONE.defaultBlockState().is(BlockTags.NETHER_PORTAL_FRAME), "Stone is not added");
        h.succeed();
    }

    @GameTest public void invalidFrameRejected(GameTestHelper h) {
        frame(h, Direction.Axis.X, 1);
        h.setBlock(position(Direction.Axis.X, -1, 1), Blocks.STONE);
        h.assertTrue(PortalShape.findEmptyPortalShape(h.getLevel(), h.absolutePos(ORIGIN), Direction.Axis.X).isEmpty(),
                "An invalid side must reject the portal");
        h.setBlock(ORIGIN, Blocks.FIRE);
        h.assertBlockPresent(Blocks.AIR, ORIGIN.above());
        h.succeed();
    }

    @GameTest public void brokenFrameCollapses(GameTestHelper h) {
        light(h, Direction.Axis.X, 1);
        h.setBlock(position(Direction.Axis.X, -1, 1), Blocks.AIR);
        h.runAfterDelay(3, () -> {
            for (int x = 0; x < 2; x++) for (int y = 0; y < 3; y++)
                h.assertBlockPresent(Blocks.AIR, position(Direction.Axis.X, x, y));
            h.succeed();
        });
    }
}
