<%-- 
    Document   : browseCatalogue
    Created on : 21/05/2020, 8:30:58 PM
    Author     : aiswarya.r
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page import="java.util.List"%>
<%@page import="uts.isd.model.Customer"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.sql.*"%>
<%@page import="java.sql.Connection"%>
<%@page import="uts.isd.model.iotbay.dao.*"%>



<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>  
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="css/webpage.css">
        <title>User Management</title>

    <img src="images/Logo.png" alt="LOGO" style="width:20%; height:10%" class="left"/>
    <div class="maincolumn2">
        <div class="card">

            </head>
            <body>

                <%
                    DBConnector dbConnector = new DBConnector();
                    Connection conn = dbConnector.openConnection();
                    DBCustomerManager dbManager = new DBCustomerManager(conn);
                    List<Customer> customers = dbManager.fetchCustomers();
                    request.setAttribute("customers", customers);
                %>

                <h1>User Management</h1>
                <a class="button1" href="register.jsp">Add New</a>

                <input type="text" id="myInput" onkeyup="myFunction()" placeholder="Search for names.." title="Type in a name">

                <table id="userTable" border="1">
                    <thead>
                        <tr>
                            <th>Email</th>
                            <th>First name</th>
                            <th>Last name</th>
                            <th>Phone number</th>
                            <th>Password</th>
                            <th>Street address</th>
                            <th>Unit number</th>
                            <th>City</th>
                            <th>State</th>
                            <th>Postcode</th>
                            <th>Login status</th>
                            <th>Register date</th>
                            <th>Gender</th>
                            <th>Active</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${customers}" var="c">
                            <tr>
                                <td>${c.getEmail()}</td>
                                <td>${c.getFirstName()}</td>
                                <td>${c.getLastName()}</td>
                                <td>${c.getPhoneNumber()}</td>
                                <td>${c.getPassword()}</td>
                                <td>${c.getStreetAddress()}</td>
                                <td>${c.getUnitNumber()}</td>
                                <td>${c.getCity()}</td>
                                <td>${c.getState()}</td>
                                <td>${c.getPostcode()}</td>
                                <td>${c.isLoginStatus()}</td>
                                <td>${c.getDateRegistered()}</td>
                                <td>${c.getGender()}</td>
                                <td>${c.isActive()}</td>
                                <td><a class="button2" href="edit.jsp">Edit</a></td>
                                <td><a class="button3" href="edit.jsp">Delete</a></td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>

                <script>
                    function myFunction() {
                        var input, filter, table, tr, td, i, txtValue;
                        input = document.getElementById("myInput");
                        filter = input.value.toUpperCase();
                        table = document.getElementById("userTable");
                        tr = table.getElementsByTagName("tr");
                        for (i = 0; i < tr.length; i++) {
                            td = tr[i].getElementsByTagName("td")[1];
                            if (td) {
                                txtValue = td.textContent || td.innerText;
                                if (txtValue.toUpperCase().indexOf(filter) > -1) {
                                    tr[i].style.display = "";
                                } else {
                                    tr[i].style.display = "none";
                                }
                            }
                        }
                    }
                </script>

            </body>
        </div>
    </div>
</html>
