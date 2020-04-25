package davidt.msp.dimension;

import davidt.msp.client.*;
import davidt.msp.dimension.mod.*;
import net.minecraft.client.*;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.dimension.*;
import net.minecraft.world.server.*;
import net.minecraftforge.common.*;
import net.minecraftforge.common.util.*;
import org.apache.logging.log4j.*;

import java.util.function.*;

import static davidt.msp.MinersSpaceProgram.*;


public class DimensionHoppingHandler
{
   // CONSTANTS
   
   public static final  ResourceLocation DIM_NAME   =
     new ResourceLocation(MOD_ID, "msp_space_dim_name_1");
   private static final Logger           LOGGER     = LogManager.getLogger();
   private static final ITeleporter      PORTALLESS = new ITeleporter()
   {
      @Override
      public Entity placeEntity(Entity entity, ServerWorld currentWorld, ServerWorld destWorld,
        float yaw, Function <Boolean, Entity> repositionEntity)
      {
         entity.moveForced(entity.getPosition().getX(), 128, entity.getPosition().getZ());
         
         return repositionEntity.apply(false);
      }
   };
   
   // STATIC METHODS
   
   
   public static boolean teleport(PlayerEntity player, World world)
   {
      if (!world.isRemote)
      {
         DimensionType type = DimensionManager.registerOrGetDimension(
           DIM_NAME, MSPModDimensions.SPACE.get(), null, false
         );
         
         DimensionType destination =
           player.dimension.equals(type) ? DimensionType.OVERWORLD : type;
         
         LOGGER.warn("Teleporter Item used by {} from {} to {}",
           player, player.dimension, destination);
         
         player.changeDimension(destination, PORTALLESS);
      }
      
      return true;
   }
   
   
   public static boolean transdimensionalHop(PlayerEntity player, World world)
   {
      if (world.isRemote)
      {
         LOGGER.warn("Using the transdimensional item! Prepare for a soft crash!");
         
         if (ClientWorldHandler.lastClientWorld != null)
            Minecraft.getInstance().loadWorld(ClientWorldHandler.lastClientWorld);
      }
      
      return true;
   }
   
   
   public static boolean createMirrorSelf(PlayerEntity player, World world)
   {
      if (!world.isRemote)
      {
         LOGGER.warn("Using the mirror self item! Who know's what will happen!");
         
         DimensionType spaceType = DimensionManager.registerOrGetDimension(
           DIM_NAME, MSPModDimensions.SPACE.get(), null, false
         );
         
         DimensionType destination =
           player.dimension.equals(spaceType) ? DimensionType.OVERWORLD : spaceType;
         ServerWorld destinationWorld = world.getServer().getWorld(destination);
         
         FakePlayer fake = new FakePlayer(destinationWorld, player.getGameProfile());
      }
      
      return true;
   }
   
   
   public static boolean killFakes(PlayerEntity player, World world)
   {
      if (!world.isRemote)
      {
      
      }
      
      return true;
   }
   
   
   public static boolean toggleExtraRender(PlayerEntity player, World world)
   {
      if (world.isRemote)
      {
         ClientWorldHandler.shouldRenderExtraWorld = !ClientWorldHandler.shouldRenderExtraWorld;
      }
      
      return true;
   }
}
