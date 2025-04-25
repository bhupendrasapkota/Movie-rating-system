<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ page
import="com.movieratingsystem.models.UserModel" %> <%@ page
import="com.movieratingsystem.models.Movie" %> <%@ page import="java.util.List"
%> <% UserModel user = (UserModel) session.getAttribute("user"); if(user == null
|| user.getRole() != UserModel.Role.admin) {
response.sendRedirect(request.getContextPath() + "/login"); return; } List<Movie>
  movies = (List<Movie
    >) request.getAttribute("movies"); %>

    <jsp:include page="../includes/header.jsp">
      <jsp:param name="title" value="Add Cast" />
    </jsp:include>

    <div class="admin-container">
      <div class="admin-header">
        <h1>Add New Cast Member</h1>
      </div>

      <% if(request.getAttribute("error") != null) { %>
      <div class="error-message"><%= request.getAttribute("error") %></div>
      <% } %>

      <form
        action="${pageContext.request.contextPath}/admin/cast/add"
        method="post"
        enctype="multipart/form-data"
        class="form-large"
      >
        <div class="form-group">
          <label for="castName">Full Name</label>
          <input type="text" id="castName" name="castName" required />
        </div>

        <div class="form-group">
          <label for="birthDate">Birth Date</label>
          <input type="date" id="birthDate" name="birthDate" />
        </div>

        <div class="form-group">
          <label for="gender">Gender</label>
          <select id="gender" name="gender" required>
            <option value="">Select Gender</option>
            <option value="Male">Male</option>
            <option value="Female">Female</option>
            <option value="Other">Other</option>
          </select>
        </div>

        <div class="form-group">
          <label for="biography">Biography</label>
          <textarea id="biography" name="biography" rows="4"></textarea>
        </div>

        <div class="form-group">
          <label for="photo">Photo</label>
          <input type="file" id="photo" name="photo" accept="image/*" />
        </div>

        <div class="form-group">
          <label for="movieId">Movie</label>
          <select id="movieId" name="movieId" required>
            <option value="">Select Movie</option>
            <% if(movies != null && !movies.isEmpty()) { for(Movie movie :
            movies) { %>
            <option value="<%= movie.getMovieId() %>">
              <%= movie.getTitle() %>
            </option>
            <% } } %>
          </select>
        </div>

        <div class="form-group">
          <label for="charName">Character Name</label>
          <input type="text" id="charName" name="charName" required />
        </div>

        <div class="form-actions">
          <button type="submit" class="btn btn-primary">Add Cast Member</button>
          <a
            href="${pageContext.request.contextPath}/admin/cast"
            class="btn btn-secondary"
            >Cancel</a
          >
        </div>
      </form>
    </div>

    <jsp:include page="../includes/footer.jsp" /> </Movie
></Movie>
