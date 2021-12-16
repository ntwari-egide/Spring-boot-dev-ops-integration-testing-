/**
 * @author: ntwari egide
 * @description: city controller integration testing
 */

package rw.ac.rca.termOneExam.controller;

import org.json.JSONException;
import org.junit.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.runner.RunWith;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import rw.ac.rca.termOneExam.domain.City;
import rw.ac.rca.termOneExam.dto.CreateCityDTO;
import rw.ac.rca.termOneExam.utils.APICustomResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CityControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void getAll_success() throws JSONException {
        String response = this.restTemplate.getForObject("/api/cities/all", String.class);
        System.out.println(response);
        JSONAssert.assertEquals("[{\"id\":101,\"name\":\"Kigali\",\"weather\":24.0,\"fahrenheit\":75.2},{\"id\":102,\"name\":\"Musanze\",\"weather\":18.0,\"fahrenheit\":64.4},{\"id\":103,\"name\":\"Rubavu\",\"weather\":20.0,\"fahrenheit\":68.0},{\"id\":104,\"name\":\"Nyagatare\",\"weather\":28.0,\"fahrenheit\":82.4}]", response, true);
    }

    @Test
    public void getById_success() {

        ResponseEntity<City> response = this.restTemplate.getForEntity("/api/cities/id/101", City.class);

        System.out.println(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(24.0, response.getBody().getWeather());
        assertEquals(75.2, response.getBody().getFahrenheit());
        assertEquals("Kigali", response.getBody().getName());
    }

    @Test
    public void getById_failed() {

        ResponseEntity<APICustomResponse> response = this.restTemplate.getForEntity("/api/cities/id/1000", APICustomResponse.class);

        System.out.println(response);
        assertEquals(404, response.getStatusCodeValue());
        assertEquals("City not found with id 1000",response.getBody().getMessage());
    }

    @Test
    public void createItem_Success() {

        CreateCityDTO cityDTO=new CreateCityDTO();

        cityDTO.setName("Nyabihu");
        cityDTO.setWeather(0);

        ResponseEntity<City> response = this.restTemplate.postForEntity("/api/cities/add", cityDTO, City.class);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Nyabihu",response.getBody().getName());
        assertEquals(32,response.getBody().getFahrenheit());
    }

    @Test
    public void createItem_BAD_REQUEST() {

        CreateCityDTO cityDTO=new CreateCityDTO();

        cityDTO.setName("Kigali");

        cityDTO.setWeather(19.0);

        ResponseEntity<City> response = this.restTemplate.postForEntity("/api/cities/add", cityDTO, City.class);

        assertEquals(400, response.getStatusCodeValue());
    }


}
