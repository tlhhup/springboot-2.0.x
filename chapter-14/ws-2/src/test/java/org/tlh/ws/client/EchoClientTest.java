package org.tlh.ws.client;

import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;

/**
 * Created by 离歌笑tlh/hu ping on 2019/4/14
 * <p>
 * Github: https://github.com/tlhhup
 */
public class EchoClientTest {

    @Test
    public void echo() throws IOException {
        EchoClient echoClient = new EchoClient();
        echoClient.setDefaultUri("http://localhost:8080/services");
        echoClient.setRequest(new ClassPathResource("echoRequest.xml"));

        echoClient.echo();
    }
}