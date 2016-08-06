package rtapps.app.databases;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import rtapps.app.network.responses.AllMessagesResponse;

//@Parcel(analyze={User.class})
@Table(database = MyDatabase.class)
public class MessagesTable extends BaseModel {

    @Column
    @PrimaryKey
    String id;

    @Column
    String header;

    @Column
    String applicationId;

    @Column
    String body;

    @Column
    String fileServerHost;

    @Column
    String fileName;

    @Column
    Long creationDate;

    @Column
    Long lastUpdateDate;

    @Column
    Boolean isExists;


    public MessagesTable() {
        super();
    }

    public MessagesTable(AllMessagesResponse.Message message) {
        this.id = message.getId();
        this.applicationId = message.getApplicationId();
        this.header = message.getHeader();
        this.body = message.getBody();
        this.fileServerHost = message.getFileServerHost();
        this.fileName = message.getFileName();
        this.creationDate = message.getCreationDate();
        this.lastUpdateDate = message.getLastUpdateDate();
        this.isExists = message.getExist();
    }

    public String getId() {
        return id;
    }


    public String getApplicationId() {
        return applicationId;
    }

    public String getBody() {
        return body;
    }

    public String getHeader() {
        return header;
    }


    public String getFileServerHost() {
        return fileServerHost;
    }


    public String getFileName() {
        return fileName;
    }


    public Long getCreationDate() {
        return creationDate;
    }


    public Long getLastUpdateDate() {
        return lastUpdateDate;
    }


    public Boolean getIsExists() {
        return isExists;
    }

    public String toString() {
        String allMessagesString = "";

        allMessagesString +=
                id + "" +
                        creationDate + "" +
                        applicationId + " " +
                        body + " " +
                        header + " " +
                        fileServerHost + "  " +
                        fileName + " " +
                        lastUpdateDate + " " +
                        isExists + "/n";

        return allMessagesString;
    }

}