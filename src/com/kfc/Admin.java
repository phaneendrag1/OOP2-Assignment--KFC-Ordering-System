
package com.kfc;

public final class Admin implements User {
    private final String username;

    public Admin(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
