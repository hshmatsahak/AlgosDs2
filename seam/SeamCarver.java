import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    private Picture pic;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture){
        if (picture == null)
            throw new IllegalArgumentException();
        pic = new Picture(picture);
    }

    // current picture
    public Picture picture(){
        return new Picture(pic);
    }

    // width of current picture
    public int width(){
        return pic.width();
    }

    // height of current picture
    public int height(){
        return pic.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y){
        if (x<0 || x>=pic.width() || y<0  || y>=pic.height())
            throw new IllegalArgumentException();
        if (x==0 || x==pic.width()-1 || y==0 || y==pic.height()-1)
            return 1000;
        // I should've put this in an array...damn
        int rgbleft = pic.getRGB(x-1, y);
        int rleft = (rgbleft >> 16) & 0xFF;
        int gleft = (rgbleft >>  8) & 0xFF;
        int bleft = (rgbleft >>  0) & 0xFF;
        int rgbright = pic.getRGB(x+1, y);
        int rright = (rgbright >> 16) & 0xFF;
        int gright = (rgbright >>  8) & 0xFF;
        int bright = (rgbright >>  0) & 0xFF;
        int rgbup = pic.getRGB(x, y-1);
        int rup = (rgbup >> 16) & 0xFF;
        int gup = (rgbup >>  8) & 0xFF;
        int bup = (rgbup >>  0) & 0xFF;
        int rgbdown = pic.getRGB(x, y+1);
        int rdown = (rgbdown >> 16) & 0xFF;
        int gdown = (rgbdown >>  8) & 0xFF;
        int bdown = (rgbdown >>  0) & 0xFF;
        int xrdiff = rleft - rright;
        int xgdiff = gleft - gright;
        int xbdiff = bleft - bright;
        int yrdiff = rup - rdown;
        int ygdiff = gup - gdown;
        int ybdiff = bup - bdown;
        return Math.sqrt((xrdiff*xrdiff)+(xgdiff*xgdiff)+(xbdiff*xbdiff)+(yrdiff*yrdiff)+(ygdiff*ygdiff)+(ybdiff*ybdiff));
    }

    private int[] horneighbours(int i){
        if (pic.height() == 1) return new int[] { i + 1 };
        int col = i/pic.height();
        int row = i - pic.height()*col;
        if (row==0){
            int[] neighs = new int[2];
            neighs[0] = i+pic.height();
            neighs[1] = i+pic.height()+1;
            return neighs;
        }
        if (row==pic.height()-1){
            int[] neighs = new int[2];
            neighs[0] = i+pic.height();
            neighs[1] = i+pic.height()-1;
            return neighs;
        }
        int[] neighs = new int [3];
        neighs[0] = i+pic.height()-1;
        neighs[1] = i+pic.height();
        neighs[2] = i+pic.height()+1;
        return neighs;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam(){
        if (pic.width()==1 && pic.height()==1)
            return new int[] {0};
        double[] distto = new double[pic.width()*pic.height()];
        int[] edgeto = new int[distto.length];
        for (int i = 0; i < distto.length; i++) {
            if (i < pic.height()) {
                distto[i] = 0;
                edgeto[i] = -1;
            }
            else
                distto[i] = Integer.MAX_VALUE;
        }
        for (int i=0; i<distto.length-pic.height(); i++){
            int col = i/pic.height();
            int row = i - pic.height()*col;
            int[] neighbours = horneighbours(i);
            for (int j =0; j < neighbours.length; j++){
                if (distto[i]+energy(col, row) < distto[neighbours[j]]) {
                    distto[neighbours[j]] = distto[i] + energy(col, row);
                    edgeto[neighbours[j]] = i;
                }
            }
        }
        int minedge = distto.length-pic.height();
        for (int i=distto.length-pic.height(); i<distto.length; i++){
            if (distto[i] < distto[minedge]){
                minedge = i;
            }
        }
        int[] seam = new int[pic.width()];
        int count = seam.length-1;
        for (int i = minedge; i!=-1; i = edgeto[i]){
            seam[count] = i-(i/pic.height())*pic.height();
            count--;
        }
        return seam;
    }

    private int[] verneighbours(int i){
        if (pic.width() == 1) return new int[] { i + 1 };
        int row = i/pic.width();
        int col = i - pic.width()*row;
        if (col==0){
            int[] neighs = new int[2];
            neighs[0] = i+pic.width();
            neighs[1] = i+pic.width()+1;
            return neighs;
        }
        if (col==pic.width()-1){
            int[] neighs = new int[2];
            neighs[0] = i+pic.width();
            neighs[1] = i+pic.width()-1;
            return neighs;
        }
        int[] neighs = new int [3];
        neighs[0] = i+pic.width()-1;
        neighs[1] = i+pic.width();
        neighs[2] = i+pic.width()+1;
        return neighs;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam(){
        if (pic.width()==1 && pic.height()==1)
            return new int[] {0};
        double[] distto = new double[pic.width()*pic.height()];
        int[] edgeto = new int[distto.length];
        for (int i = 0; i < distto.length; i++) {
            if (i < pic.width()) {
                distto[i] = 0;
                edgeto[i] = -1;
            }
            else
                distto[i] = Integer.MAX_VALUE;
        }
        for (int i=0; i<distto.length-pic.width(); i++){
            int row = i/pic.width();
            int col = i - pic.width()*row;
            int[] neighbours = verneighbours(i);
            for (int j =0; j < neighbours.length; j++){
                if (distto[i]+energy(col, row) < distto[neighbours[j]]) {
                    distto[neighbours[j]] = distto[i] + energy(col, row);
                    edgeto[neighbours[j]] = i;
                }
            }
        }
        int minedge = distto.length-pic.width();
        for (int i=distto.length-pic.width(); i<distto.length; i++){
            if (distto[i] < distto[minedge]){
                minedge = i;
            }
        }
        int[] seam = new int[pic.height()];
        int count = seam.length-1;
        for (int i = minedge; i!=-1; i = edgeto[i]){
            seam[count] = i-(i/pic.width())*pic.width();
            count--;
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam){
        if (pic.height() <= 1)
            throw new IllegalArgumentException();
        if (seam == null)
            throw new IllegalArgumentException();
        if (seam.length != pic.width())
            throw new IllegalArgumentException();
        for (int i=0; i<seam.length; i++){
            if (seam[i]<0 || seam[i]>=pic.height())
                throw new IllegalArgumentException();
            if (i!=0 && (seam[i]-seam[i-1] < -1 || seam[i]-seam[i-1] > 1))
                throw new IllegalArgumentException();
        }
        Picture newpic = new Picture(pic.width(), pic.height()-1);
        for (int wid = 0; wid < pic.width(); wid++){
            for (int i = 0; i < pic.height()-1; i++){
                if (i < seam[wid])
                    newpic.setRGB(wid, i, pic.getRGB(wid,i));
                else
                    newpic.setRGB(wid, i, pic.getRGB(wid, i+1));
            }
        }
        pic = newpic;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam){
        if (pic.width() <= 1)
            throw new IllegalArgumentException();
        if (seam == null)
            throw new IllegalArgumentException();
        if (seam.length != pic.height())
            throw new IllegalArgumentException();
        for (int i=0; i<seam.length; i++){
            if (seam[i]<0 || seam[i]>=pic.width())
                throw new IllegalArgumentException();
            if (i!=0 && (seam[i]-seam[i-1] < -1 || seam[i]-seam[i-1] > 1))
                throw new IllegalArgumentException();
        }
        Picture newpic = new Picture(pic.width()-1, pic.height());
        for (int hei = 0; hei < pic.height(); hei++){
            for (int i = 0; i < pic.width()-1; i++){
                if (i < seam[hei])
                    newpic.setRGB(i, hei, pic.getRGB(i,hei));
                else
                    newpic.setRGB(i, hei, pic.getRGB(i+1, hei));
            }
        }
        pic = newpic;
    }

    //  unit testing (optional)
    public static void main(String[] args){

    }

}