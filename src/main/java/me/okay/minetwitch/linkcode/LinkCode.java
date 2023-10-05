package me.okay.minetwitch.linkcode;

public class LinkCode {
    private String code;
    private int expiresIn;

    public LinkCode(String code, int expiresIn) {
        this.code = code;
        this.expiresIn = expiresIn;
    }

    public String getCode() {
        return code;
    }

    public long getExpiresIn() {
        return expiresIn;
    }
}
