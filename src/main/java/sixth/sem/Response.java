package sixth.sem;

import java.io.Serializable;

/**
 * Response
 */
public class Response implements Serializable {
    public int responseCode;
    public String data;

    public Response(String s, int code) {
        this.responseCode = code;
        this.data = s;
    }

    public Response() {
    }
}
