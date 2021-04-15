import java.util.*;
import java.io.*;

public class CKY {

    // Bin tree Node
    public static class Node {
        String term;
        Node left;
        Node right;
        
        public Node(String term) {
            this.term = term;
        }
        public Node(String term, Node left) {
            this.term = term;
            this.left = left;
        }
        public Node(String term, Node left, Node right) { 
            this.term = term; 
            this.left = left;
            this.right = right;
        }
    }

    // part a, the cky algorithm
    public static List<Node> parse(HashMap<String[], String> grammar, String sentence) {

        List<Node> trees = new ArrayList<>();

        String[] parts = sentence.split(" ");
        int n  = parts.length;
        List<Node>[][] dptime = new ArrayList[n + 1][n];

        // initialize lists??
        for (int i = 0; i < n + 1; i++) {
            for (int j = 0; j < n; j++) {
                dptime[i][j] = new ArrayList<Node>();
            }
        }

        // initialize words in the diagonal
        for (int i = 0; i < n; i++) {
            dptime[i][i].add(new Node(parts[i]));
        }
        
        // initialize non term - term
        for (int i = 0; i < n; i++) {
            for (String[] key : grammar.keySet()) {
                if (key[0].equals(dptime[i][i].get(0).term)) {
                    dptime[i + 1][i].add(new Node(grammar.get(key), dptime[i][i].get(0)));
                    
                }
            }
        }

        // initialize the rest
        for (int i = 2; i < n + 1; i++) {
            for (int j = 0; j + i < n + 1; j++) {


                // check if there exists a pair of non terms that will make up this term right here
                for (String[] key : grammar.keySet()) { 

                    // corner
                    if (key.length < 2) continue; 

                    // more loops to go as up or as right as you need
                    for (int kk = 1; kk < i; kk++) {
                        for (int ll = 1; ll < i; ll++) {

                            List<Node> left = dptime[j + i - kk][j];
                            List<Node> right = dptime[j + i][j + ll];

                            int leftsize = left.size();
                            int rightsize = right.size();

                            // more loops to check each term in the list at this index
                            for (int iii = 0; iii < leftsize; iii++) {
                                for (int jjj = 0; jjj < rightsize; jjj++) {


                                    // same as the key pair? great! then add the thing that the pair constructs
                                    if (left.get(iii).term.equals(key[0]) && 
                                        right.get(jjj).term.equals(key[1])) {

                                        dptime[j + i][j].add(new Node(grammar.get(key), left.get(iii), right.get(jjj)));
                                    }
                                }
                            }
                            
                        }
                    }
                }

            }
        }

        // add the final parses 
        int finalsize = dptime[n][0].size();
        for (int i = 0; i < finalsize; i++) {
            
            if (!dptime[n][0].get(i).term.equals("S")) continue;

            StringBuilder sb = new StringBuilder();
            myTraversal(dptime[n][0].get(i), sb);
            
            // check that the sentence order is maintained
            if (sb.toString().strip().equals(sentence.strip())) {

                // check no duplicate trees
                boolean good = true;
                for (int j = 0; j < trees.size(); j++) {
                    if (identicalTrees(dptime[n][0].get(i), trees.get(j))) {
                        good = false;
                        break;
                    }
                }

                if (good) trees.add(dptime[n][0].get(i));
                
            }
        }

        return trees;
    }

