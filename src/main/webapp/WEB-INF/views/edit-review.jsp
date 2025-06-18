<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ page
import="com.movieratingsystem.models.Review" %> <%@ page
import="com.movieratingsystem.models.UserModel" %> <%@ page
import="java.text.SimpleDateFormat" %> <% Review review = (Review)
request.getAttribute("review"); if (review == null) {
response.sendRedirect(request.getContextPath() + "/profile"); return; }
SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy"); %>

<jsp:include page="includes/header.jsp">
  <jsp:param name="title" value="Edit Review" />
</jsp:include>

<div class="edit-profile-container">
  <form
    action="${pageContext.request.contextPath}/review/edit"
    method="post"
    class="edit-profile-form"
  >
    <div class="edit-profile-header">
      <h1>Edit Review</h1>
      <p class="movie-title">Movie: <%= review.getMovie().getTitle() %></p>
    </div>

    <% if(request.getAttribute("error") != null) { %>
    <div class="error-message">
      <i class="fas fa-exclamation-circle"></i>
      <%= request.getAttribute("error") %>
    </div>
    <% } %>

    <input type="hidden" name="reviewId" value="<%= review.getReviewId() %>" />

    <div class="form-group">
      <label for="comment">Your Review</label>
      <textarea id="comment" name="comment" rows="6" required>
<%= review.getComment() %></textarea
      >
      <p class="form-hint">
        Last updated: <%= dateFormat.format(review.getReviewDate()) %>
      </p>
    </div>

    <div class="form-actions">
      <button type="submit" class="btn btn-primary">
        <i class="fas fa-save"></i>
        Save Changes
      </button>
      <a
        href="${pageContext.request.contextPath}/profile"
        class="btn btn-secondary"
      >
        <i class="fas fa-times"></i>
        Cancel
      </a>
    </div>
  </form>
</div>

<style>
  .movie-title {
    color: rgba(255, 255, 255, 0.7);
    font-size: 1.1rem;
    margin-top: 0.5rem;
  }

  textarea {
    width: 100%;
    padding: 1rem;
    background: rgba(255, 255, 255, 0.05);
    border: 1px solid rgba(255, 255, 255, 0.1);
    border-radius: 8px;
    color: #ffffff;
    font-size: 1rem;
    resize: vertical;
    transition: all 0.3s ease;
  }

  textarea:focus {
    outline: none;
    border-color: rgba(255, 255, 255, 0.3);
    box-shadow: 0 0 0 2px rgba(255, 255, 255, 0.1);
  }
</style>

<jsp:include page="includes/footer.jsp" />
