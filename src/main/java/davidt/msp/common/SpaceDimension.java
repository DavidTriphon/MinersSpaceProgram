package davidt.msp.common;

import net.minecraft.world.*;
import net.minecraft.world.dimension.*;
import net.minecraftforge.common.*;

import java.util.function.*;


public class SpaceDimension extends ModDimension
{
   
   @Override
   public BiFunction <World, DimensionType, ? extends Dimension> getFactory()
   {
      return null;
   }
}
