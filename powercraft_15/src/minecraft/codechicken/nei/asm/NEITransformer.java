package codechicken.nei.asm;

import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import codechicken.core.asm.ASMHelper;
import codechicken.core.asm.CC_ClassWriter;
import codechicken.core.asm.ClassHeirachyManager;
import codechicken.core.asm.ClassOverrider;
import codechicken.core.asm.ObfuscationMappings.ClassMapping;
import codechicken.core.asm.ObfuscationMappings.DescriptorMapping;
import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.ClassWriter.*;
import static codechicken.core.asm.InstructionComparator.*;

import cpw.mods.fml.relauncher.FMLRelauncher;
import cpw.mods.fml.relauncher.IClassTransformer;

public class NEITransformer implements IClassTransformer
{
    /**
     * Adds super.updateScreen() to non implementing GuiContainer subclasses
     */
    public byte[] transformer001(String name, byte[] bytes)
    {
        ClassMapping classmap = new ClassMapping("net/minecraft/client/gui/inventory/GuiContainer");
        if(ClassHeirachyManager.classExtends(name, classmap.javaClass(), bytes))
        {
            ClassNode cnode = ASMHelper.createClassNode(bytes);

            DescriptorMapping methodmap = new DescriptorMapping("net/minecraft/client/gui/GuiScreen", "updateScreen", "()V");
            DescriptorMapping supermap = new DescriptorMapping(methodmap, cnode.superName);

            InsnList supercall = new InsnList();
            supercall.add(new VarInsnNode(ALOAD, 0));
            supercall.add(supermap.toInsn(INVOKESPECIAL));

            boolean changed = false;
            for(MethodNode methodnode : (List<MethodNode>) cnode.methods)
            {
                if(methodmap.matches(methodnode))
                {
                    InsnList importantNodeList = getImportantList(methodnode.instructions);
                    if(!insnListMatches(importantNodeList, supercall, 0))
                    {
                        methodnode.instructions.insertBefore(methodnode.instructions.getFirst(), supercall);
                        System.out.println("Inserted super call into " + name + "." + supermap.s_name);
                        changed = true;
                    }
                }
            }
            
            if(changed)
                bytes = ASMHelper.createBytes(cnode, COMPUTE_MAXS | COMPUTE_FRAMES);
        }
        return bytes;
    }

    /**
     * Generates method for setting the placed position for the mob spawner item
     */
    public byte[] transformer002(String name, byte[] bytes)
    {
        ClassMapping classmap = new ClassMapping("net/minecraft/block/BlockMobSpawner");
        if(classmap.isClass(name))
        {
            ClassNode cnode = ASMHelper.createClassNode(bytes);

            DescriptorMapping methodmap = new DescriptorMapping("net/minecraft/block/Block", "onBlockPlacedBy", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/EntityLiving;Lnet/minecraft/item/ItemStack;)V");
            MethodNode methodnode = (MethodNode) cnode.visitMethod(ACC_PUBLIC, methodmap.s_name, methodmap.s_desc, null, null);

            methodnode.instructions.add(new VarInsnNode(ILOAD, 2));//param2
            methodnode.instructions.add(new FieldInsnNode(PUTSTATIC, "codechicken/nei/ItemMobSpawner", "placedX", "I"));
            methodnode.instructions.add(new VarInsnNode(ILOAD, 3));//param3
            methodnode.instructions.add(new FieldInsnNode(PUTSTATIC, "codechicken/nei/ItemMobSpawner", "placedY", "I"));
            methodnode.instructions.add(new VarInsnNode(ILOAD, 4));//param4
            methodnode.instructions.add(new FieldInsnNode(PUTSTATIC, "codechicken/nei/ItemMobSpawner", "placedZ", "I"));
            methodnode.instructions.add(new InsnNode(RETURN));
            
            bytes = ASMHelper.createBytes(cnode, COMPUTE_MAXS | COMPUTE_FRAMES);
            System.out.println("Generated BlockMobSpawner helper method.");
        }
        return bytes;
    }

    /**
     * Generates Slot.getBackgroundIconTexture for when forge is not installed.
     */
    public byte[] transformer003(String name, byte[] bytes)
    {
        DescriptorMapping methodmap = new DescriptorMapping("net/minecraft/inventory/Slot", "getBackgroundIconTexture", "()Ljava/lang/String;");
        if(methodmap.isClass(name))
        {
            ClassNode cnode = ASMHelper.createClassNode(bytes);

            boolean declared = false;
            for(MethodNode method : cnode.methods)
            {
                if(methodmap.matches(method))
                {
                    declared = true;
                    break;
                }
            }

            if(!declared)
            {
                ClassWriter cw = new CC_ClassWriter(COMPUTE_FRAMES);
                cnode.accept(cw);

                MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getBackgroundIconTexture", "()Ljava/lang/String;", null, null);
                mv.visitCode();
                mv.visitLdcInsn("/gui/items.png");
                mv.visitInsn(ARETURN);
                mv.visitMaxs(1, 0);
                mv.visitEnd();

                bytes = cw.toByteArray();

                System.out.println("Generated default "+methodmap.s_owner+".getBackgroundIconTexture().");
            }
        }
        return bytes;
    }

    @Override
    public byte[] transform(String name, String tname, byte[] bytes)
    {
        try
        {
            if(FMLRelauncher.side().equals("CLIENT"))
            {
                bytes = transformer001(name, bytes);
                bytes = transformer002(name, bytes);
                bytes = transformer003(name, bytes);
                bytes = ClassOverrider.overrideBytes(name, bytes, new ClassMapping("net/minecraft/client/gui/inventory/GuiContainer"), NEICorePlugin.location);
            }
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }

        return bytes;
    }
}
