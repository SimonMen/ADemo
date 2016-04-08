package com.cedarwood.ademo.manager;

import com.cedarwood.ademo.EventConsts;

import de.greenrobot.event.EventBus;

/**
 * Created by pyx on 10/14/14.
 */
public class EventManager {
    private EventBus eventBus;
    private volatile static EventManager instance;

    public EventBus getEventBus() {
        return eventBus;
    }


    private EventManager() {
        super();
        eventBus = EventBus.getDefault();
    }

    public static EventManager getInstance() {
        if (instance == null) {
            synchronized (EventManager.class) {
                if (instance == null) {
                    instance = new EventManager();
                }
            }
        }
        return instance;
    }

    public void post(EventConsts.BaseEvent event) {
        eventBus.post(event);
    }
}
