/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import javax.servlet.http.HttpServlet;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import uts.isd.model.AccountTracker;
import uts.isd.model.CustomerAccessLogBean;
import uts.isd.model.CustomerBean;
import uts.isd.model.dao.DBConnector;
import uts.isd.model.dao.DBManager;

/**
 *
 * @author willi
 */
@WebServlet(
  name = "getAccessLogs", 
  urlPatterns = "/accessLogs")
public class AccessLogQueryController  extends HttpServlet {
    private DBConnector db;
    private DBManager manager;
    private Connection conn;
    
    @Override 
    public void init() {
        try {
            db = new DBConnector();
            conn = db.openConnection();
            manager = new DBManager(conn);
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
        }      
    }

    @Override //Destroy the servlet and release the resources of the application (terminate also the db connection)
    public void destroy() {
       try {
           db.closeConnection();
       } catch (SQLException ex) {
           Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
       }
   }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        CustomerBean cb = (CustomerBean)req.getSession().getAttribute("login");
        try{
            ArrayList<CustomerAccessLogBean> queryresult = manager.listCustomerLoginRecord(cb.getId());
            String query = req.getParameter("search_date");
            if(query!=null){

            }
            System.out.println("returned? "+queryresult.toString());
            RequestDispatcher dispatch = req.getRequestDispatcher("accesslogs.jsp");
            req.setAttribute("data",  queryresult);
            dispatch.forward(req, resp);
        }catch(Exception e){
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
}
