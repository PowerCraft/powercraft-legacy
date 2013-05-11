package powercraft.projector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustrum;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;

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

import powercraft.api.reflect.PC_ReflectHelper;
import powercraft.api.utils.PC_ClientUtils;
import powercraft.api.utils.PC_Struct2;
import powercraft.api.utils.PC_Struct3;
import powercraft.api.utils.PC_Utils;

public class PCpj_ProjectorRenderer {
	
	private static int projectorPreShader;
	private static int projectorPostShader;
	
	private static PC_Struct2<int[], PCpj_TileEntityProjector>[] framebuffer = new PC_Struct2[1];
	
	public static List<PCpj_TileEntityProjector> toRender = new ArrayList<PCpj_TileEntityProjector>();
	
	public static int dephtTextureID;
	
	private static int fboDephtTextureSize = 512;
	private static int dephtTextureSize = 512;
	
	public static void preRendering(float par1){
		ARBShaderObjects.glUseProgramObjectARB(projectorPreShader);
		for(int i=0; i<framebuffer.length; i++){
			framebuffer[i].b = null;
		}
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPushMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		for(int i=0; i<framebuffer.length && i<toRender.size(); i++){
			GL11.glPushMatrix();
			framebuffer[i].b = toRender.get(i);
			doPreRendering(par1, framebuffer[i].b, framebuffer[i].a);
			GL11.glPopMatrix();
		}
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		ARBShaderObjects.glUseProgramObjectARB(0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
	}
	
	private static void doPreRendering(float par1, PCpj_TileEntityProjector projector, int[] buffer) {
		Minecraft mc = PC_ClientUtils.mc();
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, buffer[0]);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		renderWorld(par1, projector);
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
		GL11.glTranslatef(projector.xCoord+0.5f, projector.yCoord+0.5f, projector.zCoord+0.5f);
		projector.projectionsMatrix = BufferUtils.createFloatBuffer(16);
		projector.modelviewMatrix = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, projector.projectionsMatrix);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, projector.modelviewMatrix);
	}
	
	public static void postRendering(float par1){
		Minecraft mc = PC_ClientUtils.mc();
		EntityLiving entityliving = mc.renderViewEntity;
		double d0 = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)par1;
	    double d1 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)par1;
	    double d2 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)par1;
	    for(int x=0; x<mc.displayWidth/dephtTextureSize+(mc.displayWidth%dephtTextureSize==0?0:1); x++){
	    	for(int y=0; y<mc.displayHeight/dephtTextureSize+(mc.displayHeight%dephtTextureSize==0?0:1); y++){
			    GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, dephtTextureID);
				GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, x*dephtTextureSize, y*dephtTextureSize, dephtTextureSize, dephtTextureSize, 0);
				GL13.glActiveTexture(GL13.GL_TEXTURE1);
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, dephtTextureID);
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glTranslated(-d0, -d1, -d2);
				ARBShaderObjects.glUseProgramObjectARB(projectorPostShader);
				int xPos = GL20.glGetUniformLocation(projectorPostShader, "x");
				int yPos = GL20.glGetUniformLocation(projectorPostShader, "y");
				GL20.glUniform1f(xPos, x*dephtTextureSize/(float)mc.displayWidth);
				GL20.glUniform1f(yPos, y*dephtTextureSize/(float)mc.displayHeight);
				int firstX=0, firstY=0, firstZ=0;
				boolean init=false;
				for(int i=framebuffer.length-1; i>=0; i--){
					if(framebuffer[i].b!=null){
						GL11.glPushMatrix();
						if(init){
							GL11.glTranslatef(firstX-framebuffer[i].b.xCoord, firstY-framebuffer[i].b.yCoord, firstZ-framebuffer[i].b.zCoord);
						}else{
							firstX = framebuffer[i].b.xCoord;
							firstY = framebuffer[i].b.yCoord;
							firstZ = framebuffer[i].b.zCoord;
						}
						init=true;
						doPostRendering(x*dephtTextureSize, y*dephtTextureSize, framebuffer[i].b, framebuffer[i].a);
						GL11.glPopMatrix();
					}
				}
				GL13.glActiveTexture(GL13.GL_TEXTURE1);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GL13.glActiveTexture(GL13.GL_TEXTURE0);
				GL11.glTranslated(d0, d1, d2);
	    	}
	    }
		ARBShaderObjects.glUseProgramObjectARB(0);
		mc.renderEngine.resetBoundTexture();
	}
	
	private static void doPostRendering(int x, int y, PCpj_TileEntityProjector projector, int[] buffer) {
		Minecraft mc = PC_ClientUtils.mc();
		int tex0 = GL20.glGetUniformLocation(projectorPostShader, "texture0");
		int tex1 = GL20.glGetUniformLocation(projectorPostShader, "texture1");
		int tex2 = GL20.glGetUniformLocation(projectorPostShader, "texture2");
		int tex3 = GL20.glGetUniformLocation(projectorPostShader, "texture3");
		int width = GL20.glGetUniformLocation(projectorPostShader, "width");
		int height = GL20.glGetUniformLocation(projectorPostShader, "height");
		int f = GL20.glGetUniformLocation(projectorPostShader, "f");
		double xDif = projector.xCoord+0.5;
		double yDif = projector.yCoord+0.5;
		double zDif = projector.zCoord+0.5;
		GL11.glTranslated(xDif*2, yDif*2, zDif*2);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glPushMatrix();
		GL11.glLoadIdentity();
		GL11.glMultMatrix(projector.projectionsMatrix);
		GL11.glMultMatrix(projector.modelviewMatrix);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL20.glUniform1i(tex0, 0);
		GL20.glUniform1i(tex1, 1);
		GL20.glUniform1i(tex2, 2);
		GL20.glUniform1i(tex3, 3);
		GL20.glUniform1f(width, mc.displayWidth/(float)dephtTextureSize);
		GL20.glUniform1f(height, mc.displayHeight/(float)dephtTextureSize);
		GL20.glUniform1f(f, 10.0f);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, buffer[1]);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, mc.renderEngine.getTexture("/textures/PowerCraft.png"));
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, buffer[2]);
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_ONE, GL11.GL_ONE);
		GL11.glDepthMask(false);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setTextureUV(0, 0);
		tessellator.addVertex(x/(float)mc.displayWidth, y/(float)mc.displayHeight, 0);
		tessellator.setTextureUV(1, 0);
		tessellator.addVertex((x+fboDephtTextureSize)/(float)mc.displayWidth, y/(float)mc.displayHeight, 0);
		tessellator.setTextureUV(1, 1);
		tessellator.addVertex((x+fboDephtTextureSize)/(float)mc.displayWidth, (y+fboDephtTextureSize)/(float)mc.displayHeight, 0);
		tessellator.setTextureUV(0, 1);
		tessellator.addVertex(x/(float)mc.displayWidth, (y+fboDephtTextureSize)/(float)mc.displayHeight, 0);
		tessellator.draw();
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glMatrixMode(GL11.GL_TEXTURE);
		GL11.glPopMatrix();
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
	}
	
	public static void init(){
		if(projectorPreShader==0){
			projectorPreShader = loadShader("ProjectorPre");
		}
		if(projectorPostShader==0){
			projectorPostShader = loadShader("ProjectorPost");
		}
		initFBO();
		
		dephtTextureID =  makeDepthTexture(dephtTextureSize);
	}
	
	private static int[] creaftFBO(){
		int[] buffer = new int[3];
		buffer[0] = EXTFramebufferObject.glGenFramebuffersEXT();
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, buffer[0]);
		buffer[1] = makeDepthTexture(fboDephtTextureSize);
		buffer[2] = makeColorTexture(fboDephtTextureSize);
		EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, GL11.GL_TEXTURE_2D, buffer[1], 0);
		EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, buffer[2], 0);
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);  
		return buffer;
	}
	
	private static int makeDepthTexture(int size){
		int id = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, size, size, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
		return id;
	}
	
	private static int makeColorTexture(int size){
		int id = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, id);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, size, size, 0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
		return id;
	}
	
	private static void initFBO(){
		for(int i=0; i<framebuffer.length; i++){
			framebuffer[i] = new PC_Struct2<int[], PCpj_TileEntityProjector>(creaftFBO(), null);
		}
	}
	
	private static int loadShader(String filename){
		int program=0;
		int vertShader = 0, fragShader = 0; 
		try {
			vertShader = createShader(filename+".vert", ARBVertexShader.GL_VERTEX_SHADER_ARB);
			fragShader = createShader(filename+".frag", ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
		}catch(Exception exc) {
			exc.printStackTrace();
			return 0;
		}finally {
			if(vertShader == 0 || fragShader == 0)
				return 0;
		} 
		program = ARBShaderObjects.glCreateProgramObjectARB();
		if(program == 0)
			return 0;

		ARBShaderObjects.glAttachObjectARB(program, vertShader);
		ARBShaderObjects.glAttachObjectARB(program, fragShader);
		
		ARBShaderObjects.glLinkProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB) == GL11.GL_FALSE) {
			System.err.println(getLogInfo(program));
			return 0;
		} 
		ARBShaderObjects.glValidateProgramARB(program);
		if (ARBShaderObjects.glGetObjectParameteriARB(program, ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB) == GL11.GL_FALSE) {
			System.err.println(getLogInfo(program));
			return 0;
		} 
		return program;
	}
	
	private static int createShader(String filename, int shaderType) throws Exception {
		int shader = 0;
		try {
			shader = ARBShaderObjects.glCreateShaderObjectARB(shaderType);
			if(shader == 0)
				return 0;
			
			ARBShaderObjects.glShaderSourceARB(shader, readFileAsString(filename));
			ARBShaderObjects.glCompileShaderARB(shader);
			
			if (ARBShaderObjects.glGetObjectParameteriARB(shader, ARBShaderObjects.GL_OBJECT_COMPILE_STATUS_ARB) == GL11.GL_FALSE)
			throw new RuntimeException("Error creating shader: " + getLogInfo(shader));
			
			return shader;
		}catch(Exception exc) {
			ARBShaderObjects.glDeleteObjectARB(shader);
			throw exc;
		}
	}
	
	private static String getLogInfo(int obj) {
		return ARBShaderObjects.glGetInfoLogARB(obj, ARBShaderObjects.glGetObjectParameteriARB(obj, ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB));
	}
	
	private static String readFileAsString(String filename) throws Exception {
		StringBuilder source = new StringBuilder();
		
		File file = new File(PC_Utils.getPowerCraftFile(), "shader");
		
		if(file.exists()){
			file.mkdirs();
		}
		
		FileInputStream in = new FileInputStream(new File(file, filename));
		 
		Exception exception = null;
		 
		BufferedReader reader;
		try{
			reader = new BufferedReader(new InputStreamReader(in,"UTF-8"));
			 
			Exception innerExc= null;
			try {
				String line;
				while((line = reader.readLine()) != null)
					source.append(line).append('\n');
			}catch(Exception exc) {
				exception = exc;
			}finally {
				try {
					reader.close();
				}catch(Exception exc) {
					if(innerExc == null)
						innerExc = exc;
					else
						exc.printStackTrace();
				}
			}
			if(innerExc != null)
				throw innerExc;
		}catch(Exception exc) {
			exception = exc;
		}finally {
			try {
				in.close();
			}catch(Exception exc) {
				if(exception == null)
					exception = exc;
				else
					exc.printStackTrace();
			}
			if(exception != null)
				throw exception;
		}
		return source.toString();
	}
	
	private static void setupCameraTransform(PCpj_ProjectorEntity entity, float par1) {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        
        GLU.gluPerspective(45, 1, 0.5F, 10.0f);

        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glLoadIdentity();

        GL11.glRotatef(entity.rotationYaw, 0, 1, 0);

    }
	
	private static void renderWorld(float par1, PCpj_TileEntityProjector projector){

		Minecraft mc = PC_ClientUtils.mc();
		PCpj_ProjectorEntity entity = new PCpj_ProjectorEntity(projector);
		
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_DEPTH_TEST);

        mc.renderViewEntity = entity;
        RenderGlobal renderglobal = mc.renderGlobal;
        EffectRenderer effectrenderer = mc.effectRenderer;
        double d0 = projector.xCoord+0.5;
        double d1 = projector.yCoord+0.5;
        double d2 = projector.zCoord+0.5;

        GL11.glViewport(0, 0, fboDephtTextureSize, fboDephtTextureSize);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        setupCameraTransform(entity, par1);
        ClippingHelperImpl.getInstance();
        
        Frustrum frustrum = new Frustrum();
        frustrum.setPosition(d0, d1, d2);
        
        mc.renderEngine.bindTexture("/terrain.png");
        RenderHelper.disableStandardItemLighting();
        renderBlocks(renderglobal, entity);
        RenderHelper.enableStandardItemLighting();
        ForgeHooksClient.setRenderPass(0);
        renderglobal.renderEntities(entity.getPosition(0), frustrum, par1);
        ForgeHooksClient.setRenderPass(-1);
        RenderHelper.disableStandardItemLighting();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glDisable(GL11.GL_FOG);
        GL11.glColorMask(true, true, true, false);
        mc.renderViewEntity = mc.thePlayer;
    }
	
	private static void renderBlocks(RenderGlobal renderglobal, Entity entity){
		WorldRenderer[] renderers = (WorldRenderer[])PC_ReflectHelper.getValue(RenderGlobal.class, renderglobal, 5, WorldRenderer[].class);
		List<WorldRenderer> forRender = new ArrayList<WorldRenderer>();
		for(int i=0; i<renderers.length; i++){
			float dist = renderers[i].distanceToEntitySquared(entity);
			for(int j=0; j<9; j++){
				if(j<forRender.size()){
					if(forRender.get(j).distanceToEntitySquared(entity)>dist){
						forRender.add(j, renderers[i]);
						break;
					}
				}else{
					forRender.add(j, renderers[i]);
					break;
				}
			}
		}
		GL11.glTranslated(-entity.posX, -entity.posY, -entity.posZ);
		for(int i=0; i<9 && i<forRender.size(); i++){
			GL11.glPushMatrix();
			WorldRenderer renderer = forRender.get(i);
            GL11.glTranslated(renderer.posXMinus, renderer.posYMinus, renderer.posZMinus);
            GL11.glCallList(renderer.getGLCallListForPass(0));
            GL11.glPopMatrix();
		}
		GL11.glTranslated(entity.posX, entity.posY, entity.posZ);
	}
	
}
