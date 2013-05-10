package mods.betterworld.DeCB.client;


import net.minecraft.item.ItemStack;
import mods.betterworld.CB.core.BWCB_BlockList;
import mods.betterworld.DeCB.BWDeCB;
import mods.betterworld.DeCB.core.BWDeCB_Proxy;
import mods.betterworld.DeCB.tileEntity.BWDeCB_TileEntityBlockColumnRound;
import mods.betterworld.DeCB.tileEntity.BWDeCB_TileEntityBlockColumnSquare;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class BWDeCB_ClientProxy extends BWDeCB_Proxy{

	@Override
	public void registerRendering() {
		RenderingRegistry.registerBlockHandler(BWDeCB_Render.getRender());
		
		ClientRegistry.bindTileEntitySpecialRenderer(BWDeCB_TileEntityBlockColumnSquare.class, new BWDeCB_RendererColumnSquare());
		ClientRegistry.bindTileEntitySpecialRenderer(BWDeCB_TileEntityBlockColumnRound.class, new BWDeCB_RendererColumnRound());
		
		BWDeCB.renderColumnSquareID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new BWDeCB_RendererColumnSquare());
		
		
		BWDeCB.renderColumnRoundID = RenderingRegistry.getNextAvailableRenderId();
		RenderingRegistry.registerBlockHandler(new BWDeCB_RendererColumnRound());
		
	}
	
}
