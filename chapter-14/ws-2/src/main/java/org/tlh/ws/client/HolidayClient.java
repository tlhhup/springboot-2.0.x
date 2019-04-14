package org.tlh.ws.client;

import org.springframework.ws.client.core.WebServiceTemplate;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.InputStream;

/**
 * Created by 离歌笑tlh/hu ping on 2019/4/14
 * <p>
 * Github: https://github.com/tlhhup
 */
public class HolidayClient {

    private static final String DEFAULT_URL="http://localhost:8080/";

    private WebServiceTemplate webServiceTemplate;

    public HolidayClient() {
        this.webServiceTemplate=new WebServiceTemplate();
        //设置web service服务器的地址
        this.webServiceTemplate.setDefaultUri(DEFAULT_URL);
    }

    public void sendHolidayRequest(){
        try (InputStream is=this.getClass().getClassLoader().getResourceAsStream("HolidayRequest.xml")){
            Source requestPayload = new StreamSource(is);
            StreamResult result = new StreamResult(System.out);
            webServiceTemplate.sendSourceAndReceiveToResult(requestPayload, result);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
