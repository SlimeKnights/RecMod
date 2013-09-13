package fuj1n.recmod.network;

import javax.sql.CommonDataSource;

import cpw.mods.fml.relauncher.Side;

import cpw.mods.fml.common.FMLCommonHandler;

import fuj1n.recmod.RecMod;

import cpw.mods.fml.common.network.*;
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
		}
	}
}
