@RunWith(SpringRunner.class)
public class LibraryUserDetailsServiceTest {

	@TestConfiguration
	static class LibraryUserDetailsServiceTestContextConfiguration {

		@Bean
		public UserDetailsService userDetailsService () {
			return new LibraryUserDetailsService();
		}
	}

	@Autowired
	private UserDetailsService userDetailsService;

	@MockBean
	private AdminRepository adminRepository;

	private Librarian lib;

	@Before
	public void setUp() {
		this.lib = new Librarian("TestId", "Test", "ADMIN", "admin");
		Mockito.when(this.adminRepository.findByUserId(this.lib.getUserId())).thenReturn(this.lib);
	}

	@Test(expected = Test.None.class)
	public void testAddLibrarian() {
		Librarian lib = this.userDetailsService.loadUserByUsername(this.lib.getUserId());
		assertThat(lib.getUserId().equals(this.lib.getUserId()));
	}
}



@RunWith(SpringRunner.class)
public class LibrarianServiceTest {

	@TestConfiguration
	static class LibrarianServiceTestContextConfiguration {

		@Bean
		public LibrarianService librarianService () {
			return new LibrarianServiceImpl();
		}
	}

	@MockBean
	private LibrarianRepository librarianRepository;

	@Autowired
	private LibrarianService librarianService;

	private Book book;
	
	@Before
	public void setUp() {
		List<Book> allList = Arrays.asList(new Book("Java","Y"),new Book("C","N"));
		List<Book> issuedList = Arrays.asList(new Book("Java","Y"));
		this.book = new Book("Java", "Y");
		Mockito.when(this.librarianRepository.findAll()).thenReturn(allList);
		Mockito.when(this.librarianRepository.save(this.book)).thenReturn(this.book);
		Mockito.when(this.librarianRepository.findAllByIssued("Y")).thenReturn(issuedList);
	}
	
	@Test
	public void testAddBook() {
		Book b = this.librarianService.addBook(this.book);
		assertThat(b.getName().equals(this.book.getName()));
	}
	@Test
	public void testGetAllBooks() {
		List<Book> list = this.librarianService.getAllBooks();
		assertThat(list.size() == 2);
		assertThat(list.get(0).getName().equals("Java"));
		assertThat(list.get(1).getName().equals("C"));
	}
	
	@Test
	public void testGetAllIssuedBooks() {
		List<Book> list = this.librarianService.getAllIssuedBooks();
		assertThat(list.size() == 1);
		assertThat(list.get(0).getName().equals("Java"));
	}
	
	@Test
	public void testIssueBook_Success() {
		Mockito.when(this.librarianRepository.findById(1)).thenReturn(Optional.of(new Book("Java", "N")));
		Book b = this.librarianService.issueBook(new Book("Java", "N"));
		assertThat(b.getIssued().equals("Y"));
	}
	
	@Test(Exception = LibraryException.class)
	public void testIssueBook_Failure1() {
		Mockito.when(this.librarianRepository.findById(1)).thenReturn(Optional.of(new Book("Java", "Y")));
		this.librarianService.issueBook(new Book("Java", "N"));
	}
	
	@Test(Exception = LibraryException.class)
	public void testIssueBook_Failure2() {
		Mockito.when(this.librarianRepository.findById(1)).thenReturn(Optional.of(null));
		this.librarianService.issueBook(new Book("Java", "N"));
	}
	
	@Test
	public void testReturnBook_Success() {
		Mockito.when(this.librarianRepository.findById(1)).thenReturn(Optional.of(new Book("Java", "Y")));
		Mockito.when(this.librarianRepository.save(new Book("Java", "Y"))).thenReturn(new Book("Java", "N");
		Book b = this.librarianService.issueBook(new Book("Java", "Y"));
		assertThat(b.getIssued().equals("N"));
		
	}
											      
	@Test(Exception = LibraryException.class)
	public void testReturnBook_Failure1() {
		Mockito.when(this.librarianRepository.findById(1)).thenReturn(Optional.of(new Book("Java", "N")));
		this.librarianService.issueBook(new Book("Java", "Y"));		
	}
											      
	@Test(Exception = LibraryException.class)
	public void testReturnBook_Failure2() {
		Mockito.when(this.librarianRepository.findById(1)).thenReturn(Optional.of(null));
		this.librarianService.issueBook(new Book("Java", "Y"));		
	}
}

											      
											      
											      
@DataJpaTest
@RunWith(SpringRunner.class)
public class LibrarianRepositoryTest {

	@Autowired
	private LibrarianRepository librarianRepository ;

	private Book issuedBook;
	private Book returnedBook;

	@Before
	public void setUp() {
		this.issuedBook = new Book("Java", "Y");
		this.returnedBook = new Book("C", "N");
		List<Book> list = Arrays.asList(this.returnedBook, this.issuedBook);
		this.librarianRepository.save(this.returnedBook)
	}
	
	@Test(expected = Test.None.class)
	public void testSaveBook() {
		Book book = this.librarianRepository.save(this.returnedBook);
		assertThat(book.getName().equals(this.returnedBook.getName()));		
	}
	
	@Test(expected = Test.None.class)
	public void testFindAllByIssued() {
		List<Book> list = this.librarianRepository.findAllByIssued("Y");
		assertThat(list.get(0).getName().equals(this.issuedBook.getName()));
	}
}
	
