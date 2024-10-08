package dataaccess;
import model.Data_Game;
import chess.ChessGame;
import java.util.HashSet;

public interface Game_DAO {
    HashSet<Data_Game> listGames(String username);
    void makeGame(int gameID, String whiteUser, String blackUser, String gameName, ChessGame game);
    void makeGame(Data_Game game);
    Data_Game getGame(int gameID) throws DataAccessException;
    void clear();
}
