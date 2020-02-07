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
