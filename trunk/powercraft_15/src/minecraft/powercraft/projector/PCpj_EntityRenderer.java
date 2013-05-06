package powercraft.projector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
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

import powercraft.api.utils.PC_ClientUtils;
import powercraft.api.utils.PC_Utils;

public class PCpj_EntityRenderer extends EntityRenderer {
	
	private boolean dorendering;
	
	public PCpj_EntityRenderer(Minecraft par1Minecraft) {
		super(par1Minecraft);
		PCpj_ProjectorRenderer.init();
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
	
}
