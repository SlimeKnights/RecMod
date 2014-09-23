package fuj1n.recmod.client.gui;

import net.minecraft.client.gui.GuiLabel;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiScreen;

import fuj1n.recmod.RecMod;
import fuj1n.recmod.inventory.ContainerDummy;
import fuj1n.recmod.network.packet.PacketUpdatePlayerStatus;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.*;
import org.lwjgl.opengl.GL11;

public class GuiSimple extends GuiContainer {

	public String boundPlayer;

	public int guiState = 0;
	public String[] stateNames = { "Main", "Interface", "Keyboard", "Integration - NYI" };
	public int[] returnStates = { -1337, 0, 0, 0 };

	// Keyboard pane globals
	public int listenKeyType = -1;

	public ResourceLocation background = new ResourceLocation("recmod:textures/gui/simple.png");

	public GuiSimple(String boundPlayer) {
		super(new ContainerDummy());

		this.boundPlayer = boundPlayer;

		if (stateNames.length != returnStates.length) {
			throw new IllegalStateException("The length of GuiSimple.stateNames must match GuiSimple.returnStates");
		}
	}

	@Override
	public void drawGuiContainerForegroundLayer(int par1, int par2) {
		this.fontRendererObj.drawString("RecMod - " + stateNames[guiState], 8, 11, 4210752);

		if (guiState == 3) {
			this.fontRendererObj.drawString("This feature is still not", 8, 11 + fontRendererObj.FONT_HEIGHT * 2, 4210752);
			this.fontRendererObj.drawString("implemented, trust me, it'll be", 8, 11 + fontRendererObj.FONT_HEIGHT * 3, 4210752);
			this.fontRendererObj.drawString("awesome!", 8, 11 + fontRendererObj.FONT_HEIGHT * 4, 4210752);
		}
	}

	@Override
	public void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(background);
		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;
		this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

	@Override
	public void initGui() {
		super.initGui();

		this.createGui();
	}

	public void createGui() {
		buttonList.clear();

		int k = (this.width - this.xSize) / 2;
		int l = (this.height - this.ySize) / 2;

		buttonList.add(new GuiButton(returnStates[guiState] == -1337 ? -1337 : 1337 + returnStates[guiState], k + this.xSize - 25, l + 5, 20, 20, returnStates[guiState] == -1337 ? "X" : "<"));

		int buttonDelim = 21;
		int buttonSize = this.xSize - 16;
		int smallButtonSize = buttonSize / 2;

		switch (guiState) {
		case 0:
			buttonList.add(new GuiButton(0, k + 8 + smallButtonSize * 0, l + 30 + buttonDelim * 0, smallButtonSize, 20, (RecMod.instance.isPlayerRecording(boundPlayer) ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + "Recording"));
			buttonList.add(new GuiButton(1, k + 8 + smallButtonSize * 1, l + 30 + buttonDelim * 0, smallButtonSize, 20, (RecMod.instance.isPlayerStreaming(boundPlayer) ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + "Streaming"));
			buttonList.add(new GuiButton(1338, k + 8, l + 30 + buttonDelim * 1, buttonSize, 20, "Interface"));
			buttonList.add(new GuiButton(1339, k + 8, l + 30 + buttonDelim * 2, buttonSize, 20, "Keyboard"));
			buttonList.add(new GuiButton(1340, k + 8, l + 30 + buttonDelim * 3, buttonSize, 20, "Integration"));

			break;
		case 1:
			buttonList.add(new GuiButton(3, k + 8 + smallButtonSize * 0, l + 30 + buttonDelim * 0, smallButtonSize, 20, "Bobber: " + (RecMod.instance.showSelf ? EnumChatFormatting.GREEN + "On" : EnumChatFormatting.RED + "Off")));
			buttonList.add(new GuiButton(4, k + 8 + smallButtonSize * 1, l + 30 + buttonDelim * 0, smallButtonSize, 20, "Never: " + (!RecMod.instance.showSelfDef ? EnumChatFormatting.GREEN + "On" : EnumChatFormatting.RED + "Off")));
			break;
		case 2:
			buttonList.add(new GuiButton(2, k + 8, l + 30 + buttonDelim * 0, buttonSize, 20, "Keyboard Enabled: " + (RecMod.instance.enableKeys ? EnumChatFormatting.GREEN + "Yes" : EnumChatFormatting.RED + "No")));

			if (RecMod.instance.enableKeys) {
				buttonList.add(new GuiButton(1594, k + 9, l + 30 + buttonDelim * 2, buttonSize, 20, (listenKeyType == 0 ? EnumChatFormatting.YELLOW : "") + "Record Key: " + (RecMod.instance.keyRec == -1337 ? "DISABLED" : Keyboard.getKeyName(RecMod.instance.keyRec))));
				buttonList.add(new GuiButton(1595, k + 9, l + 30 + buttonDelim * 3, buttonSize, 20, (listenKeyType == 1 ? EnumChatFormatting.YELLOW : "") + "Stream Key: " + (RecMod.instance.keyStr == -1337 ? "DISABLED" : Keyboard.getKeyName(RecMod.instance.keyStr))));
			}

			break;
		}
	}

	@Override
	public void actionPerformed(GuiButton button) {
		listenKeyType = -1;

		if (button.id == -1337) {
			this.mc.thePlayer.closeScreen();
		}

		if (button.id >= 1594) {
			listenKeyType = button.id - 1594;
		} else if (button.id >= 1337) {
			guiState = button.id - 1337;
		} else {
			switch (button.id) {
			case 0:
				RecMod.instance.updatePlayerInformation(boundPlayer, 0, !RecMod.instance.isPlayerRecording(boundPlayer));
				RecMod.packetPipeline.sendToServer(new PacketUpdatePlayerStatus(boundPlayer, 0, RecMod.instance.isPlayerRecording(boundPlayer)));

				break;
			case 1:
				RecMod.instance.updatePlayerInformation(boundPlayer, 1, !RecMod.instance.isPlayerStreaming(boundPlayer));
				RecMod.packetPipeline.sendToServer(new PacketUpdatePlayerStatus(boundPlayer, 1, RecMod.instance.isPlayerStreaming(boundPlayer)));

				break;
			case 2:
				RecMod.instance.enableKeys = !RecMod.instance.enableKeys;
				RecMod.instance.writeToFile();

				break;
			case 3:
				RecMod.instance.showSelf = !RecMod.instance.showSelf;

				break;
			case 4:
				RecMod.instance.showSelfDef = !RecMod.instance.showSelfDef;
				RecMod.instance.writeToFile();

				break;
			}
		}

		createGui();
	}

	@Override
	protected void keyTyped(char c, int key) {
		if (listenKeyType == -1 && key == 1) {
			if (returnStates[guiState] != -1337) {
				guiState = returnStates[guiState];
				createGui();
			} else {
				this.mc.thePlayer.closeScreen();
			}
		} else {
			switch (listenKeyType) {
			case 0:
				RecMod.instance.keyRec = key == 1 ? -1337 : key;
				break;
			case 1:
				RecMod.instance.keyStr = key == 1 ? -1337 : key;
				break;
			}
			listenKeyType = -1;
			RecMod.instance.writeToFile();

			createGui();
		}
	}

}
