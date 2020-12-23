import java.util.*;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
    private Stack<Tile[][]> previousStates = new Stack<>();
    private Stack<Integer> previousScores = new Stack<>();
    private boolean isSaveNeeded = true;
    protected int score;
    protected int maxTile;

    public Model() {
        resetGameTiles();
    }

    private void addTile() {
        List<Tile> emptyTiles = getEmptyTiles();
        if (!emptyTiles.isEmpty()) {
            emptyTiles.get((int) (emptyTiles.size() * Math.random())).value = Math.random() < 0.9 ? 2 : 4;
        }
    }

    private List<Tile> getEmptyTiles() {
        List<Tile> emptyTiles = new LinkedList<>();
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                if (gameTiles[i][j].value == 0) {
                    emptyTiles.add(gameTiles[i][j]);
                }
            }
        }

        return emptyTiles;
    }

    protected void resetGameTiles() {
        score = 0;
        maxTile = 0;
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
    }

    boolean compressTiles(Tile[] tiles) {
        boolean change = false;
        for (int k = 0; k < tiles.length; k++) {
            for (int i = 0; i < tiles.length - 1; i++) {
                if (tiles[i].value == 0 && tiles[i + 1].value != 0) {
                    tiles[i].value = tiles[i + 1].value;
                    tiles[i + 1].value = 0;
                    if (tiles[i].value != 0) {
                        change = true;
                    }
                }
            }
        }

        return change;
    }

    boolean mergeTiles(Tile[] tiles) {
        boolean change = false;
        for (int i = 0; i < tiles.length - 1; i++) {
            if (tiles[i].value == tiles[i + 1].value && tiles[i].value != 0) {
                tiles[i].value += tiles[i + 1].value;
                tiles[i + 1].value = 0;
                compressTiles(tiles);

                if (tiles[i].value > maxTile) {
                    maxTile = tiles[i].value;
                }

                score += tiles[i].value;
                change = true;
            }
        }

        return change;
    }

    protected void left() {
        boolean change = false;

        if (isSaveNeeded) {
            saveState(gameTiles);
        }

        for (int i = 0; i < gameTiles.length; i++) {
            if (compressTiles(gameTiles[i]) | mergeTiles(gameTiles[i])) {
                change = true;
            }
        }

        if (change == true) {
            addTile();
        }

        isSaveNeeded = true;
    }

    protected void right() {
        boolean change = false;

        saveState(gameTiles);

        rotationClockwise(gameTiles);
        rotationClockwise(gameTiles);

        for (int i = 0; i < gameTiles.length; i++) {
            if (compressTiles(gameTiles[i]) | mergeTiles(gameTiles[i])) {
                change = true;
            }
        }

        if (change == true) {
            addTile();
        }

        rotationClockwise(gameTiles);
        rotationClockwise(gameTiles);
    }

    protected void up() {
        boolean change = false;

        saveState(gameTiles);

        for (int i = 0; i < 3; i++) {
            rotationClockwise(gameTiles);
        }

        for (int i = 0; i < gameTiles.length; i++) {
            if (compressTiles(gameTiles[i]) | mergeTiles(gameTiles[i])) {
                change = true;
            }
        }

        if (change == true) {
            addTile();
        }

        rotationClockwise(gameTiles);
    }

    protected void down() {
        boolean change = false;

        saveState(gameTiles);

        rotationClockwise(gameTiles);

        for (int i = 0; i < gameTiles.length; i++) {
            if (compressTiles(gameTiles[i]) | mergeTiles(gameTiles[i])) {
                change = true;
            }
        }

        if (change == true) {
            addTile();
        }

        for (int i = 0; i < 3; i++) {
            rotationClockwise(gameTiles);
        }
    }

    protected void rotationClockwise(Tile[][] tiles) {
        Tile[][] secondArray = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                secondArray[i][j] = tiles[tiles.length - j - 1][i];
            }
        }

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                tiles[i][j] = secondArray[i][j];
            }
        }
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    public boolean canMove() {
        boolean change = false;
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                if (gameTiles[i][j].value == 0) {
                    change = true;
                }

                if (i != 0 && gameTiles[i][j].value == gameTiles[i - 1][j].value) {
                    change = true;
                }

                if (j != 0 && gameTiles[i][j].value == gameTiles[i][j - 1].value) {
                    change = true;
                }
            }
        }

        return change;
    }

    private void saveState(Tile[][] tiles) {
        Tile[][] saveGameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];

        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles[i].length; j++) {
                saveGameTiles[i][j] = new Tile(tiles[i][j].value);
            }
        }

        previousStates.push(saveGameTiles);
        previousScores.push(score);

        isSaveNeeded = false;
    }

    public void rollback() {
        if (!previousStates.isEmpty() && !previousScores.isEmpty()) {
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }
    }

    public void randomMove() {
        int nextMove = ((int) (Math.random() * 100)) % 4;

        switch (nextMove) {
            case 0: {
                left();
                break;
            }
            case 1: {
                right();
                break;
            }
            case 2: {
                down();
                break;
            }
            case 3: {
                up();
                break;
            }
        }
    }

    public boolean hasBoardChanged() {
        for (int i = 0; i < gameTiles.length; i++) {
            for (int j = 0; j < gameTiles[i].length; j++) {
                if (gameTiles[i][j].value != previousStates.peek()[i][j].value) {
                    return true;
                }
            }
        }

        return false;
    }

    public MoveEfficiency getMoveEfficiency(Move move) {
        move.move();
        MoveEfficiency moveEfficiency = new MoveEfficiency(getEmptyTiles().size(), score, move);

        if (!hasBoardChanged()) {
            return new MoveEfficiency(-1, 0, move);
        }

        rollback();
        return moveEfficiency;
    }

    public void autoMove() {
        PriorityQueue<MoveEfficiency> queue = new PriorityQueue<>(4, Collections.reverseOrder());

        queue.add(getMoveEfficiency(() -> left()));
        queue.add(getMoveEfficiency(() -> up()));
        queue.add(getMoveEfficiency(() -> right()));
        queue.add(getMoveEfficiency(() -> down()));

        queue.peek().getMove().move();
    }
}
