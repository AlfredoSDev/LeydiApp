package clases;

import java.io.IOException;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.Doc;
import javax.print.PrintException;
import javax.print.ServiceUI;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JOptionPane;

/**
 *
 * @author Alfredo
 */
public class Ticked {

    //Atributos que almacenan los datos de la empresa y de la compra
    private final String empresa = "        Mini-Market Leydi";
    private String articulos;
    private String total;
    private String dolar;

    /*Atributo que almacena la estructura del contenido del ticket
    *   Los campos que tengan la siguiente estructura {{nombreAtributo}}, ejemplo: {{telefono}}
    *   serán reemplazados por los datos correspondientes.
     */
    private String formatoTicket
            = "               **\n" + (char) 27 + (char) 112 + (char) 0 + (char) 10 + (char) 100 + "{{empresa}} \n"
            + "================================\n"
            + "{{articulos}}\n"
            + "================================\n"
            + "TOTAL:{{total}}\n"
            + "DOLAR:{{dolar}}\n"
            + "       GRACIAS POR SU COMPRA\n"
            + "\n\n\n\n ";

    //Definimos los metodos SET de cada atributo para asignar los datos al TICKET.
    /**
     * @param dolar dolar
     */
    public void setDolar(String dolar) {
        this.dolar = dolar;
    }
    /**
     * @param articulos datos del producto vendido
     */
    public void setArticulos(String articulos) {
        this.articulos = articulos;
    }

    /**
     * @param total total de la compra
     */
    public void setTotal(String total) {
        this.total = total;
    }

    public String getFormatoTicket() {
        return formatoTicket;
    }
    
    /*
    *PARA ESTE EJEMPLO USAMOS UNA IMPRESORA TERMICA CON EL NOMBRE DE  SUBARASI
    *EL CUAL LE ASIGNAMOS DESDE LA VENTANA DE IMPRESORA Y DISPOSITIVOS (WINDOWS)
    *Configuración del documento de impresión y reemplazo de los campos
    *con el valor de las propiedades
     */
    public void print(boolean flagServicio) throws IOException {
        //Datos de impresion.
        //Datos de la Empresa y/o negocio
        this.formatoTicket = formatoTicket.replace("{{empresa}}", this.empresa);
        //Datos de la venta
        this.formatoTicket = formatoTicket.replace("{{articulos}}", this.articulos);
        this.formatoTicket = formatoTicket.replace("{{total}}", this.total);
        this.formatoTicket = formatoTicket.replace("{{dolar}}", this.dolar);

        //Especificamos el tipo de dato a imprimir
        //Tipo: bytes -- Subtipo: autodetectado
        DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
        //Creamos un arreglo de tipo byte y le agregamos el string convertido (cuerpo del ticket)
        //a bytes tal como lo maneja la impresora.
        byte[] bytes = this.formatoTicket.getBytes();
        //Creamos un documento a imprimir pasandole el arreglo de byte
        Doc doc = new SimpleDoc(bytes, flavor, null);

        //Creamos un trabajo de impresión
        DocPrintJob job = null;
        //Creamos una bandera para determinar si se encontro la impresora
        //que especificamos en este caso "subarasi" O si usamos la impresora predeterminada del S.O.
        boolean flagJob = false;

        //Opcion 1 ->nos da el array de los servicios de impresion
        PrintService[] services = PrintServiceLookup.lookupPrintServices(null, null);
        //Opcion 2-> servicios de impresion por default
        PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
        //Opcion 3-> mostramos dialogo para seleccionar impresoras que soporten arreglos de bits
        PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
        PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);

        /*flagServicio
        *   
        *true --si queremos imprimir en una impresora especifica  en este caso "subarasi"
        *       o si deseamos imprimir en la impresora establecida como predeterminada en el Sistema Operativo
        *false --si queremos visualizar el cuadro de dialogo de impresion y elegir una impresora
        *        que soporte arreglo de bits
         */
        if (flagServicio == true) {
            if (services.length > 0) {//usamos la opcion 1 y comprobamos si hay impresoras disponibles
                //Recorremos el arreglo de impresoras
                for (PrintService service1 : services) {
                    //Aqui definimos el nombre de la impresora (para este ejemplo: subarasi)
                    // y comparamos si esta dentro del arreglo de impresoras
                    if (service1.getName().equals("subaru")) {
                        job = service1.createPrintJob(); // creamos el trabajo de impresion
                        flagJob = true; //si la impresora existe ponemos en TRUE la bandera
                    }
                }
            }
            //En caso de que la opcion 1 no encuentre la impresora que buscamos
            // el flagJob es false y job es null, entonces empleamos la opcion 2
            if (job == null && flagJob == false) {
                //creamos el trabajo de impresion con el servicio de impresion por default
                //(la impresora establecida como predeterminada en el sistema operativo)
                job = defaultService.createPrintJob();
            }
            flagJob = false;
        } else {
            //si flagServicio es false, usamos la opcion 3 para crear el trabajo de impresion
            //seleccionando la impresora desde el cuadro de dialogo de impresion
            PrintService service = ServiceUI.printDialog(null, 700, 200, printService, defaultService, flavor, pras);
            job = service.createPrintJob();
        }

        //por ultimo Imprimimos dentro de un try obligatoriamente para el contro de excepciones
        try {
            job.print(doc, null);
        } catch (PrintException ex) {
            JOptionPane.showMessageDialog(null, "IMPRIMIR TICKET (Compruebe impresion) " + ex.getMessage());
        }
    }
}
