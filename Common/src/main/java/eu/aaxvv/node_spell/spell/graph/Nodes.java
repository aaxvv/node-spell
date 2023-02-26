package eu.aaxvv.node_spell.spell.graph;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.spell.graph.nodes.block_and_item.BlockFromItemNode;
import eu.aaxvv.node_spell.spell.graph.nodes.action.PlaceBlockNode;
import eu.aaxvv.node_spell.spell.graph.nodes.comparison.BasicNumberCompNode;
import eu.aaxvv.node_spell.spell.graph.nodes.comparison.EqualsNode;
import eu.aaxvv.node_spell.spell.graph.nodes.comparison.NotEqualsNode;
import eu.aaxvv.node_spell.spell.graph.nodes.entity.ItemInHandNode;
import eu.aaxvv.node_spell.spell.graph.nodes.logic.BasicBoolOpNode;
import eu.aaxvv.node_spell.spell.graph.nodes.math.*;
import eu.aaxvv.node_spell.spell.graph.nodes.action.DebugPrintNode;
import eu.aaxvv.node_spell.spell.graph.nodes.constant.BoolConstantNode;
import eu.aaxvv.node_spell.spell.graph.nodes.constant.NumberConstantNode;
import eu.aaxvv.node_spell.spell.graph.nodes.constant.StringConstantNode;
import eu.aaxvv.node_spell.spell.graph.nodes.entity.EntityPositionNode;
import eu.aaxvv.node_spell.spell.graph.nodes.flow.BranchNode;
import eu.aaxvv.node_spell.spell.graph.nodes.flow.EntryPointNode;
import eu.aaxvv.node_spell.spell.graph.nodes.flow.ForLoopNode;
import eu.aaxvv.node_spell.spell.graph.nodes.input.CasterNode;
import eu.aaxvv.node_spell.spell.graph.nodes.string.BasicStringOpNode;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import net.minecraft.core.Registry;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class Nodes {
    public static Supplier<Registry<Node>> REGISTRY_SUPPLIER;

    // ===== INPUT =====
    public static final Node NUMBER_CONSTANT = new NumberConstantNode();
    public static final Node BOOL_CONSTANT = new BoolConstantNode();
    public static final Node STRING_CONSTANT = new StringConstantNode();
    public static final Node CASTER = new CasterNode();

//    public static final Node ADD = new AddNode();
    // ===== MATH =====
    public static final Node ADD = new BasicNumberOpNode(ModConstants.resLoc("add"), Double::sum);
    public static final Node SUBTRACT = new BasicNumberOpNode(ModConstants.resLoc("subtract"), (a, b) -> a - b);
    public static final Node MULTIPLY = new BasicNumberOpNode(ModConstants.resLoc("multiply"), (a, b) -> a * b);
    public static final Node DIVIDE = new BasicNumberOpNode(ModConstants.resLoc("divide"), (a, b) -> b == 0 ? (a < 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY) : a / b);
    public static final Node POW = new BasicNumberOpNode(ModConstants.resLoc("power"), "socket.node_spell.base", "socket.node_spell.exponent", Math::pow);
    public static final Node SQRT = new BasicNumberUnaryOpNode(ModConstants.resLoc("sqrt"), Math::sqrt);
    public static final Node SIGN = new BasicNumberUnaryOpNode(ModConstants.resLoc("sign"), Math::signum);
    public static final Node CLAMP = new BasicNumberTriOpNode(ModConstants.resLoc("clamp"), "Low", "High", "Val", (l, h, v) -> Math.max(l, Math.min(h, v)));
    // map

    public static final Node VEC_ADD = new BasicVectorOpNode(ModConstants.resLoc("vec_add"), Vec3::add);
    public static final Node VEC_SUB = new BasicVectorOpNode(ModConstants.resLoc("vec_subtract"), Vec3::subtract);
    public static final Node VEC_LENGTH = new BasicVecToNumberNode(ModConstants.resLoc("vec_length"), Vec3::length);
    public static final Node VEC_DOT = new VectorDotNode();
    public static final Node VEC_CROSS = new BasicVectorOpNode(ModConstants.resLoc("vec_cross"), Vec3::cross);
    // scale

    // ===== LOGIC =====
    public static final Node AND = new BasicBoolOpNode(ModConstants.resLoc("and"), (a, b) -> a && b);
    public static final Node OR = new BasicBoolOpNode(ModConstants.resLoc("or"), (a, b) -> a || b);
    public static final Node XOR = new BasicBoolOpNode(ModConstants.resLoc("xor"), (a, b) -> a ^ b);

    // ===== COMPARE =====
    public static final Node EQUALS = new EqualsNode();
    public static final Node NOT_EQUAL = new NotEqualsNode();
    public static final Node LESS_THAN = new BasicNumberCompNode(ModConstants.resLoc("less_than"), (a, b) -> a < b);
    public static final Node GREATER_THAN = new BasicNumberCompNode(ModConstants.resLoc("greater_than"), (a, b) -> a > b);
    public static final Node LESS_THAN_OR_EQUALS = new BasicNumberCompNode(ModConstants.resLoc("less_than_or_eq"), (a, b) -> a <= b);
    public static final Node GREATER_THAN_OR_EQUALS = new BasicNumberCompNode(ModConstants.resLoc("greater_than_or_eq"), (a, b) -> a >= b);

    // ===== FLOW =====
    public static final Node ENTRY_POINT = new EntryPointNode();
    public static final Node BRANCH = new BranchNode();
    public static final Node FOR_RANGE = new ForLoopNode();

    // ===== ENTITY =====
    public static final Node ENTITY_POSITION = new EntityPositionNode();
    // look direction, velocity, target entity / block, health / max health,

    public static final Node ITEM_IN_HAND = new ItemInHandNode();
    public static final Node BLOCK_FROM_ITEM = new BlockFromItemNode();

    // ===== STRING =====
    public static final Node STRING_APPEND = new BasicStringOpNode<>(ModConstants.resLoc("string_append"), Datatype.STRING, Value::createString, (a, b) -> a + b);
    public static final Node STRING_CONTAINS = new BasicStringOpNode<>(ModConstants.resLoc("string_contains"), Datatype.BOOL, Value::createBool, String::contains);
    public static final Node STRING_STARTS_WITH = new BasicStringOpNode<>(ModConstants.resLoc("string_starts_with"), Datatype.BOOL, Value::createBool, String::startsWith);
    public static final Node STRING_ENDS_WITH = new BasicStringOpNode<>(ModConstants.resLoc("string_ends_with"), Datatype.BOOL, Value::createBool, String::endsWith);
    public static final Node STRING_INDEX_OF = new BasicStringOpNode<>(ModConstants.resLoc("string_index_of"), Datatype.NUMBER, Value::createNumber, (a, b) -> (double)a.indexOf(b));
    // substring, char at,

    // ===== ACTION =====
    public static final Node PRINT = new DebugPrintNode();
    public static final Node PLACE_BLOCK = new PlaceBlockNode();

    public static void initRegistry(Supplier<Registry<Node>> nodeRegistry) {
        REGISTRY_SUPPLIER = nodeRegistry;
    }

    public static void registerNodes() {
        ModConstants.LOG.info("Registering node types.");
        register(NUMBER_CONSTANT);
        register(BOOL_CONSTANT);
        register(STRING_CONSTANT);
        register(CASTER);

        register(ADD);
        register(SUBTRACT);
        register(MULTIPLY);
        register(DIVIDE);
        register(POW);
        register(SQRT);
        register(SIGN);
        register(CLAMP);

        register(VEC_ADD);
        register(VEC_SUB);
        register(VEC_LENGTH);
        register(VEC_DOT);
        register(VEC_CROSS);

        register(AND);
        register(OR);
        register(XOR);

        register(EQUALS);
        register(NOT_EQUAL);
        register(GREATER_THAN);
        register(LESS_THAN);
        register(GREATER_THAN_OR_EQUALS);
        register(LESS_THAN_OR_EQUALS);

        register(ENTRY_POINT);
        register(BRANCH);
        register(FOR_RANGE);

        register(ENTITY_POSITION);
        register(ITEM_IN_HAND);
        register(BLOCK_FROM_ITEM);

        register(STRING_APPEND);
        register(STRING_CONTAINS);
        register(STRING_STARTS_WITH);
        register(STRING_ENDS_WITH);
        register(STRING_INDEX_OF);

        register(PRINT);
        register(PLACE_BLOCK);
    }

    private static void register(Node node) {
        Registry.register(REGISTRY_SUPPLIER.get(), node.getResourceLocation(), node);
    }
}
