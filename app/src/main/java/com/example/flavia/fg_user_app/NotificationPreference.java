package com.example.flavia.fg_user_app;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotificationPreference {
    private static final NotificationPreference ourInstance = new NotificationPreference();

    public static NotificationPreference getInstance() {
        return ourInstance;
    }

    private NotificationPreference() {
        this.subscribeNotifications = DEFAULT_PREFERENCES;
    }

    static final List<String> DEFAULT_PREFERENCES = Collections.unmodifiableList(
            new ArrayList<String>() {
                {
                    add("HS");
                    add("LOL");
                    add("CSGO");
                    add("OW");
                    add("Anim");
                    add("Food");
                    add("Ceremony");
                }
            });

    static final List<String> GAMES = Collections.unmodifiableList(
            new ArrayList<String>() {
                {
                    add("HS");
                    add("LOL");
                    add("CSGO");
                    add("OW");
                }
            });


    // Notification List should contain only :
    // HS
    // LOL
    // CSGO
    // OW
    // Anim
    // Food
    // Ceremony
    private List<String> subscribeNotifications = new ArrayList<>();
    private boolean atLeastOneGame = true;



    public List<String> getSubscribeNotifications() {
        return subscribeNotifications;
    }

    public void setSubscribeNotifications(List<String> subscribeNotifications) {
        this.subscribeNotifications = subscribeNotifications;
    }

    public boolean isAlreadyActif(String tag) {
        return subscribeNotifications.contains(tag);
    }

    public void add(String tag) {
        if (isAlreadyActif(tag))
            throw new IllegalArgumentException("Already on the list");
        subscribeNotifications.add(tag);

        atLeastOneGame = atLeastOneGame || GAMES.contains(tag);
    }

    public void remove(String tag) {
        if (!isAlreadyActif(tag))
            throw new IllegalArgumentException("Not on the list");
        subscribeNotifications.remove(tag);


       atLeastOneGame = subscribeNotifications.contains("HS")
               || subscribeNotifications.contains("LOL")
               || subscribeNotifications.contains("CSGO")
               || subscribeNotifications.contains("OW");
    }

    public boolean haveAtLeastOneGame() {
        return atLeastOneGame;
    }
}
