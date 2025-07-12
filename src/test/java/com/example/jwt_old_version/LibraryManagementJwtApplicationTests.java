//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//import org.springframework.test.web.servlet.MockMvc;
//
//import javax.transaction.Transactional;
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
//@Transactional
//public class LibraryManagementJwtApplicationTests {
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private BookRepository bookRepository;
//    @BeforeEach
//    public void setUp() {
//        // Clear the database before each test
//        userRepository.deleteAll();
//        bookRepository.deleteAll();
//    }
//    @Test
//    public void shouldRegisterUser() throws Exception {
//        // Given - User data
//        User user = new User();
//        user.setUsername("testuser");
//        user.setPassword("password");
//        user.setRole("ADMIN");
//        // When - Calling the registration endpoint
//        mockMvc.perform(post("/users/register")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(user)))
//                .andExpect(jsonPath("$.username").value("testuser")); // Verify the username in the response
//        User savedUser = userRepository.findAll().get(0);
//        assertThat(savedUser.getUsername()).isEqualTo("testuser");
//        assertThat(savedUser.getRole()).isEqualTo("ADMIN");
//    }
//    @Test
//    public void shouldLoginAndReturnJwt() throws Exception {
//        // Given - Register the user first
//        User user = new User();
//        user.setUsername("testuser");
//        user.setPassword("password");
//        user.setRole("ADMIN");
//        // Save user to the database
//        mockMvc.perform(post("/users/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(user)));
//        // When - Creating an authentication request
//        AuthRequest authRequest = new AuthRequest("testuser", "password");
//        // Perform login and verify the response contains JWT token
//        mockMvc.perform(post("/users/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(authRequest)))
//                .andExpect(jsonPath("$.token").exists()); // Verify the JWT token is in the response
//    }
//    @Test
//    public void testShouldGiveUnAuthorizedForInvalidUser() throws Exception {
//        // Given - Register the user first
//        User user = new User();
//        user.setUsername("testuser");
//        user.setPassword("password");
//        user.setRole("ADMIN");
//        // Save user to the database
//        mockMvc.perform(post("/users/register")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(user)));
//        // When - Creating an authentication request with invalid password
//        AuthRequest authRequest = new AuthRequest("testuser", "invalidpassword");
//        // Perform login and verify the response contains JWT token
//        mockMvc.perform(post("/users/login")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(authRequest)))
//                .andExpect(status().isUnauthorized()); // Verify the response status is 401
//    }
//    @Test
//    @WithMockUser(username = "testuser", authorities = {"ADMIN", "USER"})
//    public void testShouldGetUserById() throws Exception {
//        // Given - Register the user first
//        User user = new User();
//        user.setUsername("testuser");
//        user.setPassword(new BCryptPasswordEncoder().encode("password"));
//        user.setRole("ADMIN");
//        user = userRepository.save(user);
//        // Get the user by id
//        mockMvc.perform(get("/users/" + user.getId()))
//                .andExpect(jsonPath("$.username").value("testuser"))
//                .andExpect(jsonPath("$.role").value("ADMIN"));
//    }
//    @Test
//    @WithMockUser(username = "testuser", authorities = {"ADMIN", "USER"})
//    public void testShouldUpdateUser() throws Exception {
//        // Given - Register the user first
//        User user = new User();
//        user.setUsername("testuser");
//        user.setPassword(new BCryptPasswordEncoder().encode("password"));
//        user.setRole("ADMIN");
//        user = userRepository.save(user);
//        // Update the user
//        user.setRole("USER");
//        mockMvc.perform(put("/users/" + user.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(user)))
//                .andExpect(jsonPath("$.role").value("USER"));
//        User updatedUser = userRepository.findById(user.getId()).get();
//        assertThat(updatedUser.getRole()).isEqualTo("USER");
//        assertThat(updatedUser.getUsername()).isEqualTo("testuser");
//    }
//    @Test
//    @WithMockUser(username = "testuser", authorities = {"ADMIN"})
//    public void testShouldCreateBook() throws Exception {
//        // Given - Register the user first
//        Book book = new Book();
//        book.setTitle("Book Title");
//        book.setAuthor("Author Name");
//        book.setDescription("Book Description");
//        book.setAvailability(true);
//        // Create a book
//        mockMvc.perform(post("/books")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(book)))
//                .andExpect(jsonPath("$.title").value("Book Title"));
//        Book savedBook = bookRepository.findAll().get(0);
//        assertThat(savedBook.getTitle()).isEqualTo("Book Title");
//        assertThat(savedBook.getAuthor()).isEqualTo("Author Name");
//        assertThat(savedBook.getDescription()).isEqualTo("Book Description");
//        assertThat(savedBook.isAvailability()).isTrue();
//    }
//    @Test
//    @WithMockUser(username = "testuser", authorities = {"ADMIN"})
//    public void testShouldUpdateBook() throws Exception {
//        // Given - Register the user first
//        Book book = new Book();
//        book.setTitle("Book Title");
//        book.setAuthor("Author Name");
//        book.setDescription("Book Description");
//        book.setAvailability(true);
//        book = bookRepository.save(book);
//        // Update the book
//        book.setTitle("Updated Book Title");
//        mockMvc.perform(put("/books/" + book.getId())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(book)))
//                .andExpect(jsonPath("$.title").value("Updated Book Title"));
//        Book updatedBook = bookRepository.findById(book.getId()).get();
//        assertThat(updatedBook.getTitle()).isEqualTo("Updated Book Title");
//        assertThat(updatedBook.getAuthor()).isEqualTo("Author Name");
//        assertThat(updatedBook.getDescription()).isEqualTo("Book Description");
//        assertThat(updatedBook.isAvailability()).isTrue();
//    }
//    @Test
//    @WithMockUser(username = "testuser", authorities = {"ADMIN"})
//    public void testShouldDeleteBook() throws Exception {
//        // Given - Register the user first
//        Book book = new Book();
//        book.setTitle("Book Title");
//        book.setAuthor("Author Name");
//        book.setDescription("Book Description");
//        book.setAvailability(true);
//        book = bookRepository.save(book);
//        // Delete the book
//        mockMvc.perform(delete("/books/" + book.getId()));
//        assertThat(bookRepository.findById(book.getId())).isEmpty();
//    }
//    @Test
//    @WithMockUser(username = "testuser", authorities = {"USER"})
//    public void testShouldViewAllBooks() throws Exception {
//        // Given - Register the user first
//        Book book = new Book();
//        book.setTitle("Book Title");
//        book.setAuthor("Author Name");
//        book.setDescription("Book Description");
//        book.setAvailability(true);
//        bookRepository.save(book);
//        // Get all books
//        mockMvc.perform(get("/books"))
//                .andExpect(jsonPath("$[0].title").value("Book Title"))
//                .andExpect(jsonPath("$[0].author").value("Author Name"))
//                .andExpect(jsonPath("$[0].description").value("Book Description"))
//                .andExpect(jsonPath("$[0].availability").value(true));
//    }
//    @Test
//    @WithMockUser(username = "testuser", authorities = {"USER"})
//    public void testShouldBorrowBook() throws Exception {
//        // Given - Register the user first
//        Book book = new Book();
//        book.setTitle("Book Title");
//        book.setAuthor("Author Name");
//        book.setDescription("Book Description");
//        book.setAvailability(true);
//        book = bookRepository.save(book);
//        // Borrow the book
//        mockMvc.perform(post("/books/" + book.getId() + "/borrow"));
//        Book borrowedBook = bookRepository.findById(book.getId()).get();
//        assertThat(borrowedBook.isAvailability()).isFalse();
//    }
//    @Test
//    @WithMockUser(username = "testuser", authorities = {"USER"})
//    public void testShouldReturnBook() throws Exception {
//        // Given - Register the user first
//        Book book = new Book();
//        book.setTitle("Book Title");
//        book.setAuthor("Author Name");
//        book.setDescription("Book Description");
//        book.setAvailability(false);
//        book = bookRepository.save(book);
//        // Return the book
//        mockMvc.perform(post("/books/" + book.getId() + "/return"));
//        Book returnedBook = bookRepository.findById(book.getId()).get();
//        assertThat(returnedBook.isAvailability()).isTrue();
//    }
//    @Test
//    @WithMockUser(username = "testuser", authorities = {"ADMIN"})
//    public void testAdminRoleShouldNotAccessUserApis() throws Exception {
//        Book book = new Book();
//        book.setTitle("Book Title");
//        book.setAuthor("Author Name");
//        book.setDescription("Book Description");
//        book.setAvailability(false);
//        book = bookRepository.save(book);
//        mockMvc.perform(post("/books/" + book.getId() + "/return"))
//                .andExpect(status().isForbidden());
//        mockMvc.perform(post("/books/" + book.getId() + "/borrow"))
//                .andExpect(status().isForbidden());
//    }
//    @Test
//    @WithMockUser(username = "testuser", authorities = {"USER"})
//    public void testUserRoleShouldNotAccessAdminApis() throws Exception {
//        Book book = new Book();
//        book.setTitle("Book Title");
//        book.setAuthor("Author Name");
//        book.setDescription("Book Description");
//        book.setAvailability(false);
//        book = bookRepository.save(book);
//        mockMvc.perform(post("/books")
//                        .content(objectMapper.writeValueAsString(book)))
//                .andExpect(status().isForbidden());
//        mockMvc.perform(put("/books/" + book.getId()))
//                .andExpect(status().isForbidden());
//        mockMvc.perform(delete("/books/" + book.getId()))
//                .andExpect(status().isForbidden());
//    }
//}