package eu.aaxvv.node_spell;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModConstants {
	public static final String MOD_ID = "node_spell";
	public static final String MOD_NAME = "NodeSpell";
	public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);

	public static final int SPELL_INTERACTION_RANGE = 20;

	public static ResourceLocation resLoc(String name) {
		return new ResourceLocation(MOD_ID, name);
	}
}