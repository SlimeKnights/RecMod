package fuj1n.recmod;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import fuj1n.recmod.client.command.CommandRec;
import fuj1n.recmod.client.event.EventRenderGame;
import fuj1n.recmod.network.*;
import java.io.*;
import java.util.HashMap;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

@Mod(name="Recording Mod", version="1.0", modid="fuj1n.recmod")
@NetworkMod(clientSideRequired=true, channels = {"recModData", "recModDataC"}, packetHandler=PacketHandler.class, serverSideRequired=false)
public class RecMod {	
	@Instance("fuj1n.recmod")
	public static RecMod instance;
	
	private static HashMap<String, Boolean> recorders = new HashMap<String, Boolean>();
	private static HashMap<String, Boolean> streamers = new HashMap<String, Boolean>();
		
	@EventHandler
	public void preinit(FMLPreInitializationEvent event){
		GameRegistry.registerPlayerTracker(new PlayerTracker());
		
		if(event.getSide() == Side.CLIENT){
			MinecraftForge.EVENT_BUS.register(new EventRenderGame());
		}
	}
	
	@EventHandler
	public void init(FMLInitializationEvent event){
		
	}
	
	@EventHandler
	public void postinit(FMLPostInitializationEvent event){
		
	}
	
	@EventHandler
	public void serverStart(FMLServerStartedEvent event){
		if (MinecraftServer.getServer() == null) {
			return;
		}
		
		recorders = new HashMap<String, Boolean>();
		streamers = new HashMap<String, Boolean>();
//		RecMod.instance.scoreboard = MinecraftServer.getServer().worldServerForDimension(0).getScoreboard();
//		if(RecMod.instance.scoreboard == null){
//			System.out.println("[Recording Mod] Unable to obtain scoreboard");
//			return;
//		}
//		if(RecMod.instance.scoreboard.getObjective("isRecording") == null){
//			RecMod.instance.scoreboard.func_96535_a("isRecording", (ScoreObjectiveCriteria)ScoreObjectiveCriteria.field_96643_a.get("dummy"));
//		}
//		if(RecMod.instance.scoreboard.getObjective("isStreaming") == null){
//			RecMod.instance.scoreboard.func_96535_a("isStreaming", (ScoreObjectiveCriteria)ScoreObjectiveCriteria.field_96643_a.get("dummy"));
//		}
		
		ServerCommandManager handler = (ServerCommandManager)MinecraftServer.getServer().getCommandManager();
		handler.registerCommand(new CommandRec());
	}
	
	public void updatePlayerInformation(String username, int type, boolean flag){
		HashMap<String, Boolean> modifyMap;
		if(type == 0){
			modifyMap = recorders;
		}else{
			modifyMap = streamers;
		}
		
		modifyMap.put(username, flag);
	}
	
	public boolean isPlayerRecording(String username){
		if(username == null){
			return false;
		}

		return recorders.get(username) != null ? recorders.get(username) : false;
	}
	
	public boolean isPlayerStreaming(String username){
		if(username == null){
			return false;
		}
		return streamers.get(username) != null ? streamers.get(username) : false;
	}
	
	public void removeUnneededData(String username){
		recorders.remove(username);
		streamers.remove(username);
	}
	
	public void sendDataToPlayer(EntityPlayer player){
		for(int i = 0; i < recorders.size(); i++){
			sendPacket(player, recorders.keySet().toArray()[i].toString(), 0, Boolean.parseBoolean(recorders.values().toArray()[i].toString()));
		}
		
		for(int i = 0; i < streamers.size(); i++){
			sendPacket(player, streamers.keySet().toArray()[i].toString(), 1, Boolean.parseBoolean(streamers.values().toArray()[i].toString()));
		}
	}
	
	public void sendPacket(EntityPlayer target, String player, int type, boolean flag){
		ByteArrayOutputStream bos = new ByteArrayOutputStream(8);
		DataOutputStream outputStream = new DataOutputStream(bos);
		try {
			outputStream.writeUTF(player);
			outputStream.writeInt(type);
			outputStream.writeBoolean(flag);
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		Packet250CustomPayload packet = new Packet250CustomPayload();
		packet.channel = "recModData";
		packet.data = bos.toByteArray();
		packet.length = bos.size();
		PacketDispatcher.sendPacketToPlayer(packet, (Player)target);
	}
	
}