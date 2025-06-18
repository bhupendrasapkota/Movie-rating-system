<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.movieratingsystem.models.UserModel" %>
<%@ page import="com.movieratingsystem.models.Cast" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Base64" %>

<%
    UserModel user = (UserModel) session.getAttribute("user");
    if(user == null || user.getRole() != UserModel.Role.admin) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    List<Cast> castList = (List<Cast>) request.getAttribute("castList");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
%>

<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Manage Cast" />
</jsp:include>

<div class="admin-container">
    <div class="admin-header">
        <h1>Manage Cast</h1>
        <div class="admin-actions">
            <a href="${pageContext.request.contextPath}/admin/cast/add" class="btn btn-primary">Add New Cast</a>
        </div>
    </div>
    
    <% if(request.getAttribute("success") != null) { %>
        <div class="success-message">
            <%= request.getAttribute("success") %>
        </div>
    <% } %>
    
    <div class="search-filter">
        <form action="${pageContext.request.contextPath}/admin/cast" method="get" class="search-form">
            <input type="text" name="search" placeholder="Search cast members..." value="${param.search}">
            <button type="submit" class="btn btn-primary">Search</button>
        </form>
    </div>
    
    <div class="table-responsive">
        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Photo</th>
                    <th>Name</th>
                    <th>Birth Date</th>
                    <th>Gender</th>
                    <th>Movie</th>
                    <th>Character</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    if(castList != null && !castList.isEmpty()) {
                        for(Cast castMember : castList) {
                %>
                    <tr>
                        <td><%= castMember.getCastId() %></td>
                        <td class="poster-cell">
                            <% if(castMember.getPhoto() != null) { %>
                                <img src="data:image/jpeg;base64,<%= Base64.getEncoder().encodeToString(castMember.getPhoto()) %>" alt="<%= castMember.getCastName() %>" class="table-poster">
                            <% } else { %>
                                <div class="no-poster-small">No Photo</div>
                            <% } %>
                        </td>
                        <td><%= castMember.getCastName() %></td>
                        <td><%= castMember.getBirthDate() != null ? dateFormat.format(castMember.getBirthDate()) : "N/A" %></td>
                        <td><%= castMember.getGender() %></td>
                        <td><%= castMember.getMovie() != null ? castMember.getMovie().getTitle() : "N/A" %></td>
                        <td><%= castMember.getCharName() %></td>
                        <td class="actions-cell">
                            <a href="${pageContext.request.contextPath}/admin/cast/edit?id=<%= castMember.getCastId() %>" class="btn btn-small">Edit</a>
                            <form action="${pageContext.request.contextPath}/admin/cast/delete" method="post" class="inline-form">
                                <input type="hidden" name="castId" value="<%= castMember.getCastId() %>">
                                <button type="submit" class="btn btn-small btn-danger" onclick="return confirm('Are you sure you want to delete this cast member?')">Delete</button>
                            </form>
                        </td>
                    </tr>
                <% 
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="8" class="no-results">No cast members found.</td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>
    
    <div class="pagination">
        <% 
            int currentPage = request.getAttribute("currentPage") != null ? (int) request.getAttribute("currentPage") : 1;
            int totalPages = request.getAttribute("totalPages") != null ? (int) request.getAttribute("totalPages") : 1;
            String searchTerm = (String) request.getAttribute("search");
            String searchParam = searchTerm != null && !searchTerm.isEmpty() ? "&search=" + searchTerm : "";
            
            if(totalPages > 1) {
        %>
            <ul>
                <% if(currentPage > 1) { %>
                    <li><a href="${pageContext.request.contextPath}/admin/cast?page=<%= currentPage - 1 %><%= searchParam %>" class="pagination-link">&laquo; Previous</a></li>
                <% } %>
                
                <% for(int i = 1; i <= totalPages; i++) { %>
                    <li><a href="${pageContext.request.contextPath}/admin/cast?page=<%= i %><%= searchParam %>" class="pagination-link <%= i == currentPage ? "active" : "" %>"><%= i %></a></li>
                <% } %>
                
                <% if(currentPage < totalPages) { %>
                    <li><a href="${pageContext.request.contextPath}/admin/cast?page=<%= currentPage + 1 %><%= searchParam %>" class="pagination-link">Next &raquo;</a></li>
                <% } %>
            </ul>
        <% } %>
    </div>
</div>

<jsp:include page="../includes/footer.jsp" />
