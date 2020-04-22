package davidt.msp.item;

import davidt.msp.client.*;
import net.minecraft.client.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;
import org.apache.logging.log4j.*;


public class TransdimensionalItem extends Item
{
   public static final Logger LOGGER = LogManager.getLogger();
   
   public TransdimensionalItem()
   {
      super(new Item.Properties());
   }
   
   
   @Override
   public ActionResult <ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
     Hand handIn)
   {
      if (worldIn.isRemote)
      {
         LOGGER.warn("Using the transdimensional item! Prepare for a soft crash!");
         
         if (ClientWorldHandler.world != null)
            Minecraft.getInstance().loadWorld(ClientWorldHandler.world);
      }
      
      return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
   }
}
