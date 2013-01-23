package codechicken.core.asm;

import java.util.Iterator;
import java.util.List;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import cpw.mods.fml.relauncher.IClassTransformer;

public class InterfaceDependancyTransformer implements IClassTransformer
{	
	@SuppressWarnings("unchecked")
	@Override
	public byte[] transform(String name, byte[] bytes)
	{
		ClassReader cr = new ClassReader(bytes);
        ClassNode classNode = new ClassNode();
        cr.accept(classNode, 0);
        
        boolean hasDependancyInterfaces = false;
        if(classNode.visibleAnnotations != null)
        {
	        for(AnnotationNode ann : (List<AnnotationNode>)classNode.visibleAnnotations)
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
        for(Iterator<String> iterator = classNode.interfaces.iterator(); iterator.hasNext();)
        {
        	try
        	{
        		CodeChickenCorePlugin.cl.findClass(iterator.next().replace('/', '.'));
        	}
        	catch(ClassNotFoundException cnfe)
        	{
        		iterator.remove();
        		hasDependancyInterfaces = true;
        	}
        }
        
        if(!hasDependancyInterfaces)
        	return bytes;
		
        ClassWriter writer = new ClassWriter(0);
        classNode.accept(writer);
        return writer.toByteArray();
	}
}
