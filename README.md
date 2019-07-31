# kotlin-mobile-test

Este aplicativo foi desenvolvido com manipulação de dados em memoria com o objetivo de realizar uma 
simulação de dados bancários divididos em conta corrente e conta poupança, nestes dois casos, será possivel transeferir valores em R$ 
de sua conta para conta de algum contato adicionado, por motivos de testes de inteface e manipulação mais dinamica dos dados, adicionei
dados fakes de contatos, como nome, numero da conta, nome do banco e etc.

De inicio, é preciso saber as restrições de cada tipo de conta que será atribuida nos *Usuarios* e também estará presente nos seus Contatos,

## **conta corrente**
 - Uma conta corrente é uma inscrição em instituição bancária que dá direito a utilizar-lhe os serviços (p.ex., receber salário, guardar dinheiro,
 emitir cheques,fazer investimentos etc.).

##  **conta poupança**

- Em uma conta poupança os serviços são mais restritos e não é possivel receber salário neste tipo de conta.


### Usuário:

Já imaginando que iria utilizar os dados iniciais do meu usuario para armazenar valores como nome, banco e principalmente valor da conta.
decide criar uma classe de Modelo, que seviria para instanciar de algum banco de dados. mas neste caso, utilizei dados armazenados na propria memoria,
por isso a necessidade de uma função para criar um usuario que será utilizado posteriormente para debitar o valor

```kt

@Parcelize
data class User(
    val name: String,
    val bank: String,
    val agency: String,
    val account: String,
    val current_account: Boolean,
    val savings_account: Boolean,
    var current_ballance: Float,
    val savings_ballance: Float
): Parcelable

fun CreateUser(): User = User(
    "Vinicius Mesquita",
    "itau",
    "001",
    "550.222.221-444",
    true,
    true,
    100000f,
    1000f
)

```

### Contato:

Seguindo a mesma lógica de Usuário, mas neste caso iremos utilizar uma biblioteca chamada fakeit, tanto para facilitar a visualização dos dados na recycler view
quanto para facilitar nosso trabalho na criação dos contatos.

```kt

@Parcelize
data class Contact(
    val name: String,
    val bank: String,
    val agency: String,
    val account: String,
    val current_account: Boolean,
    val savings_account: Boolean,
    var current_ballance: Float,
    val savings_ballance: Float
): Parcelable

class ContactBuilder {
    var name = ""
    var bank = ""
    var agency = "001"
    var account = ""
    var current_account = false
    var savings_account = false
    var current_ballance = 0.0f
    var savings_ballance = 0.0f

    fun build(): Contact = Contact(name,bank,agency,account, false, false, current_ballance, savings_ballance)
}
fun contact(block: ContactBuilder.() -> Unit): Contact = ContactBuilder().apply(block).build()

fun fakeContacts():MutableList<Contact> = mutableListOf(
    contact {
        name = "Vinicius Mesquita"
        bank = "itau"
        agency = "011"
        account = "550.222.221-444"
        current_account = true
        savings_account = false
    }
)

```

### Criação das telas
O aplicativo possui no total 2 telas.
- **activity_contatos**
talvez a tela mais complexa, pois reune todos os dados no viewHolder da RecyclerView,ainda temos que organizar por nome cada novo contato adicionado. Complicando um pouco do processo de organização das posições e dos dados na recyclerview.

<img src= "https://user-images.githubusercontent.com/43412432/62231771-14190e80-b39b-11e9-8c59-41ae58fb06c8.png" height="400" width="250">

- **activity_information_contact**
essa tela é util para confirmar alguns dados de cada usuario especifico, como o número da conta e para qual banco esta sendo enviado.
nesta tela também será possivel escolher a quantia que será depositada, ao clicar no botão "depositar", aparacerá uma mensagem de confirmação do processo.

<img src= "https://user-images.githubusercontent.com/43412432/62232686-314edc80-b39d-11e9-96c6-8414412b5835.png" height="400" width="250">
