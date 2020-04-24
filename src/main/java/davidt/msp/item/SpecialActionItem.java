package davidt.msp.item;

import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraft.world.*;

import java.util.function.*;


public class SpecialActionItem extends Item
{
   private static final Item.Properties DEFAULT_PROPERTIES = new Item.Properties();
   
   private final BiFunction <PlayerEntity, World, Boolean> _action;
   
   
   public SpecialActionItem(BiFunction <PlayerEntity, World, Boolean> action, Properties properties)
   {
      super(properties);
      _action = action;
   }
   
   
   public SpecialActionItem(BiFunction <PlayerEntity, World, Boolean> action)
   {
      this(action, DEFAULT_PROPERTIES);
   }
   
   
   @Override
   public ActionResult <ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn,
     Hand handIn)
   {
      if (_action.apply(playerIn, worldIn))
      {
         return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
      }
      else
      {
         return ActionResult.resultFail(playerIn.getHeldItem(handIn));
      }
   }
}
