# Sistema de Agendamento de Transferências Financeiras

API REST desenvolvida com Spring Boot e Java 11 para gerenciar agendamentos de transferências financeiras com cálculo automático de taxas.

## Tecnologias Utilizadas

- Java 11
- Spring Boot 2.7.18
- Spring Data JPA
- H2 Database (em memória)
- Lombok
- Maven
- Bean Validation

## Funcionalidades

1. **Agendar Transferência**: Cadastra uma nova transferência com cálculo automático da taxa
2. **Listar Extrato**: Visualiza todas as transferências agendadas

## Regras de Cálculo de Taxa

| Dias até Transferência | Taxa Fixa | Taxa Percentual |
|------------------------|-----------|-----------------|
| 0 dias                 | R$ 3,00   | 2,5%            |
| 1 a 10 dias            | R$ 12,00  | -               |
| 11 a 20 dias           | -         | 8,2%            |
| 21 a 30 dias           | -         | 6,9%            |
| 31 a 40 dias           | -         | 4,7%            |
| 41 a 50 dias           | -         | 1,7%            |
| Acima de 50 dias       | ❌ Taxa não aplicável |

## Como Executar

### Pré-requisitos
- Java 11 instalado
- Maven 3.6+ instalado

### Executando a aplicação

```bash
# Clone o repositório
git clone <url-do-repositorio>

# Entre no diretório
cd agendamento-transferencias

# Execute com Maven
mvn spring-boot:run
```

A aplicação estará disponível em: `http://localhost:8080/transferencia`

## Endpoints da API

### 1. Agendar Transferência

**POST** `/transferencia`

**Request Body:**
```json
{
  "contaOrigem": "1234567890",
  "contaDestino": "0987654321",
  "valorTransferencia": 10,
  "dataTransferencia": "2025-11-15"
}
```

**Response (201 Created):**
```json
{
    "id": 1,
    "contaOrigem": "1234567890",
    "contaDestino": "0987654320",
    "valorTransferencia": 10,
    "taxa": 0.82,
    "dataTransferencia": "2025-11-15",
    "dataAgendamento": "2025-10-26"
}
```

### 2. Listar Extrato

**GET** `/transferencia/extrato?page=0&size=5`

**Response (200 OK):**
```json
{
  "content": [
    {
      "id": 1,
      "contaOrigem": "1234567890",
      "contaDestino": "0987654320",
      "valorTransferencia": 10.00,
      "taxa": 0.82,
      "dataTransferencia": "2025-11-15",
      "dataAgendamento": "2025-10-26"
    }
  ],
  "pageable": {
    "sort": {
      "sorted": true,
      "empty": false,
      "unsorted": false
    },
    "pageNumber": 0,
    "pageSize": 5,
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": true,
  "totalPages": 1,
  "totalElements": 1,
  "first": true,
  "size": 5,
  "number": 0,
  "sort": {
    "sorted": true,
    "empty": false,
    "unsorted": false
  },
  "numberOfElements": 1,
  "empty": false
}
```
## Validações Implementadas

- Conta de origem e destino devem ter exatamente 10 dígitos numéricos
- Valor da transferência deve ser maior que zero
- Data da transferência é obrigatória
- Data da transferência não pode ser anterior à data de agendamento
- Cálculo automático da taxa conforme regras de negócio
- Bloqueio de transferências com mais de 50 dias (taxa não aplicável)
- Data de agendamento é automaticamente definida como a data atual

## Notas Importantes

1. O banco de dados H2 é **em memória**, portanto todos os dados são perdidos ao reiniciar a aplicação
2. A data de agendamento é sempre a data atual do sistema (LocalDate.now())
3. As contas devem ter exatamente 10 dígitos numéricos (padrão: XXXXXXXXXX)
4. Valores monetários são armazenados com precisão de 2 casas decimais
5. As taxas são arredondadas usando RoundingMode.HALF_UP (arredondamento matemático)

## Build do Projeto

```bash
# Compilar o projeto
mvn clean install

# Gerar JAR executável
mvn package

# Executar o JAR
java -jar target/agendamento-transferencias-1.0.0.jar
```
# Decisões Arquiteturais

## Decisão: Arquitetura em Camadas (Layered Architecture)

```
┌─────────────────────────────────────┐
│         Controller Layer            │  ← Exposição da API REST
├─────────────────────────────────────┤
│          Service Layer              │  ← Lógica de negócio
├─────────────────────────────────────┤
│        Repository Layer             │  ← Acesso a dados
├─────────────────────────────────────┤
│         Database (H2)               │  ← Persistência
└─────────────────────────────────────┘
```

### Por que essa arquitetura?

 **Separação de Responsabilidades (SoC)**
- Cada camada tem uma responsabilidade específica
- Facilita manutenção e testes
- Controller não conhece detalhes do banco
- Service não conhece detalhes HTTP

 **Facilita Testes Unitários**
```java
// Posso testar TaxaService sem precisar do banco ou HTTP
TaxaService service = new TaxaService();
BigDecimal taxa = service.calcularTaxa(...);
```

 **Reutilização de Código**
```java
// TaxaService pode ser usado por diferentes controllers
TransferenciaService usa TaxaService
AgendamentoService usa TaxaService (futuro)
```

