/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package libraryproject;

public class Journal extends Publication {

    private String issue;

    public Journal(String id, String title, String issue) {
        super(id, title);
        this.issue = issue;
    }

    public String getIssue() {
        return this.issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    @Override
    public long getFinePerDay() {
        return 1000;
    }

    @Override
    public String getType() {
        return "JOURNAL";
    }

    @Override
    public String toString() {
        return "Journal[" + super.toString() + ", issue=" + this.issue + "]";
    }
}
