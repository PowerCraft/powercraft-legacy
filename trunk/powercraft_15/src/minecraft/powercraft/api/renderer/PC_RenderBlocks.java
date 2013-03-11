package powercraft.api.renderer;

import net.minecraft.block.Block;
import net.minecraft.block.BlockGrass;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;

public class PC_RenderBlocks extends RenderBlocks {

	public PC_RenderBlocks(IBlockAccess blockAccess){
		super(blockAccess);
	}
	
	public boolean renderStandardBlockWithAmbientOcclusion(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7)
    {
        this.enableAO = true;
        boolean flag = false;
        float f3 = this.lightValueOwn;
        float f4 = this.lightValueOwn;
        float f5 = this.lightValueOwn;
        float f6 = this.lightValueOwn;
        boolean flag1 = true;
        boolean flag2 = true;
        boolean flag3 = true;
        boolean flag4 = true;
        boolean flag5 = true;
        boolean flag6 = true;
        this.lightValueOwn = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4);
        this.aoLightValueXNeg = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
        this.aoLightValueYNeg = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
        this.aoLightValueZNeg = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
        this.aoLightValueXPos = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
        this.aoLightValueYPos = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
        this.aoLightValueZPos = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
        int l = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
        int i1 = l;
        int j1 = l;
        int k1 = l;
        int l1 = l;
        int i2 = l;
        int j2 = l;

