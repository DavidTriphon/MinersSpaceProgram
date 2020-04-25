package davidt.msp.client;

import com.mojang.blaze3d.matrix.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.world.*;
import net.minecraft.potion.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.client.event.*;
import net.minecraftforge.common.*;
import net.minecraftforge.event.*;
import net.minecraftforge.event.world.*;


public class ClientWorldHandler
{
   public static ClientWorld lastClientWorld = null;
   
   public static boolean shouldRenderExtraWorld = false;
   
   private static WorldRenderer worldRenderer = null;
   
   private static float renderPartialTicks = 0;
   
   
   public static void register()
   {
      MinecraftForge.EVENT_BUS.addListener(ClientWorldHandler::worldUnload);
      MinecraftForge.EVENT_BUS.addListener(ClientWorldHandler::renderWorldLast);
      MinecraftForge.EVENT_BUS.addListener(ClientWorldHandler::renderStart);
      MinecraftForge.EVENT_BUS.addListener(ClientWorldHandler::runTickPostClient);
   }
   
   
   private static void worldUnload(WorldEvent.Unload unloadEvent)
   {
      IWorld iWorld = unloadEvent.getWorld();
      
      if (iWorld.isRemote())
      {
         Minecraft mc = Minecraft.getInstance();
         
         lastClientWorld = (ClientWorld) iWorld;
         worldRenderer = new WorldRenderer(mc, mc.worldRenderer.renderTypeTextures);
         worldRenderer.loadRenderers();
         worldRenderer.setWorldAndLoadRenderers(lastClientWorld);
      }
   }
   
   
   private static void renderStart(TickEvent.RenderTickEvent renderTickEvent)
   {
      if (renderTickEvent.phase == TickEvent.Phase.START)
      {
         Minecraft mc = Minecraft.getInstance();
         renderPartialTicks = mc.isGamePaused() ? mc.renderPartialTicksPaused : mc.getRenderPartialTicks();
      }
   }
   
   
   private static void runTickPostClient(TickEvent.ClientTickEvent clientTickEvent)
   {
      if (clientTickEvent.phase == TickEvent.Phase.END)
      {
         if (worldRenderer != null)
            worldRenderer.tick();
      }
   }
   
   
   private static void renderWorldLast(RenderWorldLastEvent renderWorldLastEvent)
   {
      if (shouldRenderExtraWorld)
      {
         Minecraft mc = Minecraft.getInstance();
         
         ClientWorld mainWorld = mc.world;
         mc.world = lastClientWorld;
         
         try
         {
            // setup render info
            ActiveRenderInfo renderInfo = mc.gameRenderer.getActiveRenderInfo();
            
            /// create projection matrix for our dimension.
            MatrixStack matrixstack = new MatrixStack();
            matrixstack.getLast().getMatrix().mul(
              mc.gameRenderer.getProjectionMatrix(renderInfo, renderPartialTicks, true));
            mc.gameRenderer.hurtCameraEffect(matrixstack, renderPartialTicks);
            if (mc.gameSettings.viewBobbing)
            {
               mc.gameRenderer.applyBobbing(matrixstack, renderPartialTicks);
            }
            
            float f = MathHelper.lerp(renderPartialTicks, mc.player.prevTimeInPortal,
              mc.player.timeInPortal);
            if (f > 0.0F)
            {
               int i = 20;
               if (mc.player.isPotionActive(Effects.NAUSEA))
               {
                  i = 7;
               }
               
               float f1 = 5.0F / (f * f + 5.0F) - f * 0.04F;
               f1 = f1 * f1;
               Vector3f vector3f =
                 new Vector3f(0.0F, MathHelper.SQRT_2 / 2.0F, MathHelper.SQRT_2 / 2.0F);
               matrixstack.rotate(vector3f.rotationDegrees(
                 ((float) mc.gameRenderer.rendererUpdateCount + renderPartialTicks) * (float) i));
               matrixstack.scale(1.0F / f1, 1.0F, 1.0F);
               float f2 = -((float) mc.gameRenderer.rendererUpdateCount + renderPartialTicks) * (float) i;
               matrixstack.rotate(vector3f.rotationDegrees(f2));
            }
            
            Matrix4f matrix4f = matrixstack.getLast().getMatrix();
            
            // continue render info setup (must happen after the other matrix stack is made for
            // projection)
            renderInfo.update(lastClientWorld,
              mc.getRenderViewEntity() == null ? mc.player : mc.getRenderViewEntity(),
              mc.gameSettings.thirdPersonView > 0, mc.gameSettings.thirdPersonView == 2,
              renderPartialTicks);
            
            // get camera
            net.minecraftforge.client.event.EntityViewRenderEvent.CameraSetup cameraSetup =
              net.minecraftforge.client.ForgeHooksClient.onCameraSetup(mc.gameRenderer, renderInfo,
                renderPartialTicks);
            renderInfo.setAnglesInternal(cameraSetup.getYaw(), cameraSetup.getPitch());
            
            // make matrixStack // whatever that means
            MatrixStack matrixStackIn = new MatrixStack();
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(cameraSetup.getRoll()));
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(renderInfo.getPitch()));
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(renderInfo.getYaw() + 180.0F));
            
            // finally render using our extra world (will probably break game)
            worldRenderer.updateCameraAndRender(matrixStackIn, renderPartialTicks,
              // this is actually longTimeNano passed through the renderWorldLastEvent
              (long) renderWorldLastEvent.getPartialTicks(),
              mc.gameRenderer.isDrawBlockOutline(),
              renderInfo, mc.gameRenderer, mc.gameRenderer.lightmapTexture, matrix4f);
         }
         finally
         {
            mc.world = mainWorld;
         }
      }
   }
}
