<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.movieratingsystem.models.Movie" %>
<%@ page import="com.movieratingsystem.models.Genre" %>
<%@ page import="com.movieratingsystem.service.RatingService" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.util.Base64" %>

<jsp:include page="includes/header.jsp">
    <jsp:param name="title" value="Movies" />
</jsp:include>

<%
    RatingService ratingService = new RatingService();
    DecimalFormat ratingFormat = new DecimalFormat("0.0");
%>

<div class="movies-container">
    <div class="filters-section">
        <div class="filter-section">
            <h3>Search</h3>
            <form action="${pageContext.request.contextPath}/movies" method="get" class="search-form">
                <input type="text" name="search" placeholder="Search movies..." value="${param.search}">
                <button type="submit" class="btn">Search</button>
            </form>
        </div>
        
        <div class="filter-section">
            <h3>Genres</h3>
            <form action="${pageContext.request.contextPath}/movies" method="get" class="filter-form">
                <div class="genre-grid">
                    <% 
                        List<Genre> genres = (List<Genre>) request.getAttribute("genres");
                        if(genres != null && !genres.isEmpty()) {
                            for(Genre genre : genres) {
                    %>
                        <div class="checkbox-group">
                            <input type="checkbox" id="genre-<%= genre.getGenreId() %>" name="genre" value="<%= genre.getGenreId() %>"
                                <% if(request.getParameterValues("genre") != null && java.util.Arrays.asList(request.getParameterValues("genre")).contains(String.valueOf(genre.getGenreId()))) { %>checked<% } %>>
                            <label for="genre-<%= genre.getGenreId() %>"><%= genre.getGenreName() %></label>
                        </div>
                    <% 
                            }
                        } else {
                    %>
                        <p>No genres available.</p>
                    <% } %>
                </div>
                <button type="submit" class="filter-submit-btn">Apply Filters</button>
            </form>
        </div>
        
        <div class="filter-section">
            <h3>Year</h3>
            <form action="${pageContext.request.contextPath}/movies" method="get" class="filter-form">
                <div class="range-slider">
                    <div class="range-input-group">
                        <label for="year-from">From:</label>
                        <input type="number" id="year-from" name="yearFrom" min="1900" max="2025" value="${param.yearFrom != null ? param.yearFrom : '1900'}">
                    </div>
                    
                    <div class="range-input-group">
                        <label for="year-to">To:</label>
                        <input type="number" id="year-to" name="yearTo" min="1900" max="2025" value="${param.yearTo != null ? param.yearTo : '2025'}">
                    </div>
                </div>
                
                <button type="submit" class="btn">Apply Filters</button>
            </form>
        </div>
    </div>
    
    <div class="movie-grid">
        <% 
            List<Movie> movies = (List<Movie>) request.getAttribute("movies");
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            
            if(movies != null && !movies.isEmpty()) {
                for(Movie movie : movies) {
                    double averageRating = ratingService.getAverageRatingForMovie(movie.getMovieId());
        %>
            <div class="movie-card">
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
                    <div class="movie-meta">
                        <div class="movie-year">
                            <i class="far fa-calendar-alt"></i>
                            <%= dateFormat.format(movie.getReleaseDate()) %>
                        </div>
                        <div class="movie-rating">
                            <i class="fas fa-star"></i>
                            <span><%= averageRating > 0 ? ratingFormat.format(averageRating) : "N/A" %></span>
                        </div>
                    </div>
                </div>
                <a href="${pageContext.request.contextPath}/movie?id=<%= movie.getMovieId() %>" class="movie-link"></a>
            </div>
        <% 
                }
            } else {
        %>
            <div class="no-results">
                <i class="fas fa-film"></i>
                <p>No movies found matching your criteria.</p>
            </div>
        <% } %>
    </div>
    
    <div class="pagination">
        <% 
            int currentPage = request.getAttribute("currentPage") != null ? (int) request.getAttribute("currentPage") : 1;
            int totalPages = request.getAttribute("totalPages") != null ? (int) request.getAttribute("totalPages") : 1;
            
            if(totalPages > 1) {
        %>
            <ul>
                <% if(currentPage > 1) { %>
                    <li><a href="${pageContext.request.contextPath}/movies?page=<%= currentPage - 1 %>" class="pagination-link">&laquo; Previous</a></li>
                <% } %>
                
                <% for(int i = 1; i <= totalPages; i++) { %>
                    <li><a href="${pageContext.request.contextPath}/movies?page=<%= i %>" class="pagination-link <%= i == currentPage ? "active" : "" %>"><%= i %></a></li>
                <% } %>
                
                <% if(currentPage < totalPages) { %>
                    <li><a href="${pageContext.request.contextPath}/movies?page=<%= currentPage + 1 %>" class="pagination-link">Next &raquo;</a></li>
                <% } %>
            </ul>
        <% } %>
    </div>
</div>

<jsp:include page="includes/footer.jsp" />
