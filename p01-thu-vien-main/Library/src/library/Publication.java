package library;

public abstract class Publication {
    private String id;
    private String title;

    public Publication(String id, String title) {
        this.id = id;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public abstract long getFinePerDay();
    public abstract String getType();

    @Override
    public String toString() {
        return "id=" + id + ", title=" + title;
    }
}