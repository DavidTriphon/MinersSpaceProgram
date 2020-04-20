package davidt.msp.item;

import davidt.msp.dimension.mod.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.dimension.*;
import net.minecraftforge.common.*;

import static davidt.msp.MinersSpaceProgram.*;


public class TeleporterItem extends Item
{
   public static final ResourceLocation DIM_NAME = new ResourceLocation(MOD_ID,"msp_space_dim_name_1");
   
   private static final Item.Properties DEFAULT_PROPERTIES = new Item.Properties();
   
   public TeleporterItem()
   {
      super(DEFAULT_PROPERTIES);
   }
   
   
   @Override
   public ActionResultType onItemUse(ItemUseContext context)
   {
      PlayerEntity player = context.getPlayer();
      
      DimensionType type = DimensionManager.registerOrGetDimension(
        DIM_NAME, MSPModDimensions.SPACE.get(), null, false
        );
      
      if (player.dimension.equals(type))
      {
         player.changeDimension(DimensionType.OVERWORLD);
      }
      else
      {
         player.changeDimension(type);
      }
      
      return ActionResultType.SUCCESS;
   }
}
