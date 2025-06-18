<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.movieratingsystem.models.UserModel" %>
<%@ page import="com.movieratingsystem.models.Movie" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Base64" %>

<%
    UserModel user = (UserModel) session.getAttribute("user");
    if(user == null || user.getRole() != UserModel.Role.admin) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    List<Movie> movies = (List<Movie>) request.getAttribute("movies");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
%>

<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Manage Movies" />
</jsp:include>

<div class="admin-container">
    <div class="admin-header">
        <h1>Manage Movies</h1>
        <div class="admin-actions">
            <a href="${pageContext.request.contextPath}/admin/movies/add" class="btn btn-primary">Add New Movie</a>
        </div>
    </div>
    
    <% if(request.getAttribute("success") != null) { %>
        <div class="success-message">
            <%= request.getAttribute("success") %>
        </div>
    <% } %>
    
    <div class="search-filter">
        <form action="${pageContext.request.contextPath}/admin/movies" method="get" class="search-form">
            <input type="text" name="search" placeholder="Search movies..." value="${param.search}">
            <button type="submit" class="btn btn-primary">Search</button>
        </form>
    </div>
    
    <div class="table-responsive">
        <table class="admin-table">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Poster</th>
                    <th>Title</th>
                    <th>Release Date</th>
                    <th>Duration</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    if(movies != null && !movies.isEmpty()) {
                        for(Movie movie : movies) {
                %>
                    <tr>
                        <td><%= movie.getMovieId() %></td>
                        <td class="poster-cell">
                            <% if(movie.getImage() != null) { %>
                                <img src="data:image/jpeg;base64,<%= Base64.getEncoder().encodeToString(movie.getImage()) %>" alt="<%= movie.getTitle() %>" class="table-poster">
                            <% } else { %>
                                <div class="no-poster-small">No Image</div>
                            <% } %>
                        </td>
                        <td><%= movie.getTitle() %></td>
                        <td><%= dateFormat.format(movie.getReleaseDate()) %></td>
                        <td><%= movie.getMinutes() %> min</td>
                        <td class="actions-cell">
                            <a href="${pageContext.request.contextPath}/movie?id=<%= movie.getMovieId() %>" class="btn btn-small">View</a>
                            <a href="${pageContext.request.contextPath}/admin/movies/edit?id=<%= movie.getMovieId() %>" class="btn btn-small">Edit</a>
                            <form action="${pageContext.request.contextPath}/admin/movies/delete" method="post" class="inline-form">
                                <input type="hidden" name="movieId" value="<%= movie.getMovieId() %>">
                                <button type="submit" class="btn btn-small btn-danger" onclick="return confirm('Are you sure you want to delete this movie?')">Delete</button>
                            </form>
                        </td>
                    </tr>
                <% 
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="6" class="no-results">No movies found.</td>
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
                    <li><a href="${pageContext.request.contextPath}/admin/movies?page=<%= currentPage - 1 %>" class="pagination-link">&laquo; Previous</a></li>
                <% } %>
                
                <% for(int i = 1; i <= totalPages; i++) { %>
                    <li><a href="${pageContext.request.contextPath}/admin/movies?page=<%= i %>" class="pagination-link <%= i == currentPage ? "active" : "" %>"><%= i %></a></li>
                <% } %>
                
                <% if(currentPage < totalPages) { %>
                    <li><a href="${pageContext.request.contextPath}/admin/movies?page=<%= currentPage + 1 %>" class="pagination-link">Next &raquo;</a></li>
                <% } %>
            </ul>
        <% } %>
    </div>
</div>

<jsp:include page="../includes/footer.jsp" />
