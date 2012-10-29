package org.bouncycastle.asn1;

public class ASN1ObjectIdentifier extends DERObjectIdentifier
{
    public ASN1ObjectIdentifier(String par1Str)
    {
        super(par1Str);
    }

    public ASN1ObjectIdentifier func_71612_a(String par1Str)
    {
        return new ASN1ObjectIdentifier(this.func_71609_b() + "." + par1Str);
    }
}
