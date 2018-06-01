package pro200.smile.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;

import com.couchbase.lite.Array;
import com.couchbase.lite.Blob;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Document;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Meta;
import com.couchbase.lite.MetaExpression;
import com.couchbase.lite.MutableArray;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import pro200.smile.model.PhotoSmile;
import pro200.smile.model.Smile;
import pro200.smile.model.SmileList;
import pro200.smile.model.VideoSmile;

public class LiveSmileService implements SmileService {

    private String smileDB = "SmileDB";

    private Context context;
    private Database database;

    private SmileList recents = new SmileList();

    public LiveSmileService(Context context) {
        this.context = context;
        DatabaseConfiguration config = new DatabaseConfiguration(context);
        database = null;
        try {
            database = new Database(smileDB, config);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public SmileList GetUserSmiles(String id) {
        MutableDocument userDoc = database.getDocument(id).toMutable();

        Array arr = userDoc.getArray("smiles");

        SmileList sl = new SmileList();

        for (Object o: arr) {
            String s = (String)o;
            MutableDocument smileDoc = database.getDocument(s).toMutable();
            Blob b = smileDoc.getBlob("data");
            if (b.getContentType().equals("image")) {
                byte[] bytes = b.getContent();
                sl.addSmile(new PhotoSmile(smileDoc.getDate("postedDate"), BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));
            }
            else if (b.getContentType().equals("video")){
                byte[] bytes = b.getContent();
//                String uriString = null;
//                try {
//                    uriString = new String(bytes, "UTF-8");
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                Uri uri = Uri.parse(uriString);
                InputStream is = new ByteArrayInputStream(bytes);
                Uri uri = Uri.
                sl.addSmile(new VideoSmile(smileDoc.getDate("postedDate"), uri));
            }
        }

        return sl;
    }

    @Override
    public SmileList GetRandomSmiles(int count) {
        if (recents.getSmiles().isEmpty()) {
            Query q = QueryBuilder.select(SelectResult.expression(Meta.id))
                    .from(DataSource.database(database))
                    .where(Expression.property("type").equalTo(Expression.string("smile"))
                            .and(Expression.property("sharing").equalTo(Expression.string("public"))));
            ResultSet results = null;
            try {
                results = q.execute();
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
            for (Result i : results.allResults()) {
                MutableDocument doc = database.getDocument(i.getString("id")).toMutable();
                Date d = doc.getDate("postedDate");
                Date yesterday = new Date(System.currentTimeMillis() - (60 * 60 * 24 * 1000));
                if (d.after(yesterday)){
//                    byte[] bytes = doc.getBlob("data").getContent();
//                    recents.addSmile(new PhotoSmile(doc.getDate("postedDate"), BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));
                    Blob b = doc.getBlob("data");
                    if (b.getContentType().equals("image")) {
                        byte[] bytes = b.getContent();
                        recents.addSmile(new PhotoSmile(doc.getDate("postedDate"), BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));
                    }
                    else if (b.getContentType().equals("video")){
                        byte[] bytes = b.getContent();
                        String uriString = null;
                        try {
                            uriString = new String(bytes, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        Uri uri = Uri.parse(uriString);
                        recents.addSmile(new VideoSmile(doc.getDate("postedDate"), uri));
                    }
                }
            }
        }
        SmileList toReturn = new SmileList();
        if (count >= recents.getSmiles().size()){
            return recents;
        }
        Random r = new Random();
        if (!recents.getSmiles().isEmpty()) {
            int start = r.nextInt(recents.getSmiles().size() - 1);
            int i = 0;
            while (toReturn.getSmiles().size() < count) {
                if (start + count < recents.getSmiles().size()) {
                    toReturn.addSmile(recents.getSmiles().get(start + i));
                    i++;
                } else {
                    start = 0;
                    i = 0;
                }
            }
        }
        return toReturn;
    }

    @Override
    public void LoginOrCreate(String id) {
        if (database.getDocument(id) == null) {
            MutableDocument mutableDoc = new MutableDocument(id)
                    .setString("type", "user")
                    .setArray("smiles", new MutableArray())
                    .setString("sharing", "public");

            try {
                database.save(mutableDoc);
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void AddSmile(String id, Bitmap smile, android.net.Uri videoFile) {
        Blob b = null;
        if (videoFile == null) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            smile.compress(Bitmap.CompressFormat.PNG, 0, bos);
            byte[] bitmapdata = bos.toByteArray();
            ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
            b = new Blob("image", bs);
        }
        else if (smile == null){
            Log.w("BLOB", "WE IN THIS");
            try {
//                String path = videoFile.getPath();
//                URL url = new URL(path);
                b = new Blob("video", context.getContentResolver().openInputStream(videoFile));
            } catch (IOException e) {
                Log.w("BLOB", "CAUGHT UP");
                e.printStackTrace();
            }
        }
        MutableDocument smileDoc = new MutableDocument()
                .setString("type", "smile")
                .setBlob("data", b)
                .setDate("postedDate", new Date())
                .setString("sharing", database.getDocument(id).toMutable().getString("sharing"));

        try {
            database.save(smileDoc);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        MutableDocument userDoc = database.getDocument(id).toMutable();
        userDoc.getArray("smiles").addString(smileDoc.getId());
        try {
            database.save(userDoc);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
//        recents.addSmile(new Smile(smileDoc.getDate("postedDate"), smile));
        if (b.getContentType().equals("image")) {
            recents.addSmile(new PhotoSmile(smileDoc.getDate("postedDate"), smile));
        }
        else if (b.getContentType().equals("video")){
            recents.addSmile((new VideoSmile(smileDoc.getDate("postedDate"), videoFile)));
        }
    }

    @Override
    public void changePreference(String id, String newPreference) {
        MutableDocument userDoc = database.getDocument(id).toMutable();
        userDoc.setString("sharing", newPreference);
        try {
            database.save(userDoc);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }
}
