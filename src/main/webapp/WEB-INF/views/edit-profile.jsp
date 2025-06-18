<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ page
import="com.movieratingsystem.models.UserModel" %> <% UserModel user =
(UserModel) session.getAttribute("user"); %>

<jsp:include page="includes/header.jsp">
  <jsp:param name="title" value="Edit Profile" />
</jsp:include>

<div class="edit-profile-container">
  <form
    action="${pageContext.request.contextPath}/profile/edit"
    method="post"
    enctype="multipart/form-data"
    class="edit-profile-form"
  >
    <div class="edit-profile-header">
      <h1>Edit Profile</h1>
    </div>

    <% if(request.getAttribute("error") != null) { %>
    <div class="error-message">
      <i class="fas fa-exclamation-circle"></i>
      <%= request.getAttribute("error") %>
    </div>
    <% } %> <% if(request.getAttribute("success") != null) { %>
    <div class="success-message">
      <i class="fas fa-check-circle"></i>
      <%= request.getAttribute("success") %>
    </div>
    <% } %>

    <div class="form-group">
      <label for="name">Full Name</label>
      <input
        type="text"
        id="name"
        name="name"
        value="<%= user.getName() %>"
        required
      />
    </div>

    <div class="form-group">
      <label for="email">Email Address</label>
      <input
        type="email"
        id="email"
        name="email"
        value="<%= user.getEmail() %>"
        required
      />
    </div>

    <div class="form-group">
      <label for="profileImage">Profile Image</label>
      <div class="current-image">
        <% if(user.getImage() != null) { %>
        <img
          src="${pageContext.request.contextPath}/user/image?id=<%= user.getId() %>"
          alt="Current Profile Image"
          class="profile-preview"
        />
        <% } else { %>
        <div class="no-image">
          <i class="fas fa-user"></i>
        </div>
        <% } %>
      </div>
      <input
        type="file"
        id="profileImage"
        name="profileImage"
        accept="image/*"
      />
      <p class="form-hint">Upload a new image to change your profile picture</p>
    </div>

    <div class="form-divider"></div>

    <div class="form-group">
      <label for="currentPassword">Current Password</label>
      <input type="password" id="currentPassword" name="currentPassword" />
      <p class="form-hint">Required only if changing password</p>
    </div>

    <div class="form-group">
      <label for="newPassword">New Password</label>
      <input type="password" id="newPassword" name="newPassword" />
      <p class="form-hint">Leave blank to keep current password</p>
    </div>

    <div class="form-group">
      <label for="confirmPassword">Confirm New Password</label>
      <input type="password" id="confirmPassword" name="confirmPassword" />
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

<jsp:include page="includes/footer.jsp" />
