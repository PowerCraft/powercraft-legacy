package powercraft.weasel;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.model.ModelRenderer;
import powercraft.management.PC_Color;
import powercraft.management.renderer.PC_Renderer;

public class PCws_WeaselModelDisplay extends PCws_WeaselModelBase {

	public PCws_WeaselModelDisplay(){
		model = new ModelRenderer[4];
		modelColorMark = new ModelRenderer[1];
	
		// the bottom pad
		model[0] = new ModelRenderer(this, 85, 27);
		model[0].addBox(-5F, -1F, -5F, 10, 1, 10, 0.0F);
	
		// the connection piece
		model[1] = new ModelRenderer(this, 86, 16);
		model[1].addBox(-1F, -2F, -1F, 2, 1, 2, 0.0F);
	
		// display body
		model[2] = new ModelRenderer(this, 58, 20);
		model[2].addBox(-8F, -16F, -1F, 16, 14, 2, 0.0F);
	
		// screen
		model[3] = new ModelRenderer(this, 0, 64);
		model[3].addBox(-8F, -16F, -1F, 16, 14, 2, 0.0F);
	
		// the colour piece
		modelColorMark[0] = new ModelRenderer(this, 13, 12);
		modelColorMark[0].addBox(-2F, -17F, -1F, 4, 1, 2, 0.0F);
	}
	
	@Override
	public void renderDevice(PCws_TileEntityWeasel te) {
		model[0].render(0.0625F);
		model[1].render(0.0625F);
		model[2].render(0.0625F);
		int bg = 0x000000;
		if(te.getData("bgcolor")!=null)
			bg = (Integer)te.getData("bgcolor");
		PC_Color bgc = PC_Color.fromHex(bg);
		PC_Renderer.glColor3f(bgc.x, bgc.y, bgc.z);
		model[3].render(0.0625F);
		PC_Renderer.glColor3f(1.0f, 1.0f, 1.0f);
	}

	@Override
	public void renderText(PCws_TileEntityWeasel te, FontRenderer fontrenderer) {
		PC_Renderer.glDepthMask(false);
		PC_Renderer.glTranslatef(0.0F, 0.0625F * 15, 0.0625F + 0.001F);
		PC_Renderer.glScalef(0.01111111f, -0.01111111f, 0.01111111f);

		int j = 0xffffff;
		if(te.getData("fgcolor")!=null)
			j = (Integer)te.getData("fgcolor");
		String s="";
		if(te.getData("text")!=null)
			s = (String)te.getData("text");
		int align = 0;
		if(te.getData("align")!=null)
			align = (Integer)te.getData("align");

		int i = -1;
		int maxlines = 6;
		int screenWidth = 76;

		String[] parts = s.split("\\\\n");
		
		try {
			String magic = "";

			two:
			for (String part : parts) {

				if (part.trim().length() == 0) {
					i++;
					continue;
				}
				if (part.charAt(part.length() - 1) == '\u00A7') {
					part = part.substring(0, part.length() - 1);
				}
				List<String> lines = fontrenderer.listFormattedStringToWidth(part, screenWidth);

				for (String line : lines) {


					if (line.trim().length() == 0) continue;

					if (line.charAt(line.length() - 1) == '\u00A7') {
						line = line.substring(0, line.length() - 1);
					}
					i++;

					String removedMagicStuff = line.replaceAll("\u00A7.", "");


					int len = fontrenderer.getStringWidth(removedMagicStuff);

					int start = -len / 2;

					if (align == 0) start = -len / 2;
					if (align == -1) start = -screenWidth / 2;
					if (align == 1) start = screenWidth / 2 - len;

					fontrenderer.drawString(magic + line, start, 2 + fontrenderer.FONT_HEIGHT * i, j);

					if (line.contains("\u00A7")) {
						int a = -1;
						while ((a = line.indexOf('\u00A7', a + 1)) != -1) {
							magic += '\u00A7';
							if (line.length() > a + 1) {
								char ch = line.charAt(a + 1);
								if (ch == 'r') {
									magic = "";
								} else {
									magic += ch;
								}
							}
						}
					}

					if (i == maxlines) break two;
				}
			}
		} catch (StringIndexOutOfBoundsException e) {}
		PC_Renderer.glDepthMask(true);
	}
	
	
	
}
