package net.minecraft.src;

import cpw.mods.fml.common.Side;
import cpw.mods.fml.common.asm.SideOnly;
import java.util.List;
import java.util.Random;
import net.minecraft.src.AxisAlignedBB;
import net.minecraft.src.BlockAnvil;
import net.minecraft.src.BlockBeacon;
import net.minecraft.src.BlockBed;
import net.minecraft.src.BlockBookshelf;
import net.minecraft.src.BlockBrewingStand;
import net.minecraft.src.BlockButton;
import net.minecraft.src.BlockCactus;
import net.minecraft.src.BlockCake;
import net.minecraft.src.BlockCarrot;
import net.minecraft.src.BlockCauldron;
import net.minecraft.src.BlockChest;
import net.minecraft.src.BlockClay;
import net.minecraft.src.BlockCloth;
import net.minecraft.src.BlockCocoa;
import net.minecraft.src.BlockCommandBlock;
import net.minecraft.src.BlockCrops;
import net.minecraft.src.BlockDeadBush;
import net.minecraft.src.BlockDetectorRail;
import net.minecraft.src.BlockDirt;
import net.minecraft.src.BlockDispenser;
import net.minecraft.src.BlockDoor;
import net.minecraft.src.BlockDragonEgg;
import net.minecraft.src.BlockEnchantmentTable;
import net.minecraft.src.BlockEndPortal;
import net.minecraft.src.BlockEndPortalFrame;
import net.minecraft.src.BlockEnderChest;
import net.minecraft.src.BlockFarmland;
import net.minecraft.src.BlockFence;
import net.minecraft.src.BlockFenceGate;
import net.minecraft.src.BlockFire;
import net.minecraft.src.BlockFlower;
import net.minecraft.src.BlockFlowerPot;
import net.minecraft.src.BlockFlowing;
import net.minecraft.src.BlockFurnace;
import net.minecraft.src.BlockGlass;
import net.minecraft.src.BlockGlowStone;
import net.minecraft.src.BlockGrass;
import net.minecraft.src.BlockGravel;
import net.minecraft.src.BlockHalfSlab;
import net.minecraft.src.BlockIce;
import net.minecraft.src.BlockJukeBox;
import net.minecraft.src.BlockLadder;
import net.minecraft.src.BlockLeaves;
import net.minecraft.src.BlockLever;
import net.minecraft.src.BlockLilyPad;
import net.minecraft.src.BlockLockedChest;
import net.minecraft.src.BlockLog;
import net.minecraft.src.BlockMelon;
import net.minecraft.src.BlockMobSpawner;
import net.minecraft.src.BlockMushroom;
import net.minecraft.src.BlockMushroomCap;
import net.minecraft.src.BlockMycelium;
import net.minecraft.src.BlockNetherStalk;
import net.minecraft.src.BlockNetherrack;
import net.minecraft.src.BlockNote;
import net.minecraft.src.BlockObsidian;
import net.minecraft.src.BlockOre;
import net.minecraft.src.BlockOreStorage;
import net.minecraft.src.BlockPane;
import net.minecraft.src.BlockPistonBase;
import net.minecraft.src.BlockPistonExtension;
import net.minecraft.src.BlockPistonMoving;
import net.minecraft.src.BlockPortal;
import net.minecraft.src.BlockPotato;
import net.minecraft.src.BlockPressurePlate;
import net.minecraft.src.BlockPumpkin;
import net.minecraft.src.BlockRail;
import net.minecraft.src.BlockRedstoneLight;
import net.minecraft.src.BlockRedstoneOre;
import net.minecraft.src.BlockRedstoneRepeater;
import net.minecraft.src.BlockRedstoneTorch;
import net.minecraft.src.BlockRedstoneWire;
import net.minecraft.src.BlockReed;
import net.minecraft.src.BlockSand;
import net.minecraft.src.BlockSandStone;
import net.minecraft.src.BlockSapling;
import net.minecraft.src.BlockSign;
import net.minecraft.src.BlockSilverfish;
import net.minecraft.src.BlockSkull;
import net.minecraft.src.BlockSnow;
import net.minecraft.src.BlockSnowBlock;
import net.minecraft.src.BlockSoulSand;
import net.minecraft.src.BlockSponge;
import net.minecraft.src.BlockStairs;
import net.minecraft.src.BlockStationary;
import net.minecraft.src.BlockStem;
import net.minecraft.src.BlockStep;
import net.minecraft.src.BlockStone;
import net.minecraft.src.BlockStoneBrick;
import net.minecraft.src.BlockTNT;
import net.minecraft.src.BlockTallGrass;
import net.minecraft.src.BlockTorch;
import net.minecraft.src.BlockTrapDoor;
import net.minecraft.src.BlockTripWire;
import net.minecraft.src.BlockTripWireSource;
import net.minecraft.src.BlockVine;
import net.minecraft.src.BlockWall;
import net.minecraft.src.BlockWeb;
import net.minecraft.src.BlockWood;
import net.minecraft.src.BlockWoodSlab;
import net.minecraft.src.BlockWorkbench;
import net.minecraft.src.CreativeTabs;
import net.minecraft.src.EnchantmentHelper;
import net.minecraft.src.Entity;
import net.minecraft.src.EntityItem;
import net.minecraft.src.EntityLiving;
import net.minecraft.src.EntityPlayer;
import net.minecraft.src.EntityXPOrb;
import net.minecraft.src.EnumMobType;
import net.minecraft.src.Explosion;
import net.minecraft.src.IBlockAccess;
import net.minecraft.src.Item;
import net.minecraft.src.ItemAnvilBlock;
import net.minecraft.src.ItemBlock;
import net.minecraft.src.ItemCloth;
import net.minecraft.src.ItemColored;
import net.minecraft.src.ItemLeaves;
import net.minecraft.src.ItemLilyPad;
import net.minecraft.src.ItemMultiTextureTile;
import net.minecraft.src.ItemPiston;
import net.minecraft.src.ItemSlab;
import net.minecraft.src.ItemStack;
import net.minecraft.src.Material;
import net.minecraft.src.MovingObjectPosition;
import net.minecraft.src.StatCollector;
import net.minecraft.src.StatList;
import net.minecraft.src.StepSound;
import net.minecraft.src.StepSoundAnvil;
import net.minecraft.src.StepSoundSand;
import net.minecraft.src.StepSoundStone;
import net.minecraft.src.TileEntitySign;
import net.minecraft.src.Vec3;
import net.minecraft.src.World;

public class Block {

