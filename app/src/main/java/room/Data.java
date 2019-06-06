package room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "ImportedRepository")
public class Data  {
    @PrimaryKey(autoGenerate = true )
    int id;
    @ColumnInfo
    String FullName;
    @ColumnInfo
    String LastUpdated;

    public Data(String FullName, String LastUpdated)
    {
   this.FullName=FullName;
    this.LastUpdated=LastUpdated;
    }

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getFullName() {
        return FullName;
    }

    public String getLastUpdated() {
        return LastUpdated;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public void setLastUpdated(String lastUpdated) {
        LastUpdated = lastUpdated;
    }
}


