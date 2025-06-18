<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.movieratingsystem.models.UserModel" %>
<%@ page import="com.movieratingsystem.models.Genre" %>
<%@ page import="java.util.List" %>

<%
    UserModel user = (UserModel) session.getAttribute("user");
    if(user == null || user.getRole() != UserModel.Role.admin) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    List<Genre> genres = (List<Genre>) request.getAttribute("genres");
%>

<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Manage Genres" />
</jsp:include>

<div class="admin-container">
    <div class="admin-header">
        <h1>Manage Genres</h1>
        <div class="admin-actions">
            <a href="${pageContext.request.contextPath}/admin/genres/add" class="btn btn-primary">Add New Genre</a>
        </div>
    </div>
    
    <% if(request.getAttribute("success") != null) { %>
        <div class="success-message">
            <%= request.getAttribute("success") %>
        </div>
    <% } %>
    
    <div class="search-filter">
        <form action="${pageContext.request.contextPath}/admin/genres" method="get" class="search-form">
            <input type="text" name="search" placeholder="Search genres..." value="${param.search}">
            <button type="submit" class="btn btn-primary">Search</button>
        </form>
    </div>
    
    <div class="table-responsive">
        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Genre Name</th>
                    <th>Description</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    if(genres != null && !genres.isEmpty()) {
                        for(Genre genre : genres) {
                %>
                    <tr>
                        <td><%= genre.getGenreId() %></td>
                        <td><%= genre.getGenreName() %></td>
                        <td><%= genre.getDescription() != null ? genre.getDescription() : "N/A" %></td>
                        <td class="actions-cell">
                            <a href="${pageContext.request.contextPath}/admin/genres/edit?id=<%= genre.getGenreId() %>" class="btn btn-small">Edit</a>
                            <form action="${pageContext.request.contextPath}/admin/genres/delete" method="post" class="inline-form">
                                <input type="hidden" name="genreId" value="<%= genre.getGenreId() %>">
                                <button type="submit" class="btn btn-small btn-danger" onclick="return confirm('Are you sure you want to delete this genre?')">Delete</button>
                            </form>
                        </td>
                    </tr>
                <% 
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="4" class="no-results">No genres found.</td>
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
                    <li><a href="${pageContext.request.contextPath}/admin/genres?page=<%= currentPage - 1 %><%= searchParam %>" class="pagination-link">&laquo; Previous</a></li>
                <% } %>
                
                <% for(int i = 1; i <= totalPages; i++) { %>
                    <li><a href="${pageContext.request.contextPath}/admin/genres?page=<%= i %><%= searchParam %>" class="pagination-link <%= i == currentPage ? "active" : "" %>"><%= i %></a></li>
                <% } %>
                
                <% if(currentPage < totalPages) { %>
                    <li><a href="${pageContext.request.contextPath}/admin/genres?page=<%= currentPage + 1 %><%= searchParam %>" class="pagination-link">Next &raquo;</a></li>
                <% } %>
            </ul>
        <% } %>
    </div>
</div>

<jsp:include page="../includes/footer.jsp" />
