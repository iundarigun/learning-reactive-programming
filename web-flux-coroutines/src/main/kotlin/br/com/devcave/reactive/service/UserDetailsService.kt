package br.com.devcave.reactive.service

import br.com.devcave.reactive.domain.toUseDetails
import br.com.devcave.reactive.repository.UserRepository
import kotlinx.coroutines.reactor.mono
import org.springframework.security.core.userdetails.ReactiveUserDetailsService
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import reactor.core.publisher.Mono

@Service
class UserDetailsService(private val userRepository: UserRepository) : ReactiveUserDetailsService {
    override fun findByUsername(username: String): Mono<UserDetails> =
        mono {
            return@mono userRepository.findByUsername(username).toUseDetails()
        }
}