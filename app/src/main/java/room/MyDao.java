package room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface MyDao {
    @Insert()
    public void addRepo(Data data);
    @Insert()
    public void addDependency(Dependencies data);
    @Query("Select * from ImportedRepository ")
    public List<Data> getImportedRepository();
    @Query("Select * from ImportedRepository where FullName=:name")
    public Data getImportedRepository(String name);
    @Query("Select * from Dependencies where RepoId=:id")
    public List<Dependencies> getDependency(int id);

}
