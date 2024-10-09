package dataaccess;
import chess.ChessGame;
import model.Data_Game;
import java.util.HashSet;




public class Mem_Game_DAO implements Game_DAO{
    HashSet<Data_Game> db;
    public Mem_Game_DAO() {
        db = HashSet.newHashSet(16);
    }
    @Override
    public HashSet<Data_Game> Games_lst() {
        return db;
    }
    @Override
    public void makeGame(int gameID, String whiteUser, String blackUser, String gameName, ChessGame game) {
        db.add(new Data_Game(gameID, whiteUser, blackUser, gameName, game));
    }
    @Override
    public void makeGame(Data_Game game) {
        db.add(game);
    }
    @Override
    public Data_Game getGame(int gameID) throws DataAccessException {
        for (Data_Game game : db) {
            if (game.gameID() == gameID) {
                return game;
            }
        }
        throw new DataAccessException("Unknown Game: " +gameID);
    }
    @Override
    public void clear() {
        db = HashSet.newHashSet(16);
    }
    @Override
    public boolean gameExists(int gameID) {
        for (Data_Game game : db) {
            if (game.gameID() == gameID) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void updateGame(Data_Game game) {
        try {
            db.remove(getGame(game.gameID()));
            db.add(game);
        } catch (DataAccessException e) {
            db.add(game);
        }
    }

}
