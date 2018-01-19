package com.example.flavia.fg_user_app;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OldNotificationPreference {
    private static final OldNotificationPreference ourInstance = new OldNotificationPreference();

    public static OldNotificationPreference getInstance() {
        Log.e(OldNotificationPreference.class.getSimpleName(), ourInstance.subscribeNotifications.toString());
        return ourInstance;
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
    private ArrayList<String> subscribeNotifications;
    private boolean atLeastOneGame = true;


    private OldNotificationPreference() {
        subscribeNotifications = new ArrayList<>();
        subscribeNotifications.add("HS");
        subscribeNotifications.add("LOL");
        subscribeNotifications.add("CSGO");
        subscribeNotifications.add("OW");
        subscribeNotifications.add("Anim");
        subscribeNotifications.add("Food");
        subscribeNotifications.add("Ceremony");

        atLeastOneGame = true;

    }

    public List<String> getSubscribeNotifications() {
        return subscribeNotifications;
    }

    /*public void setSubscribeNotifications(List<String> subscribeNotifications) {
        this.subscribeNotifications = subscribeNotifications;
    }*/

    public boolean isActif(String tag) {
        return subscribeNotifications.contains(tag);
    }

    public void add(String tag) {
        if (! isActif(tag)) {
            subscribeNotifications.add(tag);
            atLeastOneGame = atLeastOneGame || GAMES.contains(tag);
        }

    }

    public void remove(String tag) {
        if (isActif(tag)) {
            subscribeNotifications.remove(tag);

            atLeastOneGame = isActif("HS")
                    || isActif("LOL")
                    || isActif("CSGO")
                    || isActif("OW");
        }
    }

    public boolean haveAtLeastOneGame() {
        return atLeastOneGame;
    }
}
