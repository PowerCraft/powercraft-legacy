package cpw.mods.fml.common.versioning;

public interface ArtifactVersion
    extends Comparable<ArtifactVersion>
{
    String getLabel();

    String getVersionString();

    boolean containsVersion(ArtifactVersion source);

    String getRangeString();
}
