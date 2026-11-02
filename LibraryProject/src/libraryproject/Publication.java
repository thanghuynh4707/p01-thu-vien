/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryproject;

import java.io.Serializable;

public abstract class Publication implements Serializable {

    private String id;
    private String title;

    public Publication(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public abstract long getFinePerDay();

    public abstract String getType();

    @Override
    public String toString() {
        return "id=" + this.id + ", title=" + this.title;
    }
}
