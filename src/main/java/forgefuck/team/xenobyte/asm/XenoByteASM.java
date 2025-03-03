package forgefuck.team.xenobyte.asm;

import java.util.Map;

import cpw.mods.fml.relauncher.IFMLLoadingPlugin;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import forgefuck.team.xenobyte.asm.transformers.*;

@MCVersion("1.7.10")
@TransformerExclusions("forgefuck.team.xenobyte.asm")
public class XenoByteASM implements IFMLLoadingPlugin{

	@Override
	public String[] getASMTransformerClass() {
		return new String[] {
			forgefuck.team.xenobyte.asm.transformers.WorldRendererClassTransformer.class.getName(),
			forgefuck.team.xenobyte.asm.transformers.ItemRendererClassTransformer.class.getName()
		};
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
	}

	@Override
	public String getAccessTransformerClass() {
		return null;
	}

}
