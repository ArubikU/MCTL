package dev.arubik.mctl.enums;

import lombok.Getter;

public enum Version {
    v1_19_R1("1.19.2", "1_19_R1"),
    v1_18_R1("1.18.1", "1_18_R1"),
    v1_17_R1("1.17.1", "1_17_R1");

    @Getter
    private String version;
    @Getter
    private String nmsVersion;

    private Version(String version, String nmsVersion) {
        this.version = version;
        this.nmsVersion = nmsVersion;
    }

    public static Version getByBukkitVersion(String version) {
        for (Version v : Version.values()) {
            if (v.getVersion().equals(version)) {
                return v;
            }
        }
        return null;
    }
}
