package InputProcessing.Contexts;

import InputProcessing.ContextualInputProcessor;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import org.apache.maven.surefire.shared.lang3.tuple.ImmutablePair;
import org.apache.maven.surefire.shared.lang3.tuple.Pair;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class Context implements Screen {
    private InputProcessor inputProcessor;
    final private String contextName;
    final private ContextualInputProcessor iProc;

    public Context(String name, ContextualInputProcessor iProc) {
        contextName = name;
        this.iProc = iProc;
    }

    protected ContextualInputProcessor getContextualInputProcessor() {
        return iProc;
    }
    protected void setInputProcessor(InputProcessor inputProcessor) {this.inputProcessor = inputProcessor;}
    public InputProcessor getInputProcessor() {
        return inputProcessor;
    }

    /**
     *
     * @return Name of the context
     */
    public String getContextName() {
        return contextName;
    }

}
