<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ page
import="com.movieratingsystem.models.Movie" %> <%@ page
import="com.movieratingsystem.models.Genre" %> <%@ page
import="com.movieratingsystem.models.Cast" %> <%@ page
import="com.movieratingsystem.models.Review" %> <%@ page
import="com.movieratingsystem.models.UserModel" %> <%@ page
import="java.util.List" %> <%@ page import="java.text.SimpleDateFormat" %> <%@
page import="java.util.Base64" %> <% Movie movie = (Movie)
request.getAttribute("movie"); List<Genre>
  genres = (List<Genre
    >) request.getAttribute("genres"); List<Cast>
      cast = (List<Cast
        >) request.getAttribute("cast"); List<Review>
          reviews = (List<Review
            >) request.getAttribute("reviews"); Double averageRating = (Double)
            request.getAttribute("averageRating"); Boolean inWatchlist =
            (Boolean) request.getAttribute("inWatchlist"); UserModel currentUser
            = (UserModel) session.getAttribute("user"); SimpleDateFormat
            dateFormat = new SimpleDateFormat("MMMM dd, yyyy"); SimpleDateFormat
            yearFormat = new SimpleDateFormat("yyyy"); %>

            <jsp:include page="includes/header.jsp">
              <jsp:param name="title" value="${movie.title}" />
            </jsp:include>

            <div class="movie-details">
              <div class="movie-header">
                <div class="movie-poster-large">
                  <% if(movie.getImage() != null) { %>
                  <img
                    src="data:image/jpeg;base64,<%= Base64.getEncoder().encodeToString(movie.getImage()) %>"
                    alt="<%= movie.getTitle() %>"
                  />
                  <% } else { %>
                  <div class="no-poster-large">No Image</div>
                  <% } %>
                </div>

                <div class="movie-info-large">
                  <h1>
                    <%= movie.getTitle() %>
                    <span class="movie-year"
                      >(<%= yearFormat.format(movie.getReleaseDate()) %>)</span
                    >
                  </h1>

                  <div class="movie-meta">
                    <div class="release-date">
                      <span class="meta-label">Release Date:</span>
                      <span class="meta-value"
                        ><%= dateFormat.format(movie.getReleaseDate()) %></span
                      >
                    </div>

                    <div class="runtime">
                      <span class="meta-label">Runtime:</span>
                      <span class="meta-value"
                        ><%= movie.getMinutes() %> minutes</span
                      >
                    </div>

                    <div class="genres">
                      <span class="meta-label">Genres:</span>
                      <span class="meta-value">
                        <% if(genres != null && !genres.isEmpty()) { for(int i =
                        0; i < genres.size(); i++) {
                        out.print(genres.get(i).getGenreName()); if(i <
                        genres.size() - 1) { out.print(", "); } } } else {
                        out.print("N/A"); } %>
                      </span>
                    </div>
                  </div>

                  <div class="movie-rating-large">
                    <div class="rating-circle">
                      <span class="rating-value"
                        ><%= averageRating != null ? String.format("%.1f",
                        averageRating) : "N/A" %></span
                      >
                      <% if(averageRating != null) { %>
                      <span class="rating-max">/10</span>
                      <% } %>
                    </div>
                    <div class="rating-label">User Rating</div>
                  </div>

                  <% if(currentUser != null) { %>
                  <div class="movie-actions">
                    <% if(inWatchlist != null && inWatchlist) { %>
                    <form
                      action="${pageContext.request.contextPath}/watchlist/remove"
                      method="post"
                    >
                      <input
                        type="hidden"
                        name="movieId"
                        value="<%= movie.getMovieId() %>"
                      />
                      <button type="submit" class="btn btn-secondary">
                        Remove from Watchlist
                      </button>
                    </form>
                    <% } else { %>
                    <form
                      action="${pageContext.request.contextPath}/watchlist/add"
                      method="post"
                    >
                      <input
                        type="hidden"
                        name="movieId"
                        value="<%= movie.getMovieId() %>"
                      />
                      <button type="submit" class="btn btn-primary">
                        Add to Watchlist
                      </button>
                    </form>
                    <% } %>

                    <button class="btn btn-primary" id="rateMovieBtn">
                      Rate Movie
                    </button>
                  </div>

                  <div
                    id="ratingForm"
                    class="rating-form"
                    style="display: none"
                  >
                    <form
                      action="${pageContext.request.contextPath}/rating/add"
                      method="post"
                    >
                      <input
                        type="hidden"
                        name="movieId"
                        value="<%= movie.getMovieId() %>"
                      />
                      <div class="rating-stars">
                        <% for(int i = 1; i <= 10; i++) { %>
                        <input
                          type="radio"
                          id="star<%= i %>"
                          name="rating"
                          value="<%= i %>"
                        />
                        <label for="star<%= i %>"><%= i %></label>
                        <% } %>
                      </div>
                      <button type="submit" class="btn btn-primary">
                        Submit Rating
                      </button>
                    </form>
                  </div>
                  <% } %>
                </div>
              </div>

              <div class="movie-tabs">
                <div class="tabs-header">
                  <button class="tab-btn active" data-tab="cast">Cast</button>
                  <button class="tab-btn" data-tab="reviews">Reviews</button>
                </div>

                <div class="tab-content" id="cast-tab">
                  <h2>Cast</h2>

                  <div class="cast-grid">
                    <% if(cast != null && !cast.isEmpty()) { for(Cast member :
                    cast) { %>
                    <div class="cast-card">
                      <div class="cast-photo">
                        <% if(member.getPhoto() != null) { %>
                        <img
                          src="data:image/jpeg;base64,<%= Base64.getEncoder().encodeToString(member.getPhoto()) %>"
                          alt="<%= member.getCastName() %>"
                        />
                        <% } else { %>
                        <div class="no-photo">No Photo</div>
                        <% } %>
                      </div>
                      <div class="cast-info">
                        <h3><%= member.getCastName() %></h3>
                        <p class="character-name">
                          <%= member.getCharName() %>
                        </p>
                      </div>
                    </div>
                    <% } } else { %>
                    <p class="no-results">No cast information available.</p>
                    <% } %>
                  </div>
                </div>

                <div class="tab-content" id="reviews-tab" style="display: none">
                  <h2>Reviews</h2>

                  <% if(currentUser != null) { %>
                  <div class="add-review">
                    <h3>Write a Review</h3>
                    <form
                      action="${pageContext.request.contextPath}/review/add"
                      method="post"
                      class="review-form"
                    >
                      <input
                        type="hidden"
                        name="movieId"
                        value="<%= movie.getMovieId() %>"
                      />
                      <div class="form-group">
                        <textarea
                          name="comment"
                          rows="4"
                          placeholder="Share your thoughts about this movie..."
                          required
                        ></textarea>
                      </div>
                      <div class="form-actions">
                        <button type="submit" class="btn btn-primary">
                          Submit Review
                        </button>
                      </div>
                    </form>
                  </div>
                  <% } %>

                  <div class="reviews-list">
                    <% if(reviews != null && !reviews.isEmpty()) { for(Review
                    review : reviews) { %>
                    <div class="review-card">
                      <div class="review-header">
                        <div class="reviewer-info">
                          <div class="reviewer-avatar">
                            <% if(review.getUser().getImage() != null) { %>
                            <img
                              src="${pageContext.request.contextPath}/user/image?id=<%= review.getUser().getId() %>"
                              alt="<%= review.getUser().getName() %>"
                            />
                            <% } else { %>
                            <span
                              ><%= review.getUser().getName().charAt(0) %></span
                            >
                            <% } %>
                          </div>
                          <div class="reviewer-name">
                            <%= review.getUser().getName() %>
                          </div>
                        </div>
                        <div class="review-date">
                          <%= dateFormat.format(review.getReviewDate()) %>
                        </div>
                      </div>
                      <div class="review-content">
                        <p><%= review.getComment() %></p>
                      </div>
                    </div>
                    <% } } else { %>
                    <p class="no-results">
                      No reviews available for this movie. Be the first to write
                      one!
                    </p>
                    <% } %>
                  </div>
                </div>
              </div>
            </div>

            <script>
              // Tab switching functionality
              const tabBtns = document.querySelectorAll(".tab-btn");
              const tabContents = document.querySelectorAll(".tab-content");

              tabBtns.forEach((btn) => {
                btn.addEventListener("click", () => {
                  const tabId = btn.getAttribute("data-tab");

                  // Remove active class from all buttons and hide all contents
                  tabBtns.forEach((b) => b.classList.remove("active"));
                  tabContents.forEach((c) => (c.style.display = "none"));

                  // Add active class to clicked button and show corresponding content
                  btn.classList.add("active");
                  document.getElementById(tabId + "-tab").style.display =
                    "block";
                });
              });

              // Rating form toggle
              const rateMovieBtn = document.getElementById("rateMovieBtn");
              const ratingForm = document.getElementById("ratingForm");

              if (rateMovieBtn && ratingForm) {
                rateMovieBtn.addEventListener("click", () => {
                  ratingForm.style.display =
                    ratingForm.style.display === "none" ? "block" : "none";
                });
              }
            </script>

            <jsp:include
              page="includes/footer.jsp"
            /> </Review></Review></Cast></Cast></Genre
></Genre>
