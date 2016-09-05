package se.determinista.tree;

/**
 * Created by Andrés on 01/09/2016.
 */
public class Node {

    protected Node left;
    protected Node right;
    private byte id;
    private long memoryAddress;

    public Node(byte _id, long _memoryAddress) {
        id = _id;
        memoryAddress = _memoryAddress;
    }

    /**
     * This method inserts a new node in the tree if the value of the entering node is minor than the
     * value of its root, goes to left, if not, goes right.
     *
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

    /*public void setId(byte _id)
    {
        id = _id;
    }

    public void setMemoryAddress(long _memoryAddress)
    {
        memoryAddress = _memoryAddress;
    }*/

    public byte getId() {
        return id;
    }

    public long getMemoryAddress() {
        return memoryAddress;
    }
}
