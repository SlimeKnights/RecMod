package fuj1n.recmod.network;

import cpw.mods.fml.common.network.IGuiHandler;
import fuj1n.recmod.client.gui.GuiSimple;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler
{

	@Override
	public Object getServerGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}

	@Override
	public Object getClientGuiElement (int ID, EntityPlayer player, World world, int x, int y, int z)
	{
		switch (ID)
		{
		case 0:
			return new GuiSimple(player.getCommandSenderName());
		}

		return null;
	}
}
