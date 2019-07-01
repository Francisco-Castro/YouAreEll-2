package views;

import controllers.*;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;

public class YouAreEll {

    private MessageController msgCtrl;
    private IdController idCtrl;

    public YouAreEll (MessageController m, IdController j) {
        // used j because i seems awkward
        this.msgCtrl = m;
        this.idCtrl = j;
    }

    public static void main(String[] args) throws IOException {
        // hmm: is this Dependency Injection?
        YouAreEll urlhandler = new YouAreEll(new MessageController(), new IdController());
        System.out.println(urlhandler.MakeURLCall("/ids", "GET", ""));
        System.out.println(urlhandler.MakeURLCall("/messages", "GET", ""));
    }

    public String get_ids() throws IOException {
        return MakeURLCall("/ids", "GET", "");
    }

    public String post_ids(String data) throws IOException {

//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("name", name);
//            jsonObject.put("github", github);
//
//        } catch (org.json.JSONException e) {
//            e.printStackTrace();
//        }

//        return null;
        return MakeURLCall("/ids", "POST", data );
    }

    public String get_messages() throws IOException {
        return MakeURLCall("/messages", "GET", "");
    }

    public String MakeURLCall(String mainurl, String method, String jpayload) throws IOException {

        OkHttpClient client = new OkHttpClient();

        if (method.equals("GET")) {
            Request request = new Request.Builder()
                    .url("http://zipcode.rocks:8085" + mainurl)
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();

                if (!response.isSuccessful()) throw new IOException("Unexpected code in GET " + response);
                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        else if (method.equals("POST")) {

            String[] dataSplitted= jpayload.split(" ");

            // put your json here
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("userid", "userid1");
                jsonObject.put("name", dataSplitted[0]);
                jsonObject.put("github", dataSplitted[1]);

            } catch (org.json.JSONException e) {
                e.printStackTrace();
            }

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");

            RequestBody body = RequestBody.create(JSON, jsonObject.toString());

            Request request = new Request.Builder()
                    .url("http://zipcode.rocks:8085" + mainurl)
                    .post(body)
                    .build();

            try {
                Response response = client.newCall(request).execute();

                if (!response.isSuccessful()) throw new IOException("Unexpected code in POST " + response);
                return response.body().string();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
