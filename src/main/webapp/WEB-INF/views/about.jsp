<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<jsp:include page="includes/header.jsp">
  <jsp:param name="title" value="About Us" />
</jsp:include>

<div class="about-hero">
  <h1>About Us</h1>
  <p>Building the future of movie discovery and rating</p>
</div>

<div class="about-container">
  <section class="mission-section">
    <div class="section-content">
      <h2>Our Mission</h2>
      <p>
        Welcome to the Movie Rating System, an innovative platform born from our
        coursework project at Itahari International College, affiliated with
        London Metropolitan University. Our mission is to revolutionize how
        movie enthusiasts discover, rate, and engage with films through a modern
        and intuitive platform.
      </p>
    </div>
  </section>

  <section class="features-section">
    <h2>Core Features</h2>
    <div class="features-grid">
      <div class="feature-card">
        <i class="fas fa-film"></i>
        <h3>Movie Database</h3>
        <p>
          Comprehensive collection of movies with detailed information and
          metadata
        </p>
      </div>

      <div class="feature-card">
        <i class="fas fa-star"></i>
        <h3>Rating System</h3>
        <p>
          Sophisticated rating algorithm with user-based scoring and reviews
        </p>
      </div>

      <div class="feature-card">
        <i class="fas fa-list"></i>
        <h3>Watchlist</h3>
        <p>Personalized watchlist management with smart recommendations</p>
      </div>

      <div class="feature-card">
        <i class="fas fa-users"></i>
        <h3>Community</h3>
        <p>Interactive community features for sharing reviews and opinions</p>
      </div>
    </div>
  </section>

  <section class="team-section">
    <h2>Meet Our Team</h2>
    <div class="team-grid">
      <div class="team-member">
        <div class="member-photo">
          <img
            src="${pageContext.request.contextPath}/images/team/aasraya.jpg"
            alt="Aasraya Chapangain"
            onerror="this.src='${pageContext.request.contextPath}/images/default-avatar.png'"
          />
        </div>
        <h3>Aasraya Chapangain</h3>
        <p class="role">Full Stack Developer</p>
        <p class="contribution">
          Led the backend development and database design
        </p>
      </div>

      <div class="team-member">
        <div class="member-photo">
          <img
            src="${pageContext.request.contextPath}/images/team/arpan.jpg"
            alt="Arpan Upreti"
            onerror="this.src='${pageContext.request.contextPath}/images/default-avatar.png'"
          />
        </div>
        <h3>Arpan Upreti</h3>
        <p class="role">Frontend Developer</p>
        <p class="contribution">
          Implemented user interface and responsive design
        </p>
      </div>

      <div class="team-member">
        <div class="member-photo">
          <img
            src="${pageContext.request.contextPath}/images/team/jenish.jpg"
            alt="Jenish Shrestha"
            onerror="this.src='${pageContext.request.contextPath}/images/default-avatar.png'"
          />
        </div>
        <h3>Jenish Shrestha</h3>
        <p class="role">Backend Developer</p>
        <p class="contribution">
          Developed API endpoints and authentication system
        </p>
      </div>

      <div class="team-member">
        <div class="member-photo">
          <img
            src="${pageContext.request.contextPath}/images/team/bhupendra.jpg"
            alt="Bhupendra Sapkota"
            onerror="this.src='${pageContext.request.contextPath}/images/default-avatar.png'"
          />
        </div>
        <h3>Bhupendra Sapkota</h3>
        <p class="role">UI/UX Designer</p>
        <p class="contribution">Created user experience and interface design</p>
      </div>

      <div class="team-member">
        <div class="member-photo">
          <img
            src="${pageContext.request.contextPath}/images/team/sarvik.jpg"
            alt="Sarvik Khadka"
            onerror="this.src='${pageContext.request.contextPath}/images/default-avatar.png'"
          />
        </div>
        <h3>Sarvik Khadka</h3>
        <p class="role">Quality Assurance</p>
        <p class="contribution">Managed testing and quality assurance</p>
      </div>
    </div>
  </section>

  <section class="tech-section">
    <h2>Technology Stack</h2>
    <div class="tech-grid">
      <div class="tech-item">
        <i class="fab fa-java"></i>
        <span>Java</span>
      </div>
      <div class="tech-item">
        <i class="fas fa-database"></i>
        <span>MySQL</span>
      </div>
      <div class="tech-item">
        <i class="fab fa-html5"></i>
        <span>HTML5</span>
      </div>
      <div class="tech-item">
        <i class="fab fa-css3-alt"></i>
        <span>CSS3</span>
      </div>
      <div class="tech-item">
        <i class="fab fa-js"></i>
        <span>JavaScript</span>
      </div>
    </div>
  </section>
