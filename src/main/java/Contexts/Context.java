package Contexts;

import InputProcessing.ContextualInputProcessor;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;

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
