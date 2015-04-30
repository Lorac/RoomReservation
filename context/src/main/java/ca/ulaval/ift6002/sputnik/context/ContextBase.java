package ca.ulaval.ift6002.sputnik.context;

public abstract class ContextBase {

    public void apply() {
        registerServices();
        applyFillers();
    }

    protected abstract void registerServices();

    protected abstract void applyFillers();
}
