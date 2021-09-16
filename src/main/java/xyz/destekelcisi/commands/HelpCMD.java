package xyz.destekelcisi.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.interactions.components.ButtonStyle;
import net.dv8tion.jda.api.utils.data.DataObject;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.destekelcisi.Main;

import java.util.Arrays;

public class HelpCMD extends ListenerAdapter {

    public JDA jda;

    public HelpCMD(JDA jda) {
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

            if(e.getMessage().getContentRaw().toLowerCase().equals(Main.prefix + "yardım")) {
                EmbedBuilder eb = Main.getEmbedBuilder();
                eb.setDescription(
                        "```Komutlar```"
                                + "\n\n"
                                + "`" + Main.prefix + "yardım` **=>** *Bu sayfayı gösterir*"
                                + "\n"
                                + "`" + Main.prefix + "destekmesajgonder` **=>** *Destek açma mesajı  gönderir*"
                                + "\n\n"
                                + "```Bağlantılar```"
                                + "\n\n"
                                + "[Davet Et](https://destekelcisi.xyz/invite) - [Discord](https://destekelcisi.xyz/discord) - [Oy ver](https://destekelcisi.xyz/discord)"
                );
                e.getMessage().reply(eb.build()).setActionRow(
                        Button.link("https://destekelcisi.xyz/invite", "Davet et"),
                        Button.link("https://destekelcisi.xyz/discord", "Oy ver"),
                        Button.link("https://destekelcisi.xyz/discord", "Discord")
                ).queue();
            }

        } else {
            return;
        }

    }
}
