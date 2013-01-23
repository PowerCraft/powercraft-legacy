package codechicken.core.asm;

import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.MethodNode;

import com.google.common.collect.Multimap;

import codechicken.core.asm.ObfuscationManager.MethodMapping;

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
    
    public static abstract class MethodWriter
    {
        public final int access;
        public final MethodMapping method;
        public final String[] exceptions;

        public MethodWriter(int access, MethodMapping method)
        {
            this(access, method, null);
        }
        
        public MethodWriter(int access, MethodMapping method, String[] exceptions)
        {
            this.access = access;
            this.method = method;
            this.exceptions = exceptions;
        }

        public abstract void write(MethodNode mv);
    }
    
    public static class MethodInjector
    {
        public final MethodMapping method;
        public final InsnList needle;
        public final InsnList injection;
        public final boolean before;
        
        public MethodInjector(MethodMapping method, InsnList needle, InsnList injection, boolean before)
        {
            this.method = method;
            this.needle = needle;
            this.injection = injection;
            this.before = before;
        }
    }
    
    @SuppressWarnings("unchecked")
    public static MethodNode findMethod(MethodMapping methodmap, ClassNode cnode)
    {
        for(MethodNode mnode : (List<MethodNode>) cnode.methods)
            if(methodmap.matches(mnode))
                return mnode;
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
        ClassWriter cw = new ClassWriter(i);
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
                    mv = (MethodNode) cnode.visitMethod(mw.access, mw.method.name, mw.method.desc, null, mw.exceptions);

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
                
                List<AbstractInsnNode> callNodes;
                if(injector.before)
                    callNodes = InstructionComparator.insnListFindStart(method.instructions, injector.needle);
                else
                    callNodes = InstructionComparator.insnListFindEnd(method.instructions, injector.needle);
                
                if(callNodes.size() == 0)
                    throw new RuntimeException("Needle not found in Haystack: "+injector.method);
                
                for(AbstractInsnNode node : callNodes)
                    if(injector.before)
                        method.instructions.insertBefore(node, injector.injection);
                    else
                        method.instructions.insert(node, injector.injection);
            }

            bytes = ASMHelper.createBytes(cnode, ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        }
        return bytes;
    }
}
