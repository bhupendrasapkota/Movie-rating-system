<%@ page contentType="text/html;charset=UTF-8" language="java"
isErrorPage="true" %>

<jsp:include page="includes/header.jsp">
  <jsp:param name="title" value="Error" />
</jsp:include>

<div class="error-container">
  <div class="error-content">
    <div class="error-icon">
      <i class="fas fa-exclamation-triangle"></i>
    </div>
    <h1>Oops!</h1>
    <h2>Something Went Wrong</h2>

    <div class="error-message">
      <% if(request.getAttribute("errorMessage") != null) { %>
      <p><%= request.getAttribute("errorMessage") %></p>
      <% } else if(exception != null) { %>
      <p><%= exception.getMessage() %></p>
      <% } else { %>
      <p>An unexpected error occurred. Please try again later.</p>
      <% } %>
    </div>

    <div class="error-actions">
      <a href="${pageContext.request.contextPath}/" class="btn btn-primary">
        <i class="fas fa-home"></i>
        Return Home
      </a>
      <a href="javascript:history.back()" class="btn btn-secondary">
        <i class="fas fa-arrow-left"></i>
        Go Back
      </a>
    </div>
  </div>
</div>

<style>
  .error-container {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: calc(100vh - 80px);
    background: #000000;
    padding: 20px;
  }

  .error-content {
    text-align: center;
    max-width: 600px;
    padding: 60px 40px;
    background: rgba(255, 255, 255, 0.05);
    border: 1px solid rgba(255, 255, 255, 0.1);
    border-radius: 20px;
    backdrop-filter: blur(10px);
    -webkit-backdrop-filter: blur(10px);
    box-shadow: 0 15px 35px rgba(0, 0, 0, 0.2);
    animation: fadeIn 0.6s ease-out;
  }

  .error-icon {
    font-size: 80px;
    color: #ff3b30;
    margin-bottom: 20px;
    animation: pulse 2s ease-in-out infinite;
  }

  .error-content h1 {
    font-size: 80px;
    font-weight: 800;
    color: #ffffff;
    margin: 0;
    line-height: 1;
    background: linear-gradient(
      135deg,
      #ffffff 0%,
      rgba(255, 255, 255, 0.4) 100%
    );
    background-clip: text;
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    animation: glow 2s ease-in-out infinite alternate;
  }

  .error-content h2 {
    font-size: 28px;
    color: rgba(255, 255, 255, 0.9);
    margin: 10px 0 30px;
    font-weight: 600;
  }

  .error-message {
    background: rgba(255, 59, 48, 0.1);
    border: 1px solid rgba(255, 59, 48, 0.2);
    border-radius: 12px;
    padding: 20px;
    margin-bottom: 40px;
  }

  .error-message p {
    color: rgba(255, 255, 255, 0.8);
    font-size: 18px;
    line-height: 1.6;
    margin: 0;
  }

  .error-actions {
    display: flex;
    justify-content: center;
    gap: 20px;
  }

  .error-actions .btn {
    padding: 12px 30px;
    font-size: 16px;
    display: flex;
    align-items: center;
    gap: 10px;
    transition: all 0.3s ease;
  }

  .error-actions .btn i {
    font-size: 18px;
  }

  .error-actions .btn-primary {
    background: #ffffff;
    color: #000000;
  }

  .error-actions .btn-primary:hover {
    transform: translateY(-2px);
    box-shadow: 0 5px 15px rgba(255, 255, 255, 0.2);
  }

  .error-actions .btn-secondary {
    background: transparent;
    color: #ffffff;
    border: 2px solid rgba(255, 255, 255, 0.2);
  }

  .error-actions .btn-secondary:hover {
    background: rgba(255, 255, 255, 0.1);
    transform: translateY(-2px);
  }

  @keyframes pulse {
    0%,
    100% {
      transform: scale(1);
      opacity: 1;
    }
    50% {
      transform: scale(1.1);
      opacity: 0.8;
    }
  }

  @keyframes glow {
    from {
      text-shadow: 0 0 10px rgba(255, 255, 255, 0.3),
        0 0 20px rgba(255, 255, 255, 0.3), 0 0 30px rgba(255, 255, 255, 0.3);
    }
    to {
      text-shadow: 0 0 20px rgba(255, 255, 255, 0.5),
        0 0 30px rgba(255, 255, 255, 0.5), 0 0 40px rgba(255, 255, 255, 0.5);
    }
  }

  @keyframes fadeIn {
    from {
      opacity: 0;
      transform: translateY(20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }

  @media (max-width: 768px) {
    .error-content {
      padding: 40px 20px;
    }

    .error-content h1 {
      font-size: 60px;
    }

    .error-content h2 {
      font-size: 24px;
    }

    .error-message p {
      font-size: 16px;
    }

    .error-actions {
      flex-direction: column;
    }

    .error-actions .btn {
      width: 100%;
      justify-content: center;
    }
  }
</style>

<jsp:include page="includes/footer.jsp" />
