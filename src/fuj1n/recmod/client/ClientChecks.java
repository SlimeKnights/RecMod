package fuj1n.recmod.client;

import net.minecraft.client.Minecraft;

public class ClientChecks {

  public static boolean isSingleplayer() {
    return Minecraft.getMinecraft().isSingleplayer();
  }
}
