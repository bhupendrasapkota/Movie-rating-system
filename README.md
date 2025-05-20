# Movie Rating System

A sophisticated web-based movie rating application that allows users to discover, rate, and review movies while maintaining their personal watchlists. Built with Java EE and modern web technologies.

## ✨ Key Feature

- **User Management**

  - Secure user authentication and authorization
  - Personalized user profiles with profile pictures
  - Role-based access control (User/Admin)

- **Movie Management**

  - Comprehensive movie catalog with detailed information
  - Advanced search and filtering capabilities
  - Genre-based categorization
  - Movie posters and metadata support

- **Rating & Reviews**

  - 10-point rating scale
  - Detailed user reviews
  - Average rating calculations
  - Recent reviews display

- **Watchlist**

  - Personal watchlist management
  - Add/remove movies from watchlist
  - Quick access to saved movies

- **Admin Features**
  - Movie CRUD operations
  - User management
  - Content moderation
  - System statistics dashboard

## 🛠️ Technology Stack

- **Backend**

  - Java EE (Jakarta EE)
  - Servlets & JSP
  - JDBC for database operations
  - BCrypt for password encryption

- **Frontend**

  - HTML5 & CSS3
  - JavaScript
  - Bootstrap components
  - Font Awesome icons

- **Database**

  - MySQL/MariaDB
  - Optimized schema design

- **Security**
  - Session management
  - Input validation
  - XSS protection
  - SQL injection prevention

## 📋 Prerequisites

- JDK 17 or higher
- Apache Tomcat 10.0+
- MySQL 8.0+
- Maven 3.6+

## 🚀 Installation & Setup

1. **Clone the Repository**

   ```bash
   git clone https://github.com/bhupendrasapkota/Movie-rating-system.git
   cd Movie-rating-system
   ```

2. **Database Configuration**

   - Create a new MySQL database
   - Execute `schema.sql` from `src/main/resources`
   - Update database credentials in `application.properties`

3. **Build the Project**

   ```bash
   mvn clean install
   ```

4. **Deploy the Application**
   - Copy `MovieRatingSystem.war` to Tomcat's webapps directory
   - Start Tomcat server
   - Access at: `http://localhost:8080/MovieRatingSystem`

## 🏗️ Project Structure

```
Movie-rating-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/movieratingsystem/
│   │   │       ├── controllers/    # Servlet controllers
│   │   │       ├── dao/           # Data Access Objects
│   │   │       ├── models/        # Entity models
│   │   │       ├── service/       # Business logic
│   │   │       └── utils/         # Utility classes
│   │   ├── resources/
│   │   │   ├── application.properties
│   │   │   └── schema.sql
│   │   └── webapp/
│   │       ├── css/
│   │       ├── images/
│   │       └── WEB-INF/
│   │           └── views/         # JSP pages
└── pom.xml
```

## 🔑 Key Features Implementation

- **Movie Management**

  - Full CRUD operations via `MovieController`
  - Image handling with `@MultipartConfig`
  - Pagination and sorting support

- **Rating System**

  - Rating validation (1-10 scale)
  - Average rating calculations
  - User rating history

- **Search & Filter**
  - Dynamic search by title
  - Genre filtering
  - Year range filtering
  - Combined search criteria

## 📚 API Documentation

### Main Endpoints

- `GET /movies` - List all movies with filtering
- `GET /movie?id={id}` - Get movie details
- `POST /rating/add` - Add/update movie rating
- `POST /review/add` - Add movie review
- `GET /profile` - User profile management
- `GET /admin/*` - Admin operations

## 🔒 Security Implementation

- Session-based authentication
- Password hashing with BCrypt
- Input sanitization
- CSRF protection
- Role-based access control

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

Built with ❤️ by the Movie Rating System Team
