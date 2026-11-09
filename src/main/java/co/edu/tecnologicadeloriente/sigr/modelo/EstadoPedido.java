package co.edu.tecnologicadeloriente.sigr.modelo;

/** Ciclo de vida de un pedido. Solo se avanza en este orden. */
public enum EstadoPedido {
    RECIBIDO, EN_PREPARACION, SERVIDO, PAGADO;

    public boolean puedePasarA(EstadoPedido siguiente) {
        return siguiente.ordinal() == this.ordinal() + 1;
    }
}
