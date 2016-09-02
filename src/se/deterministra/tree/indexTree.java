package se.deterministra.tree;

import se.determinista.files.Index_File;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Andrés on 01/09/2016.
 */
public class indexTree {

    private Node tree;


    public void insertNodeToTree(Node _node)
    {
        if(tree!=null)
            tree.insertNode(_node);
        else
            tree = _node;
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

    public void generateIndexTree()
    {
        try
        {
            Index_File index = new Index_File("E:\\knowledgebase"+Index_File.FILE_EXTENSION,"rw");
            ArrayList<String> rulesAddresses = index.getRulesAddresses();
            byte arrayIndex=0;
            do
            {
                String address = rulesAddresses.get(arrayIndex);
                if(address!=null)
                {
                    Node node = new Node(Byte.parseByte(address.split("-")[0]),Long.parseLong(address.split("-")[1]));
                    insertNodeToTree(node);
                    System.out.println(""+node.getId()+"-"+node.getMemoryAddress());
                }
                arrayIndex++;
            }while(arrayIndex<rulesAddresses.size());
        }
        catch(Exception ex)
        {

        }
    }

}
