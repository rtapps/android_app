package rtapps.app.account.authentication.network.throwables;

/**
 * Created by rtichauer on 9/13/16.
 */
public abstract class NetworkError extends Exception {

    public abstract int getErrorCode();
}
