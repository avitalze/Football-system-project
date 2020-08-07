package Stubs;

import Domain.MainSystem;

public class StubExternalSystem {

    private MainSystem ms;

    public StubExternalSystem() {

    }

    public boolean connectToSystem(MainSystem ms){
        if(ms != null)
        {
            this.ms=ms;
            return true;
        }
        return false;
    }

    public MainSystem getMs() {
        return ms;
    }
}
