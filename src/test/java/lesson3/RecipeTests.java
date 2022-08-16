package lesson3;

import io.restassured.http.ContentType;
import io.restassured.http.Cookie;
import io.restassured.http.Headers;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.lessThan;

public class RecipeTests extends AbstractTest {

    @Test
    void getRecipePositiveTest() {
        given().spec(getRequestSpecification())
                .when()
                .get(getBaseUrl() + "recipes/complexSearch")
                .then()
                .spec(responseSpecification);
    }

    @Test
    void getRecipeNegativeTest() {
        given()
                .when()
                .get(getBaseUrl() + "recipes/complexSearch").prettyPeek()
                .then()
                .statusCode(401);
    }

    @Test
    void getRecipeWithQueryParametersSpec1PositiveTest() {
        given().spec(requestSpecification1)
                .when()
                .get(getBaseUrl() + "recipes/{id}/information")
                .then()
                .spec(responseSpecification);
    }

    @Test
    void getRecipeWithBodyPositiveTest() {
        JsonPath response = given().spec(requestSpecification)
                .when()
                .queryParam("number", "14")
                .get(getBaseUrl() + "recipes/complexSearch").prettyPeek()
                .body()
                .jsonPath();
        assertThat(response.get("number"), equalTo(14));
    }

    @Test
    void getRecipeCuisineTest() {
        JsonPath response = given().spec(requestSpecification)
                .when()
                .queryParam("cuisine", "Russian")
                .get(getBaseUrl() + "recipes/complexSearch").prettyPeek()
                .body()
                .jsonPath();
        assertThat(response.get("totalResults"), equalTo(0));

    }

    @Test
    void postRecipeCuisineTest() {
        JsonPath response = given().spec(requestSpecification)
                .when()
                .post(getBaseUrl() + "recipes/cuisine").prettyPeek()
                .body()
                .jsonPath();
        assertThat(response.get("cuisine"), equalTo("Mediterranean"));
    }

    @Test
    void postRecipeCuisineQueryTest() {
        JsonPath response = given().spec(requestSpecification)
                .contentType("application/x-www-form-urlencoded")
                .formParam("title", "Cauliflower, Brown Rice, and Vegetable Fried Rice")
                .when()
                .post(getBaseUrl() + "recipes/cuisine").prettyPeek()
                .body()
                .jsonPath();
        assertThat(response.get("cuisine"), equalTo("Chinese"));
    }

    @Test
    void postRecipeCuisineHeaderTest() {
        given().spec(requestSpecification)
                .when()
                .post(getBaseUrl() + "recipes/cuisine")
                .then()
                .assertThat()
                .spec(responseSpecification)
                .statusLine(containsString("OK"))
                .header("Connection", "keep-alive");

    }

    String id;

    @Test
    void addDeleteMealplanTest() {
        id = given()
                .queryParam("hash", "21c5194a2e65969807fd36901a5353de562c10f6")
                .queryParam("apiKey", getApiKey())
                .body("{\n"
                        + " \"date\": 1660547927,\n"
                        + " \"slot\": 2,\n"
                        + " \"position\": 1,\n"
                        + " \"type\": \"INGREDIENTS\",\n"
                        + " \"value\": {\n"
                        + " \"ingredients\": [\n"
                        + " {\n"
                        + " \"name\": \"5 apple\"\n"
                        + " }\n"
                        + " ]\n"
                        + " }\n"
                        + "}")
                .when()
                .post(getBaseUrl() + "mealplanner/aleks/items")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .get("id")
                .toString();

        given()
                .queryParam("hash", "21c5194a2e65969807fd36901a5353de562c10f6")
                .queryParam("apiKey", getApiKey())
                .delete("https://api.spoonacular.com/mealplanner/aleks/items/" + id)
                .then()
                .statusCode(200);
    }

    @Test
    void PostGetDeleteShoppinglistTest() {
        id = given().spec(ShoppinglistRequestSpecification)
                .body("{\n"
                        + " \"date\": 1660547927,\n"
                        + " \"slot\": 1,\n"
                        + " \"position\": 0,\n"
                        + " \"type\": \"INGREDIENTS\",\n"
                        + " \"value\": {\n"
                        + " \"ingredients\": [\n"
                        + " {\n"
                        + " \"name\": \"1 egs\"\n"
                        + " }\n"
                        + " ]\n"
                        + " }\n"
                        + "}")
                .when()
                .post(getBaseUrl() + "mealplanner/aleks/shopping-list/items")
                .then()
                .spec(responseSpecification)
                .extract()
                .jsonPath()
                .get("id")
                .toString();

        given().spec(ShoppinglistRequestSpecification)
                .get(getBaseUrl() + "mealplanner/aleks/shopping-list")
                .then()
                .spec(responseSpecification);

        given().spec(ShoppinglistRequestSpecification)
                .delete("https://api.spoonacular.com/mealplanner/aleks/shopping-list/items/" + id)
                .then()
                .spec(responseSpecification);
    }
}
