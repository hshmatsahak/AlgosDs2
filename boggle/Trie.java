/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

class Trie {
    private static final int R = 26;
    private Node root;

    static class Node{
        private Node[] next = new Node[R];

        Node getnext(char ch){
            return next[ch-'A'];
        }
    }

    public Node getRoot(){
        return root;
    }

    public void put(String key){
        root = put(root, key, 0);
    }

    private Node put(Node x, String key, int d){
        if (x==null) x=new Node();
        if (d == key.length())
            return x;
        char ch = key.charAt(d);
        x.next[ch-'A'] = put(x.getnext(ch), key, d+1);
        return x;
    }

    public boolean prefix_exist(String key){
        return prefix_exist(root, key, 0);
    }

    private boolean prefix_exist(Node x, String key, int d){
        if (d == key.length())
            return true;
        if (x==null)
            return false;
        return prefix_exist(x.next[key.charAt(d)-'A'], key, d+1);
    }
}
