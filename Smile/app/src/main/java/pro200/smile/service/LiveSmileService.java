package pro200.smile.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.provider.ContactsContract;

import com.couchbase.lite.Blob;
import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Expression;
import com.couchbase.lite.MutableDocument;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Date;

import pro200.smile.model.SmileList;

public class LiveSmileService implements SmileService {

    private String smileDB = "SmileDB";
    private String userSmilesProperty = "SmileList";

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
//        Query query = QueryBuilder.select(SelectResult.property(userSmilesProperty))
//                .from(DataSource.database(database))
//                .where(Expression.property(idProperty).equalTo(Expression.string(id)));
//        ResultSet result = null;
//        try {
//            result = query.execute();
//        } catch (CouchbaseLiteException e) {
//            e.printStackTrace();
//        }

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
                    .setString("type", "user");

            try {
                database.save(mutableDoc);
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void AddSmile(String id, Bitmap smile) {
//        ByteArrayOutputStream out = new ByteArrayOutputStream();
//        ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
//        Blob b = new Blob("image/jpg", in);
//
//        MutableDocument mutableDoc = new MutableDocument()
//                .setBlob("")
//                .setValue("postedDate", new Date());
//
//        try {
//            database.save(mutableDoc);
//        } catch (CouchbaseLiteException e) {
//            e.printStackTrace();
//        }
    }
}
