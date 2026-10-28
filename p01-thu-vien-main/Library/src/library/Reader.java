package librarymanagement;

import java.io.Serializable;


public class Reader implements Serializable {
    private String id;
    private String fullName;
    private String contact;

    public Reader(String id, String fullName, String contact) {
        if(id == null || id.trim().isEmpty()
                || id.trim().length() > 20
                || id.trim().matches(".*\\s+.*")){
            throw new IllegalArgumentException("Ma doc gia khong hop le!");
        }
        if(fullName == null || fullName.trim().isEmpty()){
            throw new IllegalArgumentException("Ho ten khong duoc rong!");
        }
        if(contact == null || contact.trim().isEmpty()){
            throw new IllegalArgumentException("Lien he khong duoc rong!");
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
        if(fullName == null || fullName.trim().isEmpty()){
            throw new IllegalArgumentException("Ho ten khong duoc rong!");
        }
        
        this.fullName = fullName.trim();
    }

    public String getContact() {
        return this.contact;
    }

    public void setContact(String contact) {
        if(contact == null || contact.trim().isEmpty()){
            throw new IllegalArgumentException("Lien he khong duoc rong!");
        }
        
        this.contact = contact.trim();
    }
    @Override
    public String toString(){
        return "Reader{"
                + "id='" + this.id + '\''
                + ", fullName = '" + this.fullName + '\''
                + ", contact = '" + this.contact + '\''
                +'}';
        
    }
    
}
