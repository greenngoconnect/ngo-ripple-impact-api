package br.com.oakshield.oakshield.core.domain.useracknowledgement;

import br.com.oakshield.oakshield.core.domain.policy.Policy;
import br.com.oakshield.oakshield.core.domain.audit.AuditDomain;
import br.com.oakshield.oakshield.core.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "tb_user_acknowledgement", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "policy_id"})
})
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
@Schema(description = "User Acknowledgement Entity", name = "Asset")
@Tag(name = "User Acknowledgement Entity", description = "Operations related to assets")
public class UserAcknowledgement extends AuditDomain {

    @Schema(description = "ID da confirmação do usuário", example = "123e4567-e89b-12d3-a456-426614174000")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Schema(description = "ID do usuário que reconheceu a política", example = "123e4567-e89b-12d3-a456-426614174000")
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @Schema(description = "ID da política reconhecida pelo usuário", example = "123e4567-e89b-12d3-a456-426614174000")
    @ManyToOne
    @JoinColumn(name = "policy_id", referencedColumnName = "id", nullable = false)
    private Policy policy;

    @Schema(description = "Data em que o usuário reconheceu a política", example = "2023-10-01")
    @Column(name = "acknowledged_at", nullable = false)
    private LocalDateTime acknowledgedAt;

}

/*
A classe UserAcknowledgement representa o registro do aceite de uma política de segurança por um usuário, ou seja, quem leu e confirmou o conhecimento ou concordância com determinada política da organização.

        📌 Objetivo principal
Registrar quando um usuário reconheceu formalmente uma política, como:

Política de Segurança da Informação

Política de Controle de Acesso

Código de Conduta etc.

Isso é crucial para conformidade com a ISO/IEC 27001, LGPD, GDPR e auditorias internas ou externas.

        🧩 O que essa classe faz exatamente?
Elemento	Função
@Entity	Indica que a classe é uma entidade JPA persistida no banco de dados
@Table(...)	Define o nome da tabela e uma restrição de unicidade (user_id, policy_id) para evitar duplicatas
id	Identificador único do aceite
userId	ID do usuário que aceitou a política
policyId	ID da política que foi aceita
acknowledgedAt	Data em que o aceite ocorreu
extends AuditDomain	Herdando dados de auditoria como createdAt, updatedAt, etc.

✅ Exemplo prático de uso:
Vamos imaginar que a empresa publique uma nova política de backup. Quando o usuário acessa o sistema, ele é solicitado a ler a política e clicar em "Li e concordo". Ao fazer isso, um registro do tipo UserAcknowledgement é criado com:

userId = ID do colaborador

        policyId = ID da política de backup

acknowledgedAt = Data e hora atual

Esse registro pode ser usado depois em relatórios de conformidade como:

        “90% dos usuários reconheceram a Política de Backup até 01/08/2025.”

        🔒 Por que isso é importante?
Ajuda a provar que os usuários estão cientes das políticas.

Garante rastreabilidade em auditorias.

Facilita o gerenciamento de conformidade contínua com normas como a ISO 27001.

*/
