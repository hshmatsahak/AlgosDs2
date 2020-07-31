import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class SAP {
    private Digraph graph;
    private HashMap<Bag<Integer>, Integer> cache;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G){
        if (G == null)
            throw new IllegalArgumentException();
        graph = new Digraph(G);
        cache = new HashMap<>();
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w){
        if ((v>=graph.V() || v < 0) || (w >= graph.V() || w < 0)) throw new IllegalArgumentException();
        Bag<Integer> search = new Bag<>();
        search.add(v);
        search.add(w);
        if (cache.containsKey(search))
            return cache.get(search);
        BreadthFirstDirectedPaths vpath = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths wpath = new BreadthFirstDirectedPaths(graph, w);
        int length = Integer.MAX_VALUE;
        boolean pathexists = false;
        for (int vertex = 0; vertex < graph.V(); vertex++){
            int temp = vpath.distTo(vertex)+wpath.distTo(vertex);
            if (vpath.hasPathTo(vertex) && wpath.hasPathTo(vertex) && temp<length){
                pathexists = true;
                length = temp;
            }
        }
        if (!pathexists) {
            cache.put(search, -1);
            return -1;
        }
        cache.put(search, length);
        return length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w){
        if ((v>=graph.V() || v < 0) || (w >= graph.V() || w < 0)) throw new IllegalArgumentException();
        BreadthFirstDirectedPaths vpath = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths wpath = new BreadthFirstDirectedPaths(graph, w);
        int common_ancestor = -1;
        int length = Integer.MAX_VALUE;
        for (int vertex = 0; vertex < graph.V(); vertex++){
            if (vpath.hasPathTo(vertex) && wpath.hasPathTo(vertex) && vpath.distTo(vertex)+wpath.distTo(vertex)<length){
                length = vpath.distTo(vertex)+wpath.distTo(vertex);
                common_ancestor = vertex;
            }
        }
        return common_ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w){
        if (v == null) throw new IllegalArgumentException();
        if (w == null) throw new IllegalArgumentException();
        /*if (v instanceof Bag) {
            if (((Bag<Integer>) v).size() == 0) return -1;
        }
        if (w instanceof Bag) {
            if (((Bag<Integer>) w).size() == 0) return -1;
        }*/
        int vsize = 0;
        int wsize = 0;
        for (Integer i : v) vsize++;
        for (Integer i: w) wsize++;
        if (vsize == 0 || wsize == 0) return -1;
        for (Integer i : v){
            if (i == null) throw new IllegalArgumentException();
            //if (i < 0 || i >= graph.V()) throw new IllegalArgumentException();
        }
        for (Integer i : w){
            if (i == null) throw new IllegalArgumentException();
            //if (i < 0 || i >= graph.V()) throw new IllegalArgumentException();
        }
        BreadthFirstDirectedPaths vpath = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths wpath = new BreadthFirstDirectedPaths(graph, w);
        int length = Integer.MAX_VALUE;
        for (int vertex = 0; vertex < graph.V(); vertex++){
            int temp = vpath.distTo(vertex)+wpath.distTo(vertex);
            if (vpath.hasPathTo(vertex) && wpath.hasPathTo(vertex) && temp<length){
                length = temp;
            }
        }
        return length;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w){
        if (v == null) throw new IllegalArgumentException();
        if (w == null) throw new IllegalArgumentException();
        /*if (v instanceof Bag) {
            if (((Bag<Integer>) v).size() == 0) return -1;
        }
        if (w instanceof Bag) {
            if (((Bag<Integer>) w).size() == 0) return -1;
        }*/
        int vsize = 0;
        int wsize = 0;
        for (Integer i : v) vsize++;
        for (Integer i: w) wsize++;
        if (vsize == 0 || wsize == 0) return -1;
        for (Integer i : v){
            if (i == null) throw new IllegalArgumentException();
            //if (i < 0 || i >= graph.V()) throw new IllegalArgumentException();
        }
        for (Integer i : w){
            if (i == null) throw new IllegalArgumentException();
            //if (i < 0 || i >= graph.V()) throw new IllegalArgumentException();
        }
        BreadthFirstDirectedPaths vpath = new BreadthFirstDirectedPaths(graph, v);
        BreadthFirstDirectedPaths wpath = new BreadthFirstDirectedPaths(graph, w);
        int common_ancestor = -1;
        int length = Integer.MAX_VALUE;
        for (int vertex = 0; vertex < graph.V(); vertex++){
            if (vpath.hasPathTo(vertex) && wpath.hasPathTo(vertex) && vpath.distTo(vertex)+wpath.distTo(vertex)<length){
                length = vpath.distTo(vertex)+wpath.distTo(vertex);
                common_ancestor = vertex;
            }
        }
        return common_ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
