/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.API.Interfaces;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import Reika.DragonAPI.Instantiable.Data.Immutable.WorldLocation;

/** The world rift implements this; this is a hook for you to make your systems interact with the rift accordingly. For a sample implementation,
 * look to RotaryCraft shaft power distribution or ElectriCraft wire network pathfinding logic. For all directional functions, the return value
 * will be the appropriate object on that side of the world rift's other "end". So calling getBlockIDFrom(ForgeDirection.EAST) on a rift,
 * no matter its location, linked to another at 300, 50, 100 in the Nether returns the block at 301, 50, 100 in the Nether, the block east of the other rift.
 *
 *  Rifts are two-directional. */
public interface WorldRift {

	/** Direction is the side it is relative to you, NOT the side of it you are asking! */
	public Block getBlockIDFrom(ForgeDirection dir);
	/** Direction is the side it is relative to you, NOT the side of it you are asking! */
	public int getBlockMetadataFrom(ForgeDirection dir);
	/** Direction is the side it is relative to you, NOT the side of it you are asking! */
	public TileEntity getTileEntityFrom(ForgeDirection dir);

	/** Returns the location of the other rift. */
	public WorldLocation getLinkTarget();

}
