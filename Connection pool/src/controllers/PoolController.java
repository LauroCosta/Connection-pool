package controllers;

import javax.swing.event.ChangeEvent;
import model.CrudModel;
import model.Observer;
import views.PoolView;

public class PoolController implements Observer{
    
    private PoolView view;
    private CrudModel model;

    public PoolController(PoolView view, CrudModel model) {
   
        this.setView(view);
        this.setModel(model);
        this.getModel().addObserver(this);
     
 
    }

    public PoolView getView() {
        return view;
    }

    public void setView(PoolView view) {
        if(view != null)
            this.view = view;
    }

    public CrudModel getModel() {
        return model;
    }

    public void setModel(CrudModel model) {
        if(model != null)
            this.model = model;
    }    


    @Override
    public void update() {
        
     this.getView().getCampoHistorico().setText(this.getModel().getHistoricoUso());
     this.getView().getTxConexoes().setText(this.getModel().getHistoricoConexoes());
        
       // this.getView().getLbDisponivel().setText(this.getModel().getPool().getLimite() +"");
      
    }
}
