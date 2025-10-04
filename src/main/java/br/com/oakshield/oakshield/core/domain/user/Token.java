package br.com.oakshield.oakshield.core.domain.user;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Entity
@Table(name = "tb_token")
public class Token {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  public UUID id;

  @Column(name = "token", unique = true)
  public String token;

  @Column(name = "token_type")
  @Enumerated(EnumType.STRING)
  @Builder.Default
  public TokenType tokenType = TokenType.BEARER;

  public boolean revoked;

  public boolean expired;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  public User user;
}