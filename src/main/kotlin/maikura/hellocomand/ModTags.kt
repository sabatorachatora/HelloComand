package maikura.hellocomand

import net.minecraft.core.registries.Registries
import net.minecraft.resources.Identifier
import net.minecraft.tags.TagKey
import net.minecraft.world.level.block.Block

/**
 * この Mod が定義するタグ。
 * 実体（どのブロックが含まれるか）は data/hellocommand/tags/ 以下の JSON で定義する。
 */
object ModTags {
    object Blocks {
        /** マルチツールで採掘できるブロック（ツルハシ・斧・シャベルの和集合） */
        val MINEABLE_WITH_MULTI_TOOL: TagKey<Block> = create("mineable/multi_tool")

        private fun create(name: String): TagKey<Block> =
            TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(ModItems.MOD_ID, name))
    }
}
