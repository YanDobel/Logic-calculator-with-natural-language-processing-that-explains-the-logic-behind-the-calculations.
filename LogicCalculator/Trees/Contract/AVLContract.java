package Trees.Contract;

public interface AVLContract<T extends Comparable<T>, N extends NodeContract<T, N>> {
   N add(T data);
   boolean contains(T data);
   T get(int index);
   T get(T data);
   T remove(T data);
   int indexOf(T data);
   boolean isEmpty();
   void clear();
}
