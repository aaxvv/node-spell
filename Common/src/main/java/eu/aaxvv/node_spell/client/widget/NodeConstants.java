package eu.aaxvv.node_spell.client.widget;

import eu.aaxvv.node_spell.spell.graph.SpellGraph;
import eu.aaxvv.node_spell.spell.graph.nodes.AddNode;
import eu.aaxvv.node_spell.spell.graph.nodes.constant.NumberConstantNode;
import eu.aaxvv.node_spell.spell.graph.nodes.flow.ForLoopNode;
import eu.aaxvv.node_spell.spell.graph.runtime.Edge;
import eu.aaxvv.node_spell.spell.graph.runtime.NodeInstance;

public class NodeConstants {
    public static final int DEFAULT_NODE_WIDTH = 64;
    public static final int HEADER_HEIGHT = 10;
    public static final int SOCKET_START_Y = 15;
    public static final int SOCKET_STEP_Y = 12;
    public static final int TITLE_TEXT_COLOR = 0xFF000000;
    public static final int SOCKET_TEXT_COLOR = 0xFF000000;
    public static final int NODE_PICKER_COLUMNS = 6;
    public static final int SPELL_BOOK_BG_COLOR = 0xFFD6BE96;
    public static final int SPELL_BOOK_SEPARATOR_COLOR = 0xFF99876C;
    public static final int NODE_GRAPH_GRID_COLOR = 0xFFE7D5B4;
    public static final int NODE_GRAPH_GRID_SPACING = 16;

    // TODO temporary
    public static final SpellGraph TEST_GRAPH = new SpellGraph();

    static {
        NumberConstantNode constNode = new NumberConstantNode();
        AddNode addNode = new AddNode();
        ForLoopNode loopNode = new ForLoopNode();
        NodeInstance constInstance1 = TEST_GRAPH.addInstance(constNode.createInstance());
        constInstance1.setPosition(0, 0);
        NodeInstance constInstance2 = TEST_GRAPH.addInstance(constNode.createInstance());
        constInstance2.setPosition(0, 64);
        NodeInstance addInstance = TEST_GRAPH.addInstance(addNode.createInstance());
        addInstance.setPosition(72, 24);
        NodeInstance loopInstance = TEST_GRAPH.addInstance(loopNode.createInstance());
        loopInstance.setPosition(72, 128);

        TEST_GRAPH.addEdge(Edge.create(constInstance1.getSocketInstance(constNode.sValue), addInstance.getSocketInstance(addNode.sA)));
        TEST_GRAPH.addEdge(Edge.create(constInstance2.getSocketInstance(constNode.sValue), addInstance.getSocketInstance(addNode.sB)));
    }
}
