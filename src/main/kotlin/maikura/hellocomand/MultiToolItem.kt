package maikura.hellocomand

import net.minecraft.world.InteractionResult
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items
import net.minecraft.world.item.context.UseOnContext

/**
 * マルチツール本体。
 *
 * 採掘性能（どのブロックを掘れるか・速度・攻撃力）は Item.Properties.tool() で
 * データコンポーネントとして設定済みなので、このクラスでは「右クリック時の挙動」だけを担当する。
 *  - 斧の挙動: 原木の樹皮剥ぎ、銅ブロックの錆・蝋落とし
 *  - シャベルの挙動: 草ブロック等を土の道に変える、焚き火を消す
 */
class MultiToolItem(properties: Properties) : Item(properties) {

    override fun useOn(context: UseOnContext): InteractionResult {
        // 斧の右クリック処理をそのまま借りる。該当しない場合は PASS が返る。
        val axeResult = Items.IRON_AXE.useOn(context)
        if (axeResult != InteractionResult.PASS) return axeResult

        // 斧で何も起きなければシャベルの右クリック処理を試す。
        return Items.IRON_SHOVEL.useOn(context)
    }
}
