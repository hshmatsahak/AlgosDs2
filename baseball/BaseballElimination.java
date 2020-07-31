import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;

public class BaseballElimination {
    private int numteams;
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private HashMap<String, Integer> wordtovertex;
    private HashMap<Integer, String> vertextoword;
    private int[][] gamesleft;
    private FordFulkerson[] cache;
    private int V;

    public BaseballElimination(String filename) {
        In in = new In(filename);
        numteams = Integer.parseInt(in.readLine());
        wins = new int[numteams];
        losses = new int[numteams];
        remaining = new int[numteams];
        gamesleft = new int[numteams][numteams];
        V = (numteams-2)*(numteams-1)/2+numteams+1;
        wordtovertex = new HashMap<>();
        vertextoword = new HashMap<>();
        int idx = 0;
        while (in.hasNextLine()){
            String line = in.readLine();
            line = line.trim();
            String[] stats = line.split("\\s+");
            wordtovertex.put(stats[0], idx);
            vertextoword.put(idx, stats[0]);
            wins[idx] = Integer.parseInt(stats[1]);
            losses[idx] = Integer.parseInt(stats[2]);
            remaining[idx] = Integer.parseInt(stats[3]);
            for (int i = 4; i<stats.length; i++){
                gamesleft[idx][i-4] = Integer.parseInt(stats[i]);
            }
            idx++;
        }
        cache = new FordFulkerson[numteams];
        for (int i=0; i<numteams; i++){
            cache[i] = null;
        }
    }

    // number of teams
    public int numberOfTeams() {
        return numteams;
    }

    // all teams
    public Iterable<String> teams() {
        return wordtovertex.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        return wins[wordtovertex.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        return losses[wordtovertex.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        return remaining[wordtovertex.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        return gamesleft[wordtovertex.get(team1)][wordtovertex.get(team2)];
    }

    private boolean trivEliminated(String team){
        int teamval = wordtovertex.get(team);
        //StdOut.println("Score: " + (wins[teamval]+remaining[teamval]));
        for (int i=0; i<numteams; i++){
            if (wins[teamval]+remaining[teamval] < wins[i] && i!=teamval) {
                //StdOut.println(wins[i]);
                return true;
            }
        }
        return false;
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        if (trivEliminated(team))
           return true;
        FordFulkerson ff = ff(team);
        //StdOut.println(ff.value());
        for (int i = 1; i <= (numteams-2)*(numteams-1)/2; i++){
            if (ff.inCut(i)) {
                //StdOut.println(i);
                return true;
            }
        }
        return false;
    }

    public Iterable<String> certificateOfElimination(String team) {
        if (!isEliminated(team))
            return null;
        if (trivEliminated(team)){
            Bag<String> tmp = new Bag<>();
            int teamval = wordtovertex.get(team);
            for (int i=0; i<numteams; i++){
                if (wins[teamval]+remaining[teamval] < wins[i] && i!=teamval) {
                    tmp.add(vertextoword.get(i));
                }
            }
            return tmp;
        }
        FordFulkerson ff = ff(team);
        Bag<String> bag = new Bag<>();
        for (int i=0; i<numteams; i++){
            if (ff.inCut((numteams-2)*(numteams-1)/2+1+i)) {
                if(i < wordtovertex.get(team))
                    bag.add(vertextoword.get(i));
                else
                    bag.add(vertextoword.get(i+1));
            }
        }
        return bag;
    }

    private FordFulkerson ff(String team) {
        int teamval = wordtovertex.get(team);
        if (cache[teamval] != null)
            return cache[teamval];
        int numv = V;
        FlowNetwork network = new FlowNetwork(numv);
        int count = 1;
        for (int i=0; i<numteams; i++){
            if (i != teamval){
                for (int j=i+1; j<numteams; j++){
                    if(j!=teamval) {
                        network.addEdge(new FlowEdge(0, count, gamesleft[i][j]));
                        //StdOut.println(gamesleft[i][j]);
                        //StdOut.println("i,j is "+ i + ", " + j);
                        if (i < teamval) {
                            network.addEdge(new FlowEdge(count, (numteams - 2) * (numteams - 1) / 2 + i + 1, Double.POSITIVE_INFINITY));
                            //StdOut.println("Add infinite edge from " + count + " to " + ((numteams - 2) * (numteams - 1) / 2 + i + 1));
                        }
                        else{
                            network.addEdge(new FlowEdge(count, (numteams - 2) * (numteams - 1) / 2 + i, Double.POSITIVE_INFINITY));
                            //StdOut.println("Add infinite edge from " + count + " to " + ((numteams - 2) * (numteams - 1) / 2 + i));
                        }
                        if (j < teamval) {
                            network.addEdge(new FlowEdge(count, (numteams - 2) * (numteams - 1) / 2 + j + 1, Double.POSITIVE_INFINITY));
                            //StdOut.println("Add infinite edge from " + count + " to " + ((numteams - 2) * (numteams - 1) / 2 + j + 1));
                        }
                        else {
                            //StdOut.println("wtf, j is " + j);
                            network.addEdge(new FlowEdge(count, (numteams - 2) * (numteams - 1) / 2 + j, Double.POSITIVE_INFINITY));
                            //StdOut.println("Add infinite edge from " + count + " to " + ((numteams - 2) * (numteams - 1) / 2 + j));
                        }
                        count++;
                    }
                }
            }
        }
        count = 0;
        for (int i=(numteams-2)*(numteams-1)/2+1; i<numv-1; i++){
            if (count < teamval) {
                network.addEdge(new FlowEdge(i, numv - 1, wins[teamval] + remaining[teamval] - wins[count]));
                //StdOut.println("Add edge between " + i + " and " + (numv-1) + " with capacity " + (wins[teamval] + remaining[teamval] - wins[count]));
            }
            else {
                network.addEdge(new FlowEdge(i, numv - 1,wins[teamval] + remaining[teamval] - wins[count + 1]));
                //StdOut.println("Add edge between " + i + " and " + (numv-1) + " with capacity " + (wins[teamval] + remaining[teamval] - wins[count+1]));
            }
            count++;
        }
        FordFulkerson __ff = new FordFulkerson(network, 0, numv-1);
        cache[teamval] = __ff;
        return __ff;
    }

    public static void main(String[] args){
        BaseballElimination test = new BaseballElimination("teams4.txt");
        /*StdOut.println(test.numteams);
        for (String i : test.teams()){
            StdOut.println(i);
        }*/
        /*
        StdOut.println(test.wins("Montreal"));
        StdOut.println(test.losses("Montreal"));
        StdOut.println(test.remaining("Montreal"));
        StdOut.println(test.against("Montreal", "Philadelphia"));*/
        for (String i : test.certificateOfElimination("Montreal")){
            StdOut.println(i);
        }
        StdOut.println(test.isEliminated("Montreal"));
    }
}