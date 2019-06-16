package ir.snappfood.keplertracker;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Networking {
    public final static String API_ANALYTICS = "https://log.ikepler.ir/write?db=snappfood_applog";
    public final static String API_IP = "https://ikepler.ir/ip/";
    public static final int POST = 203;
    public static final int GET = 202;

    public static class Response<T> {
        public Response(boolean status, T response) {
            this.status = status;
            this.response = response;
        }

        private boolean status;
        private T response;

        public boolean isStatus() {
            return status;
        }

        public void setStatus(boolean status) {
            this.status = status;
        }

        public T getResponse() {
            return response;
        }

        public void setResponse(T response) {
            this.response = response;
        }
    }

    public static Response<String> createHttpsURLConnection(String urlString, String data, int method) throws Exception {
        DataOutputStream wr = null;

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            switch (method) {
                case POST:
                    connection.setRequestMethod("POST");
                    wr = new DataOutputStream(connection.getOutputStream());
                    wr.writeBytes(data);
                    break;
                case GET:
                    connection.setRequestMethod("GET");
                    break;
            }
            return readHttpResponse(connection);

        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (wr != null) {
                    wr.flush();
                    wr.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static Response<String> readHttpResponse(HttpURLConnection connection) {
        StringBuffer sb = new StringBuffer();
        Integer responseCode = null;

        try {
            connection.connect();

            responseCode = connection.getResponseCode();
            InputStream inputStream;

            if (responseCode >= 400) {
                inputStream = connection.getErrorStream();
            } else {
                inputStream = connection.getInputStream();
            }

            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;

            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            SAnalytics.log(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return new Response<>(responseCode != null && responseCode < 400, sb.toString());
    }

}
