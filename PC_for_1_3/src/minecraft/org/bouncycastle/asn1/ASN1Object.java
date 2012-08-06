package org.bouncycastle.asn1;

public abstract class ASN1Object implements ASN1Encodable
{
    public int hashCode()
    {
        return this.func_71606_a().hashCode();
    }

    public boolean equals(Object par1Obj)
    {
        if (this == par1Obj)
        {
            return true;
        }
        else if (!(par1Obj instanceof ASN1Encodable))
        {
            return false;
        }
        else
        {
            ASN1Encodable var2 = (ASN1Encodable)par1Obj;
            return this.func_71606_a().equals(var2.func_71606_a());
        }
    }

    public abstract ASN1Primitive func_71606_a();
}
