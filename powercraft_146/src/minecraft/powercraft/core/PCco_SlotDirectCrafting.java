package powercraft.core;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import powercraft.management.PC_ISlotWithBackground;
import powercraft.management.PC_Utils.GameInfo;

public class PCco_SlotDirectCrafting extends Slot implements PC_ISlotWithBackground
{
    public static boolean recursiveCrafting = true;

    public static boolean survivalCheating = false;

    private EntityPlayer thePlayer;

    public ItemStack product;
    private static final int RECURSION_LIMIT = 50;

    private boolean available = false;

    public PCco_SlotDirectCrafting(EntityPlayer entityplayer, ItemStack product, int index, int x, int y)
    {
        super(null, index, x, y);
        thePlayer = entityplayer;
        this.product = product;
        updateAvailability();
    }

    @Override
    public boolean isItemValid(ItemStack itemstack)
    {
        return false;
    }

    @Override
    public void onPickupFromSlot(EntityPlayer player, ItemStack itemstack)
    {
        super.onPickupFromSlot(player, itemstack);
        doCrafting();
        updateAvailability();
    }

    @Override
    public ItemStack decrStackSize(int i)
    {
        updateAvailability();

        if (available && product != null)
        {
            ItemStack output = product.copy();

            if ((GameInfo.isCreative(thePlayer) || survivalCheating) && GameInfo.isPlacingReversed(thePlayer))
            {
                output.stackSize = output.getMaxStackSize();
            }

            return output;
        }

        return null;
    }

    @Override
    public ItemStack getStack()
    {
        updateAvailability();

        if (available && product != null)
        {
            ItemStack output = product.copy();

            if ((GameInfo.isCreative(thePlayer) || survivalCheating) && GameInfo.isPlacingReversed(thePlayer))
            {
                output.stackSize = output.getMaxStackSize();
            }

            return output;
        }

        return null;
    }

    @Override
    public void putStack(ItemStack itemstack) {}

    public void setProduct(ItemStack itemstack)
    {
        product = itemstack;
    }

    @Override
    public void onSlotChanged() {}

    @Override
    public int getSlotStackLimit()
    {
        if (product == null)
        {
            return 1;
        }

        return product.getMaxStackSize();
    }

    private boolean isAvailable()
    {
        if (product == null)
        {
            return false;
        }

        if (GameInfo.isCreative(thePlayer) || survivalCheating)
        {
            return true;
        }

        lastRecipe = -1;
        int recipe_number = -1;

        while (true)
        {
            lastRecipe = recipe_number;
            IRecipe irecipe = getNextRecipeForProduct(product);
            recipe_number = lastRecipe;

            if (irecipe == null)
            {
                return false;
            }

            recursionCount = 0;
            allocatedStacks.clear();
            toConsume.clear();
            toGiveBack.clear();
            stacksSeekedInThisRecursion.clear();
            recipeBannedStacks.clear();
            recipeBannedStacks.add(getStackDescriptor(product));
            recipeBannedStacks.add(getStackDescriptorAnyMeta(product));

            if (tryToFindMaterialsForRecipe(irecipe))
            {
                if (toConsume.size() == 1 && toConsume.get(0).isItemEqual(product)
                        || (toConsume.get(0).itemID == product.itemID && toConsume.get(0).getItemDamage() == -1))
                {
                    continue;
                }

                return true;
            }
        }
    }

    private void doCrafting()
    {
        if (product == null)
        {
            return;
        }

        if (GameInfo.isCreative(thePlayer) || survivalCheating)
        {
            return;
        }

        lastRecipe = -1;
        int recipe_number = -1;

        while (true)
        {
            lastRecipe = recipe_number;
            IRecipe irecipe = getNextRecipeForProduct(product);
            recipe_number = lastRecipe;

            if (irecipe == null)
            {
                return;
            }

            recursionCount = 0;
            allocatedStacks.clear();
            toConsume.clear();
            toGiveBack.clear();
            stacksSeekedInThisRecursion.clear();
            recipeBannedStacks.clear();
            recipeBannedStacks.add(getStackDescriptor(product));
            recipeBannedStacks.add(getStackDescriptorAnyMeta(product));

            if (tryToFindMaterialsForRecipe(irecipe))
            {
                if (toConsume.size() == 1 && toConsume.get(0).isItemEqual(product)
                        || (toConsume.get(0).itemID == product.itemID && toConsume.get(0).getItemDamage() == -1))
                {
                    continue;
                }

                for (ItemStack stack : toConsume)
                {
                    consumePlayerItems(stack, stack.stackSize);
                }

                if (irecipe.getRecipeOutput().stackSize > product.stackSize)
                {
                    toGiveBack.add(new ItemStack(irecipe.getRecipeOutput().itemID, irecipe.getRecipeOutput().stackSize - product.stackSize, product
                            .getItemDamage()));
                }

                for (ItemStack stack : toGiveBack)
                {
                    thePlayer.inventory.addItemStackToInventory(stack);

                    if (stack.stackSize > 0)
                    {
                        thePlayer.dropPlayerItem(stack);
                    }
                }

                return;
            }
        }
    }

