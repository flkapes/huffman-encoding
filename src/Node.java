import org.jetbrains.annotations.NotNull;

public class Node implements Comparable<Node> {

  String character;
  int frequency;
  Node left;
  Node right;

  public Node(String character, int frequency) {
    this.character = character;
    this.frequency = frequency;
    this.left = null;
    this.right = null;
  }

  public int getFrequency() {
    return frequency;
  }

  @Override
  public int compareTo(@NotNull Node o) {
    return Integer.compare(o.frequency, frequency);
  }
}
