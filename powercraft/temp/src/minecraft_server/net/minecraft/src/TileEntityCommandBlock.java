package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import net.minecraft.server.MinecraftServer;
import net.minecraft.src.ChunkCoordinates;
import net.minecraft.src.ICommandManager;
import net.minecraft.src.ICommandSender;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.Packet;
import net.minecraft.src.Packet132TileEntityData;
import net.minecraft.src.TileEntity;
import net.minecraft.src.World;

public class TileEntityCommandBlock extends TileEntity implements ICommandSender {

   private String field_82354_a = "";


   public void func_82352_b(String p_82352_1_) {
      this.field_82354_a = p_82352_1_;
      this.func_70296_d();
   }

   @SideOnly(Side.CLIENT)
   public String func_82353_c() {
      return this.field_82354_a;
   }

   public void func_82351_a(World p_82351_1_) {
      if(!p_82351_1_.field_72995_K) {
         MinecraftServer var2 = MinecraftServer.func_71276_C();
         if(var2 != null && var2.func_82356_Z()) {
            ICommandManager var3 = var2.func_71187_D();
            var3.func_71556_a(this, this.field_82354_a);
         }

      }
   }

   public String func_70005_c_() {
      return "@";
   }

   public void func_70006_a(String p_70006_1_) {}

   public boolean func_70003_b(int p_70003_1_, String p_70003_2_) {
      return p_70003_1_ <= 2;
   }

   public String func_70004_a(String p_70004_1_, Object ... p_70004_2_) {
      return p_70004_1_;
   }

   public void func_70310_b(NBTTagCompound p_70310_1_) {
      super.func_70310_b(p_70310_1_);
      p_70310_1_.func_74778_a("Command", this.field_82354_a);
   }

   public void func_70307_a(NBTTagCompound p_70307_1_) {
      super.func_70307_a(p_70307_1_);
      this.field_82354_a = p_70307_1_.func_74779_i("Command");
   }

   public ChunkCoordinates func_82114_b() {
      return new ChunkCoordinates(this.field_70329_l, this.field_70330_m, this.field_70327_n);
   }

   public Packet func_70319_e() {
      NBTTagCompound var1 = new NBTTagCompound();
      this.func_70310_b(var1);
      return new Packet132TileEntityData(this.field_70329_l, this.field_70330_m, this.field_70327_n, 2, var1);
   }
}
