package codechicken.nei.recipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.SetMultimap;

import net.minecraft.inventory.Container;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.inventory.Slot;
import codechicken.core.inventory.InventoryUtils;
import codechicken.nei.FastTransferManger;
import codechicken.nei.PositionedStack;
import codechicken.nei.api.IOverlayHandler;

public class DefaultOverlayHandler implements IOverlayHandler
{
    public static class DistributedIngred
    {
        public DistributedIngred(ItemStack item)
        {
            stack = InventoryUtils.copyStack(item, 1);
        }
        
        public ItemStack stack;
        public int invAmount;
        public int distributed;
        public int numSlots;
        public int recipeAmount;
    }
    
    public static class IngredientDistribution
    {
        public IngredientDistribution(DistributedIngred distrib, ItemStack permutation)
        {
            this.distrib = distrib;
            this.permutation = permutation;
        }
        
        public DistributedIngred distrib;
        public ItemStack permutation;
        public Slot[] slots;
    }
    
    public DefaultOverlayHandler(int x, int y)
    {
        offsetx = x;
        offsety = y;
    }
    
    public DefaultOverlayHandler()
    {
        this(5, 11);
    }

    int offsetx;
    int offsety;
    
    @Override
    public void overlayRecipe(GuiContainer gui, List<PositionedStack> ingredients, boolean shift)
    {
        List<DistributedIngred> ingredStacks = getPermutationIngredients(ingredients);
        
        findInventoryQuantities(gui, ingredStacks);
        
        List<IngredientDistribution> assignedIngredients = assignIngredients(ingredients, ingredStacks);
        if(assignedIngredients == null)
            return;
        
        assignIngredSlots(gui, ingredients, assignedIngredients);
        int quantity = calculateRecipeQuantity(assignedIngredients);
        
        if(quantity != 0)
            moveIngredients(gui, assignedIngredients, quantity);             
    }
    
    private void moveIngredients(GuiContainer gui, List<IngredientDistribution> assignedIngredients, int quantity)
    {        
        for(IngredientDistribution distrib : assignedIngredients)
        {
            ItemStack pstack = distrib.permutation;
            int transferCap = quantity*pstack.stackSize;
            int transferred = 0;
            
            int destSlotIndex = 0;
            Slot dest = distrib.slots[0];
            int slotTransferred = 0;
            int slotTransferCap = pstack.getMaxStackSize();
            
            for(Slot slot : (List<Slot>)gui.inventorySlots.inventorySlots)
            {
                if(!slot.getHasStack() || !(slot.inventory instanceof InventoryPlayer))
                    continue;
                
                ItemStack stack = slot.getStack();
                if(!InventoryUtils.canStack(stack, pstack))
                    continue;
                
                FastTransferManger.clickSlot(gui, slot.slotNumber);
                int amount = Math.min(transferCap-transferred, stack.stackSize);
                for(int c = 0; c < amount; c++)
                {
                    FastTransferManger.clickSlot(gui, dest.slotNumber, 1);
                    transferred++;
                    slotTransferred++;
                    if(slotTransferred >= slotTransferCap)
                    {
                        destSlotIndex++;
                        if(destSlotIndex == distrib.slots.length)
                        {
                            dest = null;
                            break;
                        }
                        dest = distrib.slots[destSlotIndex];
                        slotTransferred = 0;
                    }
                }
                FastTransferManger.clickSlot(gui, slot.slotNumber);
                if(transferred >= transferCap || dest == null)
                    break;
            }
        }       
    }

    private int calculateRecipeQuantity(List<IngredientDistribution> assignedIngredients)
    {
        int quantity = Integer.MAX_VALUE;
        for(IngredientDistribution distrib : assignedIngredients)
        {
            DistributedIngred istack = distrib.distrib;
            if(istack.numSlots == 0)
                return 0;
            
            int allSlots = istack.invAmount;
            if(allSlots/istack.numSlots > istack.stack.getMaxStackSize())
                allSlots = istack.numSlots*istack.stack.getMaxStackSize();
            
            quantity = Math.min(quantity, allSlots/istack.distributed);
        }
        
        return quantity;
    }

