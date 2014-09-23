package fuj1n.recmod;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import fuj1n.recmod.client.event.*;
import fuj1n.recmod.client.keybind.KeyHandlerRecMod;
import fuj1n.recmod.command.CommandRec;
import fuj1n.recmod.network.*;
import fuj1n.recmod.network.packet.*;
import java.io.*;
import java.util.HashMap;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.*;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;

@Mod(name = "Recording Status Mod", version = "v1.4", modid = "fuj1n.recmod", acceptableRemoteVersions = "*", canBeDeactivated = false)
public class RecMod {

	@Instance("fuj1n.recmod")
	public static RecMod instance;

	public static final PacketPipeline packetPipeline = new PacketPipeline();

	private static HashMap<String, Boolean> recorders = new HashMap<String, Boolean>();
	private static HashMap<String, Boolean> streamers = new HashMap<String, Boolean>();

	public boolean showSelf = true;

	//Keyboard stuffs
	public boolean enableKeys = false;
	public int keyRec = 44;
	public int keyStr = 45;

	public String sheetLocation = "recmod:textures/sheets/indicators.png";

	public boolean showSelfDef = false;

	public File configFile;

	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(new PlayerTracker());

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

		configFile = new File(event.getModConfigurationDirectory(), "recmod.ui");

		if (event.getSide() == Side.CLIENT) {
			readFromFile();
			MinecraftForge.EVENT_BUS.register(new EventRenderGame());
			FMLCommonHandler.instance().bus().register(new EventClientEntityLogin());
			FMLCommonHandler.instance().bus().register(new KeyHandlerRecMod());
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		packetPipeline.initialise();

		PacketPipeline pp = packetPipeline;
		//Packet Registration
		pp.registerPacket(PacketUpdatePlayerStatus.class);
		pp.registerPacket(PacketRemovePlayer.class);
		pp.registerPacket(PacketClientCommand.class);
	}

	@EventHandler
	public void postinit(FMLPostInitializationEvent event) {
		packetPipeline.postInitialise();
	}

	@EventHandler
	public void serverStart(FMLServerStartedEvent event) {
		if (MinecraftServer.getServer() == null) {
			return;
		}

		recorders = new HashMap<String, Boolean>();
		streamers = new HashMap<String, Boolean>();

		ServerCommandManager handler = (ServerCommandManager) MinecraftServer.getServer().getCommandManager();
		handler.registerCommand(new CommandRec());
	}

	public void updatePlayerInformation(String username, int type, boolean flag) {
		HashMap<String, Boolean> modifyMap;
		if (type == 0) {
			modifyMap = recorders;
		} else {
			modifyMap = streamers;
		}

		modifyMap.put(username, flag);
	}

	public boolean isPlayerRecording(String username) {
		if (username == null) {
			return false;
		}

		return recorders.get(username) != null ? recorders.get(username) : false;
	}

	public boolean isPlayerStreaming(String username) {
		if (username == null) {
			return false;
		}
		return streamers.get(username) != null ? streamers.get(username) : false;
	}

	public void onUIStateChanged() {
		writeToFile();
	}

	public void onSheetChanged() {
		writeToFile();
	}

	public void readFromFile() {
		if (!configFile.exists()) {
			writeToFile();
			
			return;
		}

		try {
			BufferedReader b = new BufferedReader(new FileReader(configFile));
			String line1 = b.readLine();
			String line2 = b.readLine();
			String line3 = b.readLine();
			String line4 = b.readLine();
			String line5 = b.readLine();

			showSelfDef = convertToBoolean(line1, true);
			sheetLocation = line2 != null && !line2.equals("") ? line2 : sheetLocation;
			enableKeys = convertToBoolean(line3, false);
			keyRec = convertToInteger(line4, 44);
			keyStr = convertToInteger(line5, 45);

			b.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			//Don't want the massive stacktrace if the file read fails.
			//e.printStackTrace();
		}
	}

	public int convertToInteger(String s, int def) {
		try {
			return Integer.parseInt(s);
		} catch (Exception e) {
		}
		return def;
	}

	public boolean convertToBoolean(String s, boolean def) {
		try {
			return Boolean.parseBoolean(s);
		} catch (Exception e) {
		}
		return def;
	}

	public void writeToFile() {
		configFile.delete();
		try {
			configFile.createNewFile();
			BufferedWriter b = new BufferedWriter(new FileWriter(configFile));
			b.write(Boolean.toString(showSelfDef));
			b.newLine();
			b.write(sheetLocation);
			b.newLine();
			b.write(Boolean.toString(enableKeys));
			b.newLine();
			b.write(Integer.toString(keyRec));
			b.newLine();
			b.write(Integer.toString(keyStr));
			b.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void removeUnneededData(String username) {
		recorders.remove(username);
		streamers.remove(username);
	}

	public void sendDataToPlayer(EntityPlayer player) {
		for (int i = 0; i < recorders.size(); i++) {
			sendPacket(player, recorders.keySet().toArray()[i].toString(), 0, Boolean.parseBoolean(recorders.values().toArray()[i].toString()));
		}

		for (int i = 0; i < streamers.size(); i++) {
			sendPacket(player, streamers.keySet().toArray()[i].toString(), 1, Boolean.parseBoolean(streamers.values().toArray()[i].toString()));
		}
	}

	public void sendPacket(EntityPlayer target, String player, int type, boolean flag) {
		PacketUpdatePlayerStatus pckt = new PacketUpdatePlayerStatus(player, type, flag);
		if (target instanceof EntityPlayerMP) {
			packetPipeline.sendTo(pckt, (EntityPlayerMP) target);
		} else {
		}
	}

}
