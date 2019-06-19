package room;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import reportgarden.view.Register;

@Entity(tableName = "Registration")
public class Registeration {
    @PrimaryKey(autoGenerate = true )
    int id;
    @ColumnInfo
    String FirstName;
    @ColumnInfo
            String LastName;
    @ColumnInfo
            String EmailId;
    @ColumnInfo
            String Password;
    public Registeration(String FirstName,String LastName,String EmailId,String Password)
    {
        this.FirstName=FirstName;
        this.LastName=LastName;
        this.EmailId=EmailId;
        this.Password=Password;
    }

    public int getId() {
        return id;
    }

    public String getEmailId() {
        return EmailId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public String getLastName() {
        return LastName;
    }

    public String getPassword() {
        return Password;
    }

    public void setId(int id) {
        this.id = id;
    }
}


