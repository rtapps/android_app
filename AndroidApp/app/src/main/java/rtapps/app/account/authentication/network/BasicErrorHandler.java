package rtapps.app.account.authentication.network;

import retrofit.ErrorHandler;
import retrofit.RetrofitError;
import rtapps.app.account.AccountManager;
import rtapps.app.account.authentication.network.throwables.Error400;
import rtapps.app.account.authentication.network.throwables.Error401;
import rtapps.app.account.authentication.network.throwables.Error500;
import rtapps.app.account.authentication.network.throwables.ErrorUnknown;

/**
 * Created by rtichauer on 9/13/16.
 */
public class BasicErrorHandler implements ErrorHandler{


    @Override
    public Throwable handleError(RetrofitError cause) {
        if (cause == null || cause.getResponse() == null){
            return new ErrorUnknown();
        }
        switch (cause.getResponse().getStatus()){
            case 400:
                return new Error400();
            case 401:
                AccountManager.get().refreshTokenAsync();
                return new Error401();
            case 500:
                return new Error500();
        }
        return new ErrorUnknown();
    }
}
