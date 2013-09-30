package fuj1n.recmod.client.event;

import fuj1n.recmod.RecMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.ForgeSubscribe;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class EventEntityJoinWorld {

	@ForgeSubscribe
	public void onEntityJoin(EntityJoinWorldEvent event){
		if(event.entity instanceof EntityPlayer){
			Minecraft mc = Minecraft.getMinecraft().getMinecraft();
			RecMod.instance.showSelf = !mc.isSingleplayer() && !RecMod.instance.showSelfDef;
		}
	}
	
}
