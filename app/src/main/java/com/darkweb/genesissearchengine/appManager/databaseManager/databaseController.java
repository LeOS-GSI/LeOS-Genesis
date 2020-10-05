package com.darkweb.genesissearchengine.appManager.databaseManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import androidx.appcompat.app.AppCompatActivity;
import com.darkweb.genesissearchengine.appManager.bookmarkManager.bookmarkRowModel;
import com.darkweb.genesissearchengine.appManager.historyManager.historyRowModel;
import com.darkweb.genesissearchengine.constants.constants;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static android.content.Context.MODE_PRIVATE;

public class databaseController
{

    /*Private Variables*/

    private static final databaseController sOurInstance = new databaseController();
    private SQLiteDatabase mDatabaseInstance;

    public static databaseController getInstance()
    {
        return sOurInstance;
    }

    private databaseController()
    {
    }

    /*Initializations*/

    public void initialize(AppCompatActivity app_context)
    {
        try
        {
            mDatabaseInstance = app_context.openOrCreateDatabase(constants.DATABASE_NAME, MODE_PRIVATE, null);

            mDatabaseInstance.execSQL("CREATE TABLE IF NOT EXISTS " + "history" + " (id  INT(4) PRIMARY KEY,date DATETIME,url VARCHAR,title VARCHAR);");
            try {
                mDatabaseInstance.execSQL("ALTER TABLE history ADD COLUMN title VARCHAR default ''");
            } catch (SQLiteException ignored) {
            }
            mDatabaseInstance.execSQL("CREATE TABLE IF NOT EXISTS " + "bookmark" + " (id INT(4) PRIMARY KEY,title VARCHAR,url VARCHAR);");

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }

    /*Helper Methods*/

    public void execSQL(String query,String[] params)
    {
        if(params==null)
        {
            mDatabaseInstance.execSQL(query);
        }
        else
        {
            mDatabaseInstance.execSQL(query,params);
        }
    }

    public ArrayList<historyRowModel> selectHistory(int startIndex,int endIndex){
        ArrayList<historyRowModel> tempmodel = new ArrayList<>();

        Cursor c = mDatabaseInstance.rawQuery("SELECT * FROM history ORDER BY date ASC LIMIT "+endIndex+" OFFSET "+startIndex, null);
        if (c.moveToFirst()){
            do {
                historyRowModel model = new historyRowModel(c.getString(3), c.getString(2),Integer.parseInt(c.getString(0)));
                try {
                    Date m_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US).parse(c.getString(1));
                    model.setDate(m_date);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(Calendar.getInstance().getTime().getTime() < model.getDate().getTime()){
                    tempmodel.add(model);
                }else {
                    tempmodel.add(0, model);
                }
            } while(c.moveToNext());
        }
        c.close();

        return  tempmodel;
    }

    public int getLargestHistoryID(){
        int id = 0;
        Cursor c = mDatabaseInstance.rawQuery("SELECT max(id) FROM history", null);

        if (c.moveToFirst()){
            do {
                if(c.getString(0)==null){
                    break;
                }else {
                    id = Integer.parseInt(c.getString(0));
                }
            } while(c.moveToNext());
        }
        c.close();

        return  id;
    }

    public ArrayList<bookmarkRowModel> selectBookmark(){
        ArrayList<bookmarkRowModel> tempmodel = new ArrayList<>();
        Cursor c = mDatabaseInstance.rawQuery("SELECT * FROM bookmark ORDER BY id DESC ", null);

        if (c.moveToFirst()){
            do {
                tempmodel.add(new bookmarkRowModel(c.getString(2), c.getString(1),Integer.parseInt(c.getString(0))));
            } while(c.moveToNext());
        }
        c.close();

        return  tempmodel;
    }

    public void deleteFromList(int index,String table) {
        databaseController.getInstance().execSQL("delete from "+table+" where id="+index,null);
    }

}
