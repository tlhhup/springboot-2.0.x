package org.tlh.ws.endpoints;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.tlh.ws.service.EchoService;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by 离歌笑tlh/hu ping on 2019/4/14
 * <p>
 * Github: https://github.com/tlhhup
 */
@Endpoint
public class EchoEndpoint {

    /**
     * Namespace of both request and response.
     */
    public static final String NAMESPACE_URI = "http://www.springframework.org/spring-ws/samples/echo";

    /**
     * The local name of the expected request.
     */
    public static final String ECHO_REQUEST_LOCAL_NAME = "echoRequest";

    /**
     * The local name of the created response.
     */
    public static final String ECHO_RESPONSE_LOCAL_NAME = "echoResponse";

    private final DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

    private final EchoService echoService;

    @Autowired
    public EchoEndpoint(EchoService echoService) {
        this.echoService = echoService;
    }


    /**
     * Reads the given <code>requestElement</code>, and sends a the response back.
     *
     * @param requestElement the contents of the SOAP message as DOM elements
     * @return the response element
     */
    @PayloadRoot(localPart = ECHO_REQUEST_LOCAL_NAME, namespace = NAMESPACE_URI)
    @ResponsePayload
    public Element handleEchoRequest(@RequestPayload Element requestElement) throws ParserConfigurationException {
        Assert.isTrue(NAMESPACE_URI.equals(requestElement.getNamespaceURI()), "Invalid namespace");
        Assert.isTrue(ECHO_REQUEST_LOCAL_NAME.equals(requestElement.getLocalName()), "Invalid local name");

        NodeList children = requestElement.getChildNodes();
        Text requestText = null;
        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeType() == Node.TEXT_NODE) {
                requestText = (Text) children.item(i);
                break;
            }
        }
        if (requestText == null) {
            throw new IllegalArgumentException("Could not find request text node");
        }

        String echo = echoService.echo(requestText.getNodeValue());

        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.newDocument();
        Element responseElement = document.createElementNS(NAMESPACE_URI, ECHO_RESPONSE_LOCAL_NAME);
        Text responseText = document.createTextNode(echo);
        responseElement.appendChild(responseText);
        return responseElement;
    }


}
