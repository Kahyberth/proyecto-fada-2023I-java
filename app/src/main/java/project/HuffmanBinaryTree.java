package project;

class HuffmanBinaryTree {
  private HuffmanNode root;

  public HuffmanBinaryTree(HuffmanNode root) {
    this.root = root;
  }

  public HuffmanNode getRoot() {
    return root;
  }

  public int getNumberKey() {
    if (Character.isDigit(root.getCharacter())) {
      return Character.getNumericValue(root.getCharacter());
    } else {
      return -1;
    }
  }

  public HuffmanBinaryTree getLeft() {
    if (root.getLeft() != null) {
      return new HuffmanBinaryTree(root.getLeft());
    } else {
      return null;
    }
  }

  public HuffmanBinaryTree getRight() {
    if (root.getRight() != null) {
      return new HuffmanBinaryTree(root.getRight());
    } else {
      return null;
    }
  }
}