package tests;
// JAVA

import java.util.Date;
import java.util.Map;
// TEST-NG
import models.Req_Comments;
import models.Req_Todos;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
// OTHER
import com.fasterxml.jackson.core.JsonProcessingException;
// CUSTOM
import utils.DB;
import models.Req_Post;
import models.Req_User;

import static io.restassured.RestAssured.responseSpecification;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class All_Tests {

    @BeforeTest
    public void setup() {
        DB.init();
    }

    //*************************
    //*************************
    //      GET tests
    //*************************
    //*************************

    /*
        get USERS
     */

    @Test
    public void test_getUsers() {
        DB.getUsers();
    }

    @Test
    public void test_getUser_byId() {
        Map<String, String> newUser = DB.createUser();

        DB.getUser_byId(newUser.get("id"), 200);
    }

    @Test
    public void test_getUsersWhere_NameIs_Test() {
        DB.get_COLLECTION_where_FIELD_contains("users", "name", "test");
    }

    @Test
    public void test_getUsersWhere_NameIs_BLANK() {
        DB.get_COLLECTION_where_FIELD_contains("users", "name", "");
    }

    @Test
    public void test_getUsersWhere_EmailIs_Gmail() {
        DB.get_COLLECTION_where_FIELD_contains("users", "email", "@gmail.com");
    }

    @Test
    public void test_getUsersWhere_EmailIs_BLANK() {
        DB.get_COLLECTION_where_FIELD_contains("users", "email", "");
    }

    @Test
    public void test_getUsersWhere_GenderIs_Male() {
        DB.get_COLLECTION_where_FIELD_contains("users", "gender", "male");
    }

    @Test
    public void test_getUsersWhere_GenderIs_Female() {
        DB.get_COLLECTION_where_FIELD_contains("users", "gender", "female");
    }

    @Test
    public void test_getUsersWhere_GenderIs_BLANK() {
        DB.get_COLLECTION_where_FIELD_contains("users", "gender", "");
    }

    @Test
    public void test_getUsersWhere_StatusIs_Active() {
        DB.get_COLLECTION_where_FIELD_contains(
                "users", "status", "active"
        );
    }

    @Test
    public void test_getUsersWhere_StatusIs_Inactive() {
        DB.get_COLLECTION_where_FIELD_contains(
                "users", "status", "inactive"
        );
    }

    @Test
    public void test_getUsersWhere_StatusIs_BLANK() {
        DB.get_COLLECTION_where_FIELD_contains(
                "users", "status", ""
        );
    }

    /*
        get POSTS
     */

    @Test
    public void test_getPosts() {
        DB.getPosts();
    }

    @Test
    public void test_getPostsByUserId() {

        // CREATE a user
        Map<String, String> newUser = DB.createUser();
        // CREATE a post
        DB.createPost(newUser.get("id"));
        // GET posts for new user
        DB.getPostsByUserId(newUser.get("id"));
    }

    @Test
    public void test_getPostsWhereTitleIsTest() {
        DB.get_COLLECTION_where_FIELD_contains(
                "posts", "title", "test"
        );
    }

    /*
        get COMMENTS
     */

    @Test
    public void test_getPostsComments() {
        String postId = "4";

        DB.getCommentsForPost(postId);
    }

    /*
         get TODOS
     */

    @Test
    public void test_getTodoById() {

        // CREATE a user
        Map<String, String> newUser = DB.createUser();

        // CREATE request body
        String requestBody = "{" +
                "    \"title\": \"Automate Life\"," +
                "    \"status\": \"pending\"" +
                "}";

        // CREATE a to-do
        Map<String, String> newTodo = DB.createTodo(newUser.get("id"), requestBody);

        // GET USER'S TODOS
        DB.getTodoById(newTodo.get("id"));
    }


    //*************************
    //*************************
    //      POST tests
    //*************************
    //*************************

    /*
        post USER
     */
    @Test
    public void test_CreateUser() throws JsonProcessingException {
        DB.createUser(
                "Ben Dover",
                "male",
                "SoapDropper@email.com",
                "active",
                201
        );
    }
    /*
        post USER <END>
     */

    /*
        post USER : blank fields
     */
    @Test
    public void test_CreateUser_fieldName_BLANK() {

        Req_User newUser = new Req_User();
        newUser.init();
        newUser.setName("");
        newUser.setGender("male");
        newUser.setEmailUnique("email@email.com");
        newUser.setStatus("active");

        String response = DB.query(newUser).post("users")
                .then().spec(responseSpecification)
                .assertThat().statusCode(422)
                .body(matchesJsonSchemaInClasspath("JsonSchemas/Blank_Field/name.json"))
                .extract().response().asString();
    }

    @Test
    public void test_CreateUser_fieldGender_BLANK() {

        Req_User newUser = new Req_User();
        newUser.init();
        newUser.setName("Ila Vainal");
        newUser.setGender("");
        newUser.setEmailUnique("email@email.com");
        newUser.setStatus("active");

        String response = DB.query(newUser).post("users")
                .then().spec(responseSpecification)
                .assertThat().statusCode(422)
                .body(matchesJsonSchemaInClasspath("JsonSchemas/Blank_Field/gender.json"))
                .extract().response().asString();
    }

    @Test
    public void test_CreateUser_fieldEmail_BLANK() {

        Req_User newUser = new Req_User();
        newUser.init();
        newUser.setName("Ila Vainal");
        newUser.setGender("female");
        newUser.setEmail("");
        newUser.setStatus("active");

        String response = DB.query(newUser).post("users")
                .then().spec(responseSpecification)
                .assertThat().statusCode(422)
                .body(matchesJsonSchemaInClasspath("JsonSchemas/Blank_Field/email.json"))
                .extract().response().asString();
    }

    @Test
    public void test_CreateUser_fieldStatus_BLANK() {

        Req_User newUser = new Req_User();
        newUser.init();
        newUser.setName("Ila Vainal");
        newUser.setGender("male");
        newUser.setEmailUnique("email@email.com");
        newUser.setStatus("");

        String response = DB.query(newUser).post("users")
                .then().spec(responseSpecification)
                .assertThat().statusCode(422)
                .body(matchesJsonSchemaInClasspath("JsonSchemas/Blank_Field/status.json"))
                .extract().response().asString();
    }
    /*
        post USER : blank fields <END>
     */

    /*
        post user's POST
     */
    @Test
    public void test_createUserPost() {

        // CREATE USER
        Map<String, String> newUser = DB.createUser();

        // CREATE request body
        String requestBody = "{" +
                "    \"title\": \"Automated Post\"," +
                "    \"body\": \"Auto Body\"" +
                "}";

        DB.createPost(newUser.get("id"), requestBody);
    }

    /*
        post user's POST : blank fields
     */
    @Test
    public void test_CreatePost_fieldTitle_BLANK() {

        // CREATE USER
        Map<String, String> newUser = DB.createUser();

        Req_Post newPost = new Req_Post();
        newPost.init();
        newPost.setTitle("");
        newPost.setBody("Ma Body");

        String response = DB.query(newPost).post("users/" + newUser.get("id") + "/posts")
                .then().spec(responseSpecification)
                .assertThat().statusCode(422)
                .body(matchesJsonSchemaInClasspath("JsonSchemas/Blank_Field/title.json"))
                .extract().response().asString();
    }

    @Test
    public void test_CreatePost_fieldBody_BLANK() {

        // CREATE USER
        Map<String, String> newUser = DB.createUser();

        Req_Post newPost = new Req_Post();
        newPost.init();
        newPost.setTitle("Ma Title");
        newPost.setBody("");

        String response = DB.query(newPost).post("users/" + newUser.get("id") + "/posts")
                .then().spec(responseSpecification)
                .assertThat().statusCode(422)
                .body(matchesJsonSchemaInClasspath("JsonSchemas/Blank_Field/body.json"))
                .extract().response().asString();
    }
    /*
        post user's POST : blank fields <END>
     */

    /*
        post post's COMMENT
     */
    @Test
    public void test_createPostsComment() {

        // CREATE a user
        Map<String, String> newUser = DB.createUser();

        // CREATE new post
        Map<String, String> newPost = DB.createPost(newUser.get("id"));


        // POST MULTIPLE COMMENTS
        for (int iteration = 1; iteration < 5; iteration++) {
            // CREATE request body
            String requestBody = "{" +
                    "    \"name\": \"Drowning Pool\"," +
                    "    \"email\": \"PoolParty@mail.com\"," +
                    "    \"body\": \"" + iteration + " Nothing wrong with me!\"" +
                    "}";

            DB.createPostComment(newPost.get("id"), requestBody);
        }
    }

    /*
        post COMMENT : blank fields
     */
    @Test
    public void test_createComment_withBlank_name() {

        // CREATE a user
        Map<String, String> newUser = DB.createUser();

        // CREATE new post
        Map<String, String> newPost = DB.createPost(newUser.get("id"));

        Req_Comments request = new Req_Comments();
        request.init();
        request.setName("");
        request.setEmail("email@email.com");
        request.setBody("Let this body hit the floor");

        String response = DB.query(request).post("posts/" + newPost.get("id") + "/comments")
                .then().spec(responseSpecification)
                .assertThat().statusCode(422)
                .body(matchesJsonSchemaInClasspath("JsonSchemas/Blank_Field/name.json"))
                .extract().response().asString();
    }

    @Test
    public void test_createComment_withBlank_email() {

        // CREATE a user
        Map<String, String> newUser = DB.createUser();

        // CREATE new post
        Map<String, String> newPost = DB.createPost(newUser.get("id"));

        Req_Comments request = new Req_Comments();
        request.init();
        request.setName("Drowning Pool");
        request.setEmail("");
        request.setBody("Let this body hit the floor");

        String response = DB.query(request).post("posts/" + newPost.get("id") + "/comments")
                .then().spec(responseSpecification)
                .assertThat().statusCode(422)
                .body(matchesJsonSchemaInClasspath("JsonSchemas/Blank_Field/email.json"))
                .extract().response().asString();
    }

    @Test
    public void test_createComment_withBlank_body() {

        // CREATE a user
        Map<String, String> newUser = DB.createUser();

        // CREATE new post
        Map<String, String> newPost = DB.createPost(newUser.get("id"));

        Req_Comments request = new Req_Comments();
        request.init();
        request.setName("Drowning Pool");
        request.setEmail("email@email.com");
        request.setBody("");

        String response = DB.query(request).post("posts/" + newPost.get("id") + "/comments")
                .then().spec(responseSpecification)
                .assertThat().statusCode(422)
                .body(matchesJsonSchemaInClasspath("JsonSchemas/Blank_Field/body.json"))
                .extract().response().asString();
    }
    /*
        post COMMENT : blank fields <END>
     */
    /*
        post post's COMMENT <END>
     */

    /*
        post user's TO-DO
     */
    @Test
    public void test_createUsersTodo() {

        // CREATE a user
        Map<String, String> newUser = DB.createUser();

        // CREATE request body
        String requestBody = "{" +
                "    \"title\": \"Automate Life\"," +
                "    \"status\": \"pending\"" +
                "}";

        DB.createTodo(newUser.get("id"), requestBody);
    }

    /*
        post TO-DO : blank fields
     */
    @Test
    public void test_createTodo_withBlank_title() {

        // CREATE a user
        Map<String, String> newUser = DB.createUser();

        Req_Todos request = new Req_Todos();
        request.init();
        request.setTitle("");
        request.setStatus("pending");

        String response = DB.query(request).post("users/" + newUser.get("id") + "/todos")
                .then().spec(responseSpecification)
                .assertThat().statusCode(422)
                .body(matchesJsonSchemaInClasspath("JsonSchemas/Blank_Field/title.json"))
                .extract().response().asString();
    }

    @Test
    public void test_createTodo_withBlank_status() {

        // CREATE a user
        Map<String, String> newUser = DB.createUser();

        Req_Todos request = new Req_Todos();
        request.init();
        request.setTitle("title");
        request.setStatus("");

        String response = DB.query(request).post("users/" + newUser.get("id") + "/todos")
                .then().spec(responseSpecification)
                .assertThat().statusCode(422)
                .body(matchesJsonSchemaInClasspath("JsonSchemas/Blank_Field/status.json"))
                .extract().response().asString();
    }
    /*
        post TO-DO : blank fields <END>
     */

    //*************************
    //*************************
    //      PATCH tests
    //*************************
    //*************************

    /*
        patch USER
     */
    @Test
    public void test_PatchUser() {

        // CREATE USER
        Map<String, String> userData = DB.createUser();

        // UPDATE NEW USER
        Map<String, String> res = DB.updateUser_PATCH(userData.get("id"));

        // GET UPDATED USER
        Map<String, String> updatedUserData = DB.getUser_byId(userData.get("id"));

        // VERIFY CHANGE
        Assert.assertEquals(res.get("email"), updatedUserData.get("email"));
    }


    //*************************
    //*************************
    //      DELETE tests
    //*************************
    //*************************

    @Test
    public void test_DeleteUser() {

        // CREATE A USER
        Map<String, String> newUser = DB.createUser();

        // DELETE NEW USER
        DB.deleteUser(newUser.get("id"));

        // VERIFY USER DELETED
        DB.getUser_byId(newUser.get("id"), 404);
    }

}
