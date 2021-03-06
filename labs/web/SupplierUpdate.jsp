<%-- 
    Document   : Supplier
    Author     : mood35-Laptop
--%>

<%@ page import="uts.isd.model.*"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
         <meta charset="utf-8">
        <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
        <title>Update Supplier Page</title>
        <link rel="stylesheet" type="text/css" href="stylesheet.css">
        <link rel="stylesheet" type="text/css" href="lib/bootstrap/css/bootstrap.min.css">
         <script src="lib/jquery/jquery-3.5.0.min.js"></script>
        <script src="lib/bootstrap/js/bootstrap.bundle.min.js"></script>
    </head>
    <body>
        <div class="container">
            <jsp:include page="header.jsp" />
             <div class="row" >
                 <div class="col-sm-12 col-md-3">
                     <jsp:include page="SupplierNavBar.jsp" />
                 </div>
                <div class="col-sm-12 col-md-9 p-4">
                    <div class="jumbotron">
                        <h1>Update</h1> 
                        <form method="post" action="">
                        <table> 
                            <tr><td>Company Name: </td><td><input type="text" value= "${Supplier.CompanyName}" placeholder="Enter Company Name" name="CName"></td></tr>
                            <tr><td>Address: </td><td><input type="text" value= "${Supplier.CompanyAddress}" placeholder="Enter Address" name="CAddress"></td></tr>
                            <tr><td>Company Type: </td><td><input type="text" value= "${Supplier.CompanyType}" placeholder="Enter Company Type" name="CType"></td></tr>
                            <tr><td>Email: </td><td><input type="email" value= "${Supplier.CompanyEmail}" placeholder="Enter Email" name="CEmail"></td></tr>
                            <tr><td>Status: </td><td><input type="email" value= "${Supplier.CompanyStatus}" placeholder="" name="CStatus"></td></tr>
                            
                            <tr><td></td><td><input type="reset"> <input class="btn  btn-primary btn-lg" type="submit" value="Update"> </td></tr>
                        </table>  
                    </div>
                </div>
            </div>  
        </div>
      </body>
</html>
