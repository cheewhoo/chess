package dataaccess;
import model.DataGame;
import java.util.HashSet;

public interface GameDAO {
    HashSet<DataGame> gamesList();
    DataGame getGame(int gameID) throws DataAccessException;
    void makeGame(DataGame game);
    boolean gameExists(int gameID);
    void updateGame(DataGame game);
    void clear();
}
