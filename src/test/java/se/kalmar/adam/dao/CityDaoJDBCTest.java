package se.kalmar.adam.dao;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import se.kalmar.adam.model.City;

import java.util.List;

public class CityDaoJDBCTest {

    CityDaoJDBC cityDaoJDBC;

    @Before
    public void before() {
        cityDaoJDBC = new CityDaoJDBC();
    }

    @After
    public void after() {
        List<City> kalmar = cityDaoJDBC.findByName("Kalmar");

        kalmar.forEach(city -> cityDaoJDBC.delete(city));
    }

    @Test
    public void findById_Successfully() {
        int id = 1;

        City foundById = cityDaoJDBC.findById(id);

        assertNotNull(foundById);
    }

    @Test
    public void findById_Unsuccessfully() {
        int id = 9999;

        City foundById = cityDaoJDBC.findById(id);

        assertNull(foundById);
    }

    @Test
    public void findByCode_Successfully() {
        String code = "SWE";

        List<City> cityList = cityDaoJDBC.findByCode(code);

        assertTrue(cityList.size() > 0);
    }

    @Test
    public void findByCode_Unsuccessfully() {
        String code = "AAA";

        List<City> cityList = cityDaoJDBC.findByCode(code);

        assertEquals(0, cityList.size());
    }

    @Test
    public void findByName_Successfully() {
        String name = "Stockholm";

        List<City> cityList = cityDaoJDBC.findByName(name);

        assertTrue(cityList.size() > 0);
    }

    @Test
    public void findByName_Unsuccessfully() {
        String name = "Kalmar";

        List<City> cityList = cityDaoJDBC.findByName(name);

        assertEquals(0, cityList.size());
    }

    @Test
    public void findAll_Successfully() {
        List<City> cityList = cityDaoJDBC.findAll();

        assertTrue(cityList.size() > 0);
        assertEquals(4079, cityList.size());
    }

    @Test
    public void add_Successfully() {
        City kalmar = new City( "Kalmar", "SWE", "Kalmar lan", 70_000);
        kalmar = cityDaoJDBC.add(kalmar);

        assertNotNull(cityDaoJDBC.findById(kalmar.getId()));
    }

    @Test
    public void update_Successfully() {
        City kalmar = new City( "Kalmar", "SWE", "Kalmar lan", 70_000);
        kalmar = cityDaoJDBC.add(kalmar);

        int newPopulation = 10;

        kalmar.setPopulation(newPopulation);
        kalmar = cityDaoJDBC.update(kalmar);

        assertEquals(newPopulation, kalmar.getPopulation());
        assertEquals(newPopulation, cityDaoJDBC.findById(kalmar.getId()).getPopulation());
    }

    @Test
    public void delete_Successfully() {
        City kalmar = new City( "Kalmar", "SWE", "Kalmar lan", 70_000);
        kalmar = cityDaoJDBC.add(kalmar);

        int databaseSize = cityDaoJDBC.findAll().size();

        int affectedRows = cityDaoJDBC.delete(kalmar);

        assertEquals(databaseSize - 1, cityDaoJDBC.findAll().size());
        assertTrue(affectedRows > 0);
        assertNull(cityDaoJDBC.findById(kalmar.getId()));
    }
}
