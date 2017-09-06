package fuj1n.recmod.command;

import fuj1n.recmod.RecMod;
import fuj1n.recmod.network.packet.PacketClientCommand;
import net.minecraft.command.*;
import net.minecraft.entity.player.*;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.*;

public class CommandRec extends CommandBase
{

    @Override public int getRequiredPermissionLevel ()
    {
        return 0;
    }

    @Override public String getName ()
    {
        return "rec";
    }

    @Override public String getUsage (ICommandSender icommandsender)
    {
        icommandsender.sendMessage(new TextComponentTranslation("\u00A7cRec Usage: "));
        icommandsender.sendMessage(new TextComponentTranslation("\u00A7c{no oprands} (displays an easy to use GUI)"));
        icommandsender.sendMessage(new TextComponentTranslation("\u00A7c<r/s> (toggle recording or streaming)"));
        return "End Rec Usage";
    }

    public void onWrongUsage (ICommandSender icommandsender)
    {
        getUsage(icommandsender);
    }

    @Override public void execute (MinecraftServer server, ICommandSender icommandsender, String[] astring) throws CommandException
    {
        if (!(icommandsender instanceof EntityPlayer))
        {
            throw new CommandException("Only players are allowed to perform this command.", new Object[0]);
        }

        EntityPlayer player = (EntityPlayer) icommandsender;

        if (astring.length != 0)
        {
            if (astring.length != 1 || (!astring[0].equals("r") && !astring[0].equals("s")))
            {
                onWrongUsage(icommandsender);
                return;
            }
        }

        sendClientProcess(player, astring);
    }

    public void sendClientProcess (EntityPlayer p, String[] params)
    {
        if (p instanceof EntityPlayerMP)
        {
            PacketClientCommand pckt = new PacketClientCommand(params);
            RecMod.packetPipeline.sendTo(pckt, (EntityPlayerMP) p);
        }
    }

    @Override public List<String> getTabCompletions (MinecraftServer server, ICommandSender sender, String[] args, BlockPos position)
    {
        List<String> l = new ArrayList<String>();
        if (args.length == 1)
        {
            l.add("r");
            l.add("s");
        }

        return l;
    }

    @Override public boolean checkPermission (MinecraftServer server, ICommandSender sender)
    {
        return true;
    }
}
