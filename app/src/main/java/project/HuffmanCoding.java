package project;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
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

  private void buildEncodingTable() {
    encodingTable = new HashMap<>();
    buildEncodingTableRecursive(tree.getRoot(), "");
  }

  private void buildEncodingTableRecursive(HuffmanNode node, String code) {
    if (node.isLeaf()) {
      encodingTable.put(node.getCharacter(), code);
    } else {
      buildEncodingTableRecursive(node.getLeft(), code + "0");
      buildEncodingTableRecursive(node.getRight(), code + "1");
    }
  }

  private int countNodes(HuffmanNode node) {
    if (node == null) {
      return 0;
    }
    return 1 + countNodes(node.getLeft()) + countNodes(node.getRight());
  }

  private int calculateDepth(HuffmanNode node) {
    if (node == null) {
      return 0;
    }
    int leftDepth = calculateDepth(node.getLeft());
    int rightDepth = calculateDepth(node.getRight());
    return 1 + Math.max(leftDepth, rightDepth);
  }

  private int calculateOriginalBits() {
    int totalBits = 0;
    for (char c : encodingTable.keySet()) {
      int frequency = getFrequency(c);
      totalBits += frequency * 16; // Assuming 16-bit ASCII characters
    }
    return totalBits;
  }

  private int calculateCompressedBits() {
    int totalBits = 0;
    for (char c : encodingTable.keySet()) {
      int frequency = getFrequency(c);
      String code = encodingTable.get(c);
      totalBits += frequency * code.length();
    }
    return totalBits;
  }

  private int getFrequency(char c) {
    int frequency = tree.getRoot().getFrequency();
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
