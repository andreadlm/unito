package graph;

import java.util.*;

/**
 * Class representing a graph data structure.
 * A graph can be directed or undirected.
 * If the graph is directed, an edge connecting two nodes has direction.
 * @param <V> type of the vertexes.
 * @param <L> type of the labels.
 *
 * @author Andrea Delmastro
 */
public class Graph<V, L> {
  /**
   * Adjacency list.
   * Each list contains the vertex from which the edges depart and an entry for any edge.
   */
  private class AdjList {
    private final V vertex;
    private final LinkedList<AdjListEntry> list;

    private AdjList(V vertex) {
      this.vertex = vertex;
      list = new LinkedList<>();
    }
  }

  /**
   * Adjacency list entry.
   * Each entry contains the ending vertex and the label associated to the edge.
   */
  private class AdjListEntry {
    private final V toVertex;
    private final L label;

    /**
     * Utility constructor, it is used to search for a particular vertex inside the adjacency list.
     * @param toVertex the vertex to which the edge arrives.
     */
    private AdjListEntry(V toVertex) {
      this.toVertex = toVertex;
      this.label = null;
    }

    /**
     * Standard constructor.
     * @param toVertex the vertex to which the edge arrives.
     * @param label the label associated to the edge.
     */
    private AdjListEntry(V toVertex, L label) {
      this.toVertex = toVertex;
      this.label = label;
    }

