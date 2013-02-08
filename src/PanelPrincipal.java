
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import org.joda.time.DateTime;

//@author Daniel Meza
 
public class PanelPrincipal extends JPanel implements Printable {    
     
       JPanel pDatos,pPrevisualizacion;
       JLabel eNombre,eFechaInicial,eFechaFinal; 
       JTextField cNombre,cFechaInicial,cFechaFinal;
       JButton bImprimir,bGenerarVistaPrevia;
       JTable tPrevisualizacion = new JTable(new DefaultTableModel());              
       JScrollPane spt;       
       DefaultTableModel modelo = (DefaultTableModel)tPrevisualizacion.getModel();              
       static List<Object[]> listaDatos = new ArrayList<Object[]>();
       
       String[] meses = {"","Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre"};
       
       GridBagConstraints gbc;
     
       public PanelPrincipal(){
                          
              pDatos = new JPanel(new GridBagLayout());              
              pDatos.setBorder(new LineBorder(Color.BLACK, 1));                            
              
              eNombre = new JLabel("Nombre : ");
              eFechaInicial = new JLabel("Fecha Inicial : ");
              eFechaFinal = new JLabel("Fecha Final : ");
              
              cNombre = new JTextField(30);
              cFechaInicial = new JTextField(20);
              cFechaFinal = new JTextField(20);                            
              
              gbc = new GridBagConstraints();
              gbc.gridx = 0;
              gbc.gridy = 0;
              gbc.gridheight = 1;       
              gbc.anchor = GridBagConstraints.WEST;
              gbc.insets = new Insets(5, 5, 5, 5);
              pDatos.add(eNombre,gbc);
              
              gbc = new GridBagConstraints();
              gbc.gridx = 1;
              gbc.gridy = 0;
              gbc.gridheight = 1;              
              gbc.anchor = GridBagConstraints.WEST;
              gbc.insets = new Insets(5, 5, 5, 5);
              pDatos.add(cNombre,gbc);
              
              gbc = new GridBagConstraints();
              gbc.gridx = 0;
              gbc.gridy = 1;
              gbc.gridheight = 1;              
              gbc.anchor = GridBagConstraints.WEST;
              gbc.insets = new Insets(5, 5, 5, 5);
              pDatos.add(eFechaInicial,gbc);
              
              gbc = new GridBagConstraints();
              gbc.gridx = 1;
              gbc.gridy = 1;
              gbc.gridheight = 1;              
              gbc.anchor = GridBagConstraints.WEST;
              gbc.insets = new Insets(5, 5, 5, 5);
              pDatos.add(cFechaInicial,gbc);
              
              gbc = new GridBagConstraints();
              gbc.gridx = 0;
              gbc.gridy = 2;
              gbc.gridheight = 1;              
              gbc.anchor = GridBagConstraints.WEST;
              gbc.insets = new Insets(5, 5, 5, 5);
              pDatos.add(eFechaFinal,gbc);
              
              gbc = new GridBagConstraints();
              gbc.gridx = 1;
              gbc.gridy = 2;
              gbc.gridheight = 1;              
              gbc.anchor = GridBagConstraints.WEST;
              gbc.insets = new Insets(5, 5, 5, 5);
              pDatos.add(cFechaFinal,gbc);
                            
              modelo.addColumn("Nombre Trabajador");
              modelo.addColumn("Fecha");
              modelo.addColumn("Hora Entrada");
              modelo.addColumn("Hora Salida");
              modelo.addColumn("Hora Entrada");
              modelo.addColumn("Hora Salida");                            
              
              bGenerarVistaPrevia = new JButton("Vista Previa");                            
              
              bGenerarVistaPrevia.addActionListener(new ActionListener() {
                           @Override
                           public void actionPerformed(ActionEvent e) {
                               
                                  String nombre = PanelPrincipal.this.cNombre.getText().trim();
                                  ArrayList<Object> alDatos = PanelPrincipal.this.getDatosBase(nombre);
                                  
                                  int diaActual = 0;                                        
                                  
                                  System.out.println( "Tamaño : "  + (alDatos.size() - 1));
                                     
                                  if( (alDatos.size() - 1) == -1 ){
                                      
                                      Object[] opciones = {"Aceptar"};
                                      JOptionPane.showMessageDialog(null,
                                                                    "No existen datos para ese rango de fechas",
                                                                    "No hay datos",                                                                    
                                                                    JOptionPane.WARNING_MESSAGE);   
                                      
                                      return;
                                                                                                                  
                                  }else{
                                  
                                        String fi = PanelPrincipal.this.cFechaInicial.getText().trim();
                                        int dFi = Integer.parseInt(fi.substring(0,2));
                                        int mFi = Integer.parseInt(fi.substring(3,5));
                                        int aFi = Integer.parseInt(fi.substring(6,10));
                                        DateTime dtFi = new DateTime(aFi,mFi,dFi,0,0);
                                  
                                        String ff = PanelPrincipal.this.cFechaFinal.getText().trim();
                                        int dFf = Integer.parseInt(ff.substring(0,2));
                                        int mFf = Integer.parseInt(ff.substring(3,5));
                                        int aFf = Integer.parseInt(ff.substring(6,10));
                                        DateTime dtFf = new DateTime(aFf,mFf,dFf,0,0);
                                        
                                        if( dtFi.isAfter(dtFf) ){
                                            
                                            Object[] opciones = {"Aceptar"};
                                            JOptionPane.showMessageDialog(null,
                                                                          "La fecha inicial no puede ser mayor que la fecha final",
                                                                          "Rango de fechas erroneo",                                                                    
                                                                          JOptionPane.ERROR_MESSAGE
                                                                          );   
                                            return;
                                            
                                        }
                                        
                                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                        ArrayList<DateTime> fechasDia = new ArrayList<>();
                                        
                                        System.out.println(fi + " " + ff);   
                                                                                
                                        Object[] dl = new Object[6];
                                        Object[] datos = new Object[6];
                                        
                                        for( int i = 0; i <= (alDatos.size() - 1); i++ ){                                      
                                             
                                             Object dato = alDatos.get(i);
                                             Date fDato = (Date)dato; 
                                             Date dfi = dtFi.toDate();
                                             Date dff = dtFf.toDate();
                                             
                                             DateTime dttemp = new DateTime(fDato);
                                                 
                                             String fechaFormateada = sdf.format(fDato).trim();
                                             int diateff = Integer.parseInt(fechaFormateada.substring(0,2));
                                             //System.out.println(fDato + " " + dfi + " " + dff + " " + dfi.after(fDato) + " " + dff.before(fDato));                                             
                                             
                                             if( dfi.before(fDato) && dff.after(fDato) ){
                                                 
                                                 System.out.println("Fecha " + fDato );     
                                                                                                                                                   
                                                 int mesff = Integer.parseInt(fechaFormateada.substring(3,5));
                                                 int añoff = Integer.parseInt(fechaFormateada.substring(6,10));
                                                 int horaff = dttemp.getHourOfDay();
                                                 int minutoff = dttemp.getMinuteOfHour();   
                                                 
                                                 if( minutoff < 10 ){
                                                     
                                                 }
                                                 
                                                 DateTime ffdt = new DateTime(añoff,mesff,diateff,horaff,minutoff);                                                                                                                                                                                                    
                                                 
                                                 datos[0] = cNombre.getText();
                                                 datos[1] = fechaFormateada;
                                                                                                  
                                                 int hora = ffdt.getHourOfDay();                                                                                                                                                  
                                                 
                                                 if( ( hora >= 8 && hora < 14) ||  diaActual == 0  ){
                                                       datos[2] = hora + ": " + ((minutoff < 10) ?  "0" + minutoff : minutoff);  
                                                 }
                                                 
                                                 if( ( hora == 14 && minutoff < 30 ) || ( hora == 13 && minutoff >= 40 ) ){
                                                     datos[3] = hora + ": " + ((minutoff < 10) ?  "0" + minutoff : minutoff);  
                                                 }
                                                                                                  
                                                 if( hora >= 15 && hora < 18 ){
                                                     datos[4] = hora + ": " + ((minutoff < 10) ?  "0" + minutoff : minutoff);  
                                                 }
                                                 
                                                 if( hora >= 18 ){
                                                     datos[5] = hora + ": " + ((minutoff < 10) ?  "0" + minutoff : minutoff);  
                                                 }
                                                                                                                                                      
                                                 if( diaActual == diateff ){                                                                                                                                                                                                                   
                                                     System.out.println( "Iguales " + datos[2] + " " + datos[3] + " " + datos[4] + " " + datos[5]);
                                                     continue;                                                      
                                                 }else{
                                                       System.out.println( "Diferentes " + datos[2] + " " + datos[3] + " " + datos[4] + " " + datos[5]);
                                                       listaDatos.add(datos);
                                                       PanelPrincipal.this.modelo.addRow(datos);
                                                       datos = new Object[6];
                                                       diaActual = diateff;                                                                                                                                                          
                                                 }
                                                                                                          
                                             }
                                                                                                                                                                                                                       
                                        }
                                  
                                  }
                                  
                           }
                           
                       }
                      
              ); 
              
              gbc.gridx = 1;
              gbc.gridy = 4;
              gbc.gridheight = 2;              
              gbc.anchor = GridBagConstraints.EAST;
              gbc.insets = new Insets(5, 5, 5, 5);
              pDatos.add(bGenerarVistaPrevia,gbc);
              
              gbc = new GridBagConstraints();
              gbc.gridx = 0;
              gbc.gridy = 0;
              gbc.gridheight = 3;                            
              gbc.insets = new Insets(5, 5, 5, 5);
              add(pDatos,gbc);              
              
              pPrevisualizacion = new JPanel();              
              spt = new JScrollPane(tPrevisualizacion);              
              pPrevisualizacion.add(spt);
              
              gbc = new GridBagConstraints();
              gbc.gridx = 0;
              gbc.gridy = 3;
              gbc.gridheight = 4;                            
              gbc.insets = new Insets(5, 5, 5, 5);
              add(pPrevisualizacion);
              
              bImprimir = new JButton("Imprimir");
              
              bImprimir.addActionListener(new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                               PrinterJob job = PrinterJob.getPrinterJob();
                               PrintRequestAttributeSet aset = new HashPrintRequestAttributeSet();
                               PageFormat pf = job.pageDialog(aset);
                               job.setPrintable(PanelPrincipal.this, pf);
                               boolean ok = job.printDialog(aset);
                               
                               if (ok) {
                                     try {
                                         job.print(aset);
                                     } catch (PrinterException ex) {                                   
                               }
                           }
                        }
                     }
              );
              
              gbc = new GridBagConstraints();
              gbc.gridx = 0;
              gbc.gridy = 6;
              gbc.gridheight = 1;                            
              gbc.insets = new Insets(5, 5, 5, 5);
              add(bImprimir,gbc);
              
             
       }
       
       public ArrayList<Object> getDatosBase(String nombre){ 
             
              ArrayList<Object> alDatos = new ArrayList<Object>();
              Connection c;
              Statement s;
              ResultSet rs = null;
              
              try{ 
                  
                   Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                   
                   String base = "jdbc:odbc:Test";
                   c = DriverManager.getConnection(base);
                   s = c.createStatement();                   
                   
                   String select = "select userid from USERINFO where Name = '" + nombre + "'";
                   
                   rs = s.executeQuery(select);
                          
                   ArrayList<Integer> ids = new ArrayList<Integer>();
                   while( rs.next() ){
                          ids.add(new Integer(rs.getInt(1)));
                   }                   
                   
                   select = "";
                   
                   for(Integer id : ids){
                       
                       select = "select checktime,userid from checkinout where userid = " + id;
                       rs = s.executeQuery(select);                       
                               
                       while(rs.next()){
                       
                             Object datos = rs.getObject(1);                                                          
                             alDatos.add(datos);
                             
                       }
                       
                       rs.close();                                              
                       
                   }
                   
                   
                   s.close();
                   c.close();
                   
              }catch(Exception e){ e.printStackTrace(); }
                                            
                   
              return alDatos;
                          
       }    
       
       public int print(Graphics g, PageFormat pf,int page){
          
              if (page > 0) { return NO_SUCH_PAGE; }
 
              Graphics2D g2d = (Graphics2D)g;
              g2d.translate(pf.getImageableX(), pf.getImageableY());
                             
              int y = 150;
              int x = 400;
              
              Font fuenteCuerpo = new Font("serif", Font.PLAIN,10);
              g.setFont(fuenteCuerpo);
              
              for( int k = 0; k <= listaDatos.size() - 1; k++ ){
                  
                   Object[] datos = listaDatos.get(k);
                   
                   g.drawString(datos[0] + " ", 50, y);                                             
                   g.drawString(datos[1] + " ", 300, y);
                   
                   for( int l = 2; l <= datos.length - 1; l++ ){                                                                       
                        
                        if( datos[l] != null ){
                            g.drawString((String)datos[l] + " ", x, y);
                            x += 70;
                        }else{
                              g.drawString("      ", x, y);
                              x += 70;
                        }
                        
                   }
                   
                   y += 25;
                   x = 400;
                   
              }
              
              Font fuenteLeyenda = new Font("serif", Font.PLAIN,7);
              g.setFont(fuenteLeyenda);

              String fi = cFechaInicial.getText().trim();
              int dFi = Integer.parseInt(fi.substring(0,2));
              int mFi = Integer.parseInt(fi.substring(3,5));
              int aFi = Integer.parseInt(fi.substring(6,10));
                                                                        
              String ff = PanelPrincipal.this.cFechaFinal.getText().trim();
              int dFf = Integer.parseInt(ff.substring(0,2));
              int mFf = Integer.parseInt(ff.substring(3,5));
              int aFf = Integer.parseInt(ff.substring(6,10));
                                          
              g.drawString("Manifiesto expresamente que la jornada de labores asentada en el presente registro de asistencia es la que labore durante el período comprendido del ASENTAR PERIODO VGR. DEL " +
                            dFi + " AL " +  dFf + " DE " + meses[mFi] + " DEL " + aFi ,50,400);
              g.drawString(" y que durante dicho período NO labore tiempo extraordinario alguno,ya que me encuentro consiente de que de acuerdo a mi contrato individual " + 
                           "de trabajo en los casos en que por circunstancias especiales sea ",50,420);
              g.drawString("necesario prolongar la jornada laboral me obligo a laborar la jornada extraordinaria que sea necesaria, de conformidad con lo dispuesto " + 
                           "por el artículo 66 de la Ley Federal del Trabajo,en la inteligencia ",50,440);
              g.drawString("de que dicha jornada sólo será laborada previa autorización que en forma previa y por escrito me entregue el PATRÓN o sus representantes.",50,460);
              
              
              g.drawString((String)listaDatos.get(0)[0] + " ", 50, 520);
               
              return PAGE_EXISTS;
             
       }
       
       public static void main(String args[]){
            
              JFrame frame = new JFrame("Reporte");
              frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
              frame.setSize(610,620);
              frame.setVisible(true);
              //frame.setResizable(false);
              frame.setContentPane(new PanelPrincipal());
            
       }
       
}
