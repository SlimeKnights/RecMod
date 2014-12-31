package fuj1n.recmod.client.event;

import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fuj1n.recmod.RecMod;
import fuj1n.recmod.lib.IndexReference;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class EventRenderGame extends Gui
{

	private ResourceLocation indicatorsHigh = new ResourceLocation("recmod:textures/sheets/indicatorsx2.png");
	private ResourceLocation indicatorsLow = new ResourceLocation("recmod:textures/sheets/indicators.png");

	@SubscribeEvent
	public void onRenderGameOverlay (RenderGameOverlayEvent event)
	{
		if (event.type != RenderGameOverlayEvent.ElementType.ALL)
		{
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		ResourceLocation indicators = mc.isFancyGraphicsEnabled() ? indicatorsHigh : indicatorsLow;

		Tessellator tes = Tessellator.instance;
		tes.setColorOpaque_F(1F, 1F, 1F);

		if (mc.gameSettings.keyBindPlayerList.getIsKeyPressed() && (!mc.isIntegratedServerRunning() || mc.thePlayer.sendQueue.playerInfoList.size() > 1 || mc.theWorld.getScoreboard().func_96539_a(0) != null))
		{
			mc.mcProfiler.startSection("playerList");
			NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
			List list = nethandlerplayclient.playerInfoList;

			int k = event.resolution.getScaledWidth();
			int i2;
			int j2;
			int k2;
			int l2;
			int i3;
			int j3;
			int k3;
			j2 = nethandlerplayclient.currentServerMaxPlayers;
			l2 = j2;

			for (k2 = 1; l2 > 20; l2 = (j2 + k2 - 1) / k2)
			{
				++k2;
			}

			int j5 = 300 / k2;

			if (j5 > 150)
			{
				j5 = 150;
			}

			int k5 = (k - k2 * j5) / 2;
			byte b0 = 10;

			for (i3 = 0; i3 < j2; ++i3)
			{
				k3 = k5 + i3 % k2 * j5;
				j3 = b0 + i3 / k2 * 9;

				GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
				GL11.glEnable(GL11.GL_ALPHA_TEST);

				if (i3 < list.size())
				{
					GuiPlayerInfo guiplayerinfo = (GuiPlayerInfo) list.get(i3);
					ScorePlayerTeam scoreplayerteam = mc.theWorld.getScoreboard().getPlayersTeam(guiplayerinfo.name);
					String s3 = ScorePlayerTeam.formatPlayerName(scoreplayerteam, guiplayerinfo.name);

					int infooffset = 20;

					int i6 = k3 + j5 - 12 - 5;
					mc.getTextureManager().bindTexture(indicators);
					int indicatorRecIndex = RecMod.instance.isPlayerRecording(guiplayerinfo.name) ? IndexReference.ICON_RED_INDEX : IndexReference.ICON_GRAY_INDEX;
					int indicatorStrIndex = RecMod.instance.isPlayerStreaming(guiplayerinfo.name) ? IndexReference.ICON_GREEN_INDEX : IndexReference.ICON_GRAY_INDEX;

					drawTexturedModalRect(i6 - 8 - infooffset, j3, indicatorRecIndex * 8, (int) Math.floor(indicatorRecIndex / 32) * 8 + IndexReference.RES_SSD, 8, 8);
					drawTexturedModalRect(i6 - 8 - (infooffset - 8), j3, indicatorStrIndex * 8, (int) Math.floor(indicatorStrIndex / 32) * 8 + IndexReference.RES_SSD, 8, 8);
				}
			}
		}
		else if (!mc.gameSettings.keyBindPlayerList.getIsKeyPressed() && RecMod.instance.showSelf && mc.currentScreen == null)
		{
			int x;
			int y;

			switch (RecMod.instance.posMode)
			{
			case 0:
				x = 0;
				y = 0;
				break;
			case 1:
				x = event.resolution.getScaledWidth() - 32;
				y = 0;
				break;
			case 2:
				x = event.resolution.getScaledWidth() / 2 - 16;
				y = event.resolution.getScaledHeight() / 2 - 8;
				break;
			case 3:
				x = 0;
				y = event.resolution.getScaledHeight() - 16;
				break;
			case 4:
				x = event.resolution.getScaledWidth() - 32;
				y = event.resolution.getScaledHeight() - 16;
				break;
			case 5:
				x = event.resolution.getScaledWidth() / 2 - 16 + RecMod.instance.absX;
				y = event.resolution.getScaledHeight() / 2 - 8 + RecMod.instance.absY;
				break;
			default:
				x = event.resolution.getScaledWidth() - 32;
				y = 0;
			}

			int indicatorRecIndex = RecMod.instance.isPlayerRecording(mc.thePlayer.getCommandSenderName()) ? IndexReference.ICON_RED_INDEX : IndexReference.ICON_GRAY_INDEX;
			int indicatorStrIndex = RecMod.instance.isPlayerStreaming(mc.thePlayer.getCommandSenderName()) ? IndexReference.ICON_GREEN_INDEX : IndexReference.ICON_GRAY_INDEX;

			mc.getTextureManager().bindTexture(indicators);
			drawTexturedModalRect(x, y, indicatorRecIndex * 16, (int) Math.floor(indicatorRecIndex / 16) * 16 + IndexReference.RES_SD, 16, 16);
			drawTexturedModalRect(x + 16, y, indicatorStrIndex * 16, (int) Math.floor(indicatorStrIndex / 16) * 16 + IndexReference.RES_SD, 16, 16);
		}
	}

}
