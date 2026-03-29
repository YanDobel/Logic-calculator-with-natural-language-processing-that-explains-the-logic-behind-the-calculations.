package Trees.Contract;

public interface NodeContract<T extends Comparable<T>, N extends NodeContract<T, N>> {
    N getRight();
    N getLeft();
    T getData();
    int getHeight();
    void setRight(N right);
    void setLeft(N left);
    void setHeight(int height);
}
