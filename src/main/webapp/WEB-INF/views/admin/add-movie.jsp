<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ page
import="com.movieratingsystem.models.UserModel" %> <%@ page
import="com.movieratingsystem.models.Genre" %> <%@ page import="java.util.List"
%> <% UserModel user = (UserModel) session.getAttribute("user"); if(user == null
|| user.getRole() != UserModel.Role.admin) {
response.sendRedirect(request.getContextPath() + "/login"); return; } List<Genre>
  genres = (List<Genre
    >) request.getAttribute("genres"); %>

    <jsp:include page="../includes/header.jsp">
      <jsp:param name="title" value="Add Movie" />
    </jsp:include>

    <div class="admin-container">
      <div class="admin-header">
        <h1>Add New Movie</h1>
      </div>

      <% if(request.getAttribute("error") != null) { %>
      <div class="error-message"><%= request.getAttribute("error") %></div>
      <% } %>

      <form
        action="${pageContext.request.contextPath}/admin/movies/add"
        method="post"
        enctype="multipart/form-data"
        class="form-large"
      >
        <div class="form-group">
          <label for="title">Movie Title</label>
          <input
            type="text"
            id="title"
            name="title"
            required
            placeholder="Enter movie title"
          />
        </div>

        <div class="form-group">
          <label for="releaseDate">Release Date</label>
          <input type="date" id="releaseDate" name="releaseDate" required />
        </div>

        <div class="form-group">
          <label for="minutes">Duration (minutes)</label>
          <input
            type="number"
            id="minutes"
            name="minutes"
            min="1"
            required
            placeholder="Enter movie duration"
          />
        </div>

        <div class="form-group file-input">
          <label for="poster">Movie Poster</label>
          <input type="file" id="poster" name="poster" accept="image/*" />
          <div class="file-input-label">No file chosen</div>
        </div>

        <div class="form-group">
          <label>Genres</label>
          <div class="checkbox-group-container">
            <% if(genres != null && !genres.isEmpty()) { for(Genre genre :
            genres) { %>
            <div class="checkbox-group">
              <input
                type="checkbox"
                id="genre-<%= genre.getGenreId() %>"
                name="genres"
                value="<%= genre.getGenreId() %>"
              />
              <label for="genre-<%= genre.getGenreId() %>"
                ><%= genre.getGenreName() %></label
              >
            </div>
            <% } } else { %>
            <p>
              No genres available.
              <a href="${pageContext.request.contextPath}/admin/genres/add"
                >Add genres first</a
              >.
            </p>
            <% } %>
          </div>
        </div>

        <div class="form-actions">
          <button type="submit" class="btn btn-primary">Add Movie</button>
          <a
            href="${pageContext.request.contextPath}/admin/movies"
            class="btn btn-secondary"
            >Cancel</a
          >
        </div>
      </form>
    </div>

    <jsp:include page="../includes/footer.jsp" />

    <style>
      .checkbox-group-container {
    display: grid;
    grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
    gap: 0.75rem;
    margin-bottom: 1rem;
    max-height: 150px;
    overflow-y: auto;

    padding: 1rem;
    border: 1px solid rgba(255, 255, 255, 0.15);
    border-radius: 6px;
    background-color: rgba(255, 255, 255, 0.07);

    align-content: start;
    justify-content: center;
    align-items: center;
    justify-items: center;

    scrollbar-width: none;
    -ms-overflow-style: none;
}

/* Chrome, Safari, and Opera */
.checkbox-group-container::-webkit-scrollbar {
    display: none;
}

    </style>

    <script>
      // Add JavaScript to update file input label
      document
        .getElementById("poster")
        .addEventListener("change", function (e) {
          const fileName = e.target.files[0]
            ? e.target.files[0].name
            : "No file chosen";
          e.target.nextElementSibling.textContent = fileName;
        });
    </script>
  </Genre></Genre
>
