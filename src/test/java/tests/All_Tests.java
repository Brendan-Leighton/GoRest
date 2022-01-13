package tests;
// JAVA
import java.util.Map;
// TEST-NG
import com.fasterxml.jackson.core.JsonProcessingException;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
// OTHER
// CUSTOM
import utils.DB;

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
        Map<String,String> newUser = DB.createUser();
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
        Map<String,String> newUser = DB.createUser();

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

        // assert
//        Assert.assertEquals(data.get("name"), "Mr Meeseeks");
    }

    @Test
    public void test_CreateUser_fieldName_BLANK() {
        Map<String, String> data = DB.createUser();

        // assert
        Assert.assertEquals(data.get("name"), "Mr Meeseeks");
    }

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
        post post's COMMENT
     */
    @Test
    public void test_createPostsComment() {

        // CREATE a user
        Map<String,String> newUser = DB.createUser();

        // CREATE new post
        Map<String, String> newPost = DB.createPost(newUser.get("id"));


        // POST MULTIPLE COMMENTS
        for (int iteration = 1; iteration < 5 ; iteration++) {
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
        Map<String,String> newUser = DB.createUser();

        // DELETE NEW USER
        DB.deleteUser(newUser.get("id"));

        // VERIFY USER DELETED
        DB.getUser_byId(newUser.get("id"), 404);
    }

}
