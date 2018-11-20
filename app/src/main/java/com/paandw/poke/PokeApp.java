package com.paandw.poke;

import android.app.Application;

public class PokeApp extends Application {

    private static PokeApp app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    public static PokeApp get() { return app; }
}
