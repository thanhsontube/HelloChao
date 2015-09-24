package son.nt.hellochao.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import son.nt.hellochao.ResourceManager;
import son.nt.hellochao.base.MySSLSocketFactory;
import son.nt.hellochao.dto.DailySpeakDto;
import son.nt.hellochao.otto.GoDownload;
import son.nt.hellochao.utils.Logger;
import son.nt.hellochao.utils.OttoBus;


/**
 * Created by Sonnt on 8/22/15.
 */
public class DownloadIntentService extends IntentService {
    public static final String TAG = "DownloadIntentService";
    public DownloadIntentService() {
        super("DownloadIntentService");
    }

    public DownloadIntentService(String name) {
        super(name);
    }

    int count = 0;

    public static Intent getIntent(Context context) {
        return new Intent(context, DownloadIntentService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        for (DailySpeakDto p : ResourceManager.getInstance().getDailySpeakDtoList()) {
            try {
                count++;
                OttoBus.post(new GoDownload(count, p.getSentenceEng(), "", "Download successful"));
//                downloadLink(p.getLinkMp3());
//                download2(p.getLinkMp3());
                getStream(p.getLinkMp3());

            }catch (Exception e) {
                e.printStackTrace();
            }

        }
        OttoBus.post(new GoDownload(-1, "Now you can hear offline", "", "Download successful"));
    }

    public void download2 (String linkSpeak) {
        Logger.debug(TAG, ">>>" + "Download2:" +  ">" + count + ">count:" + linkSpeak);

        if (TextUtils.isEmpty(linkSpeak)) {
            return;
        }
        File fileTo = new File(ResourceManager.getInstance().getPathAudio(linkSpeak));
        if (fileTo.exists()) {
            Logger.debug(TAG, ">>>" + "File downloaded before");
            return;
        }

        try {
            URL url = new URL(linkSpeak);
            trustEveryone();
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.setDoOutput(true);
            c.connect();

            String PATH = ResourceManager.getInstance().folderAudio + File.separator;
            File file = new File(PATH);
            if(!file.exists()) {
                file.mkdirs();
            }
            File outputFile = new File(ResourceManager.getInstance().getPathAudio(linkSpeak));
            FileOutputStream fos = new FileOutputStream(outputFile);
            InputStream is = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len1);
            }
            fos.flush();
            fos.close();
            is.close();
        } catch (Exception e) {
            Logger.error(TAG, ">>>" + "downloadLink:" + e);
        }

    }

    public void downloadLink(String linkSpeak) {
        trustEveryone();
        Logger.debug(TAG, ">>>" + "Download:" +  ">" + count + ">count:" + linkSpeak);

        if (TextUtils.isEmpty(linkSpeak)) {
            return;
        }
        File fileTo = new File(ResourceManager.getInstance().getPathAudio(linkSpeak));
        if (fileTo.exists()) {
            Logger.debug(TAG, ">>>" + "File downloaded before");
            return;
        }

        try {
            HttpClient httpClient = getNewHttpClient();
            HttpResponse response = httpClient.execute(new HttpGet(linkSpeak));
            StatusLine status = response.getStatusLine();
            if (status.getStatusCode() == HttpStatus.SC_OK) {

                InputStream in = response.getEntity().getContent();
                String path = ResourceManager.getInstance().folderAudio + File.separator;
                File f = new File((path));
                if (!f.exists()) {
                    f.mkdirs();
                }
                File fileOut = new File(path, "down_temp");

                OutputStream out = new FileOutputStream(fileOut);
                byte[] buffer = new byte[512];
                int read;
                while ((read = in.read(buffer)) != -1) {
                    out.write(buffer, 0, read);
                }
                out.flush();
                out.close();
                in.close();

                if (fileOut.renameTo(fileTo)) {
                    Logger.debug(TAG, ">>>" + "Download success");
                }

            }
        } catch (Exception e) {
            Logger.error(TAG, ">>>" + "downloadLink:" + e);
        }
    }

    public HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    private void trustEveryone() {
        try {
            HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, new X509TrustManager[]{new X509TrustManager(){
                public void checkClientTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] chain,
                                               String authType) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }}}, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(
                    context.getSocketFactory());
        } catch (Exception e) { // should never happen
            e.printStackTrace();
        }
    }

    private void getStream (String link) {
        Logger.debug(TAG, ">>>" + "getStream:" + link);
        InputStream inputStream;
        try {
            int response;
            URL url = new URL(link);
            HttpURLConnection mConnection = (HttpURLConnection) url.openConnection();
            mConnection.setInstanceFollowRedirects(true);
            response = mConnection.getResponseCode();
            String method = mConnection.getRequestMethod();


            String location = mConnection.getHeaderField("Location");

            String protocol = url.getProtocol();

            Logger.debug(TAG, ">>>" + "response:" + response + ";method:" + method + ";location:" + location + ";protocol:" + protocol);


            inputStream = new BufferedInputStream(mConnection.getInputStream());
            File fileOut = new File(ResourceManager.getInstance().folderAlarm, "u_" + System.currentTimeMillis() + ".mp3");

            OutputStream out = new FileOutputStream(fileOut);
            byte[] buffer = new byte[512];
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
            out.flush();
            out.close();
            inputStream.close();

        } catch (Exception e) {
            Logger.error(TAG, ">>>" + "Err:" + e.toString());
        }
    }



}
