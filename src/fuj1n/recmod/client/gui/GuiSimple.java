package fuj1n.recmod.client.gui;

import net.minecraft.client.resources.I18n;

import fuj1n.recmod.inventory.ContainerDummy;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

public class GuiSimple extends GuiContainer{

	public ResourceLocation background = new ResourceLocation("recmod:gui/simple.png");
	
	public GuiSimple() {
		super(new ContainerDummy());
	}

	@Override
	public void drawGuiContainerForegroundLayer(int par1, int par2) {
        this.fontRenderer.drawString("Simple Gui", 8, this.ySize - 96 + 2, 4210752);
	}
	
	@Override
	public void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(background);
        int k = (this.width - this.xSize) / 2;
        int l = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
	}

}
