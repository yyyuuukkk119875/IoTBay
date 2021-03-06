/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.isd.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uts.isd.model.Customer;
import uts.isd.model.CustomerOrder;
import uts.isd.model.iotbay.dao.DBOrderManager;

/**
 *
 * @author sanyadua
 */
public class OrderHistorySearch extends HttpServlet {

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        ArrayList<CustomerOrder> orders = new ArrayList<>();
        HttpSession session = request.getSession();
        
        DBOrderManager orderManager = (DBOrderManager) session.getAttribute("orderManager");
        // get id from url
        String id = request.getParameter("searchId");
        if(id == null){
            id = "";
        }
        if (id.equals("")) {
            ArrayList<String> searchErrors = new ArrayList<>();
            searchErrors.add("Enter valid search input");

            session.setAttribute("searchErrors", searchErrors);
        } else {
           
            try {
                // get Orders by the id of that esp. user
                // if user is logged in search DB
                Customer loggedInCustomer = (Customer)session.getAttribute("customer");
                
                
                
                if(loggedInCustomer == null){
                    // id not search session
                    ArrayList<CustomerOrder> ordersFromSession = (ArrayList) session.getAttribute("allOrders");
                    System.out.println("-------------Search---------------");
                    System.out.println(ordersFromSession);
                    
                    for(CustomerOrder o: ordersFromSession){
                        if(o.getOrderID().equalsIgnoreCase(id)){
                            System.out.println(o);
                            orders.add(o);
                        }
                    }
                }  else {
                      orders = orderManager.getOrdersById(id);                   
                }
                // Have to check if the order is of user or not
                // TODO:
                session.setAttribute("allOrders", orders);
            } catch (SQLException e) {
                System.out.println(e);
            }
        }
        RequestDispatcher view = request.getRequestDispatcher("orderHistory.jsp");
        view.forward(request, response);
    }
}
