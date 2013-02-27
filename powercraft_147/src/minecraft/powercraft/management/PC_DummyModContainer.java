package powercraft.management;

import java.io.File;
import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.MetadataCollection;
import cpw.mods.fml.common.ModContainer;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionRange;

public class PC_DummyModContainer implements ModContainer {

	private PC_IModule module;
	private ModMetadata modMeta = new ModMetadata();
	
	public PC_DummyModContainer(PC_IModule module){
		this.module = module;
		modMeta.version = module.getVersion();
		modMeta.modId = "PowerCraft-"+module.getName();
		modMeta.name = module.getName();
		ModContainer md = mod_PowerCraft.getInstance().getModContainer();
		modMeta.parent = md.getModId();
	}
	
	@Override
	public String getModId() {
		return modMeta.modId;
	}

	@Override
	public String getName() {
		return modMeta.name;
	}

	@Override
	public String getVersion() {
		return modMeta.version;
	}

	@Override
	public File getSource() {
		return null;
	}

	@Override
	public ModMetadata getMetadata() {
		return modMeta;
	}

	@Override
	public void bindMetadata(MetadataCollection mc) {
	}

	@Override
	public void setEnabledState(boolean enabled) {
	}

	@Override
	public Set<ArtifactVersion> getRequirements() {
		return null;
	}

	@Override
	public List<ArtifactVersion> getDependencies() {
		return null;
	}

	@Override
	public List<ArtifactVersion> getDependants() {
		return null;
	}

	@Override
	public String getSortingRules() {
		return null;
	}

	@Override
	public boolean registerBus(EventBus bus, LoadController controller) {
		return false;
	}

	@Override
	public boolean matches(Object mod) {
		return false;
	}

	@Override
	public Object getMod() {
		return null;
	}

	@Override
	public ArtifactVersion getProcessedVersion() {
		return null;
	}

	@Override
	public boolean isImmutable() {
		return false;
	}

	@Override
	public boolean isNetworkMod() {
		return false;
	}

	@Override
	public String getDisplayVersion() {
		return getModId();
	}

	@Override
	public VersionRange acceptableMinecraftVersionRange() {
		return null;
	}

	@Override
	public Certificate getSigningCertificate() {
		return null;
	}

}
