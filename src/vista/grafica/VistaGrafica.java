package vista.grafica;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.ArrayList;

import controlador.Controlador;
import modelo.EstadoEnvido;
import modelo.EstadoTruco;
import modelo.IEnvido;
import modelo.IJugador;
import serializacion.AdministradorDeGanadores;
import serializacion.Serializador;
import vista.IVista;

public class VistaGrafica implements IVista {
	private static Serializador serializador=new Serializador("src/datos.dat");
	private VentanaInicioSesion vInicioSesion;
	private VentanaPrincipal vPrincipal;
	private VentanaRanking vRanking;
	private Controlador controlador;
	//private String nameJugador;//PARA PRUEBAS EL ATRIBUTO,SACAR PARA INSERTAR NOMBRES DE JUGADORES

	public VistaGrafica() {//String nameJugadorPARA PRUEBAS EL PARAMETRO,SACAR PARA INSERTAR NOMBRES DE JUGADORES
		super();
		this.vInicioSesion = new VentanaInicioSesion();
		this.vPrincipal = new VentanaPrincipal();
		this.vRanking = new VentanaRanking();
		//this.nameJugador=nameJugador;//PARA PRUEBAS

		mostrarInicioSesion();
		
		this.vInicioSesion.onClickIniciar(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				//PARA INSERTAR NOMBRES DE JUGADORES DESCOMENTAR ABAJO
				try {
					if(controlador.noNombreRepetido(vInicioSesion.getGetNombreUsuario())) {
					controlador.agregarJugador(vInicioSesion.getGetNombreUsuario());}
					else {
						vInicioSesion.nombreRepetido();
					}
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		this.vPrincipal.onClickTirarCarta(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				vPrincipal.ocultarBotonesEnvido();
				vPrincipal.ocultarNotificaciones();
				try {
					controlador.tirar(vPrincipal.getCartaSeleccionada());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

		this.vPrincipal.onClickEnvido(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				vPrincipal.ocultarBotonesEnvido();
				controlador.cantar(EstadoEnvido.ENVIDO);
				
			}
		});
		
		this.vPrincipal.onClickRealEnvido(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				vPrincipal.ocultarBotonesEnvido();
				controlador.cantar(EstadoEnvido.REALENVIDO);
				
			}
		});
		
		this.vPrincipal.onClickFaltaEnvido(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				vPrincipal.ocultarBotonesEnvido();
				controlador.cantar(EstadoEnvido.FALTAENVIDO);
				
			}
		});
		this.vPrincipal.onClickMazo(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlador.alMazo();
				vPrincipal.ocultarNotificaciones();
			}
		});
		this.vPrincipal.onClickTruco(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				vPrincipal.ocultarNotificaciones();
				controlador.cantar(EstadoTruco.TRUCO);
				
			}
		});
		this.vPrincipal.onClickReTruco(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				vPrincipal.ocultarNotificaciones();
				controlador.cantar(EstadoTruco.RETRUCO);
				
			}
		});
		this.vPrincipal.onClickValeCuatro(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				vPrincipal.ocultarNotificaciones();
				controlador.cantar(EstadoTruco.VALECUATRO);
				
			}
		});
		this.vInicioSesion.onClickVerRanking(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				AdministradorDeGanadores lista=(AdministradorDeGanadores) serializador.readFirstObject();
				vRanking.setVisible(true);
				vRanking.mostrarTabla(lista.getNombresGanadores(),lista.getCantGanadas());
			}
		});
		this.vPrincipal.onClickQuieroEnv(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlador.quiero();
				vPrincipal.ocultarBotonesEnvido();
				return;
			}
		});
		this.vPrincipal.onClickNoQuieroEnv(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlador.noQuiero();
				vPrincipal.ocultarBotonesEnvido();
				vPrincipal.ocultarNotificaciones();
				return;
			}
		});
		
		this.vPrincipal.onClickNoQuieroTru(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlador.alMazo();
				vPrincipal.ocultarBotonesTruco();
			}
		});
	}
	
	private void mostrarInicioSesion() {
		this.vInicioSesion.setVisible(true);
		this.vPrincipal.setVisible(false);
	}

	@Override
	public void setControlador(Controlador controlador) {
		this.controlador = controlador;
		
	}

	@Override
	public void iniciar() {
		//controlador.agregarJugador(nameJugador);//PARA PRUEBAS RAPIDAS
	}

	@Override
	public void mostrarPuntajes() {
		ArrayList<IJugador> jugadores=controlador.darJugadores();
		int puntos1=jugadores.get(0).getPuntos();
		int puntos2=jugadores.get(1).getPuntos();
		this.vPrincipal.actualizarPuntos(puntos1,puntos2);
	}

	@Override
	public void juegoTerminado() {
		mostrarPuntajes();
		String ganador=controlador.termino().getNombre();
		this.vPrincipal.juegoTerminado(ganador);}
	@Override
	public void avisarParda() {
		this.vPrincipal.pardaRonda();
	}

	@Override
	public void rondaTerminada(boolean ganador) {
		if (ganador) {
			this.vPrincipal.ganoRonda();
		}else {
			this.vPrincipal.perdioRonda();
		}
		
	}

	@Override
	public void manoTerminada() {
		mostrarPuntajes();
		vPrincipal.laManoTermino();
		vPrincipal.limpiarVista();
		}

	@Override
	public void mostrarEnvido(String nombre) {
		Integer p1= controlador.obtenerTantosEnvido().get(0);
		Integer p2=controlador.obtenerTantosEnvido().get(1);
		this.vPrincipal.mostrarGanadorEnvido(nombre,p1,p2);
	}

	@Override
	public void quererNoQuererEnvido(String nombreTurno, IEnvido envido) {
		EstadoEnvido ultCantado=envido.getEnvidoPreguntado();
		this.vPrincipal.notificarCanto(ultCantado.toString());
		this.vPrincipal.mostrarBotonesEnvido(envido.puedeCantar(),controlador.nombreTurno());
		mostrarCartasEnMano();
		
	}

	@Override
	public void quererNoQuererTruco(String nombreTurno, EstadoTruco truco) {
		this.vPrincipal.notificarCanto(truco.toString());
		boolean puedeCantarEnvido = false;
		try {
			puedeCantarEnvido = controlador.puedeCantarEnvidos();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		mostrarCartasEnMano();
		this.vPrincipal.mostrarBotonesTruco(puedeCantarEnvido);
		
		this.vPrincipal.onClickQuieroTru(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				controlador.quiero(truco);
				vPrincipal.ocultarBotonesTruco();
			}
		});
	}

	@Override
	public void esperandoJugadores() {
		this.vInicioSesion.esperandoOtrosJugadores();
		}
	@Override
	public void jugar() {
		this.vInicioSesion.setVisible(false);
		this.vPrincipal.setVisible(true);
		if(!vPrincipal.jugadoresCargados()) {//es porque es la primera mano
			String str1=controlador.darJugadores().get(0).getNombre();
			String str2=controlador.darJugadores().get(1).getNombre();
			this.vPrincipal.setJugadores(str1,str2);
			this.vPrincipal.botonesComienzo();
		}
		mostrarPuntajes();
		mostrarCartasEnMano();
		try {
			if (controlador.puedeCantarEnvidos()) {
				this.vPrincipal.mostrarBotonesEnvido();
			}
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String canto=controlador.queTrucoPuedeCantar();
		this.vPrincipal.turnoActual(canto,controlador.nombreTurno());
		mostrarCartaTirada();
	}
	@Override
	public void esperarJugandoOponente() {
		this.vInicioSesion.setVisible(false);
		this.vPrincipal.setVisible(true);
		if(!vPrincipal.jugadoresCargados()) {//es porque es la primera mano
			String str1=controlador.darJugadores().get(0).getNombre();
			String str2=controlador.darJugadores().get(1).getNombre();
			this.vPrincipal.setJugadores(str1,str2);
			this.vPrincipal.botonesComienzo();
		}
		;
		this.vPrincipal.esperarJugandoOponente(this.controlador.nombreTurno());
		mostrarCartaTirada();
		this.mostrarPuntajes();
	}
	@Override
	public void mostrarCartasEnMano() {
		try {
			this.vPrincipal.mostrarCartas(controlador.listarCartas());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public void mostrarCartaTirada() {
		String cartaTirada=controlador.getCartaTirada();
		if (cartaTirada!=null){
			try {
				this.vPrincipal.mostrarCartaTirada(cartaTirada);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
		
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
