package models;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class RequestMeta implements java.io.Serializable {

    private static final long serialVersionUID = 2652061019439385069L;

    private String address;
    private Timestamp timestamp;
    private Map<String, String[]> headers = new TreeMap<String, String[]>();
    private Map<String, String> parsedParams = new HashMap<String, String>();
    private Map<String, String[]> urlParams = new HashMap<String, String[]>();

    private String content;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, String[]> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String[]> headers) {
        this.headers = headers;
    }

    public Map<String, String> getParsedParams() {
        return parsedParams;
    }

    public void addHeader(String name, String[] header) {
        headers.put(name, header);
    }

    public void addParam(String name, String value) {
        parsedParams.put(name, value);
    }

    public Map<String, String[]> getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(Map<String, String[]> urlParams) {
        this.urlParams = urlParams;
    }
}
