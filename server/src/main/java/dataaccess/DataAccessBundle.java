package dataaccess;


public class DataAccessBundle {
    public final UserDAO userDAO;
    public final AuthDAO authDAO ;
    public final GameDAO gameDAO;
    public DataAccessBundle(boolean isInMemory){
        userDAO = (isInMemory) ? new MemoryUserDAO() : new MySQLUserDAO();
        authDAO = (isInMemory) ? new MemoryAuthDAO() :new MySQLAuthDAO();
        gameDAO = (isInMemory) ? new MemoryGameDAO() :new MySQLGameDAO();
    }
}
