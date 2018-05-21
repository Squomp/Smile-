package pro200.smile.service;

import android.content.Context;

import com.couchbase.lite.CouchbaseLiteException;
import com.couchbase.lite.DataSource;
import com.couchbase.lite.Database;
import com.couchbase.lite.DatabaseConfiguration;
import com.couchbase.lite.Query;
import com.couchbase.lite.QueryBuilder;
import com.couchbase.lite.ResultSet;
import com.couchbase.lite.SelectResult;

import pro200.smile.model.Smile;
import pro200.smile.model.SmileList;

public class LiveSmileService implements SmileService {

    private String userDB = "Users";
    private String smileDB = "Smiles";
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
            database = new Database(userDB, config);
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }

        // Create a query to fetch documents of type SDK.
        Query query = QueryBuilder.select(SelectResult.property("message"))
                .from(DataSource.database(database));
        ResultSet result = null;
        try {
            result = query.execute();
        } catch (CouchbaseLiteException e) {
            e.printStackTrace();
        }return null;
    }

    @Override
    public SmileList GetRandomSmiles(int count) {
        return null;
    }
}
