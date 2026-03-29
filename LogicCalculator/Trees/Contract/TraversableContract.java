package Trees.Contract;
import java.util.List;

public interface TraversableContract<T> {
    List<T> inOrder();
    List<T> preOrder();
    List<T> postOrder();
}
