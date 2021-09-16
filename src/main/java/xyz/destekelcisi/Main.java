package xyz.destekelcisi;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;

import java.awt.*;

public class Main {

    public static JDA jda;
    public static String prefix = ";";


    public static EmbedBuilder getEmbedBuilder() {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setFooter("TicketBot 2021 © all rights reserved");
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle("TicketBot");
        return embedBuilder;
    }
    public static EmbedBuilder getErrorMessage() {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        embedBuilder.setFooter("TicketBot 2021 © all rights reserved");
        embedBuilder.setColor(Color.RED);
        embedBuilder.setDescription("❌ Hata! Bu komutu kullanmak için yeterli yetkiniz bulunmuyor.");
        embedBuilder.setTitle("TicketBot");
        return embedBuilder;
    }

    public static void main(String[] args) {
        new Bot(jda);
    }
}
