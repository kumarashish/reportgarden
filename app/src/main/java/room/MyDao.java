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
    @Query("Select * from Dependencies")
    public List<Dependencies> getImportedDependency();
    @Query("Select * from ImportedRepository where FullName=:name")
    public Data getImportedRepository(String name);
    @Query("Select * from Dependencies where RepoId=:id and DependencyType =:type")
    public List<Dependencies> getDependency(int id,int type);
    @Query("Select * from Dependencies where Dependency=:dependency and DependencyType =:type")
    public Dependencies getDependency(String dependency,int type);
    @Query("Delete from dependencies where Dependency=:dependency and RepoId=:repoId and DependencyType =:type")
    public void deleteDependency(String dependency,int repoId,int type);
    @Query("Update ImportedRepository set LastUpdated=:value where id=:id")
    public void updateLastSyncTime(String value,int id);

}
