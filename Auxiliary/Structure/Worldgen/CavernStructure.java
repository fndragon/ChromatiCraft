package Reika.ChromatiCraft.Auxiliary.Structure.Worldgen;

import java.util.Random;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.ChestGenHooks;
import net.minecraftforge.common.util.ForgeDirection;

import Reika.ChromatiCraft.Auxiliary.ChromaStacks;
import Reika.ChromatiCraft.Base.FragmentStructureWithBonusLoot;
import Reika.ChromatiCraft.Block.Worldgen.BlockLootChest.TileEntityLootChest;
import Reika.ChromatiCraft.Registry.ChromaBlocks;
import Reika.ChromatiCraft.Registry.CrystalElement;
import Reika.DragonAPI.Instantiable.Data.BlockStruct.FilledBlockArray;
import Reika.DragonAPI.Instantiable.Data.Immutable.Coordinate;


public class CavernStructure extends FragmentStructureWithBonusLoot {

	public CavernStructure() {
		this.addBonusItem(ChromaStacks.purpleShard, 2, 8, 10);
		this.addBonusItem(ChromaStacks.magentaShard, 2, 8, 5);
		this.addBonusItem(ChromaStacks.limeShard, 2, 8, 10);
		this.addBonusItem(new ItemStack(Items.ender_pearl), 1, 4, 20);
		this.addBonusItem(new ItemStack(Items.redstone), 4, 24, 25);
		this.addBonusItem(new ItemStack(Items.diamond), 1, 3, 5);
		this.addBonusItem(new ItemStack(Items.gold_ingot), 1, 8, 10);
		this.addBonusItem(new ItemStack(Items.iron_ingot), 1, 16, 20);
		this.addBonusItem(new ItemStack(Items.quartz), 1, 8, 10);
	}

	@Override
	public Coordinate getControllerRelativeLocation() {
		return new Coordinate(0, 0, 0);
	}

