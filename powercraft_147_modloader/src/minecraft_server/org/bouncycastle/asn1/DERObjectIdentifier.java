package org.bouncycastle.asn1;

public class DERObjectIdentifier extends ASN1Primitive
{
    String field_71611_a;
    private static ASN1ObjectIdentifier[][] field_71610_b = new ASN1ObjectIdentifier[255][];

    public DERObjectIdentifier(String par1Str)
    {
        if (!func_71608_a(par1Str))
        {
            throw new IllegalArgumentException("string " + par1Str + " not an OID");
        }
        else
        {
            this.field_71611_a = par1Str;
        }
    }

    public String func_71609_b()
    {
        return this.field_71611_a;
    }

    public int hashCode()
    {
        return this.field_71611_a.hashCode();
    }

    boolean asn1Equals(ASN1Primitive par1ASN1Primitive)
    {
        return !(par1ASN1Primitive instanceof DERObjectIdentifier) ? false : this.field_71611_a.equals(((DERObjectIdentifier)par1ASN1Primitive).field_71611_a);
    }

    public String toString()
    {
        return this.func_71609_b();
    }

    private static boolean func_71608_a(String par0Str)
    {
        if (par0Str.length() >= 3 && par0Str.charAt(1) == 46)
        {
            char var1 = par0Str.charAt(0);

            if (var1 >= 48 && var1 <= 50)
            {
                boolean var2 = false;

                for (int var3 = par0Str.length() - 1; var3 >= 2; --var3)
                {
                    char var4 = par0Str.charAt(var3);

                    if (48 <= var4 && var4 <= 57)
                    {
                        var2 = true;
                    }
                    else
                    {
                        if (var4 != 46)
                        {
                            return false;
                        }

                        if (!var2)
                        {
                            return false;
                        }

                        var2 = false;
                    }
                }

                return var2;
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}