    /**
     * Equals implementation used to search for a particular vertex inside the adjacency list.
     * Two adjacency list entry are considered the same if they represent the same arriving vertex, two edges between
     * the same vertexes and with different labels cannot exist.
     * @param o the object to be compared.
     * @return true if the two objects are the same, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      @SuppressWarnings("unchecked") AdjListEntry adjListEntry = (AdjListEntry) o; /* No warning needed: the cast is already checked before */
      return toVertex.equals(adjListEntry.toVertex);
    }
  }

  /**
   * Edge of the graph.
   * Each edge contains the two connected vertexes and the label associated to the edge.
   * If the edge refers to an undirected graph, the two vertex are interchangeable.
   * This is an immutable class, it is only a convenient way to represent an edge and all the operations
   * on the graph must be done using the main class methods.
   */
  public class Edge {
    private final V from;
    private final V to;
    private final L label;

    private Edge(V from, V to, L label) {
      this.from = from;
      this.to = to;
      this.label = label;
    }

    /**
     * Returns the vertex from which the edge departs.
     * @return the vertex from which the edge departs.
     */
    public V getFrom() {
      return from;
    }

    /**
     * Returns the vertex to which the edge arrives.
     * @return the vertex to which the edge arrives.
     */
    public V getTo() {
      return to;
    }

    /**
     * Returns the label associated to the edge.
     * @return the label associated to the edge.
     */
    public L getLabel() {
      return label;
    }

    /**
     * Two edges are defined equals if they connect the same vertexes with the same label.
     * @param o the object to be compared.
     * @return true if the two objects are equal, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
      if(this == o) return true;
      if(o == null || getClass() != o.getClass()) return false;
      @SuppressWarnings("unchecked") Edge edge = (Edge) o; /* No warning needed: the cast is already checked before */
      return Objects.equals(this.from, edge.from) &&
             Objects.equals(this.to, edge.to) &&
             Objects.equals(this.label, edge.label);
    }

    /**
     * Returns a string representing the edge. The string contains the departing and arriving vertexes and the
     * associated label.
     * @return a string representing the edge. *
     */
    @Override
    public String toString() {
      return "{" + from + "-[" + label + "]->" + to + "}";
    }
  }

  private final boolean directed;

  /* Hashmap used to map each vertex to its own position inside the adjacency list array.
   * This implementation allows to retrieve a single adjacency list in O(1) time, as well as checking
   * if a vertex is contained inside the graph and adding/removing an edge, all in constant time. */
  private final HashMap<V, Integer> idx;
  /* Array of adjacency lists */
  private final ArrayList<AdjList> adjLists;

  /**
   * Constructor for the graph class.
   * @param directed if true, a directed graph is created, if false an undirected graph is created.
   *                 The type of the graph cannot be changed in a second time, but it can be checked
   *                 using the {@link graph.Graph#isDirected()} method.
   */
  public Graph(boolean directed) {
    this.directed = directed;
    idx = new HashMap<>();
    adjLists = new ArrayList<>();
  }

  /**
   * Returns true if the graph is directed, false if the graph is undirected.
   * The type of the graph cannot be changed.
   * @return true if the graph is directed, false if the graph is undirected.
   */
  public boolean isDirected() {
    return this.directed;
  }

  /**
   * Returns the number of vertexes stored inside the graph.
   * @return the number of vertexes stored inside the graph
   */
  public int getVertexesCount() {
    return adjLists.size();
  }

  /**
   * Adds a vertex to the graph.
   * If the graph already contains the vertex, the vertex is not added twice.
   * @param vertex the vertex to be added.
   * @throws NullPointerException if the vertex passed is null.
   * @throws GraphException if the graph already contains the vertex.
   */
  public void addVertex(V vertex) throws NullPointerException, GraphException {
    if(vertex == null)
      throw new NullPointerException("vertex parameter cannot be null");
    if(containsVertex(vertex))
      throw new GraphException("the vertex has already been added to the graph");

    idx.put(vertex, adjLists.size());
    adjLists.add(new AdjList(vertex));
  }

  /**
   * Checks if a vertex is contained inside the graph.
   * @param vertex the vertex to be searched.
   * @return true is the vertex is contained inside the graph, false otherwise.
   * @throws NullPointerException if the vertex passed as parameter is null.
   */
  public boolean containsVertex(V vertex) throws NullPointerException {
    if(vertex == null)
      throw new NullPointerException("vertex parameter cannot be null");

    return idx.containsKey(vertex);
  }

  /**
   * Removes a vertex and its corresponding edges (departing and arriving to the edge) from the graph.
   * @param vertex the vertex to be removed.
   * @throws NullPointerException if the vertex passed as parameter is null.
   * @throws GraphException if the vertex isn't contained inside the graph.
   */
  public void removeVertex(V vertex) throws NullPointerException, GraphException {
    if(vertex == null)
      throw new NullPointerException("vertex parameter cannot be null");
    if(!containsVertex(vertex))
      throw new GraphException("the vertex isn't contained inside the graph");

    Integer idxVertex = idx.get(vertex);
    for(int i = 0; i < adjLists.size(); i++) {
      AdjList adjList = adjLists.get(i);
      if(i > idxVertex) idx.replace(adjList.vertex, i - 1);
      adjList.list.remove(new AdjListEntry(vertex));
    }
    adjLists.remove((int)idxVertex);
    idx.remove(vertex);
  }

  /**
   * Returns the edge connecting from vertex and to vertex if exist, null otherwise.
   * @param from the vertex from which the edge departs.
   * @param to the vertex to which the edge arrives.
   * @return the edge connecting the vertexes.
   */
  private Edge getEdge(V from, V to) {
    Integer idxFrom = idx.get(from);
    LinkedList<AdjListEntry> adjList = adjLists.get(idxFrom).list;
    for(AdjListEntry edge : adjList)
      if(edge.toVertex == to) return new Edge(from, to, edge.label);
    return null;
  }

  /**
   * Checks if an edge is contained inside the graph.
   * If the graph is undirected, the departing and the arriving vertex are interchangeable.
   * @param from the vertex from which the edge departs.
   * @param to the vertex to which the edge arrives.
   * @return true if the edge is contained inside the graph, false otherwise.
   * @throws NullPointerException if one of the two vertexes passed as parameters (or both) is null.
   * @throws GraphException if one of the two vertexes passed as parameters (or both) aren't part of the graph.
   */
  public boolean containsEdge(V from, V to) throws NullPointerException, GraphException {
    if(from == null)
      throw new NullPointerException("from parameter cannot be null");
    if(to == null)
      throw new NullPointerException("to parameter cannot be null");
    if(!containsVertex(from))
      throw new GraphException("vertex from isn't contained inside the graph");
    if(!containsVertex(to))
      throw new GraphException("vertex to isn't contained inside the graph");

    Edge edge = getEdge(from, to);
    return edge != null;
  }

  /**
   * Adds and edge to the graph.
   * If the graph is undirected, the departing and the arriving vertex are interchangeable.
   * The vertexes must be added before using the {@link graph.Graph#addVertex(V)} method.
   * If one of the vertexes is not contained inside the graph, no operation is performed.
   * @param from the vertex from which the edge departs.
   * @param to the vertex to which the edge arrives.
   * @param label the label associated to the edge.
   * @throws NullPointerException if one of the parameters is null.
   * @throws GraphException if one of the two vertexes passed as parameters (or both) aren't part of the graph or if the
   * edge already exists.
   */
  public void addEdge(V from, V to, L label) throws NullPointerException {
    if(from == null)
      throw new NullPointerException("from parameter cannot be null");
    if(to == null)
      throw new NullPointerException("to parameter cannot be null");
    if(containsEdge(from, to))
      throw new GraphException("the edge has already been added to the graph");
    if(label == null)
      throw new NullPointerException("label parameter cannot be null");

    insertEdge(from, to, label);
    if(!directed) insertEdge(to, from, label);
  }

  /**
   * Adds and edge to the graph.
   * @param from the vertex from which the edge departs.
   * @param to the vertex to which the edge arrives.
   * @param label the label associated to the edge.
   */
  private void insertEdge(V from, V to, L label) {
    Integer idxFrom = idx.get(from);
    adjLists.get(idxFrom).list.addFirst(new AdjListEntry(to, label));
  }

  /**
   * Returns the label associated to an edge.
   * If the graph is undirected, the departing and arriving vertexes are interchangeable.
   * @param from the vertex from which the edge departs.
   * @param to the vertex to which the edge arrives.
   * @return the label associated to the edge.
   * @throws NullPointerException if on of the parameters is null.
   * @throws GraphException if one of the two vertexes passed as parameters (or both) aren't part of the graph or if the
   * edge isn't contained inside the graph.
   */
  public L getEdgeLabel(V from, V to) throws NullPointerException, GraphException {
    if(from == null)
      throw new NullPointerException("from parameter cannot be null");
    if(to == null)
      throw new NullPointerException("to parameter cannot be null");
    if(!containsVertex(from))
      throw new GraphException("vertex from isn't contained inside the graph");
    if(!containsVertex(to))
      throw new GraphException("vertex to isn't contained inside the graph");

    Edge edge = getEdge(from, to);
    if(edge == null)
      throw new GraphException("edge isn't contained inside the graph");
    else
      return edge.label;
  }

  /**
   * Removes an edge from the graph.
   * @param from the vertex from which the edge departs.
   * @param to the vertex to which the edge arrives.
   * @throws NullPointerException if one of the parameters is null.
   * @throws GraphException if the edge isn't contained inside the graph.
   */
  public void removeEdge(V from, V to) throws NullPointerException, GraphException {
    if(from == null)
      throw new NullPointerException("from parameter cannot be null");
    if(to == null)
      throw new NullPointerException("to parameter cannot be null");
    if(!containsEdge(from, to))
      throw new GraphException("edge isn't contained inside the graph");

    AdjList adjList = adjLists.get(idx.get(from));
    adjList.list.remove(new AdjListEntry(to));
  }

  /**
   * Returns the number of edges contained inside the graph.
   * If the graph is undirected, each edge is counted as two separate edges, one in a direction, on in the other direction.
   * @return the number of edges contained inside the graph.
   */
  public int getEdgesCount() {
    int count = 0;
    for(AdjList adjList: adjLists)
      count += adjList.list.size();
    return count;
  }

  /**
   * Returns a list containing all the vertexes stored inside the graph.
   * @return a list containing all the vertexes stored inside the graph;
   */
  public LinkedList<V> getVertexesList() {
    LinkedList<V> vertexesList = new LinkedList<>();
    for(AdjList adjList : adjLists)
      vertexesList.add(adjList.vertex);
    return vertexesList;
  }

  /**
   * Returns a LinkedList containing all the edges of the graph.
   * If the graph is undirected, the edges are showed twice, once in a direction and once in the other direction.
   * @return a LinkedList containing all the edges of the graph.
   * @see Edge
   */
  public LinkedList<Edge> getEdgesList() {
    LinkedList<Edge> edgesList = new LinkedList<>();
    for(AdjList adjList : adjLists)
      for(AdjListEntry adjListEntry : adjList.list)
        edgesList.addFirst(new Edge(adjList.vertex, adjListEntry.toVertex, adjListEntry.label));
    return edgesList;
  }

  /**
   * Returns a LinkedList containing all the vertexes connected to the passed vertex.
   * @param vertex the vertex you want to get the adjacent vertexes.
   * @return a LinkedList containing all the vertexes connected to the passed vertex.
   * @throws NullPointerException if vertex is null.
   * @throws GraphException is the vertex isn't contained inside the graph.
   */
  public LinkedList<V> getAdjacentVertexes(V vertex) throws NullPointerException, GraphException {
    if(vertex == null)
      throw new NullPointerException("vertex parameter cannot be null");
    if(!containsVertex(vertex))
      throw new GraphException("vertex isn't contained inside the graph");

    Integer idxVertex = idx.get(vertex);
    LinkedList<V> adjVertexesList = new LinkedList<>();
    AdjList adjList = adjLists.get(idxVertex);
    for(AdjListEntry edge : adjList.list)
      adjVertexesList.addFirst(edge.toVertex);
    return adjVertexesList;
  }

  /**
   * Returns a string representing the graph. The string contains all the vertexes and edges stored inside the graph.
   * This method is a wrapper of {@link graph.Graph#toJSON()} method.
   * @return a string representing the graph.
   */
  @Override
  public String toString() {
    return toJSON();
  }

  /**
   * Returns a JSON representation of the graph.
   * @return a JSON representation of the graph.
   */
  public String toJSON() {
    StringBuilder ret = new StringBuilder("{");
    ret.append("\n\t\"directed\": ")
        .append(directed)
        .append(",")
        .append("\n\t\"vertexes count\": ")
        .append(getVertexesCount())
        .append(",")
        .append("\n\t\"vertexes\": [");
    LinkedList<V> vertexesList = getVertexesList();
    for(V vertex : vertexesList)
      ret.append("\n\t\t\"")
          .append(vertex)
          .append("\",");
    if(vertexesList.size()  != 0) ret.deleteCharAt(ret.length() - 1);
    ret.append("\n\t],")
        .append("\n\t\"edges count\": ")
        .append(getEdgesCount()).append(",")
        .append("\n\t\"edges\": " + "[");
    LinkedList<Edge> edgesList = getEdgesList();
    for(Edge edge : edgesList) {
      ret.append("\n\t\t{").append("\n\t\t\t\"from\": \"").append(edge.from).append("\",")
          .append("\n\t\t\t\"to\": \"").append(edge.to).append("\",")
          .append("\n\t\t\t\"label\": \"").append(edge.label).append("\"")
          .append("\n\t\t},");
    }
    if(edgesList.size() != 0) ret.deleteCharAt(ret.length() - 1);
    ret.append("\n\t]\n}");
    return ret.toString();
  }

  /**
   * Returns a CSV representation of the graph.
   * @return a CSV representation of the graph.
   */
  public String toCSV() {
    StringBuilder ret = new StringBuilder();
    LinkedList<Edge> edgesList = getEdgesList();
    for(Edge edge : edgesList)
      ret.append(edge.from).append(",").append(edge.to).append(",").append(edge.label).append("\n");
    return ret.toString();
  }
}
