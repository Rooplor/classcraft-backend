import com.rooplor.classcraftbackend.entities.User
import com.rooplor.classcraftbackend.messages.ErrorMessages
import com.rooplor.classcraftbackend.repositories.UserRepository
import com.rooplor.classcraftbackend.services.UserDetailService
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import java.util.Optional

class UserDetailServiceTest {
    private lateinit var userDetailService: UserDetailService
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        userRepository = Mockito.mock(UserRepository::class.java)
        userDetailService = UserDetailService()

        // Use reflection to set the private userRepository field
        val userRepositoryField = UserDetailService::class.java.getDeclaredField("userRepository")
        userRepositoryField.isAccessible = true
        userRepositoryField.set(userDetailService, userRepository)
    }

    @Test
    fun `loadUserByUsername should return UserDetails when user exists`() {
        val username = "testUser"
        val user = User(username = username, email = "test@example.com", profilePicture = "profilePictureUrl")
        `when`(userRepository.findByUsername(username)).thenReturn(Optional.of(user))

        val userDetails = userDetailService.loadUserByUsername(username)

        assertEquals(username, userDetails.username)
    }

    @Test
    fun `loadUserByUsername should throw Exception when user does not exist`() {
        val username = "nonExistentUser"
        `when`(userRepository.findByUsername(username)).thenReturn(Optional.empty())

        val exception =
            assertThrows<Exception> {
                userDetailService.loadUserByUsername(username)
            }

        assertEquals(ErrorMessages.USER_NOT_FOUND, exception.message)
    }
}
