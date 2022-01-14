package utils;

// JAVA

import com.fasterxml.jackson.core.JsonProcessingException;
import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import models.Req_User;
import models.Request;
import models.Response;
import org.json.JSONObject;
import org.testng.Assert;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static io.restassured.RestAssured.requestSpecification;
import static io.restassured.RestAssured.responseSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

/**
 * Database Query Class
 */
public class DB {

    public static void init() {
        RestAssured.baseURI = "https://gorest.co.in/public/v1/";
        requestSpecification = RestAssured.given()
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + Props.getProp("API_KEY"))
                .log().all();
        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .log(LogDetail.ALL).build();
    }

    //*************************
    //*************************
    //      QUERY BASICS
    //*************************
    //*************************

    private static RequestSpecification query() {
        return requestSpecification;
    }

    public static RequestSpecification query(Object requestBody) {
        return requestSpecification.body(requestBody);
    }

    // GET
    private static String get(String path, int expectedStatusCode) {

        // FIGURE JSON-SCHEMA PATH NEEDED
        String jsonSchemaPath = "";
        if (expectedStatusCode >= 200 && expectedStatusCode < 300) {
            int size = path.split("/").length;
            int slashIndex = path.lastIndexOf('/');
            int questionMarkIndex = path.lastIndexOf('?');
            // if there's a '?' but no '/'
            if (questionMarkIndex != -1 && slashIndex == -1) jsonSchemaPath = path.substring(0, questionMarkIndex);
            // if no '/' and '?'
            if (slashIndex == -1 && questionMarkIndex == -1) jsonSchemaPath = path;
            // path has 1 '/' == something/something, 2 something's
            if (size == 2) jsonSchemaPath = path.substring(0, slashIndex);
            // path has 2 '/' == 1/2/3, 3 something's
            if (size == 3) jsonSchemaPath = path.substring(slashIndex + 1);
        }
        if (expectedStatusCode >= 400 && expectedStatusCode < 500) {
            Integer statusCode = (Integer) expectedStatusCode;
            jsonSchemaPath = statusCode.toString();
        }

        // QUERY DB
        return DB.query().get(path)
                .then().spec(responseSpecification)
                .assertThat().statusCode(expectedStatusCode)
                .body(matchesJsonSchemaInClasspath(jsonSchemaPath + ".json"))
                .extract().response().asString();
    }

    // POST 1
    private static String post(String path, String requestBody, int expectedStatusCode) {

        // FIGURE JSON-SCHEMA PATH NEEDED
        String jsonSchemaPath = "";
        int slashIndex = path.lastIndexOf('/');
        if (slashIndex == -1) jsonSchemaPath = path;
        else jsonSchemaPath = path.substring(slashIndex + 1);

        // QUERY DB
        return DB.query(requestBody).post(path)
                .then().spec(responseSpecification)
                .assertThat().statusCode(expectedStatusCode)
                .body(matchesJsonSchemaInClasspath("JsonSchemas/" + jsonSchemaPath + ".json"))
                .extract().response().asString();
    }

    // POST 2
    private static <Req_Type extends Request, Res_Type extends Response<?>> void post(String path, Req_Type requestBody, int expectedStatusCode) throws JsonProcessingException {

        // QUERY DB
        Res_Type response = DB.query(requestBody).post(path)
                .then().spec(responseSpecification)
                .assertThat().statusCode(expectedStatusCode)
                .body(matchesJsonSchemaInClasspath(requestBody.getJsonSchema()))
                .extract().response().as((Type) requestBody.getResponseType());

        requestBody.assertEquals(response);
    }

    // PATCH
    private static String patch(String path, String requestBody, int expectedStatusCode) {
        return DB.query(requestBody).patch(path)
                .then().spec(responseSpecification)
                .assertThat().statusCode(expectedStatusCode)
                .body(matchesJsonSchemaInClasspath("JsonSchemas/users.json"))
                .extract().response().asString();
    }

    // DELETE
    private static String delete(String path, int expectedStatusCode) {
        return DB.query().delete(path)
                .then().spec(responseSpecification)
                .assertThat().statusCode(expectedStatusCode)
                .extract().response().asString();
    }


    //*************************
    //*************************
    //      GET requests
    //*************************
    //*************************

    public static String getUsers() {
        return DB.get("users", 200);
    }

    /**
     * A generic method for specifying what data is returned from a getAll query.
     * @param collection One of: "users", "posts", "comments", "todos"
     *
     * @param field Depending on Collection...
     *     <ul>
     *          <li>"users":    id, name, gender, email, status</li>
     *          <li>"posts":    id, user_id, title, body</li>
     *          <li>"comments": id, post_id, name, email, body</li>
     *          <li>"todos":    id, user_id, title, due_on, status</li>
     *     </ul>
     *
     * @param searchValue For their respective Fields
     *     <ul>
     *          <li>"id": int, as String</li>
     *          <li>"user_id": ^id</li>
     *          <li>"post_id": ^id</li>
     *          <li>"name": String</li>
     *          <li>"gender": "male", "female"</li>
     *          <li>"email": String or Email format</li>
     *          <li>"status": "active", "inactive"</li>
     *          <li>"title": String</li>
     *          <li>"body": String</li>
     *          <li>"due_on": Date datatype</li>
     *     </ul>
     *
     * @return Map{ String, Map{ String, String } } >>> { "id" { field, value } }
     */
    public static Map<String, Map<String, String>> get_COLLECTION_where_FIELD_contains(String collection, String field, String searchValue) {

        // query DB
        String usersData = DB.get(collection + '?' + field + '=' + searchValue, 200);

        // convert query res to JSON
        JSONObject json = new JSONObject(usersData);

        // initialize return object
        Map<String, Map<String, String>> result = new HashMap<>();

        // loop through query results, assert each Object, add Object to return object
        for (Object object : json.getJSONArray("data")) {

            // Map Object
            Map<String, String> map = null;
            if (collection.equals("users")) { map = JSON.mapUserData(object); }
            if (collection.equals("posts")) { map = JSON.mapPostData(object); }
            if (collection.equals("comments")) { map = JSON.mapCommentData(object); }
            if (collection.equals("todos")) { map = JSON.mapTodoData(object); }

            // assert 'field' == searchValue
            assert map != null;
            Assert.assertTrue(map.get(field).toLowerCase(Locale.ROOT).contains(searchValue.toLowerCase(Locale.ROOT)));

            // add this Object to return object
            result.put(map.get("id"), map);
        }

        return result;
    }

    /**
     * Get a user by their ID and expect a returned status code of 200
     *
     * @param userId ID of the user to query
     * @return
     *      <ul> Map:
     *          <li>"id":     user_id,</li>
     *          <li>"name":   of user,</li>
     *          <li>"gender": of user--male/female,</li>
     *          <li>"email":  of user--follows traditional email formatting,</li>
     *          <li>"status": of user--active/inactive</li>
     *      </ul>
     */
    public static Map<String, String> getUser_byId(String userId) {
        String response = DB.get("users/" + userId, 200);
        return JSON.parseUserData(response);
    }

    /**
     * Get a user by their ID. Specify the expected returned status code.
     *
     * @param userId ID of the user to query
     * @param expectedStatusCode The status-code you expect to be returned.
     * @return IF status-code == 200 || 201 ->
     *      <ul>Map:
     *          <li>"id":     user_id,</li>
     *          <li>"name":   of user,</li>
     *          <li>"gender": of user--male/female,</li>
     *          <li>"email":  of user--follows traditional email formatting,</li>
     *          <li>"status": of user--active/inactive</li>
     *      </ul>
     *          ELSE returns empty HashMap.
     */
    public static Map<String, String> getUser_byId(String userId, int expectedStatusCode) {
        String response = DB.get("users/" + userId, expectedStatusCode);

        if (expectedStatusCode == 200 || expectedStatusCode == 201)
            return JSON.parseUserData(response);
        else { return new HashMap<>(); }
    }

    public static Map<String,Map<String, String>> getCommentsForPost(String postId) {
        String response = DB.get("posts/" + postId + "/comments", 200);

        return JSON.parsePostsCommentData(response);
    }

    public static Map<String, Map<String, String>> getPosts() {
        String response = DB.get("posts", 200);

        return JSON.parsePostsData(response);
    }

    public static Map<String, Map<String, String>> getPostsByUserId(String userId) {
        String response = DB.get("users/" + userId + "/posts", 200);

        return JSON.parsePostsData(response);
    }

    public static Map<String, Map<String, String>> getTodosByUserId(String userId) {
        String response = DB.get("users/" + userId + "/todos", 200);
        return JSON.parseTodosData(response);
    }

    public static Map<String, String> getTodoById(String todoId) {
        String response = DB.get("todos/" + todoId, 200);
        return JSON.parseTodoData(response);
    }

    //*************************
    //*************************
    //      POST requests
    //*************************
    //*************************

    /**
     * Create a user with predetermined data.
     *
     * <p>
     *     {
     *     <br/>_ _ "name": "Mr Meeseeks",
     *     <br/>_ _ "gender": "male",
     *     <br/>_ _ "email": "LifeIsShort${some number}@mail.com"
     *     <br/>_ _ "status": "active"
     *     <br/>}
     * </p>
     *
     * <hr />
     *
     * @return A User Object reflective of the User JSON Schema
     *      <ul>
     *          <li>"id":     user_id,</li>
     *          <li>"name":   of user,</li>
     *          <li>"gender": of user--male/female,</li>
     *          <li>"email":  of user--follows traditional email formatting,</li>
     *          <li>"status": of user--active/inactive</li>
     *      </ul>
     */
    public static Map<String, String> createUser() {
        // make email unique
        String requestBody = "{" +
                "    \"name\": \"Mr Meeseeks\"," +
                "    \"gender\": \"male\"," +
                "    \"email\": \"LifeIsShort"+ System.currentTimeMillis() +"@mail.com\"," +
                "    \"status\": \"active\"" +
                "}";

//        User user = new User(
//                "Mr Meeseeks",
//                "male",
//                "LifeIsShort" + System.currentTimeMillis() + "@mail.com",
//                "active");

        String response = DB.post("users", requestBody, 201);

        return JSON.parseUserData(response);
    }

    /**
     * Create a user with provided data.
     *
     * <hr />
     *
     * @return A User Object reflective of the User JSON Schema
     *      <ul>
     *          <li>"id":     user_id,</li>
     *          <li>"name":   of user,</li>
     *          <li>"gender": of user--male/female,</li>
     *          <li>"email":  of user--follows traditional email formatting,</li>
     *          <li>"status": of user--active/inactive</li>
     *      </ul>
     */
    public static void createUser(String name, String gender, String email, String status, int statusCode) throws JsonProcessingException {

        Req_User reqUser = new Req_User();
        reqUser.init();
        reqUser.setName(name);
        reqUser.setGender(gender);
        reqUser.setEmailUnique(email);
        reqUser.setStatus(status);

        DB.post("users", reqUser, statusCode);

    }

    public static Map<String, String> createPost(String userId) {

        // CREATE request body
        String requestBody = "{" +
                "    \"title\": \"Automated Post\"," +
                "    \"body\": \"Auto Body\"" +
                "}";

        String response = DB.post("users/" + userId + "/posts", requestBody, 201);

        return JSON.parsePostData(response);
    }

    public static Map<String, String> createPost(String userId, String requestBody) {
        String response = DB.post("users/" + userId + "/posts", requestBody, 201);

        return JSON.parsePostData(response);
    }

    public static Map<String, String> createPostComment(String postId, String requestBody) {
        String response = DB.post("posts/" + postId + "/comments", requestBody, 201);

        return JSON.parseCommentData(response);
    }

    public static Map<String, String> createTodo(String userId, String requestBody) {
        String response = DB.post("users/" + userId + "/todos", requestBody, 201);

        return JSON.parseTodoData(response);
    }

    //*************************
    //*************************
    //      PATCH requests
    //*************************
    //*************************

    public static Map<String, String> updateUser_PATCH(String userId) {
        Map<String, String> userData = DB.createUser();

        String userEmail = "LifeIsShort" + System.currentTimeMillis() + "@mail.com";
        // make email unique
        String requestBody = "{" +
                "    \"name\": \"Mr Meeseeks\"," +
                "    \"gender\": \"male\"," +
                "    \"email\": \"" + userEmail + "\"," +
                "    \"status\": \"active\"" +
                "}";

        String response = DB.patch("users/" + userId, requestBody, 200);

        return JSON.parseUserData(response);
    }

    //*************************
    //*************************
    //      DELETE requests
    //*************************
    //*************************

    public static void deleteUser(String userId) {
        DB.delete("users/" + userId, 204);
    }
}
