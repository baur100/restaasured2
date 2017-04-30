package org.megaprint;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

/**
 * Created by Baurz on 4/15/2017.
 */
public class PostTest {

    Response res;
    Properties prop=new Properties();
    String placeid=null;
    String body="{" +
            "  \"location\": {" +
            "    \"lat\": -33.8669710," +
            "    \"lng\": 151.1958750" +
            "  }," +
            "  \"accuracy\": 50," +
            "  \"name\": \"Google Shoes!\"," +
            "  \"phone_number\": \"(02) 9374 4000\"," +
            "  \"address\": \"48 Pirrama Road, Pyrmont, NSW 2009, Australia\"," +
            "  \"types\": [\"shoe_store\"]," +
            "  \"website\": \"http://www.google.com.au/\"," +
            "  \"language\": \"en-AU\"" +
            "}";
    @BeforeMethod
    public void init() throws IOException {

        FileInputStream fis=new FileInputStream("data//env.properties");
        prop.load(fis);
        RestAssured.baseURI=prop.getProperty("HOST");
    }
    @Test
    public void test1(){
        res=given().
                queryParam("key",prop.getProperty("KEY")).
                body(body).
        when().
                post("/maps/api/place/add/json").
        then().
                assertThat().
                statusCode(200).
                and().
                contentType(ContentType.JSON).
                and().
                body("status",equalTo("OK")).
        extract().response();
    }

    @Test
    public void test2(){
         System.out.println(res.asString());
    }

    @Test
    public void test3(){
        JsonPath js=new JsonPath(res.asString());
        placeid=js.get("place_id");
        System.out.println(placeid);
    }

    @Test
    public void test4(){
        given().
                queryParam("key",prop.getProperty("KEY")).
                body("{"+
                        "\"place_id\":\""+placeid+"\""+
                        "}").
        when().
                post("/maps/api/place/delete/json").
        then().
                assertThat().
                statusCode(200).
                and().
                body("status",equalTo("OK"));


    }
}



