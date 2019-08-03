package com.example.pi_week_2.Flickr;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;

public class FlickrManager {
    private static FlickrManager fm;
    private final String FLICKR_SEARCH_PHOTO = "method=flickr.photos.search";
    private static final String API_KEY = "ad0fe81ffeee38fab599748d37a3ee50";
    private static final String SECRET = "16336b5dae0b117d";
    private static final String URL_START = "https://api.flickr.com/services/rest/?";
    private static final String API_KEY_QUERY = "&api_key=";
    private static final String PER_PAGE_QUERY = "&per_page=";
    private static final String PAGE_QUERY = "&page=";
    private static final String NO_JSON_CALLBACK_QUERY = "&nojsoncallback=1";
    private static final String FORMAT_QUERY = "&format=";
    private static final String TAGS_QUERY = "&tags=";
    private static final String FORMAT = "json";
    private int perPage = 4000;
    private int page = 0;


    public static FlickrManager getInstance() {
        if (fm == null) {
            fm = new FlickrManager();
        }
        return fm;
    }

    public String searchPhotos(String tags) {
        URL url;
        StringBuilder json = new StringBuilder();
        try {
            url = new URL(URL_START + FLICKR_SEARCH_PHOTO + API_KEY_QUERY + API_KEY + PER_PAGE_QUERY + perPage +
                    PAGE_QUERY + page + NO_JSON_CALLBACK_QUERY + FORMAT_QUERY + FORMAT + TAGS_QUERY + tags);
            HttpURLConnection hul = (HttpURLConnection) url.openConnection();
            hul.setRequestMethod("GET");
            hul.setReadTimeout(5000);
            hul.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(hul.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null)
                json.append(line);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return makeLinks(getPhotos(json.toString()));
    }

    private ArrayList<PhotoFlickr> getPhotos(String json) {
        ArrayList<PhotoFlickr> list = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonPhotos = jsonObject.getJSONObject("photos");
            JSONArray arrayPhoto = jsonPhotos.getJSONArray("photo");
            for (int i = 0; i < arrayPhoto.length(); i++) {
                JSONObject obj = arrayPhoto.getJSONObject(i);
                list.add(new PhotoFlickr(obj.getString("id"), obj.getString("owner"), obj.getString("secret"), obj.getString("server"), obj.getString("farm"), obj.getString("title"), obj.getInt("ispublic") == 1, obj.getInt("isfriend") == 1, obj.getInt("isfamily") == 1));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private String makeLinks(ArrayList<PhotoFlickr> list) {
        StringBuilder links = new StringBuilder();

        for (PhotoFlickr pf : list)
            links.append("https://farm" + pf.getFarm() + ".staticflickr.com/" + pf.getServer() + "/" + pf.getId() + "_" + pf.getSecret() + ".png\n\n");

        return links.toString();
    }

    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
