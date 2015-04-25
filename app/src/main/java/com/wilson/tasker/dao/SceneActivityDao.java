package com.wilson.tasker.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.Property;
import de.greenrobot.dao.internal.DaoConfig;

import com.wilson.tasker.dao.SceneActivity;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table SCENE_ACTIVITY.
*/
public class SceneActivityDao extends AbstractDao<SceneActivity, Long> {

    public static final String TABLENAME = "SCENE_ACTIVITY";

    /**
     * Properties of entity SceneActivity.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Time = new Property(1, java.util.Date.class, "time", false, "TIME");
        public final static Property SceneName = new Property(2, String.class, "sceneName", false, "SCENE_NAME");
        public final static Property ActionType = new Property(3, Integer.class, "actionType", false, "ACTION_TYPE");
    };


    public SceneActivityDao(DaoConfig config) {
        super(config);
    }
    
    public SceneActivityDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(SQLiteDatabase db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "'SCENE_ACTIVITY' (" + //
                "'_id' INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "'TIME' INTEGER," + // 1: time
                "'SCENE_NAME' TEXT," + // 2: sceneName
                "'ACTION_TYPE' INTEGER);"); // 3: actionType
    }

    /** Drops the underlying database table. */
    public static void dropTable(SQLiteDatabase db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "'SCENE_ACTIVITY'";
        db.execSQL(sql);
    }

    /** @inheritdoc */
    @Override
    protected void bindValues(SQLiteStatement stmt, SceneActivity entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        java.util.Date time = entity.getTime();
        if (time != null) {
            stmt.bindLong(2, time.getTime());
        }
 
        String sceneName = entity.getSceneName();
        if (sceneName != null) {
            stmt.bindString(3, sceneName);
        }
 
        Integer actionType = entity.getActionType();
        if (actionType != null) {
            stmt.bindLong(4, actionType);
        }
    }

    /** @inheritdoc */
    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    /** @inheritdoc */
    @Override
    public SceneActivity readEntity(Cursor cursor, int offset) {
        SceneActivity entity = new SceneActivity( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)), // time
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // sceneName
            cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3) // actionType
        );
        return entity;
    }
     
    /** @inheritdoc */
    @Override
    public void readEntity(Cursor cursor, SceneActivity entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setTime(cursor.isNull(offset + 1) ? null : new java.util.Date(cursor.getLong(offset + 1)));
        entity.setSceneName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setActionType(cursor.isNull(offset + 3) ? null : cursor.getInt(offset + 3));
     }
    
    /** @inheritdoc */
    @Override
    protected Long updateKeyAfterInsert(SceneActivity entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    /** @inheritdoc */
    @Override
    public Long getKey(SceneActivity entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    /** @inheritdoc */
    @Override    
    protected boolean isEntityUpdateable() {
        return true;
    }
    
}