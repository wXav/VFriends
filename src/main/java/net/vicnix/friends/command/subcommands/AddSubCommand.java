package net.vicnix.friends.command.subcommands;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.vicnix.friends.command.FriendAnnotationCommand;
import net.vicnix.friends.command.FriendSubCommand;
import net.vicnix.friends.session.Session;
import net.vicnix.friends.session.SessionException;
import net.vicnix.friends.session.SessionManager;
import net.vicnix.friends.translation.Translation;

@FriendAnnotationCommand(
        name = "add",
        syntax = "/amigos add <jugador>",
        description = "Enviar solicitud de amigos a un jugador"
)
public class AddSubCommand extends FriendSubCommand {

    @Override
    public void execute(Session session, String[] args) {
        try {
            Session target = SessionManager.getInstance().getOfflineSession(args[0]);

            /*if (target.getUniqueId().equals(session.getUniqueId())) {
                session.sendMessage(new ComponentBuilder("No puedes enviarte una solicitud de amistad a ti mismo.").color(ChatColor.RED).create()[0]);

                return;
            }*/

            if (target.alreadyRequested(session.getUniqueId())) {
                session.sendMessage(new TextComponent(Translation.getInstance().translateString("ALREADY_SENT_FRIEND_REQUEST", session.getName())));

                return;
            }

            target.addRequest(session);

            target.intentSave();

            target.sendMessage(Translation.getInstance().translateString("FRIEND_REQUEST_RECEIVE", session.getName()));

            session.sendMessage(new TextComponent(Translation.getInstance().translateString("FRIEND_REQUEST_SENT", target.getName())));
        } catch (SessionException e) {
            session.sendMessage(new ComponentBuilder(e.getMessage()).color(ChatColor.RED).create()[0]);
        }
    }
}