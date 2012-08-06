package org.bouncycastle.asn1;

public abstract class ASN1Primitive extends ASN1Object
{
    public final boolean equals(Object par1Obj)
    {
        return this == par1Obj ? true : par1Obj instanceof ASN1Encodable && this.func_71607_a(((ASN1Encodable)par1Obj).func_71606_a());
    }

    public ASN1Primitive func_71606_a()
    {
        return this;
    }

    public abstract int hashCode();

    abstract boolean func_71607_a(ASN1Primitive var1);
}
