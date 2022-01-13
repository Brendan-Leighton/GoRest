package models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

@JsonInclude(JsonInclude.Include.NON_DEFAULT)
public abstract class Request {


    private int id;
    private static Class<?> RESPONSE_TYPE;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public <T extends Response<?>> void assertEquals(T responseType) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        JSONAssert.assertEquals(
                mapper.writeValueAsString(this),
                mapper.writeValueAsString(responseType.getData()),
                JSONCompareMode.LENIENT
        );
    }

    public void init(Class<?> resType){
        RESPONSE_TYPE = resType;
    }

    public Class<?> getResponseType() {
        return RESPONSE_TYPE;
    }

    public abstract String getJsonSchema();
}
