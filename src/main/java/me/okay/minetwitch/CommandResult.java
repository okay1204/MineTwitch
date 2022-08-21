package me.okay.minetwitch;

public enum CommandResult {
    SUCCESS,
    USAGE_FAILURE,
    PERMISSION_FAILURE;

    public boolean isSuccess() {
        return this == SUCCESS;
    }
}
