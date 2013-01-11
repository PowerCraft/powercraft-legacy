package codechicken.core.asm;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import codechicken.core.asm.ObfuscationManager.MethodMapping;

import cpw.mods.fml.common.asm.ASMTransformer;

public class FeatureHackTransformer extends ASMTransformer implements Opcodes
{	
	public FeatureHackTransformer()
	{
	}
	
	/**
	 * Allow GameData to hide some items.
	 */
    MethodMapping m_newItemAdded = new MethodMapping("cpw.mods.fml.common.registry.GameData", "newItemAdded", "(Lnet/minecraft/item/Item;)V");
	public byte[] transform001(String name, byte[] bytes)
	{
	    if(m_newItemAdded.isClass(name))
        {
            ClassNode cnode = ASMHelper.createClassNode(bytes);
            MethodNode mnode = ASMHelper.findMethod(m_newItemAdded, cnode);
            
            InsnList overrideList = new InsnList();
            LabelNode label = new LabelNode();
            overrideList.add(new MethodInsnNode(INVOKESTATIC, "codechicken/core/featurehack/GameDataManipulator", "override", "()Z"));
            overrideList.add(new JumpInsnNode(IFEQ, label));
            overrideList.add(new InsnNode(RETURN));
            overrideList.add(label);
            mnode.instructions.insert(mnode.instructions.get(1), overrideList);
            
            bytes = ASMHelper.createBytes(cnode, ClassWriter.COMPUTE_FRAMES|ClassWriter.COMPUTE_MAXS);
            System.out.println("Inserted GameData hook");
        }
	    return bytes;
	}
	
	@Override
	public byte[] transform(String name, byte[] bytes)
	{
	    bytes = transform001(name, bytes);
		return bytes;
	}
}
