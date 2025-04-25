<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ page
import="com.movieratingsystem.models.UserModel" %> <%@ page
import="java.util.Map" %> <% UserModel user = (UserModel)
session.getAttribute("user"); if(user == null || user.getRole() !=
UserModel.Role.admin) { response.sendRedirect(request.getContextPath() +
"/login"); return; } Map<String, Integer>
  stats = (Map<String, Integer
    >) request.getAttribute("stats"); %>

    <jsp:include page="../includes/header.jsp">
      <jsp:param name="title" value="Admin Dashboard" />
    </jsp:include>

    <div class="admin-dashboard">
      <div class="admin-header">
        <h1>Admin Dashboard</h1>
      </div>

      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-value"><%= stats.get("totalMovies") %></div>
          <div class="stat-label">Total Movies</div>
        </div>

        <div class="stat-card">
          <div class="stat-value"><%= stats.get("totalUsers") %></div>
          <div class="stat-label">Registered Users</div>
        </div>

        <div class="stat-card">
          <div class="stat-value"><%= stats.get("totalReviews") %></div>
          <div class="stat-label">Reviews</div>
        </div>

        <div class="stat-card">
          <div class="stat-value"><%= stats.get("totalRatings") %></div>
          <div class="stat-label">Ratings</div>
        </div>
      </div>

      <div class="admin-sections">
        <div class="admin-section">
          <h2>Manage Movies</h2>
          <div class="admin-actions">
            <a
              href="${pageContext.request.contextPath}/admin/movies"
              class="btn btn-primary"
              >View All Movies</a
            >
            <a
              href="${pageContext.request.contextPath}/admin/movies/add"
              class="btn btn-primary"
              >Add New Movie</a
            >
          </div>
        </div>

        <div class="admin-section">
          <h2>Manage Users</h2>
          <div class="admin-actions">
            <a
              href="${pageContext.request.contextPath}/admin/users"
              class="btn btn-primary"
              >View All Users</a
            >
          </div>
        </div>

        <div class="admin-section">
          <h2>Manage Genres</h2>
          <div class="admin-actions">
            <a
              href="${pageContext.request.contextPath}/admin/genres"
              class="btn btn-primary"
              >View All Genres</a
            >
            <a
              href="${pageContext.request.contextPath}/admin/genres/add"
              class="btn btn-primary"
              >Add New Genre</a
            >
          </div>
        </div>

        <div class="admin-section">
          <h2>Manage Cast</h2>
          <div class="admin-actions">
            <a
              href="${pageContext.request.contextPath}/admin/cast"
              class="btn btn-primary"
              >View All Cast</a
            >
            <a
              href="${pageContext.request.contextPath}/admin/cast/add"
              class="btn btn-primary"
              >Add New Cast</a
            >
          </div>
        </div>

        <div class="admin-section">
          <h2>Content Moderation</h2>
          <div class="admin-actions">
            <a
              href="${pageContext.request.contextPath}/admin/manage-reviews"
              class="btn btn-primary"
              >Manage Reviews</a
            >
          </div>
        </div>
      </div>
    </div>

    <jsp:include page="../includes/footer.jsp" /> </String,
></String,>
