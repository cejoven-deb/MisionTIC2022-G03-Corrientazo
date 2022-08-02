package co.edu.utp.misiontic.cesardiaz.view;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import co.edu.utp.misiontic.cesardiaz.model.Bandeja;
import co.edu.utp.misiontic.cesardiaz.model.Completo;
import co.edu.utp.misiontic.cesardiaz.model.EstadoPedido;
import co.edu.utp.misiontic.cesardiaz.model.Mesa;
import co.edu.utp.misiontic.cesardiaz.model.OpcionCarne;
import co.edu.utp.misiontic.cesardiaz.model.OpcionEnsalada;
import co.edu.utp.misiontic.cesardiaz.model.OpcionJugo;
import co.edu.utp.misiontic.cesardiaz.model.OpcionPedido;
import co.edu.utp.misiontic.cesardiaz.model.OpcionPrincipio;
import co.edu.utp.misiontic.cesardiaz.model.OpcionSopa;
import co.edu.utp.misiontic.cesardiaz.model.Pedido;

public class PedidoView {

    private Scanner scanner;

    public PedidoView() {
        this.scanner = new Scanner(System.in);
    }

    public Mesa seleccionarMesa(List<Mesa> mesas) {
        return pedirOpcion(mesas, "Mesas");
    }

    public Pedido pedirInformacionPedido(List<OpcionSopa> sopas, List<OpcionPrincipio> principios,
            List<OpcionCarne> carnes, List<OpcionEnsalada> ensaladas, List<OpcionJugo> jugos) {
        // Pedir informacion del cliente
        System.out.print("Ingrese el nombre (descripcion) del cliente: ");
        var cliente = scanner.nextLine();

        // Pedir opcion de pedido (completo o bandeja)
        var opcion = pedirOpcionPedido();

        if (opcion instanceof Completo) {
            // Pedir Sopa
            ((Completo) opcion).setSopa(pedirOpcion(sopas, "Sopas"));
        }

        // Pedir Principio
        opcion.setPrincipio(pedirOpcion(principios, "Principios"));
        // Pedir Carne
        opcion.setCarne(pedirOpcion(carnes, "Carnes"));
        // Pedir Ensalada (si la desea)
        opcion.setEnsalada(pedirOpcion(ensaladas, "Ensaladas", true));
        // Pedir Jugo
        opcion.setJugo(pedirOpcion(jugos, "Jugos"));

        return new Pedido(cliente, opcion);
    }

    private <T> T pedirOpcion(List<T> opciones, String nombre) {
        return pedirOpcion(opciones, nombre, false);
    }

    private <T> T pedirOpcion(List<T> opciones, String nombre, Boolean opcional) {
        while (true) {
            // Listo las opciones existentes
            System.out.println(nombre + " existentes:");
            for (int i = 0; i < opciones.size(); i++) {
                System.out.printf("%d -> %s %n", (i + 1), opciones.get(i));
            }
            if (opcional) {
                System.out.printf("%d -> %s %n", 0, "Ninguno");
            }

            // Selecciono la mesa
            System.out.print("Cual es su elección: ");
            try {
                var opcion = scanner.nextInt();
                scanner.nextLine();
                if (opcional && opcion == 0) {
                    return null;
                } else if (opcion >= 1 && opcion <= opciones.size()) {
                    return opciones.get(opcion - 1);
                } else {
                    System.err.println("Opcion inválida. Intente de nuevo.");
                }
            } catch (InputMismatchException e) {
                System.err.println("Opcion inválida. Intente de nuevo.");
                scanner.nextLine();
            }
        }
    }

    private OpcionPedido pedirOpcionPedido() {
        while (true) {
            System.out.println("Opciones de pedido:\nC -> Almuerzo Completo\nB -> Bandeja");
            System.out.print("C / B: ");
            var opcion = scanner.nextLine();
            switch (opcion.toUpperCase()) {
                case "C":
                    return new Completo(12_000);
                case "B":
                    return new Bandeja(10_000);
                default:
                    System.err.println("Opcion inválida. Intenta de nuevo.");
            }
        }
    }

    public void mostrarEstadoMesa(Mesa mesa) {
        System.out.println(mesa);
        System.out.println("Pedidos:");
        mesa.getPedidos()
                .forEach(System.out::println);
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    public void mostrarError(String error) {
        System.out.println(error);
    }

    public Pedido seleccionarPedidoEntrega(Mesa mesa) {
        var pedidos = mesa.getPedidos().stream()
                .filter(p -> p.getEstado() == EstadoPedido.PENDIENTE_ENTREGAR)
                .collect(Collectors.toList());

        return pedirOpcion(pedidos, "Pedido");
    }

    public Integer leerEfectivo() {
        Integer respuesta = null;

        while (respuesta == null) {
            try {
                System.out.print("Ingrese el valor de efectivo recibido: ");
                respuesta = scanner.nextInt();
            } catch (InputMismatchException ex) {
                System.err.println("Valor inválidi. Intente de nuevo.");
            } finally {
                scanner.nextLine();
            }
        }

        return respuesta;
    }

}
