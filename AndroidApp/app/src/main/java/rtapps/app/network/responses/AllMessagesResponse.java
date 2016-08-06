package rtapps.app.network.responses;

import java.util.List;

/**
 * Created by tazo on 25/07/2016.
 */
public class AllMessagesResponse {

    private List<Message> messageList;
    private long lastUpdateTime;

    public List<Message> getMessagesList() {
        return messageList;
    }

    public long getLastUpdateTime() {
        return lastUpdateTime;
    }

    public static class Message {
        private String id;
        private String applicationId;
        private String header;
        private String body;
        private String fileName;
        private String fileServerHost;
        private Long creationDate;
        private Long lastUpdateDate;
        private boolean exists;

        //For tests only
        public Message(String id, Long creationDate, String applicationId, String header, String body, String fileUrl, String fileServerHost, Long lastUpdateDate, boolean exist) {
            this.id = id;
            this.applicationId = applicationId;
            this.header = header;
            this.body = body;
            this.fileName = fileUrl;
            this.fileServerHost = fileServerHost;
            this.lastUpdateDate = lastUpdateDate;
            this.exists = exist;
            this.creationDate = creationDate;
        }


        public String getId() {
            return id;
        }

        public String getApplicationId() {
            return applicationId;
        }

        public String getHeader() {
            return header;
        }

        public String getBody() {
            return body;
        }

        public String getFileName() {
            return fileName;
        }

        public String getFileServerHost() {
            return fileServerHost;
        }

        public Long getLastUpdateDate() {
            return lastUpdateDate;
        }

        public Long getCreationDate() {
            return creationDate;
        }

        public boolean getExist() {
            return exists;
        }

        @Override
        public String toString() {
            String allMessagesString = "";

            allMessagesString +=
                    id + "" +
                            creationDate + "" +
                            applicationId + " " +
                            header + " " +
                            body + " " +
                            fileName + "  " +
                            lastUpdateDate + " " +
                            exists + "/n";

            return allMessagesString;
        }

    }

    @Override
    public String toString() {
        String allMessagesString = "";
        for (int i = 0; i < messageList.size(); i++) {
            allMessagesString += messageList.get(i).toString() + "\n";
        }
        return allMessagesString;
    }
}
