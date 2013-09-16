package powercraft.extendedEnchant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class PC_EEClassTransformer implements net.minecraft.launchwrapper.IClassTransformer
{
	private static ArrayList<String> enchantmentclasses_obs = new ArrayList<String>(Arrays.asList("aam", 
			"aan",	"aao",	"aap", "aaq", "aar", "aas", "abb",	"abc", "abd", "abe", "abf", "abg", "abh", "abi"));
	private static ArrayList<String> enchantmentclasses_dev = new ArrayList<String>(Arrays.asList(
			"net.minecraft.enchantment.EnchantmentArrowDamage",
			"net.minecraft.enchantment.EnchantmentArrowFire",
			"net.minecraft.enchantment.EnchantmentArrowInfinite",
			"net.minecraft.enchantment.EnchantmentArrowKnockback",
			"net.minecraft.enchantment.EnchantmentDamage",
			"net.minecraft.enchantment.EnchantmentDurability",
			"net.minecraft.enchantment.EnchantmentDigging",
			"net.minecraft.enchantment.EnchantmentFireAspect",
			"net.minecraft.enchantment.EnchantmentKnockback",
			"net.minecraft.enchantment.EnchantmentLootBonus",
			"net.minecraft.enchantment.EnchantmentOxygen",
			"net.minecraft.enchantment.EnchantmentProtection",
			"net.minecraft.enchantment.EnchantmentThorns",
			"net.minecraft.enchantment.EnchantmentUntouching",
			"net.minecraft.enchantment.EnchantmentWaterWorker"));
	private static int[] vanillaEnchantValues = 	{ 5, 1,1,2, 5,3,5, 2,2,3,3,4, 3,1,1 };
	private static int[] newEnchantvalues = 		{ 10,1,1,5,10,5,10,5,5,5,3,10,5,1,1 };
			
	private static final HashMap<Integer, Integer> intbytecodes;
	static
	{
		intbytecodes = new HashMap<Integer, Integer>();
		intbytecodes.put(1, Opcodes.ICONST_1);
		intbytecodes.put(2, Opcodes.ICONST_2);
		intbytecodes.put(3, Opcodes.ICONST_3);
		intbytecodes.put(4, Opcodes.ICONST_4);
		intbytecodes.put(5, Opcodes.ICONST_5);
		intbytecodes.put(10, Opcodes.BIPUSH);
	}
	
	@Override
	public byte[] transform(String classname, String arg1, byte[] bytearray)
	{
		int enchantedindex = 0;
		
		// check for obsfucated classname
		if (enchantmentclasses_obs.contains(classname))
		{
			System.out.println("********* INSIDE EnchantmentDigging Obsfucated TRANSFORMER ABOUT TO PATCH: " + classname);
			enchantedindex = enchantmentclasses_obs.indexOf(classname);
			return patchClassASM(classname, bytearray, true, enchantedindex);
		}
		else if (enchantmentclasses_dev.contains(classname))
		{
			System.out.println("********* INSIDE EnchantmentDigging Developer TRANSFORMER ABOUT TO PATCH: " + classname);
			enchantedindex = enchantmentclasses_dev.indexOf(classname);
			return patchClassASM(classname, bytearray, false, enchantedindex);
		}
		return bytearray;
	}

	@SuppressWarnings("unused")
	public byte[] patchClassASM(String name, byte[] bytes, boolean obfuscated, int enchantmentindex)
	{		
		
		
		String targetMethodName = "";

		if (obfuscated == true)
			targetMethodName = "b";
		else
			targetMethodName = "getMaxLevel";

		// set up ASM class manipulation stuff. Consult the ASM docs for details
		ClassNode classNode = new ClassNode();
		ClassReader classReader = new ClassReader(bytes);
		classReader.accept(classNode, 0);

		// Now we loop over all of the methods declared inside the Explosion class until we get to the targetMethodName "doExplosionB"

		// find method to inject into
		Iterator<MethodNode> methods = classNode.methods.iterator();
		while (methods.hasNext())
		{
			MethodNode m = methods.next();
			System.out.println("********* Method Name: " + m.name + " Desc:" + m.desc);
			int fdiv_index = -1;

			// Check if this is getMaxLevel and it's method signature is ()I which means that it accepts a nothing () and returns an int I
			if ((m.name.equals(targetMethodName) && m.desc.equals("()I")))
			{
				System.out.println("********* Inside target method!");

				AbstractInsnNode currentNode = null;
				AbstractInsnNode targetNode = null;

				Iterator<AbstractInsnNode> iter = m.instructions.iterator();

				int index = -1;

				// Loop over the instruction set and find the instruction  which does the division of 1/explosionSize
				while (iter.hasNext())
				{
					index++;
					currentNode = iter.next();
					System.out.println("********* index : " + index + " currentNode.getOpcode() = " + currentNode.getOpcode());

					// Found it! save the index location of instruction ICONST_5 and the node for this instruction
					if (currentNode.getOpcode() == intbytecodes.get(vanillaEnchantValues[enchantmentindex]))
					{
						targetNode = currentNode;
						fdiv_index = index;
					}
				}

				/*
				 * 2013-09-14 21:02:12 [INFO] [STDOUT] ********* index : 0 currentNode.getOpcode() = -1 
				 * 2013-09-14 21:02:12 [INFO] [STDOUT] ********* index : 1 currentNode.getOpcode() = -1 
				 * 2013-09-14 21:02:12 [INFO] [STDOUT] ********* index : 2 currentNode.getOpcode() = 8 
				 * 2013-09-14 21:02:12 [INFO] [STDOUT] ********* index : 3 currentNode.getOpcode() = 172 
				 * 2013-09-14 21:02:12 [INFO] [STDOUT] ********* index : 4 currentNode.getOpcode() = -1
				 */

				System.out.println("********* fdiv_index should be 2 -> " + fdiv_index);

				if (targetNode == null)
				{
					System.out.println("Did not find all necessary target nodes! ABANDON CLASS!");
					return bytes;
				}

				if (fdiv_index == -1)
				{
					System.out.println("Did not find all necessary target nodes! ABANDON CLASS!");
					return bytes;
				}

				/*
				 * now we want the save nods that load the variable explosionSize and the division instruction:
				 * 
				 * The instruction we want to modify is thus:
				 * 
				 * mv.visitInsn(ICONST_5);
				 */

				AbstractInsnNode ourNode = m.instructions.get(fdiv_index); // mv.visitInsn(ICONST_5);
				AbstractInsnNode newinst;
				if (vanillaEnchantValues[enchantmentindex] != newEnchantvalues[enchantmentindex])
				{
					if (newEnchantvalues[enchantmentindex] > 5)
					{
						newinst = new IntInsnNode(intbytecodes.get(newEnchantvalues[enchantmentindex]), newEnchantvalues[enchantmentindex]);
					}
					else
					{
						newinst = new InsnNode(intbytecodes.get(newEnchantvalues[enchantmentindex]));
					}			
					// we want to change this node
					m.instructions.set(ourNode, newinst);
					System.out.println("Patching Complete!");
				}
				else
				{
					System.out.println("Patching not needed, new values are the same as old.");
				}				
				break;
			}
		}

		// ASM specific for cleaning up and returning the final bytes for JVM processing.
		ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
		classNode.accept(writer);
		return writer.toByteArray();
	}
}
