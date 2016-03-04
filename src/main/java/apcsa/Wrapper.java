package apcsa;

/**
 * Classes for wrapping objects for the purpose of controlling their appearance in a JList
 */
public class Wrapper<E> implements Comparable<Wrapper<E>> {

    protected E element;

    public Wrapper(E element) {
        this.element = element;
    }

    public E get() {
        return element;
    }

    @Override
    public int compareTo(Wrapper<E> o) {
        return this.element.toString().compareTo(o.element.toString());
    }


    public static class Method extends Wrapper<java.lang.reflect.Method> {

        public Method(java.lang.reflect.Method element) {
            super(element);
        }

        @Override
        public String toString() {
            return element.getName();
        }
    }

}
