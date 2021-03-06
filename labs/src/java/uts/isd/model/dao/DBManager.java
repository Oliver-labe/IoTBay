/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.isd.model.dao;

/**
 *
 * @author willi
 */
import uts.isd.model.CustomerBean;
import uts.isd.model.ProductBean;
import uts.isd.model.Supplier;
import uts.isd.model.OrderBean;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Random;
import javax.swing.text.DateFormatter;
import uts.isd.model.CustomerAccessLogBean;

/* 
* DBManager is the primary DAO class to interact with the database. 
* Complete the existing methods of this classes to perform CRUD operations with the db.
*/

public class DBManager {

    private Statement st;

    public DBManager(Connection conn) throws SQLException {       
       st = conn.createStatement();   
    }

    //Find user by email and password in the database   
    public CustomerBean findCustomer(String emaild) throws SQLException {  
        return findCustomer(emaild, "");
    }
    public CustomerBean findCustomer(String email, String password) throws SQLException {   
        String query = "SELECT * FROM APP.CUSTOMERDB WHERE  Email='"+email+"'"+ (password.length()>0? " AND Password = '"+password+"'":"");
        ResultSet rs = st.executeQuery(query);
        while(rs.next()){
            String cust_email = rs.getString(2);
            String cust_password = rs.getString(3);
            System.out.println(cust_email+","+cust_password);
            if(cust_email.equals(email)&& cust_password.equals(password)){
                CustomerBean cb = new CustomerBean();
                cb.setEmail(cust_email);
                cb.setPassword(cust_password);
                cb.setName(rs.getString(4));
                cb.setId(rs.getInt(1));
                String[] dt = rs.getString(5).split("/");
                System.out.println(Arrays.toString(dt));
                
                cb.setDOB(Date.valueOf(rs.getString(5)));
                cb.setAddress(rs.getString(6));
                cb.setPhone(rs.getString(7));
                cb.setTitle(rs.getString(8));
                return cb;
            }
        }
       //setup the select sql query string       
       //execute this query using the statement field       
       //add the results to a ResultSet       
       //search the ResultSet for a user using the parameters               
       return null;   
    } 
    
    
    

    //Add a user-data into the database   
    public void addCustomer(CustomerBean cb) throws SQLException {                   
//code for add-operation       
//VALUES(0,'pepe@gmail.com','password','Pai pei','12/17/1947','123 Hujianyan St, HongDoui, Singapore',35702572,'Mr');
      SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
      String values=
              "'"
              +cb.getEmail()+"','"
              +cb.getPassword()+"','"
              +cb.getName()+"','"
              +format.format(cb.getDOB())+"','"+
              cb.getAddress()+"','"
              +cb.getPhone()+"','"
              +cb.getTitle()+"'"
              ;
      System.out.println(values);
      st.executeUpdate("INSERT INTO APP.CUSTOMERDB(Email, Password,FullName,DOB,Address,Phone,Title)  VALUES("+values+")");   
      cb.setId(findCustomer(cb.getEmail(),cb.getPassword()).getId());

    }
    public CustomerAccessLogBean addCustomerLoginRecord(CustomerBean cb) throws SQLException {                   
//code for add-operation       
//VALUES(0,'pepe@gmail.com','password','Pai pei','12/17/1947','123 Hujianyan St, HongDoui, Singapore',35702572,'Mr');
      SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
      SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm:ss");
      java.util.Date d =  new java.util.Date();
      String values=
              ""
              +cb.getId()+",'"
              +dateformat.format(d)+"','"
              +timeformat.format(d)+"'"
              ;
      System.out.println(values);
      st.executeUpdate("INSERT INTO APP.CUSTOMER_SESSION(Customer_ID, LOGGEDIN_DATE,LOGGEDIN_TIME)  VALUES("+values+")");   
      CustomerAccessLogBean cab = new CustomerAccessLogBean();
      cab.setCustomer(cb);
      cab.setLoggedin(d);
      return cab;
    }
    
    public void endCustomerLoginRecord(CustomerAccessLogBean cb) throws SQLException {                   
//code for add-operation       
//VALUES(0,'pepe@gmail.com','password','Pai pei','12/17/1947','123 Hujianyan St, HongDoui, Singapore',35702572,'Mr');
      SimpleDateFormat dateformat = new SimpleDateFormat("MM/dd/yyyy");
      SimpleDateFormat timeformat = new SimpleDateFormat("H:m:s");
      java.util.Date d =  new java.util.Date();
      
      
      String values=
              "LOGGEDOUT_DATE = '"+dateformat.format(d)+"',"+
              "LOGGEDOUT_TIME = '"+timeformat.format(d)+"'"
              ;
      System.out.println(values);
      st.executeUpdate(
                "UPDATE APP.CUSTOMER_SESSION SET "+values+" "
                        + "WHERE"
                        + " Customer_ID = "+cb.getCustomerid()+
                          " AND LOGGEDIN_TIME = '"+timeformat.format(cb.getLoggedin())+"'"+
                          " AND LOGGEDIN_DATE = '"+dateformat.format(cb.getLoggedin())+"'" );   
      cb.setLoggedout(d);
    }
    
