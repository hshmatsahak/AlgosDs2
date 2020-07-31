import edu.princeton.cs.algs4.Merge;

public class CircularSuffixArray {
    private int len;
    private CircularSuffix[] suffices;

    // circular suffix array of s
    public CircularSuffixArray(String s){
        if (s == null)
            throw new IllegalArgumentException();
        len = s.length();
        suffices = new CircularSuffix[len];
        for(int i=0; i<len; i++){
            suffices[i] = new CircularSuffix(s, i);
        }
        Merge.sort(suffices);
    }

    // length of s
    public int length(){
        return len;
    }

    // returns index of ith sorted suffix
    public int index(int i){
        if (i<0 || i>len-1) throw new IllegalArgumentException();
        return suffices[i].index;
    }

    private class CircularSuffix implements Comparable<CircularSuffix>{
        private int index;
        private String s;

        private CircularSuffix(String s, int index){
            if (s == null) {throw new IllegalArgumentException();}
            if (index < 0) {throw new IllegalArgumentException();}
            if (index > len) {throw new IllegalArgumentException();}
            this.s = s;
            this.index = index;
        }

        public int getIndex(){
            return index;
        }

        public int compareTo(CircularSuffix that){
            if (that == null) {throw new IllegalArgumentException();}
            if (this == that) return 0;
            for (int i=0; i<len; i++){
                char c1 = s.charAt((this.index+i)%len);
                char c2 = s.charAt((that.index+i)%len);
                if (c1 < c2) return -1;
                if (c1 > c2) return 1;
            }
            return 0;
        }
    }
    // unit testing (required)
    public static void main(String[] args){
        String test=args[0];
        CircularSuffixArray circSA=new CircularSuffixArray(test);
        int n=test.length();
        for (int i=0; i<test.length(); i++) {
            System.out.println(circSA.index(i));}
    }

}