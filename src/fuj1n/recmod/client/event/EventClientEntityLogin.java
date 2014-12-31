package fuj1n.recmod.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import fuj1n.recmod.RecMod;
import net.minecraft.client.Minecraft;

public class EventClientEntityLogin
{

	@SubscribeEvent
	public void onEntityLogin (PlayerLoggedInEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if (RecMod.instance.showMode != 0)
		{
			switch (RecMod.instance.showMode)
			{
			case 1:
				RecMod.instance.showSelf = true;
				break;
			case 2:
				RecMod.instance.showSelf = !mc.isSingleplayer();
				break;
			case 3:
				RecMod.instance.showSelf = RecMod.instance.recState || RecMod.instance.strState;
				break;
			}
		}

		RecMod.instance.mapsDirty = true;
	}

}
