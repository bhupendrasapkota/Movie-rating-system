<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ page
import="com.movieratingsystem.models.UserModel" %> <%@ page
import="com.movieratingsystem.models.Movie" %> <%@ page
import="com.movieratingsystem.service.RatingService" %> <%@ page
import="java.util.List" %> <%@ page import="java.text.SimpleDateFormat" %> <%@
page import="java.text.DecimalFormat" %> <%@ page import="java.util.Base64" %>
<% UserModel user = (UserModel) session.getAttribute("user"); if(user == null) {
response.sendRedirect(request.getContextPath() + "/login"); return; } List<Movie>
  watchlist = (List<Movie
    >) request.getAttribute("watchlist"); SimpleDateFormat yearFormat = new
    SimpleDateFormat("yyyy"); RatingService ratingService = new RatingService();
    DecimalFormat ratingFormat = new DecimalFormat("0.0"); %>

    <jsp:include page="includes/header.jsp">
      <jsp:param name="title" value="My Watchlist" />
    </jsp:include>

    <div class="watchlist-container">
      <div class="watchlist-header">
        <h1>My Watchlist</h1>
        <p class="watchlist-count">
          <%= watchlist != null ? watchlist.size() : 0 %> movies
        </p>
      </div>

      <% if(request.getAttribute("success") != null) { %>
      <div class="success-message">
        <i class="fas fa-check-circle"></i>
        <%= request.getAttribute("success") %>
      </div>
      <% } %> <% if(watchlist != null && !watchlist.isEmpty()) { %>
      <div class="movie-grid">
        <% for(Movie movie : watchlist) { double averageRating =
        ratingService.getAverageRatingForMovie(movie.getMovieId()); %>
        <div class="movie-card">
          <div class="movie-poster">
            <% if(movie.getImage() != null) { %>
            <img
              src="data:image/jpeg;base64,<%= Base64.getEncoder().encodeToString(movie.getImage()) %>"
              alt="<%= movie.getTitle() %>"
            />
            <% } else { %>
            <div class="no-poster">
              <i class="fas fa-film"></i>
            </div>
            <% } %>
          </div>
          <div class="movie-info">
            <h3><%= movie.getTitle() %></h3>
            <div class="movie-meta">
              <div class="movie-year">
                <i class="far fa-calendar-alt"></i>
                <%= yearFormat.format(movie.getReleaseDate()) %>
              </div>
              <div class="movie-rating">
                <i class="fas fa-star"></i>
                <span
                  ><%= averageRating > 0 ? ratingFormat.format(averageRating) :
                  "N/A" %></span
                >
              </div>
            </div>
          </div>
          <div class="movie-actions">
            <a
              href="${pageContext.request.contextPath}/movie?id=<%= movie.getMovieId() %>"
              class="btn btn-primary"
            >
              <i class="fas fa-info-circle"></i>
              Details
            </a>
            <form
              action="${pageContext.request.contextPath}/watchlist/remove"
              method="post"
              class="remove-form"
            >
              <input
                type="hidden"
                name="movieId"
                value="<%= movie.getMovieId() %>"
              />
              <button type="submit" class="btn btn-danger">
                <i class="fas fa-trash-alt"></i>
                Remove
              </button>
            </form>
          </div>
          <div class="hover-overlay"></div>
        </div>
        <% } %>
      </div>
      <% } else { %>
      <div class="empty-watchlist">
        <i class="fas fa-film"></i>
        <h2>Your watchlist is empty</h2>
        <p>
          Start adding movies to your watchlist to keep track of what you want
          to watch!
        </p>
        <a
          href="${pageContext.request.contextPath}/movies"
          class="btn btn-primary"
        >
          <i class="fas fa-search"></i>
          Browse Movies
        </a>
      </div>
      <% } %>
    </div>

    <jsp:include page="includes/footer.jsp" /> </Movie
></Movie>
