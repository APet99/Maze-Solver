package misc.graphs;

import datastructures.concrete.*;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import datastructures.interfaces.ISet;
import misc.Searcher;
import misc.exceptions.NoPathExistsException;


/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 * <p>
 * Note: This class is not meant to be a full-featured way of representing a graph.
 * We stick with supporting just a few, core set of operations needed for the
 * remainder of the project.
 */
public class Graph<V, E extends Edge<V> & Comparable<E>> {
    IDictionary<V, ISet<E>> adj;
    IList<E> sortedEdges;
    ArrayDisjointSet forest;
    // NOTE 1:
    //
    // Feel free to add as many fields, private helper methods, and private
    // inner classes as you want.
    //
    // And of course, as always, you may also use any of the data structures
    // and algorithms we've implemented so far.
    //
    // Note: If you plan on adding a new class, please be sure to make it a private
    // static inner class contained within this file. Our testing infrastructure
    // works by copying specific files from your project to ours, and if you
    // add new files, they won't be copied and your code will not compile.
    //
    //
    // NOTE 2:
    //
    // You may notice that the generic types of Graph are a little bit more
    // complicated then usual.
    //
    // This class uses two generic parameters: V and E.
    //
    // - 'V' is the type of the vertices in the graph. The vertices can be
    //   any type the client wants -- there are no restrictions.
    //
    // - 'E' is the type of the edges in the graph. We've contrained Graph
    //   so that E *must* always be an instance of Edge<V> AND Comparable<E>.
    //
    //   What this means is that if you have an object of type E, you can use
    //   any of the methods from both the Edge interface and from the Comparable
    //   interface
    //
    // If you have any additional questions about generics, or run into issues while
    // working with them, please ask for help ASAP.
    //
    // Working with generics is really not the focus of this class, so if you
    // get stuck, let us know we'll try and help you get unstuck as best as we can.

    /**
     * Constructs a new graph based on the given vertices and edges.
     *
     * @throws IllegalArgumentException if any of the edges have a negative weight
     * @throws IllegalArgumentException if one of the edges connects to a vertex not
     *                                  present in the 'vertices' list
     */
    public Graph(IList<V> vertices, IList<E> edges) {
        sortedEdges = Searcher.topKSort(edges.size(), edges);
        if (sortedEdges == null || sortedEdges.get(0).getWeight() < 0) {
            throw new IllegalArgumentException("ERROR: Can not have a negative weight.");
        }
        forest = new ArrayDisjointSet(sortedEdges.size());
        adj = new ChainedHashDictionary<>();

        for (V v : vertices) {
            adj.put(v, new ChainedHashSet<>());
            forest.makeSet(v);
        }
        for (E e : edges) {
            V vertex1 = e.getVertex1();
            V vertex2 = e.getVertex2();
            if (!adj.containsKey(vertex1) || !adj.containsKey(vertex2)) {
                throw new IllegalArgumentException("ERROR: The vertex is not contained");
            }
            adj.get(vertex1).add(e);
            adj.get(vertex2).add(e);
        }
    }

    /**
     * Sometimes, we store vertices and edges as sets instead of lists, so we
     * provide this extra constructor to make converting between the two more
     * convenient.
     */
    public Graph(ISet<V> vertices, ISet<E> edges) {
        // You do not need to modify this method.
        this(setToList(vertices), setToList(edges));
    }

    // You shouldn't need to call this helper method -- it only needs to be used
    // in the constructor above.
    private static <T> IList<T> setToList(ISet<T> set) {
        IList<T> output = new DoubleLinkedList<>();
        for (T item : set) {
            output.add(item);
        }
        return output;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return adj.size();
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return sortedEdges.size();
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of
     * this graph.
     * <p>
     * If there exists multiple valid MSTs, return any one of them.
     * <p>
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<E> findMinimumSpanningTree() {
        ISet<E> mst = new ChainedHashSet<>();
        for (E e : sortedEdges) {
            if (forest.findSet(e.getVertex1()) != forest.findSet(e.getVertex2())) {
                mst.add(e);
                forest.union(e.getVertex1(), e.getVertex2());
            }
        }
        return mst;
    }

    /**
     * Returns the edges that make up the shortest path from the start
     * to the end.
     * <p>
     * The first edge in the output list should be the edge leading out
     * of the starting node; the last edge in the output list should be
     * the edge connecting to the end node.
     * <p>
     * Return an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException if there does not exist a path from the start to the end
     */
    // FIXME: 11/7/2019 Clean up this section
    public IList<E> findShortestPathBetween(V start, V end) {
        IDictionary<V, Double> weights = new ChainedHashDictionary<>();
        IDictionary<V, IList<E>> paths = new ChainedHashDictionary<>();
        IPriorityQueue<VertexInfo<V>> nextVertex = new ArrayHeap<>();

        if (start.equals(end)){
            return new DoubleLinkedList<>();
        }
        validateIndex(start);
        validateIndex(end);
        for (KVPair<V, ISet<E>> pair : adj) {
            weights.put(pair.getKey(), Double.POSITIVE_INFINITY);
        }
        weights.put(start, 0.0);
        paths.put(start, new DoubleLinkedList<>());

        //base case:
        nextVertex.insert(new VertexInfo<V>(start, 0));
        while (!nextVertex.isEmpty()) {
            VertexInfo<V> info = nextVertex.removeMin();
            if (weights.get(info.v) >= info.weight) {
                for (E e : adj.get(info.v)) {
                    V v1 = info.v;
                    V v2 = (info.v.equals(e.getVertex1())) ? e.getVertex2() : e.getVertex1();

                    if (weights.get(v2) > weights.get(v1) + e.getWeight()) {
                        weights.put(v2, weights.get(v1) + e.getWeight());
                        IList<E> newPath = new DoubleLinkedList<>();
                        for (E e2 : paths.get(v1)) {
                            newPath.add(e2);
                        }
                        newPath.add(e);
                        paths.put(v2, newPath);
                        nextVertex.insert(new VertexInfo<>(v2, weights.get(v2)));
                    }
                }
            }
        }
        if (!paths.containsKey(end)){
            throw new NoPathExistsException("ERROR: The path does not exist.");
        }
        return paths.get(end);
    }


    private void validateIndex(V v) {
        if (!adj.containsKey(v)) {
            throw new IllegalArgumentException("ERROR: The vertex is not valid.");
        }
    }


    private static class VertexInfo<V> implements Comparable<VertexInfo<V>> {
        public V v;
        public double weight;

        public VertexInfo(V v, double weight) {
            this.v = v;
            this.weight = weight;
        }

        @Override
        public int compareTo(VertexInfo o) {
            double diff = weight - o.weight;
            if (diff == 0) {
                return 0;
            }
            return (diff > 0) ? 1 : -1;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj != null & getClass() == obj.getClass()) {
                VertexInfo vInfo = (VertexInfo) obj;
                return vInfo.v == this.v;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return v.hashCode();
        }
    }
}
