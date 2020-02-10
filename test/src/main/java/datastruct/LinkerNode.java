package datastruct;

import java.util.HashSet;

public class LinkerNode {

    int value;
    LinkerNode next;

    public LinkerNode() {
    }

    public LinkerNode(int value) {
        this.value = value;
    }

    public LinkerNode(int value, LinkerNode next) {
        this.value = value;
        this.next = next;
    }

    public static LinkerNode reverse(LinkerNode root){

        if (root == null || root.next ==null){
            return root;
        }

        //保存指向,1->2
        LinkerNode temp = root.next;
        //递归
        LinkerNode newRoot = reverse(root.next);
        //2->1
        temp.next = root;
        //1->null
        root.next = null;
        //返回新链表
        return newRoot;
    }

    public static void main(String[] args) {

        LinkerNode linkerNode1 = new LinkerNode(1);
        LinkerNode linkerNode2 = new LinkerNode(2);
        LinkerNode linkerNode3 = new LinkerNode(3);

        linkerNode1.next=linkerNode2;
        linkerNode2.next=linkerNode3;

        LinkerNode reverseLinkerNode = reverse(linkerNode1);
        System.out.println(reverseLinkerNode.value);
        System.out.println(reverseLinkerNode.next.value);
        System.out.println(reverseLinkerNode.next.next.value);

    }
}
