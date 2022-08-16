package lesson3;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeAll;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public abstract class AbstractTest {

    static Properties prop = new Properties();
    private static InputStream configFile;
    private static String apiKey;
    private static String baseUrl;
    protected static ResponseSpecification responseSpecification;
    protected static RequestSpecification requestSpecification;
    protected static RequestSpecification requestSpecification1;
    protected static RequestSpecification shoppinglistRequestSpecification;
    @BeforeAll
    static void initTest() throws IOException {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        configFile = new FileInputStream("src/main/resources/my.properties");
        prop.load(configFile);

        apiKey =  prop.getProperty("apiKey");
        baseUrl= prop.getProperty("base_url");

        responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(200)
                .expectStatusLine("HTTP/1.1 200 OK")
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .log(LogDetail.ALL)
                .build();


        requestSpecification = new RequestSpecBuilder()
                .addQueryParam("apiKey", apiKey)
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();
        requestSpecification1 = new RequestSpecBuilder()
                .addQueryParam("apiKey", apiKey)
                .addQueryParam("includeNutrition", "false")
                .addPathParam("id","715594")
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        shoppinglistRequestSpecification = new RequestSpecBuilder()
                .addQueryParam("apiKey", apiKey)
                .addQueryParam("hash", "21c5194a2e65969807fd36901a5353de562c10f6")
                .setContentType(ContentType.JSON)
                .log(LogDetail.ALL)
                .build();

        //RestAssured.responseSpecification = responseSpecification;
        //RestAssured.requestSpecification = requestSpecification;
    }

    public static String getApiKey() {
        return apiKey;
    }

    public static String getBaseUrl() {
        return baseUrl;
    }

    public RequestSpecification getRequestSpecification(){
        return requestSpecification;
    }
}
