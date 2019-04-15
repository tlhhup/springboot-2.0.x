package org.tlh.ws.client;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.xml.transform.ResourceSource;
import org.springframework.xml.transform.StringResult;

import javax.xml.transform.Source;
import java.io.IOException;

/**
 * Created by 离歌笑tlh/hu ping on 2019/4/15
 * <p>
 * Github: https://github.com/tlhhup
 */
@Component
public class EchoClient  extends WebServiceGatewaySupport {

    private Resource request;

    public void setRequest(Resource request) {
        this.request = request;
    }

    public void echo() throws IOException {
        Source requestSource = new ResourceSource(request);
        StringResult result = new StringResult();
        getWebServiceTemplate().sendSourceAndReceiveToResult(requestSource, result);
        System.out.println();
        System.out.println(result);
        System.out.println();
    }

}
