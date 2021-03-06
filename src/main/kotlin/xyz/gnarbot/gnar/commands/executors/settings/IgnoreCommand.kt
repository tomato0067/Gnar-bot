package xyz.gnarbot.gnar.commands.executors.settings

import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.IMentionable
import net.dv8tion.jda.core.entities.Member
import net.dv8tion.jda.core.entities.Role
import net.dv8tion.jda.core.entities.TextChannel
import xyz.gnarbot.gnar.commands.Category
import xyz.gnarbot.gnar.commands.Command
import xyz.gnarbot.gnar.commands.template.CommandTemplate
import xyz.gnarbot.gnar.commands.template.annotations.Description
import xyz.gnarbot.gnar.utils.Context

@Command(
        id = 53,
        aliases = arrayOf("ignore"),
        usage = "(user|channel|role|list) [?entity]",
        description = "Make the bot ignore certain users, channels or roles.",
        category = Category.SETTINGS,
        permissions = arrayOf(Permission.MANAGE_SERVER)
)
class IgnoreCommand : CommandTemplate() {
    @Description("Ignore/unignore users.")
    fun user(context: Context, member: Member) {
        if (!context.member.canInteract(member)) {
            context.send().error("You can not interact with this user.").queue()
            return
        }

        context.data.ignored.users.let {
            if (it.contains(member.user.id)) {
                it.remove(member.user.id)

                context.send().embed("Ignore") {
                    desc {
                        "No longer ignoring user ${member.asMention}."
                    }
                }.action().queue()
            } else {
                it.add(member.user.id)

                context.send().embed("Ignore") {
                    desc {
                        "Ignored user ${member.asMention}."
                    }
                }.action().queue()
            }
        }
        context.data.save()
    }

    @Description("Ignore/unignore a channel.")
    fun channel(context: Context, channel: TextChannel) {
        context.data.ignored.channels.let {
            if (it.contains(channel.id)) {
                it.remove(channel.id)

                context.send().embed("Ignore") {
                    desc {
                        "No longer ignoring channel ${channel.asMention}."
                    }
                }.action().queue()
            } else {
                it.add(channel.id)

                context.send().embed("Ignore") {
                    desc {
                        "Ignored channel ${channel.asMention}."
                    }
                }.action().queue()
            }
        }
        context.data.save()
    }

    @Description("Ignore/unignore a role.")
    fun role(context: Context, role: Role) {
        if (role == context.guild.publicRole) {
            context.send().error("You can't ignore the public role!").queue()
            return
        }

        context.data.ignored.roles.let {
            if (it.contains(role.id)) {
                it.remove(role.id)

                context.send().embed("Ignore") {
                    desc {
                        "No longer ignoring role ${role.asMention}."
                    }
                }.action().queue()
            } else {
                it.add(role.id)

                context.send().embed("Ignore") {
                    desc {
                        "Ignored role ${role.asMention}."
                    }
                }.action().queue()
            }
        }
        context.data.save()
    }

    @Description("List ignored entities.")
    fun list(context: Context) {
        context.send().embed("Ignored Entities") {
            field("Users") {
                buildString {
                    context.data.ignored.users.let {
                        if (it.isEmpty()) {
                            append("None of the users are ignored.")
                        }

                        it.mapNotNull(context.guild::getMemberById)
                                .map(IMentionable::getAsMention)
                                .forEach { append("• ").append(it).append('\n') }
                    }
                }
            }
            field("Channel") {
                buildString {
                    context.data.ignored.channels.let {
                        if (it.isEmpty()) {
                            append("None of the channels are ignored.")
                        }

                        it.mapNotNull(context.guild::getTextChannelById)
                                .map(IMentionable::getAsMention)
                                .forEach { append("• ").append(it).append('\n') }
                    }
                }
            }
            field("Roles") {
                buildString {
                    context.data.ignored.roles.let {
                        if (it.isEmpty()) {
                            append("None of the roles are ignored.")
                        }

                        it.mapNotNull(context.guild::getRoleById)
                                .map(IMentionable::getAsMention)
                                .forEach { append("• ").append(it).append('\n') }
                    }
                }
            }
        }.action().queue()
    }
}