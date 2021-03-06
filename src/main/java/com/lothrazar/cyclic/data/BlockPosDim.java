package com.lothrazar.cyclic.data;

import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;

public class BlockPosDim {

  private double x;
  private double y;
  private double z;
  private BlockPos pos;
  private String dimension;
  private String display;
  private Vector3d hitVec = Vector3d.ZERO;
  private Direction side;
  private Direction sidePlayerFacing;

  public BlockPosDim(BlockPos pos, String dimension) {
    setX(pos.getX());
    setY(pos.getY());
    setZ(pos.getZ());
    this.setPos(pos);
    this.setDimension(dimension);
  }

  @Override
  public String toString() {
    return "[" + getDimension() + "] (" + (int) getX() + ", " + (int) getY() + ", " + (int) getZ() + ")";
  }

  public String getDimension() {
    return dimension;
  }

  public void setDimension(String dimension) {
    this.dimension = dimension;
  }

  public String getDisplay() {
    return display;
  }

  public void setDisplay(String display) {
    this.display = display;
  }

  public double getZ() {
    return z;
  }

  public void setZ(double z) {
    this.z = z;
  }

  public double getY() {
    return y;
  }

  public void setY(double y) {
    this.y = y;
  }

  public double getX() {
    return x;
  }

  public void setX(double x) {
    this.x = x;
  }

  public Direction getSide() {
    return side;
  }

  public void setSide(Direction side) {
    this.side = side;
  }

  public Vector3d getHitVec() {
    return hitVec;
  }

  public void setHitVec(Vector3d hitVec) {
    this.hitVec = hitVec;
  }

  public Direction getSidePlayerFacing() {
    return sidePlayerFacing;
  }

  public void setSidePlayerFacing(Direction sidePlayerFacing) {
    this.sidePlayerFacing = sidePlayerFacing;
  }

  public BlockPos getPos() {
    return pos;
  }

  public void setPos(BlockPos pos) {
    this.pos = pos;
  }
}
