package eu.aaxvv.node_spell.spell.graph;

import eu.aaxvv.node_spell.ModConstants;
import eu.aaxvv.node_spell.helper.EntityHelper;
import eu.aaxvv.node_spell.platform.registry.PlatformRegistryWrapper;
import eu.aaxvv.node_spell.spell.graph.nodes.action.DebugPrintNode;
import eu.aaxvv.node_spell.spell.graph.nodes.action.PlaceBlockNode;
import eu.aaxvv.node_spell.spell.graph.nodes.block.BlockFromItemNode;
import eu.aaxvv.node_spell.spell.graph.nodes.block.RaycastBlockNode;
import eu.aaxvv.node_spell.spell.graph.nodes.comparison.BasicNumberCompNode;
import eu.aaxvv.node_spell.spell.graph.nodes.comparison.EqualsNode;
import eu.aaxvv.node_spell.spell.graph.nodes.comparison.NotEqualsNode;
import eu.aaxvv.node_spell.spell.graph.nodes.constant.*;
import eu.aaxvv.node_spell.spell.graph.nodes.entity.GenericEntityPropertyNode;
import eu.aaxvv.node_spell.spell.graph.nodes.entity.ItemInHandNode;
import eu.aaxvv.node_spell.spell.graph.nodes.flow.BranchNode;
import eu.aaxvv.node_spell.spell.graph.nodes.flow.EntryPointNode;
import eu.aaxvv.node_spell.spell.graph.nodes.flow.ForLoopNode;
import eu.aaxvv.node_spell.spell.graph.nodes.generic.GenericConversionNode;
import eu.aaxvv.node_spell.spell.graph.nodes.generic.GenericSelectNode;
import eu.aaxvv.node_spell.spell.graph.nodes.input.CasterNode;
import eu.aaxvv.node_spell.spell.graph.nodes.logic.BasicBoolOpNode;
import eu.aaxvv.node_spell.spell.graph.nodes.math.BasicNumberOpNode;
import eu.aaxvv.node_spell.spell.graph.nodes.math.BasicNumberTriOpNode;
import eu.aaxvv.node_spell.spell.graph.nodes.math.BasicNumberUnaryOpNode;
import eu.aaxvv.node_spell.spell.graph.nodes.math.MapRangeNode;
import eu.aaxvv.node_spell.spell.graph.nodes.memory.GetSpellStorage;
import eu.aaxvv.node_spell.spell.graph.nodes.memory.GetVariableNode;
import eu.aaxvv.node_spell.spell.graph.nodes.memory.SetSpellStorage;
import eu.aaxvv.node_spell.spell.graph.nodes.memory.SetVariableNode;
import eu.aaxvv.node_spell.spell.graph.nodes.string.BasicStringOpNode;
import eu.aaxvv.node_spell.spell.graph.nodes.string.ToStringNode;
import eu.aaxvv.node_spell.spell.graph.nodes.vector.*;
import eu.aaxvv.node_spell.spell.graph.structure.Node;
import eu.aaxvv.node_spell.spell.value.Datatype;
import eu.aaxvv.node_spell.spell.value.Value;
import eu.aaxvv.node_spell.util.VectorUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class Nodes {
    public static PlatformRegistryWrapper<Node> REGISTRY;

    // ===== INPUT =====
    public static final Node NUMBER_CONSTANT = new NumberConstantNode();
    public static final Node BOOL_CONSTANT = new BoolConstantNode();
    public static final Node STRING_CONSTANT = new StringConstantNode();
    public static final Node VECTOR_CONSTANT = new VectorConstantNode();
    public static final Node CASTER = new CasterNode();

    public static final Node CONST_PI = new BuiltinConstNumberNode(ModConstants.resLoc("const_pi"), Math.PI);
    public static final Node CONST_TAU = new BuiltinConstNumberNode(ModConstants.resLoc("const_tau"), Math.PI * 2);
    public static final Node CONST_E = new BuiltinConstNumberNode(ModConstants.resLoc("const_e"), Math.E);

    // ===== MATH =====
    public static final Node ADD = new BasicNumberOpNode(ModConstants.resLoc("add"), Double::sum);
    public static final Node SUBTRACT = new BasicNumberOpNode(ModConstants.resLoc("subtract"), (a, b) -> a - b);
    public static final Node MULTIPLY = new BasicNumberOpNode(ModConstants.resLoc("multiply"), (a, b) -> a * b);
    public static final Node DIVIDE = new BasicNumberOpNode(ModConstants.resLoc("divide"), (a, b) -> b == 0 ? (a < 0 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY) : a / b);
    public static final Node POW = new BasicNumberOpNode(ModConstants.resLoc("power"), "socket.node_spell.base", "socket.node_spell.exponent", Math::pow);
    public static final Node SQRT = new BasicNumberUnaryOpNode(ModConstants.resLoc("sqrt"), Math::sqrt);
    public static final Node SIGN = new BasicNumberUnaryOpNode(ModConstants.resLoc("sign"), Math::signum);
    public static final Node CLAMP = new BasicNumberTriOpNode(ModConstants.resLoc("clamp"), "socket.node_spell.low", "socket.node_spell.high", "socket.node_spell.val", (l, h, v) -> Math.max(l, Math.min(h, v)));
    public static final Node MIN = new BasicNumberOpNode(ModConstants.resLoc("min"), Math::min);
    public static final Node MAX = new BasicNumberOpNode(ModConstants.resLoc("max"), Math::max);

    public static final Node SIN = new BasicNumberUnaryOpNode(ModConstants.resLoc("sin"), Math::sin);
    public static final Node COS = new BasicNumberUnaryOpNode(ModConstants.resLoc("cos"), Math::cos);
    public static final Node TAN = new BasicNumberUnaryOpNode(ModConstants.resLoc("tan"), Math::tan);
    public static final Node ASIN = new BasicNumberUnaryOpNode(ModConstants.resLoc("asin"), Math::asin);
    public static final Node ACOS = new BasicNumberUnaryOpNode(ModConstants.resLoc("acos"), Math::acos);
    public static final Node ATAN = new BasicNumberUnaryOpNode(ModConstants.resLoc("atan"), Math::atan);
    public static final Node MAP_RANGE = new MapRangeNode();
    public static final Node SELECT_NUM = new GenericSelectNode(NodeCategories.MATH, ModConstants.resLoc("select_num"), Datatype.NUMBER);

    // ===== VECTOR =====
    public static final Node VEC_CONSTRUCT = new VectorConstructNode();
    public static final Node VEC_DESTRUCT = new VectorDestructNode();
    public static final Node VEC_ADD = new BasicVectorOpNode(ModConstants.resLoc("vec_add"), Vec3::add);
    public static final Node VEC_SUB = new BasicVectorOpNode(ModConstants.resLoc("vec_subtract"), Vec3::subtract);
    public static final Node VEC_LENGTH = new GenericConversionNode
        .Builder<Vec3, Double>(NodeCategories.VECTOR, "vec_length")
        .types(Datatype.VECTOR, Datatype.NUMBER)
        .socketNames("vector", "length")
        .function(Vec3::length)
        .build();
    public static final Node VEC_DOT = new VectorDotNode();
    public static final Node VEC_CROSS = new BasicVectorOpNode(ModConstants.resLoc("vec_cross"), Vec3::cross);
    public static final Node VEC_NEAREST_AXIS = new GenericConversionNode
            .Builder<Vec3, Vec3>(NodeCategories.VECTOR, "vec_nearest_axis")
            .types(Datatype.VECTOR, Datatype.VECTOR)
            .socketNames("vector", "vector")
            .function(VectorUtil::nearestAxis)
            .build();
    public static final Node VEC_NORMALIZE = new GenericConversionNode
            .Builder<Vec3, Vec3>(NodeCategories.VECTOR, "vec_normalize")
            .types(Datatype.VECTOR, Datatype.VECTOR)
            .socketNames("vector", "vector")
            .function(Vec3::normalize)
            .build();
    public static final Node VEC_SCALE = new VectorScaleNode();
    public static final Node SELECT_VEC = new GenericSelectNode(NodeCategories.VECTOR, ModConstants.resLoc("select_vec"), Datatype.VECTOR);

    // scale, project, rotate around

    // ===== LOGIC =====
    public static final Node AND = new BasicBoolOpNode(ModConstants.resLoc("and"), (a, b) -> a && b);
    public static final Node OR = new BasicBoolOpNode(ModConstants.resLoc("or"), (a, b) -> a || b);
    public static final Node XOR = new BasicBoolOpNode(ModConstants.resLoc("xor"), (a, b) -> a ^ b);
    public static final Node NOT = new GenericConversionNode.Builder<Boolean, Boolean>(NodeCategories.LOGIC, "not")
            .types(Datatype.BOOL, Datatype.BOOL)
            .socketNames("val", "result")
            .function(b -> !b)
            .build();

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
    public static final Node ENTITY_POSITION = new GenericEntityPropertyNode<>(
            "entity_pos",
            Datatype.VECTOR,
            "position",
            Entity::position
    );
    public static final Node ENTITY_HEALTH = new GenericEntityPropertyNode<>(
            "entity_health",
            Datatype.NUMBER,
            "hp",
            e -> e instanceof LivingEntity living ? (double)living.getHealth() : 0.0
    );
    public static final Node ENTITY_MAX_HEALTH = new GenericEntityPropertyNode<>(
            "entity_max_health",
            Datatype.NUMBER,
            "max",
            e -> e instanceof LivingEntity living ? (double)living.getMaxHealth() : 0.0
    );
    public static final Node ENTITY_VELOCITY = new GenericEntityPropertyNode<>(
            "entity_velocity",
            Datatype.VECTOR,
            "velocity",
            EntityHelper::getEntityVelocity
    );
    public static final Node ENTITY_LOOK_DIRECTION = new GenericEntityPropertyNode<>(
            "entity_look_direction",
            Datatype.VECTOR,
            "direction",
            Entity::getLookAngle
    );
    public static final Node ENTITY_IS_SNEAKING = new GenericEntityPropertyNode<>(
            "entity_is_sneaking",
            Datatype.BOOL,
            "bool",
            EntityHelper::isSneaking
    );
    public static final Node ENTITY_EYE_POSITION = new GenericEntityPropertyNode<>(
            "entity_eye_pos",
            Datatype.VECTOR,
            "position",
            Entity::getEyePosition
    );
    public static final Node SELECT_ENTITY = new GenericSelectNode(NodeCategories.ENTITY, ModConstants.resLoc("select_entity"), Datatype.ENTITY);

    // target entity / position,
    // item: next in hot bar, hand (other for caster), from entity

    public static final Node ITEM_IN_HAND = new ItemInHandNode();

    // ===== BLOCK =====
    // block: at position, from item, is in tag, redstone activated, name, waterlogged / flammable?
    public static final Node BLOCK_FROM_ITEM = new BlockFromItemNode();
    public static final Node RAY_CAST_BLOCK = new RaycastBlockNode();
    public static final Node SELECT_BLOCK = new GenericSelectNode(NodeCategories.BLOCK, ModConstants.resLoc("select_block"), Datatype.BLOCK);
    // break, get at position, get id, is liquid, is solid, is flammable

    // ===== ITEM =====
    public static final Node ITEM_COUNT = new GenericConversionNode
            .Builder<ItemStack, Double>(NodeCategories.ITEM, "item_count")
            .types(Datatype.ITEM, Datatype.NUMBER)
            .socketNames("item", "count")
            .function(i -> (double) i.getCount())
            .build();
    public static final Node SELECT_ITEM = new GenericSelectNode(NodeCategories.ITEM, ModConstants.resLoc("select_item"), Datatype.ITEM);
    // is stackable, has nbt, is edible, is damaged, from block

    // ===== STRING =====
    public static final Node STRING_APPEND = new BasicStringOpNode<>(ModConstants.resLoc("string_append"), Datatype.STRING, Value::createString, (a, b) -> a + b);
    public static final Node STRING_CONTAINS = new BasicStringOpNode<>(ModConstants.resLoc("string_contains"), Datatype.BOOL, Value::createBool, String::contains);
    public static final Node STRING_STARTS_WITH = new BasicStringOpNode<>(ModConstants.resLoc("string_starts_with"), Datatype.BOOL, Value::createBool, String::startsWith);
    public static final Node STRING_ENDS_WITH = new BasicStringOpNode<>(ModConstants.resLoc("string_ends_with"), Datatype.BOOL, Value::createBool, String::endsWith);
    public static final Node STRING_INDEX_OF = new BasicStringOpNode<>(ModConstants.resLoc("string_index_of"), Datatype.NUMBER, Value::createNumber, (a, b) -> (double)a.indexOf(b));
    public static final Node SELECT_STRING = new GenericSelectNode(NodeCategories.STRING, ModConstants.resLoc("select_string"), Datatype.STRING);
    public static final Node TO_STRING = new ToStringNode();
    // substring, char at,

    // ===== ACTION =====
    public static final Node PRINT = new DebugPrintNode();
    public static final Node PLACE_BLOCK = new PlaceBlockNode();
    // place block, break block, apply impulse,

    // ===== MEMORY =====
    public static final Node GET_VARIABLE = new GetVariableNode();
    public static final Node SET_VARIABLE = new SetVariableNode();
    public static final Node GET_SPELL_STORAGE = new GetSpellStorage();
    public static final Node SET_SPELL_STORAGE = new SetSpellStorage();
    // player storage too? for passing data between spells?

    // ===== LIST =====
    public static final Node SELECT_LIST = new GenericSelectNode(NodeCategories.LIST, ModConstants.resLoc("select_list"), Datatype.LIST);

    public static void initRegistry(PlatformRegistryWrapper<Node> nodeRegistry) {
        REGISTRY = nodeRegistry;
    }

    public static void registerNodes() {
        ModConstants.LOG.info("Registering node types.");
        register(NUMBER_CONSTANT);
        register(BOOL_CONSTANT);
        register(STRING_CONSTANT);
        register(VECTOR_CONSTANT);
        register(CONST_PI);
        register(CONST_TAU);
        register(CONST_E);
        register(CASTER);

        register(ADD);
        register(SUBTRACT);
        register(MULTIPLY);
        register(DIVIDE);
        register(POW);
        register(SQRT);
        register(SIGN);
        register(CLAMP);
        register(MIN);
        register(MAX);
        register(SIN);
        register(COS);
        register(TAN);
        register(ASIN);
        register(ACOS);
        register(ATAN);
        register(MAP_RANGE);
        register(SELECT_NUM);

        register(VEC_CONSTRUCT);
        register(VEC_DESTRUCT);
        register(VEC_ADD);
        register(VEC_SUB);
        register(VEC_LENGTH);
        register(VEC_DOT);
        register(VEC_CROSS);
        register(VEC_NEAREST_AXIS);
        register(VEC_NORMALIZE);
        register(VEC_SCALE);
        register(SELECT_VEC);

        register(AND);
        register(OR);
        register(XOR);
        register(NOT);

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
        register(ENTITY_HEALTH);
        register(ENTITY_MAX_HEALTH);
        register(ENTITY_VELOCITY);
        register(ENTITY_LOOK_DIRECTION);
        register(ENTITY_IS_SNEAKING);
        register(ENTITY_EYE_POSITION);
        register(SELECT_ENTITY);

        register(BLOCK_FROM_ITEM);
        register(RAY_CAST_BLOCK);
        register(SELECT_BLOCK);

        register(ITEM_COUNT);
        register(SELECT_ITEM);

        register(STRING_APPEND);
        register(STRING_CONTAINS);
        register(STRING_STARTS_WITH);
        register(STRING_ENDS_WITH);
        register(STRING_INDEX_OF);
        register(SELECT_STRING);
        register(TO_STRING);

        register(PRINT);
        register(PLACE_BLOCK);

        register(GET_VARIABLE);
        register(SET_VARIABLE);
        register(GET_SPELL_STORAGE);
        register(SET_SPELL_STORAGE);

        register(SELECT_LIST);
    }

    private static void register(Node node) {
        REGISTRY.register(node.getResourceLocation(), node);
    }
}
