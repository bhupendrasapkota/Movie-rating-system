<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ page
import="com.movieratingsystem.models.UserModel" %> <% UserModel user =
(UserModel) session.getAttribute("user"); if(user == null || user.getRole() !=
UserModel.Role.admin) { response.sendRedirect(request.getContextPath() +
"/login"); return; } %>

<jsp:include page="../includes/header.jsp">
  <jsp:param name="title" value="Add Genre" />
</jsp:include>

<div class="admin-container">
  <div class="admin-header">
    <h1>Add New Genre</h1>
  </div>

  <% if(request.getAttribute("error") != null) { %>
  <div class="error-message"><%= request.getAttribute("error") %></div>
  <% } %> <% if(request.getAttribute("success") != null) { %>
  <div class="success-message"><%= request.getAttribute("success") %></div>
  <% } %>

  <form
    action="${pageContext.request.contextPath}/admin/genres/add"
    method="post"
    class="form-large genre-form"
  >
    <div class="form-group">
      <label for="genreName">Genre Name</label>
      <input
        type="text"
        id="genreName"
        name="genreName"
        required
        placeholder="Enter genre name"
      />
    </div>

    <div class="form-group">
      <label for="description">Description</label>
      <textarea
        id="description"
        name="description"
        rows="4"
        placeholder="Enter genre description"
      ></textarea>
    </div>

    <div class="form-actions">
      <button type="submit" class="btn btn-primary">Add Genre</button>
      <a
        href="${pageContext.request.contextPath}/admin/genres"
        class="btn btn-secondary"
        >Cancel</a
      >
    </div>
  </form>
</div>

<jsp:include page="../includes/footer.jsp" />
