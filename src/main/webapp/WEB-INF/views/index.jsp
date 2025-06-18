<%--
  Created by IntelliJ IDEA.
  User: Sharvik
  Date: 4/19/2025
  Time: 8:34 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.movieratingsystem.models.Movie" %>
<%@ page import="com.movieratingsystem.models.Review" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.Base64" %>

<jsp:include page="includes/header.jsp">
    <jsp:param name="title" value="Home" />
</jsp:include>

<section class="hero-section">
    <div class="hero-content">
        <h1>Discover and Rate Your Favorite Movies</h1>
        <p>Join our community to explore, rate, and review the latest movies. Share your thoughts and connect with fellow movie enthusiasts.</p>
        <div class="hero-actions">
            <a href="${pageContext.request.contextPath}/movies" class="btn btn-primary">
                <i class="fas fa-film"></i> Browse Movies
            </a>
            <% if(session.getAttribute("user") == null) { %>
            <a href="${pageContext.request.contextPath}/register" class="btn btn-secondary">
                <i class="fas fa-user-plus"></i> Join Now
            </a>
            <% } %>
        </div>
    </div>
</section>

<section class="featured-section">
    <h2 class="section-title">Featured Movies</h2>

    <div class="movie-grid">
        <%
            List<Movie> featuredMovies = (List<Movie>) request.getAttribute("featuredMovies");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            SimpleDateFormat reviewDateFormat = new SimpleDateFormat("MMMM dd, yyyy");

            if(featuredMovies != null && !featuredMovies.isEmpty()) {
                for(Movie movie : featuredMovies) {
        %>
        <a href="${pageContext.request.contextPath}/movie?id=<%= movie.getMovieId() %>" class="movie-card">
            <div class="movie-poster">
                <% if(movie.getImage() != null) { %>
                <img src="data:image/jpeg;base64,<%= Base64.getEncoder().encodeToString(movie.getImage()) %>" alt="<%= movie.getTitle() %>">
                <% } else { %>
                <div class="no-poster">
                    <i class="fas fa-film"></i>
                </div>
                <% } %>
            </div>
            <div class="movie-info">
                <h3><%= movie.getTitle() %></h3>
                <p class="movie-year"><i class="far fa-calendar-alt"></i> <%= dateFormat.format(movie.getReleaseDate()) %></p>
            </div>
        </a>
        <%
            }
        } else {
        %>
        <p class="no-results">
            <i class="fas fa-film"></i>
            <br>No featured movies available.
        </p>
        <% } %>
    </div>
</section>

<section class="recent-reviews">
    <h2 class="section-title">Recent Reviews</h2>

    <div class="reviews-container">
        <%
            List<Review> recentReviews = (List<Review>) request.getAttribute("recentReviews");
            if(recentReviews != null && !recentReviews.isEmpty()) {
                for(Review review : recentReviews) {
        %>
        <div class="review-card">
            <div class="review-header">
                <div class="reviewer-info">
                    <div class="reviewer-avatar">
                        <% if(review.getUser().getImage() != null) { %>
                        <img src="${pageContext.request.contextPath}/user/image?id=<%= review.getUser().getId() %>" alt="<%= review.getUser().getName() %>">
                        <% } else { %>
                        <span><%= review.getUser().getName().charAt(0) %></span>
                        <% } %>
                    </div>
                    <div class="reviewer-name"><%= review.getUser().getName() %></div>
                </div>
                <div class="review-movie">
                    <a href="${pageContext.request.contextPath}/movie?id=<%= review.getMovie().getMovieId() %>">
                        <i class="fas fa-film"></i> <%= review.getMovie().getTitle() %>
                    </a>
                </div>
            </div>
            <div class="review-content">
                <p><%= review.getComment() %></p>
            </div>
            <div class="review-footer">
                <div class="review-date">
                    <i class="far fa-clock"></i> <%= reviewDateFormat.format(review.getReviewDate()) %>
                </div>
            </div>
        </div>
        <%
            }
        } else {
        %>
        <p class="no-results">
            <i class="fas fa-comments"></i>
            <br>No recent reviews available.
        </p>
        <% } %>
    </div>
</section>

<jsp:include page="includes/footer.jsp" />