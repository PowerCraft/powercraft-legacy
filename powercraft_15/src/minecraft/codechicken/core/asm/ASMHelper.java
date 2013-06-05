package codechicken.core.asm;

import java.util.HashMap;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import codechicken.core.asm.InstructionComparator.InsnListSection;
import codechicken.core.asm.ObfuscationMappings.DescriptorMapping;

public class ASMHelper
{    
    public static class CodeBlock
    {
        public Label start = new Label();
        public Label end = new Label();
    }
    
    public static class ForBlock extends CodeBlock
    {
        public Label cmp = new Label();
        public Label inc = new Label();
        public Label body = new Label();
    }
    
    public static abstract class MethodAltercator
    {
        public final DescriptorMapping method;
        
        public MethodAltercator(DescriptorMapping method)
        {
            this.method = method;
        }
        
        public abstract void alter(MethodNode mv);
    }
    
    public static abstract class MethodWriter
    {
        public final int access;
        public final DescriptorMapping method;
        public final String[] exceptions;

        public MethodWriter(int access, DescriptorMapping method)
        {
            this(access, method, null);
        }
        
        public MethodWriter(int access, DescriptorMapping method, String[] exceptions)
        {
            this.access = access;
            this.method = method;
            this.exceptions = exceptions;
        }

        public abstract void write(MethodNode mv);
    }
    
    public static class MethodInjector
    {
        public final DescriptorMapping method;
        public final InsnList needle;
        public final InsnList injection;
        public final boolean before;
        
        public MethodInjector(DescriptorMapping method, InsnList needle, InsnList injection, boolean before)
        {
            this.method = method;
            this.needle = needle;
            this.injection = injection;
            this.before = before;
        }
    }
    
    public static MethodNode findMethod(DescriptorMapping methodmap, ClassNode cnode)
    {
        for(MethodNode mnode : cnode.methods)
            if(methodmap.matches(mnode))
                return mnode;
        return null;
    }

    public static FieldNode findField(DescriptorMapping fieldmap, ClassNode cnode)
    {
        for(FieldNode fnode : cnode.fields)
            if(fieldmap.matches(fnode))
                return fnode;
        return null;
    }

    public static ClassNode createClassNode(byte[] bytes)
    {
        ClassNode cnode = new ClassNode();
        ClassReader reader = new ClassReader(bytes);
        reader.accept(cnode, 0);
        return cnode;
    }

    public static byte[] createBytes(ClassNode cnode, int i)
    {
        ClassWriter cw = new CC_ClassWriter(i);
        cnode.accept(cw);
        return cw.toByteArray();
    }
    
    public static byte[] writeMethods(String name, byte[] bytes, Multimap<String, MethodWriter> writers)
    {
        if(writers.containsKey(name))
        {
            ClassNode cnode = ASMHelper.createClassNode(bytes);
            
            for(MethodWriter mw : writers.get(name))
            {
                MethodNode mv = ASMHelper.findMethod(mw.method, cnode);
                if(mv == null)
                    mv = (MethodNode) cnode.visitMethod(mw.access, mw.method.s_name, mw.method.s_desc, null, mw.exceptions);

                mv.access = mw.access;
                mv.instructions.clear();
                mw.write(mv);
            }

            bytes = ASMHelper.createBytes(cnode, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        }
        return bytes;
    }
    
    public static byte[] injectMethods(String name, byte[] bytes, Multimap<String, MethodInjector> injectors)
    {
        if(injectors.containsKey(name))
        {
            ClassNode cnode = ASMHelper.createClassNode(bytes);
            
            for(MethodInjector injector : injectors.get(name))
            {
                MethodNode method = ASMHelper.findMethod(injector.method, cnode);
                if(method == null)
                    throw new RuntimeException("Method not found: "+injector.method);
                System.out.println("Injecting into " + injector.method + "\n" + printInsnList(injector.injection));
                
                List<AbstractInsnNode> callNodes;
                if(injector.before)
                    callNodes = InstructionComparator.insnListFindStart(method.instructions, injector.needle);
                else
                    callNodes = InstructionComparator.insnListFindEnd(method.instructions, injector.needle);
                
                if(callNodes.size() == 0)
                {
                    throw new RuntimeException("Needle not found in Haystack: " + injector.method+"\n" + printInsnList(injector.needle));
                }
                
                for(AbstractInsnNode node : callNodes)
                {
                    if(injector.before)
                    {
                        System.out.println("Injected before: "+printInsn(node));
                        method.instructions.insertBefore(node, clone(injector.injection));
                    }
                    else
                    {
                        System.out.println("Injected after: "+printInsn(node));
                        method.instructions.insert(node, clone(injector.injection));
                    }
                }
            }

            bytes = ASMHelper.createBytes(cnode, ClassWriter.COMPUTE_FRAMES);
        }
        return bytes;
    }
    
    public static String printInsnList(InsnList list)
    {
        InsnListPrinter p =  new InsnListPrinter();
        p.visitInsnList(list);
        return p.textString();
    }
    
    public static String printInsn(AbstractInsnNode insn)
    {
        InsnListPrinter p = new InsnListPrinter();
        p.visitInsn(insn);
        return p.textString();
    }
    
    public static InsnList clone(InsnList list)
    {
        HashMap<LabelNode, LabelNode> labels = new HashMap<LabelNode, LabelNode>();
        for(AbstractInsnNode insn = list.getFirst(); insn != null; insn = insn.getNext())
            if(insn.getType() == 8)
                labels.put((LabelNode) insn, new LabelNode());
        
        InsnList clone = new InsnList();
        for(AbstractInsnNode insn = list.getFirst(); insn != null; insn = insn.getNext())
            clone.add(insn.clone(labels));
        
        return clone;
    }

    public static byte[] alterMethods(String name, byte[] bytes, HashMultimap<String, MethodAltercator> altercators)
    {
        if(altercators.containsKey(name))
        {
            ClassNode cnode = ASMHelper.createClassNode(bytes);
            
            for(MethodAltercator injector : altercators.get(name))
            {
                MethodNode method = ASMHelper.findMethod(injector.method, cnode);
                if(method == null)
                    throw new RuntimeException("Method not found: "+injector.method);
                
                injector.alter(method);
            }
            
            bytes = ASMHelper.createBytes(cnode, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        }
        return bytes;
    }
    

    public static String printInsnList(InsnListSection subsection)
    {
        InsnListPrinter p =  new InsnListPrinter();
        p.visitInsnList(subsection);
        return p.textString();
    }

    public static int getLocal(List<LocalVariableNode> list, String name)
    {
        int found = -1;
        for(LocalVariableNode node : list)
        {
            if(node.name.equals(name))
            {
                if(found >= 0)
                    throw new RuntimeException("Duplicate local variable: "+name+" not coded to handle this scenario.");
                
                found = node.index;
            }
        }
        return found;
    }

    public static void replaceMethodCode(MethodNode original, MethodNode replacement)
    {
        original.instructions.clear();
        if(original.localVariables != null)
            original.localVariables.clear();
        if(original.tryCatchBlocks != null)
            original.tryCatchBlocks.clear();
        replacement.accept(original);
    }
}
