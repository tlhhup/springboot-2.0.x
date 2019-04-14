package org.tlh.ws.endpoints;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.ws.test.server.MockWebServiceClient;
import org.springframework.xml.transform.StringSource;

import javax.xml.transform.Source;

import static org.springframework.ws.test.server.RequestCreators.withPayload;

/**
 * Created by 离歌笑tlh/hu ping on 2019/4/14
 * <p>
 * Github: https://github.com/tlhhup
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-ws-servlet.xml")
public class HolidayEndpointTest {

    @Autowired
    private ApplicationContext applicationContext;

    private MockWebServiceClient mockClient;

    @Before
    public void createClient() {
        mockClient = MockWebServiceClient.createClient(applicationContext);
    }

    @Test
    public void handleHolidayRequest() {
        Source requestPayload = new StringSource(
                "<HolidayRequest xmlns=\"http://mycompany.com/hr/schemas\">\n" +
                        "    <Holiday>\n" +
                        "        <StartDate>2006-07-03</StartDate>\n" +
                        "        <EndDate>2006-07-07</EndDate>\n" +
                        "    </Holiday>\n" +
                        "    <Employee>\n" +
                        "        <Number>42</Number>\n" +
                        "        <FirstName>Arjen</FirstName>\n" +
                        "        <LastName>Poutsma</LastName>\n" +
                        "    </Employee>\n" +
                        "</HolidayRequest>");

        mockClient.sendRequest(withPayload(requestPayload));
    }
}