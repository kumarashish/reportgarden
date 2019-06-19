package room;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {Registeration.class},version = 1)
public abstract class Database extends RoomDatabase {
    public abstract MyDao Dao();

}
