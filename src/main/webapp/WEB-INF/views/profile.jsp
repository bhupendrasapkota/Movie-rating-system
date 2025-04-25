<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.movieratingsystem.models.UserModel" %>
<%@ page import="com.movieratingsystem.models.Review" %>
<%@ page import="com.movieratingsystem.models.Movie" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Base64" %>
<%@ page import="java.text.SimpleDateFormat" %>

<%
    UserModel user = (UserModel) session.getAttribute("user");
    List<Review> userReviews = (List<Review>) request.getAttribute("userReviews");
    SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
%>

<jsp:include page="includes/header.jsp">
    <jsp:param name="title" value="My Profile" />
</jsp:include>

<div class="profile-container">
    <div class="profile-header">
        <div class="profile-avatar">
            <% if(user.getImage() != null) { %>
            <img src="${pageContext.request.contextPath}/user/image?id=<%= user.getId() %>" alt="<%= user.getName() %>">
            <% } else { %>
            <div class="avatar-placeholder">
                <span><%= user.getName().charAt(0) %></span>
            </div>
            <% } %>
        </div>

        <div class="profile-info">
            <h1><%= user.getName() %></h1>
            <p class="profile-email"><%= user.getEmail() %></p>
            <p class="profile-role">Role: <%= user.getRole().toString() %></p>
        </div>

        <div class="profile-actions">
            <a href="${pageContext.request.contextPath}/profile/edit" class="btn btn-primary">Edit Profile</a>
        </div>
    </div>

    <div class="profile-tabs">
        <div class="tabs-header">
            <button class="tab-btn active" data-tab="reviews">My Reviews</button>
            <button class="tab-btn" data-tab="watchlist">My Watchlist</button>
        </div>

        <div class="tab-content" id="reviews-tab">
            <h2>My Reviews</h2>

            <div class="reviews-list">
                <%
                    if(userReviews != null && !userReviews.isEmpty()) {
                        for(Review review : userReviews) {
                %>
                <div class="review-card">
                    <div class="review-header">
                        <div class="review-movie">
                            <a href="${pageContext.request.contextPath}/movie?id=<%= review.getMovie().getMovieId() %>">
                                <%= review.getMovie().getTitle() %>
                            </a>
                        </div>
                        <div class="review-date"><%= dateFormat.format(review.getReviewDate()) %></div>
                    </div>
                    <div class="review-content">
                        <p><%= review.getComment() %></p>
                    </div>
                    <div class="review-actions">
                        <a href="${pageContext.request.contextPath}/review/edit?id=<%= review.getReviewId() %>" class="btn btn-small">Edit</a>
                        <form action="${pageContext.request.contextPath}/review/delete" method="post" class="inline-form">
                            <input type="hidden" name="reviewId" value="<%= review.getReviewId() %>">
                            <button type="submit" class="btn btn-small btn-danger" onclick="return confirm('Are you sure you want to delete this review?')">Delete</button>
                        </form>
                    </div>
                </div>
                <%
                    }
                } else {
                %>
                <p class="no-results">You haven't written any reviews yet.</p>
                <% } %>
            </div>
        </div>

        <div class="tab-content" id="watchlist-tab" style="display: none;">
            <h2>My Watchlist</h2>

            <div class="movie-grid">
                <%
                    List<Movie> watchlist = (List<Movie>) request.getAttribute("watchlist");
                    SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

                    if(watchlist != null && !watchlist.isEmpty()) {
                        for(Movie movie : watchlist) {
                %>
                <div class="movie-card">
                    <div class="movie-poster">
                        <% if(movie.getImage() != null) { %>
                        <img src="data:image/jpeg;base64,<%= Base64.getEncoder().encodeToString(movie.getImage()) %>" alt="<%= movie.getTitle() %>">
                        <% } else { %>
                        <div class="no-poster">No Image</div>
                        <% } %>
                    </div>
                    <div class="movie-info">
                        <h3><a href="${pageContext.request.contextPath}/movie?id=<%= movie.getMovieId() %>"><%= movie.getTitle() %></a></h3>
                        <p class="movie-year"><%= yearFormat.format(movie.getReleaseDate()) %></p>
                    </div>
                    <div class="movie-actions">
                        <form action="${pageContext.request.contextPath}/watchlist/remove" method="post">
                            <input type="hidden" name="movieId" value="<%= movie.getMovieId() %>">
                            <button type="submit" class="btn btn-small btn-danger">Remove</button>
                        </form>
                    </div>
                </div>
                <%
                    }
                } else {
                %>
                <p class="no-results">Your watchlist is empty.</p>
                <% } %>
            </div>
        </div>
    </div>
</div>

<script>
    // Tab switching functionality
    const tabBtns = document.querySelectorAll('.tab-btn');
    const tabContents = document.querySelectorAll('.tab-content');

    tabBtns.forEach(btn => {
        btn.addEventListener('click', () => {
            const tabId = btn.getAttribute('data-tab');

            // Remove active class from all buttons and hide all contents
            tabBtns.forEach(b => b.classList.remove('active'));
            tabContents.forEach(c => c.style.display = 'none');

            // Add active class to clicked button and show corresponding content
            btn.classList.add('active');
            document.getElementById(tabId + '-tab').style.display = 'block';
        });
    });
</script>

<jsp:include page="includes/footer.jsp" />
