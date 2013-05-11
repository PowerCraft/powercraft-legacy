package codechicken.core.asm;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.Remapper;
import org.objectweb.asm.commons.RemappingClassAdapter;
import org.objectweb.asm.commons.RemappingMethodAdapter;

import codechicken.core.JavaUtils;
import codechicken.core.config.ConfigFile;

import com.google.common.base.Function;

import cpw.mods.fml.relauncher.IClassTransformer;
import cpw.mods.fml.relauncher.RelaunchClassLoader;

public class MCPDeobfuscationTransformer implements IClassTransformer, Opcodes
{
    public static MCPDeobfuscationRemapper instance;
    
    public static String map(String name)
    {
        if(instance!=null)
        {
            String remapped = instance.mappings.get(name);
            if(remapped != null)
                return remapped;
        }
        return name;
    }
    
    public static class MCPDeobfuscationRemapper extends Remapper
    {
        public HashMap<String, String> mappings = new HashMap<String, String>();
        
        public MCPDeobfuscationRemapper(File methods, File fields)
        {
            final Pattern srgmap = Pattern.compile("((?:func|field)\\w+),(\\w+)");
            Function<String, Void> function = new Function<String, Void>()
            {
                @Override
                public Void apply(String line)
                {
                    Matcher matcher = srgmap.matcher(line);
                    while(matcher.find())
                    {
                        mappings.put(matcher.group(1), matcher.group(2));
                        return null;
                    }
                    return null;
                }
            };
            
            JavaUtils.processLines(methods, function);
            JavaUtils.processLines(fields, function);
        }
        
        @Override
        public String mapMethodName(String owner, String name, String desc)
        {
            if(name.startsWith("func"))
            {
                String remapped = mappings.get(name);
                if(remapped != null)
                    name = remapped;
            }
            return name;
        }
        
        @Override
        public String mapFieldName(String owner, String name, String desc)
        {
            if(name.startsWith("field"))
            {
                String remapped = mappings.get(name);
                if(remapped != null)
                    name = remapped;
            }
            return name;
        }

        public Object mapCst(Object cst)
        {
            if(!(cst instanceof String))
                return cst;
            String remapped = mappings.get(cst);
            if(remapped != null)
                return remapped;
            return cst;
        }
    }
    
    public static class MCPDeobfuscationMethodAdapter extends RemappingMethodAdapter
    {
        MCPDeobfuscationRemapper mremapper;
        
        public MCPDeobfuscationMethodAdapter(int access, String desc, MethodVisitor mv, Remapper remapper)
        {
            super(access, desc, mv, remapper);
            mremapper = (MCPDeobfuscationRemapper) remapper;
        }
        
        @Override
        public void visitLdcInsn(Object cst)
        {
            super.visitLdcInsn(mremapper.mapCst(cst));
        }
    }
    
    public static class MCPDeobfuscationAdapter extends RemappingClassAdapter
    {
        public MCPDeobfuscationAdapter(ClassVisitor cv)
        {
            super(cv, instance);
        }
        
        @Override
        protected MethodVisitor createRemappingMethodAdapter(int access, String newDesc, MethodVisitor mv)
        {
            return new RemappingMethodAdapter(access, newDesc, mv, remapper);
        }
    }
    
    @Override
    public byte[] transform(String name, String transformedName, byte[] bytes)
    {
        ClassReader classReader = new ClassReader(bytes);
        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
        RemappingClassAdapter remapAdapter = new MCPDeobfuscationAdapter(classWriter);
        classReader.accept(remapAdapter, ClassReader.EXPAND_FRAMES);
        return classWriter.toByteArray();
    }
    
    public static void load(File mcDir, ConfigFile config, RelaunchClassLoader cl)
    {
        if(!config.getTag("dev").getTag("deobfuscate").setComment("set to true to completely deobfuscate mcp names").getBooleanValue(false))
            return;
        
        File mappingDir = new File(mcDir, "config/conf");
        if(!mappingDir.exists())
            mappingDir.mkdirs();
        
        File methods = new File(mappingDir, "methods.csv");
        File fields = new File(mappingDir, "fields.csv");
        if(!methods.exists())
            throw new RuntimeException("FileNotFound: "+methods.getAbsolutePath());
        if(!fields.exists())
            throw new RuntimeException("FileNotFound: "+fields.getAbsolutePath());
        
        instance = new MCPDeobfuscationRemapper(methods, fields);
        cl.registerTransformer(MCPDeobfuscationTransformer.class.getName());
    }
}
