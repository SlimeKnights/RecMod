package fuj1n.recmod.client.event;

import fuj1n.recmod.RecMod;
import fuj1n.recmod.network.packet.PacketUpdatePlayerStatus;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.Type;
import org.lwjgl.input.Keyboard;

public class EventClientTick {

  boolean recDown = false;
  boolean strDown = false;

  Minecraft mc = Minecraft.getMinecraft();

  @SubscribeEvent
  public void onTick(ClientTickEvent event) {
    if (event.type == Type.CLIENT && event.phase == Phase.END && mc.theWorld == null && mc.thePlayer == null && RecMod.instance.mapsDirty) {

      if (RecMod.instance.keepState) {
        String player = mc.getSession().getUsername();
        RecMod.instance.recState = RecMod.instance.isPlayerRecording(player);
        RecMod.instance.strState = RecMod.instance.isPlayerStreaming(player);
      }

      RecMod.instance.clearMaps();

      RecMod.instance.mapsDirty = false;
    }

    if (event.type == Type.CLIENT && event.phase == Phase.START && mc.theWorld != null && mc.thePlayer != null && RecMod.instance.showMode == 3) {
      String player = mc.thePlayer.getName();
      RecMod.instance.showSelf = RecMod.instance.isPlayerRecording(player) || RecMod.instance.isPlayerStreaming(player);
    }

    if (!RecMod.instance.enableKeys) {
      return;
    }
    if (event.type == Type.CLIENT && event.phase == Phase.START && mc.theWorld != null && mc.currentScreen == null) {
      keyTick();
    }
  }

  public void keyTick() {
    if (Keyboard.isKeyDown(RecMod.instance.keyRec)) {
      if (!recDown) {
        recDown = true;
      }
    } else {
      if (recDown) {
        recDown = false;
        RecMod.instance.updatePlayerInformation(mc.thePlayer.getName(), 0, !RecMod.instance.isPlayerRecording(mc.thePlayer.getName()));
        RecMod.packetPipeline.sendToServer(new PacketUpdatePlayerStatus(mc.thePlayer.getName(), 0, RecMod.instance.isPlayerRecording(mc.thePlayer.getName())));
      }
    }

    if (Keyboard.isKeyDown(RecMod.instance.keyStr)) {
      if (!strDown) {
        strDown = true;
      }
    } else {
      if (strDown) {
        strDown = false;
        RecMod.instance.updatePlayerInformation(mc.thePlayer.getName(), 1, !RecMod.instance.isPlayerStreaming(mc.thePlayer.getName()));
        RecMod.packetPipeline.sendToServer(new PacketUpdatePlayerStatus(mc.thePlayer.getName(), 1, RecMod.instance.isPlayerStreaming(mc.thePlayer.getName())));
      }
    }
  }
}
