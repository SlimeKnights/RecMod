package fuj1n.recmod.client.event;

import org.lwjgl.input.Keyboard;

import fuj1n.recmod.lib.IndexReference;

import fuj1n.recmod.RecMod;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.scoreboard.*;
import net.minecraft.util.*;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.ForgeSubscribe;
import org.lwjgl.opengl.GL11;

public class EventRenderGame extends Gui {

	private String sheetLocation = RecMod.instance.sheetLocation;
	private ResourceLocation indicators = new ResourceLocation(sheetLocation);
	
	@ForgeSubscribe
	public void onRenderGameOverlay(RenderGameOverlayEvent event) {
		if(!sheetLocation.equals(RecMod.instance.sheetLocation)){
			sheetLocation = RecMod.instance.sheetLocation;
			indicators = new ResourceLocation(sheetLocation);
		}
		
		if (event.type != RenderGameOverlayEvent.ElementType.ALL) {
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		if (mc.gameSettings.keyBindPlayerList.pressed && (!mc.isIntegratedServerRunning() || mc.thePlayer.sendQueue.playerInfoList.size() > 1)) {
			NetClientHandler netclienthandler = mc.thePlayer.sendQueue;
			List list = netclienthandler.playerInfoList;

			int k = event.resolution.getScaledWidth();
			int i2;
			int j2;
			int k2;
			int l2;
			int i3;
			int j3;
			int k3;
			j2 = netclienthandler.currentServerMaxPlayers;
			l2 = j2;

			for (k2 = 1; l2 > 20; l2 = (j2 + k2 - 1) / k2) {
				++k2;
			}

			int j5 = 300 / k2;

			if (j5 > 150) {
				j5 = 150;
			}

			int k5 = (k - k2 * j5) / 2;
			byte b0 = 10;

			for (i3 = 0; i3 < j2; ++i3) {
				k3 = k5 + i3 % k2 * j5;
				j3 = b0 + i3 / k2 * 9;

				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glEnable(GL11.GL_ALPHA_TEST);

				if (i3 < list.size()) {
					GuiPlayerInfo guiplayerinfo = (GuiPlayerInfo) list.get(i3);
					ScorePlayerTeam scoreplayerteam = mc.theWorld.getScoreboard().getPlayersTeam(guiplayerinfo.name);
					String s3 = ScorePlayerTeam.formatPlayerName(scoreplayerteam, guiplayerinfo.name);
					mc.fontRenderer.drawStringWithShadow(s3, k3, j3, 16777215);

					int infooffset = 18;

					int l5 = k3 + mc.fontRenderer.getStringWidth(s3) + 5;
					int i6 = k3 + j5 - 12 - 5;
					if (i6 - l5 > 5) {
						mc.getTextureManager().bindTexture(indicators);
						int indicatorRecIndex = RecMod.instance.isPlayerRecording(guiplayerinfo.name) ? IndexReference.ICON_RED_INDEX : IndexReference.ICON_GRAY_INDEX;
						int indicatorStrIndex = RecMod.instance.isPlayerStreaming(guiplayerinfo.name) ? IndexReference.ICON_GREEN_INDEX : IndexReference.ICON_GRAY_INDEX;
						
						drawTexturedModalRect(i6 - 8 - infooffset, j3, indicatorRecIndex < 32 ? indicatorRecIndex * 8 : indicatorRecIndex * 8 /*Better equation here*/, (int)Math.floor(indicatorRecIndex / 32) * 8, 8, 8);
						drawTexturedModalRect(i6 - 8 - (infooffset - 8), j3, indicatorStrIndex * 8, (int)Math.floor(indicatorStrIndex / 32) * 8, 8, 8);
					}
				}
			}
		}else if(!mc.gameSettings.keyBindPlayerList.pressed && RecMod.instance.showSelf){
			int x = event.resolution.getScaledWidth() - 32;
			int y = 0;
			
			int indicatorRecIndex = RecMod.instance.isPlayerRecording(mc.thePlayer.username) ? 1 : 0;
			int indicatorStrIndex = RecMod.instance.isPlayerStreaming(mc.thePlayer.username) ? 2 : 0;
			
			mc.getTextureManager().bindTexture(indicators);
			drawTexturedModalRect(x, y, indicatorRecIndex * 16, (int)Math.floor(indicatorRecIndex / 16) * 16 + IndexReference.RESOLUTION_SPLIT_Y, 16, 16);
			drawTexturedModalRect(x + 16, y, indicatorStrIndex * 16, (int)Math.floor(indicatorStrIndex / 16) * 16 + IndexReference.RESOLUTION_SPLIT_Y, 16, 16);
		}
	}

}
