
package com.kfc;

public sealed interface User permits Admin, Customer {
    String getUsername();
}
