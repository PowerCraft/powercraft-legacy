package argo.format;

import argo.jdom.JsonRootNode;
import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;

@SideOnly(Side.CLIENT)
public interface JsonFormatter {

   String func_74785_a(JsonRootNode var1);
}
