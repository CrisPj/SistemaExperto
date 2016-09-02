package se.deterministra.tree;

/**
 * Created by Andrés on 01/09/2016.
 */
public class indexTree {

    private Node tree;

    /**
     * Constructor for the tree, requires the root node.
     * @param _root
     */
    public indexTree(Node _root)
    {
        tree = _root;
    }

    public void insertNodeToTree(Node _node)
    {
        tree.insertNode(_node);
    }

    /**
     * This method returns the actual memory address offset of the rule with the specified id
     * @return
     */
    public long getRuleMemoryAddress(byte _id)
    {
        return searchRuleAddress(_id,tree);
    }

    public long searchRuleAddress(long _id,Node _root)
    {
            if(_root.getId() == _id)
                return _root.getMemoryAddress();
            else
                if(_root.getId() > _id)
                    if(_root.left != null)
                        return searchRuleAddress(_id,_root.left);
                    else
                        return -1;
                else
                    if (_root.right != null)
                        return searchRuleAddress(_id,_root.right);
                    else
                        return -1;

    }

}
