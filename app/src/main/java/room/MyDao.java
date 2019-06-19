package room;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

import model.DependencyModel;

@Dao
public interface MyDao {
    @Insert()
    public void addRegistrationData(Registeration data);


    @Query("Select COUNT(1) AS `num`  from Registration  where EmailId=:emailId")
    public int checkUserName(String emailId);

    @Query("Select COUNT(1) AS `num` from Registration where EmailId=:emailId and Password =:Password")
    public int checkLoginStatus(String emailId,String Password);


}
