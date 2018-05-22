package pro200.smile.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract;

import com.couchbase.lite.Array;
import com.couchbase.lite.Blob;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Document;
import com.couchbase.lite.Expression;
import com.couchbase.lite.MutableArray;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import pro200.smile.model.Smile;
import pro200.smile.model.SmileList;

public class LiveSmileService implements SmileService {

    private String smileDB = "SmileDB";

    private Context context;
    private Database database;

    public LiveSmileService(Context context) {
        this.context = context;
        DatabaseConfiguration config = new DatabaseConfiguration(context);
        Database database = null;
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
//            byte[] bytes = smileDoc.getBlob("data");
//            sl.addSmile(new Smile(smileDoc.getDate("postedDate"), smileDoc.getBlob("data")));
        }

        return null;
    }

    @Override
    public SmileList GetRandomSmiles(int count) {
        return null;
    }

    @Override
    public void LoginOrCreate(String id) {
        if (database.getDocument(id) == null) {
            MutableDocument mutableDoc = new MutableDocument(id)
                    .setString("type", "user")
                    .setArray("smiles", new MutableArray());

            try {
                database.save(mutableDoc);
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void AddSmile(String id, Bitmap smile) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        smile.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
        byte[] bitmapdata = bos.toByteArray();
        ByteArrayInputStream bs = new ByteArrayInputStream(bitmapdata);
        Blob b = new Blob("image/jpg", bs);

        MutableDocument smileDoc = new MutableDocument()
                .setString("type", "smile")
                .setBlob("data", b)
                .setValue("postedDate", new Date());

        try {
            database.save(smileDoc);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        MutableDocument userDoc = database.getDocument(id).toMutable();
        userDoc.getArray("smiles").toMutable().addString(smileDoc.getId());
        try {
            database.save(userDoc);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
    }
}
