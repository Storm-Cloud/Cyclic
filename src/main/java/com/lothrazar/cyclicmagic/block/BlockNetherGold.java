package com.lothrazar.cyclicmagic.block;

import java.util.Random;
import net.minecraft.block.BlockOre;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;

public class BlockNetherGold extends BlockOre{

	public static final String name = "nether_gold_ore";

	public BlockNetherGold(){

		super();
		this.setStepSound(SoundType.STONE);
		//copy what gold ore uses)
		this.setHardness(3.0F).setResistance(5.0F);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune){

		return Items.gold_nugget;// Item.getItemFromBlock(this);
	}

	public int quantityDropped(Random random){

		// lapis uses 4 + random.nextInt(5);
		// so just a bit less
		return 3 + random.nextInt(3);
	}
}
