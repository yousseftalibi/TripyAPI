package com.isep.trippy.Models;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.TypeDef;
import com.vladmihalcea.hibernate.type.array.StringArrayType;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
@TypeDef(name = "string-array", typeClass = StringArrayType.class)

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private int id;
    @Column(name="username")
    private String username;

    @Column(name="password")
    private String password;

    @Column(name = "places", columnDefinition = "text[]")
    private String places;

}
