package cz.dsw.camel_examples.example01.entity;

import java.net.URI;
import java.util.List;

public class HealthCareProvider {

    private URI oid;
    private String name;
    private String dn;
    private String ico;
    private String addr;
    private String icz;
    private String phone;
    private String email;
    private List<String> icp;
    private List<Contact> contacts;

    public URI getOid() { return oid; }
    public void setOid(URI oid) { this.oid = oid; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDn() { return dn; }
    public void setDn(String dn) { this.dn = dn; }

    public String getIco() { return ico; }
    public void setIco(String ico) { this.ico = ico; }

    public String getAddr() { return addr; }
    public void setAddr(String addr) { this.addr = addr; }

    public String getIcz() { return icz; }
    public void setIcz(String icz) { this.icz = icz; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public List<String> getIcp() { return icp; }
    public void setIcp(List<String> icp) { this.icp = icp; }

    public List<Contact> getContacts() { return contacts; }
    public void setContacts(List<Contact> contacts) { this.contacts = contacts; }
}
