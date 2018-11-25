package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CrudModel {

    private String historicoUso;
    private String historicoConexoes;
    private Pool pool;
    private ArrayList<Observer> observers;

    public CrudModel() {

        this.setPool(new Pool());
        this.setHistoricoUso("Histórico Connection Pool");
        this.setHistoricoConexoes("Disp.:     Em uso:");
        this.setObservers(new ArrayList<Observer>());

    }

    public boolean salvar(Contato contato) {

        Connection con = this.getPool().usarConexao();
        
        if(con == null){
            return false;
        }
        
        this.setHistoricoUso(historicoUso + "\n" + "Usando: insert contato");
        this.setHistoricoConexoes(historicoConexoes + "\n" + this.getPool().getConexoes().size() + "          "
                + (this.getPool().getTotal() - this.getPool().getConexoes().size()));

        PreparedStatement stmt = null;

        try {

            stmt = con.prepareStatement("INSERT INTO contato (nome,telefone,email)VALUES(?,?,?)");

            stmt.setString(1, contato.getNome());
            stmt.setString(2, contato.getTelefone());
            stmt.setString(3, contato.getEmail());

            stmt.executeUpdate();

        } catch (SQLException ex) {
            return false;
        } finally {

            //fazendo o natify antes, pra obrigar usar outra conexao
            this.notifyALL();
            this.getPool().devolverConexao(con);
            con = null;
            this.setHistoricoUso(historicoUso + "\n" + "Devolvendo: insert ok!");
            this.setHistoricoConexoes(historicoConexoes + "\n" + this.getPool().getConexoes().size() + "          "
                    + (this.getPool().getTotal() - this.getPool().getConexoes().size()));
            this.getObservers().get(0).update();
            
        }
        return true;
    }

    public boolean editar(Contato p) {

        Connection con = this.getPool().usarConexao();
        
        if(con == null){
            return false;
        }
        
        this.setHistoricoUso(historicoUso + "\n" + "Usando: update contato");
        this.setHistoricoConexoes(historicoConexoes + "\n" + this.getPool().getConexoes().size() + "          "
                + (this.getPool().getTotal() - this.getPool().getConexoes().size()));

        PreparedStatement stmt = null;

        try {
            stmt = con.prepareStatement("UPDATE contato SET nome = ?,telefone = ?, email = ? WHERE id = ?");
            stmt.setString(1, p.getNome());
            stmt.setString(2, p.getTelefone());
            stmt.setString(3, p.getEmail());

            stmt.setInt(4, p.getId());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            return false;
        } finally {

            this.getPool().devolverConexao(con);
            con = null;
            this.setHistoricoUso(historicoUso + "\n" + "Devolvendo: update ok!");
            this.setHistoricoConexoes(historicoConexoes + "\n" + this.getPool().getConexoes().size() + "          "
                    + (this.getPool().getTotal() - this.getPool().getConexoes().size()));
            this.notifyALL();
        }
        return true;
    }

    public boolean excluir(int id) {

        Connection con = this.getPool().usarConexao();
        
        
        if(con == null){
            return false;
        }
        this.setHistoricoUso(historicoUso + "\n" + "Usando: delete contato");
        this.setHistoricoConexoes(historicoConexoes + "\n" + this.getPool().getConexoes().size() + "          "
                + (this.getPool().getTotal() - this.getPool().getConexoes().size()));

        PreparedStatement stmt = null;

        try {

            stmt = con.prepareStatement("DELETE FROM contato WHERE id = ?");
            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException ex) {

            return false;

        } finally {

            this.getPool().devolverConexao(con);
            con = null;
            this.setHistoricoUso(historicoUso + "\n" + "Devolvendo: delete ok!");
            this.setHistoricoConexoes(historicoConexoes + "\n" + this.getPool().getConexoes().size() + "          "
                    + (this.getPool().getTotal() - this.getPool().getConexoes().size()));
            this.notifyALL();
        }
        return true;
    }

    public Contato buscar(int id) {

        Connection con = this.getPool().usarConexao();

        
        if(con == null){
            return null;
        }
        
        PreparedStatement stmt = null;
        ResultSet rs = null;

        Contato c1 = null;

        try {
            stmt = con.prepareStatement("SELECT * FROM contato WHERE id = ?");
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {

                c1 = new Contato();

                c1.setId(rs.getInt("id"));
                c1.setNome(rs.getString("nome"));
                c1.setTelefone(rs.getString("telefone"));
                c1.setEmail(rs.getString("email"));

            }

        } catch (SQLException ex) {

            return null;

        } finally {

            this.getPool().devolverConexao(con);
            con = null;
            this.setHistoricoConexoes(historicoConexoes + "\n" + this.getPool().getConexoes().size() + "          "
                    + (this.getPool().getTotal() - this.getPool().getConexoes().size()));

            this.notifyALL();
        }

        return c1;

    }

    public ArrayList<Contato> lista() {

        Connection con = this.getPool().usarConexao();
        
        if(con == null){
            return null;
        }
        
        this.setHistoricoUso(historicoUso + "\n" + "Usando: Listar contatos");
        this.setHistoricoConexoes(historicoConexoes + "\n" + this.getPool().getConexoes().size() + "          "
                + (this.getPool().getTotal() - this.getPool().getConexoes().size()));

        PreparedStatement stmt = null;
        ResultSet rs = null;

        ArrayList<Contato> contatos = new ArrayList<>();

        try {
            stmt = con.prepareStatement("SELECT * FROM contato");
            rs = stmt.executeQuery();

            while (rs.next()) {

                Contato c1 = new Contato();

                c1.setId(rs.getInt("id"));
                c1.setNome(rs.getString("nome"));
                c1.setTelefone(rs.getString("telefone"));
                c1.setEmail(rs.getString("email"));

                contatos.add(c1);
            }

        } catch (SQLException ex) {

            return null;

        } finally {

            this.getPool().devolverConexao(con);
            con = null;
            this.setHistoricoUso(historicoUso + "\n" + "Devolvendo: listar ok!");
            this.setHistoricoConexoes(historicoConexoes + "\n" + this.getPool().getConexoes().size() + "          "
                    + (this.getPool().getTotal() - this.getPool().getConexoes().size()));

            this.getObservers().get(0).update();
        }
        return contatos;
    }

    public Pool getPool() {
        return pool;
    }

    public void setPool(Pool pool) {
        this.pool = pool;
    }

    public ArrayList<Observer> getObservers() {
        return observers;
    }

    public void setObservers(ArrayList<Observer> observers) {
        this.observers = observers;
    }

    public String getHistoricoUso() {
        return historicoUso;
    }

    public void setHistoricoUso(String historico) {
        this.historicoUso = historico;
    }

    public void addObserver(Observer ob) {
        this.getObservers().add(ob);
    }

    public String getHistoricoConexoes() {
        return historicoConexoes;
    }

    public void setHistoricoConexoes(String historicoConexoes) {
        this.historicoConexoes = historicoConexoes;
    }

    private void notifyALL() {

        for (Observer observer : observers) {
            observer.update();
        }
    
    }
    
}
