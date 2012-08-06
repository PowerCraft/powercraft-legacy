package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ComponentNetherBridgeStartPiece extends ComponentNetherBridgeCrossing3
{
    public StructureNetherBridgePieceWeight field_74970_a;
    public List field_74968_b = new ArrayList();
    public List field_74969_c;
    public ArrayList field_74967_d = new ArrayList();

    public ComponentNetherBridgeStartPiece(Random par1Random, int par2, int par3)
    {
        super(par1Random, par2, par3);
        StructureNetherBridgePieceWeight[] var4 = StructureNetherBridgePieces.getPrimaryComponents();
        int var5 = var4.length;
        int var6;
        StructureNetherBridgePieceWeight var7;

        for (var6 = 0; var6 < var5; ++var6)
        {
            var7 = var4[var6];
            var7.field_78827_c = 0;
            this.field_74968_b.add(var7);
        }

        this.field_74969_c = new ArrayList();
        var4 = StructureNetherBridgePieces.getSecondaryComponents();
        var5 = var4.length;

        for (var6 = 0; var6 < var5; ++var6)
        {
            var7 = var4[var6];
            var7.field_78827_c = 0;
            this.field_74969_c.add(var7);
        }
    }
}
