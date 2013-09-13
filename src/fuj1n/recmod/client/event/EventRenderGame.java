package fuj1n.recmod.client.event;

import fuj1n.recmod.RecMod;

import net.minecraft.client.gui.GuiPlayerInfo;
import net.minecraft.scoreboard.*;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.NetClientHandler;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.ForgeSubscribe;

public class EventRenderGame extends Gui {

	@ForgeSubscribe
	public void onRenderGameOverlay(RenderGameOverlayEvent event) {
		if (event.type != RenderGameOverlayEvent.ElementType.ALL) {
			return;
		}
		Minecraft mc = Minecraft.getMinecraft();
		ScoreObjective scoreobjective = mc.theWorld.getScoreboard().func_96539_a(0);
		if (mc.gameSettings.keyBindPlayerList.pressed && (!mc.isIntegratedServerRunning() || mc.thePlayer.sendQueue.playerInfoList.size() > 1 || scoreobjective != null)) {
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
						if (RecMod.instance.isPlayerRecording(guiplayerinfo.name)) {
							String s4 = EnumChatFormatting.RED + "@";
							mc.fontRenderer.drawStringWithShadow(s4, i6 - mc.fontRenderer.getStringWidth(s4) - infooffset, j3, 16777215);
						}else{
							String s4 = EnumChatFormatting.DARK_GRAY + "@";
							mc.fontRenderer.drawStringWithShadow(s4, i6 - mc.fontRenderer.getStringWidth(s4) - infooffset, j3, 16777215);
						}

						if (RecMod.instance.isPlayerStreaming(guiplayerinfo.name)) {
							String s4 = EnumChatFormatting.GREEN + "@";
							mc.fontRenderer.drawStringWithShadow(s4, i6 - mc.fontRenderer.getStringWidth(s4) - (infooffset - mc.fontRenderer.getStringWidth(s4)), j3, 16777215);
						}else{
							String s4 = EnumChatFormatting.DARK_GRAY + "@";
							mc.fontRenderer.drawStringWithShadow(s4, i6 - mc.fontRenderer.getStringWidth(s4) - (infooffset - mc.fontRenderer.getStringWidth(s4)), j3, 16777215);
						}
					}
				}
			}

			// drawRect(k5 - 1, b0 - 1, k5 + j5 * k2, b0 + 9 * l2,
			// Integer.MIN_VALUE);

			// System.out.println(list);
		}
	}

}
