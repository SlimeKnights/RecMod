package fuj1n.recmod.client.event;

import fuj1n.recmod.RecMod;
import fuj1n.recmod.client.ClientChecks;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

public class EventClientEntityLogin {

  @SubscribeEvent
  public void onEntityLogin(PlayerLoggedInEvent event) {
    Minecraft mc = Minecraft.getMinecraft();

    if(RecMod.instance.showMode != 0) {
      switch(RecMod.instance.showMode) {
        case 1:
          RecMod.instance.showSelf = true;
          break;
        case 2:
          RecMod.instance.showSelf = !ClientChecks.isSingleplayer();
          break;
        case 3:
          RecMod.instance.showSelf = RecMod.instance.recState || RecMod.instance.strState;
          break;
      }
    }

    RecMod.instance.mapsDirty = true;
  }
}
