package project;
/**

 Clase de decodificacion de Huffman
 Esta clase se encarga de decodificar un texto en base a un arbol de huffman
 @Author: <Kahyberth Steven Gonzales, Carlos Eduardo Guerrero, Juan Camilo Varela Ocoro, Yuliana Serna Sanchez>
 @Version: <1>
 */
class HuffmanDecoding {
  public static String decode(String encodedString, HuffmanBinaryTree tree) {
    StringBuilder decodedString = new StringBuilder();
    HuffmanNode current = tree.getRoot();
    for (char bit : encodedString.toCharArray()) {
      if (bit == '0' && current.getLeft() != null) {
        current = current.getLeft();
      } else if (bit == '1' && current.getRight() != null) {
        current = current.getRight();
      }

      if (current.isLeaf()) {
        decodedString.append(current.getCharacter());
        current = tree.getRoot();
      }
    }
    return decodedString.toString();
  }
}