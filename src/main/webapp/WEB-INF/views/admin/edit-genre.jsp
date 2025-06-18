<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ page
import="com.movieratingsystem.models.UserModel" %> <%@ page
import="com.movieratingsystem.models.Genre" %> <% UserModel user = (UserModel)
session.getAttribute("user"); if(user == null || user.getRole() !=
UserModel.Role.admin) { response.sendRedirect(request.getContextPath() +
"/login"); return; } Genre genre = (Genre) request.getAttribute("genre"); %>

<jsp:include page="../includes/header.jsp">
  <jsp:param name="title" value="Edit Genre" />
</jsp:include>

<div class="admin-container">
  <div class="admin-header">
    <h1>Edit Genre</h1>
  </div>

  <% if(request.getAttribute("error") != null) { %>
  <div class="error-message"><%= request.getAttribute("error") %></div>
  <% } %>

  <form
    action="${pageContext.request.contextPath}/admin/genres/edit"
    method="post"
    class="form-large"
  >
    <input type="hidden" name="genreId" value="<%= genre.getGenreId() %>" />

    <div class="form-group">
      <label for="genreName">Genre Name</label>
      <input
        type="text"
        id="genreName"
        name="genreName"
        value="<%= genre.getGenreName() %>"
        required
      />
    </div>

    <div class="form-group">
      <label for="description">Description</label>
      <textarea id="description" name="description" rows="4">
<%= genre.getDescription() != null ? genre.getDescription() : "" %></textarea
      >
    </div>

    <div class="form-actions">
      <button type="submit" class="btn btn-primary">Update Genre</button>
      <a
        href="${pageContext.request.contextPath}/admin/genres"
        class="btn btn-secondary"
        >Cancel</a
      >
    </div>
  </form>
</div>

<jsp:include page="../includes/footer.jsp" />
