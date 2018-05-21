package pro200.smile.service;

import android.content.Context;
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

import pro200.smile.model.SmileList;

public class LiveSmileService implements SmileService {

    private String smileDB = "SmileDB";
    private String userSmilesProperty = "SmileList";
    private String idProperty = "id";

    private Context context;

    public LiveSmileService(Context context) {
        this.context = context;
    }

    @Override
    public SmileList GetUserSmiles(String id) {
        // Get the database (and create it if it doesn’t exist).
        DatabaseConfiguration config = new DatabaseConfiguration(context);
        Database database = null;
        try {
            database = new Database(smileDB, config);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        // Create a query to fetch documents of type SDK.
        Query query = QueryBuilder.select(SelectResult.property(userSmilesProperty))
                .from(DataSource.database(database))
                .where(Expression.property(idProperty).equalTo(Expression.string(id)));
        ResultSet result = null;
        try {
            result = query.execute();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public SmileList GetRandomSmiles(int count) {
        return null;
    }

    @Override
    public void LoginOrCreate(String id) {
        // Get the database (and create it if it doesn’t exist).
        DatabaseConfiguration config = new DatabaseConfiguration(context);
        Database database = null;
        try {
            database = new Database(smileDB, config);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        // Create a query to fetch documents of type SDK.
        Query query = QueryBuilder.select(SelectResult.property(idProperty))
                .from(DataSource.database(database))
                .where(Expression.property(idProperty).equalTo(Expression.string(id)));
        ResultSet result = null;
        try {
            result = query.execute();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }
        /* not sure if execute will return null or empty if nothing is found */
        if (result == null || result.allResults().isEmpty()) {
            // Create a new document (i.e. a record) in the database.
            MutableDocument mutableDoc = new MutableDocument()
                    .setString(idProperty, id)
                    .setString("type", "user");

            try {
                database.save(mutableDoc);
            } catch (CouchbaseLiteException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void AddSmile(String id) {
        // Get the database (and create it if it doesn’t exist).
        DatabaseConfiguration config = new DatabaseConfiguration(context);
        Database smileDatabase = null;
        try {
            smileDatabase = new Database(smileDB, config);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        // Create a new document (i.e. a record) in the database.
//        MutableDocument mutableDoc = new MutableDocument()
//                .setBlob(/* blob stuff*/);

//        try {
//            smileDatabase.save(mutableDoc);
//        } catch (CouchbaseLiteException e) {
//            e.printStackTrace();
//        }
    }
}
