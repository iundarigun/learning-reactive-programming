package br.com.devcave.reactive.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
//import org.springframework.security.core.GrantedAuthority
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//import org.springframework.security.core.userdetails.UserDetails

@Table("users")
data class User(
    @Id
    val id: Long,
    val name: String,
    val username: String,
    val password: String,
    val authorities: String
)

//fun User.toUseDetails(): UserDetails =
//    object : UserDetails {
//        override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
//            return this@toUseDetails
//                .authorities
//                .split(",")
//                .map { SimpleGrantedAuthority(it) }
//                .toMutableList()
//        }
//
//        override fun getPassword(): String = this@toUseDetails.password
//
//        override fun getUsername(): String = this@toUseDetails.username
//
//        override fun isAccountNonExpired(): Boolean = true
//
//        override fun isAccountNonLocked(): Boolean = true
//
//        override fun isCredentialsNonExpired(): Boolean = true
//
//        override fun isEnabled(): Boolean = true
//    }
