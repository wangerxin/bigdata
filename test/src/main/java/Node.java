import java.util.ArrayList;
import java.util.List;

public class Node {

    int data;
    Node leftNode;
    Node rightNode;
    private static Node orignNode = new Node();

    //构造方法
    public Node(int data, Node leftNode, Node rightNode) {
        super();
        this.data = data;
        this.leftNode = leftNode;
        this.rightNode = rightNode;
    }

    public Node() {
        super();
    }

    public Node(int data) {
        super();
        this.data = data;
    }

    //插入数据
    public void InsertTree(Node node, Node orign) {
        if (node.data < orign.data) {
            if (orign.leftNode != null) {
                InsertTree(node, orign.leftNode);
            } else {
                orign.leftNode = new Node(node.data);
            }
        } else if (node.data > orign.data) {
            if (orign.rightNode != null) {
                InsertTree(node, orign.rightNode);
            } else {
                orign.rightNode = new Node(node.data);
            }
        } else {
            // do nothing
        }
    }

    public Node Create(List<Integer> integers) {
        for (int i = 0; i < integers.size(); i++) {
            if (i == 0) {
                orignNode.data = integers.get(i);
            } else {
                Node node = new Node(integers.get(i));
                InsertTree(node, orignNode);
            }
        }
        return orignNode;
    }

    //先序遍历
    public void preSort(Node tree) {

        List<Integer> treeList = new ArrayList<Integer>();
    }
}
