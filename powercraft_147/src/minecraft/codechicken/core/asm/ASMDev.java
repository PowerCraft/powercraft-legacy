package codechicken.core.asm;

import java.io.File;

import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.Textifier;

import codechicken.core.asm.ObfuscationManager.MethodMapping;

public class ASMDev
{
    public static void print()
    {
        MethodASMifier.printMethod(
                new MethodMapping("codechicken.core.asm.ASMDev", "test", "(I)V"), 
                new Textifier(), new File("asm/testA.txt"));
        MethodASMifier.printMethod(
                new MethodMapping("codechicken.core.asm.ASMDev", "test", "(I)V"), 
                new ASMifier(), new File("asm/test.txt"));
    }
    
    Test perp;
    
    private static class Test
    {
        public void invoke(int c){}
    }
    
    public void test(int c)
    {
        if(perp != null)
            perp.invoke(c);
    }
    
}
