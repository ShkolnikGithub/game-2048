import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Controller extends KeyAdapter {
    private static final int WINNING_TILE = 2048;
    private Model model;
    private View view;

    public Controller(Model model) {
        this.model = model;
        view = new View(this);
    }

    public Tile[][] getGameTiles() {
        return model.getGameTiles();
    }

    public int getScore() {
        return model.score;
    }

    @Override
    public void keyPressed(KeyEvent keyEvent) {
        if (!model.canMove()) {
            view.isGameLost = true;
        }

        if (view.isGameLost == false && view.isGameWon == false) {
            switch (keyEvent.getKeyCode()) {
                case KeyEvent.VK_LEFT: {
                    model.left();
                    break;
                }
                case KeyEvent.VK_RIGHT: {
                    model.right();
                    break;
                }
                case KeyEvent.VK_UP: {
                    model.up();
                    break;
                }
                case KeyEvent.VK_DOWN: {
                    model.down();
                    break;
                }
            }
        }

        if (model.maxTile == WINNING_TILE) {
            view.isGameWon = true;
        }

        switch (keyEvent.getKeyCode()) {
            case KeyEvent.VK_ESCAPE: {
                resetGame();
                break;
            }
            case KeyEvent.VK_Z: {
                model.rollback();
                break;
            }
            case KeyEvent.VK_X: {
                model.randomMove();
                break;
            }
            case KeyEvent.VK_C: {
                model.autoMove();
                break;
            }
        }

        view.repaint();
    }

    public void resetGame() {
        model.score = 0;
        view.isGameWon = false;
        view.isGameLost = false;
        model.resetGameTiles();
    }

    public View getView() {
        return view;
    }
}
