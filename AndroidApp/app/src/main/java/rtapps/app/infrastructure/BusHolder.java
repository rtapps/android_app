package rtapps.app.infrastructure;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class BusHolder {
    private Bus bus;
    private static BusHolder instance;

    private BusHolder() {
        this.bus = new Bus(ThreadEnforcer.ANY);
    }

    public static BusHolder get() {
        if(instance == null) {
            Class var0 = BusHolder.class;
            synchronized(BusHolder.class) {
                if(instance == null) {
                    instance = new BusHolder();
                }
            }
        }

        return instance;
    }

    public Bus getBus() {
        return this.bus;
    }
}
