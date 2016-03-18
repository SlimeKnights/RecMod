package fuj1n.recmod.network;

import fuj1n.recmod.RecMod;
import fuj1n.recmod.network.packet.PacketRemovePlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.relauncher.Side;

public class PlayerTracker {

  @SubscribeEvent
  public void onPlayerLogout(PlayerLoggedOutEvent event) {
    if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
      removePlayerName(event.player.getName());
    }
  }

  public void removePlayerName(String name) {
    RecMod.instance.removeUnneededData(name);

    PacketRemovePlayer pckt = new PacketRemovePlayer(name);
    RecMod.packetPipeline.sendToAll(pckt);
  }
}
