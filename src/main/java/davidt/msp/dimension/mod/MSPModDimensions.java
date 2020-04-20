package davidt.msp.dimension.mod;

import davidt.msp.*;
import net.minecraftforge.common.*;
import net.minecraftforge.fml.*;
import net.minecraftforge.registries.*;


public final class MSPModDimensions
{
   public static final DeferredRegister <ModDimension> MOD_DIMENSIONS =
     new DeferredRegister <>(ForgeRegistries.MOD_DIMENSIONS, MinersSpaceProgram.MOD_ID);
   
   public static final RegistryObject <ModDimension> SPACE =
     MOD_DIMENSIONS.register("msp_space", SpaceModDim::new);
}
