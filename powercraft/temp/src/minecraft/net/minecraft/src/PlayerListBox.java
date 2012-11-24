package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.Vector;
import javax.swing.JList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.EntityPlayerMP;
import net.minecraft.src.IUpdatePlayerListBox;

@SideOnly(Side.SERVER)
public class PlayerListBox extends JList implements IUpdatePlayerListBox {

   private MinecraftServer field_79012_a;
   private int field_79011_b = 0;


   public PlayerListBox(MinecraftServer p_i4105_1_) {
      this.field_79012_a = p_i4105_1_;
      p_i4105_1_.func_82010_a(this);
   }

   public void func_73660_a() {
      if(this.field_79011_b++ % 20 == 0) {
         Vector var1 = new Vector();

         for(int var2 = 0; var2 < this.field_79012_a.func_71203_ab().field_72404_b.size(); ++var2) {
            var1.add(((EntityPlayerMP)this.field_79012_a.func_71203_ab().field_72404_b.get(var2)).field_71092_bJ);
         }

         this.setListData(var1);
      }

   }
}
