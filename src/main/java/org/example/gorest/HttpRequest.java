package org.example.gorest;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.example.gorest.file.ConfigurationManager;

import static io.restassured.RestAssured.given;

@Slf4j
@Data
public class HttpRequest {

    protected String url;
    protected RequestSpecification requestSpecification; // блок request --> post/get, body/headers
    protected Response response; // блок response

    private static final String SLASH = "/";

    public HttpRequest(String url) {
        this.url = url;
        this.requestSpecification = given().baseUri(url)
                .header("Authorization", "Bearer " +
                        ConfigurationManager.getBaseConfig().bearerToken()).contentType(ContentType.JSON)
                .accept(ContentType.JSON);
    }

    public Response get(String endPoint){
        log.info("Performed GET {}", endPoint);
        this.response = given().spec(requestSpecification).get(endPoint);
        logResponse();
        return this.response;
    }

    public Response post(String endPoint, String body){
        log.info("Performed POST {}", endPoint);
        log.info("Body is {}", body);
        this.response = given().spec(requestSpecification).body(body).post(endPoint);
        logResponse();
        return this.response;
    }

    public Response patch(String endPoint, String body){
        log.info("Performed PATCH {}", endPoint);
        log.info("Body is {}", body);
        this.response = given().spec(requestSpecification).body(body).patch(endPoint);
        logResponse();
        return this.response;
    }

    public Response put(String endPoint, String body){
        log.info("Performed PUT {}", endPoint);
        log.info("Body is {}", body);
        this.response = given().spec(requestSpecification).body(body).put(endPoint);
        logResponse();
        return this.response;
    }

    public Response delete(String endPoint){
        log.info("Performed DELETE {}", endPoint);
        this.response = given().spec(requestSpecification).delete(endPoint);
        logResponse();
        return this.response;
    }

    private void logResponse(){
        log.warn("Response is: ");
        log.warn(getResponse().getBody().asPrettyString());
        log.warn("Status code is: {}", getResponse().getStatusCode());
    }

    public String getEndPoint(String... endPoints){ // "..." means optional, strick to be Array
        StringBuilder endPoint = new StringBuilder();

        for (String arg : endPoints){
            endPoint.append(arg).append(SLASH);
        }
        return endPoint.substring(0,endPoint.length()-1); //public, v2, users --> 1) public/
//                                                                                2) public/v2/
//                                                                                3) public/v2/users
    }
}
