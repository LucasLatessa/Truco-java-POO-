package vista;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

import controlador.Controlador;
import modelo.EstadoEnvido;
import modelo.EstadoTruco;
import modelo.IEnvido;
import modelo.IJugador;
import serializacion.AdministradorDeGanadores;
import serializacion.Serializador;
public class VistaConsola implements IVista {
	private static Serializador serializador=new Serializador("src/datos.dat");
	private Controlador controlador;
	private Scanner entrada;
	public VistaConsola() {
		this.entrada = new Scanner(System.in);
	}
	private boolean valido(int min ,int op,int max) {
		return op>=min&&op<=max;
	}
	public void iniciar() {
		System.out.println("########################");
		System.out.println("######### TRUCO ########");
		System.out.println("########################");
		System.out.println();
		System.out.println("1-Agregar jugador \n2-Ver ranking \n3-Salir \nIngrese su opcion");
		
		int opcion = Integer.valueOf(this.entrada.nextLine());
		while(!valido(1,opcion,2)) {
			System.out.println("Opcion invalida \nIngrese su opcion");
			opcion = Integer.valueOf(this.entrada.nextLine());
		}
		switch(opcion) {
		case 1:
			System.out.println("Ingrese nombre de jugador:");
			String nameJ1 = this.entrada.nextLine();
			this.controlador.agregarJugador(nameJ1);
			break;
		case 2:
			verRanking();
			break;
		case 3:
			System.exit(0);
			break;
		}
		}
	private void verRanking() {
		AdministradorDeGanadores lista=(AdministradorDeGanadores) serializador.readFirstObject();
		 ArrayList<String> nombres=lista.getNombresGanadores();
		 ArrayList<Integer> cant=lista.getCantGanadas();
		 System.out.println("Nombre"+"    "+"Partidas ganadas");
		for(int x=0;x<nombres.size();x++) {
			System.out.println(nombres.get(x)+"        "+cant.get(x));
		}
		System.out.println("presione enter para volver");
		entrada.nextLine();
		iniciar();}
	public void menuCantos(String opcion) {
		boolean valido=false;
		while(!valido) {
			valido=true;
			switch(opcion) {
			case "C":
				pedirCarta();
				break;
			case "M":
				controlador.alMazo();
				break;
			case "E":
				controlador.cantar(EstadoEnvido.ENVIDO);
				break;
			case "R":
				if (controlador.queTrucoPuedeCantar().equals("Re truco")) {
					controlador.cantar(EstadoTruco.RETRUCO);
				}else {
					controlador.cantar(EstadoEnvido.REALENVIDO);
				}
				break;
			case "F":
				controlador.cantar(EstadoEnvido.FALTAENVIDO);
				break;
			case "T":
				controlador.cantar(EstadoTruco.TRUCO);
				break;
			case "V":
				controlador.cantar(EstadoTruco.VALECUATRO);
				break;
			default:
				System.out.println("Opcion invalida \nIngrese opcion nuevamente");
				opcion =(this.entrada.nextLine());
				valido=false;}
			}}

