package net.minecraft.src;

final class StepSoundStone extends StepSound
{
    StepSoundStone(String par1Str, float par2, float par3)
    {
        super(par1Str, par2, par3);
    }

    public String getBreakSound()
    {
        return "random.glass";
    }

    public String getPlaceSound()
    {
        return "step.stone";
    }
}
