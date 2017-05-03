package org.thilina.systemx.licencemanager;

/**
 * Created by thilina on 3/15/17.
 */
public class UUID {
    private static String uuid;

    public static void setUuid(String uuid) {
        UUID.uuid = uuid;
    }

    public static String getUuid() {
        return uuid;
    }
}
