package fuj1n.recmod;

import fuj1n.recmod.command.CommandRec;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.*;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import fuj1n.recmod.client.event.*;
import fuj1n.recmod.client.keybind.KeyHandlerRecMod;
import fuj1n.recmod.network.*;
import java.io.*;
import java.util.*;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

@Mod(name="Recording Mod", version="1.2", modid="fuj1n.recmod")
@NetworkMod(clientSideRequired=true, channels = {"recModData", "recModDataC", "recModUI"}, packetHandler=PacketHandler.class, serverSideRequired=false)
public class RecMod {	
	@Instance("fuj1n.recmod")
	public static RecMod instance;
	
	private static HashMap<String, Boolean> recorders = new HashMap<String, Boolean>();
	private static HashMap<String, Boolean> streamers = new HashMap<String, Boolean>();
	
	public boolean showSelf = true;
	public boolean showUI = false;
	
	public String sheetLocation = "recmod:textures/sheets/indicators.png";
	
	public boolean showSelfDef = false;
		
	public File configFile;
	
	@EventHandler
	public void preinit(FMLPreInitializationEvent event){
		GameRegistry.registerPlayerTracker(new PlayerTracker());
		configFile = new File(event.getModConfigurationDirectory(), "recmod.ui");
		
		if(event.getSide() == Side.CLIENT){
			readFromFile();
			MinecraftForge.EVENT_BUS.register(new EventRenderGame());
			MinecraftForge.EVENT_BUS.register(new EventEntityJoinWorld());
			
			//WIP
//			KeyHandlerRecMod.registerSelf();
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
	
	public void onUIStateChanged(){
		writeToFile();
	}
	
	public void onSheetChanged(){
		writeToFile();
	}
	
	public void readFromFile() {
		if(!configFile.exists()){
			return;
		}
		
		try {
			BufferedReader b = new BufferedReader(new FileReader(configFile));
			String line1 = b.readLine();
			String line2 = b.readLine();
			String line3 = b.readLine();
			
			showSelfDef = convertToBoolean(line1);
			showUI = convertToBoolean(line2);
			sheetLocation = line3 != null && !line3.equals("") ? line3 : sheetLocation;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			//Don't want the massive stacktrace if the file read fails.
			//e.printStackTrace();
		}
	}
	
	public boolean convertToBoolean(String s){
		try{
			return Boolean.parseBoolean(s);
		}catch(Exception e){}
		return false;
	}
	
	public void writeToFile() {
		configFile.delete();
		try {
			configFile.createNewFile();
			BufferedWriter b = new BufferedWriter(new FileWriter(configFile));
			b.write(Boolean.toString(showSelfDef));
			b.newLine();
			b.write(Boolean.toString(showUI));
			b.newLine();
			b.write(sheetLocation);
			b.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int calculateNumber(){
		ArrayList<String> list = new ArrayList<String>();
		
		
		
		return list.size();
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
