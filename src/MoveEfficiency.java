public class MoveEfficiency implements Comparable<MoveEfficiency> {
    private int numberOfEmptyTiles;
    private int score;
    private Move move;

    public MoveEfficiency(int numberOfEmptyTiles, int score, Move move) {
        this.numberOfEmptyTiles = numberOfEmptyTiles;
        this.score = score;
        this.move = move;
    }

    public Move getMove() {
        return move;
    }

    @Override
    public int compareTo(MoveEfficiency moveEff) {
        if (Integer.compare(this.numberOfEmptyTiles, moveEff.numberOfEmptyTiles) == 0) {
            return Integer.compare(this.score, moveEff.score);
        } else if (Integer.compare(this.numberOfEmptyTiles, moveEff.numberOfEmptyTiles) < 0) {
            return -1;
        }

        return 1;
    }
}