    public ArrayList<CustomerAccessLogBean> listCustomerLoginRecord(int cid) throws SQLException, ParseException {                   
        ArrayList<CustomerAccessLogBean> result = new ArrayList<>();
      String query = "SELECT * FROM APP.CUSTOMER_SESSION WHERE  Customer_ID="+cid;
        ResultSet rs = st.executeQuery(query);
        
        SimpleDateFormat timeformat = new SimpleDateFormat("yyyy-MM-dd H:m:s");
        while(rs.next()){
             CustomerAccessLogBean cb = new CustomerAccessLogBean();
                cb.setCustomerid(cid);
                cb.setLoggedin(timeformat.parse(rs.getString(2)+" "+rs.getString(3)));
                cb.setLoggedout(rs.getString(4)==null?cb.getLoggedin():(timeformat.parse(rs.getString(4)+" "+rs.getString(5))));
                //apply time here.
                //add search and delete and ur done yey
                result.add(cb);
        }
        return result;
    }

    //update a user details in the database   
    public void updateCustomer(CustomerBean cb) throws SQLException {       
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
        String values=
              "Email = '"+cb.getEmail()+"',"+
              "Password = '"+cb.getPassword()+"',"+
              "FullName = '"+cb.getName()+"',"+
              "DOB = '"+format.format(cb.getDOB())+"',"+
              "Address = '"+cb.getAddress()+"',"+
              "Phone = '"+cb.getPhone()+"',"+
              "Title = '"+cb.getTitle()+"'"
              ;
        System.out.println(values);
        st.executeUpdate("UPDATE APP.CUSTOMERDB SET "+values+" WHERE Customer_ID ="+cb.getId());   

    }       

    //delete a user from the database   
    public void deleteCustomer(String email) throws SQLException{       
       st.executeUpdate("DELETE FROM APP.CUSTOMERDB WHERE Email='"+email+"'");
    }


    
    
    
    
    
    
    

//PRODUCT 
    //Find Product by ID in the database   
    
    // public CustomerBean findCustomer(String emaild) throws SQLException {  
    //    return findCustomer(emaild, "");
    // }
    

    public ProductBean findProduct(String Product_ID) throws SQLException {   
        String query = "SELECT * FROM APP.PRODUCTDB WHERE  Product_ID='"+Product_ID;
        ResultSet rs = st.executeQuery(query);
        while(rs.next()){
            String prod_id = rs.getString(2);
            System.out.println(prod_id);
            if(prod_id.equals(Product_ID)){
                ProductBean pb = new ProductBean();
                pb.setID(prod_id);
                pb.setName(rs.getString(4));
                pb.setPrice(rs.getDouble(5));
                pb.setCategory(rs.getString(6));
                pb.setSupplier(rs.getString(7));
                
                String[] dt = rs.getString(5).split("/");
                System.out.println(Arrays.toString(dt));
                
                return pb;
            }
        }
      return null;
    }
    
    //delete a product from the database   
    public void deleteProduct(String ID) throws SQLException{       
       //code for delete-operation   

    }
    
    
    //add a product from the database   
    public void addProduct(String ID, String name, String price, String supplierid) throws SQLException{       
       //code for add-operation   
        st.executeUpdate("sql query");

    }
    
    
    //update a product from the database   
    public void updateProduct(String ID, String name, String price, String supplierid) throws SQLException{       
       //code for update-operation   

    }
    
    


 
    
    
    
    
    
    
    
    
    
    
    
    //SUPPLIER INFO
    //can view supplier info
     public void showSupplier(String ContactName, String CompanyAddress,int ConNumber, String CompanyType, String CompanyEmail, int Status) throws SQLException{
        String query = "SELECT FROM * APP.SUPPLIERDB";
        ResultSet rs = st.executeQuery(query);
        while(rs.next()){
            ContactName = rs.getString(2);
            CompanyAddress = rs.getString(3);
            ConNumber = rs.getInt(4);
            CompanyType = rs.getString(5);
            CompanyEmail = rs.getString(6);
            Status = rs.getInt(7);
        } 
     }
    
        //find supplier from db
     public Supplier findSupplier(String ContactName, String CompanyEmail) throws SQLException {   
        String query = "SELECT * FROM APP.SUPPLIERDB WHERE  CONTACTNAME='"+ContactName+"'"+ (" AND EMAILADDRESS = '"+CompanyEmail+"'");
        ResultSet rs = st.executeQuery(query);
        
        while(rs.next()){
            String C_Name = rs.getString(2);
            String C_Email = rs.getString(3);
            System.out.println(C_Name+","+C_Email);
            
            if(C_Name.equals(ContactName)&& C_Email.equals(CompanyEmail)){
                String C_Address = rs.getString(3);
                String C_Type = rs.getString(4);
                int C_Status = rs.getInt(6);
                return new Supplier (C_Name, C_Address, C_Type, C_Email, C_Status); 
            }
        }         
       return null;   
    }
       
