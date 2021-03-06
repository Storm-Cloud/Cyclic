package com.lothrazar.cyclic.util;

import com.lothrazar.cyclic.ModCyclic;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.WallTorchBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.Property;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class UtilPlaceBlocks {

  public static boolean rotateBlockValidState(World worldObj, BlockPos pos, Direction side) {
    BlockState clicked = worldObj.getBlockState(pos);
    if (clicked.getBlock() == null) {
      return false;
    }
    Block clickedBlock = clicked.getBlock();
    BlockState newState = null;
    if (clickedBlock.isIn(BlockTags.SLABS)) {
      final String key = "type"; //top or bottom
      final String valueDupe = "double"; //actually theres 3 but dont worry about it
      //      clicked.get(property)
      for (Property<?> prop : clicked.getProperties()) {
        //yes
        if (prop.getName().equals(key)) {
          //then cycle me 
          newState = clicked.func_235896_a_(prop); // cycle
          if (newState.get(prop).toString().equals(valueDupe)) {
            //haha just hack and skip. turns into length 2. dont worry about it
            newState = newState.func_235896_a_(prop);
          }
        }
      }
    }
    else {
      //default whatever
      switch (side) {
        case DOWN:
          newState = clickedBlock.rotate(clicked, worldObj, pos, Rotation.CLOCKWISE_180);
        break;
        case EAST:
          newState = clickedBlock.rotate(clicked, worldObj, pos, Rotation.CLOCKWISE_90);
        break;
        case NORTH:
          newState = clickedBlock.rotate(clicked, worldObj, pos, Rotation.COUNTERCLOCKWISE_90);
        break;
        case SOUTH:
          newState = clickedBlock.rotate(clicked, worldObj, pos, Rotation.CLOCKWISE_90);
        break;
        case UP:
          newState = clickedBlock.rotate(clicked, worldObj, pos, Rotation.CLOCKWISE_180);
        break;
        case WEST:
          newState = clickedBlock.rotate(clicked, worldObj, pos, Rotation.COUNTERCLOCKWISE_90);
        break;
        default:
        break;
      }
    }
    if (newState != null) {
      return worldObj.setBlockState(pos, newState);
    }
    return false;
  }

  public static boolean placeStateSafe(World world, PlayerEntity player,
      BlockPos placePos, BlockState placeState) {
    return placeStateSafe(world, player, placePos, placeState, false);
  }

  /**
   * This will return true only if world.setBlockState(..) returns true or if the block here is already identical
   *
   * @param world
   * @param player
   * @param placePos
   * @param placeState
   * @param playSound
   * @return
   */
  public static boolean placeStateSafe(World world, PlayerEntity player, BlockPos placePos, BlockState placeState, boolean playSound) {
    if (placePos == null) {
      return false;
    }
    BlockState stateHere = null;
    //    if (player != null && PermissionRegistry.hasPermissionHere(player, placePos) == false) {
    //      return false;
    //    }
    if (world.isAirBlock(placePos) == false) {
      // if there is a block here, we might have to stop
      stateHere = world.getBlockState(placePos);
      if (stateHere != null) {
        //        Block blockHere = stateHere.getBlock();
        //
        //        if (blockHere.isReplaceable(world, placePos) == false) {
        //          // for example, torches, and the top half of a slab if you click
        //          // in the empty space
        //          return false;
        //        }
        // ok its a soft (isReplaceable == true) block so try to break it first try to destroy it
        // unless it is liquid, don't try to destroy liquid
        //blockHere.getMaterial(stateHere)
        if (stateHere.getMaterial().isLiquid() == false) {
          boolean dropBlock = true;
          if (world.isRemote == false) {
            world.destroyBlock(placePos, dropBlock);
          }
        }
      }
    }
    //    if (placeState.getBlock() instanceof BlockLeaves) { //dont let them decay
    //      placeState = placeState.withProperty(BlockLeaves.DECAYABLE, false);
    //    }
    boolean success = false;
    try {
      // flags specifies what to update, '3' means notify client & neighbors
      // isRemote to make sure we are in a server thread
      if (world.isRemote == false) {
        success = world.setBlockState(placePos, placeState, 3); // returns false when placement failed
      }
    }
    catch (Exception e) {
      // PR for context https://github.com/PrinceOfAmber/Cyclic/pull/577/files
      // and  https://github.com/PrinceOfAmber/Cyclic/pull/579/files
      // show exception from above, possibly failed placement
      ModCyclic.LOGGER.error("Error attempting to place block ", e);
    }
    // play sound to area when placement is a success
    if (success && playSound) {
      //      SoundType type = UtilSound.getSoundFromBlockstate(placeState, world, placePos);
      //      if (type != null && type.getPlaceSound() != null) {
      //        UtilSound.playSoundFromServer(type.getPlaceSound(), SoundCategory.BLOCKS, placePos, world.provider.getDimension(), UtilSound.RANGE_DEFAULT);
      //      }
    }
    return success;
  }

  public static boolean destroyBlock(World world, BlockPos pos) {
    world.removeTileEntity(pos);
    return world.setBlockState(pos, Blocks.AIR.getDefaultState()); // world.destroyBlock(pos, false);
  }

  public static boolean placeTorchSafely(World world, BlockPos blockPos) {
    Direction actual = findFirstSolidFace(world, blockPos, Direction.DOWN);
    if (actual == null) {
      return false;
    }
    if (actual.getAxis().isHorizontal()) {
      world.setBlockState(blockPos, Blocks.WALL_TORCH.getDefaultState().with(WallTorchBlock.HORIZONTAL_FACING, actual));
      return true;
    }
    else if (actual != Direction.DOWN) {
      world.setBlockState(blockPos, Blocks.TORCH.getDefaultState());
      return true;
    }
    return false;
  }

  public static Direction findFirstSolidFace(World world, BlockPos blockPos, Direction prefer) {
    Direction actual = null;
    Direction[] alternatives = { Direction.DOWN, Direction.EAST, Direction.WEST, Direction.NORTH, Direction.SOUTH, Direction.UP };
    if (world.getBlockState(blockPos.offset(prefer)).isSolid()) {
      actual = prefer;
    }
    else {
      for (Direction dir : alternatives) {
        if (world.getBlockState(blockPos.offset(dir)).isSolid()) {
          actual = dir;
        }
      }
    }
    return actual == null ? null : actual.getOpposite();
  }
}
