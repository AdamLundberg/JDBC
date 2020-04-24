package se.kalmar.adam;

import se.kalmar.adam.dao.CityDaoJDBC;
import se.kalmar.adam.model.City;

import java.util.ArrayList;
import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        CityDaoJDBC cityDaoJDBC = new CityDaoJDBC();

        /*System.out.println(cityDaoJDBC.findAll().size());

        cityDaoJDBC.findAll().forEach(System.out::println);

        System.out.println(cityDaoJDBC.findById(1).toString());

        cityDaoJDBC.findByCode("aaa").forEach(System.out::println);*/

        City kalmar = new City( "Kalmar", "SWE", "Kalmar lan", 10000);
        //kalmar = cityDaoJDBC.add(kalmar);

        List<City> myList = new ArrayList<>();

        myList = cityDaoJDBC.findByName("kalmar");
        myList.forEach(System.out::println);

        kalmar.setPopulation(20000);
        kalmar = cityDaoJDBC.update(kalmar);

        myList = cityDaoJDBC.findByName("kalmar");
        myList.forEach(System.out::println);

        cityDaoJDBC.delete(kalmar);

        myList = cityDaoJDBC.findByName("kalmar");
        myList.forEach(System.out::println);
    }
}
