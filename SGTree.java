import com.sun.source.tree.Tree;

/**
 * ScapeGoat Tree class
 * <p>
 * This class contains some basic code for implementing a ScapeGoat tree. This version does not include any of the
 * functionality for choosing which node to scapegoat. It includes only code for inserting a node, and the code for
 * rebuilding a subtree.
 */

public class SGTree {

    // Designates which child in a binary tree
    enum Child {LEFT, RIGHT}

    /**
     * TreeNode class.
     * <p>
     * This class holds the data for a node in a binary tree.
     * <p>
     * Note: we have made things public here to facilitate problem set grading/testing. In general, making everything
     * public like this is a bad idea!
     */
    public static class TreeNode {
        int key;
        public TreeNode left = null;
        public TreeNode right = null;

        public int weight = 1;

        TreeNode(int k) {
            key = k;
        }
    }

    // Root of the binary tree
    public TreeNode root = null;

    /**
     * Counts the number of nodes in the specified subtree.
     *
     * @param node  the parent node, not to be counted
     * @param child the specified subtree
     * @return number of nodes
     */
    public int countNodes(TreeNode node, Child child) {
        TreeNode n = (child == child.LEFT)? node.left: node.right;
        return (n==null)?0:1 + countNodes(n, child.LEFT) + countNodes(n, child.RIGHT);
    }

    /**
     * Builds an array of nodes in the specified subtree.
     *
     * @param node  the parent node, not to be included in returned array
     * @param child the specified subtree
     * @return array of nodes
     */
    TreeNode[] enumerateNodes(TreeNode node, Child child) {
        int n = countNodes(node, child);
        TreeNode[] ra = new TreeNode[n];
        TreeNode sn = (child == child.LEFT)? node.left: node.right;
        iterateThrough(ra, sn, 0);
        return ra;
    }

    public int iterateThrough(TreeNode[] ra, TreeNode n, int i) {
        if (n.left != null) {
            i = iterateThrough(ra, n.left, i);
        }
        ra[i] = n;
        i += 1;
        if (n.right != null) {
            i = iterateThrough(ra, n.right, i);
        }
        return i;
    }

    /**
     * Builds a tree from the list of nodes Returns the node that is the new root of the subtree
     *
     * @param nodeList ordered array of nodes
     * @return the new root node
     */
    TreeNode buildTree(TreeNode[] nodeList) {
        int tail = nodeList.length;
        TreeNode returnNode = helpbuilder(nodeList, 0, tail);
        return returnNode;
    }

    public TreeNode helpbuilder(TreeNode[] nodeList, int head, int tail) {
        if (head >= tail) {
            return null;
        }
        int median = (head + tail) / 2;
        TreeNode returnNode = nodeList[median];
        returnNode.right = helpbuilder(nodeList, median + 1, tail);
        returnNode.left = helpbuilder(nodeList, head, median);
        return returnNode;
    }

    /**
     * Determines if a node is balanced. If the node is balanced, this should return true. Otherwise, it should return
     * false. A node is unbalanced if either of its children has weight greater than 2/3 of its weight.
     *
     * @param node a node to check balance on
     * @return true if the node is balanced, false otherwise
     */
    public boolean checkBalance(TreeNode node) {
        if (node == null) {
            return true;
        }
        int leftWeight = (node.left != null) ? node.left.weight : 0;
        int rightWeight =(node.right != null) ? node.right.weight : 0;
        double weight = node.weight;

        if (leftWeight > (2.0 / 3.0) * weight || rightWeight > (2.0 / 3.0) * weight) {
            return false;

        }

        return true;
    }


    /**
     * Rebuilds the specified subtree of a node.
     *
     * @param node  the part of the subtree to rebuild
     * @param child specifies which child is the root of the subtree to rebuild
     */
    public void rebuild(TreeNode node, Child child) {
        // Error checking: cannot rebuild null tree
        if (node == null) return;
        // First, retrieve a list of all the nodes of the subtree rooted at child
        TreeNode[] nodeList = enumerateNodes(node, child);
        // Then, build a new subtree from that list
        TreeNode newChild = buildTree(nodeList);
        // Finally, replace the specified child with the new subtree
        if (child == Child.LEFT) {
            node.left = newChild;
        } else if (child == Child.RIGHT) {
            node.right = newChild;
        }

        fixWeights(node, child);

    }

    public int fixWeights(TreeNode u, Child child) {
        TreeNode node = (child == child.LEFT)? u.left: u.right;
        if (node == null) {
            return 0;
        }
        int leftv = fixWeights(node, Child.LEFT);
        int rightv = fixWeights(node, Child.RIGHT);
        node.weight = leftv + rightv + 1;
        return node.weight;
    }

    /**
     * Inserts a key into the tree.
     *
     * @param key the key to insert
     */

    public void checkAll(int key) {
        TreeNode node = root;
        while (true) {
            if (!checkBalance(node.left)) {
                rebuild(node, Child.LEFT);
                break;
            } else if (!checkBalance(node.right)) {
                rebuild(node, Child.RIGHT);
                break;
            }

            if (key<=node.key) {
                if (node.left == null) break;
                node = node.left;
            } else {
                if (node.right == null) break;
                node = node.right;
            }
        }
    }
    public void insert(int key) {

        if (root == null) {
            root = new TreeNode(key);
            return;
        }

        TreeNode node = root;
        while (true) {
            node.weight += 1;
            if (key <= node.key) {
                if (node.left == null) break;
                node = node.left;

            } else {
                if (node.right == null) break;
                node = node.right;
            }
        }

        if (key <= node.key) {
            node.left = new TreeNode(key);
        } else {
            node.right = new TreeNode(key);
        }

        checkAll(key);

    }
    // Simple main function for debugging purposes
    public static void main(String[] args) {
        SGTree tree = new SGTree();
        for (int i = 0; i < 10; i++) {
            tree.insert(i);
        }
        tree.rebuild(tree.root, Child.RIGHT);
        tree.insert(40);
        tree.insert(41);
        tree.insert(42);
        tree.insert(43);
        tree.insert(44);
        tree.insert(45);
        System.out.println(tree.root.right.weight);
        System.out.println(tree.root.right.right.weight);
        System.out.println(tree.checkBalance(tree.root.right));
    }
}
