import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode(){
        char[] ascii_chars = new char[R];
        int[] pos = new int[R];
        for (int i=0; i<R; i++){
            ascii_chars[i] = (char) i;
            pos[i] = i;
        }
        while(!BinaryStdIn.isEmpty()){
            char ch = BinaryStdIn.readChar();
            int idx = pos[(int)ch];
            BinaryStdOut.write((char)idx);
            for (int i=idx-1; i>=0; i--){
                pos[(int)ascii_chars[i]] +=1;
                ascii_chars[i+1] = ascii_chars[i];
            }
            pos[(int)ch] = 0;
            ascii_chars[0] = ch;
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode(){
        char[] ascii_chars = new char[R];
        int[] pos = new int[R];
        for (int i=0; i<R; i++){
            ascii_chars[i] = (char) i;
            pos[i] = i;
        }
        while(!BinaryStdIn.isEmpty()){
            int idx = (int)BinaryStdIn.readChar();
            char ch = ascii_chars[idx];
            BinaryStdOut.write(ch);
            for (int i=idx-1; i>=0; i--){
                pos[(int)ascii_chars[i]] +=1;
                ascii_chars[i+1] = ascii_chars[i];
            }
            pos[(int)ch] = 0;
            ascii_chars[0] = ch;
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args){
        String in = args[0];
        if (in.equals("-"))
            encode();
        else
            decode();
        BinaryStdOut.close();
    }

}