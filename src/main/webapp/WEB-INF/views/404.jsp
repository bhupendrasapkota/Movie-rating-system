<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="includes/header.jsp">
  <jsp:param name="title" value="Page Not Found" />
</jsp:include>

<div class="error-container">
  <div class="error-content">
    <div class="error-icon">
      <i class="fas fa-ghost"></i>
    </div>
    <h1>404</h1>
    <h2>Page Not Found</h2>
    <p>
      Oops! The page you're looking for seems to have vanished into thin air.
    </p>

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
    color: rgba(255, 255, 255, 0.7);
    margin-bottom: 20px;
    animation: float 3s ease-in-out infinite;
  }

  .error-content h1 {
    font-size: 120px;
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
    margin: 10px 0 20px;
    font-weight: 600;
  }

  .error-content p {
    color: rgba(255, 255, 255, 0.7);
    font-size: 18px;
    line-height: 1.6;
    margin-bottom: 40px;
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

  @keyframes float {
    0%,
    100% {
      transform: translateY(0);
    }
    50% {
      transform: translateY(-20px);
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
      font-size: 80px;
    }

    .error-content h2 {
      font-size: 24px;
    }

    .error-content p {
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
