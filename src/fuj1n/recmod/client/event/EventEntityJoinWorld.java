package fuj1n.recmod.client.event;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fuj1n.recmod.RecMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class EventEntityJoinWorld {

	@SubscribeEvent
	public void onEntityJoin(EntityJoinWorldEvent event){
		if(event.entity instanceof EntityPlayer){
			Minecraft mc = Minecraft.getMinecraft().getMinecraft();
			RecMod.instance.showSelf = !mc.isSingleplayer() && !RecMod.instance.showSelfDef;
		}
	}
	
}
