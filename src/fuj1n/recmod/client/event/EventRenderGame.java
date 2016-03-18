package fuj1n.recmod.client.event;

import com.mojang.authlib.GameProfile;
import fuj1n.recmod.RecMod;
import fuj1n.recmod.lib.IndexReference;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.scoreboard.IScoreCriteria;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.WorldSettings;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventRenderGame extends Gui {

  private static final int INDICATOR_WIDTH = 8;
  private static final int INDICATOR_PADDING = 0;
  private static final int INDICATOR_TOTAL = INDICATOR_WIDTH * 2 + INDICATOR_PADDING * 2;

  private ResourceLocation indicatorsHigh = new ResourceLocation("recmod:textures/sheets/indicatorsx2.png");
  private ResourceLocation indicatorsLow = new ResourceLocation("recmod:textures/sheets/indicators.png");

  private GuiPlayerTabOverlay to;

  @SubscribeEvent
  public void preRenderGameOverlay(RenderGameOverlayEvent.Pre event) {
    Minecraft mc = Minecraft.getMinecraft();
    ResourceLocation indicators = Minecraft.isFancyGraphicsEnabled() ? indicatorsHigh : indicatorsLow;

    if (event.type == ElementType.PLAYER_LIST) {
      event.setCanceled(true);

      to = mc.ingameGUI.getTabList();

      renderPlayerList(event, mc, indicators);
    }
  }

  public void renderPlayerList(RenderGameOverlayEvent.Pre event, Minecraft mc, ResourceLocation indicators) {
    int width = event.resolution.getScaledWidth();
    Scoreboard scoreboardIn = mc.theWorld.getScoreboard();
    ScoreObjective scoreObjectiveIn = scoreboardIn.getObjectiveInDisplaySlot(0);

    NetHandlerPlayClient nethandlerplayclient = mc.thePlayer.sendQueue;
    List<NetworkPlayerInfo> list = GuiPlayerTabOverlay.ENTRY_ORDERING.<NetworkPlayerInfo>sortedCopy(nethandlerplayclient.getPlayerInfoMap());

    int namePingGap = 0;
    int j = 0;

    for (NetworkPlayerInfo networkplayerinfo : list) {
      int nameWidth = mc.fontRendererObj.getStringWidth(to.getPlayerName(networkplayerinfo));
      namePingGap = Math.max(namePingGap, nameWidth);

      if (scoreObjectiveIn != null && scoreObjectiveIn.getRenderType() != IScoreCriteria.EnumRenderType.HEARTS) {
        nameWidth = mc.fontRendererObj.getStringWidth(" " + scoreboardIn.getOrCreateScore(networkplayerinfo.getGameProfile().getName(), scoreObjectiveIn).getScorePoints());
        j = Math.max(j, nameWidth);
      }
    }

    list = list.subList(0, Math.min(list.size(), 80));
    int l3 = list.size();
    int i4 = l3;
    int userRows;

    for (userRows = 1; i4 > 20; i4 = (l3 + userRows - 1) / userRows) {
      ++userRows;
    }

    boolean flag = mc.isIntegratedServerRunning() || mc.getNetHandler().getNetworkManager().getIsencrypted();
    int l;

    if (scoreObjectiveIn != null) {
      if (scoreObjectiveIn.getRenderType() == IScoreCriteria.EnumRenderType.HEARTS) {
        l = 90;
      } else {
        l = j;
      }
    } else {
      l = 0;
    }

    int userWidth = Math.min(userRows * ((flag ? 9 : 0) + namePingGap + l + 13), width - 50) / userRows;
    int userX = width / 2 - (userWidth * userRows + (userRows - 1) * 5) / 2;
    int menuY = 10;
    int menuWidth = userWidth * userRows + (userRows - 1) * 5;
    List<String> list1 = null;
    List<String> list2 = null;

    if (to.header != null) {
      list1 = mc.fontRendererObj.listFormattedStringToWidth(to.header.getFormattedText(), width - 50);

      for (String s : list1) {
        menuWidth = Math.max(menuWidth, mc.fontRendererObj.getStringWidth(s));
      }
    }

    if (to.footer != null) {
      list2 = mc.fontRendererObj.listFormattedStringToWidth(to.footer.getFormattedText(), width - 50);

      for (String s2 : list2) {
        menuWidth = Math.max(menuWidth, mc.fontRendererObj.getStringWidth(s2));
      }
    }

    // Indicator space transforms
    userWidth += INDICATOR_TOTAL;
    userX -= INDICATOR_TOTAL * userRows / 2;
    menuWidth += INDICATOR_TOTAL * userRows;

    if (list1 != null) {
      drawRect(width / 2 - menuWidth / 2 - 1, menuY - 1, width / 2 + menuWidth / 2 + 1, menuY + list1.size() * mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

      for (String s3 : list1) {
        int i2 = mc.fontRendererObj.getStringWidth(s3);
        mc.fontRendererObj.drawStringWithShadow(s3, (float) (width / 2 - i2 / 2), (float) menuY, -1);
        menuY += mc.fontRendererObj.FONT_HEIGHT;
      }

      ++menuY;
    }

    drawRect(width / 2 - menuWidth / 2 - 1, menuY - 1, width / 2 + menuWidth / 2 + 1, menuY + i4 * 9, Integer.MIN_VALUE);

    for (int k4 = 0; k4 < l3; ++k4) {
      int l4 = k4 / i4;
      int i5 = k4 % i4;
      int j2 = userX + l4 * userWidth + l4 * 5;
      int k2 = menuY + i5 * 9;
      drawRect(j2, k2, j2 + userWidth, k2 + 8, 553648127);
      GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
      GlStateManager.enableAlpha();
      GlStateManager.enableBlend();
      GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);

      if (k4 < list.size()) {
        NetworkPlayerInfo networkplayerinfo1 = (NetworkPlayerInfo) list.get(k4);
        String s1 = to.getPlayerName(networkplayerinfo1);
        GameProfile gameprofile = networkplayerinfo1.getGameProfile();

        if (flag) {
          EntityPlayer entityplayer = mc.theWorld.getPlayerEntityByUUID(gameprofile.getId());
          boolean flag1 = entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.CAPE) && (gameprofile.getName().equals("Dinnerbone") || gameprofile.getName().equals("Grumm"));
          mc.getTextureManager().bindTexture(networkplayerinfo1.getLocationSkin());
          int l2 = 8 + (flag1 ? 8 : 0);
          int i3 = 8 * (flag1 ? -1 : 1);
          Gui.drawScaledCustomSizeModalRect(j2, k2, 8.0F, (float) l2, 8, i3, 8, 8, 64.0F, 64.0F);

          if (entityplayer != null && entityplayer.isWearing(EnumPlayerModelParts.HAT)) {
            int j3 = 8 + (flag1 ? 8 : 0);
            int k3 = 8 * (flag1 ? -1 : 1);
            Gui.drawScaledCustomSizeModalRect(j2, k2, 40.0F, (float) j3, 8, k3, 8, 8, 64.0F, 64.0F);
          }

          j2 += 9;
        }

        if (networkplayerinfo1.getGameType() == WorldSettings.GameType.SPECTATOR) {
          s1 = TextFormatting.ITALIC + s1;
          mc.fontRendererObj.drawStringWithShadow(s1, (float) j2, (float) k2, -1862270977);
        } else {
          mc.fontRendererObj.drawStringWithShadow(s1, (float) j2, (float) k2, -1);
        }

        // Indicator rendering
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(indicators);
        int indicatorRecIndex = RecMod.instance.isPlayerRecording(networkplayerinfo1.getGameProfile().getName()) ? IndexReference.ICON_RED_INDEX : IndexReference.ICON_GRAY_INDEX;
        int indicatorStrIndex = RecMod.instance.isPlayerStreaming(networkplayerinfo1.getGameProfile().getName()) ? IndexReference.ICON_GREEN_INDEX : IndexReference.ICON_GRAY_INDEX;
        if (indicatorRecIndex != IndexReference.ICON_DISABLE_INDEX)
          drawTexturedModalRect(j2 - (flag ? 9 : 0) + userWidth - INDICATOR_TOTAL + INDICATOR_PADDING, k2, indicatorRecIndex * 8, (int) Math.floor(indicatorRecIndex / 32) * 8 + IndexReference.RES_SSD, INDICATOR_WIDTH, INDICATOR_WIDTH);
        drawTexturedModalRect(j2 - (flag ? 9 : 0) + userWidth - INDICATOR_TOTAL + INDICATOR_PADDING * 2 + INDICATOR_WIDTH, k2, indicatorStrIndex * 8, (int) Math.floor(indicatorStrIndex / 32) * 8 + IndexReference.RES_SSD, INDICATOR_WIDTH, INDICATOR_WIDTH);

        if (scoreObjectiveIn != null && networkplayerinfo1.getGameType() != WorldSettings.GameType.SPECTATOR) {
          int k5 = j2 + namePingGap + 1;
          int l5 = k5 + l;

          if (l5 - k5 > 5) {
            to.drawScoreboardValues(scoreObjectiveIn, k2, gameprofile.getName(), k5, l5, networkplayerinfo1);
          }
        }

        to.drawPing(userWidth - INDICATOR_TOTAL, j2 - (flag ? 9 : 0), k2, networkplayerinfo1);
      }
    }

    if (list2 != null) {
      menuY = menuY + i4 * 9 + 1;
      drawRect(width / 2 - menuWidth / 2 - 1, menuY - 1, width / 2 + menuWidth / 2 + 1, menuY + list2.size() * mc.fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);

      for (String s4 : list2) {
        int j5 = mc.fontRendererObj.getStringWidth(s4);
        mc.fontRendererObj.drawStringWithShadow(s4, (float) (width / 2 - j5 / 2), (float) menuY, -1);
        menuY += mc.fontRendererObj.FONT_HEIGHT;
      }
    }
  }

  @SubscribeEvent
  public void renderGameOverlay(RenderGameOverlayEvent.Post event) {
    Minecraft mc = Minecraft.getMinecraft();
    ResourceLocation indicators = Minecraft.isFancyGraphicsEnabled() ? indicatorsHigh : indicatorsLow;

    if (event.type == ElementType.ALL && RecMod.instance.showSelf && mc.currentScreen == null) {
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
