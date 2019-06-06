package room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "Dependencies")

public class Dependencies {
    @PrimaryKey(autoGenerate = true )
    int id;
    @ColumnInfo
    String Dependency;
    @ColumnInfo
    int RepoId;

public Dependencies(String Dependency,int RepoId)
{
    this.RepoId=RepoId;
    this.Dependency=Dependency;

}

    public int getId() {
        return id;
    }

    public int getRepoId() {
        return RepoId;
    }

    public String getDependency() {
        return Dependency;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDependency(String dependency) {
        Dependency = dependency;
    }
}