### Service Layer

```java
@Service
public class TransferenciaService {
    // Responsabilidades:
    // - Lógica de negócio
    // - Orquestração de operações
    // - Transações
    // - Conversão de DTOs
}

@Service
public class TaxaService {
    // Responsabilidades:
    // - Cálculo de taxas (regra de negócio complexa)
    // - Isolada para facilitar testes
}
```

**Por que dois Services?**
- **Single Responsibility Principle (SRP)**
- TaxaService tem UMA responsabilidade: calcular taxas
- TransferenciaService orquestra a operação completa
- Facilita reutilização e testes

### Repository Layer

```java
@Repository
public interface TransferenciaRepository extends JpaRepository<Transferencia, Long> {
    // Responsabilidades:
    // - Acesso ao banco de dados
    // - Queries específicas
}
```

**Por que usar Spring Data JPA?**
-  Reduz código boilerplate (não preciso escrever SQL)
-  Queries derivadas dos nomes dos métodos
-  Paginação nativa
-  Auditoria automática possível

### Lombok

**Por quê?**
-  Reduz código boilerplate (getters/setters)
-  Melhora legibilidade
-  Menos erros (não esqueço getter)

### Bean Validation
**Por quê?**
-  Validações declarativas
-  Menos código
-  Mensagens de erro padronizadas
-  Reutilizável

```java
@NotBlank(message = "Conta de origem é obrigatória")
@Pattern(regexp = "\\d{10}", message = "Deve ter 10 dígitos")
private String contaOrigem;
```
---

## Padrões de Projeto Aplicados
### DTO Pattern (Data Transfer Object)
**Por quê?**
-  Desacopla API da entidade
-  Controla o que é exposto
-  Evita lazy loading issues

```java
// Entidade (banco)
@Entity
public class Transferencia { ... }

// DTO Request (recebe do cliente)
public class TransferenciaDTO { ... }

// DTO Response (retorna ao cliente)
public class TransferenciaResponseDTO { ... }
```

**Vantagens:**
1. **Segurança:** Não expõe campos internos
2. **Flexibilidade:** API pode mudar sem afetar banco
3. **Performance:** Controla serialização JSON

**Exemplo prático:**
```java
// Entidade tem ID gerado automaticamente
// DTO Request NÃO tem ID (usuário não envia)
// DTO Response TEM ID (retornamos ao usuário)
```
### Builder Pattern (via Lombok)

**Por quê?**
-  Criação de objetos mais legível
-  Imutabilidade (se necessário)
-  Parâmetros opcionais

```java
TransferenciaResponseDTO response = TransferenciaResponseDTO.builder()
    .id(1L)
    .contaOrigem("1234567890")
    .contaDestino("0987654321")
    .valorTransferencia(new BigDecimal("1000.00"))
    .taxa(new BigDecimal("82.00"))
    .build();
```

### Strategy Pattern (Implícito no TaxaService)

**Por quê?**
-  Diferentes estratégias de cálculo de taxa
-  Fácil adicionar novas regras

```java
// Poderia ser refatorado para:
interface TaxaStrategy {
    boolean aplica(long dias);
    BigDecimal calcular(BigDecimal valorTransferencia);
}

public class TaxaDia0Strategy implements TaxaStrategy { ... }
public class TaxaDe11a20DiasStrategy implements TaxaStrategy { ... }
```
### BigDecimal para Valores Monetários

**Por quê?**
-  Precisão exata (não usa ponto flutuante)
-  Evita erros de arredondamento
-  Padrão para cálculos financeiros

### LocalDate para Datas

**Por quê?**
-  API moderna (Java 8+)
-  Imutável (thread-safe)
-  Sem hora/fuso horário (apenas data)
-  Melhor que Date/Calendar antigos
---

## Validações e Regras de Negócio

### Validação em Múltiplas Camadas

```
┌─────────────────────────────────────┐
│  Controller: Bean Validation        │ ← @Valid, @NotBlank, @Pattern
├─────────────────────────────────────┤
│  Service: Regras de Negócio         │ ← Cálculo de taxa, datas
├─────────────────────────────────────┤
│  Database: Constraints               │ ← NOT NULL, unique
└─────────────────────────────────────┘
```

**Por que validar em várias camadas?**
- **Defense in Depth** (defesa em profundidade)
- Cada camada protege contra diferentes problemas

## Tratamento de Erros

### GlobalExceptionHandler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    // Captura todas as exceções da aplicação
}
```
**Por quê?**
-  Centraliza tratamento de erros
-  Respostas padronizadas
-  Separa erro de lógica de negócio

### ErrorResponse Padronizado

```json
{
  "timestamp": "2025-10-26 10:30:00",
  "status": 400,
  "error": "Taxa não aplicável",
  "message": "Descrição detalhada",
  "path": "/api/transferencias"
}
```

**Por quê?**
-  Cliente sempre recebe mesma estrutura
-  Facilita debug
-  Melhor UX no front-end

### Exceções Customizadas

```java
public class TaxaNaoAplicavelException extends RuntimeException {
    // Exceção específica do domínio
}
```

**Por quê?**
-  Semântica clara
-  Tratamento específico
-  Mensagens de erro melhores

---
