package com.druidwood.vendingmachineapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.containsString;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class Tests {
    @Test
    public void testAddFloat() {
        Map<String, Integer> floatPayload = new HashMap<>();
        floatPayload.put("FIFTY_PENCE", 20);
        floatPayload.put("ONE_PENCE", 100);

        with().contentType("application/json").body(floatPayload)
                .when()
                .request("POST", "/float")
                .then().body(containsString("1100"))
                .statusCode(200);
    }
}
