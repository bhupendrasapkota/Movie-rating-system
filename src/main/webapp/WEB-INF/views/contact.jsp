<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="includes/header.jsp">
  <jsp:param name="title" value="Contact Us" />
</jsp:include>

<div class="contact-hero">
  <h1>Contact Us</h1>
  <p>Have questions? We'd love to hear from you.</p>
</div>

<div class="contact-container">
  <div class="contact-info-bar">
    <div class="info-item">
      <i class="fas fa-envelope"></i>
      <span>info@movieratingsystem.com</span>
    </div>
    <div class="info-item">
      <i class="fas fa-phone"></i>
      <span>+977-9818000000</span>
    </div>
    <div class="info-item">
      <i class="fas fa-map-marker-alt"></i>
      <span>Itahari, Nepal</span>
    </div>
  </div>

  <div class="contact-form-wrapper">
    <% if(request.getAttribute("success") != null) { %>
    <div class="message success">
      <i class="fas fa-check-circle"></i>
      <%= request.getAttribute("success") %>
    </div>
    <% } %> <% if(request.getAttribute("error") != null) { %>
    <div class="message error">
      <i class="fas fa-exclamation-circle"></i>
      <%= request.getAttribute("error") %>
    </div>
    <% } %>

    <form
      action="${pageContext.request.contextPath}/contact/submit"
      method="post"
      class="contact-form"
    >
      <div class="form-grid">
        <div class="input-group">
          <i class="fas fa-user"></i>
          <input type="text" name="name" placeholder="Your Name" required />
        </div>

        <div class="input-group">
          <i class="fas fa-envelope"></i>
          <input type="email" name="email" placeholder="Your Email" required />
        </div>
      </div>

      <div class="input-group">
        <i class="fas fa-heading"></i>
        <input type="text" name="subject" placeholder="Subject" required />
      </div>

      <div class="input-group">
        <i class="fas fa-comment-alt"></i>
        <textarea
          name="message"
          placeholder="Your Message"
          rows="6"
          required
        ></textarea>
      </div>

      <button type="submit" class="submit-btn">
        <span>Send Message</span>
        <i class="fas fa-paper-plane"></i>
      </button>
    </form>
  </div>
</div>

<style>
  .contact-hero {
    background: #000000;
    padding: 80px 20px;
    text-align: center;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  }

  .contact-hero h1 {
    font-size: 3.5rem;
    margin: 0;
    color: #ffffff;
    font-weight: 700;
    letter-spacing: 1px;
    margin-bottom: 20px;
    text-transform: uppercase;
  }

  .contact-hero p {
    font-size: 1.2rem;
    color: rgba(255, 255, 255, 0.7);
    max-width: 600px;
    margin: 0 auto;
  }

  .contact-container {
    background: #000000;
    min-height: calc(100vh - 80px);
    padding: 0 20px 60px;
  }

  .contact-info-bar {
    max-width: 1200px;
    margin: -40px auto 60px;
    background: rgba(255, 255, 255, 0.03);
    border: 1px solid rgba(255, 255, 255, 0.1);
    border-radius: 4px;
    padding: 30px;
    display: flex;
    justify-content: space-around;
    flex-wrap: wrap;
    gap: 20px;
  }

  .info-item {
    display: flex;
    align-items: center;
    gap: 15px;
    color: #ffffff;
    padding: 10px 20px;
  }

  .info-item i {
    font-size: 24px;
    color: #ffffff;
  }

  .info-item span {
    font-size: 1.1rem;
    color: rgba(255, 255, 255, 0.9);
  }

  .contact-form-wrapper {
    max-width: 800px;
    margin: 0 auto;
    padding: 40px;
    background: rgba(255, 255, 255, 0.02);
    border: 1px solid rgba(255, 255, 255, 0.1);
    border-radius: 4px;
  }

  .message {
    padding: 16px;
    border-radius: 4px;
    margin-bottom: 30px;
    display: flex;
    align-items: center;
    gap: 12px;
    font-size: 1.1rem;
  }

  .message.success {
    background: rgba(255, 255, 255, 0.05);
    border: 1px solid rgba(255, 255, 255, 0.1);
    color: #ffffff;
  }

  .message.error {
    background: rgba(255, 255, 255, 0.05);
    border: 1px solid rgba(255, 255, 255, 0.1);
    color: #ffffff;
  }

  .form-grid {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 20px;
    margin-bottom: 20px;
  }

  .input-group {
    position: relative;
    margin-bottom: 20px;
  }

  .input-group i {
    position: absolute;
    left: 20px;
    top: 50%;
    transform: translateY(-50%);
    color: rgba(255, 255, 255, 0.4);
    font-size: 18px;
  }

  .input-group input,
  .input-group textarea {
    width: 100%;
    padding: 16px 20px 16px 50px;
    background: rgba(255, 255, 255, 0.03);
    border: 1px solid rgba(255, 255, 255, 0.1);
    border-radius: 4px;
    color: #ffffff;
    font-size: 1.1rem;
    transition: all 0.3s ease;
  }

  .input-group textarea {
    resize: vertical;
    min-height: 150px;
  }

  .input-group input:focus,
  .input-group textarea:focus {
    background: rgba(255, 255, 255, 0.05);
    border-color: rgba(255, 255, 255, 0.3);
    outline: none;
  }

  .input-group input::placeholder,
  .input-group textarea::placeholder {
    color: rgba(255, 255, 255, 0.3);
  }

  .submit-btn {
    width: 100%;
    padding: 18px;
    background: #ffffff;
    border: none;
    border-radius: 4px;
    color: #000000;
    font-size: 1.2rem;
    font-weight: 600;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
    transition: all 0.3s ease;
    text-transform: uppercase;
    letter-spacing: 1px;
  }

  .submit-btn:hover {
    background: rgba(255, 255, 255, 0.9);
    transform: translateY(-2px);
  }

  .submit-btn i {
    font-size: 1.2rem;
  }

  @media (max-width: 768px) {
    .contact-hero h1 {
      font-size: 2.5rem;
    }

    .contact-info-bar {
      flex-direction: column;
      align-items: center;
      text-align: center;
      padding: 20px;
      margin-top: 0;
    }

    .form-grid {
      grid-template-columns: 1fr;
    }

    .contact-form-wrapper {
      padding: 20px;
    }

    .info-item {
      width: 100%;
      justify-content: center;
    }
  }
</style>

<jsp:include page="includes/footer.jsp" />
