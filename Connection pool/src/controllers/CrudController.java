package controllers;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import model.Contato;
import model.CrudModel;
import model.Observer;
import views.CrudView;

public class CrudController implements Observer {

    private CrudView view;
    private CrudModel model;

    public CrudController(CrudView view, CrudModel model) {

        this.setView(view);
        this.setModel(model);
        this.getModel().addObserver(this);

    }

    public CrudView getView() {
        return view;
    }

    public void setView(CrudView view) {

        if (view != null) {
            this.view = view;
        }
    }

    public CrudModel getModel() {
        return model;
    }

    public void setModel(CrudModel model) {
        if (model != null) {
            this.model = model;
        }
    }

    public void tratarEvento(ActionEvent evt) {

        switch (evt.getActionCommand()) {

            case "salvar":

     
                if (this.validarFormulario(this.dadosFormulario()) != null) {
                        this.getView().notificaUsuario(this.validarFormulario(this.dadosFormulario()));
                        break;
                }
                
                if (this.getModel().salvar(this.dadosFormulario())) {
                    this.getView().notificaUsuario("Contato salvo com sucesso: \n");
                } else {
                    this.getView().notificaUsuario("Não foi possivel salvar o contato");
                }

                break;

            case "novo":

                this.getView().getBtEditar().setText("Editar");
                 this.getView().getBtEditar().setActionCommand("editar");
                this.desabilitaCampos(true);
                this.limparForm();

                break;

            case "excluir":

                if (this.getModel().excluir(this.getTabelaSelecionado().getId())) {
                    this.getView().notificaUsuario("Contato excluído com sucesso!");
                } else {
                    this.getView().notificaUsuario("Contato não foi excluído, tente novamente...");
                }

                break;

            case "editar":

                this.desabilitaCampos(true);
                this.getView().getBtExcluir().setEnabled(false);
                this.getView().getBtSalvar().setEnabled(false);
                this.getView().getBtEditar().setActionCommand("salvarAlt");
                this.getView().getBtEditar().setText("Salvar");
                
               
                break;

            case "salvarAlt":

                if (this.validarFormulario(this.dadosFormulario()) != null) {
                    this.getView().notificaUsuario(this.validarFormulario(this.dadosFormulario()));
                    break;

                } else {

                    this.desabilitaCampos(false);
                    this.getView().getBtExcluir().setEnabled(true);
          
                    this.getView().getBtEditar().setActionCommand("editar");
                    this.getView().getBtEditar().setText("Editar");

                    Contato contato = this.dadosFormulario();
                    
                    contato.setId(this.getTabelaSelecionado().getId());
                    
                    if (this.getModel().editar(contato)) {
                        this.getView().notificaUsuario("Contato alterado com sucesso!");
                    } else {
                        this.getView().notificaUsuario("Contato não alterado, tente novamente...");
                    }
                }

                break;

        }
    }

    public void tratarTecladoTabela(KeyEvent evt) {

        
        this.desabilitaCampos(false);
        this.getTabelaSelecionado();
    }

    public void tratarTecladoMouse(MouseEvent evt) {

 
          this.desabilitaCampos(false);
          this.getTabelaSelecionado();

    }

    private String validarFormulario(Contato form) {

        if (form.getNome().length() < 2) {
            return "Campo nome é obrigatório";
        }

        if (form.getTelefone().indexOf(" ") != -1) {
            return "Digite um telefone válido";
        }

        if (form.getEmail().indexOf("@") == -1) {
            return "Digite um Email válido";
        }

        return null;
    }

    private void limparForm() {

        this.getView().getCampoNome().setText("");
        this.getView().getCampoTelefone().setText("");
        this.getView().getCampoEmail().setText("");
        this.getView().getBtSalvar().setEnabled(true);
        this.getView().getBtEditar().setEnabled(false);
        this.getView().getBtExcluir().setEnabled(false);
        this.getView().getTabelaDados().clearSelection();

    }

    public void preencherTabela() {

        DefaultTableModel modelo = (DefaultTableModel) this.getView().getTabelaDados().getModel();
        modelo.setNumRows(0);

        ArrayList<Contato> contatos = this.getModel().lista();

        if (contatos != null) {
            for (Contato c : contatos) {

                modelo.addRow(new Object[]{
                    c.getId(),
                    c.getNome(),
                    c.getTelefone(),
                    c.getEmail()

                });
            }

        }
        this.limparForm();
    }

    private Contato getTabelaSelecionado() {

        if (this.getView().getTabelaDados().getSelectedRow() != -1) {

            this.getView().getBtSalvar().setEnabled(false);
            this.getView().getBtEditar().setEnabled(true);
            this.getView().getBtExcluir().setEnabled(true);

            int id = (int) this.getView().getTabelaDados().getValueAt(this.getView().getTabelaDados().getSelectedRow(), 0);
            String nome = (String) this.getView().getTabelaDados().getValueAt(this.getView().getTabelaDados().getSelectedRow(), 1);
            String telefone = (String) this.getView().getTabelaDados().getValueAt(this.getView().getTabelaDados().getSelectedRow(), 2);
            String email = (String) this.getView().getTabelaDados().getValueAt(this.getView().getTabelaDados().getSelectedRow(), 3);

            this.getView().getCampoNome().setText(nome);
            this.getView().getCampoTelefone().setText(telefone);
            this.getView().getCampoEmail().setText(email);

 
            return new Contato(id, nome, telefone, email);

        }
        return null;
    }

    private Contato dadosFormulario() {

        Contato contato = new Contato();

        contato.setNome(this.getView().getCampoNome().getText());
        contato.setTelefone(this.getView().getCampoTelefone().getText());
        contato.setEmail(this.getView().getCampoEmail().getText());

        return contato;
    }
    
    private void desabilitaCampos(boolean b){
        
        this.getView().getCampoNome().setEditable(b);
        this.getView().getCampoTelefone().setEditable(b);
        this.getView().getCampoEmail().setEditable(b);
   
    }
    

    private boolean isCadastrado(Contato cont) {

        Contato contato = this.getModel().buscar(cont.getId());

        if (contato == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void update() {
        this.preencherTabela();
        this.limparForm();
    }

}
