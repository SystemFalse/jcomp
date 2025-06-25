package io.github.systemfalse.jcomp;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * Class represents list of component children. It supports retrieving components
 * by index and name.
 */
public class ComponentList implements Iterable<Component>, Cloneable {
    /**
     * Class represents list entry. It contains name and component. It is used
     * for containing list of components and retrieving components by name.
     */
    public static class Entry {
        /**
         * Name of the component.
         */
        private final String name;
        /**
         * Component value.
         */
        private Component component;

        /**
         * Default constructor that creates new entry using given name and component.
         *
         * @param name component name
         * @param component component value
         */
        public Entry(String name, Component component) {
            this.name = name;
            this.component = component;
        }

        /**
         * Method returns name of the component.
         *
         * @return name
         */
        public String getName() {
            return name;
        }

        /**
         * Method returns component value.
         *
         * @return component
         */
        public Component getComponent() {
            return component;
        }

        /**
         * Method sets component value.
         *
         * @param component new value
         */
        private void setComponent(Component component) {
            this.component = component;
        }
    }

    private TreeSet<String> names;
    private List<Entry> entries;
    private Cache<String, Entry> cache;

    /**
     * Protected constructor that creates new component list using given supplier of entry list.
     * @param listSupplier supplier of entry list
     *
     * @throws NullPointerException if {@code listSupplier} is {@code null}
     * @throws IllegalArgumentException if duplicate name is found
     */
    protected ComponentList(Supplier<List<Entry>> listSupplier) {
        Objects.requireNonNull(listSupplier, "listSupplier");
        entries = Objects.requireNonNull(listSupplier.get(), "list");
        names = new TreeSet<>();
        if (!entries.isEmpty()) {
            for (Entry entry : entries) {
                if (names.contains(entry.getName())) {
                    throw new IllegalArgumentException("Duplicate name '" + entry.getName() + "'");
                }
                names.add(entry.getName());
            }
        }
        cache = CacheBuilder.newBuilder().weakValues().build();
    }

    /**
     * Public constructor creates new component list.
     */
    public ComponentList() {
        this(ArrayList::new);
    }

    /**
     * Method returns set of component names.
     *
     * @return set of names
     */
    public Set<String> names() {
        return Set.copyOf(names);
    }

    /**
     * Method returns number of components.
     *
     * @return number of components
     */
    public int size() {
        return names.size();
    }

    /**
     * Method returns component by index.
     *
     * @param index index
     * @return component at given index
     *
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    public Component get(int index) {
        return entries.get(index).getComponent();
    }

    /**
     * Method searches entry with given name.
     *
     * @param name name of the component
     * @return optional entry
     */
    private Optional<Entry> find(String name) {
        try {
            return Optional.of(cache.get(name, () -> entries.stream().dropWhile(e -> !e.getName().equals(name)).findFirst().orElseThrow()));
        } catch (ExecutionException e) {
            return Optional.empty();
        }
    }

    /**
     * Method returns component by name.
     *
     * @param name component name
     * @return component
     *
     * @throws NoSuchElementException if component with given name was not found
     */
    public Component get(String name) {
        return find(name).orElseThrow().getComponent();
    }

    /**
     * Method returns list of components of given type.
     *
     * @param type component type
     * @return list of components
     * @param <T> component type
     */
    public <T extends Component> List<T> get(ComponentType<T> type) {
        return entries.stream().filter(e -> type.isInstance(e.getComponent())).map(e -> type.cast(e.getComponent())).toList();
    }

