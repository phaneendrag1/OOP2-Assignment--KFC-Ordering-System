
package com.kfc;

public final class Customer implements User {
    private final String username;

    public Customer(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return username;
    }
}
