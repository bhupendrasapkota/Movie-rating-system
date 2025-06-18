<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ page
import="com.movieratingsystem.models.UserModel" %> <%@ page
import="com.movieratingsystem.models.Movie" %> <%@ page
import="com.movieratingsystem.models.Genre" %> <%@ page import="java.util.List"
%> <%@ page import="java.text.SimpleDateFormat" %> <%@ page
import="java.util.Base64" %> <% UserModel user = (UserModel)
session.getAttribute("user"); if(user == null || user.getRole() !=
UserModel.Role.admin) { response.sendRedirect(request.getContextPath() +
"/login"); return; } Movie movie = (Movie) request.getAttribute("movie");
List<Genre>
  allGenres = (List<Genre
    >) request.getAttribute("allGenres"); List<Integer>
      movieGenreIds = (List<Integer
        >) request.getAttribute("movieGenreIds"); SimpleDateFormat dateFormat =
        new SimpleDateFormat("yyyy-MM-dd"); %>

        <jsp:include page="../includes/header.jsp">
          <jsp:param name="title" value="Edit Movie" />
        </jsp:include>

        <div class="admin-container">
          <div class="admin-header">
            <h1>Edit Movie</h1>
          </div>

          <% if(request.getAttribute("error") != null) { %>
          <div class="error-message"><%= request.getAttribute("error") %></div>
          <% } %>

          <form
            action="${pageContext.request.contextPath}/admin/movies/edit"
            method="post"
            enctype="multipart/form-data"
            class="form-large"
          >
            <input
              type="hidden"
              name="movieId"
              value="<%= movie.getMovieId() %>"
            />

            <div class="form-group">
              <label for="title">Movie Title</label>
              <input
                type="text"
                id="title"
                name="title"
                value="<%= movie.getTitle() %>"
                required
              />
            </div>

            <div class="form-group">
              <label for="releaseDate">Release Date</label>
              <input
                type="date"
                id="releaseDate"
                name="releaseDate"
                value="<%= dateFormat.format(movie.getReleaseDate()) %>"
                required
              />
            </div>

            <div class="form-group">
              <label for="minutes">Duration (minutes)</label>
              <input
                type="number"
                id="minutes"
                name="minutes"
                min="1"
                value="<%= movie.getMinutes() %>"
                required
              />
            </div>

            <div class="form-group">
              <label for="poster">Movie Poster</label>
              <div class="current-image">
                <% if(movie.getImage() != null) { %>
                <img
                  src="data:image/jpeg;base64,<%= Base64.getEncoder().encodeToString(movie.getImage()) %>"
                  alt="<%= movie.getTitle() %>"
                  class="poster-preview"
                />
                <% } else { %>
                <div class="no-image">No poster image set</div>
                <% } %>
              </div>
              <input type="file" id="poster" name="poster" accept="image/*" />
              <p class="form-hint">Leave empty to keep current poster</p>
            </div>

            <div class="form-group">
              <label>Genres</label>
              <div class="checkbox-group-container">
                <% if(allGenres != null && !allGenres.isEmpty()) { for(Genre
                genre : allGenres) { boolean isChecked = movieGenreIds != null
                && movieGenreIds.contains(genre.getGenreId()); %>
                <div class="checkbox-group">
                  <input type="checkbox" id="genre-<%= genre.getGenreId() %>"
                  name="genres" value="<%= genre.getGenreId() %>" <%= isChecked
                  ? "checked" : "" %>>
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
              <button type="submit" class="btn btn-primary">
                Update Movie
              </button>
              <a
                href="${pageContext.request.contextPath}/admin/movies"
                class="btn btn-secondary"
                >Cancel</a
              >
            </div>
          </form>
        </div>

        <jsp:include
          page="../includes/footer.jsp"
        /> </Integer></Integer></Genre
></Genre>
