package Parsing.ObjectDefineParser;

import java.util.Map;
import java.util.stream.Collectors;

public class VariableNotInScopeException extends RuntimeException {
    public VariableNotInScopeException(String animationDefinitionSting, Map<String, Variable> scope) {
        super("Variable not in scope: '" + animationDefinitionSting + "', value of scope:\n" + scope.entrySet().stream().map(Map.Entry::toString).collect(Collectors.joining("\n")));
    }
}
