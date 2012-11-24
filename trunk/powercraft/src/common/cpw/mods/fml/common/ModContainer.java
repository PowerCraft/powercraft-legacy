package cpw.mods.fml.common;

import java.io.File;
import java.util.List;
import java.util.Set;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionRange;

public interface ModContainer
{
    String getModId();

    String getName();

    String getVersion();

    File getSource();

    ModMetadata getMetadata();

    void bindMetadata(MetadataCollection mc);

    void setEnabledState(boolean enabled);

    Set<ArtifactVersion> getRequirements();

    List<ArtifactVersion> getDependencies();

    List<ArtifactVersion> getDependants();

    String getSortingRules();

    boolean registerBus(EventBus bus, LoadController controller);

    boolean matches(Object mod);

    Object getMod();

    ArtifactVersion getProcessedVersion();

    boolean isImmutable();

    boolean isNetworkMod();

    String getDisplayVersion();

    VersionRange acceptableMinecraftVersionRange();
}
