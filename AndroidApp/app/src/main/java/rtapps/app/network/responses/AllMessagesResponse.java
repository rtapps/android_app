package rtapps.app.network.responses;

import java.util.List;

/**
 * Created by tazo on 25/07/2016.
 */
public class AllMessagesResponse {

    private List<Message> messageList;
    private int lastUpdateTime;

    public List<Message> getMessagesList() {
        return messageList;
    }

    public int getLastUpdateTime() {
        return lastUpdateTime;
    }

    public static class Message {
        private String id;
        private double creationDate;
        private int applicationId;
        private String header;
        private String body;
        private String fileUrl;
        private double lastUpdateDate;
        private boolean exists;

        //For tests only
        public Message(String id, double creationDate, int applicationId, String header, String body, String fileUrl, double lastUpdateDate, boolean exist) {
            this.id = id;
            this.applicationId = applicationId;
            this.header = header;
            this.body = body;
            this.fileUrl = fileUrl;
            this.lastUpdateDate = lastUpdateDate;
            this.exists = exist;
            this.creationDate = creationDate;
        }


        public String getId() {
            return id;
        }

        public int getApplicationId() {
            return applicationId;
        }

        public String getHeader() {
            return header;
        }

        public String getBody() {
            return body;
        }

        public String getFileUrl() {
            return fileUrl;
        }

        public double getLastUpdateDate() {
            return lastUpdateDate;
        }

        public double getCreationDate() {
            return creationDate;
        }

        public boolean getExist() {
            return exists;
        }

    }

    public String toString() {
        String allMessagesString = "";
        for (int i = 0; i < messageList.size(); i++) {
            allMessagesString +=
                    messageList.get(i).id + "" +
                            messageList.get(i).creationDate + "" +
                            messageList.get(i).applicationId + " " +
                            messageList.get(i).header + " " + messageList.get(i).body + " " +
                            messageList.get(i).fileUrl + "  " + messageList.get(i).lastUpdateDate + " " +
                            messageList.get(i).exists + "/n";
        }
        return allMessagesString;
    }
}
