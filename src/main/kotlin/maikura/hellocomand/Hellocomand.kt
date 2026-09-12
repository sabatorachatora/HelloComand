package maikura.hellocomand

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.ArgumentType
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.minecraft.commands.Commands
import net.minecraft.commands.arguments.TimeArgument
import net.minecraft.commands.arguments.coordinates.Vec3Argument
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.server.commands.TeleportCommand
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.portal.TeleportTransition
import org.objectweb.asm.tree.analysis.Value


class Hellocomand : ModInitializer {
    override fun onInitialize() {

        ModItems.initialize()


        //commandMtp()
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback({ dispatcher, _, _ ->
            dispatcher.register(
                Commands.literal("mweather1")
                    .then(Commands.literal("clear_sunny").executes({ context ->
                        context.source.sendSystemMessage(Component.literal("."))
                        val server = context.source.server
                        server.commands.performPrefixedCommand(context.source, "weather clear")
                        server.commands.performPrefixedCommand(context.source, "time set 0")
                        Command.SINGLE_SUCCESS
                    }))
            )
            dispatcher.register(
                Commands.literal("mw_sunny")
                    .then(Commands.literal("").executes({ context ->
                        context.source.sendSystemMessage(Component.literal("."))
                        val server = context.source.server
                        server.commands.performPrefixedCommand(context.source, "weather clear")
                        server.commands.performPrefixedCommand(context.source, "time set 0")
                        Command.SINGLE_SUCCESS
                    }))
            )
            dispatcher.register(
                Commands.literal("mw_time")
                    .then(Commands.literal("clear_sunny").executes { context ->
                            val timed = "0"
                            println("Timed: $timed")
                            val counts: Int = timed.toInt()
                            println("Timed -> timedInt: $counts")
                            val timedInt = TimeArgument.time(counts)
                            println("timedInt = TimeArgument.time( $counts )")
                            println("counts -> timedInt: $timedInt")
                            val server = context.source.server
                            val player = context.source.playerOrException
                            player.sendSystemMessage(Component.literal("time=${timed}"))
                            //player.teleportTo(pos.x, pos.y, pos.z)
                            1
                        })
            )
            dispatcher.register(
                Commands.literal("mw_setblocks")
                    .then(
                        Commands.literal("x3" ).executes { context ->
                            val player = context.source.playerOrException
                            val playerPosX = player.blockPosition().x
                            val playerPosY = player.blockPosition().y
                            val playerPosZ = player.blockPosition().z
                            val level = player.level()
                            val block = Blocks.IRON_BLOCK.defaultBlockState()
                            var counts = 0
                            player.sendSystemMessage(Component.literal("${playerPosX}, ${playerPosY}, ${playerPosZ}, Block: iron_block."))
                            for (height in 0..2){
                                for (floor in 0..2){
                                    for (i in 1..3){
                                        level.setBlock(BlockPos(playerPosX + i,playerPosY + height,playerPosZ + floor), block, 2)
                                    }
                                }
                            }

                            1
                        }
                    )
            )
            dispatcher.register(
                Commands.literal("mw_setblocks2")
                    .then(
                        Commands.argument("pos", Vec3Argument.vec3()).executes { context ->
                            val pos = Vec3Argument.getVec3(context, "pos")
                            val player = context.source.playerOrException
                            val playerPosX = player.blockPosition().x
                            val playerPosY = player.blockPosition().y
                            val playerPosZ = player.blockPosition().z
                            val level = player.level()
                            val block = Blocks.IRON_BLOCK.defaultBlockState()
                            var posX = pos.x.toInt()
                            var posY = pos.y.toInt()
                            var posZ = pos.z.toInt()

                            for (height in 0..2){
                                for (floor in 0..2){
                                    for (i in 1..3){
                                        level.setBlock(BlockPos(playerPosX + i,playerPosY + height,playerPosZ + floor), block, 2)
                                    }
                                }
                            }

                            1
                        })
            )
        })
        )
    }

    fun commandMtp(){
        CommandRegistrationCallback.EVENT.register(CommandRegistrationCallback({ dispatcher, _, _ ->

            dispatcher.register(
                Commands.literal("hello")
                    .then(Commands.literal("asa").executes({ context ->
                        context.source.sendSystemMessage(Component.literal("Asa!"))
                        val server = context.source.server
                        server.commands.performPrefixedCommand(context.source, "time set 0")
                        Command.SINGLE_SUCCESS
                    }))
                    .then(Commands.literal("hiru").executes({ context ->
                        context.source.sendSystemMessage(Component.literal("Hiru!"))
                        Command.SINGLE_SUCCESS
                    }))
                    .then(Commands.literal("yoru").executes({ context ->
                        context.source.sendSystemMessage(Component.literal("Yoru!"))
                        Command.SINGLE_SUCCESS
                    }))

                    .executes({ context ->
                    context.source.sendSystemMessage(Component.literal("Hello World"))
                    Command.SINGLE_SUCCESS
                })
            )

            dispatcher.register(
                Commands.literal("mtp1") //通常のテレポートコマンドと同等。 もうひとつの方はおそらくベッド関連 -> 起床機能？
                    .then(
                        Commands.argument("pos", Vec3Argument.vec3()).executes { context ->
                            val pos = Vec3Argument.getVec3(context, "pos")
                            val player = context.source.playerOrException
                            player.sendSystemMessage(Component.literal("pos=${pos}"))
                            player.teleportTo(pos.x, pos.y, pos.z)
                            1
                        })
            )
            dispatcher.register(
                Commands.literal("mtp2") //スポーン地点を中心軸・縦軸として斜め右に移動？
                    .then(
                        Commands.argument("pos", Vec3Argument.vec3()).executes { context ->
                            val pos = Vec3Argument.getVec3(context, "pos")
                            val player = context.source.playerOrException
                            player.sendSystemMessage(Component.literal("pos=${pos}"))
                            player.teleportRelative(pos.x, pos.y, pos.z)
                            1
                        })
            )
            dispatcher.register(
                Commands.literal("mtp3") //機能しない、あるいは '"reset"'Position のとおり何らかの値のリセット用 -> 位置調整用？
                    .then(
                        Commands.argument("pos", Vec3Argument.vec3()).executes { context ->
                            val pos = Vec3Argument.getVec3(context, "pos")
                            val player = context.source.playerOrException
                            player.sendSystemMessage(Component.literal("pos=${pos}"))
                            player.snapTo(pos.x, pos.y, pos.z)
                            1
                        })
            )
            Commands.literal("mtp4") //通常のテレポートコマンドと同等
                .then(
                    Commands.argument("pos", Vec3Argument.vec3()).executes { context ->
                        val pos = Vec3Argument.getVec3(context, "pos")
                        val player = context.source.playerOrException
                        player.sendSystemMessage(Component.literal("pos=${pos}"))
                        player.teleportTo(pos.x, pos.y, pos.z)
                        1
                    })
        })
        )
    }



}
