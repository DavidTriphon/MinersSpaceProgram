package davidt.msp.item;

import davidt.msp.dimension.mod.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import net.minecraft.world.dimension.*;
import net.minecraftforge.common.*;
import org.apache.logging.log4j.*;

import static davidt.msp.MinersSpaceProgram.*;


public class TeleporterItem extends Item
{
   public static final ResourceLocation DIM_NAME =
     new ResourceLocation(MOD_ID, "msp_space_dim_name_1");
   
   private static final Item.Properties DEFAULT_PROPERTIES = new Item.Properties();
   private static final Logger LOGGER = LogManager.getLogger();
   
   
   public TeleporterItem()
   {
      super(DEFAULT_PROPERTIES);
   }
   
   
   @Override
   public ActionResult <ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
     Hand handIn)
   {
      if (!worldIn.isRemote)
      {
         LOGGER.debug("Teleporter Item used by {}", playerIn);
         
         DimensionType type = DimensionManager.registerOrGetDimension(
           DIM_NAME, MSPModDimensions.SPACE.get(), null, false
         );
         
         LOGGER.debug("TP: Dimension type is {}", type);
         
         DimensionType destination =
           playerIn.dimension.equals(type) ? DimensionType.OVERWORLD : type;
         
         LOGGER.debug("teleported {} to {}", playerIn, destination);
         
         playerIn.changeDimension(destination);
      }
      
      return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
   }
}
