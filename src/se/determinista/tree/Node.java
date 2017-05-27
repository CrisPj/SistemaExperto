package se.determinista.tree;

/**
 * Created by Andrés on 01/09/2016.
 */
public class Node {

    protected Node left;
    protected Node right;
    private byte id;
    private long memoryAddress;

    /**
     * Constructs a new Node with the specified id and memory address for a new inserted Regla
     *
     * @param _id
     * @param _memoryAddress
     */
    public Node(byte _id, long _memoryAddress) {
        id = _id;
        memoryAddress = _memoryAddress;
    }

    /**
     * This method inserts a new node in the tree if the value of the entering node is minor than the
     * value of its root, goes to left, if not, goes right.
     * @param _node
     */
    public void insertNode(Node _node) {
        if (id > _node.getId()) {
            if (left == null) {
                left = _node;
            } else {
                left.insertNode(_node);
            }
        } else {
            if (right == null) {
                right = _node;
            } else {
                right.insertNode(_node);
            }
        }
    }

    /**
     * Returns the id of the rule stored in this Node
     * @return Regla id stored in file
     */
    public byte getId() {
        return id;
    }

    /**
     * Returns the memory address of the rule with the specified ID
     * @return Memory Address of the rule
     */
    public long getMemoryAddress() {
        return memoryAddress;
    }

}