</div>

<style>
  .about-hero {
    background: #000000;
    padding: 100px 20px;
    text-align: center;
    border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  }

  .about-hero h1 {
    font-size: 4rem;
    margin: 0;
    color: #ffffff;
    font-weight: 700;
    letter-spacing: 2px;
    text-transform: uppercase;
  }

  .about-hero p {
    font-size: 1.2rem;
    color: rgba(255, 255, 255, 0.7);
    margin-top: 20px;
  }

  .about-container {
    background: #000000;
    padding: 60px 20px;
  }

  section {
    max-width: 1200px;
    margin: 0 auto 80px;
  }

  section h2 {
    font-size: 2.5rem;
    color: #ffffff;
    text-align: center;
    margin-bottom: 50px;
    text-transform: uppercase;
    letter-spacing: 1px;
  }

  .mission-section {
    text-align: center;
    padding: 0 20px;
  }

  .mission-section p {
    color: rgba(255, 255, 255, 0.8);
    font-size: 1.2rem;
    line-height: 1.8;
    max-width: 800px;
    margin: 0 auto;
  }

  .features-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 30px;
    padding: 20px;
  }

  .feature-card {
    background: rgba(255, 255, 255, 0.03);
    border: 1px solid rgba(255, 255, 255, 0.1);
    border-radius: 4px;
    padding: 30px;
    text-align: center;
    transition: transform 0.3s ease;
  }

  .feature-card:hover {
    transform: translateY(-5px);
    background: rgba(255, 255, 255, 0.05);
  }

  .feature-card i {
    font-size: 2.5rem;
    color: #ffffff;
    margin-bottom: 20px;
  }

  .feature-card h3 {
    color: #ffffff;
    font-size: 1.3rem;
    margin-bottom: 15px;
  }

  .feature-card p {
    color: rgba(255, 255, 255, 0.7);
    font-size: 1rem;
    line-height: 1.6;
  }

  .team-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 40px;
    padding: 20px;
  }

  .team-member {
    text-align: center;
    background: rgba(255, 255, 255, 0.03);
    border: 1px solid rgba(255, 255, 255, 0.1);
    border-radius: 4px;
    padding: 30px;
    transition: transform 0.3s ease;
  }

  .team-member:hover {
    transform: translateY(-5px);
    background: rgba(255, 255, 255, 0.05);
  }

  .member-photo {
    width: 150px;
    height: 150px;
    margin: 0 auto 20px;
    border-radius: 50%;
    overflow: hidden;
    border: 3px solid rgba(255, 255, 255, 0.1);
  }

  .member-photo img {
    width: 100%;
    height: 100%;
    object-fit: cover;
  }

  .team-member h3 {
    color: #ffffff;
    font-size: 1.2rem;
    margin-bottom: 5px;
  }

  .team-member .role {
    color: rgba(255, 255, 255, 0.7);
    font-size: 0.9rem;
    margin-bottom: 15px;
    font-style: italic;
  }

  .team-member .contribution {
    color: rgba(255, 255, 255, 0.6);
    font-size: 0.9rem;
    line-height: 1.6;
  }

  .tech-grid {
    display: flex;
    justify-content: center;
    flex-wrap: wrap;
    gap: 30px;
    padding: 20px;
  }

  .tech-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 10px;
    color: #ffffff;
    transition: transform 0.3s ease;
  }

  .tech-item:hover {
    transform: translateY(-5px);
  }

  .tech-item i {
    font-size: 2.5rem;
  }

  .tech-item span {
    font-size: 1rem;
    color: rgba(255, 255, 255, 0.8);
  }

  @media (max-width: 768px) {
    .about-hero h1 {
      font-size: 3rem;
    }

    section h2 {
      font-size: 2rem;
    }

    .features-grid {
      grid-template-columns: 1fr;
    }

    .team-grid {
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    }

    .tech-grid {
      gap: 20px;
    }

    .tech-item i {
      font-size: 2rem;
    }
  }
</style>

<jsp:include page="includes/footer.jsp" />
