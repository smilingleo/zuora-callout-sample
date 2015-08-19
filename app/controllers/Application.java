package controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import models.RequestMeta;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http.RequestBody;
import play.mvc.Result;

public class Application extends Controller {
    private static final int AMOUNT = 10;
    private RequestMeta[] cache = new RequestMeta[AMOUNT];

    private ReentrantLock lock = new ReentrantLock();

    public Result index() {
        return ok("Welcome to Zuora callout demo, view /view page please.");
    }

    @BodyParser.Of(BodyParser.TolerantText.class)
    public Result callout() throws ParserConfigurationException, SAXException, IOException {
        Document doc = null;
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();

        RequestBody body = request().body();
        String xml = body.asText();
        doc = docBuilder.parse(new InputSource(new ByteArrayInputStream(xml.getBytes("utf-8"))));

        // you might want to return '40x' instead, but keep in mind Zuora will
        // retry any failed callout if you enabled callout-retry
        if (body == null || doc == null)
            return ok("empty");

        Map<String, String[]> headers = request().headers();

        lock.lock();
        System.arraycopy(cache, 0, cache, 1, AMOUNT - 1); // shift array to right
        lock.unlock();

        cache[0] = new RequestMeta();
        cache[0].setContent(xml);
        cache[0].setUrlParams(request().queryString());
        cache[0].setAddress(request().remoteAddress());
        cache[0].setTimestamp(new Timestamp(System.currentTimeMillis()));
        if (headers != null)
            cache[0].setHeaders(headers);

        doc.getDocumentElement().normalize();

        // parse the raw xml content
        NodeList paramList = doc.getElementsByTagName("parameter");
        for (int i = 0; i < paramList.getLength(); i++) {
            Node node = paramList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element elem = (Element) node;
                String name = elem.getAttribute("name");
                String value = elem.getTextContent();
                cache[0].addParam(name, value);
            }
        }

        // only 200 (OK) is considered as a succeeded callout, not '201' or '202'.
        return ok("Got request " + request() + "!");
    }

    public Result view() {
        return ok(views.html.index.render(cache));
    }

}