    /**
     * Method adds component with given name to the end of list.
     *
     * @param name name
     * @param component component
     *
     * @throws NoSuchPropertyException if {@code component} is {@code null}
     */
    public void add(String name, Component component) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(component, "component");
        synchronized (this) {
            if (names.contains(name)) {
                throw new IllegalArgumentException("name '" + name + "' already exists");
            }
            names.add(name);
            entries.add(new Entry(name, component));
        }
    }

    /**
     * Method sets new value for given name.
     *
     * @param name component name
     * @param component new value
     * @return old value
     *
     * @throws NoSuchElementException if component with given name was not found
     * @throws NoSuchPropertyException if {@code component} is {@code null}
     */
    public Component replace(String name, Component component) {
        Objects.requireNonNull(name, "name");
        Objects.requireNonNull(component, "component");
        synchronized (this) {
            Entry entry = find(name).orElseThrow();
            entry.setComponent(component);
            cache.invalidate(name);
            return entry.getComponent();
        }
    }

    /**
     * Method sets new value for given index.
     *
     * @param index index
     * @param component new value
     * @return old value
     *
     * @throws IndexOutOfBoundsException if index is out of bounds
     * @throws NoSuchPropertyException if {@code component} is {@code null}
     */
    public Component set(int index, Component component) {
        Objects.checkIndex(index, entries.size());
        Objects.requireNonNull(component, "component");
        synchronized (this) {
            Entry entry = entries.get(index);
            entry.setComponent(component);
            cache.invalidate(entry.getName());
            return entry.getComponent();
        }
    }

    /**
     * Method removes component with given index.
     *
     * @param index index
     * @return removed component
     *
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    public Component remove(int index) {
        Objects.checkIndex(index, entries.size());
        synchronized (this) {
            Entry entry = entries.remove(index);
            cache.invalidate(entry.getName());
            return entry.getComponent();
        }
    }

    /**
     * Method removes component with given name.
     *
     * @param name component name
     * @return removed component or {@code null} if component with given name was not found
     */
    public Component remove(String name) {
        Entry entry;
        try {
            entry = find(name).orElse(null);
        } catch (NoSuchElementException e) {
            return null;
        }
        if (entry != null) {
            synchronized (this) {
                cache.invalidate(name);
                entries.remove(entry);
                return entry.getComponent();
            }
        } else return null;
    }

    /**
     * Method clears the list.
     */
    public synchronized void clear() {
        entries.clear();
        names.clear();
        cache.invalidateAll();
    }

    /**
     * Method returns iterator of components in their list order.
     *
     * @return iterator
     */
    @Override
    public Iterator<Component> iterator() {
        return entries.stream().map(Entry::getComponent).iterator();
    }

    /**
     * Method returns copy of the list with copies of all components.
     *
     * @return copy
     */
    @Override
    public ComponentList clone() {
        ComponentList clone;
        try {
            clone = (ComponentList) super.clone();
        } catch (CloneNotSupportedException e) {
            //should never happen
            throw new RuntimeException(e);
        }
        clone.names = new TreeSet<>(names);
        clone.entries = new ArrayList<>(entries.stream()
                .map(e -> new Entry(e.getName(), e.getComponent().clone()))
                .toList());
        clone.cache = CacheBuilder.newBuilder().weakValues().build();
        return clone;
    }

    /**
     * Method creates new entry with given name and component.
     *
     * @param name component name
     * @param component component value
     * @return new entry
     */
    public static Entry entry(String name, Component component) {
        return new Entry(name, component);
    }

    /**
     * Method creates new list from given map. Components will be ordered according to
     * map order.
     *
     * @param map map of components
     * @return new list
     *
     * @throws NoSuchPropertyException if map value is {@code null}
     */
    public static ComponentList fromMap(Map<String, Component> map) {
        Objects.requireNonNull(map, "map");
        return new ComponentList(() -> map.entrySet().stream().map(e -> new Entry(e.getKey(), e.getValue()))
                .collect(ArrayList::new, ArrayList::add, ArrayList::addAll));
    }

    /**
     * Method creates new list from given list and name function.
     *
     * @param list list of components
     * @param nameMapper function that returns component name
     * @return new list
     *
     * @throws IllegalArgumentException if any duplicate name was found
     */
    public static ComponentList fromList(List<Component> list, Function<Component, String> nameMapper) {
        return new ComponentList(() -> list.stream().map(e -> new Entry(nameMapper.apply(e), e))
                .collect(Collectors.toList()));
    }

    /**
     * Method creates new list from given entries.
     *
     * @param entries array of entries
     * @return new list
     *
     * @throws IllegalArgumentException if any duplicate name was found
     */
    public static ComponentList ofArray(Entry... entries) {
        return new ComponentList(() -> new ArrayList<>(Arrays.asList(entries)));
    }

    /**
     * Method creates new linked list from given entries.
     *
     * @param entries array of entries
     * @return new list
     *
     * @throws IllegalArgumentException if any duplicate name was found
     */
    public static ComponentList ofLinked(Entry... entries) {
        return new ComponentList(() -> new LinkedList<>(Arrays.asList(entries)));
    }
}
