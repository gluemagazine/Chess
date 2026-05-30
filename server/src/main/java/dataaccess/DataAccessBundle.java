package dataaccess;


import dataaccess.daointerfaces.AuthDAO;
import dataaccess.daointerfaces.GameDAO;
import dataaccess.daointerfaces.UserDAO;
import exceptions.DataAccessException;
import dataaccess.memorydaos.MemoryAuthDAO;
import dataaccess.memorydaos.MemoryGameDAO;
import dataaccess.memorydaos.MemoryUserDAO;
import dataaccess.sqldataaccess.MySQLAuthDAO;
import dataaccess.sqldataaccess.MySQLGameDAO;
import dataaccess.sqldataaccess.MySQLUserDAO;

public class DataAccessBundle {
    public final UserDAO userDAO;
    public final AuthDAO authDAO ;
    public final GameDAO gameDAO;
    public DataAccessBundle(boolean isInMemory){
        try {
            userDAO = (isInMemory) ? new MemoryUserDAO() : new MySQLUserDAO();
            authDAO = (isInMemory) ? new MemoryAuthDAO() : new MySQLAuthDAO();
            gameDAO = (isInMemory) ? new MemoryGameDAO() : new MySQLGameDAO();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
