package web.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import javax.persistence.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "users")
public class User {

    private @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Column(name = "name")
    @NotEmpty(message = "Name should not be empty")
    @Size(min = 2, max = 30, message = "Name should be between 2 to 30")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "password")
    @NotEmpty(message = "Password should not be empty")
    @Min(value = 4, message = "Password should be min 4 characters")
    private String password;
}
