package dataaccess;
import model.Data_Game;
import chess.ChessGame;
import java.util.HashSet;

public interface Game_DAO {
    HashSet<Data_Game> Games_lst();
    void makeGame(int gameID, String whiteUser, String blackUser, String gameName, ChessGame game);
    void makeGame(Data_Game game);
    Data_Game getGame(int gameID) throws DataAccessException;
    boolean gameExists(int gameID);
    void updateGame(Data_Game game);
    void clear();
}
