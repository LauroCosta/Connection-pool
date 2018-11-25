package model;




public class Contato {
    
    private int id;
    private String nome;
    private String telefone;
    private String email;

    public Contato(int id, String nome, String telefone, String email) {
       
        this.setId(id);
        this.setNome(nome);
        this.setTelefone(telefone);
        this.setEmail(email);
     
    }

    public Contato() {
        super();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }    

    @Override
    public String toString() {
        return "Nome: " + nome + "\nTelefone: " + telefone + "\nEmail: " + email;
    }

}