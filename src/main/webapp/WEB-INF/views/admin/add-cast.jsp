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

        <div class="movie-roles-container">
          <h3>Movie Roles</h3>
          <div id="movieRoles">
            <div class="movie-role-entry">
              <div class="form-group">
                <label for="movieId_0">Movie</label>
                <select id="movieId_0" name="movieId[]" required>
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
                <label for="charName_0">Character Name</label>
                <input type="text" id="charName_0" name="charName[]" required />
              </div>
            </div>
          </div>
          <button
            type="button"
            class="btn btn-secondary"
            onclick="addMovieRole()"
          >
            Add Another Movie Role
          </button>
        </div>

        <div class="form-actions">
          <button
            type="submit"
            class="btn btn-primary"
            onclick="return validateAndSubmitForm(event)"
          >
            Add Cast Member
          </button>
          <a
            href="${pageContext.request.contextPath}/admin/cast"
            class="btn btn-secondary"
            >Cancel</a
          >
        </div>
      </form>
    </div>

    <style>
      .movie-role-entry {
        border: 1px solid #ddd;
        padding: 15px;
        margin-bottom: 15px;
        border-radius: 4px;
        position: relative;
      }
      .remove-role-btn {
        position: absolute;
        top: 10px;
        right: 10px;
        background: #ff4444;
        color: white;
        border: none;
        border-radius: 4px;
        padding: 5px 10px;
        cursor: pointer;
      }
      .movie-roles-container {
        margin: 20px 0;
      }
    </style>

    <script>
      function addMovieRole() {
        const container = document.getElementById("movieRoles");
        const newEntry = document.createElement("div");
        newEntry.className = "movie-role-entry";

        newEntry.innerHTML = `
            <button type="button" class="remove-role-btn" onclick="this.parentElement.remove()">Remove</button>
            <div class="form-group">
                <label for="movieId_${container.children.length}">Movie</label>
                <select id="movieId_${container.children.length}" name="movieId[]" required>
                    <option value="">Select Movie</option>
                    <% if(movies != null && !movies.isEmpty()) { 
                        for(Movie movie : movies) { %>
                        <option value="<%= movie.getMovieId() %>">
                            <%= movie.getTitle() %>
                        </option>
                    <% } } %>
                </select>
            </div>
            <div class="form-group">
                <label for="charName_${container.children.length}">Character Name</label>
                <input type="text" id="charName_${container.children.length}" name="charName[]" required />
            </div>
        `;

        container.appendChild(newEntry);
      }

      function validateAndSubmitForm(event) {
        const container = document.getElementById("movieRoles");
        const entries = container.getElementsByClassName("movie-role-entry");

        for (let i = 0; i < entries.length; i++) {
          const entry = entries[i];
          const movieSelect = entry.querySelector("select");
          const charInput = entry.querySelector("input");

          if (!movieSelect.value || !charInput.value) {
            event.preventDefault();
            alert("Please fill in all movie and character name fields");
            return false;
          }
        }

        return true;
      }
    </script>

    <jsp:include page="../includes/footer.jsp" /> </Movie
></Movie>
