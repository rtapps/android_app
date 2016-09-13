package rtapps.app.account.authentication.network.throwables;

/**
 * Created by rtichauer on 9/13/16.
 */
public class Error401 extends NetworkError{
    @Override
    public int getErrorCode() {
        return 401;
    }
}
