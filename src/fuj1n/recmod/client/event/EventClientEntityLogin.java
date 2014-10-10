package fuj1n.recmod.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import fuj1n.recmod.RecMod;
import net.minecraft.client.Minecraft;

public class EventClientEntityLogin {

	@SubscribeEvent
	public void onEntityLogin(PlayerLoggedInEvent event) {
		Minecraft mc = Minecraft.getMinecraft();
		RecMod.instance.showSelf = !mc.isSingleplayer() && !RecMod.instance.showSelfDef;

		RecMod.instance.mapsDirty = true;
	}

}
