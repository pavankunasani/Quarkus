package org.earth.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.containsString;

@QuarkusTest
public class FilemResourceTest {

    @Test
    public void getFilmsTest(){
        RestAssured.given().when().get("/api/v1/films/film/5").then().statusCode(200)
                .body(containsString("AFRICAN EGG"));
    }
}
