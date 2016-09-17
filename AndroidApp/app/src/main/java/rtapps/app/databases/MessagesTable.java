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
    String fullImageName;

    @Column
    String previewImageName;

    @Column
    Long creationDate;

    @Column
    String tag;

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
        this.fullImageName = message.getFullImageName();
        this.tag = message.getTag();
        this.previewImageName = message.getPreviewImageName();
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


    public String getFullImageName(){
        return fullImageName;
    }

    public String getPreviewImageName(){
        return previewImageName;
    }

    public Long getCreationDate() {
        return creationDate;
    }

    public String getTag() {
        return tag;
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
                        tag + " " +
                        fileServerHost + "  " +
                        fullImageName + " " +
                        previewImageName + " " +
                        lastUpdateDate + " " +
                        isExists + "/n";

        return allMessagesString;
    }

    @Override
    public boolean equals(Object o) {
        MessagesTable messagesTable = (MessagesTable) o;
        return this.id.equals(messagesTable.id)&&
                this.previewImageName.equals(messagesTable.previewImageName) &&
                this.isExists.equals(messagesTable.isExists) &&
                this.creationDate.equals(messagesTable.creationDate) &&
                this.applicationId.equals(messagesTable.applicationId) &&
                this.header.equals(messagesTable.header) &&
                this.body.equals(messagesTable.body)&&
                this.lastUpdateDate.equals(messagesTable.lastUpdateDate) &&
                this.fileServerHost.equals(fileServerHost);
    }
}