package davidt.msp.client;

import net.minecraft.client.world.*;
import net.minecraft.world.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.world.*;


public class ClientWorldHandler
{
   public static ClientWorld world = null;
   
   
   public static void register()
   {
      MinecraftForge.EVENT_BUS.addListener(ClientWorldHandler::worldUnload);
   }
   
   
   private static void worldUnload(WorldEvent.Unload unloadEvent)
   {
      IWorld iWorld = unloadEvent.getWorld();
      
      if (iWorld.isRemote())
      {
         world = (ClientWorld) iWorld;
      }
   }
}
