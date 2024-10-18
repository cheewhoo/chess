package dataaccess;

import model.DataGame;
import java.util.HashSet;
import java.util.Optional;

public class MemGameDAO implements GameDAO {
    private HashSet<DataGame> gameDatabase;

    public MemGameDAO() {
        gameDatabase = new HashSet<>();
    }

    @Override
    public HashSet<DataGame> gamesList() {
        return new HashSet<>(gameDatabase); // Return a copy of the game database
    }

    @Override
    public void makeGame(DataGame game) {
        if (!gameDatabase.contains(game)) {
            gameDatabase.add(game);
        }
    }

    @Override
    public DataGame getGame(int gameID) throws DataAccessException {
        Optional<DataGame> foundGame = gameDatabase.stream()
                .filter(game -> game.gameID() == gameID)
                .findFirst();

        return foundGame.orElseThrow(() -> new DataAccessException("Game not found: " + gameID));
    }

    @Override
    public void clear() {
        gameDatabase.clear();
    }

    @Override
    public boolean gameExists(int gameID) {
        return gameDatabase.stream().anyMatch(game -> game.gameID() == gameID);
    }

    @Override
    public void updateGame(DataGame game) {
        gameDatabase.removeIf(g -> g.gameID() == game.gameID());
        gameDatabase.add(game);
    }
}
