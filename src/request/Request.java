package request;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;

public class Request {
    private String url;
    private String endpoint;

    private Map<String, String> parameters;

    public Request(InputStream client) throws Exception{
        parameters = new HashMap<>();
        BufferedReader br = new BufferedReader(new InputStreamReader(client));
        try {
            // read the first line to get the request method, URI and HTTP version
            url = br.readLine();
            parseParameter(url);
        } catch (Exception e) {
            System.out.println("Error create request");
        }
    }

    private void parseParameter(String url) throws Exception{
        int endpointIndex = url.indexOf("?");
        if (endpointIndex < 0) {
            this.endpoint = url;
        } else {
            this.endpoint = url.substring(1, endpointIndex);
            String temp = url.substring(endpointIndex + 1, url.length());
            String[] pairs = temp.split("&");
            for (String pair : pairs) {
                String[] tempAry = pair.split("=");
                parameters.put(tempAry[0], tempAry[1]);
            }
        }
    }


    public String getUrl() {
        return url;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
