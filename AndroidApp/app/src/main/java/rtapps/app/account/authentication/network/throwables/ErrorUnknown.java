package rtapps.app.account.authentication.network.throwables;

/**
 * Created by rtichauer on 9/13/16.
 */
public class ErrorUnknown extends NetworkError{

    public ErrorUnknown(){

    }

    @Override
    public int getErrorCode() {
        return -1;
    }
}
