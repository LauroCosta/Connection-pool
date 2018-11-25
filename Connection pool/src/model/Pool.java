package model;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public class Pool extends Conexao {

    private final int MIN = 4;
    private final int MAX = 10;
    private int total = this.MIN;

    private ArrayList<Connection> conexoes;

    public Pool() {

        this.setConexoes(new ArrayList<>());
        this.abrirConexoes(this.getMIN());

    }

    private void abrirConexoes(int min) {

        for (int i = 0; i < min; i++) {
            this.getConexoes().add(this.getConnection());
        }

    }

    public Connection usarConexao() {

        if (this.conexoes.size() != 0) {

            return this.conexoes.remove(0);

        } else if (this.getTotal() < this.getMAX()) {

            this.getConexoes().add(this.getConnection());
            this.setTotal(this.getTotal() + 1);

            return this.conexoes.remove(0);

        }

        return null;
    }

    public void devolverConexao(Connection conn) {

        try {

            if (this.getConexoes().size() < this.getMIN()) {
                this.conexoes.add(conn);

            } else {

                conn.close();
                this.setTotal(this.getTotal() - 1);
            }

        } catch (SQLException ex) {

        }
    }

    public ArrayList<Connection> getConexoes() {
        return conexoes;
    }

    private void setConexoes(ArrayList<Connection> conexoes) {
        this.conexoes = conexoes;
    }

    private int getMIN() {
        return MIN;
    }

    private int getMAX() {
        return MAX;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

}
