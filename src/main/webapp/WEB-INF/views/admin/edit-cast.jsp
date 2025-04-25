<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.movieratingsystem.models.UserModel" %>
<%@ page import="com.movieratingsystem.models.Cast" %>
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
    
    Cast cast = (Cast) request.getAttribute("cast");
    List<Movie> movies = (List<Movie>) request.getAttribute("movies");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
%>

<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Edit Cast" />
</jsp:include>

<div class="admin-container">
    <div class="admin-header">
        <h1>Edit Cast Member</h1>
    </div>
    
    <% if(request.getAttribute("error") != null) { %>
        <div class="error-message">
            <%= request.getAttribute("error") %>
        </div>
    <% } %>
    
    <form action="${pageContext.request.contextPath}/admin/cast/edit" method="post" enctype="multipart/form-data" class="form-large">
        <input type="hidden" name="castId" value="<%= cast.getCastId() %>">
        
        <div class="form-group">
            <label for="castName">Full Name</label>
            <input type="text" id="castName" name="castName" value="<%= cast.getCastName() %>" required>
        </div>
        
        <div class="form-group">
            <label for="birthDate">Birth Date</label>
            <input type="date" id="birthDate" name="birthDate" value="<%= cast.getBirthDate() != null ? dateFormat.format(cast.getBirthDate()) : "" %>">
        </div>
        
        <div class="form-group">
            <label for="gender">Gender</label>
            <select id="gender" name="gender" required>
                <option value="">Select Gender</option>
                <option value="Male" <%= "Male".equals(cast.getGender()) ? "selected" : "" %>>Male</option>
                <option value="Female" <%= "Female".equals(cast.getGender()) ? "selected" : "" %>>Female</option>
                <option value="Other" <%= "Other".equals(cast.getGender()) ? "selected" : "" %>>Other</option>
            </select>
        </div>
        
        <div class="form-group">
            <label for="biography">Biography</label>
            <textarea id="biography" name="biography" rows="4"><%= cast.getBiography() != null ? cast.getBiography() : "" %></textarea>
        </div>
        
        <div class="form-group">
            <label for="photo">Photo</label>
            <div class="current-image">
                <% if(cast.getPhoto() != null) { %>
                    <img src="data:image/jpeg;base64,<%= Base64.getEncoder().encodeToString(cast.getPhoto()) %>" alt="<%= cast.getCastName() %>" class="profile-preview">
                <% } else { %>
                    <div class="no-image">No photo set</div>
                <% } %>
            </div>
            <input type="file" id="photo" name="photo" accept="image/*">
            <p class="form-hint">Leave empty to keep current photo</p>
        </div>
        
        <div class="form-group">
            <label for="movieId">Movie</label>
            <select id="movieId" name="movieId" required>
                <option value="">Select Movie</option>
                <% 
                    if(movies != null && !movies.isEmpty()) {
                        for(Movie movie : movies) {
                            boolean isSelected = cast.getMovie() != null && cast.getMovie().getMovieId() == movie.getMovieId();
                %>
                    <option value="<%= movie.getMovieId() %>" <%= isSelected ? "selected" : "" %>><%= movie.getTitle() %></option>
                <% 
                        }
                    }
                %>
            </select>
        </div>
        
        <div class="form-group">
            <label for="charName">Character Name</label>
            <input type="text" id="charName" name="charName" value="<%= cast.getCharName() %>" required>
        </div>
        
        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Update Cast Member</button>
            <a href="${pageContext.request.contextPath}/admin/cast" class="btn btn-secondary">Cancel</a>
        </div>
    </form>
</div>

<jsp:include page="../includes/footer.jsp" />
