import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.DataFormatException;


public class Decrypter {


    private static final String LEVEL_ID = "2342";
    private static final String AUTHOR_ID = "2024";
    private static final String ACTION = "get_record";
    private static final String SITE_URL = "http://www.totaljerkface.com/get_level.hw";

    private static final String IV = "abcd1234";
    private static final byte[] KEY = {101, 97, 116, 115, 104, 105, 116};
    private static final String FULLKEY = new String(KEY) + AUTHOR_ID;


    public static void main(String[] args) throws Exception {

        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(SITE_URL);

        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("action", ACTION));
        nvps.add(new BasicNameValuePair("level_id", LEVEL_ID));
        nvps.add(new BasicNameValuePair("ip_tracking", "22222"));

        httpPost.setEntity(new UrlEncodedFormEntity(nvps));
        CloseableHttpResponse response = httpclient.execute(httpPost);
        HttpEntity entity = response.getEntity();

        byte [] responseByteArray = EntityUtils.toByteArray(entity);
        decryptAndUncompress(responseByteArray);

        EntityUtils.consume(entity);
        response.close();
        httpclient.close();
    }


    private static void decryptAndUncompress(byte [] responseBytes) throws IOException, DataFormatException {
        byte [] decryptedBytes = decryptByteArray(responseBytes, FULLKEY);
        String uncompressedXML = CompressionUtils.uncompress(decryptedBytes);
        System.out.println(uncompressedXML);
    }


    private static byte[] decryptByteArray(byte[] byteArray, String k) {
        PKCS5 pad = new PKCS5(0);
        CBCMode cipher = getCipher( k, pad );
        pad.setBlockSize(cipher.getBlockSize());
        return cipher.decrypt(byteArray);
    }


    private static CBCMode getCipher(String key, PKCS5 pad) {

        byte[] kBA = key.getBytes(StandardCharsets.UTF_8);
        CBCMode mode = new CBCMode(new BlowFishKey(kBA), pad);
        mode.setIV(IV.getBytes(StandardCharsets.UTF_8));

        return mode;
    }
}