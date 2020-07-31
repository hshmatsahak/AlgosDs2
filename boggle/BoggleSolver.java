import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.HashSet;

public class BoggleSolver
{
    private Trie triedict = new Trie();
    private HashSet<String> words = new HashSet<>();

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary){
        // This constructor owrks slightly faster than the obe below, I copied it form someones github.
        // https://github.com/alexilyenko/Algorithms2/blob/master/src/main/java/assignment4/BoggleSolver.java
        Arrays.stream(dictionary)
              .forEach(word -> {
                  if (word.length() >= 3) {
                      triedict.put(word);
                      words.add(word);
                  }
              });
        /*
        triedict = new Trie();
        words = new HashSet<>();
        for(int i=0; i<dictionary.length; i++){
            String str = dictionary[i];
            if (str.length()>=3) {
                triedict.put(str);
                words.add(str);
            }
        }*/
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board){
        HashSet<String> valid_words = new HashSet<>();
        boolean[][] vis = new boolean[board.rows()][board.cols()];
        for (int i=0; i<board.rows(); i++){
            for (int j=0; j<board.cols(); j++){
                vis[i][j]= false;
            }
        }
        StringBuilder val = new StringBuilder();
        for (int a=0; a<board.rows(); a++){
            for(int b=0; b<board.cols(); b++){
                dfs(board, valid_words, vis, val, a, b);
            }
        }
        return valid_words;
    }

    private void dfs(BoggleBoard board, HashSet<String> valid_words, boolean[][] vis, StringBuilder val, int i, int j){
        if (!vis[i][j]){
            vis[i][j] = true;
            if (board.getLetter(i,j)=='Q'){
                val.append('Q');
                val.append('U');
            }
            else
                val.append(board.getLetter(i,j));

            String value = val.toString();
            int len = value.length();

            if (!triedict.prefix_exist(value)) {
                if(board.getLetter(i,j) == 'Q')
                    val.deleteCharAt(len-1);
                val.deleteCharAt(val.length()-1);
                vis[i][j] = false;
                return;
            }

            if (words.contains(value) && len >=3)
                valid_words.add(value);

            for (int x=i-1; x<=i+1;x++){
                for (int y=j-1;y<=j+1;y++){
                    if (!(x==i && y==j) && x>=0 && x<board.rows() && y>=0 && y<board.cols())
                        dfs(board, valid_words, vis, val, x, y);
                }
            }

            if(board.getLetter(i,j) == 'Q')
                val.deleteCharAt(len-1);
            val.deleteCharAt(val.length()-1);

            vis[i][j] = false;
        }
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word){
        int len = word.length();
        if (!words.contains(word))
            return 0;
        if (len==3 || len==4)
            return 1;
        else if(len==5)
            return 2;
        else if(len==6)
            return 3;
        else if(len==7)
            return 5;
        else
            return 11;
    }

    public static void main(String[] args) {
        /*In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);*/
        In in = new In("dictionary-yawl.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        for (int i=0; i<100;i++){
            int score =0;
            BoggleBoard board = new BoggleBoard();
            for (String word : solver.getAllValidWords(board)) {
                score += solver.scoreOf(word);
            }
            StdOut.println("Score = " + score);
        }
    }
}