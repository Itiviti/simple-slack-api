package com.ullink.slack.simpleslackapi;

//TODO: collapse this down into SlackPersonaImpl
public interface SlackPersona {

    String getId();

    String getUserName();
    String getRealName();
    String getUserMail();
    String getUserSkype();
    String getUserPhone();
    String getUserTitle();

    /**
     * To get the user's status
     * @return The status of users
     */
    String getStatusText();
    /**
     * To get the users statuses's emoji text
     * @return The emoji text of users' statuses
     */
    String getStatusEmoji();
    boolean isDeleted();
    boolean isAdmin();
    boolean isOwner();
    boolean isPrimaryOwner();
    boolean isRestricted();
    boolean isUltraRestricted();
    boolean isBot();
    String getTimeZone();
    String getTimeZoneLabel();
    Integer getTimeZoneOffset();
    SlackPresence getPresence();
}
