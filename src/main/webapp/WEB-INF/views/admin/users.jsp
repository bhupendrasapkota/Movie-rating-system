<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.movieratingsystem.models.UserModel" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Base64" %>

<%
    UserModel user = (UserModel) session.getAttribute("user");
    if(user == null || user.getRole() != UserModel.Role.admin) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    List<UserModel> users = (List<UserModel>) request.getAttribute("users");
%>

<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Manage Users" />
</jsp:include>

<div class="admin-container">
    <div class="admin-header">
        <h1>Manage Users</h1>
    </div>
    
    <% if(request.getAttribute("success") != null) { %>
        <div class="success-message">
            <%= request.getAttribute("success") %>
        </div>
    <% } %>
    
    <div class="search-filter">
        <form action="${pageContext.request.contextPath}/admin/users" method="get" class="search-form">
            <input type="text" name="search" placeholder="Search users..." value="${param.search}">
            <button type="submit" class="btn btn-primary">Search</button>
        </form>
    </div>
    
    <div class="table-responsive">
        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Profile</th>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Role</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    if(users != null && !users.isEmpty()) {
                        for(UserModel userItem : users) {
                %>
                    <tr>
                        <td><%= userItem.getId() %></td>
                        <td class="poster-cell">
                            <% if(userItem.getImage() != null) { %>
                                <img src="${pageContext.request.contextPath}/user/image?id=<%= userItem.getId() %>" alt="<%= userItem.getName() %>" class="table-poster">
                            <% } else { %>
                                <div class="avatar-placeholder-small">
                                    <span><%= userItem.getName().charAt(0) %></span>
                                </div>
                            <% } %>
                        </td>
                        <td><%= userItem.getName() %></td>
                        <td><%= userItem.getEmail() %></td>
                        <td><%= userItem.getRole().toString() %></td>
                        <td class="actions-cell">
                            <a href="${pageContext.request.contextPath}/admin/users/edit?id=<%= userItem.getId() %>" class="btn btn-small">Edit</a>
                            <% if(userItem.getId() != user.getId()) { %>
                                <form action="${pageContext.request.contextPath}/admin/users/delete" method="post" class="inline-form">
                                    <input type="hidden" name="userId" value="<%= userItem.getId() %>">
                                    <button type="submit" class="btn btn-small btn-danger" onclick="return confirm('Are you sure you want to delete this user?')">Delete</button>
                                </form>
                            <% } %>
                        </td>
                    </tr>
                <% 
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="6" class="no-results">No users found.</td>
                    </tr>
                <% } %>
            </tbody>
        </table>
    </div>
    
    <div class="pagination">
        <% 
            int currentPage = request.getAttribute("currentPage") != null ? (int) request.getAttribute("currentPage") : 1;
            int totalPages = request.getAttribute("totalPages") != null ? (int) request.getAttribute("totalPages") : 1;
            
            if(totalPages > 1) {
        %>
            <ul>
                <% if(currentPage > 1) { %>
                    <li><a href="${pageContext.request.contextPath}/admin/users?page=<%= currentPage - 1 %>" class="pagination-link">&laquo; Previous</a></li>
                <% } %>
                
                <% for(int i = 1; i <= totalPages; i++) { %>
                    <li><a href="${pageContext.request.contextPath}/admin/users?page=<%= i %>" class="pagination-link <%= i == currentPage ? "active" : "" %>"><%= i %></a></li>
                <% } %>
                
                <% if(currentPage < totalPages) { %>
                    <li><a href="${pageContext.request.contextPath}/admin/users?page=<%= currentPage + 1 %>" class="pagination-link">Next &raquo;</a></li>
                <% } %>
            </ul>
        <% } %>
    </div>
</div>

<jsp:include page="../includes/footer.jsp" />
