<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JPA 2.1 Schema Generation (using scripts)</title>
    </head>
    <body>
        <h1>JPA 2.1 Schema Generation (using scripts)</h1>
        
        <a href="${pageContext.request.contextPath}/TestServlet"/>List</a> employees.

    <br><br>
        If you see this page, that means database tables are created 
        using JPA 2.1 standard properties. Look for table name "Employee" in the 
        default database configured for your application server. <br><br>
        WildFly8: in-memory database is used.<br>
    </body>
</html>
