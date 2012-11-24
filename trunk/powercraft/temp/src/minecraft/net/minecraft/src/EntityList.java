package net.minecraft.src;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityArrow;
import net.minecraft.src.EntityBat;
import net.minecraft.src.EntityBlaze;
import net.minecraft.src.EntityBoat;
import net.minecraft.src.EntityCaveSpider;
import net.minecraft.src.EntityChicken;
import net.minecraft.src.EntityCow;
import net.minecraft.src.EntityCreeper;
import net.minecraft.src.EntityDragon;
import net.minecraft.src.EntityEggInfo;
import net.minecraft.src.EntityEnderCrystal;
import net.minecraft.src.EntityEnderEye;
import net.minecraft.src.EntityEnderPearl;
import net.minecraft.src.EntityEnderman;
import net.minecraft.src.EntityExpBottle;
import net.minecraft.src.EntityFallingSand;
import net.minecraft.src.EntityGhast;
import net.minecraft.src.EntityGiantZombie;
import net.minecraft.src.EntityIronGolem;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityItemFrame;
import net.minecraft.src.EntityLargeFireball;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityMagmaCube;
import net.minecraft.src.EntityMinecart;
import net.minecraft.src.EntityMob;
import net.minecraft.src.EntityMooshroom;
import net.minecraft.src.EntityOcelot;
import net.minecraft.src.EntityPainting;
import net.minecraft.src.EntityPig;
import net.minecraft.src.EntityPigZombie;
import net.minecraft.src.EntityPotion;
import net.minecraft.src.EntitySheep;
import net.minecraft.src.EntitySilverfish;
import net.minecraft.src.EntitySkeleton;
import net.minecraft.src.EntitySlime;
import net.minecraft.src.EntitySmallFireball;
import net.minecraft.src.EntitySnowball;
import net.minecraft.src.EntitySnowman;
import net.minecraft.src.EntitySpider;
import net.minecraft.src.EntitySquid;
import net.minecraft.src.EntityTNTPrimed;
import net.minecraft.src.EntityVillager;
import net.minecraft.src.EntityWitch;
import net.minecraft.src.EntityWither;
import net.minecraft.src.EntityWitherSkull;
import net.minecraft.src.EntityWolf;
import net.minecraft.src.EntityXPOrb;
import net.minecraft.src.EntityZombie;
import net.minecraft.src.NBTTagCompound;
import net.minecraft.src.World;

public class EntityList {

   public static Map field_75625_b = new HashMap();
   public static Map field_75626_c = new HashMap();
   public static Map field_75623_d = new HashMap();
   private static Map field_75624_e = new HashMap();
   private static Map field_75622_f = new HashMap();
   public static HashMap field_75627_a = new LinkedHashMap();


   public static void func_75618_a(Class p_75618_0_, String p_75618_1_, int p_75618_2_) {
      field_75625_b.put(p_75618_1_, p_75618_0_);
      field_75626_c.put(p_75618_0_, p_75618_1_);
      field_75623_d.put(Integer.valueOf(p_75618_2_), p_75618_0_);
      field_75624_e.put(p_75618_0_, Integer.valueOf(p_75618_2_));
      field_75622_f.put(p_75618_1_, Integer.valueOf(p_75618_2_));
   }

   public static void func_75614_a(Class p_75614_0_, String p_75614_1_, int p_75614_2_, int p_75614_3_, int p_75614_4_) {
      func_75618_a(p_75614_0_, p_75614_1_, p_75614_2_);
      field_75627_a.put(Integer.valueOf(p_75614_2_), new EntityEggInfo(p_75614_2_, p_75614_3_, p_75614_4_));
   }

