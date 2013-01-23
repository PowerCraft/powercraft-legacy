package codechicken.core.asm;

import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

public class ObfuscationManager
{
	public static class ClassMapping
	{
		public String classname;
		
		public ClassMapping(String classname)
		{
			this.classname = classname;
		}
		
		public boolean matches(ClassNode node)
		{
			return classname.equals(node.name);
		}
	}
	
	public static class MethodMapping
	{
		public String owner;
		public String name;
		public String desc;
		
		public MethodMapping(String declaringclass, String methodname, String descriptor)
		{
			this.owner = declaringclass;
			this.name = methodname;
			this.desc = descriptor;
		}
		
		public MethodMapping(String declaringclass, MethodMapping methodmap)
		{
			this.owner = declaringclass;
			this.name = methodmap.name;
			this.desc = methodmap.desc;
		}

		public boolean matches(MethodNode node)
		{
			return name.equals(node.name) && desc.equals(node.desc);
		}
		
		public boolean matches(MethodInsnNode node)
		{
			return owner.equals(node.owner) && name.equals(node.name) && desc.equals(node.desc);
		}

		public MethodInsnNode toInsn(int Opcode)
		{
			return new MethodInsnNode(Opcode, owner.replace('.', '/'), name, desc);
		}

        public boolean isClass(String classname)
        {
            return classname.replace('/', '.').equals(owner.replace('/', '.'));
        }
	}
	
	public static class FieldMapping
	{
		public String owner;
		public String name;
		public String type;
		
		public FieldMapping(String declaringclass, String fieldname, String type)
		{
			this.owner = declaringclass;
			this.name = fieldname;
			this.type = type;
		}
		
		public boolean matches(FieldNode node)
		{
			return name.equals(node.name) && name.equals(node.desc);
		}

		public FieldInsnNode toInsn(int Opcode)
		{
			return new FieldInsnNode(Opcode, owner.replace('.', '/'), name, type);
		}
	}
	
	public static boolean obfuscated = false;
}
