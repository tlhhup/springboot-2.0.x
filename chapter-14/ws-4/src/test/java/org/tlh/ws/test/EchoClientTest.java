package org.tlh.ws.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.tlh.ws.WsClientApplication;
import org.tlh.ws.client.EchoClient;

import java.io.IOException;

/**
 * Created by 离歌笑tlh/hu ping on 2019/4/15
 * <p>
 * Github: https://github.com/tlhhup
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = WsClientApplication.class)
public class EchoClientTest {

    @Autowired
    private EchoClient echoClient;

    @Test
    public void echo() throws IOException {
        echoClient.setDefaultUri("http://localhost:8080/services");
        echoClient.setRequest(new ClassPathResource("echoRequest.xml"));

        echoClient.echo();
    }

}
