/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2017
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.Magic.Network;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import Reika.ChromatiCraft.ChromatiCraft;
import Reika.ChromatiCraft.Auxiliary.CrystalNetworkLogger;
import Reika.ChromatiCraft.Auxiliary.CrystalNetworkLogger.LoggingLevel;
import Reika.ChromatiCraft.Magic.Interfaces.CrystalNetworkTile;
import Reika.ChromatiCraft.Magic.Interfaces.CrystalReceiver;
import Reika.ChromatiCraft.Magic.Interfaces.CrystalRepeater;
import Reika.ChromatiCraft.Magic.Interfaces.CrystalSource;
import Reika.ChromatiCraft.Magic.Interfaces.CrystalTransmitter;
import Reika.ChromatiCraft.Magic.Interfaces.WrapperTile;
import Reika.ChromatiCraft.Magic.Network.NetworkSorters.TransmitterDistanceSorter;
import Reika.ChromatiCraft.ModInterface.ThaumCraft.NodeReceiverWrapper;
import Reika.ChromatiCraft.ModInterface.ThaumCraft.NodeRecharger;
import Reika.ChromatiCraft.Registry.ChromaBlocks;
import Reika.ChromatiCraft.Registry.ChromaOptions;
import Reika.ChromatiCraft.Registry.CrystalElement;
import Reika.ChromatiCraft.TileEntity.Decoration.TileEntityCrystalMusic;
import Reika.ChromatiCraft.TileEntity.Networking.TileEntityCrystalPylon;
import Reika.ChromatiCraft.TileEntity.Networking.TileEntitySkypeater;
import Reika.ChromatiCraft.TileEntity.Networking.TileEntitySkypeater.NodeClass;
import Reika.ChromatiCraft.World.IWG.PylonGenerator;
import Reika.DragonAPI.ModList;
import Reika.DragonAPI.ASM.DependentMethodStripper.ModDependent;
import Reika.DragonAPI.Auxiliary.ModularLogger;
import Reika.DragonAPI.Instantiable.RayTracer;
import Reika.DragonAPI.Instantiable.Data.Immutable.WorldLocation;
import Reika.DragonAPI.Instantiable.Data.Maps.MultiMap;
import Reika.DragonAPI.ModInteract.ItemHandlers.ExtraUtilsHandler;
import Reika.DragonAPI.ModInteract.ItemHandlers.TinkerBlockHandler;
import Reika.DragonAPI.ModRegistry.InterfaceCache;
import Reika.GeoStrata.Registry.GeoBlocks;
import Reika.RotaryCraft.Registry.BlockRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class PylonFinder {

	public static final String LOGGER_ID = "PylonFinder";

	private final LinkedList<WorldLocation> nodes = new LinkedList();
	private final Collection<WorldLocation> blacklist = new HashSet();
	private final MultiMap<WorldLocation, WorldLocation> duplicates = new MultiMap(new MultiMap.HashSetFactory());

	private final CrystalNetworker net;

	private static final RayTracer tracer;

	//private final int stepRange;
	private final CrystalReceiver target;
	private final CrystalElement element;
	private final EntityPlayer user;

	private int maxSteps = Integer.MAX_VALUE;
	private int steps = 0;
	private int stepsThisTick = 0;
	private boolean suspended = false;
	public static final int MAX_STEPS_PER_TICK = 1000;
	private final boolean isValidWorld;

	private NodeClass skypeaterEntry;
	private NodeClass lastSkypeaterType;

	private static boolean invalid = false;

	private static final HashMap<WorldLocation, EnumMap<CrystalElement, ArrayList<CrystalPath>>> paths = new HashMap();

	//private final HashMap<ChunkCoordIntPair, ChunkCopy> chunkCache = new HashMap();

	static {
		ModularLogger.instance.addLogger(ChromatiCraft.instance, LOGGER_ID);
	}

	PylonFinder(CrystalElement e, CrystalReceiver r, EntityPlayer ep) {
		element = e;
		target = r;
		//stepRange = r;
		net = CrystalNetworker.instance;
		blacklist.add(this.getLocation(r));
		user = ep;
		isValidWorld = r.getWorld().provider.dimensionId == 0 || PylonGenerator.instance.canGenerateIn(r.getWorld());
	}

	CrystalPath findPylon() {
		return this.findPylonWith(0);
	}

	CrystalPath findPylonWith(int thresh) {
		if (!isValidWorld)
			return null;
		invalid = false;
		CrystalPath p = this.checkExistingPaths(thresh);
		//ReikaJavaLibrary.pConsole(p != null ? p.nodes.size() : "null", Side.SERVER);
		if (p != null)
			return p;
		if (!this.anyConnectedSources())
			return null;

		this.findFrom(target, thresh);
		//ReikaJavaLibrary.pConsole(this.toString());
		if (this.isComplete()) {
			ArrayList<WorldLocation> li = new ArrayList(nodes);
			CrystalPath path = new CrystalPath(net, !(target instanceof WrapperTile), element, CrystalPath.cleanRoute(net, element, li));
			if (!(target instanceof WrapperTile))
				this.addValidPath(path);
			return path;
		}
		return null;
	}

	CrystalFlow findPylon(int amount, int maxthru) {
		return this.findPylon(amount, maxthru, 0);
	}

	CrystalFlow findPylon(int amount, int maxthru, int thresh) {
		if (!isValidWorld)
			return null;
		invalid = false;
		CrystalPath p = this.checkExistingPaths(thresh);
		//ReikaJavaLibrary.pConsole(element+" to "+this.getLocation(target)+": "+p);
		if (p != null)
			return new CrystalFlow(net, p, target, amount, maxthru);
		if (!this.anyConnectedSources())
			return null;
		this.findFrom(target, thresh);
		//ReikaJavaLibrary.pConsole(this.toString());
		if (this.isComplete()) {
			ArrayList<WorldLocation> li = new ArrayList(nodes);
			if (lastSkypeaterType != null) {
				this.optimizeSkypeaterRoute(li);
			}
			CrystalFlow flow = new CrystalFlow(net, target, element, amount, CrystalPath.cleanRoute(net, element, li), maxthru);
			//ReikaJavaLibrary.pConsole(flow.checkLineOfSight()+":"+flow);
			if (!(target instanceof WrapperTile))
				this.addValidPath(flow.asPath());
			return flow;
		}
		return null;
	}

	private void optimizeSkypeaterRoute(ArrayList<WorldLocation> path) {
		this.optimizeRoute(net, path, Integer.MAX_VALUE, new JumpOptimizationCheck() {

			@Override
			public boolean canDirectLink(CrystalNetworkTile t1, CrystalNetworkTile t2) {
				return t1 instanceof TileEntitySkypeater && t2 instanceof TileEntitySkypeater;
			}});
	}

	static boolean optimizeRoute(CrystalNetworker net, ArrayList<WorldLocation> path, int nsteps, JumpOptimizationCheck joc) {
		ArrayList<WorldLocation> li = new ArrayList(path);
		boolean flag = true;
		int cycles = 0;
		boolean limit = false;
		while (flag) {
			flag = false;
			for (int i = 0; i < li.size() && !flag; i++) {
				for (int k = i; k < li.size() && !flag; k++) {
					if (i < k && Math.abs(i-k) > 1) {
						WorldLocation loc = li.get(i);
						WorldLocation loc2 = li.get(k);
						CrystalNetworkTile te1 = getNetTileAt(loc, true);
						CrystalNetworkTile te2 = getNetTileAt(loc2, true);
						if (te1 instanceof CrystalReceiver && te2 instanceof CrystalTransmitter && joc.canDirectLink(te1, te2)) {
							double d = ((CrystalReceiver)te1).getReceiveRange();
							if (te1.getDistanceSqTo(te2.getX(), te2.getY(), te2.getZ()) <= d*d && net.checkLOS((CrystalTransmitter)te2, (CrystalReceiver)te1)) {
								while (k > i+1) {
									li.remove(k-1);
									k--;
								}
								flag = true;
								cycles++;
							}
						}
					}
				}
			}
			if (cycles >= nsteps) {
				flag = false;
				limit = true;
			}
		}
		path.clear();
		path.addAll(li);
		return !limit;
	}
	/*
	private boolean anyConnectedSources() {
		return net.activeSourceCount(element) != 0;
	}*/


	private boolean anyConnectedSources() {
		if (net.size() > 100) //slower than just pathfinding
			return true;
		ArrayList<CrystalSource> li = net.getAllSourcesFor(element, true);
		if (ModularLogger.instance.isEnabled(LOGGER_ID))
			ModularLogger.instance.log(LOGGER_ID, "Found "+li.size()+" sources for "+element+": "+li);
		for (CrystalSource s : li) {
			if (this.isSourceConnected(s))
				return true;
		}
		return false;
	}


	private boolean isSourceConnected(CrystalSource s) {
		return target instanceof WrapperTile || !net.getNearbyReceivers(s, element).isEmpty();
	}

	private CrystalPath checkExistingPaths(int thresh) {
		EnumMap<CrystalElement, ArrayList<CrystalPath>> map = paths.get(getLocation(target));
		if (map != null) {
			ArrayList<CrystalPath> c = map.get(element);
			if (c != null) {
				Iterator<CrystalPath> it = c.iterator();
				while (it.hasNext()) {
					CrystalPath p = it.next();
					if (!p.stillValid()) {
						//ReikaJavaLibrary.pConsole("rem "+p, Side.SERVER);
						it.remove();
					}
					else
						return p;
				}
			}
		}
		return null;
	}

	private void addValidPath(CrystalPath p) {
		EnumMap<CrystalElement, ArrayList<CrystalPath>> map = paths.get(p.origin);
		if (map == null) {
			ArrayList<CrystalPath> c = new ArrayList();
			map = new EnumMap(CrystalElement.class);
			c.add(p);
			map.put(element, c);
			paths.put(p.origin, map);
		}
		else {
			ArrayList<CrystalPath> c = map.get(p.element);
			if (c == null) {
				c = new ArrayList();
				c.add(p);
				map.put(element, c);
			}
			else {
				if (!c.contains(p))
					c.add(p);
				//ReikaJavaLibrary.pConsole(c.size(), Side.SERVER);
			}
		}
		for (int i = 0; i < p.nodes.size(); i++) {
			WorldLocation loc = p.nodes.get(i);
			CrystalNetworkTile te = this.getNetTileAt(loc, true);
			if (te instanceof CrystalRepeater) {
				CrystalRepeater tile = (CrystalRepeater)te;
				tile.setSignalDepth(p.element, p.nodes.size()-1-i);
			}
		}
		//ReikaJavaLibrary.pConsole(paths, Side.SERVER);
		Collections.sort(paths.get(p.origin).get(p.element));
	}

	static void removePathsWithTile(CrystalNetworkTile te) {
		if (te == null)
			return;
		EnumMap<CrystalElement, ArrayList<CrystalPath>> map = paths.get(getLocation(te));
		if (map != null) {
			for (CrystalElement e : map.keySet()) {
				ArrayList<CrystalPath> c = map.get(e);
				Iterator<CrystalPath> it = c.iterator();
				while (it.hasNext()) {
					CrystalPath p = it.next();
					if (p.contains(te))
						it.remove();
				}
			}
		}
	}

	@Override
	public String toString() {
		return element+": "+target;//+" by "+stepRange;
	}

	public boolean isComplete() {
		return nodes.size() >= 2 && this.getSourceAt(nodes.getLast(), false) != null;
	}

	private void findFrom(CrystalReceiver r, int thresh) {
		if (invalid)
			return;
		WorldLocation loc = getLocation(r);
		if (nodes.contains(loc)) {
			return;
		}
		if (this.isComplete()) {
			/*
			StringBuilder sb = new StringBuilder();
			sb.append(nodes.size());
			sb.append(":[");
			int i = 0;
			for (WorldLocation l : nodes) {
				sb.append(i);
				sb.append("=");
				sb.append(l.getTileEntity());
				sb.append(";");
				i++;
			}
			sb.append("]");
			ReikaJavaLibrary.pConsole("Returning with "+sb.toString());
			 */
			return;
		}
		steps++;
		stepsThisTick++;
		if (steps > maxSteps) {
			//return;
		}
		/*
		if (stepsThisTick >= MAX_STEPS_PER_TICK) {
			stepsThisTick = 0;
			this.suspendUntilNextTick(r);
			return;
		}
		 */
		//ReikaJavaLibrary.pConsole("Stepped in, Receiver="+r);
		nodes.add(loc);
		ArrayList<CrystalTransmitter> li = net.getTransmittersTo(r, element);
		if (CrystalNetworkLogger.getLogLevel().isAtLeast(LoggingLevel.PATHFIND))
			CrystalNetworkLogger.logPathFind(target, element, r, li.toString(), nodes.toString());
		if (ChromaOptions.SHORTPATH.getState() || skypeaterEntry != null) {
			Collections.sort(li, new TransmitterDistanceSorter(r)); //basic "start with closest and work outwards" logic; A* too complex and expensive
		}
		Collections.sort(li, NetworkSorters.prioritizer);
		//ReikaJavaLibrary.pConsole("Found "+li.size()+" for "+r+": "+li);
		for (CrystalTransmitter te : li) {
			if (this.isComplete())
				return;
			WorldLocation loc2 = getLocation(te);
			if (!blacklist.contains(loc2) && !duplicates.containsValue(loc2)) {
				CrystalLink l = net.getLink(loc2, loc);

				if (te != target) {
					if ((te.needsLineOfSightToReceiver(r) || r.needsLineOfSightFromTransmitter(te)) && !l.hasLineOfSight()) {
						continue;
					}

					/*
					ReikaJavaLibrary.pConsole("Data for "+te+":");
					if (te instanceof CrystalSource)
						ReikaJavaLibrary.pConsole("Source: "+(te instanceof CrystalSource)+" && "+this.isConnectableSource((CrystalSource)te, thresh));
					else
						ReikaJavaLibrary.pConsole("Source: false");

					ReikaJavaLibrary.pConsole("Repeater: "+(te instanceof CrystalRepeater));
					 */

					if (te instanceof CrystalSource && this.isConnectableSource((CrystalSource)te, thresh)) {
						net.addLink(l, true);
						nodes.add(loc2);
						//ReikaJavaLibrary.pConsole("Found source for "+element+" > "+r+", returning: "+this.isComplete());
						return;
					}
					else if (te instanceof CrystalRepeater) {
						net.addLink(l, true);
						Collection<WorldLocation> others = new ArrayList(li);
						others.remove(te);
						duplicates.put(loc2, others);
						if (te instanceof TileEntitySkypeater) {
							NodeClass c = ((TileEntitySkypeater)te).getNodeType();
							if (skypeaterEntry == null) {
								skypeaterEntry = c;
							}
							else {
								if (c.isAbove(skypeaterEntry))
									;//continue;
							}
							lastSkypeaterType = c;
						}
						else {
							if (lastSkypeaterType != null && skypeaterEntry != null && skypeaterEntry != lastSkypeaterType)
								;//continue;
						}
						this.findFrom((CrystalRepeater)te, thresh);
					}
				}
			}
		}
		if (!this.isComplete()) {
			nodes.removeLast();
			blacklist.add(loc);
			duplicates.remove(loc);
		}
	}

	private boolean isConnectableSource(CrystalSource te, int thresh) {
		return te.canSupply(target, element) && te.getEnergy(element) >= thresh && (user == null || te.playerCanUse(user)) && (!(te instanceof TileEntityCrystalPylon) || !((TileEntityCrystalPylon)te).enhancing);
	}

	/*
	private void suspendUntilNextTick(CrystalReceiver r) {
		suspended = true;
	}
	 */

	static final CrystalReceiver getReceiverAt(WorldLocation loc, boolean exception) {
		TileEntity te = loc.getTileEntity();
		if (te instanceof CrystalReceiver) {
			return (CrystalReceiver)te;
		}
		if (te instanceof TileEntityCrystalMusic) {
			return ((TileEntityCrystalMusic)te).createTemporaryReceiver();
		}
		if (ModList.THAUMCRAFT.isLoaded() && InterfaceCache.NODE.instanceOf(te)) {
			NodeReceiverWrapper wrap = NodeRecharger.instance.getWrapper(loc);
			if (wrap != null) {
				return wrap;
			}
		}
		if (exception)
			throw new IllegalStateException("How did a non-receiver tile ("+te+") get put in the network here ("+loc+")?");
		else
			return null;
	}

	static final CrystalTransmitter getTransmitterAt(WorldLocation loc, boolean exception) {
		TileEntity te = loc.getTileEntity();
		if (te instanceof CrystalTransmitter) {
			return (CrystalTransmitter)te;
		}
		if (exception)
			throw new IllegalStateException("How did a non-transmitter tile ("+te+") get put in the network here ("+loc+")?");
		else
			return null;
	}

	static final CrystalSource getSourceAt(WorldLocation loc, boolean exception) {
		TileEntity te = loc.getTileEntity();
		if (te instanceof CrystalSource) {
			return (CrystalSource)te;
		}
		if (exception)
			throw new IllegalStateException("How did a non-source tile ("+te+") get put in the network here ("+loc+")?");
		else
			return null;
	}

	static final CrystalNetworkTile getNetTileAt(WorldLocation loc, boolean exception) {
		TileEntity te = loc.getTileEntity();
		if (te instanceof CrystalNetworkTile) {
			return (CrystalNetworkTile)te;
		}
		if (te instanceof TileEntityCrystalMusic) {
			return ((TileEntityCrystalMusic)te).createTemporaryReceiver();
		}
		if (ModList.THAUMCRAFT.isLoaded() && InterfaceCache.NODE.instanceOf(te)) {
			NodeReceiverWrapper wrap = NodeRecharger.instance.getWrapper(loc);
			if (wrap != null) {
				return wrap;
			}
		}
		if (exception)
			throw new IllegalStateException("How did a non-network tile ("+te+") get put in the network here ("+loc+")?");
		else
			return null;
	}

	public static CrystalPath convertTileListToPath(LinkedList<CrystalNetworkTile> li, CrystalElement e) {
		LinkedList<WorldLocation> li2 = new LinkedList();
		for (CrystalNetworkTile te : li) {
			li2.add(new WorldLocation(te.getWorld(), te.getX(), te.getY(), te.getZ()));
		}
		return new CrystalPath(CrystalNetworker.instance, false, e, li2);
	}

	static void replacePath(CrystalSource src, WorldLocation tgt, CrystalElement color, CrystalPath p) {
		EnumMap<CrystalElement, ArrayList<CrystalPath>> data = paths.get(tgt);
		ArrayList<CrystalPath> li = data.get(color);
		for (int i = 0; i < li.size(); i++) {
			CrystalPath at = li.get(i);
			if (at.hasSameEndpoints(p) && at.element == p.element) {
				li.set(i, p);
				return;
			}
		}
		ChromatiCraft.logger.logError("Tried to replace a "+color+" path from "+src+" to "+tgt+", but no such path exists!");
	}

	static Collection<? extends CrystalPath> getCachedPaths(CrystalElement e) {
		ArrayList<CrystalPath> li = new ArrayList();
		for (EnumMap<CrystalElement, ArrayList<CrystalPath>> map : paths.values()) {
			ArrayList<CrystalPath> list = map.get(e);
			if (list != null)
				li.addAll(list);
		}
		return li;
	}

	static LOSData lineOfSight(WorldLocation l1, WorldLocation l2) {
		return lineOfSight(l1.getWorld(), l1.xCoord, l1.yCoord, l1.zCoord, l2.xCoord, l2.yCoord, l2.zCoord);
	}

	private LOSData lineOfSight(CrystalNetworkTile te1, CrystalNetworkTile te) {
		return lineOfSight(te1.getWorld(), te1.getX(), te1.getY(), te1.getZ(), te.getX(), te.getY(), te.getZ());
	}

	//private boolean lineOfSight(World world, int x, int y, int z, CrystalNetworkTile te) {
	//	return lineOfSight(world, x, y, z, te.getX(), te.getY(), te.getZ());
	//}

	public static LOSData lineOfSight(World world, int x1, int y1, int z1, int x2, int y2, int z2) {
		tracer.setOrigins(x1, y1, z1, x2, y2, z2);
		tracer.offset(0.5, 0.5, 0.5);
		boolean los = tracer.isClearLineOfSight(world);
		return new LOSData(los, tracer.getRayBlocks());
	}

	static {
		tracer = new RayTracer(0, 0, 0, 0, 0, 0);
		tracer.softBlocksOnly = true;
		tracer.allowFluids = false;
		tracer.uniDirectionalChecks = true;
		tracer.addTransparentBlock(Blocks.glass);
		tracer.addTransparentBlock(Blocks.glass_pane);
		tracer.addTransparentBlock(Blocks.snow_layer, 0);
		tracer.addTransparentBlock(ChromaBlocks.SELECTIVEGLASS.getBlockInstance());
		if (ModList.ROTARYCRAFT.isLoaded()) {
			addRCGlass();
		}
		if (ModList.GEOSTRATA.isLoaded()) {
			addGeoVines();
		}
		if (ModList.EXTRAUTILS.isLoaded() && ExtraUtilsHandler.getInstance().initializedProperly()) {
			Block b = ExtraUtilsHandler.getInstance().deco2ID;
			if (b != null) {
				tracer.addTransparentBlock(b, 1);
				tracer.addTransparentBlock(b, 2);
				tracer.addTransparentBlock(b, 4);
			}

			b = ExtraUtilsHandler.getInstance().etherealBlockID;
			if (b != null) {
				tracer.addTransparentBlock(b, ExtraUtilsHandler.getInstance().ethereal);
				tracer.addTransparentBlock(b, ExtraUtilsHandler.getInstance().invethereal);
				tracer.addTransparentBlock(b, ExtraUtilsHandler.getInstance().ineffable);
				tracer.addTransparentBlock(b, ExtraUtilsHandler.getInstance().invineffable);
			}
		}
		if (ModList.TINKERER.isLoaded() && TinkerBlockHandler.getInstance().initializedProperly()) {
			tracer.addTransparentBlock(TinkerBlockHandler.getInstance().clearGlassID);
		}
		if (ModList.ENDERIO.isLoaded()) {
			Block b = GameRegistry.findBlock(ModList.ENDERIO.modLabel, "blockFusedQuartz");
			if (b != null) {
				tracer.addTransparentBlock(b, 1);
			}
		}

		tracer.addOpaqueBlock(Blocks.deadbush);
		tracer.addOpaqueBlock(Blocks.tallgrass, 0);
		tracer.addOpaqueBlock(Blocks.tallgrass, 2);
		tracer.addOpaqueBlock(Blocks.fire);
		tracer.addOpaqueBlock(Blocks.vine);

		/*
		tracer.addOpaqueBlock(Blocks.standing_sign);
		tracer.addOpaqueBlock(Blocks.reeds);
		tracer.addOpaqueBlock(Blocks.carpet);
		tracer.addOpaqueBlock(Blocks.rail);
		tracer.addOpaqueBlock(Blocks.web);
		tracer.addOpaqueBlock(Blocks.torch);
		tracer.addOpaqueBlock(Blocks.redstone_torch);
		tracer.addOpaqueBlock(Blocks.unlit_redstone_torch);
		tracer.addOpaqueBlock(Blocks.powered_comparator);
		tracer.addOpaqueBlock(Blocks.unpowered_comparator);
		tracer.addOpaqueBlock(Blocks.powered_repeater);
		tracer.addOpaqueBlock(Blocks.unpowered_repeater);
		tracer.addOpaqueBlock(Blocks.wheat);
		tracer.addOpaqueBlock(Blocks.carrots);
		tracer.addOpaqueBlock(Blocks.potatoes);*/

		tracer.cacheBlockRay = true;
	}

	@ModDependent(ModList.ROTARYCRAFT)
	private static void addRCGlass() {
		tracer.addTransparentBlock(BlockRegistry.BLASTGLASS.getBlockInstance());
		tracer.addTransparentBlock(BlockRegistry.BLASTPANE.getBlockInstance());
	}

	@ModDependent(ModList.GEOSTRATA)
	private static void addGeoVines() {
		tracer.addTransparentBlock(GeoBlocks.GLOWVINE.getBlockInstance());
	}

	public static final WorldLocation getLocation(CrystalNetworkTile te) {
		return new WorldLocation(te.getWorld(), te.getX(), te.getY(), te.getZ());
	}

	static void stopAllSearches() {
		invalid = true;
	}

	static boolean isBlockPassable(World world, int x, int y, int z) {
		return tracer.isBlockPassable(world, x, y, z);
	}
	/*
	void receiveChunk(Chunk c) {
		ChunkCoordIntPair key = c.getChunkCoordIntPair();
		ChunkCopy copy = new ChunkCopy(c);
		chunkCache.put(key, copy);
	}

	private static class ChunkCopy extends Chunk/*implements IBlockAccess*//* {

		private final short[][][] data = new short[16][256][16];

		private ChunkCopy(Chunk c) {
			super(c.worldObj, c.xPosition, c.zPosition);
			for (int i = 0; i < 16; i++) {
				for (int k = 0; k < 16; k++) {
					for (int j = 0; j < 256; j++) {
						data[i][j][k] = encode(c.getBlock(i, j, k), c.getBlockMetadata(i, j, k));
					}
				}
			}
		}

		private static short encode(Block b, int meta) {
			return (short)(Block.getIdFromBlock(b)+(meta << 12));
		}

		private static BlockKey decode(short id) {
			return new BlockKey(Block.getBlockById(id & 4095), (id >> 12));
		}

		@Override
		public Block getBlock(int x, int y, int z) {
			return this.decode(data[x&15][y][z&15]).blockID;
		}

		@Override
		public int getBlockMetadata(int x, int y, int z) {
			return this.decode(data[x&15][y][z&15]).metadata;
		}
		/*
		@Override
		public boolean isAirBlock(int x, int y, int z) {
			return this.getBlock(x, y, z).getMaterial() == Material.air;
		}

		@Override
		public TileEntity getTileEntity(int x, int y, int z) {return null;}

		@Override
		@SideOnly(Side.CLIENT)
		public int getLightBrightnessForSkyBlocks(int x, int y, int z, int p_72802_4_) {return 0;}

		@Override
		public int isBlockProvidingPowerTo(int x, int y, int z, int side) {return 0;}

		@Override
		@SideOnly(Side.CLIENT)
		public BiomeGenBase getBiomeGenForCoords(int x, int z) {return null;}

		@Override
		@SideOnly(Side.CLIENT)
		public int getHeight() {return 256;}

		@Override
		@SideOnly(Side.CLIENT)
		public boolean extendedLevelsInChunkCache() {return false;}

		@Override
		public boolean isSideSolid(int x, int y, int z, ForgeDirection side, boolean _default) {return false;}
	 *//*
}

static class ChunkRequest {

	final PylonFinder pathfinder;
	final WorldChunk chunk;

	private ChunkRequest(PylonFinder f, WorldChunk wc) {
		pathfinder = f;
		chunk = wc;
	}

}
	  */
}
