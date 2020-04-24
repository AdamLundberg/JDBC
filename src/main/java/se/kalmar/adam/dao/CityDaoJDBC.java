package se.kalmar.adam.dao;

import se.kalmar.adam.model.City;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CityDaoJDBC implements CityDao {

    private static final String URL = "jdbc:mysql://localhost:3306/world?&autoReconnect=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=Europe/Berlin";
    private static final String USER_NAME = "root";
    private static final String PASSWORD = "123456789";

    private static final String FIND_BY_ID = "SELECT * FROM city WHERE ID LIKE ?";
    private static final String FIND_BY_CODE = "SELECT * FROM city WHERE CountryCode LIKE ?";
    private static final String FIND_BY_NAME = "SELECT * FROM city WHERE Name LIKE ?";
    private static final String FIND_ALL = "SELECT * FROM city";
    private static final String ADD_TO_CITY = "INSERT INTO city (Name, CountryCode, District, Population) VALUES( ?, ?, ?, ?)";
    private static final String UPDATE_CITY = "UPDATE city SET Name = ?, CountryCode = ?, District = ?, Population = ? WHERE ID = ?";
    private static final String DELETE_CITY = "DELETE FROM city WHERE ID LIKE ?";

    @Override
    public City findById(int id) {
        City cityById = null;

        try(Connection connection = getConnection();
            PreparedStatement findById = createStatementFindById(connection, id);
            ResultSet rs = findById.executeQuery()
        ) {
            while (rs.next()) {
                cityById = createCity(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
       return cityById;
    }

    private PreparedStatement createStatementFindById(Connection connection, int id) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_ID);
        preparedStatement.setInt(1, id);
        return preparedStatement;
    }

    @Override
    public List<City> findByCode(String code) {
        List<City> ListByCode = new ArrayList<>();

        try(Connection connection = getConnection();
            PreparedStatement findById = createStatementFindByCode(connection, code);
            ResultSet rs = findById.executeQuery()
        ) {
            while (rs.next()) {
                ListByCode.add(createCity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ListByCode;
    }

    private PreparedStatement createStatementFindByCode(Connection connection, String code) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_CODE);
        preparedStatement.setString(1, code);
        return preparedStatement;
    }

    @Override
    public List<City> findByName(String name) {
        List<City> ListByName = new ArrayList<>();

        try(Connection connection = getConnection();
            PreparedStatement findById = createStatementFindByName(connection, name);
            ResultSet rs = findById.executeQuery()
        ) {
            while (rs.next()) {
                ListByName.add(createCity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ListByName;
    }

    private PreparedStatement createStatementFindByName(Connection connection, String name) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_NAME);
        preparedStatement.setString(1, name);
        return preparedStatement;
    }

    @Override
    public List<City> findAll() {
        List<City> cityList = new ArrayList<>();

        try(Connection connection = getConnection();
            PreparedStatement findAll = createStatementFindAll(connection);
            ResultSet rs = findAll.executeQuery()
        ) {
            while(rs.next()) {
                cityList.add(createCity(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return cityList;
    }

    private PreparedStatement createStatementFindAll(Connection connection) throws SQLException {
        return connection.prepareStatement(FIND_ALL);
    }

    @Override
    public City add(City city) {
        City cityFromTable = null;
        ResultSet generatedKeys = null;

        try(Connection connection = getConnection();
            PreparedStatement add = createStatementAdd(connection, city)
        ) {
            add.executeUpdate();

            generatedKeys = add.getGeneratedKeys();
            while (generatedKeys.next()) {
                cityFromTable = new City(generatedKeys.getInt(1), city.getName(), city.getCountryCode(), city.getDistrict(), city.getPopulation());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (generatedKeys != null) {
                    generatedKeys.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return cityFromTable;
    }

    private PreparedStatement createStatementAdd(Connection connection, City city) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(ADD_TO_CITY, Statement.RETURN_GENERATED_KEYS);
        preparedStatement.setString(1, city.getName());
        preparedStatement.setString(2, city.getCountryCode());
        preparedStatement.setString(3, city.getDistrict());
        preparedStatement.setInt(4, city.getPopulation());
        return preparedStatement;
    }

    @Override
    public City update(City city) {
        try(Connection connection = getConnection();
            PreparedStatement update = createStatementUpdate(connection, city)
        ) {
            update.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return city;
    }

    private PreparedStatement createStatementUpdate(Connection connection, City city) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CITY);
        preparedStatement.setString(1, city.getName());
        preparedStatement.setString(2, city.getCountryCode());
        preparedStatement.setString(3, city.getDistrict());
        preparedStatement.setInt(4, city.getPopulation());
        preparedStatement.setInt(5, city.getId());
        return preparedStatement;
    }

    @Override
    public int delete(City city) {
        int rowsAffected = 0;

        try(Connection connection = getConnection();
            PreparedStatement delete = connection.prepareStatement(DELETE_CITY)
        ) {
            delete.setInt(1, city.getId());
            rowsAffected = delete.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    private City createCity(ResultSet rs) throws SQLException {
        return new City(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getInt(5));
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }
}
