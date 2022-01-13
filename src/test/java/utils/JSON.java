package utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class JSON {

    public static JSONObject parse(String response) {
        // parse response
        JSONObject json = new JSONObject(response);
        return json.getJSONObject("data");
    }

    /**
     *
     * Parses data from JSON that represents a User returned from a query.
     *
     * @param response Query response returning a User's data
     * @return Map:
     *      <ul>
     *          <li>"id":     user_id,</li>
     *          <li>"name":   of user,</li>
     *          <li>"gender": of user--male/female,</li>
     *          <li>"email":  of user--follows traditional email formatting,</li>
     *          <li>"status": of user--active/inactive</li>
     *      </ul>
     */
    public static Map<String,String> parseUserData(String response) {

        // turn String response into JSON
        JSONObject data = JSON.parse(response);

        // build Map
        return JSON.mapUserData(data);
    }

    /**
     *
     * Parses data from JSON that represents a User returned from a query.
     *
     * @param data Key/value pair representing a User's JSON Schema.
     * @return Map{String, String}
     *      <ul> Map:
     *          <li>"id":     user_id,</li>
     *          <li>"name":   of user,</li>
     *          <li>"gender": of user--male/female,</li>
     *          <li>"email":  of user--follows traditional email formatting,</li>
     *          <li>"status": of user--active/inactive</li>
     *      </ul>
     */
    public static Map<String,String> mapUserData(Object data) {

        JSONObject jData = (JSONObject) data;

        // build Map
        Map<String, String> returnData = new HashMap<>();
        returnData.put("id", String.valueOf(jData.getInt("id")));
        returnData.put("name", jData.getString("name"));
        returnData.put("gender", jData.getString("gender"));
        returnData.put("email", jData.getString("email"));
        returnData.put("status", jData.getString("status"));

        return returnData;
    }

    /**
     *
     * Parses data from JSON that represents a Post returned from a query. "Post" doesn't refer to a post-request, but rather the term used by users. "He posted to my feed".
     *
     * @param response Query response returning a Post's data
     * @return Map:
     *       <ul>
     *          <li>"id":    post_id,</li>
     *          <li>"title": title of post--given by user,</li>
     *          <li>"body":  body of the post--given by user</li>
     *       </ul>
     */
    public static Map<String, String> parsePostData(String response) {

        // parse response
        JSONObject json = new JSONObject(response);
        JSONObject data = json.getJSONObject("data");

        // build Map
        Map<String, String> returnData = new HashMap<>();
        returnData.put("id", String.valueOf(data.getInt("id")));
        returnData.put("title", data.getString("title"));
        returnData.put("body", data.getString("body"));

        return returnData;
    }

    public static Map<String, Map<String, String>> parsePostsData(String response) {

        // parse response
        JSONObject json = new JSONObject(response);

        // get property data which is an array
        JSONArray data = json.getJSONArray("data");

        // build MAP to be returned
        Map<String, Map<String, String>> result = new HashMap<>();
        for(Object post : data) {
            Map<String, String> mappedPost = mapPostData(post);
            result.put(mappedPost.get("id"), mappedPost);
        }

        // return MAP
        return result;
    }

    /**
     *
     * Parses data from JSON that represents a Post returned from a query. "Post" doesn't refer to a post-request, but rather the term used by users. "He posted to my feed".
     *
     * @param data Key/value pair representing a Post's JSON Schema.
     * @return Map:
     *       <ul>
     *          <li>"id":    post_id,</li>
     *          <li>"title": title of post--given by user,</li>
     *          <li>"body":  body of the post--given by user</li>
     *       </ul>
     */
    public static Map<String, String> mapPostData(Object data) {

        JSONObject jData = (JSONObject) data;

        // build Map
        Map<String, String> returnData = new HashMap<>();
        returnData.put("id", String.valueOf(jData.getInt("id")));
        returnData.put("title", jData.getString("title"));
        returnData.put("body", jData.getString("body"));

        return returnData;
    }

    /**
     *
     * Parses data from JSON that represents a Post's Comment returned from a query. "Post" doesn't refer to a post-request, but rather the term used by users. "He posted to my feed".
     *
     * @param response Query response returning a Post's Comment's data
     * @return Map:
     *      <ul>
     *          <li>"id":    comment's ID,</li>
     *          <li>"name":  comment's creator's name,</li>
     *          <li>"email": comment's creator's email,</li>
     *          <li>"body":  main text of the comment</li>
     *      </ul>
     */
    public static Map<String, Map<String, String>> parsePostsCommentData(String response) {

        // parse response
        JSONObject json = new JSONObject(response);

        // get property data which is an array
        JSONArray data = json.getJSONArray("data");

        // build MAP to be returned
        Map<String, Map<String, String>> result = new HashMap<>();
        for(Object comment : data) {
            JSONObject currentComment = new JSONObject(comment);
            Map<String, String> mappedComment = mapCommentData(comment);
            result.put(mappedComment.get("id"), mappedComment);
        }

        // return MAP
        return result;
    }

    public static Map<String, String> parseCommentData(String response) {
        // parse response
        JSONObject json = new JSONObject(response);
        JSONObject data = json.getJSONObject("data");

        return JSON.mapCommentData(data);
    }

    /**
     *
     * Parses data from JSON that represents a Post's Comment returned from a query. "Post" doesn't refer to a post-request, but rather the term used by users. "He posted to my feed".
     *
     * @param data Key/value pair representing a Post's Comment's JSON Schema.
     * @return Map:
     *      <ul>
     *          <li>"id":    comment's ID,</li>
     *          <li>"name":  comment's creator's name,</li>
     *          <li>"email": comment's creator's email,</li>
     *          <li>"body":  main text of the comment</li>
     *      </ul>
     */
    public static Map<String, String> mapCommentData(Object data) {

        JSONObject jData = (JSONObject) data;

        // build Map
        Map<String, String> returnData = new HashMap<>();
        returnData.put("id", String.valueOf(jData.getInt("id")));
        returnData.put("name", jData.getString("name"));
        returnData.put("email", jData.getString("email"));
        returnData.put("body", jData.getString("body"));

        return returnData;
    }

    /**
     *
     * Parses data from JSON that represents a Post's Comment returned from a query. "Post" doesn't refer to a post-request, but rather the term used by users. "He posted to my feed".
     *
     * @param response Query response returning a Post's Comment's data
     * @return Map:
     *      <ul>
     *          <li>"id":    comment's ID,</li>
     *          <li>"name":  comment's creator's name,</li>
     *          <li>"email": comment's creator's email,</li>
     *          <li>"body":  main text of the comment</li>
     *      </ul>
     */
    public static Map<String, String> parseTodoData(String response) {

        // parse response
        JSONObject json = new JSONObject(response);
        JSONObject data = json.getJSONObject("data");

        // build Map
        return JSON.mapTodoData(data);
    }

    public static Map<String, Map<String, String>> parseTodosData(String response) {

        // parse response
        JSONObject json = new JSONObject(response);

        // get property data which is an array
        JSONArray data = json.getJSONArray("data");

        // build MAP to be returned
        Map<String, Map<String, String>> result = new HashMap<>();
        for(Object todo : data) {
            Map<String, String> curTodo = mapCommentData(todo);
            result.put(curTodo.get("id"), curTodo);
        }

        // return MAP
        return result;
    }

    /**
     *
     * Parses data from JSON that represents a Post's Comment returned from a query. "Post" doesn't refer to a post-request, but rather the term used by users. "He posted to my feed".
     *
     * @param data Key/value pair representing a Post's Comment's JSON Schema.
     * @return Map:
     *      <ul>
     *          <li>"id":    comment's ID,</li>
     *          <li>"name":  comment's creator's name,</li>
     *          <li>"email": comment's creator's email,</li>
     *          <li>"body":  main text of the comment</li>
     *      </ul>
     */
    public static Map<String, String> mapTodoData(Object data) {

        JSONObject jData = (JSONObject) data;

        // build Map
        Map<String, String> returnData = new HashMap<>();
        returnData.put("id", String.valueOf(jData.getInt("id")));
        returnData.put("title", jData.getString("title"));
        returnData.put("status", jData.getString("status"));

        return returnData;
    }

}
