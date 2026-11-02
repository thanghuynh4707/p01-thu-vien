/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryproject;

import java.io.Serializable;

public class Reader implements Serializable {

    private String id;
    private String fullName;
    private String contact;

    public Reader(String id, String fullName, String contact) {
        if (id == null || id.trim().isEmpty() || id.trim().length() > 20 || id.trim().matches(".*\\s+.*")) {
            throw new IllegalArgumentException("Invalid reader Id");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name must not be empty");
        }
        if (contact == null || contact.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact must not be empty");
        }

        this.id = id.trim();
        this.fullName = fullName.trim();
        this.contact = contact.trim();
    }

    public String getId() {
        return this.id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Full name must not be empty");
        }

        this.fullName = fullName.trim();
    }

    public String getContact() {
        return this.contact;
    }

    public void setContact(String contact) {
        if (contact == null || contact.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact must not be empty");
        }

        this.contact = contact.trim();
    }

    @Override
    public String toString() {
        return "Reader{ id = " + this.id + " | full name = " + this.fullName + " | contact = " + this.contact + "}";
    }

}
