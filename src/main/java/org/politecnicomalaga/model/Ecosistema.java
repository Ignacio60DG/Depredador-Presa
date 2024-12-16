package org.politecnicomalaga.model;

public class Ecosistema {
    private float anioInicial;
    private float depredadorValorAnioInicial;
    private float presaAnioValorInicial;
    private float natalidadDepredador;
    private float mortalidadDepredador;
    private float mortalidadPresa;
    private float natalidadPresa;
    private float indiceCazaPresa;
    private float indiceAlimentacionDepredador;
    private float maximoPresaEcosistema;

    private int anioCalculo;
    private float anioCalculoDepredador;
    private float anioCalculoPresa;

    public Ecosistema(
            float anioInicial,
            float depredadorValorAnioInicial,
            float presaValorAnioInicial,
            float natalidadDepredador,
            float mortalidadDepredador,
            float mortalidadPresa,
            float natalidadPresa,
            float indiceCazaPresa,
            float indiceAlimentacionDepredador,
            float maximoPresaEcosistema
    ) {
        this.anioInicial = anioInicial;
        this.depredadorValorAnioInicial = depredadorValorAnioInicial;
        this.presaAnioValorInicial = presaValorAnioInicial;
        // Dividimos entre cien para poder manejar los porcentajes
        this.natalidadDepredador = natalidadDepredador / 100;
        this.natalidadPresa = natalidadPresa / 100;
        this.mortalidadDepredador = mortalidadDepredador / 100;
        this.mortalidadPresa = mortalidadPresa /100;
        this.indiceCazaPresa = indiceCazaPresa / 100;
        this.indiceAlimentacionDepredador = indiceAlimentacionDepredador / 100;
        this.maximoPresaEcosistema = maximoPresaEcosistema;

        // No tiene sentido, lo inicializamos a un valor para que no se queje el constructor
        this.anioCalculo = 0;
        this.anioCalculoDepredador = 0;
        this.anioCalculoPresa = 0;
    }

    public void setAnioCalculo(int nuevoAnio) {
        this.anioCalculo = nuevoAnio;
        if(nuevoAnio > this.anioInicial) {
            this.calculaDepredadorYPresaParaAnioCalculo();
        }
    }

    public float getAnioCalculoDepredador(){
        return Math.round(this.anioCalculoDepredador);
    }
    public float getAnioCalculoPresa() {
        return Math.round(this.anioCalculoPresa);
    }
    // J3
    // C3*B3*R4
    // PresaAnioAnterior * DepredadorAnioAnterior * this.presaIndiceCaza
    private float calculaPresaCazadaAnterior(float depredadorAnioAnterior, float presaAnioAnterior) {
        return presaAnioAnterior * depredadorAnioAnterior * this.indiceCazaPresa;
    }

    // K3
    // if(B3-J3*Q5) <0
    //    0
    // else
    //    B3-J3*Q5
    private float calculaDepredadorHambrientoAnterior(float depredadorAnioAnterior, float presaAnioAnterior) {
        //J3
        float presaCazadaAnterior = this.calculaPresaCazadaAnterior(depredadorAnioAnterior, presaAnioAnterior);
        float valorDepredadorHambrientoAnterior = depredadorAnioAnterior - presaCazadaAnterior * this.indiceAlimentacionDepredador;

        if(valorDepredadorHambrientoAnterior < 0) {
            valorDepredadorHambrientoAnterior = 0;
        }

        return valorDepredadorHambrientoAnterior;
    }
    // B3+L3
    // DepredadorAnioAnterior + Dif_depredadorAnioAnterior
    private float calculaDepredadorUnaFila(float depredadorAnioAnterior, float presaAnioAnterior) {
        // B3*Q2
        float depredadorNatalidadAnterior = depredadorAnioAnterior * this.natalidadDepredador;
        // B3*Q3
        float depredadorMortalidadAnterior = depredadorAnioAnterior* this.mortalidadDepredador;

        // K3
        // if(B3-J3*Q5) <0
        //    0
        // else
        //    B3-J3*Q5
        float depredadorHambrientoAnterior = this.calculaDepredadorHambrientoAnterior(depredadorAnioAnterior, presaAnioAnterior);
        // F4-G4-K4
        float diferenciaDepredadorAnterior = depredadorNatalidadAnterior - depredadorMortalidadAnterior - depredadorHambrientoAnterior;
        // B3 + L3 inicio
        return depredadorAnioAnterior + diferenciaDepredadorAnterior;
    }
    // M3
    // H3-I3-J3
    private float calculaDiferenciaPresaAnterior(float depredadorAnioAnterior, float presaAnioAnterior) {
        // C3*R2
        // PresaAnioAnterior * this.PresaMortalidad
        float presaNatalidad = presaAnioAnterior * this.natalidadPresa;
        // C3*(R3+C3/R7)
        // PresaAnioAnterior*(MortalidadPresa+PresaAnioAnterior/this.maximoPresaEcosistema)
        float presaMortalidadNatural = presaAnioAnterior*(this.mortalidadPresa+presaAnioAnterior/this.maximoPresaEcosistema);

        // C3*B3*R4
        float presaCazada = presaAnioAnterior*depredadorAnioAnterior*this.indiceCazaPresa;
        return presaNatalidad - presaMortalidadNatural - presaCazada;
    }
    // C3 + M3
    // PresaAnterior + difPresaAnterior
    private float calculaPresaUnaFila(float depredadorAnioAnterior, float presaAnioAnterior) {
        // H3-I3-J3
        // PresaNatalidad - PresaMortalidadNatural - PresaCazada
        return presaAnioAnterior + this.calculaDiferenciaPresaAnterior(depredadorAnioAnterior, presaAnioAnterior);
    }
    private void calculaDepredadorYPresaParaAnioCalculo(){
        float valorDepredador = this.depredadorValorAnioInicial;
        float valorPresa = this.presaAnioValorInicial;
        for(float anioItera = this.anioInicial; anioItera < this.anioCalculo;anioItera++){
            float valorDepredadorAnterior = valorDepredador;
            valorDepredador = this.calculaDepredadorUnaFila(valorDepredador, valorPresa);
            valorPresa = this.calculaPresaUnaFila(valorDepredadorAnterior, valorPresa);
        }
        this.anioCalculoDepredador = valorDepredador;
        this.anioCalculoPresa = valorPresa;
    }
}