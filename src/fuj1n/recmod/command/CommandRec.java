package fuj1n.recmod.command;

import java.util.*;

import fuj1n.recmod.RecMod;
import fuj1n.recmod.network.packet.PacketClientCommand;
import net.minecraft.command.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;

public class CommandRec extends CommandBase
{

    @Override
    public int getRequiredPermissionLevel ()
    {
        return 0;
    }

    @Override
    public String getName ()
    {
        return "rec";
    }

    @Override
    public String getCommandUsage (ICommandSender icommandsender)
    {
        icommandsender.addChatMessage(new ChatComponentText("\u00A7cRec Usage: "));
        icommandsender.addChatMessage(new ChatComponentText("\u00A7c{no oprands} (displays an easy to use GUI)"));
        icommandsender.addChatMessage(new ChatComponentText("\u00A7c<r/s> (toggle recording or streaming)"));
        return "End Rec Usage";
    }

    public void onWrongUsage (ICommandSender icommandsender)
    {
        getCommandUsage(icommandsender);
    }

    @Override
    public void execute (ICommandSender icommandsender, String[] astring) throws CommandException
    {
        if (!(icommandsender instanceof EntityPlayer))
        {
            throw new CommandException("Only players are allowed to perform this command.", new Object[0]);
        }

        EntityPlayer player = (EntityPlayer) icommandsender;

        sendClientProcess(player, astring);

        if (astring.length == 0)
        {
        }
        else if (astring.length == 1 && (astring[0].equals("r") || astring[0].equals("s")))
        {
        }
        else
        {
            onWrongUsage(icommandsender);
            return;
        }
    }

    public void sendClientProcess (EntityPlayer p, String[] params)
    {
        if (p instanceof EntityPlayerMP)
        {
            PacketClientCommand pckt = new PacketClientCommand(params);
            RecMod.packetPipeline.sendTo(pckt, (EntityPlayerMP) p);
        }
    }

    @Override
    public List addTabCompletionOptions (ICommandSender par1ICommandSender, String[] par2ArrayOfStr, BlockPos position)
    {
        List l = new ArrayList();
        if (par2ArrayOfStr.length == 1)
        {
            l.add("r");
            l.add("s");
        }

        return l;
    }

    @Override
    public boolean canCommandSenderUse (ICommandSender icommandsender)
    {
        return true;
    }

}
