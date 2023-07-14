package project;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

/**
 Clase huffmanCoding
 Esta clase se encarga de codificar un texto en base a un arbol de huffman
 @Author: <Kahyberth Steven Gonzales, Carlos Eduardo Guerrero, Juan Camilo Varela, Yuliana Serna>
 @Version: <1>
 */
class HuffmanCoding {
  private HuffmanBinaryTree tree;
  private Map<Character, String> encodingTable;

  public HuffmanCoding(String text) {
    buildHuffmanTree(text);
    buildEncodingTable();
  }

  public String encode(String input) {
    StringBuilder encodedString = new StringBuilder();
    for (char c : input.toCharArray()) {
      encodedString.append(encodingTable.get(c));
    }
    return encodedString.toString();
  }

  public HuffmanBinaryTree getTree() {
    return tree;
  }

  public Map<Character, String> getTable() {
    return encodingTable;
  }

  public Map<String, Object> getSummary() {
    int numNodes = countNodes(tree.getRoot());
    int depth = calculateDepth(tree.getRoot());
    int originalBits = calculateOriginalBits();
    int compressedBits = calculateCompressedBits();
    double compressionRatio = (double) compressedBits / originalBits;

    Map<String, Object> summary = new HashMap<>();
    summary.put("CompressionRatio", compressionRatio);
    summary.put("NumNodes", numNodes);
    summary.put("Depth", depth);
    return summary;
  }

  private void buildHuffmanTree(String text) {
    Map<Character, Integer> frequencyMap = new HashMap<>();
    for (char c : text.toCharArray()) {
      frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
    }

    PriorityQueue<HuffmanNode> queue = new PriorityQueue<>();
    for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
      HuffmanNode node = new HuffmanNode(entry.getKey(), entry.getValue());
      queue.add(node);
    }

    while (queue.size() > 1) {
      HuffmanNode left = queue.poll();
      HuffmanNode right = queue.poll();

      HuffmanNode parent = new HuffmanNode('\0', left.getFrequency() + right.getFrequency());
      parent.setLeft(left);
      parent.setRight(right);

      queue.add(parent);
    }

    HuffmanNode root = queue.poll();
    tree = new HuffmanBinaryTree(root);
  }
  //Objeto encodingTable limpio
  private void buildEncodingTable() {
    encodingTable = new HashMap<>();
    buildEncodingTableRecursive(tree.getRoot(), "");
  }

  //Se encarga de construir la tabla
  private void buildEncodingTableRecursive(HuffmanNode node, String code) {
    if (node.isLeaf()) {
      encodingTable.put(node.getCharacter(), code);
    } else {
      buildEncodingTableRecursive(node.getLeft(), code + "0");
      buildEncodingTableRecursive(node.getRight(), code + "1");
    }
  }


  //Se encarga de contar los nodos del Arbol de Huffman
  private int countNodes(HuffmanNode node) {
    if (node == null) {
      return 0;
    }
    return 1 + countNodes(node.getLeft()) + countNodes(node.getRight());
  }

  //Método para calcular la profundidad
  //Toma como argumento un nodo en el árbol y devuelve la profundidad del subárbol que tiene a ese nodo como raíz.
  private int calculateDepth(HuffmanNode node) {
    if (node == null) {
      return 0;
    }
    int leftDepth = calculateDepth(node.getLeft());
    int rightDepth = calculateDepth(node.getRight());
    return 1 + Math.max(leftDepth, rightDepth);
  }

  //Método para representar la cantidad de bits necesarios del texto original
  private int calculateOriginalBits() {
    int totalBits = 0;
    for (char c : encodingTable.keySet()) {
      int frequency = getFrequency(c);
      totalBits += frequency * 256;
    }
    return totalBits;
  }

  //Este método se encarga de calcular el número de bits necesarios para representar el texto codificado
  private int calculateCompressedBits() {
    int totalBits = 0;
    for (char c : encodingTable.keySet()) {
      int frequency = getFrequency(c);
      String code = encodingTable.get(c);
      totalBits += frequency * code.length();
    }
    return totalBits;
  }

  //Este metodo se utiliza para obtener la frecuencia de un caracter específico
  private int getFrequency(char c) {
    int frequency = tree.getRoot().getFrequency(); //Nodo raiz del arbol de huffman
    HuffmanNode current = tree.getRoot();
    while (!current.isLeaf()) {
      if (current.getLeft() != null && current.getLeft().getCharacter() == c) {
        frequency = current.getLeft().getFrequency();
        current = current.getLeft();
      } else if (current.getRight() != null && current.getRight().getCharacter() == c) {
        frequency = current.getRight().getFrequency();
        current = current.getRight();
      } else {
        break;
      }
    }
    return frequency;
  }
}
