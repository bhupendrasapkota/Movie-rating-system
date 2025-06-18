<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.movieratingsystem.models.UserModel" %>
<%@ page import="com.movieratingsystem.models.Review" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
    UserModel user = (UserModel) session.getAttribute("user");
    if(user == null || user.getRole() != UserModel.Role.admin) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    List<Review> reviews = (List<Review>) request.getAttribute("reviews");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
%>

<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Manage Reviews" />
</jsp:include>

<div class="admin-container">
    <div class="admin-header">
        <h1>Manage Reviews</h1>
    </div>
    
    <% if(request.getAttribute("success") != null) { %>
        <div class="success-message">
            <%= request.getAttribute("success") %>
        </div>
    <% } %>
    
    <div class="search-filter">
        <form action="${pageContext.request.contextPath}/admin/manage-reviews" method="get" class="search-form">
            <input type="text" name="search" placeholder="Search reviews..." value="${param.search}">
            <button type="submit" class="btn btn-primary">Search</button>
        </form>
    </div>
    
    <div class="table-responsive">
        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>User</th>
                    <th>Movie</th>
                    <th>Comment</th>
                    <th>Date</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    if(reviews != null && !reviews.isEmpty()) {
                        for(Review review : reviews) {
                %>
                    <tr>
                        <td><%= review.getReviewId() %></td>
                        <td><%= review.getUser().getName() %></td>
                        <td><%= review.getMovie().getTitle() %></td>
                        <td class="comment-cell"><%= review.getComment().length() > 100 ? review.getComment().substring(0, 100) + "..." : review.getComment() %></td>
                        <td><%= dateFormat.format(review.getReviewDate()) %></td>
                        <td class="actions-cell">
                            <a href="${pageContext.request.contextPath}/movie?id=<%= review.getMovie().getMovieId() %>" class="btn btn-small">View Movie</a>
                            <form action="${pageContext.request.contextPath}/admin/reviews/delete" method="post" class="inline-form">
                                <input type="hidden" name="reviewId" value="<%= review.getReviewId() %>">
                                <button type="submit" class="btn btn-small btn-danger" onclick="return confirm('Are you sure you want to delete this review?')">Delete</button>
                            </form>
                        </td>
                    </tr>
                <% 
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="6" class="no-results">No reviews found.</td>
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
                    <li><a href="${pageContext.request.contextPath}/admin/manage-reviews?page=<%= currentPage - 1 %>" class="pagination-link">&laquo; Previous</a></li>
                <% } %>
                
                <% for(int i = 1; i <= totalPages; i++) { %>
                    <li><a href="${pageContext.request.contextPath}/admin/manage-reviews?page=<%= i %>" class="pagination-link <%= i == currentPage ? "active" : "" %>"><%= i %></a></li>
                <% } %>
                
                <% if(currentPage < totalPages) { %>
                    <li><a href="${pageContext.request.contextPath}/admin/manage-reviews?page=<%= currentPage + 1 %>" class="pagination-link">Next &raquo;</a></li>
                <% } %>
            </ul>
        <% } %>
    </div>
</div>

<jsp:include page="../includes/footer.jsp" />
