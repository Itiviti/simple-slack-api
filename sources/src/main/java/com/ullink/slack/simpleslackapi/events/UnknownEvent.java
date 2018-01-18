/*************************************************************************
 * ULLINK CONFIDENTIAL INFORMATION
 * _______________________________
 *
 * All Rights Reserved.
 *
 * NOTICE: This file and its content are the property of Ullink. The
 * information included has been classified as Confidential and may
 * not be copied, modified, distributed, or otherwise disseminated, in
 * whole or part, without the express written permission of Ullink.
 *************************************************************************/
package com.ullink.slack.simpleslackapi.events;

public class UnknownEvent implements SlackEvent
{
    private final String jsonPayload;

    public UnknownEvent(String jsonPayload) {
        this.jsonPayload = jsonPayload;
    }

    @Override public SlackEventType getEventType()
    {
        return SlackEventType.UNKNOWN;
    }

    public String getJsonPayload()
    {
        return jsonPayload;
    }
}
