package com.blkchainsolutions.common;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

@Slf4j
public class FabricHttpUtil {

    /**
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String httpGet(String url) throws IOException {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpPost = new HttpGet(url);
        ResponseHandler< String > responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } if(status==500){
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            }
            else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
        String responseBody = httpclient.execute(httpPost, responseHandler);

        return  responseBody;
    }

    /**
     *
     * @param requestJson
     * @param url
     * @return
     * @throws IOException
     */
    public static String httpPOST(String requestJson,String url) throws IOException {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        log.info("here!!");
        StringEntity stringEntity = new StringEntity(requestJson, ContentType.APPLICATION_JSON);
        httpPost.setEntity(stringEntity);
        ResponseHandler < String > responseHandler = response -> {
            int status = response.getStatusLine().getStatusCode();

            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } if(status==500){
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            }
            else {
                throw new ClientProtocolException("Unexpected response status: " + status);
            }
        };
        String responseBody = httpclient.execute(httpPost, responseHandler);
        return responseBody;
    }

}
