
package databaseConnect;



public class Patients {
    
    private int id;
    private String name,surname,borthDate,phone;
    private int tolov;
    private String regDate;

    public Patients(String name, String surname, String borthDate,String phone, int tolov, String regDate) {
        this.name = name;
        this.surname = surname;
        this.borthDate = borthDate;
        this.phone = phone;
        this.tolov = tolov;
        this.regDate = regDate;
    }
    
    public Patients(int id,String name, String surname, String borthDate,String phone, int tolov, String regDate) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.borthDate = borthDate;
        this.phone = phone;
        this.tolov = tolov;
        this.regDate = regDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
    
    

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getBorthDate() {
        return borthDate;
    }

    public void setBorthDate(String borthDate) {
        this.borthDate = borthDate;
    }

    public int getTolov() {
        return tolov;
    }

    public void setTolov(int tolov) {
        this.tolov = tolov;
    }

    public String getRegDate() {
        return regDate;
    }

    public void setRegDate(String regDate) {
        this.regDate = regDate;
    }
    
    
    
}
