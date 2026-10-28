package library;

public class Journal extends Publication {
    private String issue;

    public Journal(String id, String title, String issue) {
        super(id, title);
        this.issue = issue;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    @Override
    public long getFinePerDay() {
        return 3000;
    }

    @Override
    public String getType() {
        return "JOURNAL";
    }

    @Override
    public String toString() {
        return "Journal[" + super.toString() + ", issue=" + issue + "]";
    }
}