   public static Entity func_75620_a(String p_75620_0_, World p_75620_1_) {
      Entity var2 = null;

      try {
         Class var3 = (Class)field_75625_b.get(p_75620_0_);
         if(var3 != null) {
            var2 = (Entity)var3.getConstructor(new Class[]{World.class}).newInstance(new Object[]{p_75620_1_});
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      return var2;
   }

   public static Entity func_75615_a(NBTTagCompound p_75615_0_, World p_75615_1_) {
      Entity var2 = null;

      try {
         Class var3 = (Class)field_75625_b.get(p_75615_0_.func_74779_i("id"));
         if(var3 != null) {
            var2 = (Entity)var3.getConstructor(new Class[]{World.class}).newInstance(new Object[]{p_75615_1_});
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      if(var2 != null) {
         var2.func_70020_e(p_75615_0_);
      } else {
         System.out.println("Skipping Entity with id " + p_75615_0_.func_74779_i("id"));
      }

      return var2;
   }

   public static Entity func_75616_a(int p_75616_0_, World p_75616_1_) {
      Entity var2 = null;

      try {
         Class var3 = (Class)field_75623_d.get(Integer.valueOf(p_75616_0_));
         if(var3 != null) {
            var2 = (Entity)var3.getConstructor(new Class[]{World.class}).newInstance(new Object[]{p_75616_1_});
         }
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      if(var2 == null) {
         System.out.println("Skipping Entity with id " + p_75616_0_);
      }

      return var2;
   }

   public static int func_75619_a(Entity p_75619_0_) {
      Class var1 = p_75619_0_.getClass();
      return field_75624_e.containsKey(var1)?((Integer)field_75624_e.get(var1)).intValue():0;
   }

   public static Class func_90035_a(int p_90035_0_) {
      return (Class)field_75623_d.get(Integer.valueOf(p_90035_0_));
   }

   public static String func_75621_b(Entity p_75621_0_) {
      return (String)field_75626_c.get(p_75621_0_.getClass());
   }

   public static String func_75617_a(int p_75617_0_) {
      Class var1 = (Class)field_75623_d.get(Integer.valueOf(p_75617_0_));
      return var1 != null?(String)field_75626_c.get(var1):null;
   }

   static {
      func_75618_a(EntityItem.class, "Item", 1);
      func_75618_a(EntityXPOrb.class, "XPOrb", 2);
      func_75618_a(EntityPainting.class, "Painting", 9);
      func_75618_a(EntityArrow.class, "Arrow", 10);
      func_75618_a(EntitySnowball.class, "Snowball", 11);
      func_75618_a(EntityLargeFireball.class, "Fireball", 12);
      func_75618_a(EntitySmallFireball.class, "SmallFireball", 13);
      func_75618_a(EntityEnderPearl.class, "ThrownEnderpearl", 14);
      func_75618_a(EntityEnderEye.class, "EyeOfEnderSignal", 15);
      func_75618_a(EntityPotion.class, "ThrownPotion", 16);
      func_75618_a(EntityExpBottle.class, "ThrownExpBottle", 17);
      func_75618_a(EntityItemFrame.class, "ItemFrame", 18);
      func_75618_a(EntityWitherSkull.class, "WitherSkull", 19);
      func_75618_a(EntityTNTPrimed.class, "PrimedTnt", 20);
      func_75618_a(EntityFallingSand.class, "FallingSand", 21);
      func_75618_a(EntityMinecart.class, "Minecart", 40);
      func_75618_a(EntityBoat.class, "Boat", 41);
      func_75618_a(EntityLiving.class, "Mob", 48);
      func_75618_a(EntityMob.class, "Monster", 49);
      func_75614_a(EntityCreeper.class, "Creeper", 50, 894731, 0);
      func_75614_a(EntitySkeleton.class, "Skeleton", 51, 12698049, 4802889);
      func_75614_a(EntitySpider.class, "Spider", 52, 3419431, 11013646);
      func_75618_a(EntityGiantZombie.class, "Giant", 53);
      func_75614_a(EntityZombie.class, "Zombie", 54, '\uafaf', 7969893);
      func_75614_a(EntitySlime.class, "Slime", 55, 5349438, 8306542);
      func_75614_a(EntityGhast.class, "Ghast", 56, 16382457, 12369084);
      func_75614_a(EntityPigZombie.class, "PigZombie", 57, 15373203, 5009705);
      func_75614_a(EntityEnderman.class, "Enderman", 58, 1447446, 0);
      func_75614_a(EntityCaveSpider.class, "CaveSpider", 59, 803406, 11013646);
      func_75614_a(EntitySilverfish.class, "Silverfish", 60, 7237230, 3158064);
      func_75614_a(EntityBlaze.class, "Blaze", 61, 16167425, 16775294);
      func_75614_a(EntityMagmaCube.class, "LavaSlime", 62, 3407872, 16579584);
      func_75618_a(EntityDragon.class, "EnderDragon", 63);
      func_75618_a(EntityWither.class, "WitherBoss", 64);
      func_75614_a(EntityBat.class, "Bat", 65, 4996656, 986895);
      func_75614_a(EntityWitch.class, "Witch", 66, 3407872, 5349438);
      func_75614_a(EntityPig.class, "Pig", 90, 15771042, 14377823);
      func_75614_a(EntitySheep.class, "Sheep", 91, 15198183, 16758197);
      func_75614_a(EntityCow.class, "Cow", 92, 4470310, 10592673);
      func_75614_a(EntityChicken.class, "Chicken", 93, 10592673, 16711680);
      func_75614_a(EntitySquid.class, "Squid", 94, 2243405, 7375001);
      func_75614_a(EntityWolf.class, "Wolf", 95, 14144467, 13545366);
      func_75614_a(EntityMooshroom.class, "MushroomCow", 96, 10489616, 12040119);
      func_75618_a(EntitySnowman.class, "SnowMan", 97);
      func_75614_a(EntityOcelot.class, "Ozelot", 98, 15720061, 5653556);
      func_75618_a(EntityIronGolem.class, "VillagerGolem", 99);
      func_75614_a(EntityVillager.class, "Villager", 120, 5651507, 12422002);
      func_75618_a(EntityEnderCrystal.class, "EnderCrystal", 200);
   }
}
