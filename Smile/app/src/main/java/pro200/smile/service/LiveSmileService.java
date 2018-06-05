package pro200.smile.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;

import com.couchbase.lite.Array;
import com.couchbase.lite.Blob;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Expression;
import com.couchbase.lite.Meta;
import com.couchbase.lite.MutableArray;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.Result;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.Random;

import pro200.smile.model.Smile;
import pro200.smile.model.SmileList;

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
    public SmileList getUserSmiles(String id) {
        MutableDocument userDoc = database.getDocument(id).toMutable();

        Array arr = userDoc.getArray("smiles");

        SmileList sl = new SmileList();

        for (Object o: arr) {
            String s = (String)o;
            MutableDocument smileDoc = database.getDocument(s).toMutable();
            Blob b = smileDoc.getBlob("data");
            byte[] bytes = b.getContent();
            sl.addSmile(new Smile(smileDoc.getDate("postedDate"), BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));

        }

        return sl;
    }

    Random r = new Random();
    private int start = r.nextInt(recents.getSmiles().size() - 1);
    @Override
    public SmileList getRandomSmiles(int count) {
        getRecents();
        SmileList toReturn = new SmileList();
        if (count >= recents.getSmiles().size()){
            return recents;
        }
        if (!recents.getSmiles().isEmpty()) {
            while (toReturn.getSmiles().size() < count) {
                if (start + count < recents.getSmiles().size()) {
                    toReturn.addSmile(recents.getSmiles().get(start));
                    start++;
                } else {
                    start = 0;
                }
            }
        }
        return toReturn;
    }

    @Override
    public void loginOrCreate(String id) {
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
    public void addSmile(String id, Bitmap smile) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        smile.compress(Bitmap.CompressFormat.PNG, 0, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        Blob b = new Blob("image", bs);

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
        getRecents();
        recents.addSmile(new Smile(smileDoc.getDate("postedDate"), smile));
    }

    private void getRecents(){
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
                Date yesterday = new Date(System.currentTimeMillis() - (60 * 60 * 72 * 1000));
                if (d.after(yesterday)){
//                    byte[] bytes = doc.getBlob("data").getContent();
//                    recents.addSmile(new PhotoSmile(doc.getDate("postedDate"), BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));
                    Blob b = doc.getBlob("data");
                    byte[] bytes = b.getContent();
                    recents.addSmile(new Smile(doc.getDate("postedDate"), BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));

                }
            }
        }
    }

    public String getPreference(String id){
        MutableDocument userDoc = database.getDocument(id).toMutable();
        return userDoc.getString("sharing");
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