    private String getStackDescriptor(ItemStack stack)
    {
        return Item.itemsList[stack.itemID].getItemName() + "@" + stack.getItemDamage();
    }

    private String getStackDescriptorAnyMeta(ItemStack stack)
    {
        return Item.itemsList[stack.itemID].getItemName() + "@-1";
    }

    private Hashtable<String, Integer> allocatedStacks = new Hashtable<String, Integer>();

    private boolean allocatePlayerItems(ItemStack stack1, int needed)
    {
        if (stack1 == null)
        {
            return true;
        }

        if (recipeBannedStacks.contains(getStackDescriptor(stack1)))
        {
            return false;
        }

        Integer alloc = allocatedStacks.get(getStackDescriptor(stack1));

        if (alloc == null)
        {
            alloc = 0;
        }

        if (GameInfo.isCreative(thePlayer) || survivalCheating)
        {
            return true;
        }

        int counter = 0;

        for (int i = 0; i < thePlayer.inventory.getSizeInventory(); i++)
        {
            ItemStack curStack = thePlayer.inventory.getStackInSlot(i);

            if (curStack != null && (curStack.isItemEqual(stack1) || (curStack.itemID == stack1.itemID && stack1.getItemDamage() == -1)))
            {
                counter += curStack.stackSize;

                if (counter - alloc >= needed)
                {
                    allocatedStacks.put(getStackDescriptor(stack1), alloc + needed);
                    return true;
                }
            }
        }

        if (counter - alloc >= needed)
        {
            allocatedStacks.put(getStackDescriptor(stack1), alloc + needed);
            return true;
        }

        return false;
    }

    private int countPlayerItems(ItemStack wanted)
    {
        if (wanted == null)
        {
            return 0;
        }

        if (recipeBannedStacks.contains(getStackDescriptor(wanted)))
        {
            return 0;
        }

        Integer alloc = allocatedStacks.get(getStackDescriptor(wanted));

        if (alloc == null)
        {
            alloc = 0;
        }

        if (GameInfo.isCreative(thePlayer) || survivalCheating)
        {
            return -1;
        }

        int counter = 0;

        for (int i = 0; i < thePlayer.inventory.getSizeInventory(); i++)
        {
            ItemStack curStack = thePlayer.inventory.getStackInSlot(i);

            if (curStack != null && (curStack.isItemEqual(wanted) || (curStack.itemID == wanted.itemID && wanted.getItemDamage() == -1)))
            {
                counter += curStack.stackSize;
            }
        }

        return counter - alloc;
    }

    private boolean consumePlayerItems(ItemStack stack1, int count)
    {
        if (GameInfo.isCreative(thePlayer) || survivalCheating)
        {
            return true;
        }

        if (stack1 == null)
        {
            return true;
        }

        for (int i = 0; i < thePlayer.inventory.getSizeInventory(); i++)
        {
            ItemStack curStack = thePlayer.inventory.getStackInSlot(i);

            if (curStack != null && (curStack.isItemEqual(stack1) || (curStack.itemID == stack1.itemID && stack1.getItemDamage() == -1)))
            {
                if (curStack.stackSize > count)
                {
                    curStack.stackSize -= count;
                    return true;
                }
                else if (curStack.stackSize <= count)
                {
                    count -= curStack.stackSize;
                    thePlayer.inventory.setInventorySlotContents(i, null);
                }
            }
        }

        if (count > 0)
        {
            return false;
        }

        return true;
    }

    private int lastRecipe = -1;

    private IRecipe getNextRecipeForProduct(ItemStack prod)
    {
        List<IRecipe> recipes = new ArrayList<IRecipe>(CraftingManager.getInstance().getRecipeList());

        if (lastRecipe == recipes.size() - 1)
        {
            return null;
        }

        int k;

        for (k = lastRecipe + 1; k < recipes.size(); k++)
        {
            IRecipe irecipe = recipes.get(k);

            try
            {
                if (irecipe.getRecipeOutput().isItemEqual(prod) || (irecipe.getRecipeOutput().itemID == prod.itemID && prod.getItemDamage() == -1))
                {
                    lastRecipe = k;
                    return irecipe;
                }
            }
            catch (NullPointerException npe)
            {
                continue;
            }
        }

        lastRecipe = k;
        return null;
    }

    private int recursionCount = 0;

    private ArrayList<String> stacksSeekedInThisRecursion = new ArrayList<String>();

    private ArrayList<String> recipeBannedStacks = new ArrayList<String>();

    private ArrayList<ItemStack> toConsume = new ArrayList<ItemStack>();

    private ArrayList<ItemStack> toGiveBack = new ArrayList<ItemStack>();