    //Add a supplier into the db
    public void addSupplier (String CompanyName, String CompanyAddress, String CompanyType, String CompanyEmail, int CompanyStatus) throws SQLException {
        st.executeUpdate("INSERT INTO SUPPLIERDB" + "VALUES ("+CompanyName+", "+CompanyAddress+", "+CompanyType+", "+CompanyEmail+", "+CompanyStatus+")");
    }
    //Update a Suppliers information
    public void updateSupplier (String CompanyName, String CompanyAddress, String CompanyType, String CompanyEmail, int Status) throws SQLException {
        st.executeUpdate("INSERT INTO SUPPLIERDB SET SupName ="+CompanyName+", SET SupAddress  "+CompanyAddress+", SET SupType "+CompanyType+",SET SupEmail "+CompanyEmail+", SET SupStatus "+Status+" WHERE SupEmail ='"+CompanyEmail+"'");
    }
    //delete a supplier from db
    public void deleteSupplier(String CompanyEmail) throws SQLException{
        st.executeUpdate("DELETE FROM APP.SUPPLIER WHERE EMAILADDRESS ='"+CompanyEmail+"'");
  
    }
   
    public Arraylist<Supplier> fetchSupplierList() throws SQLException{
            String fetch = "SELECT * FROM APP.SUPPLIERDB";
            ResultSet rs = st.executeQuery(fetch);
            Arraylist<Supplier> temp = new Arraylist();
            while(rs.next()){
            String C_Name = rs.getString(2);
            String C_Address = rs.getString(3);
            String C_Type = rs.getString(4);
            String C_Email = rs.getString(5);
            int C_Status = rs.getInt(6);
            temp.add(new Supplier(C_Name, C_Address, C_Type, C_Email, C_Status));
        } 
            
            return temp;
    }

    private static class Arraylist<T> {

        public Arraylist() {
        }

        private void add(Supplier supplier) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }
    
    //check if supplier exists using company name and type
    public boolean checkSupplier(String ContactName, String CompanyType) throws SQLException{
       String fetch = "SELECT * FROM APP.SUPPLIERDB WHERE  CONTACTNAME='"+ContactName+"'"+ (" AND COMTYPE = '"+CompanyType+"'");
       ResultSet rs = st.executeQuery(fetch);
       
        while(rs.next()){
            String C_Name = rs.getString(2);
            String C_Type = rs.getString(4);
            if(C_Name.equals(ContactName) && C_Type.equals(CompanyType)){
                return true;
            }
        }
        return false; 
    }
    

    
    
    //Order Management [MVC]
    //Find all orders based on Date_Of_Order
    public OrderBean findOrder(String Date_Of_Order) throws SQLException {  
        return findOrder(Date_Of_Order, "");
    }
    //Find the specific order using Date_Of_Order and Order_ID
    public OrderBean findOrder(String Date_Of_Order, String Order_ID) throws SQLException {   
        String query = "SELECT * FROM APP.ORDERDB WHERE Date_Of_Order='"+Date_Of_Order+"' AND Order_ID = '"+Order_ID;
        ResultSet rs = st.executeQuery(query);
        while(rs.next()){
            String order_date = rs.getString(3);
            String ord_id = rs.getString(1);
            System.out.println(order_date+","+ord_id);
            if(order_date.equals(Date_Of_Order)&& ord_id.equals(Order_ID)){
                OrderBean ob = new OrderBean();
                ob.setOrderId(ord_id);
                ob.setAddress(rs.getString(4));
                ob.setStatus(rs.getString(5));
                ob.setQuanity(rs.getString(7));
                
                String[] dt = rs.getString(3).split("/");
                System.out.println(Arrays.toString(dt));
                
                ob.setDOO(Date.valueOf(rs.getString(3)));
                return ob;
            }
        }            
        return null;   
    }
    //Add a order-data into the database   
    public void addOrder(String Customer_ID, String Date_Of_Order, String Address, String Status, String Product_ID, String Quanity) throws SQLException {                   
    //code for add-operation       
      st.executeUpdate("INSERT INTO APP.ORDERDB" + "VALUES ("+Customer_ID+", "+Date_Of_Order+", "+Address+", "+Status+", "+Product_ID+", "+Quanity+")");   

    }

    //update a order details in the database   
    public void updateOrder(String Customer_ID, String Date_Of_Order, String Address, String Status, String Product_ID, String Quanity) throws SQLException {       
       //code for update-operation   
       st.executeUpdate("INSERT INTO APP.ORDERDB SET Customer_ID ="+Customer_ID+", SET Customer_ID ="+Date_Of_Order+", SET Customer_ID ="+Address+", SET Customer_ID ="+Status+", SET Customer_ID ="+Product_ID+", SET Customer_ID ='"+Quanity+"'"); 
    }   
   
    //delete a order from the database   
    public void deleteOrder(String Order_ID) throws SQLException{       
       //code for delete-operation   
       st.executeUpdate("DELETE FROM APP.ORDERDB WHERE Order_ID ='"+Order_ID+"'");
    }
}