   private CreativeTabs field_71969_a;
   public static final StepSound field_71966_d = new StepSound("stone", 1.0F, 1.0F);
   public static final StepSound field_71967_e = new StepSound("wood", 1.0F, 1.0F);
   public static final StepSound field_71964_f = new StepSound("gravel", 1.0F, 1.0F);
   public static final StepSound field_71965_g = new StepSound("grass", 1.0F, 1.0F);
   public static final StepSound field_71976_h = new StepSound("stone", 1.0F, 1.0F);
   public static final StepSound field_71977_i = new StepSound("stone", 1.0F, 1.5F);
   public static final StepSound field_71974_j = new StepSoundStone("stone", 1.0F, 1.0F);
   public static final StepSound field_71975_k = new StepSound("cloth", 1.0F, 1.0F);
   public static final StepSound field_71972_l = new StepSound("sand", 1.0F, 1.0F);
   public static final StepSound field_82509_m = new StepSound("snow", 1.0F, 1.0F);
   public static final StepSound field_82507_n = new StepSoundSand("ladder", 1.0F, 1.0F);
   public static final StepSound field_82508_o = new StepSoundAnvil("anvil", 0.3F, 1.0F);
   public static final Block[] field_71973_m = new Block[4096];
   public static final boolean[] field_71970_n = new boolean[4096];
   public static final int[] field_71971_o = new int[4096];
   public static final boolean[] field_71985_p = new boolean[4096];
   public static final int[] field_71984_q = new int[4096];
   public static final boolean[] field_71983_r = new boolean[4096];
   public static boolean[] field_71982_s = new boolean[4096];
   public static final Block field_71981_t = (new BlockStone(1, 1)).func_71848_c(1.5F).func_71894_b(10.0F).func_71884_a(field_71976_h).func_71864_b("stone");
   public static final BlockGrass field_71980_u = (BlockGrass)(new BlockGrass(2)).func_71848_c(0.6F).func_71884_a(field_71965_g).func_71864_b("grass");
   public static final Block field_71979_v = (new BlockDirt(3, 2)).func_71848_c(0.5F).func_71884_a(field_71964_f).func_71864_b("dirt");
   public static final Block field_71978_w = (new Block(4, 16, Material.field_76246_e)).func_71848_c(2.0F).func_71894_b(10.0F).func_71884_a(field_71976_h).func_71864_b("stonebrick").func_71849_a(CreativeTabs.field_78030_b);
   public static final Block field_71988_x = (new BlockWood(5)).func_71848_c(2.0F).func_71894_b(5.0F).func_71884_a(field_71967_e).func_71864_b("wood").func_71912_p();
   public static final Block field_71987_y = (new BlockSapling(6, 15)).func_71848_c(0.0F).func_71884_a(field_71965_g).func_71864_b("sapling").func_71912_p();
   public static final Block field_71986_z = (new Block(7, 17, Material.field_76246_e)).func_71875_q().func_71894_b(6000000.0F).func_71884_a(field_71976_h).func_71864_b("bedrock").func_71896_v().func_71849_a(CreativeTabs.field_78030_b);
   public static final Block field_71942_A = (new BlockFlowing(8, Material.field_76244_g)).func_71848_c(100.0F).func_71868_h(3).func_71864_b("water").func_71896_v().func_71912_p();
   public static final Block field_71943_B = (new BlockStationary(9, Material.field_76244_g)).func_71848_c(100.0F).func_71868_h(3).func_71864_b("water").func_71896_v().func_71912_p();
   public static final Block field_71944_C = (new BlockFlowing(10, Material.field_76256_h)).func_71848_c(0.0F).func_71900_a(1.0F).func_71868_h(255).func_71864_b("lava").func_71896_v().func_71912_p();
   public static final Block field_71938_D = (new BlockStationary(11, Material.field_76256_h)).func_71848_c(100.0F).func_71900_a(1.0F).func_71868_h(255).func_71864_b("lava").func_71896_v().func_71912_p();
   public static final Block field_71939_E = (new BlockSand(12, 18)).func_71848_c(0.5F).func_71884_a(field_71972_l).func_71864_b("sand");
   public static final Block field_71940_F = (new BlockGravel(13, 19)).func_71848_c(0.6F).func_71884_a(field_71964_f).func_71864_b("gravel");
   public static final Block field_71941_G = (new BlockOre(14, 32)).func_71848_c(3.0F).func_71894_b(5.0F).func_71884_a(field_71976_h).func_71864_b("oreGold");
   public static final Block field_71949_H = (new BlockOre(15, 33)).func_71848_c(3.0F).func_71894_b(5.0F).func_71884_a(field_71976_h).func_71864_b("oreIron");
   public static final Block field_71950_I = (new BlockOre(16, 34)).func_71848_c(3.0F).func_71894_b(5.0F).func_71884_a(field_71976_h).func_71864_b("oreCoal");
   public static final Block field_71951_J = (new BlockLog(17)).func_71848_c(2.0F).func_71884_a(field_71967_e).func_71864_b("log").func_71912_p();
   public static final BlockLeaves field_71952_K = (BlockLeaves)(new BlockLeaves(18, 52)).func_71848_c(0.2F).func_71868_h(1).func_71884_a(field_71965_g).func_71864_b("leaves").func_71912_p();
   public static final Block field_71945_L = (new BlockSponge(19)).func_71848_c(0.6F).func_71884_a(field_71965_g).func_71864_b("sponge");
   public static final Block field_71946_M = (new BlockGlass(20, 49, Material.field_76264_q, false)).func_71848_c(0.3F).func_71884_a(field_71974_j).func_71864_b("glass");
   public static final Block field_71947_N = (new BlockOre(21, 160)).func_71848_c(3.0F).func_71894_b(5.0F).func_71884_a(field_71976_h).func_71864_b("oreLapis");
   public static final Block field_71948_O = (new Block(22, 144, Material.field_76246_e)).func_71848_c(3.0F).func_71894_b(5.0F).func_71884_a(field_71976_h).func_71864_b("blockLapis").func_71849_a(CreativeTabs.field_78030_b);
   public static final Block field_71958_P = (new BlockDispenser(23)).func_71848_c(3.5F).func_71884_a(field_71976_h).func_71864_b("dispenser").func_71912_p();
   public static final Block field_71957_Q = (new BlockSandStone(24)).func_71884_a(field_71976_h).func_71848_c(0.8F).func_71864_b("sandStone").func_71912_p();
   public static final Block field_71960_R = (new BlockNote(25)).func_71848_c(0.8F).func_71864_b("musicBlock").func_71912_p();
   public static final Block field_71959_S = (new BlockBed(26)).func_71848_c(0.2F).func_71864_b("bed").func_71896_v().func_71912_p();
   public static final Block field_71954_T = (new BlockRail(27, 179, true)).func_71848_c(0.7F).func_71884_a(field_71977_i).func_71864_b("goldenRail").func_71912_p();
   public static final Block field_71953_U = (new BlockDetectorRail(28, 195)).func_71848_c(0.7F).func_71884_a(field_71977_i).func_71864_b("detectorRail").func_71912_p();
   public static final Block field_71956_V = (new BlockPistonBase(29, 106, true)).func_71864_b("pistonStickyBase").func_71912_p();
   public static final Block field_71955_W = (new BlockWeb(30, 11)).func_71868_h(1).func_71848_c(4.0F).func_71864_b("web");
   public static final BlockTallGrass field_71962_X = (BlockTallGrass)(new BlockTallGrass(31, 39)).func_71848_c(0.0F).func_71884_a(field_71965_g).func_71864_b("tallgrass");
   public static final BlockDeadBush field_71961_Y = (BlockDeadBush)(new BlockDeadBush(32, 55)).func_71848_c(0.0F).func_71884_a(field_71965_g).func_71864_b("deadbush");
   public static final Block field_71963_Z = (new BlockPistonBase(33, 107, false)).func_71864_b("pistonBase").func_71912_p();
   public static final BlockPistonExtension field_72099_aa = (BlockPistonExtension)(new BlockPistonExtension(34, 107)).func_71912_p();
   public static final Block field_72101_ab = (new BlockCloth()).func_71848_c(0.8F).func_71884_a(field_71975_k).func_71864_b("cloth").func_71912_p();
   public static final BlockPistonMoving field_72095_ac = new BlockPistonMoving(36);
   public static final BlockFlower field_72097_ad = (BlockFlower)(new BlockFlower(37, 13)).func_71848_c(0.0F).func_71884_a(field_71965_g).func_71864_b("flower");
   public static final BlockFlower field_72107_ae = (BlockFlower)(new BlockFlower(38, 12)).func_71848_c(0.0F).func_71884_a(field_71965_g).func_71864_b("rose");
   public static final BlockFlower field_72109_af = (BlockFlower)(new BlockMushroom(39, 29)).func_71848_c(0.0F).func_71884_a(field_71965_g).func_71900_a(0.125F).func_71864_b("mushroom");
   public static final BlockFlower field_72103_ag = (BlockFlower)(new BlockMushroom(40, 28)).func_71848_c(0.0F).func_71884_a(field_71965_g).func_71864_b("mushroom");
   public static final Block field_72105_ah = (new BlockOreStorage(41, 23)).func_71848_c(3.0F).func_71894_b(10.0F).func_71884_a(field_71977_i).func_71864_b("blockGold");
   public static final Block field_72083_ai = (new BlockOreStorage(42, 22)).func_71848_c(5.0F).func_71894_b(10.0F).func_71884_a(field_71977_i).func_71864_b("blockIron");
   public static final BlockHalfSlab field_72085_aj = (BlockHalfSlab)(new BlockStep(43, true)).func_71848_c(2.0F).func_71894_b(10.0F).func_71884_a(field_71976_h).func_71864_b("stoneSlab");
   public static final BlockHalfSlab field_72079_ak = (BlockHalfSlab)(new BlockStep(44, false)).func_71848_c(2.0F).func_71894_b(10.0F).func_71884_a(field_71976_h).func_71864_b("stoneSlab");
   public static final Block field_72081_al = (new Block(45, 7, Material.field_76246_e)).func_71848_c(2.0F).func_71894_b(10.0F).func_71884_a(field_71976_h).func_71864_b("brick").func_71849_a(CreativeTabs.field_78030_b);
   public static final Block field_72091_am = (new BlockTNT(46, 8)).func_71848_c(0.0F).func_71884_a(field_71965_g).func_71864_b("tnt");
   public static final Block field_72093_an = (new BlockBookshelf(47, 35)).func_71848_c(1.5F).func_71884_a(field_71967_e).func_71864_b("bookshelf");
   public static final Block field_72087_ao = (new Block(48, 36, Material.field_76246_e)).func_71848_c(2.0F).func_71894_b(10.0F).func_71884_a(field_71976_h).func_71864_b("stoneMoss").func_71849_a(CreativeTabs.field_78030_b);
   public static final Block field_72089_ap = (new BlockObsidian(49, 37)).func_71848_c(50.0F).func_71894_b(2000.0F).func_71884_a(field_71976_h).func_71864_b("obsidian");
   public static final Block field_72069_aq = (new BlockTorch(50, 80)).func_71848_c(0.0F).func_71900_a(0.9375F).func_71884_a(field_71967_e).func_71864_b("torch").func_71912_p();
   public static final BlockFire field_72067_ar = (BlockFire)(new BlockFire(51, 31)).func_71848_c(0.0F).func_71900_a(1.0F).func_71884_a(field_71967_e).func_71864_b("fire").func_71896_v();
   public static final Block field_72065_as = (new BlockMobSpawner(52, 65)).func_71848_c(5.0F).func_71884_a(field_71977_i).func_71864_b("mobSpawner").func_71896_v();
   public static final Block field_72063_at = (new BlockStairs(53, field_71988_x, 0)).func_71864_b("stairsWood").func_71912_p();
   public static final Block field_72077_au = (new BlockChest(54)).func_71848_c(2.5F).func_71884_a(field_71967_e).func_71864_b("chest").func_71912_p();
   public static final Block field_72075_av = (new BlockRedstoneWire(55, 164)).func_71848_c(0.0F).func_71884_a(field_71966_d).func_71864_b("redstoneDust").func_71896_v().func_71912_p();
   public static final Block field_72073_aw = (new BlockOre(56, 50)).func_71848_c(3.0F).func_71894_b(5.0F).func_71884_a(field_71976_h).func_71864_b("oreDiamond");
   public static final Block field_72071_ax = (new BlockOreStorage(57, 24)).func_71848_c(5.0F).func_71894_b(10.0F).func_71884_a(field_71977_i).func_71864_b("blockDiamond");
   public static final Block field_72060_ay = (new BlockWorkbench(58)).func_71848_c(2.5F).func_71884_a(field_71967_e).func_71864_b("workbench");
   public static final Block field_72058_az = (new BlockCrops(59, 88)).func_71864_b("crops");
   public static final Block field_72050_aA = (new BlockFarmland(60)).func_71848_c(0.6F).func_71884_a(field_71964_f).func_71864_b("farmland").func_71912_p();
   public static final Block field_72051_aB = (new BlockFurnace(61, false)).func_71848_c(3.5F).func_71884_a(field_71976_h).func_71864_b("furnace").func_71912_p().func_71849_a(CreativeTabs.field_78031_c);
   public static final Block field_72052_aC = (new BlockFurnace(62, true)).func_71848_c(3.5F).func_71884_a(field_71976_h).func_71900_a(0.875F).func_71864_b("furnace").func_71912_p();
   public static final Block field_72053_aD = (new BlockSign(63, TileEntitySign.class, true)).func_71848_c(1.0F).func_71884_a(field_71967_e).func_71864_b("sign").func_71896_v().func_71912_p();
   public static final Block field_72054_aE = (new BlockDoor(64, Material.field_76245_d)).func_71848_c(3.0F).func_71884_a(field_71967_e).func_71864_b("doorWood").func_71896_v().func_71912_p();
   public static final Block field_72055_aF = (new BlockLadder(65, 83)).func_71848_c(0.4F).func_71884_a(field_82507_n).func_71864_b("ladder").func_71912_p();
   public static final Block field_72056_aG = (new BlockRail(66, 128, false)).func_71848_c(0.7F).func_71884_a(field_71977_i).func_71864_b("rail").func_71912_p();
   public static final Block field_72057_aH = (new BlockStairs(67, field_71978_w, 0)).func_71864_b("stairsStone").func_71912_p();
   public static final Block field_72042_aI = (new BlockSign(68, TileEntitySign.class, false)).func_71848_c(1.0F).func_71884_a(field_71967_e).func_71864_b("sign").func_71896_v().func_71912_p();
   public static final Block field_72043_aJ = (new BlockLever(69, 96)).func_71848_c(0.5F).func_71884_a(field_71967_e).func_71864_b("lever").func_71912_p();
   public static final Block field_72044_aK = (new BlockPressurePlate(70, field_71981_t.field_72059_bZ, EnumMobType.mobs, Material.field_76246_e)).func_71848_c(0.5F).func_71884_a(field_71976_h).func_71864_b("pressurePlate").func_71912_p();
   public static final Block field_72045_aL = (new BlockDoor(71, Material.field_76243_f)).func_71848_c(5.0F).func_71884_a(field_71977_i).func_71864_b("doorIron").func_71896_v().func_71912_p();
   public static final Block field_72046_aM = (new BlockPressurePlate(72, field_71988_x.field_72059_bZ, EnumMobType.everything, Material.field_76245_d)).func_71848_c(0.5F).func_71884_a(field_71967_e).func_71864_b("pressurePlate").func_71912_p();
   public static final Block field_72047_aN = (new BlockRedstoneOre(73, 51, false)).func_71848_c(3.0F).func_71894_b(5.0F).func_71884_a(field_71976_h).func_71864_b("oreRedstone").func_71912_p().func_71849_a(CreativeTabs.field_78030_b);
   public static final Block field_72048_aO = (new BlockRedstoneOre(74, 51, true)).func_71900_a(0.625F).func_71848_c(3.0F).func_71894_b(5.0F).func_71884_a(field_71976_h).func_71864_b("oreRedstone").func_71912_p();
   public static final Block field_72049_aP = (new BlockRedstoneTorch(75, 115, false)).func_71848_c(0.0F).func_71884_a(field_71967_e).func_71864_b("notGate").func_71912_p();
   public static final Block field_72035_aQ = (new BlockRedstoneTorch(76, 99, true)).func_71848_c(0.0F).func_71900_a(0.5F).func_71884_a(field_71967_e).func_71864_b("notGate").func_71912_p().func_71849_a(CreativeTabs.field_78028_d);
   public static final Block field_72034_aR = (new BlockButton(77, field_71981_t.field_72059_bZ, false)).func_71848_c(0.5F).func_71884_a(field_71976_h).func_71864_b("button").func_71912_p();
   public static final Block field_72037_aS = (new BlockSnow(78, 66)).func_71848_c(0.1F).func_71884_a(field_82509_m).func_71864_b("snow").func_71912_p().func_71868_h(0);
   public static final Block field_72036_aT = (new BlockIce(79, 67)).func_71848_c(0.5F).func_71868_h(3).func_71884_a(field_71974_j).func_71864_b("ice");
   public static final Block field_72039_aU = (new BlockSnowBlock(80, 66)).func_71848_c(0.2F).func_71884_a(field_82509_m).func_71864_b("snow");
   public static final Block field_72038_aV = (new BlockCactus(81, 70)).func_71848_c(0.4F).func_71884_a(field_71975_k).func_71864_b("cactus");
   public static final Block field_72041_aW = (new BlockClay(82, 72)).func_71848_c(0.6F).func_71884_a(field_71964_f).func_71864_b("clay");
   public static final Block field_72040_aX = (new BlockReed(83, 73)).func_71848_c(0.0F).func_71884_a(field_71965_g).func_71864_b("reeds").func_71896_v();
   public static final Block field_72032_aY = (new BlockJukeBox(84, 74)).func_71848_c(2.0F).func_71894_b(10.0F).func_71884_a(field_71976_h).func_71864_b("jukebox").func_71912_p();
   public static final Block field_72031_aZ = (new BlockFence(85, 4)).func_71848_c(2.0F).func_71894_b(5.0F).func_71884_a(field_71967_e).func_71864_b("fence");
   public static final Block field_72061_ba = (new BlockPumpkin(86, 102, false)).func_71848_c(1.0F).func_71884_a(field_71967_e).func_71864_b("pumpkin").func_71912_p();
   public static final Block field_72012_bb = (new BlockNetherrack(87, 103)).func_71848_c(0.4F).func_71884_a(field_71976_h).func_71864_b("hellrock");
   public static final Block field_72013_bc = (new BlockSoulSand(88, 104)).func_71848_c(0.5F).func_71884_a(field_71972_l).func_71864_b("hellsand");
   public static final Block field_72014_bd = (new BlockGlowStone(89, 105, Material.field_76264_q)).func_71848_c(0.3F).func_71884_a(field_71974_j).func_71900_a(1.0F).func_71864_b("lightgem");
   public static final BlockPortal field_72015_be = (BlockPortal)(new BlockPortal(90, 14)).func_71848_c(-1.0F).func_71884_a(field_71974_j).func_71900_a(0.75F).func_71864_b("portal");
   public static final Block field_72008_bf = (new BlockPumpkin(91, 102, true)).func_71848_c(1.0F).func_71884_a(field_71967_e).func_71900_a(1.0F).func_71864_b("litpumpkin").func_71912_p();
   public static final Block field_72009_bg = (new BlockCake(92, 121)).func_71848_c(0.5F).func_71884_a(field_71975_k).func_71864_b("cake").func_71896_v().func_71912_p();
   public static final Block field_72010_bh = (new BlockRedstoneRepeater(93, false)).func_71848_c(0.0F).func_71884_a(field_71967_e).func_71864_b("diode").func_71896_v().func_71912_p();
   public static final Block field_72011_bi = (new BlockRedstoneRepeater(94, true)).func_71848_c(0.0F).func_71900_a(0.625F).func_71884_a(field_71967_e).func_71864_b("diode").func_71896_v().func_71912_p();
   public static final Block field_72004_bj = (new BlockLockedChest(95)).func_71848_c(0.0F).func_71900_a(1.0F).func_71884_a(field_71967_e).func_71864_b("lockedchest").func_71907_b(true).func_71912_p();
   public static final Block field_72005_bk = (new BlockTrapDoor(96, Material.field_76245_d)).func_71848_c(3.0F).func_71884_a(field_71967_e).func_71864_b("trapdoor").func_71896_v().func_71912_p();
   public static final Block field_72006_bl = (new BlockSilverfish(97)).func_71848_c(0.75F).func_71864_b("monsterStoneEgg");
   public static final Block field_72007_bm = (new BlockStoneBrick(98)).func_71848_c(1.5F).func_71894_b(10.0F).func_71884_a(field_71976_h).func_71864_b("stonebricksmooth");
   public static final Block field_72000_bn = (new BlockMushroomCap(99, Material.field_76245_d, 142, 0)).func_71848_c(0.2F).func_71884_a(field_71967_e).func_71864_b("mushroom").func_71912_p();
   public static final Block field_72001_bo = (new BlockMushroomCap(100, Material.field_76245_d, 142, 1)).func_71848_c(0.2F).func_71884_a(field_71967_e).func_71864_b("mushroom").func_71912_p();
   public static final Block field_72002_bp = (new BlockPane(101, 85, 85, Material.field_76243_f, true)).func_71848_c(5.0F).func_71894_b(10.0F).func_71884_a(field_71977_i).func_71864_b("fenceIron");
   public static final Block field_72003_bq = (new BlockPane(102, 49, 148, Material.field_76264_q, false)).func_71848_c(0.3F).func_71884_a(field_71974_j).func_71864_b("thinGlass");
   public static final Block field_71997_br = (new BlockMelon(103)).func_71848_c(1.0F).func_71884_a(field_71967_e).func_71864_b("melon");
   public static final Block field_71996_bs = (new BlockStem(104, field_72061_ba)).func_71848_c(0.0F).func_71884_a(field_71967_e).func_71864_b("pumpkinStem").func_71912_p();
   public static final Block field_71999_bt = (new BlockStem(105, field_71997_br)).func_71848_c(0.0F).func_71884_a(field_71967_e).func_71864_b("pumpkinStem").func_71912_p();
   public static final Block field_71998_bu = (new BlockVine(106)).func_71848_c(0.2F).func_71884_a(field_71965_g).func_71864_b("vine").func_71912_p();
   public static final Block field_71993_bv = (new BlockFenceGate(107, 4)).func_71848_c(2.0F).func_71894_b(5.0F).func_71884_a(field_71967_e).func_71864_b("fenceGate").func_71912_p();
   public static final Block field_71992_bw = (new BlockStairs(108, field_72081_al, 0)).func_71864_b("stairsBrick").func_71912_p();
   public static final Block field_71995_bx = (new BlockStairs(109, field_72007_bm, 0)).func_71864_b("stairsStoneBrickSmooth").func_71912_p();
   public static final BlockMycelium field_71994_by = (BlockMycelium)(new BlockMycelium(110)).func_71848_c(0.6F).func_71884_a(field_71965_g).func_71864_b("mycel");
   public static final Block field_71991_bz = (new BlockLilyPad(111, 76)).func_71848_c(0.0F).func_71884_a(field_71965_g).func_71864_b("waterlily");
   public static final Block field_72033_bA = (new Block(112, 224, Material.field_76246_e)).func_71848_c(2.0F).func_71894_b(10.0F).func_71884_a(field_71976_h).func_71864_b("netherBrick").func_71849_a(CreativeTabs.field_78030_b);
   public static final Block field_72098_bB = (new BlockFence(113, 224, Material.field_76246_e)).func_71848_c(2.0F).func_71894_b(10.0F).func_71884_a(field_71976_h).func_71864_b("netherFence");
   public static final Block field_72100_bC = (new BlockStairs(114, field_72033_bA, 0)).func_71864_b("stairsNetherBrick").func_71912_p();
   public static final Block field_72094_bD = (new BlockNetherStalk(115)).func_71864_b("netherStalk").func_71912_p();
   public static final Block field_72096_bE = (new BlockEnchantmentTable(116)).func_71848_c(5.0F).func_71894_b(2000.0F).func_71864_b("enchantmentTable");
   public static final Block field_72106_bF = (new BlockBrewingStand(117)).func_71848_c(0.5F).func_71900_a(0.125F).func_71864_b("brewingStand").func_71912_p();
   public static final Block field_72108_bG = (new BlockCauldron(118)).func_71848_c(2.0F).func_71864_b("cauldron").func_71912_p();
   public static final Block field_72102_bH = (new BlockEndPortal(119, Material.field_76237_B)).func_71848_c(-1.0F).func_71894_b(6000000.0F);
   public static final Block field_72104_bI = (new BlockEndPortalFrame(120)).func_71884_a(field_71974_j).func_71900_a(0.125F).func_71848_c(-1.0F).func_71864_b("endPortalFrame").func_71912_p().func_71894_b(6000000.0F).func_71849_a(CreativeTabs.field_78031_c);
   public static final Block field_72082_bJ = (new Block(121, 175, Material.field_76246_e)).func_71848_c(3.0F).func_71894_b(15.0F).func_71884_a(field_71976_h).func_71864_b("whiteStone").func_71849_a(CreativeTabs.field_78030_b);
   public static final Block field_72084_bK = (new BlockDragonEgg(122, 167)).func_71848_c(3.0F).func_71894_b(15.0F).func_71884_a(field_71976_h).func_71900_a(0.125F).func_71864_b("dragonEgg");
   public static final Block field_72078_bL = (new BlockRedstoneLight(123, false)).func_71848_c(0.3F).func_71884_a(field_71974_j).func_71864_b("redstoneLight").func_71849_a(CreativeTabs.field_78028_d);
   public static final Block field_72080_bM = (new BlockRedstoneLight(124, true)).func_71848_c(0.3F).func_71884_a(field_71974_j).func_71864_b("redstoneLight");
   public static final BlockHalfSlab field_72090_bN = (BlockHalfSlab)(new BlockWoodSlab(125, true)).func_71848_c(2.0F).func_71894_b(5.0F).func_71884_a(field_71967_e).func_71864_b("woodSlab");
   public static final BlockHalfSlab field_72092_bO = (BlockHalfSlab)(new BlockWoodSlab(126, false)).func_71848_c(2.0F).func_71894_b(5.0F).func_71884_a(field_71967_e).func_71864_b("woodSlab");
   public static final Block field_72086_bP = (new BlockCocoa(127)).func_71848_c(0.2F).func_71894_b(5.0F).func_71884_a(field_71967_e).func_71864_b("cocoa").func_71912_p();
   public static final Block field_72088_bQ = (new BlockStairs(128, field_71957_Q, 0)).func_71864_b("stairsSandStone").func_71912_p();
   public static final Block field_72068_bR = (new BlockOre(129, 171)).func_71848_c(3.0F).func_71894_b(5.0F).func_71884_a(field_71976_h).func_71864_b("oreEmerald");
   public static final Block field_72066_bS = (new BlockEnderChest(130)).func_71848_c(22.5F).func_71894_b(1000.0F).func_71884_a(field_71976_h).func_71864_b("enderChest").func_71912_p().func_71900_a(0.5F);
   public static final BlockTripWireSource field_72064_bT = (BlockTripWireSource)(new BlockTripWireSource(131)).func_71864_b("tripWireSource").func_71912_p();
   public static final Block field_72062_bU = (new BlockTripWire(132)).func_71864_b("tripWire").func_71912_p();
   public static final Block field_72076_bV = (new BlockOreStorage(133, 25)).func_71848_c(5.0F).func_71894_b(10.0F).func_71884_a(field_71977_i).func_71864_b("blockEmerald");
   public static final Block field_72074_bW = (new BlockStairs(134, field_71988_x, 1)).func_71864_b("stairsWoodSpruce").func_71912_p();
   public static final Block field_72072_bX = (new BlockStairs(135, field_71988_x, 2)).func_71864_b("stairsWoodBirch").func_71912_p();
   public static final Block field_72070_bY = (new BlockStairs(136, field_71988_x, 3)).func_71864_b("stairsWoodJungle").func_71912_p();
   public static final Block field_82517_cc = (new BlockCommandBlock(137)).func_71864_b("commandBlock");
   public static final Block field_82518_cd = (new BlockBeacon(138)).func_71864_b("beacon").func_71900_a(1.0F);
   public static final Block field_82515_ce = (new BlockWall(139, field_71978_w)).func_71864_b("cobbleWall");
   public static final Block field_82516_cf = (new BlockFlowerPot(140)).func_71848_c(0.0F).func_71884_a(field_71966_d).func_71864_b("flowerPot");
   public static final Block field_82513_cg = (new BlockCarrot(141)).func_71864_b("carrots");
   public static final Block field_82514_ch = (new BlockPotato(142)).func_71864_b("potatoes");
   public static final Block field_82511_ci = (new BlockButton(143, field_71988_x.field_72059_bZ, true)).func_71848_c(0.5F).func_71884_a(field_71967_e).func_71864_b("button").func_71912_p();
   public static final Block field_82512_cj = (new BlockSkull(144)).func_71848_c(1.0F).func_71884_a(field_71976_h).func_71864_b("skull").func_71912_p();
   public static final Block field_82510_ck = (new BlockAnvil(145)).func_71848_c(5.0F).func_71884_a(field_82508_o).func_71894_b(2000.0F).func_71864_b("anvil").func_71912_p();
   public int field_72059_bZ;
   public final int field_71990_ca;
   protected float field_71989_cb;
   protected float field_72029_cc;
   protected boolean field_72030_cd;
   protected boolean field_72027_ce;
   protected boolean field_72028_cf;
   protected boolean field_72025_cg;
   protected double field_72026_ch;
   protected double field_72023_ci;
   protected double field_72024_cj;
   protected double field_72021_ck;
   protected double field_72022_cl;
   protected double field_72019_cm;
   public StepSound field_72020_cn;
   public float field_72017_co;
   public final Material field_72018_cp;
   public float field_72016_cq;
   private String field_71968_b;


