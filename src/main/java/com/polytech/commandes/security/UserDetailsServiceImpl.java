package com.polytech.commandes.security;

import com.polytech.commandes.entity.Utilisateur;
import com.polytech.commandes.repository.UtilisateurRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UtilisateurRepository utilisateurRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Utilisateur utilisateur = utilisateurRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Utilisateur non trouvé : " + username));

        return User.builder()
                .username(utilisateur.getUsername())
                .password(utilisateur.getPassword())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + utilisateur.getRole().name())))
                .build();
    }
}
