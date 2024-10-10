package dataaccess;

import chess.ChessGame;
import model.Data_Game;
import java.util.HashSet;
import java.util.Optional;

public class Mem_Game_DAO implements Game_DAO {
    private HashSet<Data_Game> gameDatabase;

    public Mem_Game_DAO() {
        gameDatabase = new HashSet<>();
    }

    @Override
    public HashSet<Data_Game> Games_lst() {
        return new HashSet<>(gameDatabase); // Return a copy of the game database
    }

    @Override
    public void makeGame(Data_Game game) {
        if (!gameDatabase.contains(game)) {
            gameDatabase.add(game);
        }
    }

    @Override
    public Data_Game getGame(int gameID) throws DataAccessException {
        Optional<Data_Game> foundGame = gameDatabase.stream()
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
    public void updateGame(Data_Game game) {
        gameDatabase.removeIf(g -> g.gameID() == game.gameID());
        gameDatabase.add(game);
    }
}
