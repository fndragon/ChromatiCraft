/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.TileEntity.AOE.Effect;

import java.util.HashMap;

import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import Reika.ChromatiCraft.Base.TileEntity.TileEntityAdjacencyUpgrade;
import Reika.ChromatiCraft.Registry.CrystalElement;
import Reika.DragonAPI.ModList;
import Reika.DragonAPI.ASM.APIStripper.Strippable;
import Reika.DragonAPI.ASM.DependentMethodStripper.ModDependent;
import Reika.DragonAPI.Instantiable.Data.Immutable.BlockKey;
import Reika.DragonAPI.Instantiable.Data.Maps.BlockMap;
import Reika.DragonAPI.Interfaces.TileEntity.ThermalTile;
import Reika.ReactorCraft.Registry.ReactorTiles;
import Reika.ReactorCraft.TileEntities.Processing.TileEntityElectrolyzer;
import Reika.ReactorCraft.TileEntities.Processing.TileEntitySynthesizer;
import Reika.RotaryCraft.TileEntities.Farming.TileEntityComposter;
import Reika.RotaryCraft.TileEntities.Processing.TileEntityCompactor;
import Reika.RotaryCraft.TileEntities.Processing.TileEntityCrystallizer;
import Reika.RotaryCraft.TileEntities.Production.TileEntityBlastFurnace;
import Reika.RotaryCraft.TileEntities.Production.TileEntityFermenter;

import ic2.api.energy.tile.IHeatSource;

@Strippable(value = "ic2.api.energy.tile.IHeatSource")
public class TileEntityHeatRelay extends TileEntityAdjacencyUpgrade implements IHeatSource {

	private static final BlockMap<Integer> blockTemps = new BlockMap();
	private static final HashMap<String, Integer> tileList = new HashMap();

	private static final int machineHeat = 0;
	private static final int tieredMachineHeat = 1;
	private static final int reactorHeat = 10;

	private static final double[] factors = {
			0.03125,
			0.0625,
			0.125,
			0.25,
			0.375,
			0.5,
			0.75,
			1,
	};

	private static final int[] ic2HeatMax = {
			2,
			5,
			10,
			20,
			50,
			100,
			250,
			1000,
	};

	private static final double[] ic2HeatEff = {
			0.1,
			0.25,
			0.5,
			1,
			1.5,
			2.5,
			5,
			10,
	};

	static {
		blockTemps.put(Blocks.ice, 0);
		blockTemps.put(Blocks.water, 15);
		blockTemps.put(Blocks.fire, 90);

		if (ModList.ROTARYCRAFT.isLoaded()) {
			addRC();
			if (ModList.REACTORCRAFT.isLoaded()) {
				addReC();
			}
		}
	}

	@ModDependent(ModList.ROTARYCRAFT)
	private static void addRC() {
		tileList.put(TileEntityBlastFurnace.class.getName(), tieredMachineHeat);
		tileList.put(TileEntityCompactor.class.getName(), machineHeat);
		tileList.put(TileEntityComposter.class.getName(), machineHeat);
		tileList.put(TileEntityCrystallizer.class.getName(), machineHeat);
		tileList.put(TileEntityFermenter.class.getName(), machineHeat);
	}

	@ModDependent(ModList.REACTORCRAFT)
	private static void addReC() {
		for (int i = 0; i < ReactorTiles.TEList.length; i++) {
			ReactorTiles r = ReactorTiles.TEList[i];
			if (r.isReactorCore()) {
				tileList.put(r.getTEClass().getName(), reactorHeat);
			}
		}

		tileList.put(TileEntitySynthesizer.class.getName(), machineHeat);
		tileList.put(TileEntityElectrolyzer.class.getName(), machineHeat);
	}

	@Override
	protected boolean ticksIndividually() {
		return false;
	}

	@Override
	protected void doCollectiveTick(World world, int x, int y, int z) {
		int Tavg = 0;
		int n = 0;
		HashMap<ThermalTile, Integer> set = new HashMap();
		int tierThisCycle = Integer.MAX_VALUE;
		double f = this.getEqualizationFactor();
		for (int i = 0; i < 6; i++) {
			BlockKey bk = BlockKey.getAt(world, x+dirs[i].offsetX, y+dirs[i].offsetY, z+dirs[i].offsetZ);
			TileEntity te = this.getEffectiveTileOnSide(dirs[i]);
			if (te instanceof ThermalTile && tileList.containsKey(te.getClass().getName())) {
				n++;
				int tier = tileList.get(te.getClass().getName());
				set.put((ThermalTile)te, tier);
				tierThisCycle = Math.min(tierThisCycle, tier);
				Tavg += ((ThermalTile)te).getTemperature();
			}
			else if (blockTemps.get(bk) != null) {
				n++;
				Tavg += blockTemps.get(bk);
			}
		}
		if (n <= 1)
			return;
		Tavg = Tavg/n;
		for (ThermalTile te : set.keySet()) {
			int hr = set.get(te);
			if (hr <= tierThisCycle) {
				int t1 = te.getTemperature();
				int t2 = (int)(this.getEqualizationFactor()*Tavg+(1-this.getEqualizationFactor())*t1);
				te.setTemperature(t2);
			}
		}
	}

	private double getEqualizationFactor() {
		return factors[this.getTier()];
	}

	@Override
	protected EffectResult tickDirection(World world, int x, int y, int z, ForgeDirection dir, long startTime) {
		return EffectResult.STOP;
	}

	@Override
	public CrystalElement getColor() {
		return CrystalElement.ORANGE;
	}

	@Override
	protected void animateWithTick(World world, int x, int y, int z) {

	}

	public static double getFactor(int tier) {
		return factors[tier];
	}

	@Override
	@ModDependent(ModList.IC2)
	public int maxrequestHeatTick(ForgeDirection dir) {
		return !this.hasRedstoneSignal() && this.hasSufficientEnergy() ? ic2HeatMax[this.getTier()] : 0;
	}

	@Override
	@ModDependent(ModList.IC2)
	public int requestHeat(ForgeDirection dir, int amt) {
		return this.collectHeat(dir, Math.min(this.maxrequestHeatTick(dir), amt));
	}

	@ModDependent(ModList.IC2)
	private int collectHeat(ForgeDirection skip, int seek) {
		if (seek <= 0)
			return 0;
		//ReikaJavaLibrary.pConsole(this.getWorldObj().getTotalWorldTime()+": "+seek+" from "+skip+" = "+this.getAdjacentTileEntity(skip));
		int has = 0;
		for (int i = 0; i < 6; i++) {
			ForgeDirection dir = dirs[i];
			if (dir == skip)
				continue;
			TileEntity te = this.getEffectiveTileOnSide(dir);
			if (te instanceof IHeatSource) {
				ForgeDirection opp = dir.getOpposite();
				IHeatSource hs = (IHeatSource)te;
				int max = hs.maxrequestHeatTick(opp);
				int take = hs.requestHeat(opp, Math.min(max, seek));
				if (take > 0) {
					//ReikaJavaLibrary.pConsole("Took "+take+" from "+dir+" = "+te);
					seek -= take;
					has += take;
					if (seek == 0)
						break;
				}
			}
		}
		//ReikaJavaLibrary.pConsole(this.getWorldObj().getTotalWorldTime()+": got "+(int)(has*ic2HeatEff[this.getTier()]));
		return (int)(has*ic2HeatEff[this.getTier()]);
	}

	public static int getIc2Max(int tier) {
		return ic2HeatMax[tier];
	}

	public static double getIc2Eff(int tier) {
		return ic2HeatEff[tier];
	}

}
