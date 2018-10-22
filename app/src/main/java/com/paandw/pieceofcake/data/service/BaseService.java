package com.paandw.pieceofcake.data.service;

import org.greenrobot.eventbus.EventBus;

public abstract class BaseService {

    private final EventBus eventBus;

    public BaseService() {
        eventBus = EventBus.getDefault();
    }

    protected void sendEvent(Object event) {
        if (checkForSubscriber(event.getClass())) {
            eventBus.post(event);
        } else {
            eventBus.postSticky(event);
        }
    }

    protected boolean checkForSubscriber(Class<?> event) {
        return eventBus.hasSubscriberForEvent(event);
    }

}
