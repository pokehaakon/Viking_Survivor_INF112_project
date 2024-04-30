package Tools.Pool;

public interface Poolable {
    /**
     * Method that is run when the Poolable is put into the pool
     */
    void put();

    /**
     * Method that is run when the Poolable is gotten from the pool
     */
    void get();

    /**
     * Get the name used by the pool
     * @return the name of the Poolable
     */
    String getName();
}