   public Block(int p_i4009_1_, Material p_i4009_2_) {
      this.field_72030_cd = true;
      this.field_72027_ce = true;
      this.field_72020_cn = field_71966_d;
      this.field_72017_co = 1.0F;
      this.field_72016_cq = 0.6F;
      if(field_71973_m[p_i4009_1_] != null) {
         throw new IllegalArgumentException("Slot " + p_i4009_1_ + " is already occupied by " + field_71973_m[p_i4009_1_] + " when adding " + this);
      } else {
         this.field_72018_cp = p_i4009_2_;
         field_71973_m[p_i4009_1_] = this;
         this.field_71990_ca = p_i4009_1_;
         this.func_71905_a(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
         field_71970_n[p_i4009_1_] = this.func_71926_d();
         field_71971_o[p_i4009_1_] = this.func_71926_d()?255:0;
         field_71985_p[p_i4009_1_] = !p_i4009_2_.func_76228_b();
      }
   }

   public Block func_71912_p() {
      field_71983_r[this.field_71990_ca] = true;
      return this;
   }

   protected void func_71928_r_() {}

   public Block(int p_i4010_1_, int p_i4010_2_, Material p_i4010_3_) {
      this(p_i4010_1_, p_i4010_3_);
      this.field_72059_bZ = p_i4010_2_;
   }

   public Block func_71884_a(StepSound p_71884_1_) {
      this.field_72020_cn = p_71884_1_;
      return this;
   }

   public Block func_71868_h(int p_71868_1_) {
      field_71971_o[this.field_71990_ca] = p_71868_1_;
      return this;
   }

   public Block func_71900_a(float p_71900_1_) {
      field_71984_q[this.field_71990_ca] = (int)(15.0F * p_71900_1_);
      return this;
   }

   public Block func_71894_b(float p_71894_1_) {
      this.field_72029_cc = p_71894_1_ * 3.0F;
      return this;
   }

   public static boolean func_71932_i(int p_71932_0_) {
      Block var1 = field_71973_m[p_71932_0_];
      return var1 == null?false:var1.field_72018_cp.func_76218_k() && var1.func_71886_c();
   }

   public boolean func_71886_c() {
      return true;
   }

   public boolean func_71918_c(IBlockAccess p_71918_1_, int p_71918_2_, int p_71918_3_, int p_71918_4_) {
      return !this.field_72018_cp.func_76230_c();
   }

   public int func_71857_b() {
      return 0;
   }

   public Block func_71848_c(float p_71848_1_) {
      this.field_71989_cb = p_71848_1_;
      if(this.field_72029_cc < p_71848_1_ * 5.0F) {
         this.field_72029_cc = p_71848_1_ * 5.0F;
      }

      return this;
   }

   public Block func_71875_q() {
      this.func_71848_c(-1.0F);
      return this;
   }

   public float func_71934_m(World p_71934_1_, int p_71934_2_, int p_71934_3_, int p_71934_4_) {
      return this.field_71989_cb;
   }

   public Block func_71907_b(boolean p_71907_1_) {
      this.field_72028_cf = p_71907_1_;
      return this;
   }

   public boolean func_71881_r() {
      return this.field_72028_cf;
   }

   public boolean func_71887_s() {
      return this.field_72025_cg;
   }

   public final void func_71905_a(float p_71905_1_, float p_71905_2_, float p_71905_3_, float p_71905_4_, float p_71905_5_, float p_71905_6_) {
      this.field_72026_ch = (double)p_71905_1_;
      this.field_72023_ci = (double)p_71905_2_;
      this.field_72024_cj = (double)p_71905_3_;
      this.field_72021_ck = (double)p_71905_4_;
      this.field_72022_cl = (double)p_71905_5_;
      this.field_72019_cm = (double)p_71905_6_;
   }

   @SideOnly(Side.CLIENT)
   public float func_71870_f(IBlockAccess p_71870_1_, int p_71870_2_, int p_71870_3_, int p_71870_4_) {
      return p_71870_1_.func_72808_j(p_71870_2_, p_71870_3_, p_71870_4_, field_71984_q[p_71870_1_.func_72798_a(p_71870_2_, p_71870_3_, p_71870_4_)]);
   }

   @SideOnly(Side.CLIENT)
   public int func_71874_e(IBlockAccess p_71874_1_, int p_71874_2_, int p_71874_3_, int p_71874_4_) {
      return p_71874_1_.func_72802_i(p_71874_2_, p_71874_3_, p_71874_4_, field_71984_q[p_71874_1_.func_72798_a(p_71874_2_, p_71874_3_, p_71874_4_)]);
   }

   @SideOnly(Side.CLIENT)
   public boolean func_71877_c(IBlockAccess p_71877_1_, int p_71877_2_, int p_71877_3_, int p_71877_4_, int p_71877_5_) {
      return p_71877_5_ == 0 && this.field_72023_ci > 0.0D?true:(p_71877_5_ == 1 && this.field_72022_cl < 1.0D?true:(p_71877_5_ == 2 && this.field_72024_cj > 0.0D?true:(p_71877_5_ == 3 && this.field_72019_cm < 1.0D?true:(p_71877_5_ == 4 && this.field_72026_ch > 0.0D?true:(p_71877_5_ == 5 && this.field_72021_ck < 1.0D?true:!p_71877_1_.func_72804_r(p_71877_2_, p_71877_3_, p_71877_4_))))));
   }

   public boolean func_71924_d(IBlockAccess p_71924_1_, int p_71924_2_, int p_71924_3_, int p_71924_4_, int p_71924_5_) {
      return p_71924_1_.func_72803_f(p_71924_2_, p_71924_3_, p_71924_4_).func_76220_a();
   }

   @SideOnly(Side.CLIENT)
   public int func_71895_b(IBlockAccess p_71895_1_, int p_71895_2_, int p_71895_3_, int p_71895_4_, int p_71895_5_) {
      return this.func_71858_a(p_71895_5_, p_71895_1_.func_72805_g(p_71895_2_, p_71895_3_, p_71895_4_));
   }

   public int func_71858_a(int p_71858_1_, int p_71858_2_) {
      return this.func_71851_a(p_71858_1_);
   }

   public int func_71851_a(int p_71851_1_) {
      return this.field_72059_bZ;
   }

   public void func_71871_a(World p_71871_1_, int p_71871_2_, int p_71871_3_, int p_71871_4_, AxisAlignedBB p_71871_5_, List p_71871_6_, Entity p_71871_7_) {
      AxisAlignedBB var8 = this.func_71872_e(p_71871_1_, p_71871_2_, p_71871_3_, p_71871_4_);
      if(var8 != null && p_71871_5_.func_72326_a(var8)) {
         p_71871_6_.add(var8);
      }

   }

   @SideOnly(Side.CLIENT)
   public AxisAlignedBB func_71911_a_(World p_71911_1_, int p_71911_2_, int p_71911_3_, int p_71911_4_) {
      return AxisAlignedBB.func_72332_a().func_72299_a((double)p_71911_2_ + this.field_72026_ch, (double)p_71911_3_ + this.field_72023_ci, (double)p_71911_4_ + this.field_72024_cj, (double)p_71911_2_ + this.field_72021_ck, (double)p_71911_3_ + this.field_72022_cl, (double)p_71911_4_ + this.field_72019_cm);
   }

   public AxisAlignedBB func_71872_e(World p_71872_1_, int p_71872_2_, int p_71872_3_, int p_71872_4_) {
      return AxisAlignedBB.func_72332_a().func_72299_a((double)p_71872_2_ + this.field_72026_ch, (double)p_71872_3_ + this.field_72023_ci, (double)p_71872_4_ + this.field_72024_cj, (double)p_71872_2_ + this.field_72021_ck, (double)p_71872_3_ + this.field_72022_cl, (double)p_71872_4_ + this.field_72019_cm);
   }

   public boolean func_71926_d() {
      return true;
   }

   public boolean func_71913_a(int p_71913_1_, boolean p_71913_2_) {
      return this.func_71935_l();
   }

   public boolean func_71935_l() {
      return true;
   }

   public void func_71847_b(World p_71847_1_, int p_71847_2_, int p_71847_3_, int p_71847_4_, Random p_71847_5_) {}

   @SideOnly(Side.CLIENT)
   public void func_71862_a(World p_71862_1_, int p_71862_2_, int p_71862_3_, int p_71862_4_, Random p_71862_5_) {}

   public void func_71898_d(World p_71898_1_, int p_71898_2_, int p_71898_3_, int p_71898_4_, int p_71898_5_) {}

   public void func_71863_a(World p_71863_1_, int p_71863_2_, int p_71863_3_, int p_71863_4_, int p_71863_5_) {}

   public int func_71859_p_() {
      return 10;
   }

   public void func_71861_g(World p_71861_1_, int p_71861_2_, int p_71861_3_, int p_71861_4_) {}

   public void func_71852_a(World p_71852_1_, int p_71852_2_, int p_71852_3_, int p_71852_4_, int p_71852_5_, int p_71852_6_) {}

   public int func_71925_a(Random p_71925_1_) {
      return 1;
   }

   public int func_71885_a(int p_71885_1_, Random p_71885_2_, int p_71885_3_) {
      return this.field_71990_ca;
   }

   public float func_71908_a(EntityPlayer p_71908_1_, World p_71908_2_, int p_71908_3_, int p_71908_4_, int p_71908_5_) {
      float var6 = this.func_71934_m(p_71908_2_, p_71908_3_, p_71908_4_, p_71908_5_);
      return var6 < 0.0F?0.0F:(!p_71908_1_.func_71062_b(this)?1.0F / var6 / 100.0F:p_71908_1_.func_71055_a(this) / var6 / 30.0F);
   }

   public final void func_71897_c(World p_71897_1_, int p_71897_2_, int p_71897_3_, int p_71897_4_, int p_71897_5_, int p_71897_6_) {
      this.func_71914_a(p_71897_1_, p_71897_2_, p_71897_3_, p_71897_4_, p_71897_5_, 1.0F, p_71897_6_);
   }

   public void func_71914_a(World p_71914_1_, int p_71914_2_, int p_71914_3_, int p_71914_4_, int p_71914_5_, float p_71914_6_, int p_71914_7_) {
      if(!p_71914_1_.field_72995_K) {
         int var8 = this.func_71910_a(p_71914_7_, p_71914_1_.field_73012_v);

         for(int var9 = 0; var9 < var8; ++var9) {
            if(p_71914_1_.field_73012_v.nextFloat() <= p_71914_6_) {
               int var10 = this.func_71885_a(p_71914_5_, p_71914_1_.field_73012_v, p_71914_7_);
               if(var10 > 0) {
                  this.func_71929_a(p_71914_1_, p_71914_2_, p_71914_3_, p_71914_4_, new ItemStack(var10, 1, this.func_71899_b(p_71914_5_)));
               }
            }
         }

      }
   }

   protected void func_71929_a(World p_71929_1_, int p_71929_2_, int p_71929_3_, int p_71929_4_, ItemStack p_71929_5_) {
      if(!p_71929_1_.field_72995_K && p_71929_1_.func_82736_K().func_82766_b("doTileDrops")) {
         float var6 = 0.7F;
         double var7 = (double)(p_71929_1_.field_73012_v.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
         double var9 = (double)(p_71929_1_.field_73012_v.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
         double var11 = (double)(p_71929_1_.field_73012_v.nextFloat() * var6) + (double)(1.0F - var6) * 0.5D;
         EntityItem var13 = new EntityItem(p_71929_1_, (double)p_71929_2_ + var7, (double)p_71929_3_ + var9, (double)p_71929_4_ + var11, p_71929_5_);
         var13.field_70293_c = 10;
         p_71929_1_.func_72838_d(var13);
      }
   }

   protected void func_71923_g(World p_71923_1_, int p_71923_2_, int p_71923_3_, int p_71923_4_, int p_71923_5_) {
      if(!p_71923_1_.field_72995_K) {
         while(p_71923_5_ > 0) {
            int var6 = EntityXPOrb.func_70527_a(p_71923_5_);
            p_71923_5_ -= var6;
            p_71923_1_.func_72838_d(new EntityXPOrb(p_71923_1_, (double)p_71923_2_ + 0.5D, (double)p_71923_3_ + 0.5D, (double)p_71923_4_ + 0.5D, var6));
         }
      }

   }

   public int func_71899_b(int p_71899_1_) {
      return 0;
   }

   public float func_71904_a(Entity p_71904_1_) {
      return this.field_72029_cc / 5.0F;
   }

   public MovingObjectPosition func_71878_a(World p_71878_1_, int p_71878_2_, int p_71878_3_, int p_71878_4_, Vec3 p_71878_5_, Vec3 p_71878_6_) {
      this.func_71902_a(p_71878_1_, p_71878_2_, p_71878_3_, p_71878_4_);
      p_71878_5_ = p_71878_5_.func_72441_c((double)(-p_71878_2_), (double)(-p_71878_3_), (double)(-p_71878_4_));
      p_71878_6_ = p_71878_6_.func_72441_c((double)(-p_71878_2_), (double)(-p_71878_3_), (double)(-p_71878_4_));
      Vec3 var7 = p_71878_5_.func_72429_b(p_71878_6_, this.field_72026_ch);
      Vec3 var8 = p_71878_5_.func_72429_b(p_71878_6_, this.field_72021_ck);
      Vec3 var9 = p_71878_5_.func_72435_c(p_71878_6_, this.field_72023_ci);
      Vec3 var10 = p_71878_5_.func_72435_c(p_71878_6_, this.field_72022_cl);
      Vec3 var11 = p_71878_5_.func_72434_d(p_71878_6_, this.field_72024_cj);
      Vec3 var12 = p_71878_5_.func_72434_d(p_71878_6_, this.field_72019_cm);
      if(!this.func_71916_a(var7)) {
         var7 = null;
      }

      if(!this.func_71916_a(var8)) {
         var8 = null;
      }

      if(!this.func_71936_b(var9)) {
         var9 = null;
      }

      if(!this.func_71936_b(var10)) {
         var10 = null;
      }

      if(!this.func_71890_c(var11)) {
         var11 = null;
      }

      if(!this.func_71890_c(var12)) {
         var12 = null;
      }

      Vec3 var13 = null;
      if(var7 != null && (var13 == null || p_71878_5_.func_72436_e(var7) < p_71878_5_.func_72436_e(var13))) {
         var13 = var7;
      }

      if(var8 != null && (var13 == null || p_71878_5_.func_72436_e(var8) < p_71878_5_.func_72436_e(var13))) {
         var13 = var8;
      }

      if(var9 != null && (var13 == null || p_71878_5_.func_72436_e(var9) < p_71878_5_.func_72436_e(var13))) {
         var13 = var9;
      }

      if(var10 != null && (var13 == null || p_71878_5_.func_72436_e(var10) < p_71878_5_.func_72436_e(var13))) {
         var13 = var10;
      }

      if(var11 != null && (var13 == null || p_71878_5_.func_72436_e(var11) < p_71878_5_.func_72436_e(var13))) {
         var13 = var11;
      }

      if(var12 != null && (var13 == null || p_71878_5_.func_72436_e(var12) < p_71878_5_.func_72436_e(var13))) {
         var13 = var12;
      }

      if(var13 == null) {
         return null;
      } else {
         byte var14 = -1;
         if(var13 == var7) {
            var14 = 4;
         }

         if(var13 == var8) {
            var14 = 5;
         }

         if(var13 == var9) {
            var14 = 0;
         }

         if(var13 == var10) {
            var14 = 1;
         }

         if(var13 == var11) {
            var14 = 2;
         }

         if(var13 == var12) {
            var14 = 3;
         }

         return new MovingObjectPosition(p_71878_2_, p_71878_3_, p_71878_4_, var14, var13.func_72441_c((double)p_71878_2_, (double)p_71878_3_, (double)p_71878_4_));
      }
   }

   private boolean func_71916_a(Vec3 p_71916_1_) {
      return p_71916_1_ == null?false:p_71916_1_.field_72448_b >= this.field_72023_ci && p_71916_1_.field_72448_b <= this.field_72022_cl && p_71916_1_.field_72449_c >= this.field_72024_cj && p_71916_1_.field_72449_c <= this.field_72019_cm;
   }

   private boolean func_71936_b(Vec3 p_71936_1_) {
      return p_71936_1_ == null?false:p_71936_1_.field_72450_a >= this.field_72026_ch && p_71936_1_.field_72450_a <= this.field_72021_ck && p_71936_1_.field_72449_c >= this.field_72024_cj && p_71936_1_.field_72449_c <= this.field_72019_cm;
   }

   private boolean func_71890_c(Vec3 p_71890_1_) {
      return p_71890_1_ == null?false:p_71890_1_.field_72450_a >= this.field_72026_ch && p_71890_1_.field_72450_a <= this.field_72021_ck && p_71890_1_.field_72448_b >= this.field_72023_ci && p_71890_1_.field_72448_b <= this.field_72022_cl;
   }

   public void func_71867_k(World p_71867_1_, int p_71867_2_, int p_71867_3_, int p_71867_4_) {}

   @SideOnly(Side.CLIENT)
   public int func_71856_s_() {
      return 0;
   }

   public boolean func_71850_a_(World p_71850_1_, int p_71850_2_, int p_71850_3_, int p_71850_4_, int p_71850_5_) {
      return this.func_71930_b(p_71850_1_, p_71850_2_, p_71850_3_, p_71850_4_);
   }

   public boolean func_71930_b(World p_71930_1_, int p_71930_2_, int p_71930_3_, int p_71930_4_) {
      int var5 = p_71930_1_.func_72798_a(p_71930_2_, p_71930_3_, p_71930_4_);
      return var5 == 0 || field_71973_m[var5].field_72018_cp.func_76222_j();
   }

   public boolean func_71903_a(World p_71903_1_, int p_71903_2_, int p_71903_3_, int p_71903_4_, EntityPlayer p_71903_5_, int p_71903_6_, float p_71903_7_, float p_71903_8_, float p_71903_9_) {
      return false;
   }

   public void func_71891_b(World p_71891_1_, int p_71891_2_, int p_71891_3_, int p_71891_4_, Entity p_71891_5_) {}

   public int func_85104_a(World p_85104_1_, int p_85104_2_, int p_85104_3_, int p_85104_4_, int p_85104_5_, float p_85104_6_, float p_85104_7_, float p_85104_8_, int p_85104_9_) {
      return p_85104_9_;
   }

   public void func_71921_a(World p_71921_1_, int p_71921_2_, int p_71921_3_, int p_71921_4_, EntityPlayer p_71921_5_) {}

   public void func_71901_a(World p_71901_1_, int p_71901_2_, int p_71901_3_, int p_71901_4_, Entity p_71901_5_, Vec3 p_71901_6_) {}

   public void func_71902_a(IBlockAccess p_71902_1_, int p_71902_2_, int p_71902_3_, int p_71902_4_) {}

   public final double func_83009_v() {
      return this.field_72026_ch;
   }

   public final double func_83007_w() {
      return this.field_72021_ck;
   }

   public final double func_83008_x() {
      return this.field_72023_ci;
   }

   public final double func_83010_y() {
      return this.field_72022_cl;
   }

   public final double func_83005_z() {
      return this.field_72024_cj;
   }

   public final double func_83006_A() {
      return this.field_72019_cm;
   }

   @SideOnly(Side.CLIENT)
   public int func_71933_m() {
      return 16777215;
   }

   @SideOnly(Side.CLIENT)
   public int func_71889_f_(int p_71889_1_) {
      return 16777215;
   }

   @SideOnly(Side.CLIENT)
   public int func_71920_b(IBlockAccess p_71920_1_, int p_71920_2_, int p_71920_3_, int p_71920_4_) {
      return 16777215;
   }

   public boolean func_71865_a(IBlockAccess p_71865_1_, int p_71865_2_, int p_71865_3_, int p_71865_4_, int p_71865_5_) {
      return false;
   }

   public boolean func_71853_i() {
      return false;
   }

   public void func_71869_a(World p_71869_1_, int p_71869_2_, int p_71869_3_, int p_71869_4_, Entity p_71869_5_) {}

   public boolean func_71855_c(IBlockAccess p_71855_1_, int p_71855_2_, int p_71855_3_, int p_71855_4_, int p_71855_5_) {
      return false;
   }

   public void func_71919_f() {}

   public void func_71893_a(World p_71893_1_, EntityPlayer p_71893_2_, int p_71893_3_, int p_71893_4_, int p_71893_5_, int p_71893_6_) {
      p_71893_2_.func_71064_a(StatList.field_75934_C[this.field_71990_ca], 1);
      p_71893_2_.func_71020_j(0.025F);
      if(this.func_71906_q_() && EnchantmentHelper.func_77502_d(p_71893_2_)) {
         ItemStack var8 = this.func_71880_c_(p_71893_6_);
         if(var8 != null) {
            this.func_71929_a(p_71893_1_, p_71893_3_, p_71893_4_, p_71893_5_, var8);
         }
      } else {
         int var7 = EnchantmentHelper.func_77517_e(p_71893_2_);
         this.func_71897_c(p_71893_1_, p_71893_3_, p_71893_4_, p_71893_5_, p_71893_6_, var7);
      }

   }

   protected boolean func_71906_q_() {
      return this.func_71886_c() && !this.field_72025_cg;
   }

   protected ItemStack func_71880_c_(int p_71880_1_) {
      int var2 = 0;
      if(this.field_71990_ca >= 0 && this.field_71990_ca < Item.field_77698_e.length && Item.field_77698_e[this.field_71990_ca].func_77614_k()) {
         var2 = p_71880_1_;
      }

      return new ItemStack(this.field_71990_ca, 1, var2);
   }

   public int func_71910_a(int p_71910_1_, Random p_71910_2_) {
      return this.func_71925_a(p_71910_2_);
   }

   public boolean func_71854_d(World p_71854_1_, int p_71854_2_, int p_71854_3_, int p_71854_4_) {
      return true;
   }

   public void func_71860_a(World p_71860_1_, int p_71860_2_, int p_71860_3_, int p_71860_4_, EntityLiving p_71860_5_) {}

   public void func_85105_g(World p_85105_1_, int p_85105_2_, int p_85105_3_, int p_85105_4_, int p_85105_5_) {}

   public Block func_71864_b(String p_71864_1_) {
      this.field_71968_b = "tile." + p_71864_1_;
      return this;
   }

   public String func_71931_t() {
      return StatCollector.func_74838_a(this.func_71917_a() + ".name");
   }

   public String func_71917_a() {
      return this.field_71968_b;
   }

   public void func_71883_b(World p_71883_1_, int p_71883_2_, int p_71883_3_, int p_71883_4_, int p_71883_5_, int p_71883_6_) {}

   public boolean func_71876_u() {
      return this.field_72027_ce;
   }

   protected Block func_71896_v() {
      this.field_72027_ce = false;
      return this;
   }

   public int func_71915_e() {
      return this.field_72018_cp.func_76227_m();
   }

   @SideOnly(Side.CLIENT)
   public float func_71888_h(IBlockAccess p_71888_1_, int p_71888_2_, int p_71888_3_, int p_71888_4_) {
      return p_71888_1_.func_72809_s(p_71888_2_, p_71888_3_, p_71888_4_)?0.2F:1.0F;
   }

   public void func_71866_a(World p_71866_1_, int p_71866_2_, int p_71866_3_, int p_71866_4_, Entity p_71866_5_, float p_71866_6_) {}

   @SideOnly(Side.CLIENT)
   public int func_71922_a(World p_71922_1_, int p_71922_2_, int p_71922_3_, int p_71922_4_) {
      return this.field_71990_ca;
   }

   public int func_71873_h(World p_71873_1_, int p_71873_2_, int p_71873_3_, int p_71873_4_) {
      return this.func_71899_b(p_71873_1_.func_72805_g(p_71873_2_, p_71873_3_, p_71873_4_));
   }

   @SideOnly(Side.CLIENT)
   public void func_71879_a(int p_71879_1_, CreativeTabs p_71879_2_, List p_71879_3_) {
      p_71879_3_.add(new ItemStack(p_71879_1_, 1, 0));
   }

   public Block func_71849_a(CreativeTabs p_71849_1_) {
      this.field_71969_a = p_71849_1_;
      return this;
   }

   public void func_71846_a(World p_71846_1_, int p_71846_2_, int p_71846_3_, int p_71846_4_, int p_71846_5_, EntityPlayer p_71846_6_) {}

   @SideOnly(Side.CLIENT)
   public CreativeTabs func_71882_w() {
      return this.field_71969_a;
   }

   public void func_71927_h(World p_71927_1_, int p_71927_2_, int p_71927_3_, int p_71927_4_, int p_71927_5_) {}

   public void func_71892_f(World p_71892_1_, int p_71892_2_, int p_71892_3_, int p_71892_4_) {}

   @SideOnly(Side.CLIENT)
   public boolean func_82505_u_() {
      return false;
   }

   public boolean func_82506_l() {
      return true;
   }

   public boolean func_85103_a(Explosion p_85103_1_) {
      return true;
   }

   static {
      Item.field_77698_e[field_72101_ab.field_71990_ca] = (new ItemCloth(field_72101_ab.field_71990_ca - 256)).func_77655_b("cloth");
      Item.field_77698_e[field_71951_J.field_71990_ca] = (new ItemMultiTextureTile(field_71951_J.field_71990_ca - 256, field_71951_J, BlockLog.field_72142_a)).func_77655_b("log");
      Item.field_77698_e[field_71988_x.field_71990_ca] = (new ItemMultiTextureTile(field_71988_x.field_71990_ca - 256, field_71988_x, BlockWood.field_72152_a)).func_77655_b("wood");
      Item.field_77698_e[field_72006_bl.field_71990_ca] = (new ItemMultiTextureTile(field_72006_bl.field_71990_ca - 256, field_72006_bl, BlockSilverfish.field_72155_a)).func_77655_b("monsterStoneEgg");
      Item.field_77698_e[field_72007_bm.field_71990_ca] = (new ItemMultiTextureTile(field_72007_bm.field_71990_ca - 256, field_72007_bm, BlockStoneBrick.field_72188_a)).func_77655_b("stonebricksmooth");
      Item.field_77698_e[field_71957_Q.field_71990_ca] = (new ItemMultiTextureTile(field_71957_Q.field_71990_ca - 256, field_71957_Q, BlockSandStone.field_72189_a)).func_77655_b("sandStone");
      Item.field_77698_e[field_72079_ak.field_71990_ca] = (new ItemSlab(field_72079_ak.field_71990_ca - 256, field_72079_ak, field_72085_aj, false)).func_77655_b("stoneSlab");
      Item.field_77698_e[field_72085_aj.field_71990_ca] = (new ItemSlab(field_72085_aj.field_71990_ca - 256, field_72079_ak, field_72085_aj, true)).func_77655_b("stoneSlab");
      Item.field_77698_e[field_72092_bO.field_71990_ca] = (new ItemSlab(field_72092_bO.field_71990_ca - 256, field_72092_bO, field_72090_bN, false)).func_77655_b("woodSlab");
      Item.field_77698_e[field_72090_bN.field_71990_ca] = (new ItemSlab(field_72090_bN.field_71990_ca - 256, field_72092_bO, field_72090_bN, true)).func_77655_b("woodSlab");
      Item.field_77698_e[field_71987_y.field_71990_ca] = (new ItemMultiTextureTile(field_71987_y.field_71990_ca - 256, field_71987_y, BlockSapling.field_72270_a)).func_77655_b("sapling");
      Item.field_77698_e[field_71952_K.field_71990_ca] = (new ItemLeaves(field_71952_K.field_71990_ca - 256)).func_77655_b("leaves");
      Item.field_77698_e[field_71998_bu.field_71990_ca] = new ItemColored(field_71998_bu.field_71990_ca - 256, false);
      Item.field_77698_e[field_71962_X.field_71990_ca] = (new ItemColored(field_71962_X.field_71990_ca - 256, true)).func_77894_a(new String[]{"shrub", "grass", "fern"});
      Item.field_77698_e[field_71991_bz.field_71990_ca] = new ItemLilyPad(field_71991_bz.field_71990_ca - 256);
      Item.field_77698_e[field_71963_Z.field_71990_ca] = new ItemPiston(field_71963_Z.field_71990_ca - 256);
      Item.field_77698_e[field_71956_V.field_71990_ca] = new ItemPiston(field_71956_V.field_71990_ca - 256);
      Item.field_77698_e[field_82515_ce.field_71990_ca] = (new ItemMultiTextureTile(field_82515_ce.field_71990_ca - 256, field_82515_ce, BlockWall.field_82539_a)).func_77655_b("cobbleWall");
      Item.field_77698_e[field_82510_ck.field_71990_ca] = (new ItemAnvilBlock(field_82510_ck)).func_77655_b("anvil");

      for(int var0 = 0; var0 < 256; ++var0) {
         if(field_71973_m[var0] != null) {
            if(Item.field_77698_e[var0] == null) {
               Item.field_77698_e[var0] = new ItemBlock(var0 - 256);
               field_71973_m[var0].func_71928_r_();
            }

            boolean var1 = false;
            if(var0 > 0 && field_71973_m[var0].func_71857_b() == 10) {
               var1 = true;
            }

            if(var0 > 0 && field_71973_m[var0] instanceof BlockHalfSlab) {
               var1 = true;
            }

            if(var0 == field_72050_aA.field_71990_ca) {
               var1 = true;
            }

            if(field_71985_p[var0]) {
               var1 = true;
            }

            if(field_71971_o[var0] == 0) {
               var1 = true;
            }

            field_71982_s[var0] = var1;
         }
      }

      field_71985_p[0] = true;
      StatList.func_75922_b();
   }
}
