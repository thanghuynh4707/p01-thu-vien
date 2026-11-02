/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package libraryproject;

import java.io.File;
import java.time.LocalDate;

public class LibraryProject {

    public static void main(String[] args) {
        FileStorage storage = new FileStorage("data" + File.separator + "library.dat");
        LibraryService service;
        
        if (storage.exists()){
            try{
                service = storage.load();
                System.out.println("Data loaded from " + storage.getFilePath());
            } catch (IllegalStateException e){
                System.out.println("ERROR: " + e.getMessage());
                System.out.println("Program stopped to avoid overwriting the old file. Please check or back up the file and run again");
                return;
            }
            if (args.length > 0 && args[0].equals("sample")){
                System.out.println("Data already exists, ignoring 'sample' argument (sample data not overwritten).");
            }
        } else {
            service = new LibraryService();
            if (args.length > 0 && args[0].equals("sample")){
                loadSampleData(service);
                System.out.println("Sample data loaded");
            } else {
                System.out.println("No data file found, starting with empty lists (run with argument 'sample' to load sample data).");
            }
        }
        
        new Menu(service, storage).run();
    }
    
    public static void loadSampleData(LibraryService service){
        service.addPublication(new Book("B01", "Bagic Java Programming", "Nguyen van A"));
        service.addPublication(new Book("B02", "Data", "Nguyen van B"));
        service.addPublication(new Journal("J01", "Technology", "Nguyen Thi C"));
        service.addPublication(new Journal("J01", "Science", "Nguyen Thi D"));
        
        service.addReader(new Reader("R01", "Le Van E", "0901000001"));
        service.addReader(new Reader("R02", "Le Thi F", "0901000002"));
        service.addReader(new Reader("R03", "Le Va G", "0901000003"));
        
        Loan l1 = service.borrow("R01", "B01", LocalDate.parse("2026-10-01"));
        service.returnLoan(l1.getLoanId(), LocalDate.parse("2026-10-11"));
        Loan l2 = service.borrow("R02", "J01", LocalDate.parse("2026-10-01"));
        service.returnLoan(l1.getLoanId(), LocalDate.parse("2026-10-08"));
        service.borrow("R01", "R02", LocalDate.parse("2026-10-15"));
    } 
}
