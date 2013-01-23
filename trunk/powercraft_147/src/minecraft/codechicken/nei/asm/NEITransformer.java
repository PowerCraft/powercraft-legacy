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
import codechicken.core.asm.ClassHeirachyManager;
import codechicken.core.asm.ClassOverrider;
import codechicken.core.asm.ObfuscationManager.ClassMapping;
import codechicken.core.asm.ObfuscationManager.MethodMapping;
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
    @SuppressWarnings("unchecked")
    public byte[] transformer001(String name, byte[] bytes)
    {
        ClassMapping classmap = new ClassMapping("net.minecraft.client.gui.inventory.GuiContainer");
        if(ClassHeirachyManager.classExtends(name, classmap.classname, bytes))
        {
            ClassNode node = new ClassNode();
            ClassReader reader = new ClassReader(bytes);
            reader.accept(node, 0);

            MethodMapping methodmap = new MethodMapping("net.minecraft.client.gui.GuiScreen", "updateScreen", "()V");
            MethodMapping supermap = new MethodMapping(node.superName, methodmap);

            InsnList supercall = new InsnList();
            supercall.add(new VarInsnNode(ALOAD, 0));
            supercall.add(supermap.toInsn(INVOKESPECIAL));

            for(MethodNode methodnode : (List<MethodNode>) node.methods)
            {
                if(methodmap.matches(methodnode))
                {
                    InsnList importantNodeList = getImportantList(methodnode.instructions);
                    if(!insnListMatches(importantNodeList, supercall, 0))
                    {
                        methodnode.instructions.insertBefore(methodnode.instructions.getFirst(), supercall);
                        System.out.println("Inserted super call into " + name + "." + supermap.name);
                    }
                }
            }

            ClassWriter writer = new ClassWriter(COMPUTE_FRAMES | COMPUTE_MAXS);
            node.accept(writer);
            bytes = writer.toByteArray();
        }
        return bytes;
    }

    /**
     * Generates method for setting the placed position for the mob spawner item
     */
    public byte[] transformer002(String name, byte[] bytes)
    {
        ClassMapping classmap = new ClassMapping("net.minecraft.block.BlockMobSpawner");
        if(name.equals(classmap.classname))
        {
            ClassNode node = new ClassNode();
            ClassReader reader = new ClassReader(bytes);
            reader.accept(node, 0);

            MethodMapping methodmap = new MethodMapping("net.minecraft.block.Block", "onBlockPlacedBy", "(Lnet/minecraft/world/World;IIILnet/minecraft/entity/EntityLiving;)V");
            MethodNode methodnode = (MethodNode) node.visitMethod(ACC_PUBLIC, methodmap.name, methodmap.desc, null, null);

            methodnode.instructions.add(new VarInsnNode(ILOAD, 2));//param2
            methodnode.instructions.add(new FieldInsnNode(PUTSTATIC, "codechicken/nei/ItemMobSpawner", "placedX", "I"));
            methodnode.instructions.add(new VarInsnNode(ILOAD, 3));//param3
            methodnode.instructions.add(new FieldInsnNode(PUTSTATIC, "codechicken/nei/ItemMobSpawner", "placedY", "I"));
            methodnode.instructions.add(new VarInsnNode(ILOAD, 4));//param4
            methodnode.instructions.add(new FieldInsnNode(PUTSTATIC, "codechicken/nei/ItemMobSpawner", "placedZ", "I"));
            methodnode.instructions.add(new InsnNode(RETURN));

            ClassWriter writer = new ClassWriter(COMPUTE_FRAMES | COMPUTE_MAXS);
            node.accept(writer);
            bytes = writer.toByteArray();

            System.out.println("Generated BlockMobSpawner helper method.");
        }
        return bytes;
    }

    /**
     * Generates Slot.getBackgroundIconTexture for when forge is not installed.
     */
    @SuppressWarnings("unchecked")
    public byte[] transformer003(String name, byte[] bytes)
    {
        ClassMapping classmap = new ClassMapping("net.minecraft.inventory.Slot");
        MethodMapping methodmap = new MethodMapping(classmap.classname, "getBackgroundIconTexture", "()Ljava/lang/String;");
        if(name.equals(methodmap.owner))
        {
            ClassNode node = new ClassNode();
            ClassReader reader = new ClassReader(bytes);
            reader.accept(node, 0);

            boolean declared = false;
            for(MethodNode method : (List<MethodNode>) node.methods)
            {
                if(methodmap.matches(method))
                {
                    declared = true;
                    break;
                }
            }

            if(!declared)
            {
                ClassWriter cw = new ClassWriter(COMPUTE_FRAMES);
                node.accept(cw);

                MethodVisitor mv = cw.visitMethod(ACC_PUBLIC, "getBackgroundIconTexture", "()Ljava/lang/String;", null, null);
                mv.visitCode();
                mv.visitLdcInsn("/gui/items.png");
                mv.visitInsn(ARETURN);
                mv.visitMaxs(1, 0);
                mv.visitEnd();

                bytes = cw.toByteArray();

                System.out.println("Generated default "+classmap.classname+".getBackgroundIconTexture().");
            }
        }
        return bytes;
    }

    @Override
    public byte[] transform(String name, byte[] bytes)
    {
        try
        {
            if(FMLRelauncher.side().equals("CLIENT"))
            {
                bytes = transformer001(name, bytes);
                bytes = transformer002(name, bytes);
                bytes = transformer003(name, bytes);
                bytes = ClassOverrider.overrideBytes(name, bytes, new ClassMapping("net.minecraft.client.gui.inventory.GuiContainer"), NEICorePlugin.location);
            }
        }
        catch(Exception e)
        {
            throw new RuntimeException(e);
        }

        return bytes;
    }
}
