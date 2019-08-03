package com.example.pi_week_2.Flickr;

import androidx.annotation.Nullable;

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
    private final String API_KEY;
    private String SECRET;
    private int PER_PAGE = 4000;
    private int PAGE = 0;

    public FlickrManager(String api_key) {
        API_KEY = api_key;
    }

    public static FlickrManager getInstance(String api_key) {
        if (fm == null) {
            fm = new FlickrManager(api_key);
        }
        return fm;
    }

    public String searchPhotos(String tags) {
        URL url;
        StringBuilder json = new StringBuilder();
        try {
            url = new URL("https://api.flickr.com/services/rest/?" + FLICKR_SEARCH_PHOTO + "&api_key=" + API_KEY + "&per_page=" + PER_PAGE +
                    "&page=" + PAGE + "&nojsoncallback=1&format=json&tags=" + tags);
            System.out.println(url);
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
                System.out.println(obj.toString());
                list.add(new PhotoFlickr(obj.getString("id"), obj.getString("owner"), obj.getString("secret"), obj.getString("server"), obj.getString("farm"), obj.getString("title"), obj.getInt("ispublic") == 1 ? true : false, obj.getInt("isfriend") == 1 ? true : false, obj.getInt("isfamily") == 1 ? true : false));
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

    public String getAPI_KEY() {
        return API_KEY;
    }

    @Nullable
    public String getSECRET() {
        return SECRET;
    }

    public void setSECRET(String SECRET) {
        this.SECRET = SECRET;
    }
}