    // part d, the extended cky algorithm for partial parses (basically the same as a but with an added call to a helper)
    public static List<Node> extendedParse(HashMap<String[], String> grammar, String sentence) {

        List<Node> trees = new ArrayList<>();

        String[] parts = sentence.split(" ");
        int n  = parts.length;
        List<Node>[][] dptime = new ArrayList[n + 1][n];

        // initialize lists??
        for (int i = 0; i < n + 1; i++) {
            for (int j = 0; j < n; j++) {
                dptime[i][j] = new ArrayList<Node>();
            }
        }

        // initialize words in the diagonal
        for (int i = 0; i < n; i++) {
            dptime[i][i].add(new Node(parts[i]));
        }
        
        // initialize non term - term
        for (int i = 0; i < n; i++) {
            for (String[] key : grammar.keySet()) {
                if (key[0].equals(dptime[i][i].get(0).term)) {
                    dptime[i + 1][i].add(new Node(grammar.get(key), dptime[i][i].get(0)));
                    
                }
            }
        }

        // initialize the rest
        for (int i = 2; i < n + 1; i++) {
            for (int j = 0; j + i < n + 1; j++) {


                // check if there exists a pair of non terms that will make up this term right here
                for (String[] key : grammar.keySet()) { 

                    // corner
                    if (key.length < 2) continue; 

                    // more loops to go as up or as right as you need
                    for (int kk = 1; kk < i; kk++) {
                        for (int ll = 1; ll < i; ll++) {

                            List<Node> left = dptime[j + i - kk][j];
                            List<Node> right = dptime[j + i][j + ll];

                            int leftsize = left.size();
                            int rightsize = right.size();

                            // more loops to check each term in the list at this index
                            for (int iii = 0; iii < leftsize; iii++) {
                                for (int jjj = 0; jjj < rightsize; jjj++) {


                                    // same as the key pair? great! then add the thing that the pair constructs
                                    if (left.get(iii).term.equals(key[0]) && 
                                        right.get(jjj).term.equals(key[1])) {

                                        dptime[j + i][j].add(new Node(grammar.get(key), left.get(iii), right.get(jjj)));
                                    }
                                }
                            }
                            
                        }
                    }
                }

            }
        }

        // add the final parses 
        int finalsize = dptime[n][0].size();
        for (int i = 0; i < finalsize; i++) {
            
            if (!dptime[n][0].get(i).term.equals("S")) continue;

            StringBuilder sb = new StringBuilder();
            myTraversal(dptime[n][0].get(i), sb);
            
            // check that the sentence order is maintained
            if (sb.toString().strip().equals(sentence.strip())) {

                // check no duplicate trees
                boolean good = true;
                for (int j = 0; j < trees.size(); j++) {
                    if (identicalTrees(dptime[n][0].get(i), trees.get(j))) {
                        good = false;
                        break;
                    }
                }

                if (good) trees.add(dptime[n][0].get(i));
                
            }
        }

        if (trees.size() == 0) extendedParse(sentence, dptime, trees);

        return trees;
    }

    // construct the sentence left to right from tree traversal
    private static void myTraversal(Node root, StringBuilder sb) {
        if (root == null) return;
        // if node is leaf node  
        if (root.left == null && root.right == null) {
            sb.append(root.term + " ");
            return;
        }
        if (root.left != null) myTraversal(root.left, sb);
        if (root.right != null) myTraversal(root.right, sb);
    }

    // check if two trees are identical
    private static boolean identicalTrees(Node a, Node b) {
        // both empty
        if (a == null && b == null) return true;
              
        // both not empty
        if (a != null && b != null) return (a.term.equals(b.term) && identicalTrees(a.left, b.left) && identicalTrees(a.right, b.right));
        
        // one empty
        return false;
    }

    // draw trees given a list of roots
    private static void printTrees(List<Node> trees) {
        System.out.println(trees.size() + " parse(s):\n");
        for (Node tree : trees) System.out.println(preOrder(tree) + '\n');
    }

    // draw tree help + preorder traversal
    private static String preOrder(Node node) {
        if (node == null) return "";
    
        StringBuilder sb = new StringBuilder();
        sb.append(node.term);
    
        String pointerRight = "|__";
        String pointerLeft = (node.right != null) ? "|--" : "|__";
    
        traverse(sb, "", pointerLeft, node.left, node.right != null);
        traverse(sb, "", pointerRight, node.right, false);
    
        return sb.toString();
    }

