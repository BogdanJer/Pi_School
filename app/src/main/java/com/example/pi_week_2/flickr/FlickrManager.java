package com.example.pi_week_2.flickr;

import android.net.Uri;

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
import java.util.List;

public class FlickrManager {
    private static final String FLICKR_SEARCH_PHOTO = "flickr.photos.search";
    private static final String API_KEY_QUERY = "api_key";
    private static final String API_KEY = "ad0fe81ffeee38fab599748d37a3ee50";
    private static final String PER_PAGE_QUERY = "per_page";
    private static final String PAGE_QUERY = "page";
    private static final String NO_JSON_CALLBACK_QUERY = "nojsoncallback";
    private static final String FORMAT_QUERY = "format";
    private static final String TAGS_QUERY = "tags";
    private static final String SCHEME = "https";
    private static final String FORMAT = "json";
    private static final String DOMAIN = "api.flickr.com";
    private static final String PATH_1 = "services";
    private static final String PATH_2 = "rest";
    private static final String METHOD_QUERY = "method";
    private static FlickrManager fm = new FlickrManager();
    private int perPage = 4000;
    private int page = 0;

    public static FlickrManager getInstance() {
        return fm;
    }

    public List<String> searchPhotos(String tags) {
        URL url;
        StringBuilder json = new StringBuilder();

        try {
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(SCHEME)
                    .authority(DOMAIN)
                    .appendPath(PATH_1)
                    .appendPath(PATH_2)
                    .appendQueryParameter(METHOD_QUERY, FLICKR_SEARCH_PHOTO)
                    .appendQueryParameter(API_KEY_QUERY, API_KEY)
                    .appendQueryParameter(PER_PAGE_QUERY, String.valueOf(perPage))
                    .appendQueryParameter(PAGE_QUERY, String.valueOf(page))
                    .appendQueryParameter(NO_JSON_CALLBACK_QUERY, String.valueOf(1))
                    .appendQueryParameter(FORMAT_QUERY, FORMAT)
                    .appendQueryParameter(TAGS_QUERY, tags);

            url = new URL(builder.build().toString());

            HttpURLConnection hul = (HttpURLConnection) url.openConnection();
            hul.setRequestMethod("GET");
            hul.setReadTimeout(5000);
            hul.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(hul.getInputStream()));


            String line;

            while ((line = reader.readLine()) != null)
                json.append(line);

            hul.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return makeLinks(getPhotosLinks(json.toString()));
    }

    private ArrayList<PhotoFlickr> getPhotosLinks(String json) {
        ArrayList<PhotoFlickr> list = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject jsonPhotos = jsonObject.getJSONObject("photos");
            JSONArray arrayPhoto = jsonPhotos.getJSONArray("photo");
            for (int i = 0; i < arrayPhoto.length(); i++) {
                JSONObject obj = arrayPhoto.getJSONObject(i);
                list.add(new PhotoFlickr(obj.getString("id"), obj.getString("owner"), obj.getString("secret"),
                        obj.getString("server"), obj.getString("farm"), obj.getString("title"), obj.getInt("ispublic") == 1,
                        obj.getInt("isfriend") == 1, obj.getInt("isfamily") == 1));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<String> makeLinks(ArrayList<PhotoFlickr> list) {
        List<String> linksList = new ArrayList<>();

        for (PhotoFlickr pf : list)
            linksList.add(String.format("https://farm%s.staticflickr.com/%s/%s_%s.png", pf.getFarm(), pf.getServer(), pf.getId(), pf.getSecret()));

        return linksList;
    }
    public void setPerPage(int perPage) {
        this.perPage = perPage;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
