package powercraft.management;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;

public class PC_RenderBlocks extends RenderBlocks {

	public PC_RenderBlocks(IBlockAccess blockAccess){
		super(blockAccess);
	}
	
	@Override
	public boolean renderStandardBlockWithAmbientOcclusion(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7)
    {
        this.enableAO = true;
        boolean var8 = false;
        float var9 = this.lightValueOwn;
        float var10 = this.lightValueOwn;
        float var11 = this.lightValueOwn;
        float var12 = this.lightValueOwn;
        boolean var13 = true;
        boolean var14 = true;
        boolean var15 = true;
        boolean var16 = true;
        boolean var17 = true;
        boolean var18 = true;
        this.lightValueOwn = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4);
        this.aoLightValueXNeg = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 - 1, par3, par4);
        this.aoLightValueYNeg = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 - 1, par4);
        this.aoLightValueZNeg = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 - 1);
        this.aoLightValueXPos = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2 + 1, par3, par4);
        this.aoLightValueYPos = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3 + 1, par4);
        this.aoLightValueZPos = par1Block.getAmbientOcclusionLightValue(this.blockAccess, par2, par3, par4 + 1);
        int var19 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);
        int var20 = var19;
        int var21 = var19;
        int var22 = var19;
        int var23 = var19;
        int var24 = var19;
        int var25 = var19;

        if (this.renderMinY <= 0.0D)
        {
            var21 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4);
        }

        if (this.renderMaxY >= 1.0D)
        {
            var24 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4);
        }

        if (this.renderMinX <= 0.0D)
        {
            var20 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4);
        }

        if (this.renderMaxX >= 1.0D)
        {
            var23 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4);
        }

        if (this.renderMinZ <= 0.0D)
        {
            var22 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1);
        }

        if (this.renderMaxZ >= 1.0D)
        {
            var25 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1);
        }

        Tessellator var26 = Tessellator.instance;
        var26.setBrightness(983055);
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

        if (par1Block.blockIndexInTexture == 3)
        {
            var18 = false;
            var17 = false;
            var16 = false;
            var15 = false;
            var13 = false;
        }

        if (this.overrideBlockTexture >= 0)
        {
            var18 = false;
            var17 = false;
            var16 = false;
            var15 = false;
            var13 = false;
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

                var9 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXYNN + this.aoLightValueScratchYZNP + this.aoLightValueYNeg) / 4.0F;
                var12 = (this.aoLightValueScratchYZNP + this.aoLightValueYNeg + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXYPN) / 4.0F;
                var11 = (this.aoLightValueYNeg + this.aoLightValueScratchYZNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNN) / 4.0F;
                var10 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNN + this.aoLightValueYNeg + this.aoLightValueScratchYZNN) / 4.0F;
                this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXYNN, this.aoBrightnessYZNP, var21);
                this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXYPN, var21);
                this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYPN, this.aoBrightnessXYZPNN, var21);
                this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNN, this.aoBrightnessYZNN, var21);
            }
            else
            {
                var12 = this.aoLightValueYNeg;
                var11 = this.aoLightValueYNeg;
                var10 = this.aoLightValueYNeg;
                var9 = this.aoLightValueYNeg;
                this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = this.aoBrightnessXYNN;
            }

            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var13 ? par5 : 1.0F) * 0.5F;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var13 ? par6 : 1.0F) * 0.5F;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var13 ? par7 : 1.0F) * 0.5F;
            this.colorRedTopLeft *= var9;
            this.colorGreenTopLeft *= var9;
            this.colorBlueTopLeft *= var9;
            this.colorRedBottomLeft *= var10;
            this.colorGreenBottomLeft *= var10;
            this.colorBlueBottomLeft *= var10;
            this.colorRedBottomRight *= var11;
            this.colorGreenBottomRight *= var11;
            this.colorBlueBottomRight *= var11;
            this.colorRedTopRight *= var12;
            this.colorGreenTopRight *= var12;
            this.colorBlueTopRight *= var12;
            this.renderBottomFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 0));
            var8 = true;
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

                var12 = (this.aoLightValueScratchXYZNPP + this.aoLightValueScratchXYNP + this.aoLightValueScratchYZPP + this.aoLightValueYPos) / 4.0F;
                var9 = (this.aoLightValueScratchYZPP + this.aoLightValueYPos + this.aoLightValueScratchXYZPPP + this.aoLightValueScratchXYPP) / 4.0F;
                var10 = (this.aoLightValueYPos + this.aoLightValueScratchYZPN + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPN) / 4.0F;
                var11 = (this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPN + this.aoLightValueYPos + this.aoLightValueScratchYZPN) / 4.0F;
                this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNPP, this.aoBrightnessXYNP, this.aoBrightnessYZPP, var24);
                this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXYZPPP, this.aoBrightnessXYPP, var24);
                this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXYPP, this.aoBrightnessXYZPPN, var24);
                this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYNP, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, var24);
            }
            else
            {
                var12 = this.aoLightValueYPos;
                var11 = this.aoLightValueYPos;
                var10 = this.aoLightValueYPos;
                var9 = this.aoLightValueYPos;
                this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = var24;
            }

            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = var14 ? par5 : 1.0F;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = var14 ? par6 : 1.0F;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = var14 ? par7 : 1.0F;
            this.colorRedTopLeft *= var9;
            this.colorGreenTopLeft *= var9;
            this.colorBlueTopLeft *= var9;
            this.colorRedBottomLeft *= var10;
            this.colorGreenBottomLeft *= var10;
            this.colorBlueBottomLeft *= var10;
            this.colorRedBottomRight *= var11;
            this.colorGreenBottomRight *= var11;
            this.colorBlueBottomRight *= var11;
            this.colorRedTopRight *= var12;
            this.colorGreenTopRight *= var12;
            this.colorBlueTopRight *= var12;
            this.renderTopFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 1));
            var8 = true;
        }

        int var27;

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

                var9 = (this.aoLightValueScratchXZNN + this.aoLightValueScratchXYZNPN + this.aoLightValueZNeg + this.aoLightValueScratchYZPN) / 4.0F;
                var10 = (this.aoLightValueZNeg + this.aoLightValueScratchYZPN + this.aoLightValueScratchXZPN + this.aoLightValueScratchXYZPPN) / 4.0F;
                var11 = (this.aoLightValueScratchYZNN + this.aoLightValueZNeg + this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXZPN) / 4.0F;
                var12 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXZNN + this.aoLightValueScratchYZNN + this.aoLightValueZNeg) / 4.0F;
                this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, var22);
                this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, var22);
                this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYZPNN, this.aoBrightnessXZPN, var22);
                this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXZNN, this.aoBrightnessYZNN, var22);
            }
            else
            {
                var12 = this.aoLightValueZNeg;
                var11 = this.aoLightValueZNeg;
                var10 = this.aoLightValueZNeg;
                var9 = this.aoLightValueZNeg;
                this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = var22;
            }

            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var15 ? par5 : 1.0F) * 0.8F;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var15 ? par6 : 1.0F) * 0.8F;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var15 ? par7 : 1.0F) * 0.8F;
            this.colorRedTopLeft *= var9;
            this.colorGreenTopLeft *= var9;
            this.colorBlueTopLeft *= var9;
            this.colorRedBottomLeft *= var10;
            this.colorGreenBottomLeft *= var10;
            this.colorBlueBottomLeft *= var10;
            this.colorRedBottomRight *= var11;
            this.colorGreenBottomRight *= var11;
            this.colorBlueBottomRight *= var11;
            this.colorRedTopRight *= var12;
            this.colorGreenTopRight *= var12;
            this.colorBlueTopRight *= var12;
            var27 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 2);
            this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, var27);

            if (Tessellator.instance.defaultTexture && fancyGrass && var27 == 3 && this.overrideBlockTexture < 0)
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
                this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
            }

            var8 = true;
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

                var9 = (this.aoLightValueScratchXZNP + this.aoLightValueScratchXYZNPP + this.aoLightValueZPos + this.aoLightValueScratchYZPP) / 4.0F;
                var12 = (this.aoLightValueZPos + this.aoLightValueScratchYZPP + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYZPPP) / 4.0F;
                var11 = (this.aoLightValueScratchYZNP + this.aoLightValueZPos + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXZPP) / 4.0F;
                var10 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXZNP + this.aoLightValueScratchYZNP + this.aoLightValueZPos) / 4.0F;
                this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYZNPP, this.aoBrightnessYZPP, var25);
                this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXZPP, this.aoBrightnessXYZPPP, var25);
                this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, var25);
                this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, this.aoBrightnessYZNP, var25);
            }
            else
            {
                var12 = this.aoLightValueZPos;
                var11 = this.aoLightValueZPos;
                var10 = this.aoLightValueZPos;
                var9 = this.aoLightValueZPos;
                this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = var25;
            }

            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var16 ? par5 : 1.0F) * 0.8F;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var16 ? par6 : 1.0F) * 0.8F;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var16 ? par7 : 1.0F) * 0.8F;
            this.colorRedTopLeft *= var9;
            this.colorGreenTopLeft *= var9;
            this.colorBlueTopLeft *= var9;
            this.colorRedBottomLeft *= var10;
            this.colorGreenBottomLeft *= var10;
            this.colorBlueBottomLeft *= var10;
            this.colorRedBottomRight *= var11;
            this.colorGreenBottomRight *= var11;
            this.colorBlueBottomRight *= var11;
            this.colorRedTopRight *= var12;
            this.colorGreenTopRight *= var12;
            this.colorBlueTopRight *= var12;
            var27 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 3);
            this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 3));

            if (Tessellator.instance.defaultTexture && fancyGrass && var27 == 3 && this.overrideBlockTexture < 0)
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
                this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
            }

            var8 = true;
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

                var12 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNP + this.aoLightValueXNeg + this.aoLightValueScratchXZNP) / 4.0F;
                var9 = (this.aoLightValueXNeg + this.aoLightValueScratchXZNP + this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPP) / 4.0F;
                var10 = (this.aoLightValueScratchXZNN + this.aoLightValueXNeg + this.aoLightValueScratchXYZNPN + this.aoLightValueScratchXYNP) / 4.0F;
                var11 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXYNN + this.aoLightValueScratchXZNN + this.aoLightValueXNeg) / 4.0F;
                this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, var20);
                this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYNP, this.aoBrightnessXYZNPP, var20);
                this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessXYNP, var20);
                this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXYNN, this.aoBrightnessXZNN, var20);
            }
            else
            {
                var12 = this.aoLightValueXNeg;
                var11 = this.aoLightValueXNeg;
                var10 = this.aoLightValueXNeg;
                var9 = this.aoLightValueXNeg;
                this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = var20;
            }

            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var17 ? par5 : 1.0F) * 0.6F;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var17 ? par6 : 1.0F) * 0.6F;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var17 ? par7 : 1.0F) * 0.6F;
            this.colorRedTopLeft *= var9;
            this.colorGreenTopLeft *= var9;
            this.colorBlueTopLeft *= var9;
            this.colorRedBottomLeft *= var10;
            this.colorGreenBottomLeft *= var10;
            this.colorBlueBottomLeft *= var10;
            this.colorRedBottomRight *= var11;
            this.colorGreenBottomRight *= var11;
            this.colorBlueBottomRight *= var11;
            this.colorRedTopRight *= var12;
            this.colorGreenTopRight *= var12;
            this.colorBlueTopRight *= var12;
            var27 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 4);
            this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, var27);

            if (Tessellator.instance.defaultTexture && fancyGrass && var27 == 3 && this.overrideBlockTexture < 0)
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
                this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
            }

            var8 = true;
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

                var9 = (this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNP + this.aoLightValueXPos + this.aoLightValueScratchXZPP) / 4.0F;
                var12 = (this.aoLightValueXPos + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPP) / 4.0F;
                var11 = (this.aoLightValueScratchXZPN + this.aoLightValueXPos + this.aoLightValueScratchXYZPPN + this.aoLightValueScratchXYPP) / 4.0F;
                var10 = (this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXZPN + this.aoLightValueXPos) / 4.0F;
                this.brightnessTopLeft = this.getAoBrightness(this.aoBrightnessXYPN, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, var23);
                this.brightnessTopRight = this.getAoBrightness(this.aoBrightnessXZPP, this.aoBrightnessXYPP, this.aoBrightnessXYZPPP, var23);
                this.brightnessBottomRight = this.getAoBrightness(this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, this.aoBrightnessXYPP, var23);
                this.brightnessBottomLeft = this.getAoBrightness(this.aoBrightnessXYZPNN, this.aoBrightnessXYPN, this.aoBrightnessXZPN, var23);
            }
            else
            {
                var12 = this.aoLightValueXPos;
                var11 = this.aoLightValueXPos;
                var10 = this.aoLightValueXPos;
                var9 = this.aoLightValueXPos;
                this.brightnessTopLeft = this.brightnessBottomLeft = this.brightnessBottomRight = this.brightnessTopRight = var23;
            }

            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = (var18 ? par5 : 1.0F) * 0.6F;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = (var18 ? par6 : 1.0F) * 0.6F;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = (var18 ? par7 : 1.0F) * 0.6F;
            this.colorRedTopLeft *= var9;
            this.colorGreenTopLeft *= var9;
            this.colorBlueTopLeft *= var9;
            this.colorRedBottomLeft *= var10;
            this.colorGreenBottomLeft *= var10;
            this.colorBlueBottomLeft *= var10;
            this.colorRedBottomRight *= var11;
            this.colorGreenBottomRight *= var11;
            this.colorBlueBottomRight *= var11;
            this.colorRedTopRight *= var12;
            this.colorGreenTopRight *= var12;
            this.colorBlueTopRight *= var12;
            var27 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 5);
            this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, var27);

            if (Tessellator.instance.defaultTexture && fancyGrass && var27 == 3 && this.overrideBlockTexture < 0)
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
                this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
            }

            var8 = true;
        }

        this.enableAO = false;
        return var8;
    }

	@Override
    public boolean renderStandardBlockWithColorMultiplier(Block par1Block, int par2, int par3, int par4, float par5, float par6, float par7)
    {
        this.enableAO = false;
        Tessellator var8 = Tessellator.instance;
        boolean var9 = false;
        float var10 = 0.5F;
        float var11 = 1.0F;
        float var12 = 0.8F;
        float var13 = 0.6F;
        float var14 = var11 * par5;
        float var15 = var11 * par6;
        float var16 = var11 * par7;
        float var17 = var10;
        float var18 = var12;
        float var19 = var13;
        float var20 = var10;
        float var21 = var12;
        float var22 = var13;
        float var23 = var10;
        float var24 = var12;
        float var25 = var13;

        if (par1Block != Block.grass)
        {
            var17 = var10 * par5;
            var18 = var12 * par5;
            var19 = var13 * par5;
            var20 = var10 * par6;
            var21 = var12 * par6;
            var22 = var13 * par6;
            var23 = var10 * par7;
            var24 = var12 * par7;
            var25 = var13 * par7;
        }

        int var26 = par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4);

        if (this.renderAllFaces || shouldSideBeRendered(par1Block, par2, par3 - 1, par4, 0))
        {
            var8.setBrightness(this.renderMinY > 0.0D ? var26 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 - 1, par4));
            var8.setColorOpaque_F(var17, var20, var23);
            this.renderBottomFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 0));
            var9 = true;
        }

        if (this.renderAllFaces || shouldSideBeRendered(par1Block, par2, par3 + 1, par4, 1))
        {
            var8.setBrightness(this.renderMaxY < 1.0D ? var26 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3 + 1, par4));
            var8.setColorOpaque_F(var14, var15, var16);
            this.renderTopFace(par1Block, (double)par2, (double)par3, (double)par4, par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 1));
            var9 = true;
        }

        int var28;

        if (this.renderAllFaces || shouldSideBeRendered(par1Block, par2, par3, par4 - 1, 2))
        {
            var8.setBrightness(this.renderMinZ > 0.0D ? var26 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 - 1));
            var8.setColorOpaque_F(var18, var21, var24);
            var28 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 2);
            this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, var28);

            if (Tessellator.instance.defaultTexture && fancyGrass && var28 == 3 && this.overrideBlockTexture < 0)
            {
                var8.setColorOpaque_F(var18 * par5, var21 * par6, var24 * par7);
                this.renderEastFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
            }

            var9 = true;
        }

        if (this.renderAllFaces || shouldSideBeRendered(par1Block, par2, par3, par4 + 1, 3))
        {
            var8.setBrightness(this.renderMaxZ < 1.0D ? var26 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2, par3, par4 + 1));
            var8.setColorOpaque_F(var18, var21, var24);
            var28 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 3);
            this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, var28);

            if (Tessellator.instance.defaultTexture && fancyGrass && var28 == 3 && this.overrideBlockTexture < 0)
            {
                var8.setColorOpaque_F(var18 * par5, var21 * par6, var24 * par7);
                this.renderWestFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
            }

            var9 = true;
        }

        if (this.renderAllFaces || shouldSideBeRendered(par1Block, par2 - 1, par3, par4, 4))
        {
            var8.setBrightness(this.renderMinX > 0.0D ? var26 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 - 1, par3, par4));
            var8.setColorOpaque_F(var19, var22, var25);
            var28 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 4);
            this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, var28);

            if (Tessellator.instance.defaultTexture && fancyGrass && var28 == 3 && this.overrideBlockTexture < 0)
            {
                var8.setColorOpaque_F(var19 * par5, var22 * par6, var25 * par7);
                this.renderNorthFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
            }

            var9 = true;
        }

        if (this.renderAllFaces || shouldSideBeRendered(par1Block, par2 + 1, par3, par4, 5))
        {
            var8.setBrightness(this.renderMaxX < 1.0D ? var26 : par1Block.getMixedBrightnessForBlock(this.blockAccess, par2 + 1, par3, par4));
            var8.setColorOpaque_F(var19, var22, var25);
            var28 = par1Block.getBlockTexture(this.blockAccess, par2, par3, par4, 5);
            this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, var28);

            if (Tessellator.instance.defaultTexture && fancyGrass && var28 == 3 && this.overrideBlockTexture < 0)
            {
                var8.setColorOpaque_F(var19 * par5, var22 * par6, var25 * par7);
                this.renderSouthFace(par1Block, (double)par2, (double)par3, (double)par4, 38);
            }

            var9 = true;
        }

        return var9;
    }

	public boolean shouldSideBeRendered(Block block, int x, int y, int z, int dir){
		return block.shouldSideBeRendered(blockAccess, x, y, z, dir);
	}
	
}
