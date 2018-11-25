
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UnsupportedLookAndFeelException;
import model.Pool;
import model.CrudModel;
import views.CrudView;
import views.PoolView;


public class Main {
   
    static void startView(javax.swing.JFrame view){

      
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Metal".equals(info.getName())) {

                    try {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    } catch (ClassNotFoundException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (InstantiationException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalAccessException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (UnsupportedLookAndFeelException ex) {
                        Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                }
            }
   

        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                view.setVisible(true);
            }
        });

    }
    
    public static void main(String[] args){
         
        CrudModel model = new CrudModel();
        
        PoolView pool = new PoolView(model);
        CrudView crud = new CrudView(model);
//        CrudView crud2 = new CrudView(model);
        
        startView(pool);
        startView(crud);
     //   startView(crud2);
          
    }
}
