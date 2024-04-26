package Parsing.ObjectDefineParser;

import Parsing.ObjectDefineParser.Defines.*;
import Parsing.Parser.ParserException;
import Parsing.Parser.ParsingException;
import Parsing.Parser.TextParser;
import Rendering.Animations.AnimationState;
import Tools.FilterTool;
import Tools.Tuple;
import com.badlogic.gdx.physics.box2d.BodyDef;
import org.javatuples.Pair;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static Tools.ListTools.getAll;
import static Tools.ListTools.getFirst;

public class ObjectDefineParser extends TextParser {

    public final Map<String, Variable> variables = new HashMap<>();

    public ObjectDefineParser(String filename) {
        super(filename);
        if(!filename.endsWith(".obj")) throw new IllegalArgumentException(filename + " must end with '.obj'");
    }

    public ObjectDefineParser(char[] text) {
        super(text);
    }

    /**
     * Parses everything between a '#' and newline
     * @return the string of the comment
     */
    private String parseComment() throws ParsingException {
        return Try(() -> {
            Void(this::space);
            parseLiteral('#');
            String opt = parseUntilLiteral('\n');
            Void(iparseLiteral('\n'));
            return opt;
        });
    }

    /**
     * Skips empty lines and lines with only comments
     */
    private void skipEmptyLinesAndComments() {
        many(ichoose(this::parseEmptyLine, this::parseComment));
    }

    private void parseIndent() throws ParsingException {
        parseStringLiteral("    ", "\n");
    }

    private Optional<Variable> lookup(String varName) {
        //varName = varName.replace("$", "");
        if (variables.containsKey(varName)) return Optional.of(variables.get(varName));
        return Optional.empty();
    }

    private <C> Optional<C> lookup(String varName, Class<C> cls) {
        //varName = varName.replace("$", "");
        if (!variables.containsKey(varName)) return Optional.empty();
        var val = variables.get(varName).get();
        if (!cls.isInstance(val))
            throw new RuntimeException(varName + " is Wrong Type, expected " + cls);
        return Optional.of(cls.cast(val));
    }

    private boolean isVariable(String varName) {
        return varName.charAt(0) == '$';
    }

    private <T> T getVarValueFromBodyCastAsT(List<Pair<String, String>> body, String fieldName, Class<T> cls) {
        String variableName = getFirst(body, e -> Objects.equals(e.getValue0().strip(), fieldName))
                .orElseThrow(() -> new ParserException(this, "Body did not contain Field '" + fieldName + "'"))
                .getValue1()
                .strip();
        return lookup(variableName, cls).orElseThrow(() -> new VariableNotInScopeException(variableName, variables));
    }

    private String getFieldValueFromBody(List<Pair<String, String>> body, String fieldName) {
        return getFirst(body, e -> Objects.equals(e.getValue0(), fieldName))
                .orElseThrow(() -> new ParserException(this, "Body did not contain Field '" + fieldName + "'"))
                .getValue1()
                .strip();
    }

    private <T> T lookupAndMakeToTIfVarOrMakeToT(String maybeVar, Function<String, T> f) {
        if (isVariable(maybeVar))
            return f.apply(lookup(maybeVar, String.class).get());
        return f.apply(maybeVar);
    }

    private String lookupIfVarElseValue(String maybeVar) {
        if (isVariable(maybeVar))
            return lookup(maybeVar, String.class).get();
        return maybeVar;
    }

    public Map<String, Variable> parseDocument() {
        try {
            while (!stream.atEOF()) {
                var pair = Try(() -> {
                    skipEmptyLinesAndComments();
                    return parseVariable();
                });

                variables.put(pair.getValue0(), pair.getValue1());
                skipEmptyLinesAndComments();
                try {parseEOF(); break;} catch (ParsingException ignore) {}
            }
        } catch (ParsingException e) {

            var ex = new ParserException(e, this, "Error while parsing Object Define:");
            ex.setStackTrace(e.getStackTrace());
            throw ex;
        }


        return variables;
    }

    private Pair<String, Variable> parseVariable() throws ParsingException {
        return Try(() -> {
            parseLiteral('$');
            String varName = '$' + parseUntilLiteral('=').strip();
            if (variables.containsKey(varName)) throw new ParserException(this, "Variable '" + varName + "' already defined!");
            Void(iparseLiteral('='));
            Void(this::space);
            if (stream.atEOF()) error("");
            return Tuple.of(varName, choose(
                    () -> {
                        parseStringLiteral("Define");
                        Void(this::space);
                        return Variable.of(choose(
                            this::parseActor,
                            this::parseAnimation,
                            this::parseStructure,
                            this::parseStats,
                            this::parseShape
                        ));
                    },
                    () -> Variable.of(parseUntilLiteral(' ', '\t', '\n', '#'))
            ));
        });
    }

