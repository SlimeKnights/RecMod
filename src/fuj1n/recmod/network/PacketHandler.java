package fuj1n.recmod.network;

import net.minecraft.util.ChatMessageComponent;

import cpw.mods.fml.common.network.*;
import fuj1n.recmod.RecMod;
import java.io.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;

public class PacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		if (packet.channel.equals("recModData")) {
			String playerName = "";
			int typeData = 0;
			boolean flag = false;

			try {
				playerName = inputStream.readUTF();
				typeData = inputStream.readInt();
				flag = inputStream.readBoolean();

			} catch (IOException e) {
				e.printStackTrace();
			}
			RecMod.instance.updatePlayerInformation(playerName, typeData, flag);
		} else if (packet.channel.equals("recModDataC")) {
			String playerName = "";
			
			try{
				playerName = inputStream.readUTF();
			}catch(IOException e){
				e.printStackTrace();
			}
			RecMod.instance.removeUnneededData(playerName);
		} else if (packet.channel.equals("recModUI")) {
			int type = -1;
			try {
				type = inputStream.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (type == 0) {
				boolean isSelf = false;
				boolean isOverride = false;

				try {
					isSelf = inputStream.readBoolean();
					isOverride = inputStream.readBoolean();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (isSelf) {
					RecMod.instance.showSelf = !RecMod.instance.showSelf;
					RecMod.instance.showSelfDef = isOverride ? !RecMod.instance.showSelfDef : RecMod.instance.showSelfDef;

					RecMod.instance.onUIStateChanged();
				} else {
					RecMod.instance.showUI = !RecMod.instance.showUI;

					RecMod.instance.onUIStateChanged();
				}
			}else if(type == 1){
				String newSheet = RecMod.instance.sheetLocation;
				
				try {
					newSheet = inputStream.readUTF();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
				net.minecraft.util.ResourceLocation testLocation = new net.minecraft.util.ResourceLocation(newSheet);
//				if(!new File(testLocation.getResourcePath()).exists()){
//					if(player instanceof EntityPlayer){
//						((EntityPlayer)player).sendChatToPlayer(ChatMessageComponent.createFromText("Location not found: " + testLocation.getResourcePath()));
//					}
//				}else{
					RecMod.instance.sheetLocation = newSheet;
					RecMod.instance.onSheetChanged();
//				}
			}
		}
	}
}
