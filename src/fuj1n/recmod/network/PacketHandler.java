package fuj1n.recmod.network;

import cpw.mods.fml.common.network.*;
import fuj1n.recmod.RecMod;
import java.io.*;
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
		} else if (packet.channel.equals("recModUI")){
			boolean isSelf = false;
			boolean isOverride = false;
			
			try{
				isSelf = inputStream.readBoolean();
				isOverride = inputStream.readBoolean();
			}catch(IOException e){
				e.printStackTrace();
			}
			
			if(isSelf){
				RecMod.instance.showSelf = !RecMod.instance.showSelf;
				RecMod.instance.showSelfDef = isOverride ? !RecMod.instance.showSelfDef : RecMod.instance.showSelfDef;
				
				RecMod.instance.onUIStateChanged();
			}else{
				RecMod.instance.showUI = !RecMod.instance.showUI;

				RecMod.instance.onUIStateChanged();
			}
		}
	}
}
