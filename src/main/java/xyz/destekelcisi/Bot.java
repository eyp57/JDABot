package xyz.destekelcisi;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.Compression;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import xyz.destekelcisi.commands.DestekMesajGonder;
import xyz.destekelcisi.commands.HelpCMD;
import xyz.destekelcisi.listeners.MessageListener;

import javax.security.auth.login.LoginException;

public class Bot extends ListenerAdapter implements EventListener {

    public JDA jda;

    public Bot(JDA jda) {
        this.jda = jda;
        try {
            Login();
        } catch(LoginException e) {
            e.printStackTrace();
        }
    }

    public void Login() throws LoginException {

        JDABuilder builder = JDABuilder.createDefault("ODY0MTQ2MjE3MjE4OTk4Mjcy.YOxM4Q.94jsqRtKFn0v5PAEefaOdD39ERs");

        builder.disableCache(CacheFlag.MEMBER_OVERRIDES, CacheFlag.VOICE_STATE);
        builder.setBulkDeleteSplittingEnabled(false);
        builder.setCompression(Compression.NONE);
        builder.enableIntents(GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_BANS,
                GatewayIntent.GUILD_VOICE_STATES,
                GatewayIntent.DIRECT_MESSAGE_REACTIONS,
                GatewayIntent.GUILD_WEBHOOKS,
                GatewayIntent.DIRECT_MESSAGE_TYPING,
                GatewayIntent.GUILD_INVITES,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_MESSAGES);
        builder.setActivity(Activity.of(Activity.ActivityType.COMPETING, "31"));
        builder.addEventListeners(new MessageListener(jda));
        builder.addEventListeners(new HelpCMD(jda));
        builder.addEventListeners(new DestekMesajGonder(jda));
        builder.addEventListeners(this);
        builder.build();
    }

    public void onButtonClick(ButtonClickEvent e) {
        if (e.getComponentId().equals("destek-Ac")) {
            Category active = e.getGuild().getCategoryById("888032164640350208");

            Role team = e.getGuild().getRoleById("882285479985303593");

            TextChannel chnl = null;

            for(TextChannel tc : e.getGuild().getTextChannels()) {
                if(tc.getName().equals("destek-" + e.getUser().getId()) && tc.getParent() == active) {
                    chnl = tc;
                }
            }
            if(chnl != null) {
                e.reply(e.getUser().getAsMention() + ", Zaten bir destek talebiniz bulunuyor. " + chnl.getAsMention()).setEphemeral(true).queue();
            } else {
                TextChannel channel = e.getGuild().createTextChannel("destek-" + e.getUser().getId()).setParent(active).complete();
                channel.upsertPermissionOverride(team)
                        .setAllow(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE).queue();
                channel.upsertPermissionOverride(e.getMember())
                        .setAllow(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE).queue();
                channel.upsertPermissionOverride(e.getGuild().getPublicRole())
                        .setDeny(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE).queue();

                EmbedBuilder eb = Main.getEmbedBuilder();

                eb.setDescription(
                        "```'"+e.getUser().getName()+"' destek talebi```"
                        + "\n\n"
                        + "Merhaba " + e.getUser().getAsMention() + ", " + team.getAsMention() + " en kısa sürede destek talebinize yanıt verecektir."
                        + "\n"
                        + "📝 Destek talebi gereksiz açıldıysa kapatılacaktır ve yasaklanacaksınız."
                );
                channel.sendMessage(eb.build())
                        .setActionRow(
                                Button.danger("destek-Archive", "Arşiv")
                                 .withEmoji(Emoji.fromUnicode("❌"))
                        )
                        .queue();
                e.reply(e.getUser().getAsMention() + ", Destek talebiniz başarıyla açıldı. " + channel.getAsMention()).setEphemeral(true).queue();

            }
        } else if (e.getComponentId().equals("destek-Archive")) {
            TextChannel channel = e.getTextChannel();
            Category archive = e.getGuild().getCategoryById("888032185007874049");
            channel.getManager().setParent(archive);


            long userId = Long.parseLong(channel.getName().split("-")[1]);
            e.getGuild().retrieveMemberById(userId).queue(user -> {
                System.out.println(Long.valueOf(channel.getName().split("-")[1]));
                System.out.println(user.getId());
                channel.upsertPermissionOverride(user)
                        .setDeny(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE).queue();

                String newName = "arşiv-" + channel.getName().split("-")[1];
                channel.getManager().setName(newName).queue();

                EmbedBuilder eb = Main.getEmbedBuilder();

                eb.setDescription(
                        "```'"+user.getUser().getAsTag()+"' destek talebi```"
                                + "\n\n"
                                + "Destek talebi " + e.getUser().getAsMention() + " tarafından kapatıldı"
                );
                channel.sendMessage(eb.build())
                        .setActionRow(
                                Button.danger("destek-geriAc", "Tekrar aç")
                                        .withEmoji(Emoji.fromUnicode("🔃"))
                        )
                        .queue();
            });
        } else if (e.getComponentId().equals("destek-geriAc")) {
            TextChannel channel = e.getTextChannel();
            Category active = e.getGuild().getCategoryById("888032164640350208");
            channel.getManager().setParent(active);
            long userId = Long.parseLong(channel.getName().split("-")[1]);
            e.getGuild().retrieveMemberById(userId).queue(user -> {
                channel.upsertPermissionOverride(user)
                        .grant(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE).queue();

                e.getMessage().delete().queue();
                String newName = "destek-" + channel.getName().split("-")[1];
                e.getChannel().sendMessage("Destek Talebi tekrar açıldı.\nTalep sahibi: " + user.getAsMention()).queue();
                channel.getManager().setName(newName).queue();
            });
        }
    }

}
