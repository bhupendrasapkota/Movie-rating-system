<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ page
import="com.movieratingsystem.models.UserModel" %> <% UserModel currentUser =
(UserModel) session.getAttribute("user"); boolean isAdmin = currentUser != null
&& currentUser.getRole() == UserModel.Role.admin; %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>${param.title} - Movie Rating System</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/style.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
    />
  </head>
  <body>
    <header class="main-header">
      <div class="container">
        <div class="header-content">
          <div class="logo">
            <a href="${pageContext.request.contextPath}/"
              ><i class="fas fa-film"></i> Movie</a
            >
          </div>
          <nav class="main-nav">
            <ul>
              <li>
                <a href="${pageContext.request.contextPath}/"
                  ><i class="fas fa-home"></i> Home</a
                >
              </li>
              <li>
                <a href="${pageContext.request.contextPath}/movies"
                  ><i class="fas fa-film"></i> Movies</a
                >
              </li>
              <% if (currentUser != null) { %>
              <li>
                <a href="${pageContext.request.contextPath}/watchlist"
                  ><i class="fas fa-list"></i> My Watchlist</a
                >
              </li>
              <% if (isAdmin) { %>
              <li>
                <a href="${pageContext.request.contextPath}/admin/dashboard"
                  ><i class="fas fa-shield-alt"></i> Admin</a
                >
              </li>
              <% } %>
              <li class="profile-menu">
                <div class="profile-trigger">
                  <div class="profile-info">
                    <% if(currentUser.getImage() != null) { %>
                    <img
                      src="${pageContext.request.contextPath}/user/image?id=<%= currentUser.getId() %>"
                      alt="Profile"
                      class="profile-img-small"
                    />
                    <% } else { %>
                    <div class="profile-img-small profile-initial">
                      <%= currentUser.getName().charAt(0) %>
                    </div>
                    <% } %>
                    <span class="profile-name"
                      ><%= currentUser.getName() %></span
                    >
                  </div>
                  <div class="profile-arrow"></div>
                </div>
                <div class="dropdown-menu">
                  <div class="dropdown-header">
                    <div class="user-info">
                      <div class="user-avatar">
                        <% if(currentUser.getImage() != null) { %>
                        <img
                          src="${pageContext.request.contextPath}/user/image?id=<%= currentUser.getId() %>"
                          alt="Profile"
                        />
                        <% } else { %>
                        <div class="profile-initial">
                          <%= currentUser.getName().charAt(0) %>
                        </div>
                        <% } %>
                      </div>
                      <div class="user-details">
                        <div class="user-name">
                          <%= currentUser.getName() %>
                        </div>
                        <div class="user-email">
                          <%= currentUser.getEmail() %>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div class="dropdown-body">
                    <a
                      href="${pageContext.request.contextPath}/profile"
                      class="menu-item"
                    >
                      <i class="fas fa-user"></i>
                      <span>Profile</span>
                    </a>
                    <div class="menu-divider"></div>
                    <a
                      href="${pageContext.request.contextPath}/logout"
                      class="menu-item logout"
                    >
                      <i class="fas fa-sign-out-alt"></i>
                      <span>Logout</span>
                    </a>
                  </div>
                </div>
              </li>
              <% } else { %>
              <li>
                <a href="${pageContext.request.contextPath}/login"
                  ><i class="fas fa-sign-in-alt"></i> Login</a
                >
              </li>
              <li>
                <a href="${pageContext.request.contextPath}/register"
                  ><i class="fas fa-user-plus"></i> Register</a
                >
              </li>
              <% } %>
            </ul>
          </nav>
          <div class="mobile-menu-toggle">
            <span></span>
            <span></span>
            <span></span>
          </div>
        </div>
      </div>
    </header>
    <div class="container main-content"></div>
  </body>
</html>