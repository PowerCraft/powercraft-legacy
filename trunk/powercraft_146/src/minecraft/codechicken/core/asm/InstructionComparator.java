package codechicken.core.asm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.LineNumberNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.objectweb.asm.tree.VarInsnNode;

import codechicken.core.asm.ObfuscationManager.MethodMapping;

public class InstructionComparator
{
	public static boolean varInsnEqual(AbstractInsnNode absnode, VarInsnNode varnode)
	{
		if(!(absnode instanceof VarInsnNode) || absnode.getOpcode() != varnode.getOpcode())
			return false;
		
		return ((VarInsnNode)absnode).var == varnode.var;
	}
	
	public static boolean methodInsnEqual(AbstractInsnNode absnode, int Opcode, MethodMapping method)
	{
		if(!(absnode instanceof MethodInsnNode) || absnode.getOpcode() != Opcode)
			return false;
		
		return method.matches((MethodInsnNode)absnode);
	}
	
	public static boolean methodInsnEqual(AbstractInsnNode absnode, MethodInsnNode methodnode)
	{
		if(!(absnode instanceof MethodInsnNode) || absnode.getOpcode() != methodnode.getOpcode())
			return false;
		
		MethodInsnNode absmethodnode = (MethodInsnNode)absnode;
		return absmethodnode.owner.equals(methodnode.owner) && absmethodnode.name.equals(methodnode.name) && absmethodnode.desc.equals(methodnode.desc);
	}
	
	public static boolean ldcInsnEqual(AbstractInsnNode absnode, LdcInsnNode ldcnode)
	{
		if(!(absnode instanceof LdcInsnNode) || absnode.getOpcode() != ldcnode.getOpcode())
			return false;
		
		LdcInsnNode absldcnode = (LdcInsnNode)absnode;
		if(absldcnode.cst.equals("~") || ldcnode.cst.equals("~"))
			return true;
		return absldcnode.cst.equals(ldcnode.cst);
	}
	
	public static boolean typeInsnEqual(AbstractInsnNode absnode, TypeInsnNode typenode)
	{
		if(!(absnode instanceof TypeInsnNode) || absnode.getOpcode() != typenode.getOpcode())
			return false;
		
		TypeInsnNode abstypenode = (TypeInsnNode)absnode;
		if(abstypenode.desc.equals("~") || typenode.desc.equals("~"))
			return true;
		return abstypenode.desc.equals(typenode.desc);
	}
	
	public static boolean insnEqual(AbstractInsnNode node1, AbstractInsnNode node2)
	{
		if(node2 instanceof VarInsnNode)
			return varInsnEqual(node1, (VarInsnNode) node2);
		else if(node2 instanceof MethodInsnNode)
			return methodInsnEqual(node1, (MethodInsnNode) node2);
		else if(node2 instanceof LdcInsnNode)
			return ldcInsnEqual(node1, (LdcInsnNode) node2);
		else if(node2 instanceof TypeInsnNode)
			return typeInsnEqual(node1, (TypeInsnNode) node2);
		else
			return node1.getOpcode() == node2.getOpcode();
	}
	
	public static InsnList getImportantList(InsnList list)
	{
		if(list.size() == 0)
			return list;
		
		HashMap<LabelNode, LabelNode> labelMap = new HashMap<LabelNode, LabelNode>();
		for(AbstractInsnNode insnnode = list.getFirst(); insnnode.getNext() != null; insnnode = insnnode.getNext())
		{
			if(insnnode instanceof LabelNode)
				labelMap.put((LabelNode)insnnode, (LabelNode)insnnode);
		}		
		
		InsnList importantNodeList = new InsnList();
		for(AbstractInsnNode insnnode = list.getFirst(); insnnode.getNext() != null; insnnode = insnnode.getNext())
		{
			if(insnnode instanceof LabelNode || insnnode instanceof LineNumberNode)
				continue;
			
			importantNodeList.add(insnnode.clone(labelMap));
		}
		return importantNodeList;
	}
	
	public static boolean insnListMatches(InsnList haystack, InsnList needle, int start)
	{
		if(haystack.size()-start < needle.size())
			return false;
		
		for(int i = 0; i < needle.size(); i++)
		{
			if(!insnEqual(haystack.get(i+start), needle.get(i)))
				return false;
		}
		return true;
	}
	
	public static List<Integer> insnListFind(InsnList haystack, InsnList needle)
	{
		LinkedList<Integer> list = new LinkedList<Integer>();
		for(int start = 0; start < haystack.size()-needle.size(); start++)
			if(insnListMatches(haystack, needle, start))
				list.add(start);
		
		return list;
	}
	
    public static List<AbstractInsnNode> insnListFindStart(InsnList haystack, InsnList needle)
    {
        LinkedList<AbstractInsnNode> callNodes = new LinkedList<AbstractInsnNode>();
        for(int callPoint : insnListFind(haystack, needle))
            callNodes.add(haystack.get(callPoint));
        return callNodes;
    }
    
    public static List<AbstractInsnNode> insnListFindEnd(InsnList haystack, InsnList needle)
    {
        LinkedList<AbstractInsnNode> callNodes = new LinkedList<AbstractInsnNode>();
        for(int callPoint : insnListFind(haystack, needle))
            callNodes.add(haystack.get(callPoint+needle.size()-1));
        return callNodes;
    }
}
