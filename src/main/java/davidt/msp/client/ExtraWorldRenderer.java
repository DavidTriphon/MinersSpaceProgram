package davidt.msp.client;

import com.mojang.blaze3d.matrix.*;
import com.mojang.blaze3d.systems.*;
import com.mojang.blaze3d.vertex.*;
import it.unimi.dsi.fastutil.longs.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.entity.player.*;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.culling.*;
import net.minecraft.client.renderer.model.*;
import net.minecraft.client.renderer.texture.*;
import net.minecraft.client.renderer.tileentity.*;
import net.minecraft.client.settings.*;
import net.minecraft.entity.*;
import net.minecraft.profiler.*;
import net.minecraft.tileentity.*;
import net.minecraft.util.*;
import net.minecraft.util.math.*;

import java.util.*;


public class ExtraWorldRenderer extends WorldRenderer
{
   
   public ExtraWorldRenderer(Minecraft mcIn,
     RenderTypeBuffers rainTimeBuffersIn)
   {
      super(mcIn, rainTimeBuffersIn);
   }
   
   
   @Override
   public void updateCameraAndRender(MatrixStack matrixStackIn, float partialTicks,
     long finishTimeNano, boolean drawBlockOutline, ActiveRenderInfo activeRenderInfoIn,
     GameRenderer gameRendererIn, LightTexture lightmapIn, Matrix4f projectionIn)
   {
      TileEntityRendererDispatcher.instance.prepare(this.world, this.mc.getTextureManager(), this.mc.fontRenderer, activeRenderInfoIn, this.mc.objectMouseOver);
      this.renderManager.cacheActiveRenderInfo(this.world, activeRenderInfoIn, this.mc.pointedEntity);
      IProfiler iprofiler = this.world.getProfiler();
      iprofiler.endStartSection("light_updates");
      this.mc.world.getChunkProvider().getLightManager().tick(Integer.MAX_VALUE, true, true);
      Vec3d vec3d = activeRenderInfoIn.getProjectedView();
      double d0 = vec3d.getX();
      double d1 = vec3d.getY();
      double d2 = vec3d.getZ();
      Matrix4f matrix4f = matrixStackIn.getLast().getMatrix();
      iprofiler.endStartSection("culling");
      boolean flag = this.debugFixedClippingHelper != null;
      ClippingHelperImpl clippinghelperimpl;
      if (flag) {
         clippinghelperimpl = this.debugFixedClippingHelper;
         clippinghelperimpl.setCameraPosition(this.debugTerrainFrustumPosition.x, this.debugTerrainFrustumPosition.y, this.debugTerrainFrustumPosition.z);
      } else {
         clippinghelperimpl = new ClippingHelperImpl(matrix4f, projectionIn);
         clippinghelperimpl.setCameraPosition(d0, d1, d2);
      }
   
      this.mc.getProfiler().endStartSection("captureFrustum");
      if (this.debugFixTerrainFrustum) {
         this.captureFrustum(matrix4f, projectionIn, vec3d.x, vec3d.y, vec3d.z, flag ? new ClippingHelperImpl(matrix4f, projectionIn) : clippinghelperimpl);
         this.debugFixTerrainFrustum = false;
      }
   
      iprofiler.endStartSection("clear");
      FogRenderer.updateFogColor(activeRenderInfoIn, partialTicks, this.mc.world, this.mc.gameSettings.renderDistanceChunks, gameRendererIn.getBossColorModifier(partialTicks));
      RenderSystem.clear(16640, Minecraft.IS_RUNNING_ON_MAC);
      float f = gameRendererIn.getFarPlaneDistance();
      boolean flag1 = this.mc.world.dimension.doesXZShowFog(MathHelper.floor(d0), MathHelper.floor(d1)) || this.mc.ingameGUI.getBossOverlay().shouldCreateFog();
      if (this.mc.gameSettings.renderDistanceChunks >= 4) {
         FogRenderer.setupFog(activeRenderInfoIn, FogRenderer.FogType.FOG_SKY, f, flag1, partialTicks);
         iprofiler.endStartSection("sky");
         this.renderSky(matrixStackIn, partialTicks);
      }
   
      iprofiler.endStartSection("fog");
      FogRenderer.setupFog(activeRenderInfoIn, FogRenderer.FogType.FOG_TERRAIN, Math.max(f - 16.0F, 32.0F), flag1, partialTicks);
      iprofiler.endStartSection("terrain_setup");
      this.setupTerrain(activeRenderInfoIn, clippinghelperimpl, flag, this.frameId++, this.mc.player.isSpectator());
      iprofiler.endStartSection("updatechunks");
      int i = 30;
      int j = this.mc.gameSettings.framerateLimit;
      long k = 33333333L;
      long l;
      if ((double)j == AbstractOption.FRAMERATE_LIMIT.getMaxValue()) {
         l = 0L;
      } else {
         l = (long)(1000000000 / j);
      }
   
      long i1 = Util.nanoTime() - finishTimeNano;
      long j1 = this.renderTimeManager.nextValue(i1);
      long k1 = j1 * 3L / 2L;
      long l1 = MathHelper.clamp(k1, l, 33333333L);
      this.updateChunks(finishTimeNano + l1);
      iprofiler.endStartSection("terrain");
      this.renderBlockLayer(RenderType.getSolid(), matrixStackIn, d0, d1, d2);
      this.mc.getModelManager().getAtlasTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).setBlurMipmap(false, this.mc.gameSettings.mipmapLevels > 0); // FORGE: fix flickering leaves when mods mess up the blurMipmap settings
      this.renderBlockLayer(RenderType.getCutoutMipped(), matrixStackIn, d0, d1, d2);
      this.mc.getModelManager().getAtlasTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE).restoreLastBlurMipmap();
      this.renderBlockLayer(RenderType.getCutout(), matrixStackIn, d0, d1, d2);
      RenderHelper.setupLevelDiffuseLighting(matrixStackIn.getLast().getMatrix());
      iprofiler.endStartSection("entities");
      iprofiler.startSection("prepare");
      this.countEntitiesRendered = 0;
      this.countEntitiesHidden = 0;
      iprofiler.endStartSection("entities");
      if (this.isRenderEntityOutlines()) {
         this.entityOutlineFramebuffer.framebufferClear(Minecraft.IS_RUNNING_ON_MAC);
         this.mc.getFramebuffer().bindFramebuffer(false);
      }
   
      boolean flag2 = false;
      IRenderTypeBuffer.Impl irendertypebuffer$impl = this.renderTypeTextures.getBufferSource();
   
      for(Entity entity : this.world.getAllEntities()) {
         if ((this.renderManager.shouldRender(entity, clippinghelperimpl, d0, d1, d2) || entity.isRidingOrBeingRiddenBy(this.mc.player)) && (entity != activeRenderInfoIn.getRenderViewEntity() || activeRenderInfoIn.isThirdPerson() || activeRenderInfoIn.getRenderViewEntity() instanceof LivingEntity && ((LivingEntity)activeRenderInfoIn.getRenderViewEntity()).isSleeping()) && (!(entity instanceof ClientPlayerEntity) || activeRenderInfoIn.getRenderViewEntity() == entity)) {
            ++this.countEntitiesRendered;
            if (entity.ticksExisted == 0) {
               entity.lastTickPosX = entity.getPosX();
               entity.lastTickPosY = entity.getPosY();
               entity.lastTickPosZ = entity.getPosZ();
            }
         
            IRenderTypeBuffer irendertypebuffer;
            if (this.isRenderEntityOutlines() && entity.isGlowing()) {
               flag2 = true;
               OutlineLayerBuffer outlinelayerbuffer = this.renderTypeTextures.getOutlineBufferSource();
               irendertypebuffer = outlinelayerbuffer;
               int i2 = entity.getTeamColor();
               int j2 = 255;
               int k2 = i2 >> 16 & 255;
               int l2 = i2 >> 8 & 255;
               int i3 = i2 & 255;
               outlinelayerbuffer.setColor(k2, l2, i3, 255);
            } else {
               irendertypebuffer = irendertypebuffer$impl;
            }
         
            this.renderEntity(entity, d0, d1, d2, partialTicks, matrixStackIn, irendertypebuffer);
         }
      }
   
      this.checkMatrixStack(matrixStackIn);
      irendertypebuffer$impl.finish(RenderType.getEntitySolid(AtlasTexture.LOCATION_BLOCKS_TEXTURE));
      irendertypebuffer$impl.finish(RenderType.getEntityCutout(AtlasTexture.LOCATION_BLOCKS_TEXTURE));
      irendertypebuffer$impl.finish(RenderType.getEntityCutoutNoCull(AtlasTexture.LOCATION_BLOCKS_TEXTURE));
      irendertypebuffer$impl.finish(RenderType.getEntitySmoothCutout(AtlasTexture.LOCATION_BLOCKS_TEXTURE));
      iprofiler.endStartSection("blockentities");
   
      for(WorldRenderer.LocalRenderInformationContainer worldrenderer$localrenderinformationcontainer : this.renderInfos) {
         List <TileEntity> list = worldrenderer$localrenderinformationcontainer.renderChunk.getCompiledChunk().getTileEntities();
         if (!list.isEmpty()) {
            for(TileEntity tileentity1 : list) {
               if(!clippinghelperimpl.isBoundingBoxInFrustum(tileentity1.getRenderBoundingBox())) continue;
               BlockPos blockpos3 = tileentity1.getPos();
               IRenderTypeBuffer irendertypebuffer1 = irendertypebuffer$impl;
               matrixStackIn.push();
               matrixStackIn.translate((double)blockpos3.getX() - d0, (double)blockpos3.getY() - d1, (double)blockpos3.getZ() - d2);
               SortedSet<DestroyBlockProgress> sortedset = this.damageProgress.get(blockpos3.toLong());
               if (sortedset != null && !sortedset.isEmpty()) {
                  int k3 = sortedset.last().getPartialBlockDamage();
                  if (k3 >= 0) {
                     IVertexBuilder ivertexbuilder = new MatrixApplyingVertexBuilder(this.renderTypeTextures.getCrumblingBufferSource().getBuffer(
                       ModelBakery.DESTROY_RENDER_TYPES.get(k3)), matrixStackIn.getLast());
                     irendertypebuffer1 = (p_230014_2_) -> {
                        IVertexBuilder ivertexbuilder3 = irendertypebuffer$impl.getBuffer(p_230014_2_);
                        return p_230014_2_.isUseDelegate() ? VertexBuilderUtils.newDelegate(ivertexbuilder, ivertexbuilder3) : ivertexbuilder3;
                     };
                  }
               }
            
               TileEntityRendererDispatcher.instance.renderTileEntity(tileentity1, partialTicks, matrixStackIn, irendertypebuffer1);
               matrixStackIn.pop();
            }
         }
      }
   
      synchronized(this.setTileEntities) {
         for(TileEntity tileentity : this.setTileEntities) {
            if(!clippinghelperimpl.isBoundingBoxInFrustum(tileentity.getRenderBoundingBox())) continue;
            BlockPos blockpos2 = tileentity.getPos();
            matrixStackIn.push();
            matrixStackIn.translate((double)blockpos2.getX() - d0, (double)blockpos2.getY() - d1, (double)blockpos2.getZ() - d2);
            TileEntityRendererDispatcher.instance.renderTileEntity(tileentity, partialTicks, matrixStackIn, irendertypebuffer$impl);
            matrixStackIn.pop();
         }
      }
   
      this.checkMatrixStack(matrixStackIn);
      irendertypebuffer$impl.finish(RenderType.getSolid());
      irendertypebuffer$impl.finish(Atlases.getSolidBlockType());
      irendertypebuffer$impl.finish(Atlases.getCutoutBlockType());
      irendertypebuffer$impl.finish(Atlases.getBedType());
      irendertypebuffer$impl.finish(Atlases.getShulkerBoxType());
      irendertypebuffer$impl.finish(Atlases.getSignType());
      irendertypebuffer$impl.finish(Atlases.getChestType());
      this.renderTypeTextures.getOutlineBufferSource().finish();
      if (flag2) {
         this.entityOutlineShader.render(partialTicks);
         this.mc.getFramebuffer().bindFramebuffer(false);
      }
   
      iprofiler.endStartSection("destroyProgress");
   
      for(Long2ObjectMap.Entry <SortedSet<DestroyBlockProgress>> entry : this.damageProgress.long2ObjectEntrySet()) {
         BlockPos blockpos1 = BlockPos.fromLong(entry.getLongKey());
         double d3 = (double)blockpos1.getX() - d0;
         double d4 = (double)blockpos1.getY() - d1;
         double d5 = (double)blockpos1.getZ() - d2;
         if (!(d3 * d3 + d4 * d4 + d5 * d5 > 1024.0D)) {
            SortedSet<DestroyBlockProgress> sortedset1 = entry.getValue();
            if (sortedset1 != null && !sortedset1.isEmpty()) {
               int j3 = sortedset1.last().getPartialBlockDamage();
               matrixStackIn.push();
               matrixStackIn.translate((double)blockpos1.getX() - d0, (double)blockpos1.getY() - d1, (double)blockpos1.getZ() - d2);
               IVertexBuilder ivertexbuilder1 = new MatrixApplyingVertexBuilder(this.renderTypeTextures.getCrumblingBufferSource().getBuffer(ModelBakery.DESTROY_RENDER_TYPES.get(j3)), matrixStackIn.getLast());
               this.mc.getBlockRendererDispatcher().renderBlockDamage(this.world.getBlockState(blockpos1), blockpos1, this.world, matrixStackIn, ivertexbuilder1);
               matrixStackIn.pop();
            }
         }
      }
   
      this.checkMatrixStack(matrixStackIn);
      iprofiler.endSection();
      RayTraceResult raytraceresult = this.mc.objectMouseOver;
      if (drawBlockOutline && raytraceresult != null && raytraceresult.getType() == RayTraceResult.Type.BLOCK) {
         iprofiler.endStartSection("outline");
         BlockPos blockpos = ((BlockRayTraceResult)raytraceresult).getPos();
         BlockState blockstate = this.world.getBlockState(blockpos);
         if (!net.minecraftforge.client.ForgeHooksClient.onDrawBlockHighlight(this, activeRenderInfoIn, mc.objectMouseOver, partialTicks, matrixStackIn, irendertypebuffer$impl))
            if (!blockstate.isAir(this.world, blockpos) && this.world.getWorldBorder().contains(blockpos)) {
               IVertexBuilder ivertexbuilder2 = irendertypebuffer$impl.getBuffer(RenderType.getLines());
               this.drawSelectionBox(matrixStackIn, ivertexbuilder2, activeRenderInfoIn.getRenderViewEntity(), d0, d1, d2, blockpos, blockstate);
            }
      }
   
      RenderSystem.pushMatrix();
      RenderSystem.multMatrix(matrixStackIn.getLast().getMatrix());
      this.mc.debugRenderer.render(matrixStackIn, irendertypebuffer$impl, d0, d1, d2);
      this.renderWorldBorder(activeRenderInfoIn);
      RenderSystem.popMatrix();
      irendertypebuffer$impl.finish(Atlases.getTranslucentBlockType());
      irendertypebuffer$impl.finish(Atlases.getBannerType());
      irendertypebuffer$impl.finish(Atlases.getShieldType());
      irendertypebuffer$impl.finish(RenderType.getGlint());
      irendertypebuffer$impl.finish(RenderType.getEntityGlint());
      irendertypebuffer$impl.finish(RenderType.getWaterMask());
      this.renderTypeTextures.getCrumblingBufferSource().finish();
      irendertypebuffer$impl.finish(RenderType.getLines());
      irendertypebuffer$impl.finish();
      iprofiler.endStartSection("translucent");
      this.renderBlockLayer(RenderType.getTranslucent(), matrixStackIn, d0, d1, d2);
      iprofiler.endStartSection("particles");
      this.mc.particles.renderParticles(matrixStackIn, irendertypebuffer$impl, lightmapIn, activeRenderInfoIn, partialTicks);
      RenderSystem.pushMatrix();
      RenderSystem.multMatrix(matrixStackIn.getLast().getMatrix());
      iprofiler.endStartSection("cloudsLayers");
      if (this.mc.gameSettings.getCloudOption() != CloudOption.OFF) {
         iprofiler.endStartSection("clouds");
         this.renderClouds(matrixStackIn, partialTicks, d0, d1, d2);
      }
   
      RenderSystem.depthMask(false);
      iprofiler.endStartSection("weather");
      this.renderRainSnow(lightmapIn, partialTicks, d0, d1, d2);
      RenderSystem.depthMask(true);
      this.renderDebug(activeRenderInfoIn);
      RenderSystem.shadeModel(7424);
      RenderSystem.depthMask(true);
      RenderSystem.disableBlend();
      RenderSystem.popMatrix();
      FogRenderer.resetFog();
   }
}
