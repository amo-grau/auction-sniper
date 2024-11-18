package com.oscar;

import java.util.Objects;

public class Item {
    public final String identifier;
    public final int stopPrice;

    public Item(String identifier, int stopPrice){
        this.identifier = identifier;
        this.stopPrice = stopPrice;
    }

    @Override
    public boolean equals(Object obj) {
        // Check for reference equality
        if (this == obj) return true;

        // Check for null or different class
        if (obj == null || getClass() != obj.getClass()) return false;

        Item other = (Item) obj;
        return this.identifier.equals(other.identifier) && this.stopPrice == other.stopPrice;
    }

    @Override
    public int hashCode() {
        // Generate hash code using the same fields as in equals
        return Objects.hash(identifier, stopPrice);
    }
    
    @Override 
    public String toString(){
        return identifier + ", " +  stopPrice;
    }

    public boolean allowsBid(int bid) {
        return bid <= stopPrice;
    }
}