    private boolean tryToFindMaterialsForRecipe(IRecipe irecipe)
    {
        if (GameInfo.isCreative(thePlayer) || survivalCheating)
        {
            return true;
        }

        recursionCount++;

        if (recursionCount > RECURSION_LIMIT)
        {
            return false;
        }

        try
        {
            ItemStack[] tmps = GameInfo.getExpectedInput(irecipe);

            if(tmps==null)
            {
                return false;
            }

            ItemStack[] recipeStacks = new ItemStack[tmps.length];

            for (int i = 0; i < tmps.length; i++)
            {
                if (tmps[i] != null)
                {
                    recipeStacks[i] = tmps[i].copy();
                }
            }

            for (int i = 0; i < recipeStacks.length; i++)
            {
                if (recipeStacks[i] != null)
                {
                    recipeStacks[i].stackSize = 1;

                    for (int j = i + 1; j < recipeStacks.length; j++)
                    {
                        if (recipeStacks[j] != null && recipeStacks[j].isItemEqual(recipeStacks[i]))
                        {
                            recipeStacks[i].stackSize++;
                            recipeStacks[j] = null;
                        }
                    }
                }
            }

            recloop:

            for (int i = 0; i < recipeStacks.length; i++)
            {
                if (recipeStacks[i] == null)
                {
                    continue recloop;
                }

                if (!allocatePlayerItems(recipeStacks[i], recipeStacks[i].stackSize))
                {
                    if (!recursiveCrafting)
                    {
                        return false;
                    }

                    if (stacksSeekedInThisRecursion.contains(getStackDescriptor(recipeStacks[i])))
                    {
                        return false;
                    }

                    if (recipeBannedStacks.contains(getStackDescriptor(recipeStacks[i])))
                    {
                        return false;
                    }

                    if (recursionCount > RECURSION_LIMIT)
                    {
                        return false;
                    }

                    ItemStack needed = recipeStacks[i].copy();
                    int availableDirectly = countPlayerItems(needed);

                    if (availableDirectly > 0)
                    {
                        needed.stackSize -= availableDirectly;
                        Integer alloc = allocatedStacks.get(getStackDescriptor(needed));

                        if (alloc == null)
                        {
                            alloc = 0;
                        }

                        allocatedStacks.put(getStackDescriptor(needed), alloc + availableDirectly);
                        ItemStack consumed = new ItemStack(needed.itemID, availableDirectly, needed.getItemDamage());
                        toConsume.add(consumed);

                        if (needed.stackSize <= 0)
                        {
                            continue recloop;
                        }
                    }

                    boolean anymeta = false;
                    int tmpmeta = 0;

                    if (needed.getItemDamage() < 0)
                    {
                        needed.setItemDamage(tmpmeta);
                        anymeta = true;
                    }

                    int recipe_number = -1;

                    while (true)
                    {
                        lastRecipe = recipe_number;
                        IRecipe irecipe2 = getNextRecipeForProduct(needed);
                        recipe_number = lastRecipe;

                        if (irecipe2 == null)
                        {
                            if (anymeta && tmpmeta <= 15)
                            {
                                needed.setItemDamage(++tmpmeta);
                                recipe_number = -1;
                                continue;
                            }
                            else
                            {
                                return false;
                            }
                        }

                        innerloop:

                        while (true)
                        {
                            stacksSeekedInThisRecursion.add(getStackDescriptor(recipeStacks[i]));

                            if (tryToFindMaterialsForRecipe(irecipe2))
                            {
                                stacksSeekedInThisRecursion.remove(getStackDescriptor(recipeStacks[i]));
                                needed.stackSize -= irecipe2.getRecipeOutput().stackSize;

                                if (needed.stackSize > 0)
                                {
                                    continue innerloop;
                                }
                                else if (needed.stackSize < 0)
                                {
                                    ItemStack got = needed.copy();
                                    got.stackSize = Math.abs(got.stackSize);

                                    if (got.getItemDamage() < 0)
                                    {
                                        got.setItemDamage(0);
                                    }

                                    toGiveBack.add(got);
                                }

                                continue recloop;
                            }
                            else
                            {
                                break;
                            }
                        }
                    }
                }
                else
                {
                    toConsume.add(recipeStacks[i]);
                }
            }

            return true;
        }
        catch (IllegalArgumentException et)
        {
            et.printStackTrace();
        }
        catch (SecurityException es)
        {
            es.printStackTrace();
        }

        return false;
    }

    public void updateAvailability()
    {
        available = isAvailable();
    }

    @Override
    public ItemStack getBackgroundStack()
    {
        return product;
    }

    @Override
    public Slot setBackgroundStack(ItemStack stack)
    {
        product = stack.copy();
        return this;
    }

    @Override
    public boolean renderTooltipWhenEmpty()
    {
        return true;
    }

    @Override
    public boolean renderGrayWhenEmpty()
    {
        return true;
    }
}
