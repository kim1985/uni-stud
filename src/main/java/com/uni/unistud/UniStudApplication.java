package com.uni.unistud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// progetto in cui permette all'utente di registrarsi, di visualizzare la lista delle lezioni online (paginata) e di registrarsi/cancellarsi ad una o più lezioni
// Le lezioni hanno un massimo di iscrizioni possibili, se si supera il limite non deve essere possibile per nessuno iscriversi
@SpringBootApplication
public class UniStudApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniStudApplication.class, args);
    }

}