    private Slot[][] assignIngredSlots(GuiContainer gui, List<PositionedStack> ingredients, List<IngredientDistribution> assignedIngredients)
    {
        Slot[][] recipeSlots = mapIngredSlots(gui, ingredients);//setup the slot map
        
        HashMap<Slot, Integer> distribution = new HashMap<Slot, Integer>();
        for(int i = 0; i < recipeSlots.length; i++)
            for(Slot slot : recipeSlots[i])
                if(!distribution.containsKey(slot))
                    distribution.put(slot, -1);
        
        HashSet<Slot> avaliableSlots = new HashSet<Slot>(distribution.keySet());
        HashSet<Integer> remainingIngreds = new HashSet<Integer>();
        ArrayList<LinkedList<Slot>> assignedSlots = new ArrayList<LinkedList<Slot>>();
        for(int i = 0; i < ingredients.size(); i++)
        {
            remainingIngreds.add(i);
            assignedSlots.add(new LinkedList<Slot>());
        }
                
        while(avaliableSlots.size() > 0 && remainingIngreds.size() > 0)
        {
            for(Iterator<Integer> iterator = remainingIngreds.iterator(); iterator.hasNext();)
            {
                int i = iterator.next();
                boolean assigned = false;
                DistributedIngred istack = assignedIngredients.get(i).distrib;
                
                for(Slot slot : recipeSlots[i])
                {
                    if(avaliableSlots.contains(slot))
                    {
                        avaliableSlots.remove(slot);
                        if(slot.getHasStack())
                            continue;
                        
                        istack.numSlots++;
                        assignedSlots.get(i).add(slot);
                        assigned = true;
                        break;
                    }
                }
                
                if(!assigned || istack.numSlots*istack.stack.getMaxStackSize() >= istack.invAmount)
                    iterator.remove();
            }
        }
        
        for(int i = 0; i < ingredients.size(); i++)
            assignedIngredients.get(i).slots = assignedSlots.get(i).toArray(new Slot[0]);
        return recipeSlots;
    }

    private List<IngredientDistribution> assignIngredients(List<PositionedStack> ingredients, List<DistributedIngred> ingredStacks)
    {
        ArrayList<IngredientDistribution> assignedIngredients = new ArrayList<IngredientDistribution>();
        for(PositionedStack posstack : ingredients)//assign what we need and have
        {
            DistributedIngred biggestIngred = null;
            ItemStack permutation = null;
            int biggestSize = 0;
            for(ItemStack pstack : posstack.items)
            {
                for(int j = 0; j < ingredStacks.size(); j++)
                {
                    DistributedIngred istack = ingredStacks.get(j);
                    if(!InventoryUtils.canStack(pstack, istack.stack) || istack.invAmount-istack.distributed < pstack.stackSize)
                        continue;
                    
                    int relsize = (istack.invAmount-istack.invAmount/istack.recipeAmount*istack.distributed)/pstack.stackSize;
                    if(relsize > biggestSize)
                    {
                        biggestSize = relsize;
                        biggestIngred = istack;
                        permutation = pstack;
                        break;
                    }
                }
            }
            
            if(biggestIngred == null)//not enough ingreds
                return null;

            biggestIngred.distributed+=permutation.stackSize;
            assignedIngredients.add(new IngredientDistribution(biggestIngred, permutation));
        }
        
        return assignedIngredients;
    }

    private void findInventoryQuantities(GuiContainer gui, List<DistributedIngred> ingredStacks)
    {
        for(Slot slot : (List<Slot>)gui.inventorySlots.inventorySlots)//work out how much we have to go round
        {
            if(slot.getHasStack() && slot.inventory instanceof InventoryPlayer)
            {
                ItemStack pstack = slot.getStack();
                DistributedIngred istack = findIngred(ingredStacks, pstack);
                if(istack != null)
                    istack.invAmount+=pstack.stackSize;
            }
        }
    }

    private List<DistributedIngred> getPermutationIngredients(List<PositionedStack> ingredients)
    {
        ArrayList<DistributedIngred> ingredStacks = new ArrayList<DistributedIngred>();
        for(PositionedStack posstack : ingredients)//work out what we need
        {
            for(ItemStack pstack : posstack.items)
            {
                DistributedIngred istack = findIngred(ingredStacks, pstack);
                if(istack == null)
                    ingredStacks.add(istack = new DistributedIngred(pstack));
                istack.recipeAmount+=pstack.stackSize;
            }
        }
        return ingredStacks;
    }

    public Slot[][] mapIngredSlots(GuiContainer gui, List<PositionedStack> ingredients)
    {
        Slot[][] recipeSlotList = new Slot[ingredients.size()][];
        for(int i = 0; i < ingredients.size(); i++)//identify slots
        {
            LinkedList<Slot> recipeSlots = new LinkedList<Slot>();
            PositionedStack pstack = ingredients.get(i);
            for(Slot slot : (List<Slot>)gui.inventorySlots.inventorySlots)
            {
                if(slot.xDisplayPosition == pstack.relx+offsetx && slot.yDisplayPosition == pstack.rely+offsety)
                {
                    recipeSlots.add(slot);
                    break;
                }
            }
            recipeSlotList[i] = recipeSlots.toArray(new Slot[0]);
        }
        return recipeSlotList;
    }

    public void clickSlot(GuiContainer window, int slotIndex, int button, int modifier)
    {
        Container container = window.inventorySlots;
        Slot slot = null;
        if(slotIndex >= 0 && slotIndex < container.inventorySlots.size())
            slot = container.getSlot(slotIndex);

        window.handleMouseClick(slot, slotIndex, button, modifier);
    }

    public DistributedIngred findIngred(List<DistributedIngred> ingredStacks, ItemStack pstack)
    {
        for(DistributedIngred istack : ingredStacks)
            if(InventoryUtils.canStack(pstack, istack.stack))
                return istack;
        return null;
    }
}