	@Override
	public FilledBlockArray getArray(World world, int x, int y, int z) {
		FilledBlockArray array = new FilledBlockArray(world);

		x -= 7;
		y -= 2; //offset compensation
		z -= 5;

		array.setBlock(x+6, y+1, z+9, ChromaBlocks.RUNE.getBlockInstance(), CrystalElement.RED.ordinal());
		array.setBlock(x+6, y+2, z+9, ChromaBlocks.CRYSTAL.getBlockInstance(), CrystalElement.RED.ordinal());

		array.setBlock(x+12, y+1, z+7, ChromaBlocks.RUNE.getBlockInstance(), CrystalElement.YELLOW.ordinal());
		array.setBlock(x+12, y+2, z+7, ChromaBlocks.CRYSTAL.getBlockInstance(), CrystalElement.YELLOW.ordinal());

		array.setBlock(x+6, y+1, z+1, ChromaBlocks.RUNE.getBlockInstance(), CrystalElement.BLACK.ordinal());
		array.setBlock(x+6, y+2, z+1, ChromaBlocks.CRYSTAL.getBlockInstance(), CrystalElement.BLACK.ordinal());

		array.setBlock(x+8, y+1, z+1, ChromaBlocks.RUNE.getBlockInstance(), CrystalElement.WHITE.ordinal());
		array.setBlock(x+8, y+2, z+1, ChromaBlocks.CRYSTAL.getBlockInstance(), CrystalElement.WHITE.ordinal());

		array.setBlock(x+8, y+1, z+9, ChromaBlocks.RUNE.getBlockInstance(), CrystalElement.BLUE.ordinal());
		array.setBlock(x+8, y+2, z+9, ChromaBlocks.CRYSTAL.getBlockInstance(), CrystalElement.BLUE.ordinal());

		array.setBlock(x+12, y+1, z+3, ChromaBlocks.RUNE.getBlockInstance(), CrystalElement.BROWN.ordinal());
		array.setBlock(x+12, y+2, z+3, ChromaBlocks.CRYSTAL.getBlockInstance(), CrystalElement.BROWN.ordinal());

		array.setBlock(x+1, y+1, z+5, ChromaBlocks.RUNE.getBlockInstance(), CrystalElement.GREEN.ordinal());
		array.setBlock(x+1, y+2, z+5, ChromaBlocks.CRYSTAL.getBlockInstance(), CrystalElement.GREEN.ordinal());

		this.addLootChest(array, x+10, y+1, z+3, ForgeDirection.WEST);
		this.addLootChest(array, x+10, y+1, z+7, ForgeDirection.WEST);

		//Air
		array.setBlock(x, y, z, Blocks.air);
		array.setBlock(x+2, y+1, z+5, Blocks.air);
		array.setBlock(x+2, y+2, z+5, Blocks.air);
		array.setBlock(x+2, y+3, z+5, Blocks.air);
		array.setBlock(x+3, y+1, z+4, Blocks.air);
		array.setBlock(x+3, y+1, z+5, Blocks.air);
		array.setBlock(x+3, y+1, z+6, Blocks.air);
		array.setBlock(x+3, y+2, z+4, Blocks.air);
		array.setBlock(x+3, y+2, z+5, Blocks.air);
		array.setBlock(x+3, y+2, z+6, Blocks.air);
		array.setBlock(x+3, y+3, z+4, Blocks.air);
		array.setBlock(x+3, y+3, z+5, Blocks.air);
		array.setBlock(x+3, y+3, z+6, Blocks.air);
		array.setBlock(x+4, y+1, z+3, Blocks.air);
		array.setBlock(x+4, y+1, z+4, Blocks.air);
		array.setBlock(x+4, y+1, z+5, Blocks.air);
		array.setBlock(x+4, y+1, z+6, Blocks.air);
		array.setBlock(x+4, y+1, z+7, Blocks.air);
		array.setBlock(x+4, y+2, z+3, Blocks.air);
		array.setBlock(x+4, y+2, z+4, Blocks.air);
		array.setBlock(x+4, y+2, z+5, Blocks.air);
		array.setBlock(x+4, y+2, z+6, Blocks.air);
		array.setBlock(x+4, y+2, z+7, Blocks.air);
		array.setBlock(x+4, y+3, z+3, Blocks.air);
		array.setBlock(x+4, y+3, z+4, Blocks.air);
		array.setBlock(x+4, y+3, z+5, Blocks.air);
		array.setBlock(x+4, y+3, z+6, Blocks.air);
		array.setBlock(x+4, y+3, z+7, Blocks.air);
		array.setBlock(x+4, y+4, z+5, Blocks.air);
		array.setBlock(x+5, y+1, z+3, Blocks.air);
		array.setBlock(x+5, y+1, z+4, Blocks.air);
		array.setBlock(x+5, y+1, z+5, Blocks.air);
		array.setBlock(x+5, y+1, z+6, Blocks.air);
		array.setBlock(x+5, y+1, z+7, Blocks.air);
		array.setBlock(x+5, y+2, z+3, Blocks.air);
		array.setBlock(x+5, y+2, z+4, Blocks.air);
		array.setBlock(x+5, y+2, z+5, Blocks.air);
		array.setBlock(x+5, y+2, z+6, Blocks.air);
		array.setBlock(x+5, y+2, z+7, Blocks.air);
		array.setBlock(x+5, y+3, z+3, Blocks.air);
		array.setBlock(x+5, y+3, z+4, Blocks.air);
		array.setBlock(x+5, y+3, z+5, Blocks.air);
		array.setBlock(x+5, y+3, z+6, Blocks.air);
		array.setBlock(x+5, y+3, z+7, Blocks.air);
		array.setBlock(x+5, y+4, z+4, Blocks.air);
		array.setBlock(x+5, y+4, z+5, Blocks.air);
		array.setBlock(x+5, y+4, z+6, Blocks.air);
		array.setBlock(x+6, y+1, z+2, Blocks.air);
		array.setBlock(x+6, y+1, z+3, Blocks.air);
		array.setBlock(x+6, y+1, z+4, Blocks.air);
		array.setBlock(x+6, y+1, z+5, Blocks.air);
		array.setBlock(x+6, y+1, z+6, Blocks.air);
		array.setBlock(x+6, y+1, z+7, Blocks.air);
		array.setBlock(x+6, y+1, z+8, Blocks.air);
		array.setBlock(x+6, y+2, z+2, Blocks.air);
		array.setBlock(x+6, y+2, z+3, Blocks.air);
		array.setBlock(x+6, y+2, z+4, Blocks.air);
		array.setBlock(x+6, y+2, z+5, Blocks.air);
		array.setBlock(x+6, y+2, z+6, Blocks.air);
		array.setBlock(x+6, y+2, z+7, Blocks.air);
		array.setBlock(x+6, y+2, z+8, Blocks.air);
		array.setBlock(x+6, y+3, z+2, Blocks.air);
		array.setBlock(x+6, y+3, z+3, Blocks.air);
		array.setBlock(x+6, y+3, z+4, Blocks.air);
		array.setBlock(x+6, y+3, z+5, Blocks.air);
		array.setBlock(x+6, y+3, z+6, Blocks.air);
		array.setBlock(x+6, y+3, z+7, Blocks.air);
		array.setBlock(x+6, y+3, z+8, Blocks.air);
		array.setBlock(x+6, y+4, z+4, Blocks.air);
		array.setBlock(x+6, y+4, z+5, Blocks.air);
		array.setBlock(x+6, y+4, z+6, Blocks.air);
		array.setBlock(x+7, y+1, z+2, Blocks.air);
		array.setBlock(x+7, y+1, z+3, Blocks.air);
		array.setBlock(x+7, y+1, z+4, Blocks.air);
		array.setBlock(x+7, y+1, z+5, Blocks.air);
		array.setBlock(x+7, y+1, z+6, Blocks.air);
		array.setBlock(x+7, y+1, z+7, Blocks.air);
		array.setBlock(x+7, y+1, z+8, Blocks.air);
		array.setBlock(x+7, y+2, z+2, Blocks.air);
		array.setBlock(x+7, y+2, z+3, Blocks.air);
		array.setBlock(x+7, y+2, z+4, Blocks.air);
		array.setBlock(x+7, y+2, z+6, Blocks.air);
		array.setBlock(x+7, y+2, z+7, Blocks.air);
		array.setBlock(x+7, y+2, z+8, Blocks.air);
		array.setBlock(x+7, y+3, z+2, Blocks.air);
		array.setBlock(x+7, y+3, z+3, Blocks.air);
		array.setBlock(x+7, y+3, z+4, Blocks.air);
		array.setBlock(x+7, y+3, z+5, Blocks.air);
		array.setBlock(x+7, y+3, z+6, Blocks.air);
		array.setBlock(x+7, y+3, z+7, Blocks.air);
		array.setBlock(x+7, y+3, z+8, Blocks.air);
		array.setBlock(x+7, y+4, z+4, Blocks.air);
		array.setBlock(x+7, y+4, z+5, Blocks.air);
		array.setBlock(x+7, y+4, z+6, Blocks.air);
		array.setBlock(x+8, y+1, z+2, Blocks.air);
		array.setBlock(x+8, y+1, z+3, Blocks.air);
		array.setBlock(x+8, y+1, z+4, Blocks.air);
		array.setBlock(x+8, y+1, z+5, Blocks.air);
		array.setBlock(x+8, y+1, z+6, Blocks.air);
		array.setBlock(x+8, y+1, z+7, Blocks.air);
		array.setBlock(x+8, y+1, z+8, Blocks.air);
		array.setBlock(x+8, y+2, z+2, Blocks.air);
		array.setBlock(x+8, y+2, z+3, Blocks.air);
		array.setBlock(x+8, y+2, z+4, Blocks.air);
		array.setBlock(x+8, y+2, z+5, Blocks.air);
		array.setBlock(x+8, y+2, z+6, Blocks.air);
		array.setBlock(x+8, y+2, z+7, Blocks.air);
		array.setBlock(x+8, y+2, z+8, Blocks.air);
		array.setBlock(x+8, y+3, z+2, Blocks.air);
		array.setBlock(x+8, y+3, z+3, Blocks.air);
		array.setBlock(x+8, y+3, z+4, Blocks.air);
		array.setBlock(x+8, y+3, z+5, Blocks.air);
		array.setBlock(x+8, y+3, z+6, Blocks.air);
		array.setBlock(x+8, y+3, z+7, Blocks.air);
		array.setBlock(x+8, y+3, z+8, Blocks.air);
		array.setBlock(x+8, y+4, z+4, Blocks.air);
		array.setBlock(x+8, y+4, z+5, Blocks.air);
		array.setBlock(x+8, y+4, z+6, Blocks.air);
		array.setBlock(x+9, y+1, z+3, Blocks.air);
		array.setBlock(x+9, y+1, z+4, Blocks.air);
		array.setBlock(x+9, y+1, z+5, Blocks.air);
		array.setBlock(x+9, y+1, z+6, Blocks.air);
		array.setBlock(x+9, y+1, z+7, Blocks.air);
		array.setBlock(x+9, y+2, z+3, Blocks.air);
		array.setBlock(x+9, y+2, z+4, Blocks.air);
		array.setBlock(x+9, y+2, z+5, Blocks.air);
		array.setBlock(x+9, y+2, z+6, Blocks.air);
		array.setBlock(x+9, y+2, z+7, Blocks.air);
		array.setBlock(x+9, y+3, z+3, Blocks.air);
		array.setBlock(x+9, y+3, z+4, Blocks.air);
		array.setBlock(x+9, y+3, z+5, Blocks.air);
		array.setBlock(x+9, y+3, z+6, Blocks.air);
		array.setBlock(x+9, y+3, z+7, Blocks.air);
		array.setBlock(x+9, y+4, z+4, Blocks.air);
		array.setBlock(x+9, y+4, z+5, Blocks.air);
		array.setBlock(x+9, y+4, z+6, Blocks.air);
		array.setBlock(x+10, y+1, z+4, Blocks.air);
		array.setBlock(x+10, y+1, z+5, Blocks.air);
		array.setBlock(x+10, y+1, z+6, Blocks.air);
		array.setBlock(x+10, y+2, z+3, Blocks.air);
		array.setBlock(x+10, y+2, z+4, Blocks.air);
		array.setBlock(x+10, y+2, z+5, Blocks.air);
		array.setBlock(x+10, y+2, z+6, Blocks.air);
		array.setBlock(x+10, y+2, z+7, Blocks.air);
		array.setBlock(x+10, y+3, z+3, Blocks.air);
		array.setBlock(x+10, y+3, z+4, Blocks.air);
		array.setBlock(x+10, y+3, z+5, Blocks.air);
		array.setBlock(x+10, y+3, z+6, Blocks.air);
		array.setBlock(x+10, y+3, z+7, Blocks.air);
		array.setBlock(x+10, y+4, z+5, Blocks.air);
		array.setBlock(x+11, y+1, z+4, Blocks.air);
		array.setBlock(x+11, y+1, z+5, Blocks.air);
		array.setBlock(x+11, y+1, z+6, Blocks.air);
		array.setBlock(x+11, y+2, z+4, Blocks.air);
		array.setBlock(x+11, y+2, z+5, Blocks.air);
		array.setBlock(x+11, y+2, z+6, Blocks.air);
		array.setBlock(x+11, y+3, z+4, Blocks.air);
		array.setBlock(x+11, y+3, z+5, Blocks.air);
		array.setBlock(x+11, y+3, z+6, Blocks.air);
		array.setBlock(x+12, y+1, z+4, Blocks.air);
		array.setBlock(x+12, y+1, z+5, Blocks.air);
		array.setBlock(x+12, y+1, z+6, Blocks.air);
		array.setBlock(x+12, y+2, z+4, Blocks.air);
		array.setBlock(x+12, y+2, z+5, Blocks.air);
		array.setBlock(x+12, y+2, z+6, Blocks.air);
		array.setBlock(x+12, y+3, z+4, Blocks.air);
		array.setBlock(x+12, y+3, z+5, Blocks.air);
		array.setBlock(x+12, y+3, z+6, Blocks.air);
		array.setBlock(x+13, y+1, z+5, Blocks.air);
		array.setBlock(x+13, y+2, z+5, Blocks.air);

		//Shielding
		array.setBlock(x+0, y+1, z+5, shield, 8);
		array.setBlock(x+0, y+2, z+5, shield, 8);
		array.setBlock(x+1, y+0, z+5, shield, 8);
		array.setBlock(x+1, y+1, z+4, shield, 8);
		array.setBlock(x+1, y+1, z+6, shield, 8);
		array.setBlock(x+1, y+2, z+4, shield, 8);
		array.setBlock(x+1, y+2, z+6, shield, 8);
		array.setBlock(x+1, y+3, z+5, shield, 8);
		array.setBlock(x+2, y+0, z+5, shield, 8);
		array.setBlock(x+2, y+1, z+4, shield, 8);
		array.setBlock(x+2, y+1, z+6, shield, 8);
		array.setBlock(x+2, y+2, z+4, shield, 8);
		array.setBlock(x+2, y+2, z+6, shield, 8);
		array.setBlock(x+2, y+3, z+4, shield, 8);
		array.setBlock(x+2, y+3, z+6, shield, 8);
		array.setBlock(x+2, y+4, z+5, shield, 8);
		array.setBlock(x+3, y+0, z+4, shield, 8);
		array.setBlock(x+3, y+0, z+5, shield, 8);
		array.setBlock(x+3, y+0, z+6, shield, 8);
		array.setBlock(x+3, y+1, z+3, shield, 8);
		array.setBlock(x+3, y+1, z+7, shield, 8);
		array.setBlock(x+3, y+2, z+3, shield, 8);
		array.setBlock(x+3, y+2, z+7, shield, 8);
		array.setBlock(x+3, y+3, z+3, shield, 8);
		array.setBlock(x+3, y+3, z+7, shield, 8);
		array.setBlock(x+3, y+4, z+4, shield, 8);
		array.setBlock(x+3, y+4, z+5, shield, 8);
		array.setBlock(x+3, y+4, z+6, shield, 8);
		array.setBlock(x+4, y+0, z+3, shield, 8);
		array.setBlock(x+4, y+0, z+4, shield, 8);
		array.setBlock(x+4, y+0, z+5, shield, 8);
		array.setBlock(x+4, y+0, z+6, shield, 8);
		array.setBlock(x+4, y+0, z+7, shield, 8);
		array.setBlock(x+4, y+1, z+2, shield, 8);
		array.setBlock(x+4, y+1, z+8, shield, 8);
		array.setBlock(x+4, y+2, z+2, shield, 8);
		array.setBlock(x+4, y+2, z+8, shield, 8);
		array.setBlock(x+4, y+3, z+2, shield, 8);
		array.setBlock(x+4, y+3, z+8, shield, 8);
		array.setBlock(x+4, y+4, z+3, shield, 8);
		array.setBlock(x+4, y+4, z+4, shield, 8);
		array.setBlock(x+4, y+4, z+6, shield, 8);
		array.setBlock(x+4, y+4, z+7, shield, 8);
		array.setBlock(x+4, y+5, z+5, shield, 8);
		array.setBlock(x+5, y+0, z+3, shield, 8);
		array.setBlock(x+5, y+0, z+4, shield, 8);
		array.setBlock(x+5, y+0, z+5, shield, 8);
		array.setBlock(x+5, y+0, z+6, shield, 8);
		array.setBlock(x+5, y+0, z+7, shield, 8);
		array.setBlock(x+5, y+1, z+1, shield, 8);
		array.setBlock(x+5, y+1, z+2, shield, 8);
		array.setBlock(x+5, y+1, z+8, shield, 8);
		array.setBlock(x+5, y+1, z+9, shield, 8);
		array.setBlock(x+5, y+2, z+1, shield, 8);
		array.setBlock(x+5, y+2, z+2, shield, 8);
		array.setBlock(x+5, y+2, z+8, shield, 8);
		array.setBlock(x+5, y+2, z+9, shield, 8);
		array.setBlock(x+5, y+3, z+2, shield, 8);
		array.setBlock(x+5, y+3, z+8, shield, 8);
		array.setBlock(x+5, y+4, z+3, shield, 8);
		array.setBlock(x+5, y+4, z+7, shield, 8);
		array.setBlock(x+5, y+5, z+4, shield, 8);
		array.setBlock(x+5, y+5, z+5, shield, 8);
		array.setBlock(x+5, y+5, z+6, shield, 8);
		array.setBlock(x+6, y+0, z+1, shield, 8);
		array.setBlock(x+6, y+0, z+2, shield, 8);
		array.setBlock(x+6, y+0, z+3, shield, 8);
		array.setBlock(x+6, y+0, z+4, shield, 8);
		array.setBlock(x+6, y+0, z+5, shield, 8);
		array.setBlock(x+6, y+0, z+6, shield, 8);
		array.setBlock(x+6, y+0, z+7, shield, 8);
		array.setBlock(x+6, y+0, z+8, shield, 8);
		array.setBlock(x+6, y+0, z+9, shield, 8);
		array.setBlock(x+6, y+1, z+0, shield, 8);
		array.setBlock(x+6, y+1, z+10, shield, 8);
		array.setBlock(x+6, y+2, z+0, shield, 8);
		array.setBlock(x+6, y+2, z+10, shield, 8);
		array.setBlock(x+6, y+3, z+1, shield, 8);
		array.setBlock(x+6, y+3, z+9, shield, 8);
		array.setBlock(x+6, y+4, z+2, shield, 8);
		array.setBlock(x+6, y+4, z+3, shield, 8);
		array.setBlock(x+6, y+4, z+7, shield, 8);
		array.setBlock(x+6, y+4, z+8, shield, 8);
		array.setBlock(x+6, y+5, z+4, shield, 8);
		array.setBlock(x+6, y+5, z+5, shield, 8);
		array.setBlock(x+6, y+5, z+6, shield, 8);
		array.setBlock(x+7, y+0, z+2, shield, 8);
		array.setBlock(x+7, y+0, z+3, shield, 8);
		array.setBlock(x+7, y+0, z+4, shield, 8);
		array.setBlock(x+7, y+0, z+5, shield, 8);
		array.setBlock(x+7, y+0, z+6, shield, 8);
		array.setBlock(x+7, y+0, z+7, shield, 8);
		array.setBlock(x+7, y+0, z+8, shield, 8);
		array.setBlock(x+7, y+1, z+1, shield, 8);
		array.setBlock(x+7, y+1, z+9, shield, 8);
		array.setBlock(x+7, y+2, z+1, shield, 8);
		array.setBlock(x+7, y+2, z+9, shield, 8);
		array.setBlock(x+7, y+3, z+1, shield, 8);
		array.setBlock(x+7, y+3, z+9, shield, 8);
		array.setBlock(x+7, y+4, z+2, shield, 8);
		array.setBlock(x+7, y+4, z+3, shield, 8);
		array.setBlock(x+7, y+4, z+7, shield, 8);
		array.setBlock(x+7, y+4, z+8, shield, 8);
		array.setBlock(x+7, y+5, z+4, shield, 8);
		array.setBlock(x+7, y+5, z+5, shield, 8);
		array.setBlock(x+7, y+5, z+6, shield, 8);
		array.setBlock(x+8, y+0, z+1, shield, 8);
		array.setBlock(x+8, y+0, z+2, shield, 8);
		array.setBlock(x+8, y+0, z+3, shield, 8);
		array.setBlock(x+8, y+0, z+4, shield, 8);
		array.setBlock(x+8, y+0, z+5, shield, 8);
		array.setBlock(x+8, y+0, z+6, shield, 8);
		array.setBlock(x+8, y+0, z+7, shield, 8);
		array.setBlock(x+8, y+0, z+8, shield, 8);
		array.setBlock(x+8, y+0, z+9, shield, 8);
		array.setBlock(x+8, y+1, z+0, shield, 8);
		array.setBlock(x+8, y+1, z+10, shield, 8);
		array.setBlock(x+8, y+2, z+0, shield, 8);
		array.setBlock(x+8, y+2, z+10, shield, 8);
		array.setBlock(x+8, y+3, z+1, shield, 8);
		array.setBlock(x+8, y+3, z+9, shield, 8);
		array.setBlock(x+8, y+4, z+2, shield, 8);
		array.setBlock(x+8, y+4, z+3, shield, 8);
		array.setBlock(x+8, y+4, z+7, shield, 8);
		array.setBlock(x+8, y+4, z+8, shield, 8);
		array.setBlock(x+8, y+5, z+4, shield, 8);
		array.setBlock(x+8, y+5, z+5, shield, 8);
		array.setBlock(x+8, y+5, z+6, shield, 8);
		array.setBlock(x+9, y+0, z+3, shield, 8);
		array.setBlock(x+9, y+0, z+4, shield, 8);
		array.setBlock(x+9, y+0, z+5, shield, 8);
		array.setBlock(x+9, y+0, z+6, shield, 8);
		array.setBlock(x+9, y+0, z+7, shield, 8);
		array.setBlock(x+9, y+1, z+1, shield, 8);
		array.setBlock(x+9, y+1, z+2, shield, 8);
		array.setBlock(x+9, y+1, z+8, shield, 8);
		array.setBlock(x+9, y+1, z+9, shield, 8);
		array.setBlock(x+9, y+2, z+1, shield, 8);
		array.setBlock(x+9, y+2, z+2, shield, 8);
		array.setBlock(x+9, y+2, z+8, shield, 8);
		array.setBlock(x+9, y+2, z+9, shield, 8);
		array.setBlock(x+9, y+3, z+2, shield, 8);
		array.setBlock(x+9, y+3, z+8, shield, 8);
		array.setBlock(x+9, y+4, z+3, shield, 8);
		array.setBlock(x+9, y+4, z+7, shield, 8);
		array.setBlock(x+9, y+5, z+4, shield, 8);
		array.setBlock(x+9, y+5, z+5, shield, 8);
		array.setBlock(x+9, y+5, z+6, shield, 8);
		array.setBlock(x+10, y+0, z+3, shield, 8);
		array.setBlock(x+10, y+0, z+4, shield, 8);
		array.setBlock(x+10, y+0, z+5, shield, 8);
		array.setBlock(x+10, y+0, z+6, shield, 8);
		array.setBlock(x+10, y+0, z+7, shield, 8);
		array.setBlock(x+10, y+1, z+2, shield, 8);
		array.setBlock(x+10, y+1, z+8, shield, 8);
		array.setBlock(x+10, y+2, z+2, shield, 8);
		array.setBlock(x+10, y+2, z+8, shield, 8);
		array.setBlock(x+10, y+3, z+2, shield, 8);
		array.setBlock(x+10, y+3, z+8, shield, 8);
		array.setBlock(x+10, y+4, z+3, shield, 8);
		array.setBlock(x+10, y+4, z+4, shield, 8);
		array.setBlock(x+10, y+4, z+6, shield, 8);
		array.setBlock(x+10, y+4, z+7, shield, 8);
		array.setBlock(x+10, y+5, z+5, shield, 8);
		array.setBlock(x+11, y+0, z+4, shield, 8);
		array.setBlock(x+11, y+0, z+5, shield, 8);
		array.setBlock(x+11, y+0, z+6, shield, 8);
		array.setBlock(x+11, y+1, z+3, shield, 8);
		array.setBlock(x+11, y+1, z+7, shield, 8);
		array.setBlock(x+11, y+2, z+3, shield, 8);
		array.setBlock(x+11, y+2, z+7, shield, 8);
		array.setBlock(x+11, y+3, z+3, shield, 8);
		array.setBlock(x+11, y+3, z+7, shield, 8);
		array.setBlock(x+11, y+4, z+4, shield, 8);
		array.setBlock(x+11, y+4, z+5, shield, 8);
		array.setBlock(x+11, y+4, z+6, shield, 8);
		array.setBlock(x+12, y+0, z+3, shield, 8);
		array.setBlock(x+12, y+0, z+4, shield, 8);
		array.setBlock(x+12, y+0, z+5, shield, 8);
		array.setBlock(x+12, y+0, z+6, shield, 8);
		array.setBlock(x+12, y+0, z+7, shield, 8);
		array.setBlock(x+12, y+1, z+2, shield, 8);
		array.setBlock(x+12, y+1, z+8, shield, 8);
		array.setBlock(x+12, y+2, z+2, shield, 8);
		array.setBlock(x+12, y+2, z+8, shield, 8);
		array.setBlock(x+12, y+3, z+3, shield, 8);
		array.setBlock(x+12, y+3, z+7, shield, 8);
		array.setBlock(x+12, y+4, z+4, shield, 8);
		array.setBlock(x+12, y+4, z+5, shield, 8);
		array.setBlock(x+12, y+4, z+6, shield, 8);
		array.setBlock(x+13, y+0, z+5, shield, 8);
		array.setBlock(x+13, y+1, z+3, shield, 8);
		array.setBlock(x+13, y+1, z+4, shield, 8);
		array.setBlock(x+13, y+1, z+6, shield, 8);
		array.setBlock(x+13, y+1, z+7, shield, 8);
		array.setBlock(x+13, y+2, z+3, shield, 8);
		array.setBlock(x+13, y+2, z+4, shield, 8);
		array.setBlock(x+13, y+2, z+6, shield, 8);
		array.setBlock(x+13, y+2, z+7, shield, 8);
		array.setBlock(x+13, y+3, z+4, shield, 8);
		array.setBlock(x+13, y+3, z+5, shield, 8);
		array.setBlock(x+13, y+3, z+6, shield, 8);
		return array;
	}

	@Override
	public int getStructureVersion() {
		return 0;
	}

	@Override
	public int getChestYield(Coordinate c, TileEntityLootChest te, FilledBlockArray arr, Random r) {
		return 1;
	}

	@Override
	public String getChestLootTable(Coordinate c, TileEntityLootChest te, FilledBlockArray arr, Random r) {
		return ChestGenHooks.DUNGEON_CHEST;
	}

	@Override
	public int getFragmentCount(TileEntityLootChest te, String s, int bonus, Random r) {
		return r.nextInt(5) > 0 ? 2 : 1;
	}

}