        if (this.renderMinY <= 0.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3 - 1, par4))
        {
            j1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
        }

        if (this.renderMaxY >= 1.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3 + 1, par4))
        {
            i2 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
        }

        if (this.renderMinX <= 0.0D || !this.blockAccess.isBlockOpaqueCube(par2 - 1, par3, par4))
        {
            i1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
        }

        if (this.renderMaxX >= 1.0D || !this.blockAccess.isBlockOpaqueCube(par2 + 1, par3, par4))
        {
            l1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
        }

        if (this.renderMinZ <= 0.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3, par4 - 1))
        {
            k1 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
        }

        if (this.renderMaxZ >= 1.0D || !this.blockAccess.isBlockOpaqueCube(par2, par3, par4 + 1))
        {
            j2 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
        }

        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);
        this.aoGrassXYZPPC = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 + 1, par4)];
        this.aoGrassXYZPNC = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3 - 1, par4)];
        this.aoGrassXYZPCP = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 + 1)];
        this.aoGrassXYZPCN = Block.canBlockGrass[this.blockAccess.getBlockId(par2 + 1, par3, par4 - 1)];
        this.aoGrassXYZNPC = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 + 1, par4)];
        this.aoGrassXYZNNC = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3 - 1, par4)];
        this.aoGrassXYZNCN = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 - 1)];
        this.aoGrassXYZNCP = Block.canBlockGrass[this.blockAccess.getBlockId(par2 - 1, par3, par4 + 1)];
        this.aoGrassXYZCPP = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 + 1)];
        this.aoGrassXYZCPN = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 + 1, par4 - 1)];
        this.aoGrassXYZCNP = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 + 1)];
        this.aoGrassXYZCNN = Block.canBlockGrass[this.blockAccess.getBlockId(par2, par3 - 1, par4 - 1)];

        if (this.func_94175_b(par1Block).func_94215_i().equals("grass_top"))
        {
            flag6 = false;
            flag5 = false;
            flag4 = false;
            flag3 = false;
            flag1 = false;
        }

        if (this.func_94167_b())
        {
            flag6 = false;
            flag5 = false;
            flag4 = false;
            flag3 = false;
            flag1 = false;
        }

        if (this.renderAllFaces || shouldSideBeRendered(par1Block, par2, par3 - 1, par4, 0))
        {
            if (this.aoType > 0)
            {
                if (this.renderMinY <= 0.0D)
                {
                    --par3;
                }

                this.aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
                this.aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
                this.aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
                this.aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
                this.aoLightValueScratchXYNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
                this.aoLightValueScratchYZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
                this.aoLightValueScratchYZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
                this.aoLightValueScratchXYPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);

                if (!this.aoGrassXYZCNN && !this.aoGrassXYZNNC)
                {
                    this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXYNN;
                    this.aoBrightnessXYZNNN = this.aoBrightnessXYNN;
                }
                else
                {
                    this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 - 1);
                    this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 - 1);
                }

                if (!this.aoGrassXYZCNP && !this.aoGrassXYZNNC)
                {
                    this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXYNN;
                    this.aoBrightnessXYZNNP = this.aoBrightnessXYNN;
                }
                else
                {
                    this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 + 1);
                    this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 + 1);
                }

                if (!this.aoGrassXYZCNN && !this.aoGrassXYZPNC)
                {
                    this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXYPN;
                    this.aoBrightnessXYZPNN = this.aoBrightnessXYPN;
                }
                else
                {
                    this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 - 1);
                    this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 - 1);
                }

                if (!this.aoGrassXYZCNP && !this.aoGrassXYZPNC)
                {
                    this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXYPN;
                    this.aoBrightnessXYZPNP = this.aoBrightnessXYPN;
                }
                else
                {
                    this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 + 1);
                    this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 + 1);
                }

                if (this.renderMinY <= 0.0D)
                {
                    ++par3;
                }

                f3 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXYNN + this.aoLightValueScratchYZNP + this.aoLightValueYNeg) / 4.0F;
                f6 = (this.aoLightValueScratchYZNP + this.aoLightValueYNeg + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXYPN) / 4.0F;
                f5 = (this.aoLightValueYNeg + this.aoLightValueScratchYZNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNN) / 4.0F;
                f4 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNN + this.aoLightValueYNeg + this.aoLightValueScratchYZNN) / 4.0F;
                this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXYNN, this.aoBrightnessYZNP, j1);
                this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXYPN, j1);
                this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYPN, this.aoBrightnessXYZPNN, j1);
                this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNN, this.aoBrightnessYZNN, j1);
            }
            else
            {
                f6 = this.aoLightValueYNeg;
                f5 = this.aoLightValueYNeg;
                f4 = this.aoLightValueYNeg;
                f3 = this.aoLightValueYNeg;
                this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = this.aoBrightnessXYNN;
            }

            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag1 ? par5 : 1.0F) * 0.5F;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag1 ? par6 : 1.0F) * 0.5F;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag1 ? par7 : 1.0F) * 0.5F;
            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            this.renderBottomFace(par1Block, (double)par2, (double)par3, (double)par4, this.func_94170_a(par1Block, this.blockAccess, par2, par3, par4, 0));
            flag = true;
        }

        if (this.renderAllFaces || shouldSideBeRendered(par1Block, par2, par3 + 1, par4, 1))
        {
            if (this.aoType > 0)
            {
                if (this.renderMaxY >= 1.0D)
                {
                    ++par3;
                }

                this.aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
                this.aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
                this.aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
                this.aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
                this.aoLightValueScratchXYNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
                this.aoLightValueScratchXYPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
                this.aoLightValueScratchYZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
                this.aoLightValueScratchYZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);

                if (!this.aoGrassXYZCPN && !this.aoGrassXYZNPC)
                {
                    this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXYNP;
                    this.aoBrightnessXYZNPN = this.aoBrightnessXYNP;
                }
                else
                {
                    this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 - 1);
                    this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 - 1);
                }

                if (!this.aoGrassXYZCPN && !this.aoGrassXYZPPC)
                {
                    this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXYPP;
                    this.aoBrightnessXYZPPN = this.aoBrightnessXYPP;
                }
                else
                {
                    this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 - 1);
                    this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 - 1);
                }

                if (!this.aoGrassXYZCPP && !this.aoGrassXYZNPC)
                {
                    this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXYNP;
                    this.aoBrightnessXYZNPP = this.aoBrightnessXYNP;
                }
                else
                {
                    this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4 + 1);
                    this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4 + 1);
                }

                if (!this.aoGrassXYZCPP && !this.aoGrassXYZPPC)
                {
                    this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXYPP;
                    this.aoBrightnessXYZPPP = this.aoBrightnessXYPP;
                }
                else
                {
                    this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4 + 1);
                    this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4 + 1);
                }

                if (this.renderMaxY >= 1.0D)
                {
                    --par3;
                }

                f6 = (this.aoLightValueScratchXYZNPP + this.aoLightValueScratchXYNP + this.aoLightValueScratchYZPP + this.aoLightValueYPos) / 4.0F;
                f3 = (this.aoLightValueScratchYZPP + this.aoLightValueYPos + this.aoLightValueScratchXYZPPP + this.aoLightValueScratchXYPP) / 4.0F;
                f4 = (this.aoLightValueYPos + this.aoLightValueScratchYZPN + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPN) / 4.0F;
                f5 = (this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPN + this.aoLightValueYPos + this.aoLightValueScratchYZPN) / 4.0F;
                this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNPP, this.aoBrightnessXYNP, this.aoBrightnessYZPP, i2);
                this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXYZPPP, this.aoBrightnessXYPP, i2);
                this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXYPP, this.aoBrightnessXYZPPN, i2);
                this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYNP, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, i2);
            }
            else
            {
                f6 = this.aoLightValueYPos;
                f5 = this.aoLightValueYPos;
                f4 = this.aoLightValueYPos;
                f3 = this.aoLightValueYPos;
                this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = i2;
            }

            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = flag2 ? par5 : 1.0F;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = flag2 ? par6 : 1.0F;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = flag2 ? par7 : 1.0F;
            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            this.renderTopFace(par1Block, (double)par2, (double)par3, (double)par4, this.func_94170_a(par1Block, this.blockAccess, par2, par3, par4, 1));
            flag = true;
        }

        float f7;
        float f8;
        float f9;
        int k2;
        float f10;
        int l2;
        Icon icon;
        int i3;
        int j3;

        if (this.renderAllFaces || shouldSideBeRendered(par1Block, par2, par3, par4 - 1, 2))
        {
            if (this.aoType > 0)
            {
                if (this.renderMinZ <= 0.0D)
                {
                    --par4;
                }

                this.aoLightValueScratchXZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
                this.aoLightValueScratchYZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
                this.aoLightValueScratchYZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
                this.aoLightValueScratchXZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
                this.aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
                this.aoBrightnessYZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
                this.aoBrightnessYZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
                this.aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);

                if (!this.aoGrassXYZNCN && !this.aoGrassXYZCNN)
                {
                    this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
                    this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
                }
                else
                {
                    this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 - 1, par4);
                    this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 - 1, par4);
                }

                if (!this.aoGrassXYZNCN && !this.aoGrassXYZCPN)
                {
                    this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
                    this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
                }
                else
                {
                    this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 + 1, par4);
                    this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 + 1, par4);
                }

                if (!this.aoGrassXYZPCN && !this.aoGrassXYZCNN)
                {
                    this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
                    this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
                }
                else
                {
                    this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 - 1, par4);
                    this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 - 1, par4);
                }

                if (!this.aoGrassXYZPCN && !this.aoGrassXYZCPN)
                {
                    this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
                    this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
                }
                else
                {
                    this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 + 1, par4);
                    this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 + 1, par4);
                }

                if (this.renderMinZ <= 0.0D)
                {
                    ++par4;
                }

                if (this.field_98189_n && this.field_94177_n.gameSettings.ambientOcclusion >= 2)
                {
                    f7 = (this.aoLightValueScratchXZNN + this.aoLightValueScratchXYZNPN + this.aoLightValueZNeg + this.aoLightValueScratchYZPN) / 4.0F;
                    f9 = (this.aoLightValueZNeg + this.aoLightValueScratchYZPN + this.aoLightValueScratchXZPN + this.aoLightValueScratchXYZPPN) / 4.0F;
                    f8 = (this.aoLightValueScratchYZNN + this.aoLightValueZNeg + this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXZPN) / 4.0F;
                    f10 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXZNN + this.aoLightValueScratchYZNN + this.aoLightValueZNeg) / 4.0F;
                    f3 = (float)((double)f7 * this.renderMaxY * (1.0D - this.renderMinX) + (double)f9 * this.renderMinY * this.renderMinX + (double)f8 * (1.0D - this.renderMaxY) * this.renderMinX + (double)f10 * (1.0D - this.renderMaxY) * (1.0D - this.renderMinX));
                    f4 = (float)((double)f7 * this.renderMaxY * (1.0D - this.renderMaxX) + (double)f9 * this.renderMaxY * this.renderMaxX + (double)f8 * (1.0D - this.renderMaxY) * this.renderMaxX + (double)f10 * (1.0D - this.renderMaxY) * (1.0D - this.renderMaxX));
                    f5 = (float)((double)f7 * this.renderMinY * (1.0D - this.renderMaxX) + (double)f9 * this.renderMinY * this.renderMaxX + (double)f8 * (1.0D - this.renderMinY) * this.renderMaxX + (double)f10 * (1.0D - this.renderMinY) * (1.0D - this.renderMaxX));
                    f6 = (float)((double)f7 * this.renderMinY * (1.0D - this.renderMinX) + (double)f9 * this.renderMinY * this.renderMinX + (double)f8 * (1.0D - this.renderMinY) * this.renderMinX + (double)f10 * (1.0D - this.renderMinY) * (1.0D - this.renderMinX));
                    k2 = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, k1);
                    i3 = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, k1);
                    j3 = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYZPNN, this.aoBrightnessXZPN, k1);
                    l2 = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXZNN, this.aoBrightnessYZNN, k1);
                    this.brightnessTopLeft = this.func_96444_a(k2, i3, j3, l2, this.renderMaxY * (1.0D - this.renderMinX), this.renderMaxY * this.renderMinX, (1.0D - this.renderMaxY) * this.renderMinX, (1.0D - this.renderMaxY) * (1.0D - this.renderMinX));
                    this.brightnessBottomLeft = this.func_96444_a(k2, i3, j3, l2, this.renderMaxY * (1.0D - this.renderMaxX), this.renderMaxY * this.renderMaxX, (1.0D - this.renderMaxY) * this.renderMaxX, (1.0D - this.renderMaxY) * (1.0D - this.renderMaxX));
                    this.brightnessBottomRight = this.func_96444_a(k2, i3, j3, l2, this.renderMinY * (1.0D - this.renderMaxX), this.renderMinY * this.renderMaxX, (1.0D - this.renderMinY) * this.renderMaxX, (1.0D - this.renderMinY) * (1.0D - this.renderMaxX));
                    this.brightnessTopRight = this.func_96444_a(k2, i3, j3, l2, this.renderMinY * (1.0D - this.renderMinX), this.renderMinY * this.renderMinX, (1.0D - this.renderMinY) * this.renderMinX, (1.0D - this.renderMinY) * (1.0D - this.renderMinX));
                }
                else
                {
                    f3 = (this.aoLightValueScratchXZNN + this.aoLightValueScratchXYZNPN + this.aoLightValueZNeg + this.aoLightValueScratchYZPN) / 4.0F;
                    f4 = (this.aoLightValueZNeg + this.aoLightValueScratchYZPN + this.aoLightValueScratchXZPN + this.aoLightValueScratchXYZPPN) / 4.0F;
                    f5 = (this.aoLightValueScratchYZNN + this.aoLightValueZNeg + this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXZPN) / 4.0F;
                    f6 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXZNN + this.aoLightValueScratchYZNN + this.aoLightValueZNeg) / 4.0F;
                    this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, k1);
                    this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, k1);
                    this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYZPNN, this.aoBrightnessXZPN, k1);
                    this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXZNN, this.aoBrightnessYZNN, k1);
                }
            }
            else
            {
                f6 = this.aoLightValueZNeg;
                f5 = this.aoLightValueZNeg;
                f4 = this.aoLightValueZNeg;
                f3 = this.aoLightValueZNeg;
                this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = k1;
            }

            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag3 ? par5 : 1.0F) * 0.8F;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag3 ? par6 : 1.0F) * 0.8F;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag3 ? par7 : 1.0F) * 0.8F;
            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            icon = this.func_94170_a(par1Block, this.blockAccess, par2, par3, par4, 2);
            this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, icon);

            if (fancyGrass && icon.func_94215_i().equals("grass_side") && !this.func_94167_b())
            {
                this.colorRedTopLeft *= par5;
                this.colorRedBottomLeft *= par5;
                this.colorRedBottomRight *= par5;
                this.colorRedTopRight *= par5;
                this.colorGreenTopLeft *= par6;
                this.colorGreenBottomLeft *= par6;
                this.colorGreenBottomRight *= par6;
                this.colorGreenTopRight *= par6;
                this.colorBlueTopLeft *= par7;
                this.colorBlueBottomLeft *= par7;
                this.colorBlueBottomRight *= par7;
                this.colorBlueTopRight *= par7;
                this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.func_94434_o());
            }

            flag = true;
        }

        if (this.renderAllFaces || shouldSideBeRendered(par1Block, par2, par3, par4 + 1, 3))
        {
            if (this.aoType > 0)
            {
                if (this.renderMaxZ >= 1.0D)
                {
                    ++par4;
                }

                this.aoLightValueScratchXZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
                this.aoLightValueScratchXZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
                this.aoLightValueScratchYZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
                this.aoLightValueScratchYZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
                this.aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
                this.aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
                this.aoBrightnessYZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
                this.aoBrightnessYZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);

                if (!this.aoGrassXYZNCP && !this.aoGrassXYZCNP)
                {
                    this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
                    this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
                }
                else
                {
                    this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 - 1, par4);
                    this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 - 1, par4);
                }

                if (!this.aoGrassXYZNCP && !this.aoGrassXYZCPP)
                {
                    this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
                    this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
                }
                else
                {
                    this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3 + 1, par4);
                    this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3 + 1, par4);
                }

                if (!this.aoGrassXYZPCP && !this.aoGrassXYZCNP)
                {
                    this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
                    this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
                }
                else
                {
                    this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 - 1, par4);
                    this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 - 1, par4);
                }

                if (!this.aoGrassXYZPCP && !this.aoGrassXYZCPP)
                {
                    this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
                    this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
                }
                else
                {
                    this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3 + 1, par4);
                    this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3 + 1, par4);
                }

                if (this.renderMaxZ >= 1.0D)
                {
                    --par4;
                }

                if (this.field_98189_n && this.field_94177_n.gameSettings.ambientOcclusion >= 2)
                {
                    f7 = (this.aoLightValueScratchXZNP + this.aoLightValueScratchXYZNPP + this.aoLightValueZPos + this.aoLightValueScratchYZPP) / 4.0F;
                    f9 = (this.aoLightValueZPos + this.aoLightValueScratchYZPP + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYZPPP) / 4.0F;
                    f8 = (this.aoLightValueScratchYZNP + this.aoLightValueZPos + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXZPP) / 4.0F;
                    f10 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXZNP + this.aoLightValueScratchYZNP + this.aoLightValueZPos) / 4.0F;
                    f3 = (float)((double)f7 * this.renderMaxY * (1.0D - this.renderMinX) + (double)f9 * this.renderMaxY * this.renderMinX + (double)f8 * (1.0D - this.renderMaxY) * this.renderMinX + (double)f10 * (1.0D - this.renderMaxY) * (1.0D - this.renderMinX));
                    f4 = (float)((double)f7 * this.renderMinY * (1.0D - this.renderMinX) + (double)f9 * this.renderMinY * this.renderMinX + (double)f8 * (1.0D - this.renderMinY) * this.renderMinX + (double)f10 * (1.0D - this.renderMinY) * (1.0D - this.renderMinX));
                    f5 = (float)((double)f7 * this.renderMinY * (1.0D - this.renderMaxX) + (double)f9 * this.renderMinY * this.renderMaxX + (double)f8 * (1.0D - this.renderMinY) * this.renderMaxX + (double)f10 * (1.0D - this.renderMinY) * (1.0D - this.renderMaxX));
                    f6 = (float)((double)f7 * this.renderMaxY * (1.0D - this.renderMaxX) + (double)f9 * this.renderMaxY * this.renderMaxX + (double)f8 * (1.0D - this.renderMaxY) * this.renderMaxX + (double)f10 * (1.0D - this.renderMaxY) * (1.0D - this.renderMaxX));
                    k2 = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYZNPP, this.aoBrightnessYZPP, j2);
                    i3 = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXZPP, this.aoBrightnessXYZPPP, j2);
                    j3 = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, j2);
                    l2 = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, this.aoBrightnessYZNP, j2);
                    this.brightnessTopLeft = this.func_96444_a(k2, l2, j3, i3, this.renderMaxY * (1.0D - this.renderMinX), (1.0D - this.renderMaxY) * (1.0D - this.renderMinX), (1.0D - this.renderMaxY) * this.renderMinX, this.renderMaxY * this.renderMinX);
                    this.brightnessBottomLeft = this.func_96444_a(k2, l2, j3, i3, this.renderMinY * (1.0D - this.renderMinX), (1.0D - this.renderMinY) * (1.0D - this.renderMinX), (1.0D - this.renderMinY) * this.renderMinX, this.renderMinY * this.renderMinX);
                    this.brightnessBottomRight = this.func_96444_a(k2, l2, j3, i3, this.renderMinY * (1.0D - this.renderMaxX), (1.0D - this.renderMinY) * (1.0D - this.renderMaxX), (1.0D - this.renderMinY) * this.renderMaxX, this.renderMinY * this.renderMaxX);
                    this.brightnessTopRight = this.func_96444_a(k2, l2, j3, i3, this.renderMaxY * (1.0D - this.renderMaxX), (1.0D - this.renderMaxY) * (1.0D - this.renderMaxX), (1.0D - this.renderMaxY) * this.renderMaxX, this.renderMaxY * this.renderMaxX);
                }
                else
                {
                    f3 = (this.aoLightValueScratchXZNP + this.aoLightValueScratchXYZNPP + this.aoLightValueZPos + this.aoLightValueScratchYZPP) / 4.0F;
                    f6 = (this.aoLightValueZPos + this.aoLightValueScratchYZPP + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYZPPP) / 4.0F;
                    f5 = (this.aoLightValueScratchYZNP + this.aoLightValueZPos + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXZPP) / 4.0F;
                    f4 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXZNP + this.aoLightValueScratchYZNP + this.aoLightValueZPos) / 4.0F;
                    this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYZNPP, this.aoBrightnessYZPP, j2);
                    this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXZPP, this.aoBrightnessXYZPPP, j2);
                    this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, j2);
                    this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, this.aoBrightnessYZNP, j2);
                }
            }
            else
            {
                f6 = this.aoLightValueZPos;
                f5 = this.aoLightValueZPos;
                f4 = this.aoLightValueZPos;
                f3 = this.aoLightValueZPos;
                this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = j2;
            }

            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag4 ? par5 : 1.0F) * 0.8F;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag4 ? par6 : 1.0F) * 0.8F;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag4 ? par7 : 1.0F) * 0.8F;
            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            icon = this.func_94170_a(par1Block, this.blockAccess, par2, par3, par4, 3);
            this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, this.func_94170_a(par1Block, this.blockAccess, par2, par3, par4, 3));

            if (fancyGrass && icon.func_94215_i().equals("grass_side") && !this.func_94167_b())
            {
                this.colorRedTopLeft *= par5;
                this.colorRedBottomLeft *= par5;
                this.colorRedBottomRight *= par5;
                this.colorRedTopRight *= par5;
                this.colorGreenTopLeft *= par6;
                this.colorGreenBottomLeft *= par6;
                this.colorGreenBottomRight *= par6;
                this.colorGreenTopRight *= par6;
                this.colorBlueTopLeft *= par7;
                this.colorBlueBottomLeft *= par7;
                this.colorBlueBottomRight *= par7;
                this.colorBlueTopRight *= par7;
                this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.func_94434_o());
            }

            flag = true;
        }

        if (this.renderAllFaces || shouldSideBeRendered(par1Block, par2 - 1, par3, par4, 4))
        {
            if (this.aoType > 0)
            {
                if (this.renderMinX <= 0.0D)
                {
                    --par2;
                }

                this.aoLightValueScratchXYNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
                this.aoLightValueScratchXZNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
                this.aoLightValueScratchXZNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
                this.aoLightValueScratchXYNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
                this.aoBrightnessXYNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
                this.aoBrightnessXZNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
                this.aoBrightnessXZNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
                this.aoBrightnessXYNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);

                if (!this.aoGrassXYZNCN && !this.aoGrassXYZNNC)
                {
                    this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
                    this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
                }
                else
                {
                    this.aoLightValueScratchXYZNNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 - 1);
                    this.aoBrightnessXYZNNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 - 1);
                }

                if (!this.aoGrassXYZNCP && !this.aoGrassXYZNNC)
                {
                    this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
                    this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
                }
                else
                {
                    this.aoLightValueScratchXYZNNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 + 1);
                    this.aoBrightnessXYZNNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 + 1);
                }

                if (!this.aoGrassXYZNCN && !this.aoGrassXYZNPC)
                {
                    this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
                    this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
                }
                else
                {
                    this.aoLightValueScratchXYZNPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 - 1);
                    this.aoBrightnessXYZNPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 - 1);
                }

                if (!this.aoGrassXYZNCP && !this.aoGrassXYZNPC)
                {
                    this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
                    this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
                }
                else
                {
                    this.aoLightValueScratchXYZNPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 + 1);
                    this.aoBrightnessXYZNPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 + 1);
                }

                if (this.renderMinX <= 0.0D)
                {
                    ++par2;
                }

                if (this.field_98189_n && this.field_94177_n.gameSettings.ambientOcclusion >= 2)
                {
                    f7 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNP + this.aoLightValueXNeg + this.aoLightValueScratchXZNP) / 4.0F;
                    f9 = (this.aoLightValueXNeg + this.aoLightValueScratchXZNP + this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPP) / 4.0F;
                    f8 = (this.aoLightValueScratchXZNN + this.aoLightValueXNeg + this.aoLightValueScratchXYZNPN + this.aoLightValueScratchXYNP) / 4.0F;
                    f10 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXYNN + this.aoLightValueScratchXZNN + this.aoLightValueXNeg) / 4.0F;
                    f3 = (float)((double)f9 * this.renderMaxY * this.renderMaxZ + (double)f8 * this.renderMaxY * (1.0D - this.renderMaxZ) + (double)f10 * (1.0D - this.renderMaxY) * (1.0D - this.renderMaxZ) + (double)f7 * (1.0D - this.renderMaxY) * this.renderMaxZ);
                    f4 = (float)((double)f9 * this.renderMaxY * this.renderMinZ + (double)f8 * this.renderMaxY * (1.0D - this.renderMinZ) + (double)f10 * (1.0D - this.renderMaxY) * (1.0D - this.renderMinZ) + (double)f7 * (1.0D - this.renderMaxY) * this.renderMinZ);
                    f5 = (float)((double)f9 * this.renderMinY * this.renderMinZ + (double)f8 * this.renderMinY * (1.0D - this.renderMinZ) + (double)f10 * (1.0D - this.renderMinY) * (1.0D - this.renderMinZ) + (double)f7 * (1.0D - this.renderMinY) * this.renderMinZ);
                    f6 = (float)((double)f9 * this.renderMinY * this.renderMaxZ + (double)f8 * this.renderMinY * (1.0D - this.renderMaxZ) + (double)f10 * (1.0D - this.renderMinY) * (1.0D - this.renderMaxZ) + (double)f7 * (1.0D - this.renderMinY) * this.renderMaxZ);
                    k2 = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, i1);
                    i3 = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYNP, this.aoBrightnessXYZNPP, i1);
                    j3 = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessXYNP, i1);
                    l2 = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXYNN, this.aoBrightnessXZNN, i1);
                    this.brightnessTopLeft = this.func_96444_a(i3, j3, l2, k2, this.renderMaxY * this.renderMaxZ, this.renderMaxY * (1.0D - this.renderMaxZ), (1.0D - this.renderMaxY) * (1.0D - this.renderMaxZ), (1.0D - this.renderMaxY) * this.renderMaxZ);
                    this.brightnessBottomLeft = this.func_96444_a(i3, j3, l2, k2, this.renderMaxY * this.renderMinZ, this.renderMaxY * (1.0D - this.renderMinZ), (1.0D - this.renderMaxY) * (1.0D - this.renderMinZ), (1.0D - this.renderMaxY) * this.renderMinZ);
                    this.brightnessBottomRight = this.func_96444_a(i3, j3, l2, k2, this.renderMinY * this.renderMinZ, this.renderMinY * (1.0D - this.renderMinZ), (1.0D - this.renderMinY) * (1.0D - this.renderMinZ), (1.0D - this.renderMinY) * this.renderMinZ);
                    this.brightnessTopRight = this.func_96444_a(i3, j3, l2, k2, this.renderMinY * this.renderMaxZ, this.renderMinY * (1.0D - this.renderMaxZ), (1.0D - this.renderMinY) * (1.0D - this.renderMaxZ), (1.0D - this.renderMinY) * this.renderMaxZ);
                }
                else
                {
                    f6 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNP + this.aoLightValueXNeg + this.aoLightValueScratchXZNP) / 4.0F;
                    f3 = (this.aoLightValueXNeg + this.aoLightValueScratchXZNP + this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPP) / 4.0F;
                    f4 = (this.aoLightValueScratchXZNN + this.aoLightValueXNeg + this.aoLightValueScratchXYZNPN + this.aoLightValueScratchXYNP) / 4.0F;
                    f5 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXYNN + this.aoLightValueScratchXZNN + this.aoLightValueXNeg) / 4.0F;
                    this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, i1);
                    this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYNP, this.aoBrightnessXYZNPP, i1);
                    this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessXYNP, i1);
                    this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXYNN, this.aoBrightnessXZNN, i1);
                }
            }
            else
            {
                f6 = this.aoLightValueXNeg;
                f5 = this.aoLightValueXNeg;
                f4 = this.aoLightValueXNeg;
                f3 = this.aoLightValueXNeg;
                this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = i1;
            }

            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag5 ? par5 : 1.0F) * 0.6F;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag5 ? par6 : 1.0F) * 0.6F;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag5 ? par7 : 1.0F) * 0.6F;
            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            icon = this.func_94170_a(par1Block, this.blockAccess, par2, par3, par4, 4);
            this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, icon);

            if (fancyGrass && icon.func_94215_i().equals("grass_side") && !this.func_94167_b())
            {
                this.colorRedTopLeft *= par5;
                this.colorRedBottomLeft *= par5;
                this.colorRedBottomRight *= par5;
                this.colorRedTopRight *= par5;
                this.colorGreenTopLeft *= par6;
                this.colorGreenBottomLeft *= par6;
                this.colorGreenBottomRight *= par6;
                this.colorGreenTopRight *= par6;
                this.colorBlueTopLeft *= par7;
                this.colorBlueBottomLeft *= par7;
                this.colorBlueBottomRight *= par7;
                this.colorBlueTopRight *= par7;
                this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.func_94434_o());
            }

            flag = true;
        }

        if (this.renderAllFaces || shouldSideBeRendered(par1Block, par2 + 1, par3, par4, 5))
        {
            if (this.aoType > 0)
            {
                if (this.renderMaxX >= 1.0D)
                {
                    ++par2;
                }

                this.aoLightValueScratchXYPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
                this.aoLightValueScratchXZPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
                this.aoLightValueScratchXZPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
                this.aoLightValueScratchXYPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
                this.aoBrightnessXYPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
                this.aoBrightnessXZPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
                this.aoBrightnessXZPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
                this.aoBrightnessXYPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);

                if (!this.aoGrassXYZPNC && !this.aoGrassXYZPCN)
                {
                    this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
                    this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
                }
                else
                {
                    this.aoLightValueScratchXYZPNN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 - 1);
                    this.aoBrightnessXYZPNN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 - 1);
                }

                if (!this.aoGrassXYZPNC && !this.aoGrassXYZPCP)
                {
                    this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
                    this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
                }
                else
                {
                    this.aoLightValueScratchXYZPNP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4 + 1);
                    this.aoBrightnessXYZPNP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4 + 1);
                }

                if (!this.aoGrassXYZPPC && !this.aoGrassXYZPCN)
                {
                    this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
                    this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
                }
                else
                {
                    this.aoLightValueScratchXYZPPN = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 - 1);
                    this.aoBrightnessXYZPPN = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 - 1);
                }

                if (!this.aoGrassXYZPPC && !this.aoGrassXYZPCP)
                {
                    this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
                    this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
                }
                else
                {
                    this.aoLightValueScratchXYZPPP = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4 + 1);
                    this.aoBrightnessXYZPPP = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4 + 1);
                }

                if (this.renderMaxX >= 1.0D)
                {
                    --par2;
                }

                if (this.field_98189_n && this.field_94177_n.gameSettings.ambientOcclusion >= 2)
                {
                    f7 = (this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNP + this.aoLightValueXPos + this.aoLightValueScratchXZPP) / 4.0F;
                    f9 = (this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXZPN + this.aoLightValueXPos) / 4.0F;
                    f8 = (this.aoLightValueScratchXZPN + this.aoLightValueXPos + this.aoLightValueScratchXYZPPN + this.aoLightValueScratchXYPP) / 4.0F;
                    f10 = (this.aoLightValueXPos + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPP) / 4.0F;
                    f3 = (float)((double)f7 * (1.0D - this.renderMinY) * this.renderMaxZ + (double)f9 * (1.0D - this.renderMinY) * (1.0D - this.renderMaxZ) + (double)f8 * this.renderMinY * (1.0D - this.renderMaxZ) + (double)f10 * this.renderMinY * this.renderMaxZ);
                    f4 = (float)((double)f7 * (1.0D - this.renderMinY) * this.renderMinZ + (double)f9 * (1.0D - this.renderMinY) * (1.0D - this.renderMinZ) + (double)f8 * this.renderMinY * (1.0D - this.renderMinZ) + (double)f10 * this.renderMinY * this.renderMinZ);
                    f5 = (float)((double)f7 * (1.0D - this.renderMaxY) * this.renderMinZ + (double)f9 * (1.0D - this.renderMaxY) * (1.0D - this.renderMinZ) + (double)f8 * this.renderMaxY * (1.0D - this.renderMinZ) + (double)f10 * this.renderMaxY * this.renderMinZ);
                    f6 = (float)((double)f7 * (1.0D - this.renderMaxY) * this.renderMaxZ + (double)f9 * (1.0D - this.renderMaxY) * (1.0D - this.renderMaxZ) + (double)f8 * this.renderMaxY * (1.0D - this.renderMaxZ) + (double)f10 * this.renderMaxY * this.renderMaxZ);
                    k2 = this.getAoBrightness(this.aoBrightnessXYPN, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, l1);
                    i3 = this.getAoBrightness(this.aoBrightnessXZPP, this.aoBrightnessXYPP, this.aoBrightnessXYZPPP, l1);
                    j3 = this.getAoBrightness(this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, this.aoBrightnessXYPP, l1);
                    l2 = this.getAoBrightness(this.aoBrightnessXYZPNN, this.aoBrightnessXYPN, this.aoBrightnessXZPN, l1);
                    this.brightnessTopLeft = this.func_96444_a(k2, l2, j3, i3, (1.0D - this.renderMinY) * this.renderMaxZ, (1.0D - this.renderMinY) * (1.0D - this.renderMaxZ), this.renderMinY * (1.0D - this.renderMaxZ), this.renderMinY * this.renderMaxZ);
                    this.brightnessBottomLeft = this.func_96444_a(k2, l2, j3, i3, (1.0D - this.renderMinY) * this.renderMinZ, (1.0D - this.renderMinY) * (1.0D - this.renderMinZ), this.renderMinY * (1.0D - this.renderMinZ), this.renderMinY * this.renderMinZ);
                    this.brightnessBottomRight = this.func_96444_a(k2, l2, j3, i3, (1.0D - this.renderMaxY) * this.renderMinZ, (1.0D - this.renderMaxY) * (1.0D - this.renderMinZ), this.renderMaxY * (1.0D - this.renderMinZ), this.renderMaxY * this.renderMinZ);
                    this.brightnessTopRight = this.func_96444_a(k2, l2, j3, i3, (1.0D - this.renderMaxY) * this.renderMaxZ, (1.0D - this.renderMaxY) * (1.0D - this.renderMaxZ), this.renderMaxY * (1.0D - this.renderMaxZ), this.renderMaxY * this.renderMaxZ);
                }
                else
                {
                    f3 = (this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNP + this.aoLightValueXPos + this.aoLightValueScratchXZPP) / 4.0F;
                    f4 = (this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXZPN + this.aoLightValueXPos) / 4.0F;
                    f5 = (this.aoLightValueScratchXZPN + this.aoLightValueXPos + this.aoLightValueScratchXYZPPN + this.aoLightValueScratchXYPP) / 4.0F;
                    f6 = (this.aoLightValueXPos + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPP) / 4.0F;
                    this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYPN, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, l1);
                    this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXZPP, this.aoBrightnessXYPP, this.aoBrightnessXYZPPP, l1);
                    this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, this.aoBrightnessXYPP, l1);
                    this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZPNN, this.aoBrightnessXYPN, this.aoBrightnessXZPN, l1);
                }
            }
            else
            {
                f6 = this.aoLightValueXPos;
                f5 = this.aoLightValueXPos;
                f4 = this.aoLightValueXPos;
                f3 = this.aoLightValueXPos;
                this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = l1;
            }

            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (flag6 ? par5 : 1.0F) * 0.6F;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (flag6 ? par6 : 1.0F) * 0.6F;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (flag6 ? par7 : 1.0F) * 0.6F;
            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            icon = this.func_94170_a(par1Block, this.blockAccess, par2, par3, par4, 5);
            this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, icon);

            if (fancyGrass && icon.func_94215_i().equals("grass_side") && !this.func_94167_b())
            {
                this.colorRedTopLeft *= par5;
                this.colorRedBottomLeft *= par5;
                this.colorRedBottomRight *= par5;
                this.colorRedTopRight *= par5;
                this.colorGreenTopLeft *= par6;
                this.colorGreenBottomLeft *= par6;
                this.colorGreenBottomRight *= par6;
                this.colorGreenTopRight *= par6;
                this.colorBlueTopLeft *= par7;
                this.colorBlueBottomLeft *= par7;
                this.colorBlueBottomRight *= par7;
                this.colorBlueTopRight *= par7;
                this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.func_94434_o());
            }

            flag = true;
        }

        this.enableAO = false;
        return flag;
    }
    
    public boolean renderStandardBlockWithColorMultiplier(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7)
    {
        this.enableAO = false;
        Tessellator tessellator = Tessellator.instance;
        boolean flag = false;
        float f3 = 0.5F;
        float f4 = 1.0F;
        float f5 = 0.8F;
        float f6 = 0.6F;
        float f7 = f4 * par5;
        float f8 = f4 * par6;
        float f9 = f4 * par7;
        float f10 = f3;
        float f11 = f5;
        float f12 = f6;
        float f13 = f3;
        float f14 = f5;
        float f15 = f6;
        float f16 = f3;
        float f17 = f5;
        float f18 = f6;

        if (par1Block != Block.grass)
        {
            f10 = f3 * par5;
            f11 = f5 * par5;
            f12 = f6 * par5;
            f13 = f3 * par6;
            f14 = f5 * par6;
            f15 = f6 * par6;
            f16 = f3 * par7;
            f17 = f5 * par7;
            f18 = f6 * par7;
        }

        int l = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);

        if (this.renderAllFaces || shouldSideBeRendered(par1Block, par2, par3 - 1, par4, 0))
        {
            tessellator.setBrightness(this.renderMinY > 0.0D ? l : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4));
            tessellator.setColorOpaque_F(f10, f13, f16);
            this.renderBottomFace(par1Block, (double)par2, (double)par3, (double)par4, this.func_94170_a(par1Block, this.blockAccess, par2, par3, par4, 0));
            flag = true;
        }

        if (this.renderAllFaces || shouldSideBeRendered(par1Block, par2, par3 + 1, par4, 1))
        {
            tessellator.setBrightness(this.renderMaxY < 1.0D ? l : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4));
            tessellator.setColorOpaque_F(f7, f8, f9);
            this.renderTopFace(par1Block, (double)par2, (double)par3, (double)par4, this.func_94170_a(par1Block, this.blockAccess, par2, par3, par4, 1));
            flag = true;
        }

        Icon icon;

        if (this.renderAllFaces || shouldSideBeRendered(par1Block, par2, par3, par4 - 1, 2))
        {
            tessellator.setBrightness(this.renderMinZ > 0.0D ? l : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            icon = this.func_94170_a(par1Block, this.blockAccess, par2, par3, par4, 2);
            this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, icon);

            if (fancyGrass && icon.func_94215_i().equals("grass_side") && !this.func_94167_b())
            {
                tessellator.setColorOpaque_F(f11 * par5, f14 * par6, f17 * par7);
                this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.func_94434_o());
            }

            flag = true;
        }

        if (this.renderAllFaces || shouldSideBeRendered(par1Block, par2, par3, par4 + 1, 3))
        {
            tessellator.setBrightness(this.renderMaxZ < 1.0D ? l : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1));
            tessellator.setColorOpaque_F(f11, f14, f17);
            icon = this.func_94170_a(par1Block, this.blockAccess, par2, par3, par4, 3);
            this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, icon);

            if (fancyGrass && icon.func_94215_i().equals("grass_side") && !this.func_94167_b())
            {
                tessellator.setColorOpaque_F(f11 * par5, f14 * par6, f17 * par7);
                this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.func_94434_o());
            }

            flag = true;
        }

        if (this.renderAllFaces || shouldSideBeRendered(par1Block, par2 - 1, par3, par4, 4))
        {
            tessellator.setBrightness(this.renderMinX > 0.0D ? l : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4));
            tessellator.setColorOpaque_F(f12, f15, f18);
            icon = this.func_94170_a(par1Block, this.blockAccess, par2, par3, par4, 4);
            this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, icon);

            if (fancyGrass && icon.func_94215_i().equals("grass_side") && !this.func_94167_b())
            {
                tessellator.setColorOpaque_F(f12 * par5, f15 * par6, f18 * par7);
                this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.func_94434_o());
            }

            flag = true;
        }

        if (this.renderAllFaces || shouldSideBeRendered(par1Block, par2 + 1, par3, par4, 5))
        {
            tessellator.setBrightness(this.renderMaxX < 1.0D ? l : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4));
            tessellator.setColorOpaque_F(f12, f15, f18);
            icon = this.func_94170_a(par1Block, this.blockAccess, par2, par3, par4, 5);
            this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, icon);

            if (fancyGrass && icon.func_94215_i().equals("grass_side") && !this.func_94167_b())
            {
                tessellator.setColorOpaque_F(f12 * par5, f15 * par6, f18 * par7);
                this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, BlockGrass.func_94434_o());
            }

            flag = true;
        }

        return flag;
    }

	public boolean shouldSideBeRendered(Block block, int x, int y, int z, int dir){
		return block.shouldSideBeRendered(blockAccess, x, y, z, dir);
	}
	
}
