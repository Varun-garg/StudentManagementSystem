package StudentManagementSystem.Model;

/**
 * Created by rishabh on 12/1/2016.
 */

import com.fasterxml.jackson.annotation.*;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({
        "message",
        "semesters"
})
public class ExamSemesterNum {

    @JsonProperty("message")
    private String message;
    @JsonProperty("semesters")
    private List<Integer> semesters = new ArrayList<Integer>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * @return The message
     */
    @JsonProperty("message")
    public String getMessage() {
        return message;
    }

    /**
     * @param message The message
     */
    @JsonProperty("message")
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return The semesters
     */
    @JsonProperty("semesters")
    public List<Integer> getSemesters() {
        return semesters;
    }

    /**
     * @param semesters The semesters
     */
    @JsonProperty("semesters")
    public void setSemesters(List<Integer> semesters) {
        this.semesters = semesters;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}