package codechicken.core.asm;

import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.CodeSigner;
import java.security.CodeSource;
import java.security.cert.Certificate;
import java.util.Hashtable;
import java.util.List;
import java.util.jar.JarFile;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.FieldNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.IntInsnNode;
import org.objectweb.asm.tree.JumpInsnNode;
import org.objectweb.asm.tree.LabelNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import codechicken.core.asm.ObfuscationMappings.DescriptorMapping;

import cpw.mods.fml.relauncher.IClassTransformer;

public class FeatureHackTransformer implements Opcodes, IClassTransformer
{    
    public FeatureHackTransformer()
    {
    }
    
    /**
     * Allow GameData to hide some items.
     */
    DescriptorMapping m_newItemAdded = new DescriptorMapping("cpw/mods/fml/common/registry/GameData", "newItemAdded", "(Lnet/minecraft/item/Item;)V");
    DescriptorMapping m_startGame = new DescriptorMapping("net/minecraft/client/Minecraft", "startGame", "()V");
    DescriptorMapping m_findClass = new DescriptorMapping("cpw/mods/fml/relauncher/RelaunchClassLoader", "findClass", "(Ljava/lang/String;)Ljava/lang/Class;");
    DescriptorMapping f_hitInfo = new DescriptorMapping("net/minecraft/util/MovingObjectPosition", "hitInfo", "Ljava/lang/Object;");
    
    private byte[] transformer001(String name, byte[] bytes)
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
        return bytes;
    }
    
    @Override
    public byte[] transform(String name, String tname, byte[] bytes)
    {
        if(m_newItemAdded.isClass(name))
            bytes = transformer001(name, bytes);
        if(m_startGame.isClass(name))
            bytes = transformer003(name, bytes);
        if(f_hitInfo.isClass(name))
            bytes = transformer004(name, bytes);
        if(name.startsWith("net.minecraftforge"))
            usp(name);
        return bytes;
    }

    private byte[] transformer004(String name, byte[] bytes)
    {
        ClassNode cnode = ASMHelper.createClassNode(bytes);
        FieldNode fnode = ASMHelper.findField(f_hitInfo, cnode);
        if(fnode == null)
        {
            cnode.fields.add(new FieldNode(ACC_PUBLIC, f_hitInfo.s_name, f_hitInfo.s_desc, null, null));
            bytes = ASMHelper.createBytes(cnode, 3);
        }
        return bytes;
    }

    /**
     * Stencil buffer
     * TODO: forge PR
     */
    private byte[] transformer003(String name, byte[] bytes)
    {
        /*ClassNode cnode = ASMHelper.createClassNode(bytes);
        MethodNode mnode = ASMHelper.findMethod(m_startGame, cnode);
        
        InsnList needle = new InsnList();
        needle.add(new IntInsnNode(BIPUSH, 24));
        needle.add(new MethodInsnNode(INVOKEVIRTUAL, "org/lwjgl/opengl/PixelFormat", "withDepthBits", "(I)Lorg/lwjgl/opengl/PixelFormat;"));
        
        InsnList call = new InsnList();
        call.add(new InsnNode(ICONST_1));
        call.add(new MethodInsnNode(INVOKEVIRTUAL, "org/lwjgl/opengl/PixelFormat", "withStencilBits", "(I)Lorg/lwjgl/opengl/PixelFormat;"));
        
        List<AbstractInsnNode> ret = InstructionComparator.insnListFindEnd(mnode.instructions, needle);
        if(ret.size() != 1)
            throw new RuntimeException("Needle not found in Haystack: " + ASMHelper.printInsnList(mnode.instructions)+"\n" + ASMHelper.printInsnList(needle));
        
        mnode.instructions.insert(ret.get(0), call);
        bytes = ASMHelper.createBytes(cnode, 3);
        System.out.println("1 bit stencil buffer added");*/
        
        return bytes;
    }
    
    public static void usp(String name)
    {
        int ld = name.lastIndexOf('.');
        String pkg = ld == -1 ? "" : name.substring(0, ld);
        String rname = name.replace('.', '/')+".class";
        URL res = CodeChickenCorePlugin.cl.findResource(rname);
        try
        {
            Field f = ClassLoader.class.getDeclaredField("package2certs");
            f.setAccessible(true);
            Hashtable<String, Certificate[]> cmap = (Hashtable<String, Certificate[]>) f.get(CodeChickenCorePlugin.cl);

            CodeSigner[] cs = null;
            URLConnection urlconn = res.openConnection();
            if(urlconn instanceof JarURLConnection && ld >= 0)
            {
                JarFile jf = ((JarURLConnection)urlconn).getJarFile();
                if (jf != null && jf.getManifest() != null)
                    cs = jf.getJarEntry(rname).getCodeSigners();
            }
            
            Certificate[] certs = new CodeSource(res, cs).getCertificates();
            cmap.put(pkg, certs == null ? new Certificate[0] : certs);
        }
        catch (Exception e)
        {
            throw new RuntimeException("qw");
        }
    }
}
