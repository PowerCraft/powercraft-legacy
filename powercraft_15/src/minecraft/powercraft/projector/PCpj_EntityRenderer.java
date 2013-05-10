package powercraft.projector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLiving;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.util.vector.Matrix4f;

import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.utils.PC_ClientUtils;
import powercraft.api.utils.PC_Utils;
import powercraft.launcher.PC_LauncherUtils;
import powercraft.launcher.PC_Logger;
import powercraft.launcher.loader.PC_ModLoader;

public class PCpj_EntityRenderer extends EntityRenderer {
	
	private Minecraft mc;
	private boolean dorendering;
	private Method onTick;
	
	public PCpj_EntityRenderer(Minecraft par1Minecraft) {
		super(par1Minecraft);
		mc = par1Minecraft;
		PCpj_ProjectorRenderer.init();
		if(PC_LauncherUtils.getModLoader() == PC_ModLoader.RISUGAMIS_MODLOADER){
			
			try {
				Class<?> c = Class.forName("ModLoader");
				onTick = c.getMethod("onTick", float.class, Minecraft.class);
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				PC_Logger.severe("Can't fine ModLoader Class while using ModLoader??");
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
				PC_Logger.severe("Can't fine ModLoader.onTick Method");
			} catch (SecurityException e) {
				e.printStackTrace();
				PC_Logger.severe("PowerCraft need reflection permissions");
			}
			
		}
	}

	@Override
	public void renderWorld(float par1, long par2) {
		if(!dorendering){
			dorendering=true;
			PCpj_ProjectorRenderer.preRendering(par1);
			dorendering=false;
		}
		super.renderWorld(par1, par2);
	}
	
	@Override
	protected void renderRainSnow(float par1) {
		super.renderRainSnow(par1);
		if(!dorendering){
			dorendering=true;
			PCpj_ProjectorRenderer.postRendering(par1);
			dorendering=false;
		}
	}

	@Override
	public void updateCameraAndRender(float par1) {
		super.updateCameraAndRender(par1);
		if(onTick!=null){
			try {
				onTick.invoke(null, par1, mc);
			} catch (Exception e) {
				e.printStackTrace();
				PC_Logger.severe("Error while ModLoader onTick Method");
			} 
		}
	}
	
}
