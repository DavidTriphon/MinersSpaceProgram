package davidt.msp;

import davidt.msp.client.*;
import davidt.msp.dimension.mod.*;
import davidt.msp.item.*;
import davidt.msp.world.biome.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.common.*;
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
      // register the client world handler
      ClientWorldHandler.register();
      
      // Link up register callbacks for blocks, item, biomes, dimensions, etc.
      MSPModDimensions.MOD_DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
      MSPBiomes.BIOMES.register(FMLJavaModLoadingContext.get().getModEventBus());
      MSPItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
      
      // Register ourselves for server and other game events we are interested in
      MinecraftForge.EVENT_BUS.register(this);
   }
}
