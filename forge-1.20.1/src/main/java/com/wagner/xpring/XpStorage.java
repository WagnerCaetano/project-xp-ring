package com.wagner.xpring;

/**
 * Saved experience is an integer named {@link #STORED_LEVELS}.
 * Keep this key unchanged so items and placed rings from older releases still load.
 */
public final class XpStorage {
    public static final String STORED_LEVELS = "StoredLevels";

    private XpStorage() {
    }

    public static int sanitize(int levels) {
        return Math.max(0, levels);
    }
}
