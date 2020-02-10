package datastruct;

public class BinTree {

    private int data;
    private BinTree left;
    private BinTree right;

    public BinTree() {
    }

    public BinTree(int data) {
        this.data = data;
    }

    public BinTree(int data, BinTree left, BinTree right) {
        this.data = data;
        this.left = left;
        this.right = right;
    }

    // 前序遍历
    public void pre(BinTree root) {

        //遍历之打印
        if (null != root) {
            System.out.println(root.data);
            pre(root.left);
            pre(root.right);
        }
    }

    // 转置
    public BinTree reserve(BinTree root) {

        if (null != root) {

            BinTree temp = root.left;
            root.left = reserve(root.right);
            root.right = reserve(temp);

            return root;
        }

        return null;
    }


    public static void main(String[] args) {

        BinTree left = new BinTree(2);
        BinTree right = new BinTree(3);
        BinTree root = new BinTree(1, left, right);

        // 前序遍历
        //root.pre(root);

        //转置
        root.reserve(root);
        root.pre(root);
    }
}
