package com.example.hzy.lovenum;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.sun.jersey.api.client.Client;

import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.core.MediaType;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.hzy.lovenum", appContext.getPackageName());
    }
    @Test
    public void testREST() throws Exception {
        // Context of the app under test.
        System.out.println("开始测试");
           String url = "http://172.28.234.9:8080/arest/services/test/aaa?params=1";

        Client client=Client.create();
        String strXML1="<customers> <customer> <id>" ;
        String strXML2="</id> <firstname>jess</firstname> </customer> <customer> <id>" ;
        String strXML3="</id> </customer> </customers>"	;
        String strXML=strXML1+"3"+strXML2+"6"+strXML3;
        System.out.println("strxml的值为：*****"+strXML);
        String responce=Client.create().resource(url).type(MediaType.APPLICATION_XML).post(String.class, strXML);
        System.out.println("client返回值"+responce);

        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.hzy.lovenum", appContext.getPackageName());
    }
}
