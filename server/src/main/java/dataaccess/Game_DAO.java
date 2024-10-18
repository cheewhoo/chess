package dataaccess;
import model.Data_Game;
import java.util.HashSet;

public interface Game_DAO {
    HashSet<Data_Game> GamesList();
    Data_Game getGame(int gameID) throws DataAccessException;
    void makeGame(Data_Game game);
    boolean gameExists(int gameID);
    void updateGame(Data_Game game);
    void clear();
}
