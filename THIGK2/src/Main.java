import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    public static void main(String[] args) throws SQLException {
        StaffManager manager = new StaffManager(); 
        StaffManager temp; 
        
        boolean c = true;
        while(c) {
            System.out.println("==========================================");
            System.out.println("1. add interm");
            System.out.println("2. add fresher");
            System.out.println("3. add experience");
            System.out.println("4. Print");
            System.out.println("5. Update");
            System.out.println("6. Delete");
            System.out.println("7. Write to file");
            System.out.println("8. Read from file");
            System.out.println("0: exit");
            System.out.println("==========================================");
            
            Scanner sc = new Scanner(System.in);
            String choose = sc.nextLine();
            
            switch (choose) {
                case "1" -> {
                    Staff staff = manager.create("intern");
                    if(staff != null)
                        manager.add(staff);
                }

                case "2" -> {
                    Staff staff = manager.create("fresher");
                    if(staff != null)
                    manager.add(staff);
                }

                case "3" -> {
                    Staff staff = manager.create("ex");
                    if(staff != null)
                        manager.add(staff);
                }
                case "4" -> {
                    manager.read();
                }
                case "5" -> {
                    System.out.print("Enter ID: ");
                    String id = sc.nextLine();
                    manager.update(id);
                }
                case "6" -> {
                    System.out.print("Enter ID: ");
                    String id = sc.nextLine();
                    manager.delete(id);
                }
                case "7" -> {
                    try {
                        System.out.print("File path: ");
                        String src = sc.nextLine();
                        File file = new File(src);
                        OutputStream os = new FileOutputStream(file);
                        
                        ObjectOutputStream oos = new ObjectOutputStream(os);
                        
                        List<Staff> list = manager.getStaffs();
                        
                            for(Staff staff : list){
                                if(staff instanceof Intern ) {
                                    Intern intern = (Intern) staff;
                                    try {
                                        oos.writeObject(intern);
                                    } catch (IOException ex) {
                                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }else if(staff instanceof Fresher) {
                                        Fresher fresher = (Fresher) staff;
                                    try {
                                        oos.writeObject(fresher);
                                    } catch (IOException ex) {
                                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                } else if(staff instanceof Experience) {
                                    Experience experience = (Experience) staff;
                                    try {
                                        oos.writeObject(experience);
                                    } catch (IOException ex) {
                                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                                    }
                                }
                            }
                            System.out.println("Done");
                            System.out.println("Data can be override");
                            oos.flush();
                            oos.close();
                        
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                case "8" -> {
                    temp = new StaffManager();
                    System.out.print("File path: ");
                    String src = sc.nextLine();
                    File file = new File(src);
                    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                        Staff staff = null;
                        while (true) {
                            try {
                                staff = (Staff) ois.readObject();
                                temp.add(staff);
                            } catch (EOFException e) {
                                break;
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        temp.read();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                case  "0" ->{
                    c = false;
                }
                default -> throw new AssertionError();
            }
        }
    }
}
