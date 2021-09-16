package xyz.destekelcisi.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Category;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import xyz.destekelcisi.Main;

import java.util.Arrays;

public class DestekMesajGonder extends ListenerAdapter {
    public JDA jda;

    public DestekMesajGonder(JDA jda) {
        this.jda = jda;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent e) {

        if(e.getMessage().getContentRaw().startsWith(Main.prefix) || !e.getMessage().getAuthor().isBot()) {


            String[] args = e.getMessage().getContentRaw().split(" ");

            Arrays.stream(args).map(arg -> {
                System.out.println(arg);
                return null;
            });

            if(e.getMessage().getContentRaw().toLowerCase().equals(Main.prefix + "destekmesajgonder")) {
                if(e.getGuild().getOwnerId().equals(e.getAuthor().getId())) {
                    EmbedBuilder eb = Main.getEmbedBuilder();
                    eb.setDescription(
                            "```Destek talebi aç```"
                                    + "\n\n"
                                    + "🎫 Destek talebi açmak için `📖` butonuna tıklayınız."
                                    + "\n"
                                    + "📝 *Gereksiz açılan tüm destekler kapatılıp, açılan kişiye ceza uygulanacaktır.*"
                    );
                    e.getMessage().getChannel().sendMessage(eb.build()).setActionRow(
                            Button.primary("destek-Ac", "Destek talebi aç")
                                    .withEmoji(Emoji.fromUnicode("📖"))
                    ).queue();
                } else {
                    e.getMessage().reply(Main.getErrorMessage().build()).queue();
                }
            }

        } else {
            return;
        }
    }

}