    private ActorDefinition parseActor() throws ParsingException {
        return Try(() -> {
            //System.out.println("Start trying to parse actor!");
            parseStringLiteral("Actor");
            //System.out.println("Parsed 'Actor'!");
            parseLiteral(':');

            var body = parseDefinitionBody(ActorDefinition.legalDefines);


            AnimationDefinition animationDefinition = getVarValueFromBodyCastAsT(body, "Animation", AnimationDefinition.class);
            StatsDefinition statsDefinition = getVarValueFromBodyCastAsT(body, "Stats", StatsDefinition.class);
            StructureDefinition structureDefinition = getVarValueFromBodyCastAsT(body, "Structure", StructureDefinition.class);

            return ActorDefinition.of(animationDefinition, statsDefinition, structureDefinition);
        });
    }

    private AnimationDefinition parseAnimation() throws ParsingException {
        return Try(() -> {
            parseStringLiteral("Animation");
            parseLiteral(':');

            var body = parseDefinitionBody(AnimationDefinition.legalDefines);

            String stateString = getFieldValueFromBody(body, "State");

            var splitString = stateString.strip().split("\\s+");
            if (splitString.length != 2)
                throw new ParserException(this, "State needs two values (State, path): only " + splitString.length + " provided. " + Arrays.toString(splitString));
            AnimationState state = lookupAndMakeToTIfVarOrMakeToT(splitString[0], AnimationState::valueOf);
            String path = lookupAndMakeToTIfVarOrMakeToT(splitString[1], s -> s);

            Map<AnimationState, String> stateStringMap = new HashMap<>();
            stateStringMap.put(state, path);


            var initial = getFirst(body, e -> Objects.equals(e.getValue0(), "initial"));
            if (initial.isPresent())
                state = AnimationState.valueOf(initial.get().getValue1().strip());

            for (var e : getAll(body, e -> Objects.equals(e.getValue0(), "state"))) {
                splitString = e.getValue1().split("\\s+");
                stateStringMap.put(
                        AnimationState.valueOf(splitString[0]),
                        isVariable(splitString[1])
                                ? lookup(splitString[1], String.class).get()
                                : splitString[1]
                        );
            }

            return AnimationDefinition.of(stateStringMap, state);
        });
    }

    private StructureDefinition parseStructure() throws ParsingException {
        return Try(() -> {
            parseStringLiteral("Structure");
            parseLiteral(':');

            var body = parseDefinitionBody(StructureDefinition.legalDefines);

            String hitsString = lookupIfVarElseValue(getFieldValueFromBody(body, "Filter"));
            FilterTool.Category[] hits = new FilterTool.Category[hitsString.strip().split("\\s+").length];
            int i = 0;
            for (String s : hitsString.strip().split("\\s+")) {
                hits[i++] = FilterTool.Category.valueOf(s);
            }

            String categoriesString = lookupIfVarElseValue(getFieldValueFromBody(body, "Category"));
            FilterTool.Category[] categories = new FilterTool.Category[categoriesString.strip().split("\\s+").length];
            i = 0;
            for (String s : categoriesString.strip().split("\\s+")) {
                categories[i++] = FilterTool.Category.valueOf(s);
            }

            var filter = FilterTool.createFilter(categories, hits);

            var bodyType = lookupAndMakeToTIfVarOrMakeToT(
                    getFieldValueFromBody(body, "BodyType"),
                    BodyDef.BodyType::valueOf
            );

            ShapeDefinition shapeDefinition = getVarValueFromBodyCastAsT(body, "Shape", ShapeDefinition.class);

            var density = getFirst(body, e -> Objects.equals(e.getValue0(), "density"))
                    .map(Pair::getValue1)
                    .map(Float::parseFloat)
                    .orElse(1.0f);
            var friction = getFirst(body, e -> Objects.equals(e.getValue0(), "friction"))
                    .map(Pair::getValue1)
                    .map(Float::parseFloat)
                    .orElse(0f);
            var isSensor = getFirst(body, e -> Objects.equals(e.getValue0(), "isSensor"))
                    .map(Pair::getValue1)
                    .map(Boolean::parseBoolean)
                    .orElse(false);

            return StructureDefinition.of(
                    filter,
                    shapeDefinition,
                    density,
                    friction,
                    isSensor,
                    bodyType
            );
        });
    }

