/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ChromatiCraft.GUI.Tile;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;

import Reika.ChromatiCraft.ChromatiCraft;
import Reika.ChromatiCraft.Base.GuiChromaBase;
import Reika.ChromatiCraft.Container.ContainerFluidRelay;
import Reika.ChromatiCraft.Registry.ChromaPackets;
import Reika.ChromatiCraft.TileEntity.Transport.TileEntityFluidRelay;
import Reika.DragonAPI.Instantiable.GUI.CustomSoundGuiButton.CustomSoundImagedGuiButton;
import Reika.DragonAPI.Instantiable.GUI.CustomSoundGuiButton.CustomSoundImagedGuiButtonSneakIcon;
import Reika.DragonAPI.Libraries.IO.ReikaPacketHelper;
import Reika.DragonAPI.Libraries.IO.ReikaTextureHelper;
import Reika.DragonAPI.Libraries.Rendering.ReikaLiquidRenderer;


public class GuiFluidRelay extends GuiChromaBase {

	private final TileEntityFluidRelay relay;

	public GuiFluidRelay(EntityPlayer ep, TileEntityFluidRelay te) {
		super(new ContainerFluidRelay(ep, te), ep, te);
		relay = te;

		ySize = 153;
	}

	@Override
	public void initGui() {
		super.initGui();

		int j = (width - xSize) / 2;
		int k = (height - ySize) / 2;

		String tex = "Textures/GUIs/buttons.png";
		int in = 22;
		int iny = 45;
		int dy = 5;
		buttonList.add(new CustomSoundImagedGuiButtonSneakIcon(0, j+in, k+iny-dy, 10, 10, 100, 66, tex, ChromatiCraft.class, this, 100, 86));
		buttonList.add(new CustomSoundImagedGuiButtonSneakIcon(1, j+xSize-10-in, k+iny-dy, 10, 10, 100, 56, tex, ChromatiCraft.class, this, 100, 76));

		buttonList.add(new CustomSoundImagedGuiButtonSneakIcon(2, j+in, k+iny+dy, 10, 10, 100, 66, tex, ChromatiCraft.class, this, 100, 86));
		buttonList.add(new CustomSoundImagedGuiButtonSneakIcon(3, j+xSize-10-in, k+iny+dy, 10, 10, 100, 56, tex, ChromatiCraft.class, this, 100, 76));


		int dx = 14;
		buttonList.add(new CustomSoundImagedGuiButton(4, j+in-dx, k+iny, 10, 10, 90, 56, tex, ChromatiCraft.class, this));
		buttonList.add(new CustomSoundImagedGuiButton(5, j+xSize-10-in+dx, k+iny-dy, 10, 10, 90, 76, tex, ChromatiCraft.class, this));

		buttonList.add(new CustomSoundImagedGuiButton(6, j+xSize-10-in+dx, k+iny+dy, 10, 10, 90, relay.autoFilter ? 86 : 56, tex, ChromatiCraft.class, this));
	}

	@Override
	protected void actionPerformed(GuiButton b) {
		super.actionPerformed(b);
		int delta1 = 0;
		int delta2 = 0;
		int n = GuiScreen.isCtrlKeyDown() ? 1 : (GuiScreen.isShiftKeyDown() ? 100 : 10);
		switch(b.id) {
			case 0:
				delta1 = -n;
				break;
			case 1:
				delta1 = n;
				break;
			case 2:
				delta2 = -n;
				break;
			case 3:
				delta2 = n;
				break;
			case 4:
				ReikaPacketHelper.sendPacketToServer(ChromatiCraft.packetChannel, ChromaPackets.RELAYCLEAR.ordinal(), relay);
				break;
			case 5:
				ReikaPacketHelper.sendPacketToServer(ChromatiCraft.packetChannel, ChromaPackets.RELAYCOPY.ordinal(), relay);
				break;
			case 6:
				relay.autoFilter = !relay.autoFilter;
				ReikaPacketHelper.sendPacketToServer(ChromatiCraft.packetChannel, ChromaPackets.RELAYAUTO.ordinal(), relay);
				break;
		}
		if (delta1 != 0) {
			ReikaPacketHelper.sendPacketToServer(ChromatiCraft.packetChannel, ChromaPackets.RELAYPRESSUREBASE.ordinal(), relay, delta1);
		}
		if (delta2 != 0) {
			ReikaPacketHelper.sendPacketToServer(ChromatiCraft.packetChannel, ChromaPackets.RELAYPRESSUREVAR.ordinal(), relay, delta2);
		}
		this.initGui();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int a, int b) {
		int j = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		super.drawGuiContainerBackgroundLayer(f, a, b);


		Fluid[] types = relay.getFluidTypes();
		int n = types.length;
		/*
		for (int i = n+1; i < 7; i++) {
			int x = j+14+i*22;
			int y = k+17;
			api.drawTexturedModalRect(x, y, 179, 0, 16, 16);
		}*/
		ReikaTextureHelper.bindTerrainTexture();
		for (int i = 0; i < n; i++) {
			if (types[i] != null) {
				int x = j+14+i*22;
				int y = k+17;
				api.drawTexturedModelRectFromIcon(x, y, ReikaLiquidRenderer.getFluidIconSafe(types[i]), 16, 16);
			}
		}

		String s = String.format("Pressure: %d + %d/B", relay.getBasePressure(), relay.getFunctionPressure());
		api.drawCenteredStringNoShadow(fontRendererObj, s, j+xSize/2, k+45, 0xffffff);
	}

	@Override
	public String getGuiTexture() {
		return "fluidrelay";
	}

}
