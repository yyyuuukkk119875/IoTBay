<%-- 
    Document   : welcome
    Created on : Apr 14, 2020, 9:52:20 PM
    Author     : aiswaryarajeev
--%>

<%@page import="java.sql.Connection"%>
<%@page import="uts.isd.model.Customer"%>
<%@page import="java.util.Date"%>
<%@page import="uts.isd.model.iotbay.dao.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/webpage.css">
        <title>Welcome Page</title>
    </head>
    <%
            Boolean newAccount = Boolean.valueOf(request.getParameter("NewAccount"));
            String firstName = request.getParameter("FirstName");
            String lastName = request.getParameter("LastName");
            String email = request.getParameter("Email");
            String password = request.getParameter("Password");
            String gender = request.getParameter("gender");
            String unitNumber = request.getParameter("UnitNumber");
            String streetAddress = request.getParameter("StreetAddress");
            String city = request.getParameter("City");
            String state = request.getParameter("State");
            String postCode = request.getParameter("PostCode");
            String phoneNumber = request.getParameter("PhoneNumber");
            
            Customer customer;
                DBConnector dbConnector = new DBConnector();
                Connection conn = dbConnector.openConnection();
                DBCustomerManager dbManager = new DBCustomerManager(conn);
                if (newAccount) {
                    customer = new Customer(firstName, lastName, email, 
                        password, gender, unitNumber, streetAddress, city, 
                        state, postCode, phoneNumber, new Date(), true);
                    dbManager.addCustomer(firstName, lastName, email, password, 
                        gender, unitNumber, streetAddress, city, state, 
                        postCode, phoneNumber);
                }
                else {
                    customer = dbManager.findCustomer(email);
                }
                session.setAttribute("customer", customer);
        %>
    <body>
         <img src="images/Logo.png" alt="LOGO" style="width:20%; height:10%" class="left"/>
         <p class="right"> <a class="button21" href="logout.jsp">Sign out</a> </p>
         <p class="right"> <a  class="button21" href="main.jsp">Main</a> </p>
                <div class="maincolumn2">
                    <div class="card">
    <body>     
            <% if (newAccount) { %>
            <h1>Hello <%= firstName %> <%= lastName %> !</h1>
            <% } else { %>
            <h1>Welcome back <%= firstName %> <%= lastName %> !</h1>
            <% } %>
            <p class="p">Your email is: <%= email %> </p>
            <p class="p">Your password is: <%= password %> </p>
            <p class="p">Your gender is: <%= gender %> </p>
            <p class="p">Your address is: <%= unitNumber %>, <%= streetAddress %>,
                <%= city %> , <%= state %> , <%= postCode %></p>
                    </div>
                </div>
                
    </body>
</html>
