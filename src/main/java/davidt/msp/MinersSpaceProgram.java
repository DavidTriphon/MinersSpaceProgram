package davidt.msp;

import davidt.msp.dimension.mod.*;
import net.minecraft.block.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.*;
import net.minecraftforge.fml.javafmlmod.*;
import org.apache.logging.log4j.*;


@Mod(MinersSpaceProgram.MOD_ID)
public class MinersSpaceProgram
{
   public static final String MOD_ID   = "miners_space_program";
   public static final String MOD_NAME = "MinersSpaceProgram";
   
   private static final Logger LOGGER = LogManager.getLogger();
   
   
   public MinersSpaceProgram()
   {
      // taken from example mod
      // Register the setup method for modloading
      FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doCommonSetup);
      // Register the doClientStuff method for modloading
      FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientSetup);
      // Register the doClientStuff method for modloading
      FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doServerSetup);
      // Register the enqueueIMC method for modloading
      FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doIMEnqueue);
      // Register the processIMC method for modloading
      FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doIMProcess);
      
      // Link up register callbacks for blocks, item, biomes, dimensions, etc.
      MSPModDimensions.MOD_DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
      
      
      // Register ourselves for server and other game events we are interested in
      MinecraftForge.EVENT_BUS.register(this);
   }
   
   
   // CALLED FIRST
   private void doCommonSetup(final FMLCommonSetupEvent event)
   {
      LOGGER.debug("Common Setup FOR {}", MOD_NAME);
   }
   
   
   // CALLED SECOND
   private void doClientSetup(final FMLClientSetupEvent event)
   {
      LOGGER.debug("Client Setup FOR {}", MOD_NAME);
   }
   
   
   // CALLED SECOND
   private void doServerSetup(final FMLDedicatedServerSetupEvent event)
   {
      LOGGER.debug("Server Setup FOR {}", MOD_NAME);
   }
   
   
   // CALLED THIRD
   private void doIMEnqueue(final InterModEnqueueEvent event)
   {
      LOGGER.debug("InterMod Enqueue FOR {}", MOD_NAME);
   }
   
   
   // CALLED FOURTH
   private void doIMProcess(final InterModProcessEvent event)
   {
      LOGGER.debug("InterMod Process FOR {}", MOD_NAME);
   }
   
   
   // You can use SubscribeEvent and let the Event Bus discover methods to call
   @SubscribeEvent
   public void onServerStarting(FMLServerStartingEvent event)
   {
      // do something when the server starts
      LOGGER.debug("HELLO from server starting");
   }
   
   
   // You can use EventBusSubscriber to automatically subscribe events on the contained class
   // (this is subscribing to the MOD Event bus for receiving Registry Events)
   @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
   public static class RegistryEvents
   {
      @SubscribeEvent
      public static void onBlocksRegistry(final RegistryEvent.Register <Block> blockRegistryEvent)
      {
         // register a new block here
         LOGGER.debug("HELLO from Register Block");
      }
   }
}
