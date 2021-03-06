/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package uts.isd.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;
import javax.servlet.RequestDispatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import uts.isd.model.Customer;
import uts.isd.model.CustomerOrder;
import uts.isd.model.Device;
import uts.isd.model.Supplier;
import uts.isd.model.User;
import uts.isd.model.iotbay.dao.DBDeviceManager;
import uts.isd.model.iotbay.dao.DBOrderManager;

/**
 *
 *
 */
public class CreateOrder extends HttpServlet {

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
        // get session
        HttpSession session = request.getSession();
        // device manager to get details of the device
        DBDeviceManager deviceManager = (DBDeviceManager) session.getAttribute("deviceManager");
        // for setting up orders errors
        session.setAttribute("orderErrors", new ArrayList<String>());
        // get id of the device that is to be ordered
        int deviceID = Integer.parseInt(request.getParameter("id"));
        System.out.println("DEvice iD = "+ deviceID);
        Device theDevice = deviceManager.findDeviceByID(deviceID);
        System.out.println("Device =" + theDevice.toString());
        if (theDevice != null) {
            // check if the device is in stock or not
            if (theDevice.getStockQuantity() > 0) {
                // set current device in session
                session.setAttribute("buyDevice", theDevice);
                // Emoty all error if have
                session.setAttribute("orderErrors", new ArrayList<>());
                
                // returns the view
                RequestDispatcher v = request.getRequestDispatcher("/createOrder.jsp");
                v.forward(request, response);
            }
            else if (theDevice.getStockQuantity() == 0) {
                response.sendRedirect("ViewDeviceServletUsers");
                session.setAttribute("quantityErr", "Error: Not enough stock!");
            }
        } else {
            response.sendRedirect("/listDevices.jsp");
        }

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            HttpSession session = request.getSession();
            DBOrderManager orderManager = (DBOrderManager) session.getAttribute("orderManager");

            DBDeviceManager deviceManager = (DBDeviceManager) session.getAttribute("deviceManager");

            Device theDevice = (Device) session.getAttribute("buyDevice");

            // if there is no device , please redirect it 
            if (theDevice == null) {
                response.sendRedirect("/");
            }

            // for tracking errors
            ArrayList<String> orderErrors = new ArrayList<>();

            // create a actual order
            CustomerOrder order = new CustomerOrder();

            // get all data from order form
            String amountFromInput = request.getParameter("amount");
            if (amountFromInput.equals("")) {
                amountFromInput = "0";
            }
            int amount = Integer.parseInt(amountFromInput);
            // delivery address details
            String unitNumber = request.getParameter("unitnumber");
            String streetAddress = request.getParameter("streetaddress");
            String city = request.getParameter("city");
            String state = request.getParameter("state");
            String postcode = request.getParameter("postcode");
            String phoneNumber = request.getParameter("phonenumber");
            double costOfDevice = theDevice.getCost();

            // calculate total cost of the device
            double totalCost = costOfDevice * amount;

            if (theDevice.getStockQuantity() < 0) {
                orderErrors.add("You can not buy more than the stock available.");
            }

            if (theDevice.getStockQuantity() < amount) {
                orderErrors.add("You can not buy more than the stock available.");
            }

            if (amount == 0) {
                orderErrors.add("Please enter atleast 1 amount.");
            }

            // set errors if any
            session.setAttribute("orderErrors", orderErrors);

            if (session.getAttribute("allOrders") == null) {
                session.setAttribute("allOrders", new ArrayList<CustomerOrder>());
            }

            // if contains error
            if (orderErrors.size() > 0) {

                RequestDispatcher view = request.getRequestDispatcher("createOrder.jsp");
                view.forward(request, response);
            } else {

                // set status of the device
                String action
                        = request.getParameter("action");
                System.out.println(action);
                String savedStatus = "SAVED";
                String submittedStatus = "SUBMITED";
                String orderStatus = "";

                if (savedStatus.equalsIgnoreCase(action)) {
                    orderStatus = savedStatus;
                } else if (submittedStatus.equalsIgnoreCase(action)) {
                    orderStatus = submittedStatus;
                }

                // Make data to be saved to Customer order ready
                String orderID = "" + (new Random()).nextInt(999999);
                // get customer from session
                Customer loggedInCustomer = (Customer) request.getAttribute("customer");

                String userEmail = "";

                if (loggedInCustomer == null) {

                    // Make everything Anynomous
                    userEmail = "Anynomous User Email";

                    User user = new User("Anynomous", "Anynomous", "Anynomous", "Anynomous", "Anynomous",
                            unitNumber, streetAddress, city, state, postcode);
                    Supplier sup = new Supplier("Anynomous", "Anynomous", "Anynomous", "Anynomous", true);

                    CustomerOrder aOrder = new CustomerOrder(
                            orderID,
                            user,
                            new Date(),
                            totalCost + 10,
                            new Date().toString(),
                            sup, 10,
                            new Date().toString(),
                            "Air",
                            orderStatus);

                    if (session.getAttribute("allOrders") == null) {
                        session.setAttribute("allOrders", new ArrayList<CustomerOrder>());
                    }
                    ArrayList<CustomerOrder> custOrders = (ArrayList) session.getAttribute("allOrders");
                    custOrders.add(aOrder);
                    System.out.println(custOrders);
                    session.setAttribute("allOrders", custOrders);
                   
                } else {
                    userEmail = loggedInCustomer.getEmail();
                }
                // Current Date
                Timestamp dateOrdered = new Timestamp(new Date().getTime());

                // Estimated Arrival Date 
                // TODO: How to calculate them?? 
                Timestamp estArrivalDate = new Timestamp(new Date().getTime());
                Timestamp departureDate = new Timestamp(new Date().getTime());

                // Get supplier Email Address
                // TODO: Just fakeing it as there is no way to link device with supplier
                String supplierEmail = "";
                // Also same for shipment Price
                double shipmentPrice = totalCost + 10;
                // Same for shipment Type
                String shipmentType = "Air";

                // Also change stock
                if (orderStatus.equalsIgnoreCase("SUBMITED")) {

                    double currentStock = theDevice.getStockQuantity();
                    theDevice.setStockQuantity((int) (currentStock - amount));

                    // update DB of device

                    deviceManager.updateDevice(theDevice.getDeviceID(), theDevice.getDeviceName(), theDevice.getType(), theDevice.getCost(),
                            theDevice.getStockQuantity(), theDevice.getDescription());

                }

                if (loggedInCustomer != null) {
                    // addOrder(String customerEmail, String dateOrdered, double totalPrice,
                    // String estArrivalDate, String departureDate, String supplierEmail, double shipmentPrice,
                    // String shipmentType, String status, String streetAddress, String unitNumber, String city,
                    // String state, String postalCode,  String phoneNumber
                    // Add all these data to DB
                    orderManager.addOrder(userEmail, dateOrdered,
                            totalCost, estArrivalDate, departureDate, supplierEmail, shipmentPrice,
                            shipmentType, orderStatus, streetAddress, unitNumber, city,
                            state, postcode, phoneNumber);
                }

                // Redirection to list of orders
                response.sendRedirect("/OrderHistory");
            }
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

}