	public void jugar() {
			mostrarPuntajes();
			this.mostrarCartaTirada();
			mostrarCartasEnMano();
			System.out.println("Opciones");
			System.out.println("C-Tirar carta \nM-Irme al mazo");
			String canto=controlador.queTrucoPuedeCantar();
			try {
				if (controlador.puedeCantarEnvidos()) {
					System.out.println("E-Cantar envido \nR-Cantar real envido \nF-Cantar falta envido");
				}
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if (controlador.quienCantoUltimo()!= controlador.turnoActual()) {
				switch(canto) {
				case "Truco":
					System.out.println("T-Cantar truco");
					break;
				case "Re truco":
					System.out.println("R-Cantar retruco");
					break;
				case "Vale cuatro":
					System.out.println("V-Cantar vale cuatro");
				}}
			System.out.println("Ingrese opcion");
			String opcion = entrada.nextLine().toUpperCase();
			while(!opcion.equals("C")&&!opcion.equals("M")&&!opcion.equals("E")&&!opcion.equals("R")&&!opcion.equals("F")
					&&!opcion.equals("T")&&!opcion.equals("V")) {
				System.out.println("Opcion invalida \nIngrese opcion nuevamente");
				opcion = entrada.nextLine().toUpperCase();
			}
			menuCantos(opcion);}
	
	public void quererNoQuererEnvido(String jugador,IEnvido envido) {
		mostrarCartasEnMano();
		EstadoEnvido ultCantado=envido.getEnvidoPreguntado();
		System.out.println(jugador+": el contrario canto " +ultCantado.toString());
		System.out.println("Opciones");
		System.out.println("S-Quiero");
		System.out.println("N-No quiero");
		for(String canto :envido.puedeCantar()) {
			System.out.println(canto.substring(0,1).toUpperCase()+"-Cantar "+canto);
		}
		System.out.println("Ingrese opcion");
		String opcion = entrada.nextLine().toUpperCase();
		while(!opcion.equals("N")&&!opcion.equals("S")&&!opcion.equals("E")&&!opcion.equals("R")&&!opcion.equals("F")) {
			System.out.println("Opcion invalida \nIngrese opcion nuevamente");
			opcion = entrada.nextLine().toUpperCase();
		}
		switch(opcion) {
		case "S":
			controlador.quiero();
			break;
		case "N":
			controlador.noQuiero();
			break;
		case "E":
			controlador.cantar(EstadoEnvido.ENVIDO);
			break;
		case "R":
			controlador.cantar(EstadoEnvido.REALENVIDO);
			break;
		case "F":
			controlador.cantar(EstadoEnvido.FALTAENVIDO);
			}
	}
	public void quererNoQuererTruco(String jugador,EstadoTruco truco) {
		mostrarCartasEnMano();
		System.out.println(jugador+": el contrario canto " +truco.toString());
		System.out.println("Opciones");
		System.out.println("S-Quiero");
		System.out.println("N-No quiero");
		boolean puedeCantarEnvidos = false;
		try {
			puedeCantarEnvidos = controlador.puedeCantarEnvidos();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (puedeCantarEnvidos) {
			System.out.println("E-Cantar envido \nR-Cantar real envido \nF-Cantar falta envido");
		}
		System.out.println("Ingrese opcion");
		String opcion =entrada.nextLine().toUpperCase();
		while(!(opcion.equals("S")||opcion.equals("N"))&&
				(!opcion.equals("E")&&!opcion.equals("R")&&!opcion.equals("F")&&puedeCantarEnvidos)) {
			System.out.println("Opcion invalida \nIngrese opcion nuevamente");
			opcion = (this.entrada.nextLine());
		}
		switch(opcion){
		case "S":
			controlador.quiero(truco);
			break;
		case "N":
			controlador.alMazo();
			break;
		case "E":
			controlador.cantar(EstadoEnvido.ENVIDO);
			break;
		case "R":
			controlador.cantar(EstadoEnvido.REALENVIDO);
			break;
		case "F":
			controlador.cantar(EstadoEnvido.FALTAENVIDO);
		}
	}
	
	public void pedirCarta() {
		int carta=4;
		int cantCartasJugActual=controlador.obtenerCantCartasJugActual();
		while (cantCartasJugActual<(Integer.valueOf(carta))) {
			System.out.println("Ingrese indice de carta<1-"+cantCartasJugActual+">");
			carta =Integer.valueOf(new Scanner(System.in).nextLine()) ;
		}
		controlador.tirar(carta);
	}
	
	/**
	 * muestra las cartas del jugador que le toca jugar
	 */
	public void mostrarCartasEnMano() {
		String ss="Cartas en mano: ";
		int cont=1;
		System.out.println("TURNO DE "+ controlador.nombreTurno().toUpperCase());
		for (String sCarta : controlador.listarCartas()) {
			ss+="["+(cont++)+"] "+sCarta+", ";
		}
		System.out.println(ss);
	}@Override
	/**
	 * muestra la carta tirada en caso de que haya
	 */
	public void mostrarCartaTirada() {
		try {
			if (controlador.getCartaTirada()!=null&&!controlador.rondaTerminada()){
			System.out.println("Se tiro el "+controlador.getCartaTirada()); }
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void mostrarEnvido(String nombre) {
		Integer p1= controlador.obtenerTantosEnvido().get(0);
		Integer p2=controlador.obtenerTantosEnvido().get(1);
		System.out.println("Los tantos fueron :"+p1+", "+p2);
		System.out.println( "El ganador del tanto es "+nombre+"\n");

		
	}
	@Override
	public void rondaTerminada(boolean ganador) {
		String mostrar="";
		ArrayList<String> cartas=controlador.obtenerCartasRonda();
		for(int i=0;i<cartas.size();i++) {
			mostrar+=cartas.get(i);
			mostrar+=cartas.size()-1!=i?", ":"";
			}
		System.out.println("\n\nLa ronda termino "
				+"\nCartas de la ronda:"+mostrar
				+ "\nEl ganador de la ronda "+controlador.rondaActual()+" es "+ controlador.obtenerGanadorDeRonda());

	}
	@Override
	public void avisarParda() {
		System.out.println("La ronda fue parda, la segunda ronda define");

	}
	@Override
	public void manoTerminada() {
		System.out.println("La mano termino");
		mostrarPuntajes();
	}
	@Override
	public void juegoTerminado() {
		mostrarPuntajes();
		System.out.println("El juego termino, el ganador es "+controlador.termino().getNombre());
		System.exit(0);
	}
	@Override
	public void setControlador(Controlador controlador) {
		this.controlador = controlador;
	}
	@Override
	public void mostrarPuntajes() {
		String ss="";
		ArrayList<IJugador> jugadores=controlador.darJugadores();
		for (IJugador jugador : jugadores) {
			ss+="Puntos " + jugador.getNombre()+": "+ jugador.getPuntos()+"\n";
		}
		System.out.println(ss);
	}
	@Override
	public void esperandoJugadores() {
		System.out.println("Esperando otros jugadores...");
	}
	@Override
	public void esperarJugandoOponente() {
		System.out.println("Esperando que juegue oponente...");
		
	}
	@Override
	public void serializar(String ganador) {
		if (serializador!=null) {
		AdministradorDeGanadores lista=(AdministradorDeGanadores) serializador.readFirstObject();
		lista.addGanador(ganador);
		serializador.writeOneObject(lista);
		serializador=null;}
	}
}
