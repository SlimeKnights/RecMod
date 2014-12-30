package fuj1n.recmod;

import cpw.mods.fml.common.*;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.event.*;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.Side;
import fuj1n.recmod.client.event.*;
import fuj1n.recmod.command.CommandRec;
import fuj1n.recmod.legacy.OldConfigConverter;
import fuj1n.recmod.network.*;
import fuj1n.recmod.network.packet.*;
import java.io.File;
import java.util.HashMap;
import net.minecraft.command.ServerCommandManager;
import net.minecraft.entity.player.*;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(name = "Recording Status Mod", version = "v1.5", modid = "fuj1n.recmod", acceptableRemoteVersions = "*", canBeDeactivated = false)
public class RecMod {

	@Instance("fuj1n.recmod")
	public static RecMod instance;

	public static final PacketPipeline packetPipeline = new PacketPipeline();

	private static HashMap<String, Boolean> recorders = new HashMap<String, Boolean>();
	private static HashMap<String, Boolean> streamers = new HashMap<String, Boolean>();

	public boolean showSelf = true;
	public boolean keepState = false;
	public boolean recState = false, strState = false;

	// Keyboard stuffs
	public boolean enableKeys = false;
	public int keyRec = 44;
	public int keyStr = 45;

	// Bobber controls
	public int showMode = 3;
	public int posMode = 1;
	public int absX = 0, absY = 0;

	public File configFile;
	public Configuration config;

	public boolean mapsDirty = false;

	@EventHandler
	public void preinit(FMLPreInitializationEvent event) {
		FMLCommonHandler.instance().bus().register(new PlayerTracker());

		NetworkRegistry.INSTANCE.registerGuiHandler(instance, new GuiHandler());

		OldConfigConverter.configFile = new File(event.getModConfigurationDirectory(), "recmod.ui");

		if (event.getSide() == Side.CLIENT) {
			configFile = event.getSuggestedConfigurationFile();

			if (OldConfigConverter.configFile.exists() && !configFile.exists()) {
				OldConfigConverter.convert();
				OldConfigConverter.configFile.delete();
			} else {
				if (OldConfigConverter.configFile.exists()) {
					OldConfigConverter.configFile.delete();
				}

				instanciateConfig();
			}

			readFromFile();
			MinecraftForge.EVENT_BUS.register(new EventRenderGame());
			FMLCommonHandler.instance().bus().register(new EventClientEntityLogin());
			FMLCommonHandler.instance().bus().register(new EventClientTick());
		}
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		packetPipeline.initialise();

		PacketPipeline pp = packetPipeline;
		// Packet Registration
		pp.registerPacket(PacketUpdatePlayerStatus.class);
		pp.registerPacket(PacketRemovePlayer.class);
		pp.registerPacket(PacketClientCommand.class);
		pp.registerPacket(PacketEndOfInitialTransmission.class);
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

		clearMaps();

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

	public void instanciateConfig() {
		config = new Configuration(configFile, true);
	}

	public void readFromFile() {
		config.load();

		showMode = config.getInt("Bobber AutoShow Mode", "Interface", showMode, 0, 3, "The automatic behaviour of the ingame bobber visibility.");
		posMode = config.getInt("Bobber Position Mode", "Interface", posMode, 0, 5, "The positioning mode of the bobber.");
		absX = config.getInt("Bobber Position Absolute X", "Interface", absX, -4096, 4096, "The absolute X position of the bobber. (only for absolute position mode)");
		absY = config.getInt("Bobber Position Absolute Y", "Interface", absY, -2160, 2160, "The absolute Y position of the bobber. (only for absolute position mode)");
		enableKeys = config.getBoolean("Enable keyboard", "Keyboard", enableKeys, "Whether the keyboard shortcuts are enabled.");
		keyRec = config.getInt("Record Key", "Keyboard", keyRec, -1338, 250, "The key that will toggle recording.");
		keyStr = config.getInt("Stream Key", "Keyboard", keyStr, -1338, 250, "The key that will toggle streaming.");
		keepState = config.getBoolean("Keep state", "General", keepState, "Whether the recording state is kept throughout the gaming session.");

		if (config.hasChanged()) {
			config.save();
		}
	}

	public void writeToFile() {
		// Delete the config to force update
		configFile.delete();

		readFromFile();
	}

	public void removeUnneededData(String username) {
		recorders.remove(username);
		streamers.remove(username);
	}

	public void clearMaps() {
		recorders.clear();
		streamers.clear();
	}

	public void sendDataToPlayer(EntityPlayer player) {
		for (int i = 0; i < recorders.size(); i++) {
			sendPacket(player, recorders.keySet().toArray()[i].toString(), 0, Boolean.parseBoolean(recorders.values().toArray()[i].toString()));
		}

		for (int i = 0; i < streamers.size(); i++) {
			sendPacket(player, streamers.keySet().toArray()[i].toString(), 1, Boolean.parseBoolean(streamers.values().toArray()[i].toString()));
		}

		if (player instanceof EntityPlayerMP) {
			PacketEndOfInitialTransmission pckt = new PacketEndOfInitialTransmission();
			packetPipeline.sendTo(pckt, (EntityPlayerMP) player);
		}
	}

	public void sendPacket(EntityPlayer target, String player, int type, boolean flag) {
		if (target instanceof EntityPlayerMP) {
			PacketUpdatePlayerStatus pckt = new PacketUpdatePlayerStatus(player, type, flag);
			packetPipeline.sendTo(pckt, (EntityPlayerMP) target);
		} else {
		}
	}

}
