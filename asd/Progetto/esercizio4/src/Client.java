import graph.Graph;
import graph.GraphException;
import graph.Kruskal;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.StringTokenizer;

/**
 * The client reads a csv file containing information about a graph and launches the Kruskal algorithm to find
 * the minimum spanning tree on it.
 *
 * @author Andrea Delmastro
 */
public class Client {
  public static void main(String[] args) {
    if(args.length == 1 && args[0].equals("--help")) {
      System.out.print("Usage: client <INPUT_FILE_PATH> <OUTPUT_FILE_PATH>\n" +
          "Finds a minimum spanning forest of the graph described in the <INPUT_FILE_PATH> and prints the result in <OUTPUT_FILE_PATH>.\n" +
          "The graph stored in the input file is interpreted as undirected.\n\n" +
          "The record structure must be in accordance with the project specifications:\n" +
          "\t- from (string)\n" +
          "\t- to (string)\n" +
          "\t- distance (float)\n");
        System.exit(0);
    } else if(args.length != 2) {
      System.err.println("Usage: client <INPUT_FILE_PATH> <OUTPUT_FILE_PATH>");
      System.exit(-1);
    }

    Path inputFilePath = Paths.get(args[0]);
    Path outputFilePath = Paths.get(args[1]);
    if(!Files.exists(outputFilePath))
      try {
        if(outputFilePath.getParent() != null)
          Files.createDirectory(outputFilePath.getParent());
        Files.createFile(outputFilePath);
      } catch(IOException ioe) {
        System.err.println(ioe.toString());
        System.exit(-1);
      }

    Graph<String, Double> map = new Graph<>(false);

    System.out.print("(1) Loading data ...: ");
    StringTokenizer stringTokenizer;
    String source;
    String destination;
    Double distance;
    String line;
    try {
      BufferedReader reader = Files.newBufferedReader(inputFilePath, StandardCharsets.UTF_8);
      while((line = reader.readLine()) != null) {
        stringTokenizer = new StringTokenizer(line, ",");
        source = stringTokenizer.nextToken();
        destination = stringTokenizer.nextToken();
        distance = Double.parseDouble(stringTokenizer.nextToken());

        if(!map.containsVertex(source)) map.addVertex(source);
        if(!map.containsVertex(destination)) map.addVertex(destination);
        map.addEdge(source, destination, distance);
      }
    } catch(GraphException | IOException e) {
      System.err.println(e.toString());
      System.exit(-1);
    }
    System.out.println("    finished");

    System.out.print("(2) Finding the MST ...: ");
    Graph<String, Double> mapMST = Kruskal.getMST(map);
    System.out.println(" finished");

    System.out.print("(3) Printing data ...:");
    try(BufferedWriter writer = Files.newBufferedWriter(outputFilePath, StandardCharsets.UTF_8)) {
      writer.write(mapMST.toJSON());
    } catch(IOException ioe) {
      System.err.println(ioe.toString());
    }
    System.out.println("    finished");
  }
}
