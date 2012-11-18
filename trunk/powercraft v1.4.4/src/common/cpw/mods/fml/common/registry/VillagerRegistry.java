package cpw.mods.fml.common.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import net.minecraft.src.ComponentVillageStartPiece;
import net.minecraft.src.EntityVillager;
import net.minecraft.src.Item;
import net.minecraft.src.MapGenVillage;
import net.minecraft.src.MerchantRecipeList;
import net.minecraft.src.StructureVillagePieceWeight;
import net.minecraft.src.StructureVillagePieces;
import net.minecraft.src.Tuple;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

import cpw.mods.fml.common.FMLLog;

public class VillagerRegistry
{
    private static final VillagerRegistry INSTANCE = new VillagerRegistry();

    private Multimap<Integer, IVillageTradeHandler> tradeHandlers = ArrayListMultimap.create();
    private Map < Class<?>, IVillageCreationHandler > villageCreationHandlers = Maps.newHashMap();
    private Map<Integer, String> newVillagers = Maps.newHashMap();
    private List<Integer> newVillagerIds = Lists.newArrayList();

    public interface IVillageCreationHandler
    {
        StructureVillagePieceWeight getVillagePieceWeight(Random random, int i);

        Class<?> getComponentClass();

        Object buildComponent(StructureVillagePieceWeight villagePiece, ComponentVillageStartPiece startPiece, List pieces, Random random, int p1,
                int p2, int p3, int p4, int p5);
    }

    public interface IVillageTradeHandler
    {
        void manipulateTradesForVillager(EntityVillager villager, MerchantRecipeList recipeList, Random random);
    }

    public static VillagerRegistry instance()
    {
        return INSTANCE;
    }

    public void registerVillagerType(int villagerId, String villagerSkin)
    {
        if (newVillagers.containsKey(villagerId))
        {
            FMLLog.severe("Attempt to register duplicate villager id %d", villagerId);
            throw new RuntimeException();
        }

        newVillagers.put(villagerId, villagerSkin);
        newVillagerIds.add(villagerId);
    }

    public void registerVillageCreationHandler(IVillageCreationHandler handler)
    {
        villageCreationHandlers.put(handler.getComponentClass(), handler);
    }

    public void registerVillageTradeHandler(int villagerId, IVillageTradeHandler handler)
    {
        tradeHandlers.put(villagerId, handler);
    }

    public static String getVillagerSkin(int villagerType, String defaultSkin)
    {
        if (instance().newVillagers.containsKey(villagerType))
        {
            return instance().newVillagers.get(villagerType);
        }

        return defaultSkin;
    }

    public static void manageVillagerTrades(MerchantRecipeList recipeList, EntityVillager villager, int villagerType, Random random)
    {
        for (IVillageTradeHandler handler : instance().tradeHandlers.get(villagerType))
        {
            handler.manipulateTradesForVillager(villager, recipeList, random);
        }
    }

    public static void addExtraVillageComponents(ArrayList components, Random random, int i)
    {
        List<StructureVillagePieceWeight> parts = components;

        for (IVillageCreationHandler handler : instance().villageCreationHandlers.values())
        {
            parts.add(handler.getVillagePieceWeight(random, i));
        }
    }

    public static Object getVillageComponent(StructureVillagePieceWeight villagePiece, ComponentVillageStartPiece startPiece, List pieces, Random random,
            int p1, int p2, int p3, int p4, int p5)
    {
        return instance().villageCreationHandlers.get(villagePiece.villagePieceClass).buildComponent(villagePiece, startPiece, pieces, random, p1, p2, p3, p4, p5);
    }

    public static void addEmeraldBuyRecipe(EntityVillager villager, MerchantRecipeList list, Random random, Item item, float chance, int min, int max)
    {
        if (min > 0 && max > 0)
        {
            EntityVillager.villagerStockList.put(item.shiftedIndex, new Tuple(min, max));
        }

        villager.addMerchantItem(list, item.getMaxDamage(), random, chance);
    }

    public static void addEmeraldSellRecipe(EntityVillager villager, MerchantRecipeList list, Random random, Item item, float chance, int min, int max)
    {
        if (min > 0 && max > 0)
        {
            EntityVillager.blacksmithSellingList.put(item.shiftedIndex, new Tuple(min, max));
        }

        villager.addBlacksmithItem(list, item.getMaxDamage(), random, chance);
    }

    public static void applyRandomTrade(EntityVillager villager, Random rand)
    {
        int extra = instance().newVillagerIds.size();
        int trade = rand.nextInt(5 + extra);
        villager.setProfession(trade < 5 ? trade : instance().newVillagerIds.get(trade - 5));
    }
}
