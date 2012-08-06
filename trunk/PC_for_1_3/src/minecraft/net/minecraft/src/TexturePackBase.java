package net.minecraft.src;

import java.io.InputStream;

public interface TexturePackBase
{
    void func_77533_a(RenderEngine var1);

    void func_77535_b(RenderEngine var1);

    /**
     * Gives a texture resource as InputStream.
     */
    InputStream getResourceAsStream(String var1);

    String func_77536_b();

    String func_77538_c();

    String func_77531_d();

    String func_77537_e();

    int func_77534_f();
}
