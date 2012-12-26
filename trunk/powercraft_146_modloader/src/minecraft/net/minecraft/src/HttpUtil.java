package net.minecraft.src;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ServerSocket;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpUtil
{
    /**
     * Builds an encoded HTTP POST content string from a string map
     */
    public static String buildPostString(Map par0Map)
    {
        StringBuilder var1 = new StringBuilder();
        Iterator var2 = par0Map.entrySet().iterator();

        while (var2.hasNext())
        {
            Entry var3 = (Entry)var2.next();

            if (var1.length() > 0)
            {
                var1.append('&');
            }

            try
            {
                var1.append(URLEncoder.encode((String)var3.getKey(), "UTF-8"));
            }
            catch (UnsupportedEncodingException var6)
            {
                var6.printStackTrace();
            }

            if (var3.getValue() != null)
            {
                var1.append('=');

                try
                {
                    var1.append(URLEncoder.encode(var3.getValue().toString(), "UTF-8"));
                }
                catch (UnsupportedEncodingException var5)
                {
                    var5.printStackTrace();
                }
            }
        }

        return var1.toString();
    }

    /**
     * Sends a HTTP POST request to the given URL with data from a map
     */
    public static String sendPost(URL par0URL, Map par1Map, boolean par2)
    {
        return sendPost(par0URL, buildPostString(par1Map), par2);
    }

    /**
     * Sends a HTTP POST request to the given URL with data from a string
     */
    public static String sendPost(URL par0URL, String par1Str, boolean par2)
    {
        try
        {
            HttpURLConnection var3 = (HttpURLConnection)par0URL.openConnection();
            var3.setRequestMethod("POST");
            var3.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            var3.setRequestProperty("Content-Length", "" + par1Str.getBytes().length);
            var3.setRequestProperty("Content-Language", "en-US");
            var3.setUseCaches(false);
            var3.setDoInput(true);
            var3.setDoOutput(true);
            DataOutputStream var4 = new DataOutputStream(var3.getOutputStream());
            var4.writeBytes(par1Str);
            var4.flush();
            var4.close();
            BufferedReader var5 = new BufferedReader(new InputStreamReader(var3.getInputStream()));
            StringBuffer var7 = new StringBuffer();
            String var6;

            while ((var6 = var5.readLine()) != null)
            {
                var7.append(var6);
                var7.append('\r');
            }

            var5.close();
            return var7.toString();
        }
        catch (Exception var8)
        {
            if (!par2)
            {
                Logger.getLogger("Minecraft").log(Level.SEVERE, "Could not post to " + par0URL, var8);
            }

            return "";
        }
    }

    /**
     * The downloader for texturepacks stored in the server.
     */
    public static void downloadTexturePack(File par0File, String par1Str, IDownloadSuccess par2IDownloadSuccess, Map par3Map, int par4, IProgressUpdate par5IProgressUpdate)
    {
        Thread var6 = new Thread(new HttpUtilRunnable(par5IProgressUpdate, par1Str, par3Map, par0File, par2IDownloadSuccess, par4));
        var6.setDaemon(true);
        var6.start();
    }

    public static int func_76181_a() throws IOException
    {
        ServerSocket var0 = null;
        boolean var1 = true;
        int var10;

        try
        {
            var0 = new ServerSocket(0);
            var10 = var0.getLocalPort();
        }
        finally
        {
            try
            {
                if (var0 != null)
                {
                    var0.close();
                }
            }
            catch (IOException var8)
            {
                ;
            }
        }

        return var10;
    }

    public static String[] func_82718_a(String par0Str, String par1Str)
    {
        HashMap var2 = new HashMap();
        var2.put("user", par0Str);
        var2.put("password", par1Str);
        var2.put("version", Integer.valueOf(13));
        String var3;

        try
        {
            var3 = sendPost(new URL("http://login.minecraft.net/"), var2, false);
        }
        catch (MalformedURLException var5)
        {
            var5.printStackTrace();
            return null;
        }

        if (var3 != null && var3.length() != 0)
        {
            if (!var3.contains(":"))
            {
                if (var3.trim().equals("Bad login"))
                {
                    System.out.println("Login failed");
                }
                else if (var3.trim().equals("Old version"))
                {
                    System.out.println("Outdated launcher");
                }
                else if (var3.trim().equals("User not premium"))
                {
                    System.out.println(var3);
                }
                else
                {
                    System.out.println(var3);
                }

                return null;
            }
            else
            {
                String[] var4 = var3.split(":");
                return new String[] {var4[2], var4[3]};
            }
        }
        else
        {
            System.out.println("Can\'t connect to minecraft.net");
            return null;
        }
    }
}
