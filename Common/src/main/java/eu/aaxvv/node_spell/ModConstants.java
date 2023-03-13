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

	/**
	 * Colors from <a href="https://lospec.com/palette-list/endesga-32">the EDG32 Palette</a>
	 */
	public static class Colors {
		public static final int RED = 0xffe43b44;
		public static final int YELLOW = 0xfffeae34;
		public static final int ORANGE = 0xfff77622;
		public static final int GREEN = 0xff63c74d;
		public static final int LIGHT_BLUE = 0xff0099db;
		public static final int DARK_BLUE = 0xff124e89;
		public static final int PURPLE = 0xffb55088;
		public static final int PINK = 0xfff6757a;

		public static final int BLACK = 0xff181425;
		public static final int DARK_GREY = 0xff3a4466;
		public static final int MIDDLE_GREY = 0xff5a6988;
		public static final int LIGHT_GREY = 0xff8b9bb4;
		public static final int WHITE = 0xffffffff;

		public static final int PAPER_BG = 0xffead4aa;
		public static final int PAPER_GRID = 0xffE8C091;
		public static final int PAPER_BORDER = 0xffe4a672;

		public static final int TEXT = 0xFF000000;
	}

	public static class Sizing {
		public static final int DEFAULT_NODE_WIDTH = 72;
		public static final int HEADER_HEIGHT = 10;
		public static final int SOCKET_START_Y = 6;
		public static final int SOCKET_STEP_Y = 12;
	}
}