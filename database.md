### Banco de Dados

1. **Usuários** (users)
   - **ID**: Chave primária, identificador único para cada usuário.
   - **Username**: Nome de usuário único para login.
   - **Email**: Endereço de email do usuário, deve ser único.
   - **Password**: Senha criptografada para login.
   - **Enabled**: Indica se o usuário está ativo.
   - **Account_non_expired**: Indica se a conta do usuário não está expirada.
   - **Credentials_non_expired**: Indica se as credenciais do usuário não estão expiradas.
   - **Account_non_locked**: Indica se a conta do usuário não está bloqueada.
   - **Role_id**: Chave estrangeira referenciando a tabela de roles.
   - **Created_at**: Data de criação da conta.
   - **Updated_at**: Data de última atualização da conta.

   ```sql
   CREATE TABLE users (
       id SERIAL PRIMARY KEY,
       username VARCHAR(50) UNIQUE,  -- Nome de usuário único
       email VARCHAR(100) UNIQUE,
       password VARCHAR(255),
       enabled BOOLEAN DEFAULT TRUE,  -- Indica se o usuário está ativo
       account_non_expired BOOLEAN DEFAULT TRUE,  -- Conta não expirada
       credentials_non_expired BOOLEAN DEFAULT TRUE,  -- Credenciais não expiradas
       account_non_locked BOOLEAN DEFAULT TRUE,  -- Conta não bloqueada
       role_id INTEGER REFERENCES roles(id),  -- Chave estrangeira para roles
       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,  -- Data de criação da conta
       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP  -- Data de última atualização da conta
   );
   ```

2. **Roles** (roles)
   - **ID**: Chave primária, identificador único para cada role.
   - **Nome**: Nome do papel (ADMIN, COMMON_USER).

   ```sql
   CREATE TABLE roles (
       id SERIAL PRIMARY KEY,
       name VARCHAR(50)
   );
   ```

3. **Tópicos de Votação** (topics)
   - **ID**: Chave primária, identificador único para cada tópico.
   - **Título**: Título do tópico.
   - **Descrição**: Descrição detalhada do tópico.
   - **Data de Início**: Data e hora de início da votação.
   - **Data de Término**: Data e hora de término da votação.
   - **Privado**: Indica se a votação é privada.
   - **Senha**: Senha para acessar a votação, se privada.

   ```sql
   CREATE TABLE topics (
       id SERIAL PRIMARY KEY,
       title VARCHAR(255),
       description TEXT,
       start_time TIMESTAMP,
       end_time TIMESTAMP,
       private BOOLEAN DEFAULT FALSE,  -- Indica se a votação é privada
       password VARCHAR(255)  -- Senha para acessar a votação, se privada
   );
   ```

4. **Opções de Votação** (options)
   - **ID**: Chave primária, identificador único para cada opção.
   - **Descrição**: Descrição da opção de voto.
   - **ID do Tópico**: Chave estrangeira referenciando a tabela de tópicos.

   ```sql
   CREATE TABLE options (
       id SERIAL PRIMARY KEY,
       description VARCHAR(255),
       topic_id INTEGER REFERENCES topics(id)
   );
   ```

5. **Votos** (votes)
   - **ID**: Chave primária, identificador único para cada voto.
   - **ID do Eleitor**: Chave estrangeira referenciando a tabela de usuários.
   - **ID da Opção**: Chave estrangeira referenciando a tabela de opções.
   - **ID do Tópico**: Chave estrangeira referenciando a tabela de tópicos.
   - **Data do Voto**: Data e hora em que o voto foi registrado.

   ```sql
   CREATE TABLE votes (
       id SERIAL PRIMARY KEY,
       voter_id INTEGER REFERENCES users(id),
       option_id INTEGER REFERENCES options(id),
       topic_id INTEGER REFERENCES topics(id),
       vote_time TIMESTAMP
   );
   ```

### Relacionamentos e Lógica Aprimorada

- A tabela `users` possui um relacionamento de muitos-para-um com a tabela `roles` através da chave estrangeira `role_id`.
- A tabela `topics` possui um relacionamento de um-para-muitos com a tabela `options`.
- A tabela `votes` possui chaves estrangeiras referenciando tanto `users`, `options` quanto `topics`.