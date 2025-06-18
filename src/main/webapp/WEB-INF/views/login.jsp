<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Login - Movie Rating System</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/style.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
    />
    <style>
      .back-home {
        position: fixed;
        top: 20px;
        left: 20px;
        color: rgba(255, 255, 255, 0.9);
        text-decoration: none;
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 16px;
        padding: 10px 15px;
        border-radius: 8px;
        background: rgba(255, 255, 255, 0.1);
        backdrop-filter: blur(10px);
        -webkit-backdrop-filter: blur(10px);
        border: 1px solid rgba(255, 255, 255, 0.1);
        transition: all 0.3s ease;
      }

      .back-home:hover {
        background: rgba(255, 255, 255, 0.2);
        transform: translateX(-3px);
      }

      .back-home i {
        font-size: 18px;
      }

      @media (max-width: 576px) {
        .back-home {
          top: 10px;
          left: 10px;
          padding: 8px 12px;
          font-size: 14px;
        }
      }
    </style>
  </head>
  <body class="auth-page">
    <a href="${pageContext.request.contextPath}/" class="back-home">
      <i class="fas fa-arrow-left"></i>
      <span>Back to Home</span>
    </a>
    <div class="auth-wrapper">
      <div class="auth-container">
        <div class="auth-header">
          <div class="auth-logo">
            <i class="fas fa-film"></i>
          </div>
          <h1>Welcome Back</h1>
          <p>Sign in to your account to continue</p>
        </div>

        <% if(request.getAttribute("error") != null) { %>
        <div class="error-message">
          <i class="fas fa-exclamation-circle"></i>
          <%= request.getAttribute("error") %>
        </div>
        <% } %>

        <form
          action="${pageContext.request.contextPath}/login"
          method="post"
          class="auth-form"
        >
          <div class="form-group">
            <div class="input-group">
              <span class="input-icon">
                <i class="fas fa-envelope"></i>
              </span>
              <input
                type="email"
                id="email"
                name="email"
                placeholder="Enter your email"
                required
              />
            </div>
          </div>

          <div class="form-group">
            <div class="input-group">
              <span class="input-icon">
                <i class="fas fa-lock"></i>
              </span>
              <input
                type="password"
                id="password"
                name="password"
                placeholder="Enter your password"
                required
              />
              <button
                type="button"
                class="password-toggle"
                onclick="togglePassword()"
              >
                <i class="fas fa-eye"></i>
              </button>
            </div>
          </div>

          <div class="form-options">
            <label class="remember-me">
              <input type="checkbox" name="remember" id="remember" />
              <span class="checkmark"></span>
              Remember me
            </label>
          </div>

          <div class="form-actions">
            <button type="submit" class="btn btn-primary btn-block">
              Sign In
            </button>
          </div>

          <div class="auth-divider">
            <span>Don't have an account?</span>
          </div>

          <div class="auth-links">
            <a
              href="${pageContext.request.contextPath}/register"
              class="btn btn-outline btn-block"
            >
              <i class="fas fa-user-plus"></i>
              Create Account
            </a>
          </div>
        </form>
      </div>
    </div>

    <script>
      function togglePassword() {
        const passwordInput = document.getElementById("password");
        const toggleButton = document.querySelector(".password-toggle i");

        if (passwordInput.type === "password") {
          passwordInput.type = "text";
          toggleButton.classList.remove("fa-eye");
          toggleButton.classList.add("fa-eye-slash");
        } else {
          passwordInput.type = "password";
          toggleButton.classList.remove("fa-eye-slash");
          toggleButton.classList.add("fa-eye");
        }
      }
    </script>
  </body>
</html>
