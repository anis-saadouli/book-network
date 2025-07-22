package com.saadouli.book_network.user;

import com.saadouli.book_network.role.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Collection;
import java.util.stream.Collectors;

/**
 * The User class represents a user in the application.
 * It implements the UserDetails and Principal interfaces for Spring Security integration.
 * This class includes personal information, relationships with other entities,
 * and metadata for tracking changes.
 */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
// @Entity
// @Table(name = "_user")
// @EntityListeners(AuditingEntityListener.class)
public class User implements UserDetails, Principal {

    /**
     * Unique identifier for the user.
     * Automatically generated.
     */
    @Id
    @GeneratedValue
    private Integer id;

    /**
     * User's first name.
     */
    private String firstname;

    /**
     * User's last name.
     */
    private String lastname;

    /**
     * User's date of birth.
     */
    private LocalDate dateOfBirth;

    /**
     * User's email address.
     * Must be unique in the database.
     */
    @Column(unique = true)
    private String email;

    /**
     * User's password.
     */
    private String password;

    /**
     * Indicates if the account is locked.
     */
    private boolean accountLocked;

    /**
     * Indicates if the account is enabled.
     */
    private boolean enabled;

    /**
     * List of roles associated with the user.
     * Many-to-many relationship with the Role class.
     * Roles are eagerly loaded.
     */
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles;

    /**
     * List of books owned by the user.
     * One-to-many relationship with the Book class.
     */
    @OneToMany(mappedBy = "owner")
    private List<Book> books;

    /**
     * User's transaction history.
     * One-to-many relationship with the BookTransactionHistory class.
     */
    @OneToMany(mappedBy = "user")
    private List<BookTransactionHistory> histories;

    /**
     * User creation date.
     * Automatically set when persisted.
     */
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;

    /**
     * Last modification date of the user.
     * Automatically updated on each modification.
     */
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;

    /**
     * Returns the user's authorities (roles) as GrantedAuthority.
     * Used by Spring Security.
     *
     * @return Collection of GrantedAuthority.
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
    }

    /**
     * Returns the user's password.
     *
     * @return Password.
     */
    @Override
    public String getPassword() {
        return password;
    }

    /**
     * Returns the username (email).
     *
     * @return Email.
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Indicates if the account is not expired.
     *
     * @return true (not expired).
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Indicates if the account is not locked.
     *
     * @return true if not locked, false otherwise.
     */
    @Override
    public boolean isAccountNonLocked() {
        return !accountLocked;
    }

    /**
     * Indicates if the credentials are not expired.
     *
     * @return true (not expired).
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Indicates if the account is enabled.
     *
     * @return true if enabled, false otherwise.
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Returns the user's full name (first name + last name).
     *
     * @return Full name.
     */
    public String fullName() {
        return getFirstname() + " " + getLastname();
    }

    /**
     * Returns the principal name (email).
     *
     * @return Email.
     */
    @Override
    public String getName() {
        return email;
    }

    /**
     * Returns the user's full name (first name + last name).
     *
     * @return Full name.
     */
    public String getFullName() {
        return firstname + " " + lastname;
    }
}