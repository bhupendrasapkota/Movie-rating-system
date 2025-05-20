<%@ page contentType="text/html;charset=UTF-8" language="java" %>
    </div>
    <footer class="main-footer">
        <div class="container">
            <div class="footer-content">
                <div class="footer-section">
                    <h3>Movie Rating System</h3>
                    <p>Your ultimate platform for movie ratings and reviews.</p>
                </div>
                <div class="footer-section">
                    <h3>Quick Links</h3>
                    <ul>
                        <li><a href="${pageContext.request.contextPath}/">Home</a></li>
                        <li><a href="${pageContext.request.contextPath}/movies">Movies</a></li>
                        <li><a href="${pageContext.request.contextPath}/about">About Us</a></li>
                        <li><a href="${pageContext.request.contextPath}/contact">Contact</a></li>
                    </ul>
                </div>
                <div class="footer-section">
                    <h3>Contact Us</h3>
                    <p>Email: info@movieratingsystem.com</p>
                    <p>Phone: +977-9818000000</p>
                </div>
            </div>
            <div class="footer-bottom">
                <p>&copy; 2025 Movie Rating System. All rights reserved.</p>
            </div>
        </div>
    </footer>
    
    <script>
        // Mobile menu toggle
        document.querySelector('.mobile-menu-toggle').addEventListener('click', function() {
            document.querySelector('.main-nav').classList.toggle('active');
            this.classList.toggle('active');
        });
        
        // Dropdown menu
        const dropdowns = document.querySelectorAll('.dropdown');
        dropdowns.forEach(dropdown => {
            dropdown.addEventListener('click', function(e) {
                this.classList.toggle('active');
                e.stopPropagation();
            });
        });
        
        // Close dropdowns when clicking outside
        document.addEventListener('click', function() {
            dropdowns.forEach(dropdown => {
                dropdown.classList.remove('active');
            });
        });
    </script>
</body>
</html>