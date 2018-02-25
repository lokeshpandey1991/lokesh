<%@include file="/libs/foundation/global.jsp"%>
<%
pageContext.setAttribute("products",slingRequest.adaptTo(com.roche.pharma.customerportal.core.models.AssayMenuModel.class).getProducts());
%>


<c:forEach items="${products}" var="product">
    ${product.productPath}
</c:forEach>