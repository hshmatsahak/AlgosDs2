import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output 
    public static void transform(){
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        int first = -1;
        for (int i=0; i<csa.length();i++){
            if (csa.index(i) == 0) {
                first = i;
                break;
            }
        }
        BinaryStdOut.write(first);
        for (int i=0; i<csa.length(); i++){
            BinaryStdOut.write(s.charAt((csa.index(i) + csa.length() - 1) % s.length()));
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform(){
        int R=256;
        int first = BinaryStdIn.readInt();
        String last_col = BinaryStdIn.readString();
        int n = last_col.length();
        int[] next = new int[n];
        int[] count = new int[R+1];
        for (int i=0; i<n; i++){
            count[last_col.charAt(i)+1]+=1;
        }
        for (int i=1; i< count.length-1; i++){
            count[i+1] += count[i];
        }
        for(int i=0; i<n; i++){
            next[count[last_col.charAt(i)]++] = i;
        }
        int tmp = first;
        for (int i=0; i<n; i++){
            BinaryStdOut.write(last_col.charAt(next[tmp]));
            tmp = next[tmp];
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args){
        if(args[0].equals("-"))
            transform();
        else if(args[0].equals("+"))
            inverseTransform();
        BinaryStdOut.close();
    }

}