package fuj1n.recmod.client.event;

import fuj1n.recmod.RecMod;
import fuj1n.recmod.lib.IndexReference;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreObjectiveCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventRenderGame extends Gui {

  private ResourceLocation indicatorsHigh = new ResourceLocation("recmod:textures/sheets/indicatorsx2.png");
  private ResourceLocation indicatorsLow = new ResourceLocation("recmod:textures/sheets/indicators.png");

  private GuiPlayerTabOverlay to;

  @SubscribeEvent
  public void preRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
    if (event.type == ElementType.PLAYER_LIST) {
      event.setCanceled(true);
      renderGameOverlay(event);
    }
  }

  @SubscribeEvent
  public void renderGameOverlay(RenderGameOverlayEvent event) {
    Minecraft mc = Minecraft.getMinecraft();
    ResourceLocation indicators = mc.isFancyGraphicsEnabled() ? indicatorsHigh : indicatorsLow;

    if (event.type == ElementType.PLAYER_LIST) {
      if (to == null) {
        to = Minecraft.getMinecraft().ingameGUI.getTabList();
      }

      int infooffset = 1;

      int width = event.resolution.getScaledWidth();
      Scoreboard scoreboard = mc.theWorld.getScoreboard();
      ScoreObjective scoreobjective = mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(0);

      NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
      List list = to.field_175252_a.sortedCopy(nethandlerplayclient.getPlayerInfoMap());
      int j = 0;
      int k = 0;
      Iterator iterator = list.iterator();
      int l;

      while (iterator.hasNext()) {
        NetworkPlayerInfo networkplayerinfo = (NetworkPlayerInfo) iterator.next();
        l = mc.fontRendererObj.getStringWidth(to.getPlayerName(networkplayerinfo));
        j = Math.max(j, l) + 24;
        k = Math.max(k, l);
      }

      list = list.subList(0, Math.min(list.size(), 80));
      int j3 = list.size();
      int k3 = j3;

      for (l = 1; k3 > 20; k3 = (j3 + l - 1) / l) {
        ++l;
      }

      boolean flag = mc.isIntegratedServerRunning() || mc.getNetHandler().getNetworkManager().getIsencrypted();
      int i1;

      if (scoreobjective != null) {
        if (scoreobjective.getRenderType() == IScoreObjectiveCriteria.EnumRenderType.HEARTS) {
          i1 = 90;
        } else {
          i1 = k;
        }
      } else {
        i1 = 0;
      }

      int j1 = Math.min(l * ((flag ? 9 : 0) + j + i1 + 13), width - 50) / l;
      int k1 = width / 2 - (j1 * l + (l - 1) * 5) / 2;
      int l1 = 10;
      int i2 = j1 * l + (l - 1) * 5;
      List list1 = null;
      List list2 = null;
      Iterator iterator1;
      String s;

      if (to.header != null) {
        list1 = mc.fontRendererObj.listFormattedStringToWidth(to.header.getFormattedText(), width - 50);

        for (iterator1 = list1.iterator(); iterator1.hasNext(); i2 = Math.max(i2, mc.fontRendererObj.getStringWidth(s))) {
          s = (String) iterator1.next();
        }
      }

      if (to.footer != null) {
        list2 = mc.fontRendererObj.listFormattedStringToWidth(to.footer.getFormattedText(), width - 50);

        for (iterator1 = list2.iterator(); iterator1.hasNext(); i2 = Math.max(i2, mc.fontRendererObj.getStringWidth(s))) {
          s = (String) iterator1.next();
        }
      }

      int j2;

      if (list1 != null) {
        drawRect(width / 2 - i2 / 2 - 1, l1 - 1, width / 2 + i2 / 2 + 1, l1 + list1.size() * mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

        for (iterator1 = list1.iterator(); iterator1.hasNext(); l1 += mc.fontRendererObj.FONT_HEIGHT) {
          s = (String) iterator1.next();
          j2 = mc.fontRendererObj.getStringWidth(s);
          mc.fontRendererObj.drawStringWithShadow(s, (float) (width / 2 - j2 / 2), (float) l1, -1);
        }

        ++l1;
      }

      drawRect(width / 2 - i2 / 2 - 1, l1 - 1, width / 2 + i2 / 2 + 1, l1 + k3 * 9, Integer.MIN_VALUE);

      for (int l3 = 0; l3 < j3; ++l3) {
        int i4 = l3 / k3;
        j2 = l3 % k3;
        int k2 = k1 + i4 * j1 + i4 * 5;
        int l2 = l1 + j2 * 9;
        drawRect(k2, l2, k2 + j1, l2 + 8, 553648127);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableAlpha();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

        if (l3 < list.size()) {
          NetworkPlayerInfo networkplayerinfo1 = (NetworkPlayerInfo) list.get(l3);
          String s1 = to.getPlayerName(networkplayerinfo1);

          if (flag) {
            mc.getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
            Gui.drawScaledCustomSizeModalRect(k2, l2, 8.0F, 8.0F, 8, 8, 8, 8, 64.0F, 64.0F);
            EntityPlayer entityplayer = mc.theWorld.getPlayerEntityByUUID(networkplayerinfo1.getGameProfile().getId());

            if (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
              Gui.drawScaledCustomSizeModalRect(k2, l2, 40.0F, 8.0F, 8, 8, 8, 8, 64.0F, 64.0F);
            }

            k2 += 9;
          }

          if (networkplayerinfo1.getGameType() == WorldSettings.GameType.SPECTATOR) {
            s1 = EnumChatFormatting.ITALIC + s1;
            mc.fontRendererObj.drawStringWithShadow(s1, (float) k2, (float) l2, -1862270977);
          } else {
            mc.fontRendererObj.drawStringWithShadow(s1, (float) k2, (float) l2, -1);
          }

          mc.getTextureManager().bindTexture(indicators);
          int indicatorRecIndex = RecMod.instance.isPlayerRecording(networkplayerinfo1.getGameProfile().getName()) ? IndexReference.ICON_RED_INDEX : IndexReference.ICON_GRAY_INDEX;
          int indicatorStrIndex = RecMod.instance.isPlayerStreaming(networkplayerinfo1.getGameProfile().getName()) ? IndexReference.ICON_GREEN_INDEX : IndexReference.ICON_GRAY_INDEX;
          drawTexturedModalRect(width / 2 + i2 / 2 + 1 - infooffset - 16, l2, indicatorRecIndex * 8, (int) Math.floor(indicatorRecIndex / 32) * 8 + IndexReference.RES_SSD, 8, 8);
          drawTexturedModalRect(width / 2 + i2 / 2 + 1 - infooffset - 8, l2, indicatorStrIndex * 8, (int) Math.floor(indicatorStrIndex / 32) * 8 + IndexReference.RES_SSD, 8, 8);

          if (scoreobjective != null && networkplayerinfo1.getGameType() != WorldSettings.GameType.SPECTATOR) {
            int j4 = k2 + j + 1;
            int i3 = j4 + i1;

            if (i3 - j4 > 5) {
              // Draw Scoreboard Values
              to.drawScoreboardValues(scoreobjective, l2, networkplayerinfo1.getGameProfile().getName(), j4 - 24, i3 - 24, networkplayerinfo1);
            }
          }

          // Draw Player Ping
          to.drawPing(j1, k2 - (flag ? 9 : 0) - 18, l2, networkplayerinfo1);
        }
      }

      if (list2 != null) {
        l1 += k3 * 9 + 1;
        drawRect(width / 2 - i2 / 2 - 1, l1 - 1, width / 2 + i2 / 2 + 1, l1 + list2.size() * mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);
        drawRect(width / 2 - i2 / 2 - 1, l1 - 1, width / 2 + i2 / 2 + 1, l1 + list2.size() * mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

        for (iterator1 = list2.iterator(); iterator1.hasNext(); l1 += mc.fontRendererObj.FONT_HEIGHT) {
          s = (String) iterator1.next();
          j2 = mc.fontRendererObj.getStringWidth(s);
          mc.fontRendererObj.drawStringWithShadow(s, (float) (width / 2 - j2 / 2), (float) l1, -1);
        }
      }
    } else if (event.type == ElementType.ALL && RecMod.instance.showSelf && mc.currentScreen == null) {
      int x;
      int y;
      switch (RecMod.instance.posMode) {
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

      int indicatorRecIndex = RecMod.instance.isPlayerRecording(mc.thePlayer.getName()) ? IndexReference.ICON_RED_INDEX : IndexReference.ICON_GRAY_INDEX;
      int indicatorStrIndex = RecMod.instance.isPlayerStreaming(mc.thePlayer.getName()) ? IndexReference.ICON_GREEN_INDEX : IndexReference.ICON_GRAY_INDEX;
      mc.getTextureManager().bindTexture(indicators);
      drawTexturedModalRect(x, y, indicatorRecIndex * 16, (int) Math.floor(indicatorRecIndex / 16) * 16 + IndexReference.RES_SD, 16, 16);
      drawTexturedModalRect(x + 16, y, indicatorStrIndex * 16, (int) Math.floor(indicatorStrIndex / 16) * 16 + IndexReference.RES_SD, 16, 16);
    }
  }
}
