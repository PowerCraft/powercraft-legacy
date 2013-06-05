package codechicken.core.asm;

import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;

import cpw.mods.fml.common.asm.transformers.deobf.FMLDeobfuscatingRemapper;
import cpw.mods.fml.relauncher.IClassTransformer;

public class InterfaceDependancyTransformer implements IClassTransformer
{    
    @Override
    public byte[] transform(String name, String tname, byte[] bytes)
    {
        ClassNode cnode = ASMHelper.createClassNode(bytes);
        
        boolean hasDependancyInterfaces = false;
        if(cnode.visibleAnnotations != null)
        {
            for(AnnotationNode ann : cnode.visibleAnnotations)
            {
                if(ann.desc.equals(Type.getDescriptor(InterfaceDependancies.class)))
                {
                    hasDependancyInterfaces = true;
                    break;
                }
            }
        }
        
        if(!hasDependancyInterfaces)
            return bytes;
        
        hasDependancyInterfaces = false;
        for(Iterator<String> iterator = cnode.interfaces.iterator(); iterator.hasNext();)
        {
            try
            {
                String s = iterator.next();
                s = FMLDeobfuscatingRemapper.INSTANCE.map(s);
                CodeChickenCorePlugin.cl.findClass(s.replace('/', '.'));
            }
            catch(ClassNotFoundException cnfe)
            {
                iterator.remove();
                hasDependancyInterfaces = true;
            }
        }
        
        if(!hasDependancyInterfaces)
            return bytes;
        
        return ASMHelper.createBytes(cnode, 0);
    }
}
