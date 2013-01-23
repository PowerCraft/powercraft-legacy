package net.minecraft.src;

import java.io.File;

class TexturePackDownloadSuccess implements IDownloadSuccess
{
    final TexturePackList texturePacks;

    TexturePackDownloadSuccess(TexturePackList par1TexturePackList)
    {
        this.texturePacks = par1TexturePackList;
    }

    public void onSuccess(File par1File)
    {
        if (TexturePackList.func_77301_a(this.texturePacks))
        {
            TexturePackList.setSelectedTexturePack(this.texturePacks, new TexturePackCustom(TexturePackList.generateTexturePackID(this.texturePacks, par1File), par1File));
            TexturePackList.getMinecraft(this.texturePacks).scheduleTexturePackRefresh();
        }
    }
}
