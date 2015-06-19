package controller;

import jms.publisher.MessagePublisher;
import model.Report;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

@Component
public class ForecasterAgentWorker {

    /**
     * Get a copy of the application context
     */

    private MessagePublisher messagePublisher;

    @Autowired
    ConfigurableApplicationContext context;

    /**
     * When you receive a message, print it out, then shut down the application.
     * Finally, clean up any ActiveMQ server stuff.
     */
    @JmsListener(destination = "mailbox-destination", containerFactory = "myJmsContainerFactory")
    public void receiveMessage(String message) {

        System.out.println("\n The ForecasterAgentWorker recieve the request from 'mailbox-desitination' queue:" + message + "\n");

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/activemq-jms-spring-context.xml");
        messagePublisher = (MessagePublisher) applicationContext.getBean("messagePublisher");

        //String res = getReport(message).toString();
        String res = getJSONResponse(message, "GET");
        messagePublisher.sendMessage(res);
        //messagePublisher.sendMessage(getReport(message).toString());
        //context.close();
        //FileSystemUtils.deleteRecursively(new File("activemq-data"));
    }

    private String getJSONResponse(String requestURL, String method) {

        try {
            URL url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod(method);
            conn.setRequestProperty("Accept", "application/json");

            BufferedReader br = new BufferedReader(new InputStreamReader(
                    (conn.getInputStream())));

            String temp;
            String output = "";

            while ((temp = br.readLine()) != null) {
                output += temp;
            }

            return output;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public Report getReport(String req) {

        String source = getJSONResponse(req, "GET");
        Gson gson = new Gson();
        JSONObject jsonRoot = new JSONObject(source);
        Report report = gson.fromJson(jsonRoot.getJSONObject("report").toString(), Report.class);
        return report;
    }
}
