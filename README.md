# Teste Desenvolvedor Backend Java Jr. - tgid
Esse sistema foi criado para o teste da vaga para desenvolvedor Java na tgid.
O sistema consiste de uma aplicação Spring Boot, que utiliza também um banco de dados em memória H2 e é consumida através do Postman.
A aplicação possui duas classes: Cliente e Empresa.

Ambas as classes podem ser cadastradas no sistema através de um controlador REST do Spring Web. O cliente é cadastrado através de um DTO e um Service, e possui como um dos seus atributos a empresa a qual sua conta está atrelada.
É importante ressaltar que tanto o CPF do cliente, quanto o CNPJ da empresa são validados através das anotações do Spring Validator. E todos os atributos possuem validadores que impedem que eles sejam nulos, por exemplo.

![image](https://github.com/lucas-olmelo/java-teste-developer/assets/112345359/093f3a09-26c6-4c94-9448-dbfb1cd0ad01)
![image](https://github.com/lucas-olmelo/java-teste-developer/assets/112345359/c4510ab5-ea47-4ba4-b175-0ba866299dce)

Após o cadastro da empresa e do cliente é possível realizar as operações de Saque e Depósito através de um Service. Cada empresa possui uma taxa de sistema que é cadastrada junto a ela, e é abatida do saldo da empresa em cada transação.
Após a realização de uma operação, um email é enviado tanto para o cliente quanto para a empresa confirmando a transação, que é enviado através de um Service que utiliza o JavaMailSender do Spring Mail e o servidor do Gmail.

![Captura de tela 2024-03-11 164225](https://github.com/lucas-olmelo/java-teste-developer/assets/112345359/2403b57a-d5f1-4186-8e37-05e4fd2f06db)
![Captura de tela 2024-03-11 164300](https://github.com/lucas-olmelo/java-teste-developer/assets/112345359/4b5ec6ec-891a-4dff-b724-c887d580980e)

Após essas transações, os saldos de cliente e empresa são atualizados no banco de dados H2.

![Captura de tela 2024-03-11 164512](https://github.com/lucas-olmelo/java-teste-developer/assets/112345359/c6017d16-b280-4ee8-b1df-6cb78fed38df)
![Captura de tela 2024-03-11 164550](https://github.com/lucas-olmelo/java-teste-developer/assets/112345359/bf16abd9-48ed-4ece-a50c-e051ded3a150)
