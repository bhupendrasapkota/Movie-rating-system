<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.movieratingsystem.models.UserModel" %>

<%
    UserModel user = (UserModel) session.getAttribute("user");
    if(user == null || user.getRole() != UserModel.Role.admin) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    
    UserModel editUser = (UserModel) request.getAttribute("editUser");
%>

<jsp:include page="../includes/header.jsp">
    <jsp:param name="title" value="Edit User" />
</jsp:include>

<div class="admin-container">
    <div class="admin-header">
        <h1>Edit User</h1>
    </div>
    
    <% if(request.getAttribute("error") != null) { %>
        <div class="error-message">
            <%= request.getAttribute("error") %>
        </div>
    <% } %>
    
    <form action="${pageContext.request.contextPath}/admin/users/edit" method="post" enctype="multipart/form-data" class="form-large">
        <input type="hidden" name="id" value="<%= editUser.getId() %>">
        
        <div class="form-group">
            <label for="name">Full Name</label>
            <input type="text" id="name" name="name" value="<%= editUser.getName() %>" required>
        </div>
        
        <div class="form-group">
            <label for="email">Email</label>
            <input type="email" id="email" name="email" value="<%= editUser.getEmail() %>" required>
        </div>
        
        <div class="form-group">
            <label for="role">Role</label>
            <select id="role" name="role" required>
                <option value="user" <%= editUser.getRole() == UserModel.Role.user ? "selected" : "" %>>User</option>
                <option value="admin" <%= editUser.getRole() == UserModel.Role.admin ? "selected" : "" %>>Admin</option>
            </select>
        </div>
        
        <div class="form-group">
            <label for="profileImage">Profile Image</label>
            <div class="current-image">
                <% if(editUser.getImage() != null) { %>
                    <img src="${pageContext.request.contextPath}/user/image?id=<%= editUser.getId() %>" alt="Current Profile Image" class="profile-preview">
                <% } else { %>
                    <div class="no-image">No profile image set</div>
                <% } %>
            </div>
            <input type="file" id="profileImage" name="profileImage" accept="image/*">
            <p class="form-hint">Leave empty to keep current image</p>
        </div>
        
        <div class="form-divider"></div>
        
        <div class="form-group">
            <label for="newPassword">New Password</label>
            <input type="password" id="newPassword" name="newPassword">
            <p class="form-hint">Leave empty to keep current password</p>
        </div>
        
        <div class="form-group">
            <label for="confirmPassword">Confirm New Password</label>
            <input type="password" id="confirmPassword" name="confirmPassword">
        </div>
        
        <div class="form-actions">
            <button type="submit" class="btn btn-primary">Update User</button>
            <a href="${pageContext.request.contextPath}/admin/users" class="btn btn-secondary">Cancel</a>
        </div>
    </form>
</div>

<jsp:include page="../includes/footer.jsp" />
