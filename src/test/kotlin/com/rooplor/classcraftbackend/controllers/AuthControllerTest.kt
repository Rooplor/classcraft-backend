// package com.rooplor.classcraftbackend.controllers
//
// import com.rooplor.classcraftbackend.services.AuthService
// import org.junit.jupiter.api.Test
// import org.mockito.Mockito.any
// import org.mockito.Mockito.`when`
// import org.springframework.beans.factory.annotation.Autowired
// import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
// import org.springframework.boot.test.mock.mockito.MockBean
// import org.springframework.http.MediaType
// import org.springframework.test.web.servlet.MockMvc
// import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
//
// @WebMvcTest(AuthController::class)
// class AuthControllerTest {
//    @Autowired
//    private lateinit var mockMvc: MockMvc
//
//    @MockBean
//    private lateinit var authService: AuthService
//
//    @Test
//    fun `test validateToken valid`() {
//        val idToken = "validToken"
//        `when`(authService.validateIdToken(idToken)).thenReturn(any())
//
//        mockMvc.perform(
//            post("/api/auth/validate")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"idToken\": \"$idToken\"}"),
//        )
//    }
//
//    @Test
//    fun `test validateToken invalid`() {
//        val idToken = "invalidToken"
//        `when`(authService.validateIdToken(idToken)).thenReturn(null)
//
//        mockMvc.perform(
//            post("/api/auth/validate")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\"idToken\": \"$idToken\"}"),
//        )
//    }
// }
