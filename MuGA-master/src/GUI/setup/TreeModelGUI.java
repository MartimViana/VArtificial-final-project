//::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::: 
//::     Antonio Manso                        Luis Correia                   ::
//::     manso@ipt.pt                   Luis.Correia@ciencias.ulisboa.pt     ::
//::                                                                         ::
//::     Biosystems & Integrative Sciences Institute                         ::
//::     Faculty of Sciences University of Lisboa                            ::
//::                                                                         ::
//::     Instituto Politécnico de Tomar                                      ::
//::     Escola Superior de Tecnologia de Tomar                              ::
//::                                                                         ::
//::     This software was build with the purpose of investigate and         ::
//::     learning.                                                           ::
//::                                                             (c) 2019    ::
//:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
//////////////////////////////////////////////////////////////////////////////

package GUI.setup;

import GUI.utils.MuGAObject;
import GUI.utils.MuGASystem;
import java.util.ArrayList;
import java.util.Enumeration;
import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

/**
 *
 * @author zulu
 */
public class TreeModelGUI {

    public static DefaultTreeModel getModel(String rootName) {
        ArrayList<MuGAObject> array = MuGASystem.getGenetic(rootName);
        //get last name
        String name = rootName;
        if (rootName.indexOf(".") >= 0) {
            name = rootName.substring(rootName.lastIndexOf(".") + 1);
        }
        // rootName = rootName.replaceAll("/", ".");
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(name);
        //for all classes in array
        for (MuGAObject obj : array) {
            String className = obj.getClassName();
            if (!className.startsWith(rootName + ".")) {
                continue;
            }
            //remove rootName
            className = className.substring(rootName.length() + 1);//1 = '.'
            //split path of class
            String[] str = className.split("\\.");
            //create path
            DefaultMutableTreeNode rootTmp = root;
            for (int i = 0; i < str.length - 1; i++) {
                DefaultMutableTreeNode node = find(str[i], rootTmp);
                if (node == null) {
                    node = new DefaultMutableTreeNode(str[i]);
                    rootTmp.add(node);
                }
                rootTmp = node;
            }
            rootTmp.add(new DefaultMutableTreeNode(obj));
        }
        return new DefaultTreeModel(root);
    }

    private static DefaultMutableTreeNode find(String str, DefaultMutableTreeNode root) {
        Enumeration children = root.children();
        if (children != null) {
            Enumeration<TreeNode> e = root.depthFirstEnumeration();
            while (e.hasMoreElements()) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
                if (node.toString().equals(str)) {
                    return node;
                }
            }
        }
        return null;
    }

    public static void select(JTree tree, String selectedNode) {
        tree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode node = null;
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        Enumeration enumeration = root.breadthFirstEnumeration();
        while (enumeration.hasMoreElements()) {
            node = (DefaultMutableTreeNode) enumeration.nextElement();
            Object nodeClasse = node.getUserObject();

            if (nodeClasse instanceof MuGAObject // leaf
                    && ((MuGAObject) nodeClasse).getName().equalsIgnoreCase(selectedNode)) {
                TreePath path = new TreePath(model.getPathToRoot(node));
                tree.getSelectionModel().setSelectionPath(path);
                tree.scrollPathToVisible(path);
                return;
            }
        }
    }

    public static void main(String[] args) {

        System.out.println("DEMO:");
        JFrame frame = new JFrame("MuGA Demo");
        DefaultTreeModel model = getModel(MuGASystem.INDIVIDUALS);
        JTree tree = new JTree();
        tree.setModel(model);
        select(tree, "Default");
        frame.add(tree);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setUndecorated(true);
        frame.getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
        frame.setSize(200, 200);
        frame.setVisible(true);
    }
}