    // draw tree help help
    private static void traverse(StringBuilder sb, String padding, String pointer, Node node, boolean hasRightSibling) {
        if (node != null) {
            sb.append("\n");
            sb.append(padding);
            sb.append(pointer);
            sb.append(node.term);

            StringBuilder paddingBuilder = new StringBuilder(padding);
            if (hasRightSibling) paddingBuilder.append("|  ");
            else paddingBuilder.append("   ");

            String paddingForBoth = paddingBuilder.toString();
            String pointerRight = "|__";
            String pointerLeft = (node.right != null) ? "|--" : "|__";

            traverse(sb, paddingForBoth, pointerLeft, node.left, node.right != null);
            traverse(sb, paddingForBoth, pointerRight, node.right, false);
        }
    }

    // help for part d
    private static List<Node> extendedParse(String sentence, List<Node>[][] dptime, List<Node> trees) {

        int max = 0;
        Node greedilychosen = null;

        for (int i = 0; i < dptime.length; i++) {
            for (int j = 0; j < dptime[0].length; j++) {
                int size = dptime[i][j].size();
                for (int k = 0; k < size; k++) {
                    StringBuilder partialsentence = new StringBuilder();
                    myTraversal(dptime[i][j].get(k), partialsentence);
                    String truepartialsentence = partialsentence.toString();
                    if (truepartialsentence.strip().split(" ").length >= max && sentence.contains(truepartialsentence.strip())) {
                        max = truepartialsentence.strip().split(" ").length;
                        greedilychosen = dptime[i][j].get(k);                        
                    }
                }
            }
        }
        trees.add(greedilychosen);
        return trees;
    }

    public static void main(String[] args) throws FileNotFoundException {
        Scanner scanner = new Scanner(new File("grammar.txt"));

        // reverse grammar map
        HashMap<String[], String> cfg = new HashMap<>();

        // construct the reverse grammar map
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split(" ");
            String[] key = new String[parts.length - 1];
            for (int i = 1; i < parts.length; i++) key[i - 1] = parts[i];
            cfg.put(key, parts[0]);
        }


        // debug
        /*String sentence = "light green cars fly";
        List<Node> trees = extendedParse(cfg, sentence);
        printTrees(trees);*/
        
        // part c
        scanner = new Scanner(new File("3-1c.txt"));
        while (scanner.hasNextLine()) {
            String sentence = scanner.nextLine();
            List<Node> trees = parse(cfg, sentence);
            System.out.println(sentence + '\n');
            printTrees(trees);
            System.out.println();
        }

        // part d
        List<Node> dtrees = extendedParse(cfg, "light green cars fly");
        System.out.println("light green cars fly" + '\n');
        printTrees(dtrees);
        System.out.println();
        dtrees = extendedParse(cfg, "the man at the light green light put the baby in a car");
        System.out.println("the man at the light green light put the baby in a car" + '\n');
        printTrees(dtrees);
        System.out.println();
        dtrees = extendedParse(cfg, "a light green car that hit the truck with cars and a baby");
        System.out.println("a light green car that hit the truck with cars and a baby" + '\n');
        printTrees(dtrees);
        System.out.println();

        // part e
        scanner = new Scanner(new File("fixedgrammar.txt"));

        // reverse grammar map
        cfg = new HashMap<>();

        // construct the reverse grammar map
        while (scanner.hasNextLine()) {
            String[] parts = scanner.nextLine().split(" ");
            String[] key = new String[parts.length - 1];
            for (int i = 1; i < parts.length; i++) key[i - 1] = parts[i];
            cfg.put(key, parts[0]);
        }

        scanner = new Scanner(new File("3-1e.txt"));
        while (scanner.hasNextLine()) {
            String sentence = scanner.nextLine();
            List<Node> trees = extendedParse(cfg, sentence);
            System.out.println(sentence + '\n');
            printTrees(trees);
            System.out.println();
        }
    }
}