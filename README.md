# kotlin-mobile-test

Este aplicativo foi desenvolvido com manipulação de dados em memoria com o objetivo de realizar uma 
simulação de dados bancários divididos em conta corrente e conta poupança, nestes dois casos, será possivel transferir valores em R$ 
de sua conta para conta de algum contato adicionado, por motivos de testes de inteface e manipulação mais dinamica dos dados, adicionei
dados fakes de contatos, como nome, numero da conta, nome do banco e etc.

De inicio, é preciso saber as restrições de cada tipo de conta que será atribuida nos *Usuarios* e também estará presente nos seus Contatos,

### Criação das telas
O aplicativo possui no total 2 telas.
- **activity_contatos**
essa activity reune todos os dados no viewHolder da RecyclerView, ainda temos que organizar por nome cada novo contato adicionado. Complicando um pouco do processo de organização das posições e dos dados na recyclerview.

<img src= "https://user-images.githubusercontent.com/43412432/62231771-14190e80-b39b-11e9-8c59-41ae58fb06c8.png" height="400" width="250" hspace="50">

- **activity_information_contact**
essa tela é util para confirmar alguns dados de cada usuario especifico, como o número da conta e para qual banco esta sendo enviado.
nesta tela também será possivel escolher a quantia que será depositada, ao clicar no botão "depositar", aparacerá uma mensagem de confirmação do processo.

<img src= "https://user-images.githubusercontent.com/43412432/62232686-314edc80-b39d-11e9-96c6-8414412b5835.png" height="400" width="250" hspace="50">


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
### Requisitos

para uma inserção dinamica inserida na recyclerview, criei um classe ContactAdapter, responsável pelo gerenciamento de itens que serão inseridos na recyclerview, juntamente com a implementação de um viewHolder que, como o nome sugere, irá segurar uma view dentro do adapter e também será rensposável por reciclar os itens, em uma inserção/exclusão, etc.

```kt

class ContactAdapter(val contacts: MutableList<Contact>): RecyclerView.Adapter<ContactAdapter.ContactViewHolder> (){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.contact_item, parent, false)
        return ContactViewHolder(inflate)
    }

    override fun getItemCount(): Int = contacts.size

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        holder.bind(contacts[position])
    }

    inner class ContactViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(contact: Contact) {
            with(contact){
                itemView.txt_name_contact.text = name
            }
        }
    }
}

```

### Funcionamento

- Na primeira tela (activity_contatos), é possivel adicionar um novo contato, ao clicar no fab_button o aplicativo irá executar essa função:

```kt

    private fun addContact() {
        val unique = Fakeit.getUniqueValue()
        var new_contact = contact {
            name = Fakeit.name().firstName()+" "+ Fakeit.name().lastName()
            bank = Fakeit.bank().name()
            agency = Fakeit.bank().bankCountryCode()
            account = Fakeit.bank().ibanDigits()
            current_account = unique
            savings_account = !unique
        }
        myContacts.add(0,new_contact)
        var sortedContacts: List<Contact> = myContacts.sortedBy{a -> a.name}
        var indice = sortedContacts.indexOf(new_contact);
        adapter.contacts.add(indice, new_contact)
        adapter.notifyItemInserted(indice)
    }

```

é possivel verificar 2 estagios integrados, a criação de dados fake atráves da biblioteca Fakeit como já havia mencionado anteriormente, e a inserção do novo contato em meu adapter.

- Na segunda tela (activity_information_contact), é possivel visualizar os dados de cada contato através de um parcelable e por fim acessar os dados do usuario principal para transferir o valor de Usuario.current_ballance -> Contato.current_ballance ou Usuario.saving_ballance -> Contato.saving_ballance, Contato e Usuario são instancias genéricas para cada ligação. na pratica é uma instância que vem de um parcelable Contact e uma instancia de User.

### Testes de Unidade
Para realizar os codigos de teste, utilizei um modulo do junit chamado jupiter que é amplamente utilizado para realizar testes dinamicos e simples, não vi necessidade de utilizar testes mais complexos na aplicação.

O meu objetivo nessa etapa foi basicamente atingir 5 funções que julguei serem essenciais:

- verificar se eu consigo adicionar contatos a lista de contatos.
- verificar se eu consigo transferir e debitar da minha conta valores determinados.
- verificar se o app impedirá a transferência caso você tente enviar um valor que não está em conta.
- verificar a lógica de limite diário de transferências de R$10.000 por dia, caso exceda esse limite, o app deverá bloquear as tranferências e aguardar até o próximo dia.
- verificar se já se passou um dia desde o bloqueio da transferência.

com esses testes posso garantir todos os requisitos solicitados no desafio.

realizei a lógica somente para contas poupança, pois a lógica para contas corrente seria a mesma e não vi necessidade de refazer um mesmo teste.


