package org.politecnicomalaga;

import org.politecnicomalaga.model.Ecosistema;

import java.util.Scanner;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        Ecosistema ecosistema = new Ecosistema(
                2017,
                150,
                4000,
                15,
                5,
                5,
                50,
                0.2f,
                60,
                5000
        );
        Scanner sc = new Scanner(System.in);
        int anioCalculo = sc.nextInt();
        System.out.println("Introduzca el año");
        ecosistema.setAnioCalculo(anioCalculo);//2160
        System.out.println("Depredador "+ecosistema.getAnioCalculoDepredador());
        System.out.println("Presa "+ecosistema.getAnioCalculoPresa());
    }
}