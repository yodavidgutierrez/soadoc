<%@ tag description="security filter" pageEncoding="UTF-8" %>
<%@ attribute name="roles" required="true" type="java.lang.String" %>
<%@ taglib uri='http://java.sun.com/jsp/jstl/core' prefix='c'%>
<%@ taglib uri="http://www.soaint.com/webframework/security-functions/1.0.0" prefix="f" %>

<c:if test="${f:userIsLogged() and f:userHasRole(roles)}">
    <jsp:doBody/>
</c:if>