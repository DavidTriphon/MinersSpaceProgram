package davidt.msp.dimension.mod;

import davidt.msp.dimension.*;
import net.minecraft.world.*;
import net.minecraft.world.dimension.*;
import net.minecraftforge.common.*;

import java.util.function.*;


public class SpaceModDim extends ModDimension
{
   
   @Override
   public BiFunction <World, DimensionType, ? extends Dimension> getFactory()
   {
      return SpaceDimension::new;
   }
}
