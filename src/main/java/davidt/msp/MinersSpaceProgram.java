package davidt.msp;

import net.minecraft.block.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.*;
import net.minecraftforge.eventbus.api.*;
import net.minecraftforge.fml.*;
import net.minecraftforge.fml.common.*;
import net.minecraftforge.fml.event.lifecycle.*;
import net.minecraftforge.fml.event.server.*;
import net.minecraftforge.fml.javafmlmod.*;
import org.apache.logging.log4j.*;

import java.util.stream.*;


@Mod(MinersSpaceProgram.MOD_ID)
public class MinersSpaceProgram
{
   public static final String MOD_ID = "msp";
   public static final String MOD_NAME = "MinersSpaceProgram";
   
   private static final Logger LOGGER = LogManager.getLogger();
   
   
   public MinersSpaceProgram()
   {
      // taken from example mod
      // Register the setup method for modloading
      FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
      // Register the enqueueIMC method for modloading
      FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
      // Register the processIMC method for modloading
      FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
      // Register the doClientStuff method for modloading
      FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);
      
      // Register ourselves for server and other game events we are interested in
      MinecraftForge.EVENT_BUS.register(this);
   }
   
   
   private void setup(final FMLCommonSetupEvent event)
   {
      LOGGER.debug("PREINIT FOR {}", MOD_NAME);
      // some preinit code
      LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
   }
   
   
   private void doClientStuff(final FMLClientSetupEvent event)
   {
      LOGGER.debug("PREINIT FOR {}", MOD_NAME);
      // do something that can only be done on the client
      LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
   }
   
   
   private void enqueueIMC(final InterModEnqueueEvent event)
   {
      LOGGER.debug("ENQUEU_IMC FOR {}", MOD_NAME);
      // some example code to dispatch IMC to another mod
      InterModComms.sendTo(MOD_ID, "helloworld", () -> {
         LOGGER.info("Hello world from the MDK");
         return "Hello world";
      });
   }
   
   
   private void processIMC(final InterModProcessEvent event)
   {
      LOGGER.debug("PREINIT FOR {}", MOD_NAME);
      // some example code to receive and process InterModComms from other mods
      LOGGER.info("Got IMC {}", event.getIMCStream().
        map(m -> m.getMessageSupplier().get()).
        collect(Collectors.toList()));
   }
   
   
   // You can use SubscribeEvent and let the Event Bus discover methods to call
   @SubscribeEvent
   public void onServerStarting(FMLServerStartingEvent event)
   {
      // do something when the server starts
      LOGGER.info("HELLO from server starting");
   }
   
   
   // You can use EventBusSubscriber to automatically subscribe events on the contained class
   // (this is subscribing to the MOD
   // Event bus for receiving Registry Events)
   @Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
   public static class RegistryEvents
   {
      @SubscribeEvent
      public static void onBlocksRegistry(final RegistryEvent.Register <Block> blockRegistryEvent)
      {
         // register a new block here
         LOGGER.info("HELLO from Register Block");
      }
   }
}
