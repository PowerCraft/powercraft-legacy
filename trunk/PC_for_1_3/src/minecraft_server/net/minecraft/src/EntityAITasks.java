package net.minecraft.src;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class EntityAITasks
{
    private List field_75782_a = new ArrayList();
    private List field_75780_b = new ArrayList();
    private final Profiler field_75781_c;
    private int field_75778_d = 0;
    private int field_75779_e = 3;

    public EntityAITasks(Profiler par1Profiler)
    {
        this.field_75781_c = par1Profiler;
    }

    public void addTask(int par1, EntityAIBase par2EntityAIBase)
    {
        this.field_75782_a.add(new EntityAITaskEntry(this, par1, par2EntityAIBase));
    }

    public void onUpdateTasks()
    {
        ArrayList var1 = new ArrayList();
        Iterator var2;
        EntityAITaskEntry var3;

        if (this.field_75778_d++ % this.field_75779_e == 0)
        {
            var2 = this.field_75782_a.iterator();

            while (var2.hasNext())
            {
                var3 = (EntityAITaskEntry)var2.next();
                boolean var4 = this.field_75780_b.contains(var3);

                if (var4)
                {
                    if (this.func_75775_b(var3) && this.func_75773_a(var3))
                    {
                        continue;
                    }

                    var3.action.resetTask();
                    this.field_75780_b.remove(var3);
                }

                if (this.func_75775_b(var3) && var3.action.shouldExecute())
                {
                    var1.add(var3);
                    this.field_75780_b.add(var3);
                }
            }
        }
        else
        {
            var2 = this.field_75780_b.iterator();

            while (var2.hasNext())
            {
                var3 = (EntityAITaskEntry)var2.next();

                if (!var3.action.continueExecuting())
                {
                    var3.action.resetTask();
                    var2.remove();
                }
            }
        }

        this.field_75781_c.startSection("goalStart");
        var2 = var1.iterator();

        while (var2.hasNext())
        {
            var3 = (EntityAITaskEntry)var2.next();
            this.field_75781_c.startSection(var3.action.getClass().getSimpleName());
            var3.action.startExecuting();
            this.field_75781_c.endSection();
        }

        this.field_75781_c.endSection();
        this.field_75781_c.startSection("goalTick");
        var2 = this.field_75780_b.iterator();

        while (var2.hasNext())
        {
            var3 = (EntityAITaskEntry)var2.next();
            this.field_75781_c.startSection(var3.action.getClass().getSimpleName());
            var3.action.updateTask();
            this.field_75781_c.endSection();
        }

        this.field_75781_c.endSection();
    }

    private boolean func_75773_a(EntityAITaskEntry par1EntityAITaskEntry)
    {
        this.field_75781_c.startSection("canContinue");
        boolean var2 = par1EntityAITaskEntry.action.continueExecuting();
        this.field_75781_c.endSection();
        return var2;
    }

    private boolean func_75775_b(EntityAITaskEntry par1EntityAITaskEntry)
    {
        this.field_75781_c.startSection("canUse");
        Iterator var2 = this.field_75782_a.iterator();

        while (var2.hasNext())
        {
            EntityAITaskEntry var3 = (EntityAITaskEntry)var2.next();

            if (var3 != par1EntityAITaskEntry)
            {
                if (par1EntityAITaskEntry.priority >= var3.priority)
                {
                    if (this.field_75780_b.contains(var3) && !this.areTasksCompatible(par1EntityAITaskEntry, var3))
                    {
                        this.field_75781_c.endSection();
                        return false;
                    }
                }
                else if (this.field_75780_b.contains(var3) && !var3.action.isContinuous())
                {
                    this.field_75781_c.endSection();
                    return false;
                }
            }
        }

        this.field_75781_c.endSection();
        return true;
    }

    /**
     * Returns whether two EntityAITaskEntries can be executed concurrently
     */
    private boolean areTasksCompatible(EntityAITaskEntry par1EntityAITaskEntry, EntityAITaskEntry par2EntityAITaskEntry)
    {
        return (par1EntityAITaskEntry.action.getMutexBits() & par2EntityAITaskEntry.action.getMutexBits()) == 0;
    }
}
