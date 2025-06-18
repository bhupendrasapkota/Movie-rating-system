<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Register - Movie Rating System</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/style.css"
    />
    <link
      rel="stylesheet"
      href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css"
    />
    <style>
      .auth-wrapper {
        max-width: 500px !important;
      }

      .register-form .form-row {
        display: flex;
        gap: 15px;
        margin-bottom: 15px;
      }

      .register-form .form-group {
        flex: 1;
        margin-bottom: 20px;
      }

      .auth-container {
        background: rgba(17, 17, 17, 0.5);
      }

      .register-form .single-row {
        margin-bottom: 20px;
      }

      .file-input-label {
        color: rgba(255, 255, 255, 0.9) !important;
      }

      .file-input-label i {
        color: rgba(255, 255, 255, 0.9) !important;
      }

      .file-name {
        color: rgba(255, 255, 255, 0.7) !important;
      }

      .file-name.has-file {
        color: rgba(255, 255, 255, 0.9) !important;
      }

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
        .register-form .form-row {
          flex-direction: column;
          gap: 15px;
        }

        .register-form .form-group {
          margin-bottom: 15px;
        }

        .auth-wrapper {
          max-width: 100% !important;
          padding: 15px;
        }

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
          <h1>Create Account</h1>
          <p>Join our movie rating community</p>
        </div>

        <% if(request.getAttribute("error") != null) { %>
        <div class="error-message">
          <i class="fas fa-exclamation-circle"></i>
          <%= request.getAttribute("error") %>
        </div>
        <% } %>

        <form
          action="${pageContext.request.contextPath}/register"
          method="post"
          class="auth-form register-form"
          enctype="multipart/form-data"
        >
          <div class="form-row">
            <div class="form-group">
              <div class="input-group">
                <span class="input-icon">
                  <i class="fas fa-user"></i>
                </span>
                <input
                  type="text"
                  id="name"
                  name="name"
                  placeholder="Enter your full name"
                  required
                />
              </div>
            </div>

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
          </div>

          <div class="form-row">
            <div class="form-group">
              <div class="input-group">
                <span class="input-icon">
                  <i class="fas fa-lock"></i>
                </span>
                <input
                  type="password"
                  id="password"
                  name="password"
                  placeholder="Create a password"
                  required
                />
                <button
                  type="button"
                  class="password-toggle"
                  onclick="togglePassword('password')"
                >
                  <i class="fas fa-eye"></i>
                </button>
              </div>
            </div>

            <div class="form-group">
              <div class="input-group">
                <span class="input-icon">
                  <i class="fas fa-lock"></i>
                </span>
                <input
                  type="password"
                  id="confirmPassword"
                  name="confirmPassword"
                  placeholder="Confirm password"
                  required
                />
                <button
                  type="button"
                  class="password-toggle"
                  onclick="togglePassword('confirmPassword')"
                >
                  <i class="fas fa-eye"></i>
                </button>
              </div>
            </div>
          </div>

          <div class="single-row">
            <div class="form-group">
              <div class="file-input-wrapper">
                <div class="file-input-label">
                  <i class="fas fa-camera"></i>
                  <span>Profile Image (Optional)</span>
                </div>
                <input
                  type="file"
                  id="profileImage"
                  name="profileImage"
                  accept="image/*"
                  onchange="updateFileName(this)"
                />
                <div class="file-name" id="fileName">No file chosen</div>
              </div>
            </div>
          </div>

          <div class="form-actions">
            <button type="submit" class="btn btn-primary btn-block">
              Create Account
            </button>
          </div>

          <div class="auth-divider">
            <span>Already have an account?</span>
          </div>

          <div class="auth-links">
            <a
              href="${pageContext.request.contextPath}/login"
              class="btn btn-outline btn-block"
            >
              <i class="fas fa-sign-in-alt"></i>
              Sign In
            </a>
          </div>
        </form>
      </div>
    </div>

    <script>
      function togglePassword(inputId) {
        const passwordInput = document.getElementById(inputId);
        const toggleButton =
          passwordInput.nextElementSibling.querySelector("i");

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

      function updateFileName(input) {
        const fileName = document.getElementById("fileName");
        if (input.files.length > 0) {
          fileName.textContent = input.files[0].name;
          fileName.classList.add("has-file");
        } else {
          fileName.textContent = "No file chosen";
          fileName.classList.remove("has-file");
        }
      }
    </script>
  </body>
</html>