    private StatsDefinition parseStats() throws ParsingException {
        return Try(() -> {
            parseStringLiteral("Stats");
            parseLiteral(':');

            var body = parseDefinitionBody(StatsDefinition.legalDefines);

            float hp = lookupAndMakeToTIfVarOrMakeToT(
                    getFieldValueFromBody(body, "HP"),
                    Float::parseFloat
            );
            float speed = lookupAndMakeToTIfVarOrMakeToT(
                    getFieldValueFromBody(body, "Speed"),
                    Float::parseFloat
            );
            float damage = lookupAndMakeToTIfVarOrMakeToT(
                    getFieldValueFromBody(body, "Damage"),
                    Float::parseFloat
            );
            float resistance = getFirst(body, e -> e.getValue0().equals("resistance"))
                    .map(Pair::getValue1)
                    .map(Float::parseFloat)
                    .orElse(1.0f);

            float scale = getFirst(body, e -> e.getValue0().equals("scale"))
                    .map(Pair::getValue1)
                    .map(Float::parseFloat)
                    .orElse(1.0f);
            return StatsDefinition.of(hp, speed, damage, resistance, scale);
        });
    }

    private ShapeDefinition parseShape() throws ParsingException {
        return choose(
            this::parseCircleShape,
            this::parseSquareShape,
            this::parsePolygonShape
        );
    }

    private ShapeDefinition parseCircleShape() throws ParsingException {
        return Try(() -> {
            parseStringLiteral("Shape");
            parseLiteral(':');
            List<Pair<String, String>> body = null;
            try {
            body = parseDefinitionBody(CircleShapeDefinition.legalDefines);
            } catch (ParserException e) {
                if (!e.message.startsWith("Element not in")) throw e;
                error("");
            }

            float radius = lookupAndMakeToTIfVarOrMakeToT(
                    getFieldValueFromBody(body, "Radius"),
                    Float::parseFloat
            );

            return CircleShapeDefinition.of(radius);
        });
    }

    private ShapeDefinition parseSquareShape() throws ParsingException {
        return Try(() -> {
            parseStringLiteral("Shape");
            parseLiteral(':');
            List<Pair<String, String>> body = null;
            try {
                body = parseDefinitionBody(SquareShapeDefinition.legalDefines);
            } catch (ParserException e) {
                if (!e.message.startsWith("Element not in")) throw e;
                error("");
            }

            float width = lookupAndMakeToTIfVarOrMakeToT(
                    getFieldValueFromBody(body, "Width"),
                    Float::parseFloat
            );

            float height = lookupAndMakeToTIfVarOrMakeToT(
                    getFieldValueFromBody(body, "Height"),
                    Float::parseFloat
            );

            return SquareShapeDefinition.of(width, height);
        });
    }

    private ShapeDefinition parsePolygonShape() throws ParsingException {
        return Try(() -> {
            parseStringLiteral("Shape");
            parseLiteral(':');
            List<Pair<String, String>> body = null;
            try {
                body = parseDefinitionBody(PolygonShapeDefinition.legalDefines);
            } catch (ParserException e) {
                if (!e.message.startsWith("Element not in")) throw e;
                error("");
            }
            assert body != null; //never happens!
            var stringPoints = getAll(body, e -> e.getValue0().equals("point"));
            float[] points = new float[stringPoints.size() * 2];

            int i = 0;
            for (var pair : stringPoints) {
                var strings = pair.getValue1().strip().split("\\s+");
                points[i++] = Float.parseFloat(strings[0]);
                points[i++] = Float.parseFloat(strings[1]);
            }

            return PolygonShapeDefinition.of(points);
        });
    }

    private List<Pair<String, String>> parseDefinitionBody(Set<String> legalDefines) {
        //set of capitalized defines (mandatory)
        Set<String> mustHave = legalDefines
                .stream()
                .filter(s -> Character.isUpperCase(s.charAt(0)))
                .collect(Collectors.toSet());

        var ret = many(iTry(() -> {
            skipEmptyLinesAndComments();
            parseIndent();
            String fieldName = parseUntilLiteral(':').strip();
            if (!legalDefines.contains(fieldName)) throw new ParserException(this, "Element not in legalDefines encountered: " + fieldName + ", legalDefines: " + legalDefines);
            if (Character.isUpperCase(fieldName.charAt(0)) && !mustHave.remove(fieldName))
                throw new ParserException(this, "value of " + fieldName + " is given more than once!");
            parseLiteral(':');
            space();
            String fieldValue = parseUntilLiteral( '\t', '\n', '#').strip();
            skipEmptyLinesAndComments();
            return Tuple.of(fieldName, fieldValue);
        }));
        if (mustHave.isEmpty()) return ret;
        throw new ParserException(this, "Body did not contain all non-optional fields, missing: " + mustHave);
    }

}
