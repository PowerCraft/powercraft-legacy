package powercraft.projector;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBFragmentShader;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.ARBVertexShader;
import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL20;
import org.lwjgl.util.vector.Matrix4f;

import powercraft.api.utils.PC_ClientUtils;
import powercraft.api.utils.PC_Utils;
import cpw.mods.fml.common.TickType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.EntityLiving;

public class PCpj_EntityRenderer extends EntityRenderer {

	public static int projectorPreShader;
	public static int projectorPostShader;
	
	public static List<PCpj_TileEntityProjector> toRender = new ArrayList<PCpj_TileEntityProjector>();
	public static boolean canAdd=false;
	public static boolean dorendering=false;
	
	private static int framebufferID;
	private static int colorTextureID;
	
	private static FloatBuffer floatBuffer;
	private static FloatBuffer floatBuffer2;
	
	private static int dephtTextureID;
	
	public PCpj_EntityRenderer(Minecraft par1Minecraft) {
		super(par1Minecraft);
		init();
	}

	@Override
	public void renderWorld(float par1, long par2) {
		if(!dorendering){
			dorendering = true;
			Minecraft mc = PC_ClientUtils.mc();
			int widht = mc.displayWidth;
			int height = mc.displayHeight;
			mc.displayWidth = 500;
			mc.displayHeight = 500;
			ARBShaderObjects.glUseProgramObjectARB(projectorPreShader);
			for(PCpj_TileEntityProjector projector:toRender){
				doRenderingFor(projector);
			}
			ARBShaderObjects.glUseProgramObjectARB(0);
			mc.displayWidth = widht;
			mc.displayHeight = height;
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
			toRender.clear();
			canAdd = true;
			super.renderWorld(par1, par2);
			dorendering = false;
		}else{
			super.renderWorld(par1, par2);
		}
	}
	
	@Override
	protected void renderRainSnow(float par1) {
		Minecraft mc = PC_ClientUtils.mc();
		EntityLiving entityliving = mc.renderViewEntity;
		double d0 = entityliving.lastTickPosX + (entityliving.posX - entityliving.lastTickPosX) * (double)par1;
	    double d1 = entityliving.lastTickPosY + (entityliving.posY - entityliving.lastTickPosY) * (double)par1;
	    double d2 = entityliving.lastTickPosZ + (entityliving.posZ - entityliving.lastTickPosZ) * (double)par1;
		canAdd = false;
		if(floatBuffer!=null){
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, dephtTextureID);
			GL11.glCopyTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, 0, 0, 1024, 1024, 0);
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, dephtTextureID);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glViewport(0, 0, mc.displayWidth, mc.displayHeight);
			GL11.glTranslated(d0, d1, d2);
			ARBShaderObjects.glUseProgramObjectARB(projectorPostShader);
			for(PCpj_TileEntityProjector projector:toRender){
				renderPostScreen(projector);
				break;
			}
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			ARBShaderObjects.glUseProgramObjectARB(0);
		}
		mc.renderEngine.resetBoundTexture();
		super.renderRainSnow(par1);
	}

	public static void doRenderingFor(PCpj_TileEntityProjector projector){
		Minecraft mc = PC_ClientUtils.mc();
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, framebufferID);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		mc.renderViewEntity = new PCpj_ProjectorEntity(mc.theWorld);
		mc.renderViewEntity.setLocationAndAngles(projector.xCoord+0.5, projector.yCoord+0.5, projector.zCoord+0.5, 0, 0);
		mc.renderViewEntity.yOffset = 1.62F;
		mc.entityRenderer.renderWorld(0, System.nanoTime()+1000000);
		mc.renderViewEntity = mc.thePlayer;
		GL11.glTranslated(projector.xCoord+0.5, projector.yCoord+0.5, projector.zCoord+0.5);
		floatBuffer = BufferUtils.createFloatBuffer(16);
		floatBuffer2 = BufferUtils.createFloatBuffer(16);
		GL11.glGetFloat(GL11.GL_PROJECTION_MATRIX, floatBuffer);
		GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, floatBuffer2);
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);
	}
	
	public static void renderPostScreen(PCpj_TileEntityProjector projector){
		Minecraft mc = PC_ClientUtils.mc();
		int mat = GL20.glGetUniformLocation(projectorPostShader, "matrix");
		int tex0 = GL20.glGetUniformLocation(projectorPostShader, "texture0");
		int tex1 = GL20.glGetUniformLocation(projectorPostShader, "texture1");
		int width = GL20.glGetUniformLocation(projectorPostShader, "width");
		int height = GL20.glGetUniformLocation(projectorPostShader, "height");
		GL20.glUniform1i(tex0, 0);
		GL20.glUniform1i(tex1, 1);
		GL20.glUniform1f(width, mc.displayWidth/1024.0f);
		GL20.glUniform1f(height, mc.displayHeight/1024.0f);
		Matrix4f matrix = new Matrix4f();
		Matrix4f matrix2 = new Matrix4f();
		Matrix4f matrix3 = new Matrix4f();
		matrix.load(floatBuffer2);
		matrix2.load(floatBuffer);
		Matrix4f.mul(matrix, matrix2, matrix3);
		floatBuffer.position(0);
		matrix2.store(floatBuffer);
		GL20.glUniformMatrix4(mat, false, floatBuffer);
		floatBuffer = null;
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureID);
		Tessellator tessellator = Tessellator.instance;
		tessellator.startDrawingQuads();
		tessellator.setTextureUV(0, 0);
		tessellator.addVertex(0, 0, 0);
		tessellator.setTextureUV(1, 0);
		tessellator.addVertex(1, 0, 0);
		tessellator.setTextureUV(1, 1);
		tessellator.addVertex(1, 1, 0);
		tessellator.setTextureUV(0, 1);
		tessellator.addVertex(0, 1, 0);
		tessellator.draw();
	}
	
	public static void init(){
		if(projectorPreShader==0){
			projectorPreShader = loadShader("ProjectorPre");
		}
		if(projectorPostShader==0){
			projectorPostShader = loadShader("ProjectorPost");
		}
		initFBO();
		
		dephtTextureID = GL11.glGenTextures();
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, dephtTextureID);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT24, 1024, 1024, 0, GL11.GL_DEPTH_COMPONENT, GL11.GL_UNSIGNED_BYTE, (java.nio.ByteBuffer) null);
		
	}
	
	private static void initFBO(){
		framebufferID = EXTFramebufferObject.glGenFramebuffersEXT();
		colorTextureID = GL11.glGenTextures();
		int depthRenderBufferID = EXTFramebufferObject.glGenRenderbuffersEXT();
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, framebufferID);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTextureID);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB16, 512, 512, 0, GL11.GL_RGBA, GL11.GL_INT, (java.nio.ByteBuffer) null);
		EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, GL11.GL_TEXTURE_2D, colorTextureID, 0);
		
		EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthRenderBufferID);
		EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, GL14.GL_DEPTH_COMPONENT24, 512, 512);
		EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, depthRenderBufferID);
		EXTFramebufferObject.glBindFramebufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, 0);  
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
	
}
